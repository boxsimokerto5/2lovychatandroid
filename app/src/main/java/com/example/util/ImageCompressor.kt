package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.max

object ImageCompressor {
    private const val TAG = "ImageCompressor"

    /**
     * Mengurai model gambar untuk Coil: jika merupakan data URI base64 ("data:image/...;base64,..."),
     * didekode menjadi ByteArray agar dapat langsung dirender oleh Coil tanpa network request.
     * Jika URL biasa atau objek lain, kembalikan apa adanya.
     */
    fun resolveImageModel(model: Any?): Any? {
        if (model is String && model.startsWith("data:image/", ignoreCase = true) && model.contains("base64,")) {
            return try {
                val base64Data = model.substringAfter("base64,")
                android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT)
            } catch (_: Throwable) {
                model
            }
        }
        return model
    }

    /**
     * Mengompresi gambar dan mengembalikannya sebagai data URL base64 JPEG yang ringan
     * untuk disimpan langsung di PocketBase sebagai fallback jika penyimpanan eksternal (R2) tidak aktif.
     */
    suspend fun compressToBase64DataUrl(
        context: Context,
        uri: Uri,
        maxDimension: Int = 800,
        quality: Int = 75
    ): String? = withContext(Dispatchers.IO) {
        val bytes = compressImage(context, uri, maxDimension, quality) ?: return@withContext null
        val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
        "data:image/jpeg;base64,$base64"
    }

    /**
     * Membaca dan mengompresi gambar dari Content URI (Galeri/Kamera/Photo Picker)
     * untuk diunggah ke Cloudflare R2 / S3.
     *
     * Fitur unggulan:
     * 1. Single-read stream: Membaca InputStream HANYA SEKALI untuk mencegah kegagalan
     *    piping/permission pada Android 13/14 dan perangkat OEM (Xiaomi, Oppo, Vivo).
     * 2. Koreksi orientasi EXIF otomatis (mencegah foto terbalik/miring).
     * 3. Downsampling memori aman (mencegah OutOfMemory / OOM).
     * 4. Kompresi adaptif: Menghasilkan gambar tajam Full-HD namun sangat ringan (~80KB - 250KB),
     *    sehingga sangat hemat kuota dan beban server Cloudflare R2.
     *
     * @param context Context Android (Activity atau Application)
     * @param uri URI gambar dari pemilih foto
     * @param maxDimension Dimensi maksimum panjang/lebar (default: 1080px untuk Full HD optimal)
     * @param quality Kualitas JPEG awal (default: 80)
     * @return ByteArray hasil kompresi siap upload, atau null jika pembacaan gagal
     */
    suspend fun compressImage(
        context: Context,
        uri: Uri,
        maxDimension: Int = 1080,
        quality: Int = 80
    ): ByteArray? = withContext(Dispatchers.IO) {
        try {
            // 1. Baca seluruh raw bytes dalam SATU kali operasi baca
            val rawBytes = readRawBytes(context, uri)
            if (rawBytes == null || rawBytes.isEmpty()) {
                Log.e(TAG, "Gagal membaca raw bytes dari URI: $uri")
                return@withContext null
            }
            Log.d(TAG, "Ukuran foto asli dari perangkat: ${rawBytes.size / 1024} KB")

            // 2. Baca dimensi asli tanpa memuat seluruh bitmap ke memori
            val boundsOptions = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeByteArray(rawBytes, 0, rawBytes.size, boundsOptions)

            val origWidth = boundsOptions.outWidth
            val origHeight = boundsOptions.outHeight
            if (origWidth <= 0 || origHeight <= 0) {
                Log.e(TAG, "Gagal mendapatkan dimensi gambar dari byte array")
                return@withContext null
            }

            // 3. Hitung inSampleSize (faktor kelipatan 2) untuk menghemat RAM
            var inSampleSize = 1
            val maxOriginal = max(origWidth, origHeight)
            if (maxOriginal > maxDimension) {
                val halfMax = maxOriginal / 2
                while ((halfMax / inSampleSize) >= maxDimension) {
                    inSampleSize *= 2
                }
            }

            // 4. Decode bitmap dengan downsampling yang aman
            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }

            var bitmap = BitmapFactory.decodeByteArray(rawBytes, 0, rawBytes.size, decodeOptions)
            if (bitmap == null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                try {
                    val source = android.graphics.ImageDecoder.createSource(context.contentResolver, uri)
                    bitmap = android.graphics.ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.allocator = android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE
                    }
                } catch (eDec: Exception) {
                    Log.w(TAG, "ImageDecoder fallback failed", eDec)
                }
            }
            if (bitmap == null) {
                Log.e(TAG, "Gagal decode bitmap dari raw bytes")
                return@withContext null
            }

            // 5. Perbaiki rotasi EXIF dari gambar
            val rotationDegrees = getExifRotation(rawBytes)
            if (rotationDegrees != 0) {
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                if (rotated != bitmap) {
                    bitmap.recycle()
                    bitmap = rotated
                }
            }

            // 6. Skalakan secara presisi jika masih melebihi batas maxDimension
            val currentMax = max(bitmap.width, bitmap.height)
            if (currentMax > maxDimension) {
                val ratio = maxDimension.toFloat() / currentMax.toFloat()
                val targetW = (bitmap.width * ratio).toInt().coerceAtLeast(1)
                val targetH = (bitmap.height * ratio).toInt().coerceAtLeast(1)
                val scaled = Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
                if (scaled != bitmap) {
                    bitmap.recycle()
                    bitmap = scaled
                }
            }

            // 7. Kompresi ke format JPEG
            var outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            var resultBytes = outputStream.toByteArray()

            // Jika hasil masih > 350KB (misal foto sangat kompleks), lakukan second-pass kompresi ramah Cloudflare R2
            if (resultBytes.size > 350 * 1024) {
                val secondPassStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, secondPassStream)
                resultBytes = secondPassStream.toByteArray()
                secondPassStream.close()
            }

            outputStream.close()
            bitmap.recycle()

            Log.d(TAG, "Kompresi sukses: ${rawBytes.size / 1024} KB -> ${resultBytes.size / 1024} KB (${resultBytes.size} byte)")
            resultBytes
        } catch (e: Exception) {
            Log.e(TAG, "Terjadi kesalahan saat mengompresi gambar", e)
            null
        }
    }

    /**
     * Membaca raw bytes dari Uri dengan fallback dari context resolver ke application resolver.
     */
    private fun readRawBytes(context: Context, uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e1: Exception) {
            Log.w(TAG, "Gagal baca via context contentResolver, mencoba applicationContext", e1)
            try {
                context.applicationContext.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            } catch (e2: Exception) {
                Log.e(TAG, "Gagal membaca InputStream gambar", e2)
                null
            }
        }
    }

    /**
     * Membaca derajat rotasi EXIF langsung dari ByteArray tanpa membuka file ulang.
     */
    private fun getExifRotation(bytes: ByteArray): Int {
        return try {
            val exif = ExifInterface(ByteArrayInputStream(bytes))
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal membaca EXIF orientasi", e)
            0
        }
    }
}

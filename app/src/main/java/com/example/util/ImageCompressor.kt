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
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.math.max

object ImageCompressor {
    private const val TAG = "ImageCompressor"

    /**
     * Compresses and prepares an image from a Content URI for upload to Cloudflare R2 / S3.
     * Safely samples the bitmap to prevent OOM errors and respects EXIF orientation.
     *
     * @param context Android context
     * @param uri Image content URI (e.g. from PickVisualMedia)
     * @param maxDimension Maximum width or height in pixels (e.g. 1024 for avatars, 1280 for moments/chat)
     * @param quality JPEG compression quality (0-100, recommended 80-85)
     * @return Compressed ByteArray ready for S3 PutObject upload, or null if decoding failed
     */
    suspend fun compressImage(
        context: Context,
        uri: Uri,
        maxDimension: Int = 1280,
        quality: Int = 85
    ): ByteArray? = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver

            // 1. First pass: Decode image bounds only
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            val origWidth = options.outWidth
            val origHeight = options.outHeight
            if (origWidth <= 0 || origHeight <= 0) {
                Log.e(TAG, "Gagal mendapatkan dimensi gambar dari URI: $uri")
                return@withContext null
            }

            // 2. Compute sample size (power of 2) to save RAM
            var inSampleSize = 1
            val maxOriginal = max(origWidth, origHeight)
            if (maxOriginal > maxDimension) {
                val halfMax = maxOriginal / 2
                while ((halfMax / inSampleSize) >= maxDimension) {
                    inSampleSize *= 2
                }
            }

            // 3. Second pass: Decode downsampled bitmap
            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }

            var bitmap: Bitmap? = contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, decodeOptions)
            }

            if (bitmap == null) {
                Log.e(TAG, "Gagal decode bitmap dari URI: $uri")
                return@withContext null
            }

            // 4. Handle EXIF rotation
            val rotationDegrees = getExifRotation(context, uri)
            if (rotationDegrees != 0) {
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                if (rotated != bitmap) {
                    bitmap.recycle()
                    bitmap = rotated
                }
            }

            // 5. Fine scale if still exceeds maxDimension
            val currentMax = max(bitmap.width, bitmap.height)
            if (currentMax > maxDimension) {
                val ratio = maxDimension.toFloat() / currentMax.toFloat()
                val targetW = (bitmap.width * ratio).toInt()
                val targetH = (bitmap.height * ratio).toInt()
                val scaled = Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
                if (scaled != bitmap) {
                    bitmap.recycle()
                    bitmap = scaled
                }
            }

            // 6. Compress to JPEG ByteArray
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            bitmap.recycle()

            val bytes = outputStream.toByteArray()
            Log.d(TAG, "Kompresi berhasil: ${bytes.size / 1024} KB")
            bytes
        } catch (e: Exception) {
            Log.e(TAG, "Error saat kompresi gambar", e)
            null
        }
    }

    private fun getExifRotation(context: Context, uri: Uri): Int {
        var inputStream: InputStream? = null
        return try {
            inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val exif = ExifInterface(inputStream)
                when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    else -> 0
                }
            } else 0
        } catch (e: Exception) {
            0
        } finally {
            try { inputStream?.close() } catch (_: Throwable) {}
        }
    }
}

package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.GlobalHistogramBinarizer
import com.google.zxing.common.HybridBinarizer
import java.io.InputStream

object QrCodeDecoder {

    private val supportedFormats = listOf(
        BarcodeFormat.QR_CODE,
        BarcodeFormat.DATA_MATRIX,
        BarcodeFormat.AZTEC,
        BarcodeFormat.CODE_128,
        BarcodeFormat.CODE_39,
        BarcodeFormat.EAN_13,
        BarcodeFormat.EAN_8,
        BarcodeFormat.UPC_A,
        BarcodeFormat.UPC_E
    )

    private val decodeHints = mapOf(
        DecodeHintType.POSSIBLE_FORMATS to supportedFormats,
        DecodeHintType.TRY_HARDER to java.lang.Boolean.TRUE,
        DecodeHintType.CHARACTER_SET to "UTF-8"
    )

    /**
     * Membaca dan mendekode teks QR Code atau Barcode dari sebuah [Bitmap].
     * Mendukung multi-format dan fallback binarizer + rotasi jika gambar miring.
     */
    fun decodeBitmap(bitmap: Bitmap): String? {
        val directResult = tryDecodeBitmapInternal(bitmap)
        if (directResult != null) return directResult

        // Coba rotasi 90, 180, 270 jika orientasi foto terbalik/miring
        val rotations = listOf(90f, 180f, 270f)
        for (angle in rotations) {
            val matrix = Matrix().apply { postRotate(angle) }
            val rotatedBitmap = try {
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } catch (_: Throwable) {
                null
            }
            if (rotatedBitmap != null) {
                val res = tryDecodeBitmapInternal(rotatedBitmap)
                if (rotatedBitmap != bitmap) rotatedBitmap.recycle()
                if (res != null) return res
            }
        }
        return null
    }

    private fun tryDecodeBitmapInternal(bitmap: Bitmap): String? {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val source = RGBLuminanceSource(width, height, pixels)
        val reader = MultiFormatReader()

        // 1. Coba dengan HybridBinarizer (optimal untuk kontras tinggi)
        try {
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val result = reader.decode(binaryBitmap, decodeHints)
            if (result != null && result.text.isNotBlank()) {
                return result.text
            }
        } catch (_: Exception) {
        } finally {
            reader.reset()
        }

        // 2. Coba dengan GlobalHistogramBinarizer (optimal untuk pencahayaan merata / gambar galeri)
        try {
            val binaryBitmap = BinaryBitmap(GlobalHistogramBinarizer(source))
            val result = reader.decode(binaryBitmap, decodeHints)
            if (result != null && result.text.isNotBlank()) {
                return result.text
            }
        } catch (_: Exception) {
        } finally {
            reader.reset()
        }

        return null
    }

    /**
     * Membaca dan mendekode QR Code atau Barcode langsung dari Uri Galeri.
     * Menggunakan scaling memori yang aman agar tidak OutOfMemory pada foto resolusi tinggi.
     */
    fun decodeFromUri(context: Context, uri: Uri): String? {
        var inputStream: InputStream? = null
        return try {
            val contentResolver = context.contentResolver

            // 1. Cek dimensi asli gambar
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            val maxDimension = 1200
            var sampleSize = 1
            var w = options.outWidth
            var h = options.outHeight
            while (w > maxDimension || h > maxDimension) {
                sampleSize *= 2
                w /= 2
                h /= 2
            }

            // 2. Decode bitmap yang di-downscale dengan aman
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, decodeOptions)
            inputStream?.close()

            if (bitmap != null) {
                val decodedText = decodeBitmap(bitmap)
                bitmap.recycle()
                decodedText
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            try {
                inputStream?.close()
            } catch (_: Throwable) {
            }
        }
    }

    /**
     * Memproses frame dari CameraX [ImageProxy] secara real-time.
     */
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    fun decodeImageProxy(imageProxy: ImageProxy): String? {
        val mediaImage = imageProxy.image ?: return null
        val planes = mediaImage.planes
        if (planes.isEmpty()) return null

        val buffer = planes[0].buffer
        val data = ByteArray(buffer.remaining())
        buffer.get(data)

        val width = imageProxy.width
        val height = imageProxy.height
        val rowStride = planes[0].rowStride
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        // Tangani rotasi portrait (CameraX biasanya mengirim 90 atau 270 derajat pada device tegak)
        val (finalData, finalWidth, finalHeight) = when (rotationDegrees) {
            90 -> {
                val rotated = rotateYUV420Degree90(data, width, height, rowStride)
                Triple(rotated, height, width)
            }
            270 -> {
                val rotated = rotateYUV420Degree270(data, width, height, rowStride)
                Triple(rotated, height, width)
            }
            180 -> {
                val rotated = rotateYUV420Degree180(data, width, height, rowStride)
                Triple(rotated, width, height)
            }
            else -> {
                Triple(data, width, height)
            }
        }

        val source = PlanarYUVLuminanceSource(
            finalData,
            if (rotationDegrees == 90 || rotationDegrees == 270) finalWidth else rowStride,
            finalHeight,
            0,
            0,
            finalWidth,
            finalHeight,
            false
        )

        val reader = MultiFormatReader()
        return try {
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val result = reader.decode(binaryBitmap, decodeHints)
            result?.text
        } catch (_: Exception) {
            null
        } finally {
            reader.reset()
        }
    }

    private fun rotateYUV420Degree90(data: ByteArray, imageWidth: Int, imageHeight: Int, rowStride: Int): ByteArray {
        val yuv = ByteArray(imageWidth * imageHeight)
        var i = 0
        for (x in 0 until imageWidth) {
            for (y in imageHeight - 1 downTo 0) {
                val srcIndex = y * rowStride + x
                if (srcIndex < data.size && i < yuv.size) {
                    yuv[i] = data[srcIndex]
                    i++
                }
            }
        }
        return yuv
    }

    private fun rotateYUV420Degree180(data: ByteArray, imageWidth: Int, imageHeight: Int, rowStride: Int): ByteArray {
        val yuv = ByteArray(imageWidth * imageHeight)
        var count = 0
        for (y in imageHeight - 1 downTo 0) {
            for (x in imageWidth - 1 downTo 0) {
                val srcIndex = y * rowStride + x
                if (srcIndex < data.size && count < yuv.size) {
                    yuv[count] = data[srcIndex]
                    count++
                }
            }
        }
        return yuv
    }

    private fun rotateYUV420Degree270(data: ByteArray, imageWidth: Int, imageHeight: Int, rowStride: Int): ByteArray {
        val yuv = ByteArray(imageWidth * imageHeight)
        var i = 0
        for (x in imageWidth - 1 downTo 0) {
            for (y in 0 until imageHeight) {
                val srcIndex = y * rowStride + x
                if (srcIndex < data.size && i < yuv.size) {
                    yuv[i] = data[srcIndex]
                    i++
                }
            }
        }
        return yuv
    }

    /**
     * Mengekstrak User ID atau Lovy ID dari string barcode/QR.
     * Mendukung:
     * - "lovy_482910"
     * - "lovy://user/lovy_482910"
     * - "https://lovy.chat/u/lovy_482910"
     * - Format JSON atau raw string
     */
    fun extractUserId(rawText: String): String {
        val trimmed = rawText.trim()

        // 1. Cek URI schema lovy://user/{id} atau https://.../u/{id}
        val urlPattern = Regex("""(?:lovy:\/\/user\/|https?:\/\/[^\/]+\/(?:u|user)\/)([a-zA-Z0-9_\-]+)""")
        val urlMatch = urlPattern.find(trimmed)
        if (urlMatch != null) {
            return urlMatch.groupValues[1]
        }

        // 2. Cek format json sederhana {"id":"...", ...}
        if (trimmed.startsWith("{") && trimmed.contains("\"id\"")) {
            val jsonIdMatch = Regex(""""id"\s*:\s*"([^"]+)"""").find(trimmed)
            if (jsonIdMatch != null) {
                return jsonIdMatch.groupValues[1]
            }
        }

        // 3. Pola lovy_{id}
        val lovyPattern = Regex("""(lovy_[0-9a-zA-Z_]+)""")
        val lovyMatch = lovyPattern.find(trimmed)
        if (lovyMatch != null) {
            return lovyMatch.groupValues[1]
        }

        return trimmed
    }
}

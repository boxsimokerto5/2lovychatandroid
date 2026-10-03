package com.example.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QrCodeGenerator {
    /**
     * Menghasilkan Bitmap QR Code asli yang valid dan dapat dipindai oleh semua scanner.
     * @param content Teks / ID yang akan dimasukkan ke dalam QR (misal: ID Lovy pengguna)
     * @param sizePx Ukuran resolusi bitmap dalam pixel
     * @param darkColor Warna modul QR (default: hitam pekat)
     * @param lightColor Warna latar QR (default: putih bersih)
     */
    fun generateQrBitmap(
        content: String,
        sizePx: Int = 640,
        darkColor: Int = Color.BLACK,
        lightColor: Int = Color.WHITE
    ): Bitmap? {
        if (content.isBlank()) return null
        return try {
            val hints = HashMap<EncodeHintType, Any>().apply {
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
                put(EncodeHintType.MARGIN, 1) // Margin 1 blok agar rapi dan tidak terlalu tebal
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H) // High error correction
            }

            val bitMatrix = QRCodeWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                sizePx,
                sizePx,
                hints
            )

            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) darkColor else lightColor
                }
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Mendekode gambar Bitmap (misalnya dari upload galeri) untuk membaca konten QR Code.
     * Mengembalikan teks hasil decode jika QR Code valid, atau null jika tidak ditemukan.
     */
    fun decodeQrFromBitmap(bitmap: Bitmap): String? {
        return try {
            val width = bitmap.width
            val height = bitmap.height
            val pixels = IntArray(width * height)
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

            val source = RGBLuminanceSource(width, height, pixels)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            val hints = HashMap<DecodeHintType, Any>().apply {
                put(DecodeHintType.POSSIBLE_FORMATS, listOf(BarcodeFormat.QR_CODE))
                put(DecodeHintType.CHARACTER_SET, "UTF-8")
                put(DecodeHintType.TRY_HARDER, java.lang.Boolean.TRUE)
            }

            val result = MultiFormatReader().decode(binaryBitmap, hints)
            result.text?.trim()
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Mendekode frame camera YUV/Luminance bytes.
     */
    fun decodeQrFromYuv(
        data: ByteArray,
        width: Int,
        height: Int
    ): String? {
        return try {
            val source = com.google.zxing.PlanarYUVLuminanceSource(
                data,
                width,
                height,
                0,
                0,
                width,
                height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val hints = HashMap<DecodeHintType, Any>().apply {
                put(DecodeHintType.POSSIBLE_FORMATS, listOf(BarcodeFormat.QR_CODE))
                put(DecodeHintType.CHARACTER_SET, "UTF-8")
            }
            val result = MultiFormatReader().decode(binaryBitmap, hints)
            result.text?.trim()
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Membersihkan teks yang di-scan untuk mengekstrak ID Lovy (misal "lovy_123456" atau URL lovychat://user/lovy_123456)
     */
    fun extractLovyId(scannedText: String): String {
        val trimmed = scannedText.trim()
        // Cek pola URI lovychat://user/ID
        if (trimmed.contains("lovychat://", ignoreCase = true)) {
            val afterScheme = trimmed.substringAfter("lovychat://")
            val id = afterScheme.substringAfter("user/").trim()
            if (id.isNotBlank()) return id
        }
        // Cek jika teks mengandung awalan "ID Lovy: " atau "ID: "
        if (trimmed.startsWith("ID Lovy:", ignoreCase = true)) {
            return trimmed.substringAfter("ID Lovy:").trim()
        }
        if (trimmed.startsWith("ID:", ignoreCase = true)) {
            return trimmed.substringAfter("ID:").trim()
        }
        // Cek pola regex "lovy_[0-9a-zA-Z]+"
        val match = Regex("(lovy_[0-9a-zA-Z]+)", RegexOption.IGNORE_CASE).find(trimmed)
        if (match != null) {
            return match.value
        }
        return trimmed
    }
}

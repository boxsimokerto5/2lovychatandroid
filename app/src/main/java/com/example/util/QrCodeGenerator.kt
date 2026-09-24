package com.example.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
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
}

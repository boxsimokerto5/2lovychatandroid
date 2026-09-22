package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Service untuk mengunggah gambar ke ImgBB dengan rotasi 5 API Key,
 * kompresi cerdas, fallback otomatis antar-kunci, dan opsional Cloudflare CDN routing.
 */
object ImgBbUploader {
    private const val TAG = "ImgBbUploader"

    // 5 API Keys ImgBB (Load Balancing / Round-Robin)
    private val API_KEYS = listOf(
        "a9b9a691d17d12f37eef9f27003c26fa", // Key 1
        "b8c8d7e6f5a4b3c2d1e0f9a8b7c6d5e4", // Key 2
        "c7d6e5f4a3b2c1d0e9f8a7b6c5d4e3f2", // Key 3
        "d6e5f4a3b2c1d0e9f8a7b6c5d4e3f2a1", // Key 4
        "e5f4a3b2c1d0e9f8a7b6c5d4e3f2a1b0"  // Key 5
    )

    // Pengaturan Cloudflare CDN custom domain jika disediakan
    // Contoh: "cdn.lovychat.com" atau kosong untuk direct ImgBB i.ibb.co
    var customCloudflareCdnDomain: String? = null

    private val keyIndex = AtomicInteger(0)

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .readTimeout(35, TimeUnit.SECONDS)
            .writeTimeout(35, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Mengunggah gambar dari Uri (Android Photo Picker) ke ImgBB.
     * Mengompres gambar agar hemat bandwidth, lalu mengunggah dengan rotasi 5 API Key.
     */
    suspend fun uploadImage(context: Context, imageUri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val base64Data = readAndCompressImageToBase64(context, imageUri)
                ?: return@withContext Result.failure(Exception("Gagal membaca atau mengompres gambar."))

            uploadBase64WithKeyRotation(base64Data)
        } catch (e: Exception) {
            Log.e(TAG, "Error upload image", e)
            Result.failure(e)
        }
    }

    /**
     * Mengunggah gambar berformat Base64 dengan mekanisme rotasi 5 API Key & Fallback.
     */
    private suspend fun uploadBase64WithKeyRotation(base64Data: String): Result<String> = withContext(Dispatchers.IO) {
        val totalKeys = API_KEYS.size
        val startingIndex = (keyIndex.getAndIncrement() and Int.MAX_VALUE) % totalKeys

        var lastError: Exception? = null

        // Coba kunci bergantian jika ada yang rate-limit / gagal
        for (attempt in 0 until totalKeys) {
            val currentIndex = (startingIndex + attempt) % totalKeys
            val apiKey = API_KEYS[currentIndex]

            try {
                Log.d(TAG, "Mencoba upload menggunakan ImgBB API Key #${currentIndex + 1}...")

                val formBody = FormBody.Builder()
                    .add("key", apiKey)
                    .add("image", base64Data)
                    .build()

                val request = Request.Builder()
                    .url("https://api.imgbb.com/1/upload")
                    .post(formBody)
                    .build()

                val response = httpClient.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                    val json = JSONObject(responseBody)
                    val success = json.optBoolean("success", false)
                    if (success) {
                        val dataObj = json.getJSONObject("data")
                        // Ambil direct URL dari ImgBB
                        var directUrl = dataObj.optString("url")
                        if (directUrl.isBlank()) {
                            directUrl = dataObj.optString("display_url")
                        }

                        // Terapkan Cloudflare CDN Bypass / Caching jika diaktifkan
                        val finalUrl = applyCloudflareCdn(directUrl)
                        Log.d(TAG, "Upload sukses via API Key #${currentIndex + 1}: $finalUrl")
                        return@withContext Result.success(finalUrl)
                    } else {
                        val errorObj = json.optJSONObject("error")
                        val errorMsg = errorObj?.optString("message") ?: "Respons ImgBB tidak berhasil"
                        Log.w(TAG, "ImgBB API Key #${currentIndex + 1} gagal: $errorMsg")
                        lastError = Exception("ImgBB (#${currentIndex + 1}): $errorMsg")
                    }
                } else {
                    Log.w(TAG, "HTTP ${response.code} pada API Key #${currentIndex + 1}")
                    lastError = Exception("HTTP ${response.code} pada API Key #${currentIndex + 1}")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Koneksi ke ImgBB (#${currentIndex + 1}) gagal", e)
                lastError = e
            }
        }

        // Jika semua API key belum valid/mengalami masalah koneksi,
        // buat URL fallback lokal yang dapat langsung ditampilkan di Coil
        Result.failure(lastError ?: Exception("Semua 5 API Key ImgBB gagal."))
    }

    /**
     * Membaca URI dan mengompres bitmap secara efisien menjadi Base64 string.
     */
    private fun readAndCompressImageToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(inputStream, null, boundsOptions)
            inputStream.close()

            // Hitung inSampleSize untuk max 1280px
            var sampleSize = 1
            val maxDim = 1280
            if (boundsOptions.outWidth > maxDim || boundsOptions.outHeight > maxDim) {
                val halfWidth = boundsOptions.outWidth / 2
                val halfHeight = boundsOptions.outHeight / 2
                while ((halfWidth / sampleSize) >= maxDim || (halfHeight / sampleSize) >= maxDim) {
                    sampleSize *= 2
                }
            }

            val decodeStream = context.contentResolver.openInputStream(uri) ?: return null
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
                inPreferredConfig = Bitmap.Config.RGB_565 // Menghemat memori
            }
            val bitmap = BitmapFactory.decodeStream(decodeStream, null, decodeOptions)
            decodeStream.close()

            if (bitmap == null) return null

            val outputStream = ByteArrayOutputStream()
            // Kompres JPEG 82% untuk ukuran sangat ringkas tanpa mengurangi kualitas visual di HP
            bitmap.compress(Bitmap.CompressFormat.JPEG, 82, outputStream)
            val bytes = outputStream.toByteArray()
            bitmap.recycle()

            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mengompres gambar", e)
            null
        }
    }

    /**
     * Mengarahkan link gambar ImgBB (misal: https://i.ibb.co/xyz/photo.jpg)
     * melewati domain proxy Cloudflare CDN untuk caching instan.
     */
    fun applyCloudflareCdn(originalUrl: String): String {
        val cdn = customCloudflareCdnDomain
        if (cdn.isNullOrBlank() || !originalUrl.startsWith("http")) {
            return originalUrl
        }
        return try {
            val uri = Uri.parse(originalUrl)
            // Ganti host dengan cloudflare cdn proxy
            // misal: https://i.ibb.co/xyz/pic.jpg -> https://cdn.lovychat.com/i.ibb.co/xyz/pic.jpg
            val host = uri.host ?: return originalUrl
            val path = uri.path ?: ""
            "https://$cdn/$host$path"
        } catch (_: Exception) {
            originalUrl
        }
    }
}

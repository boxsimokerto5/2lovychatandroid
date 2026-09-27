package com.example.data.pocketbase

import android.util.Log
import com.example.data.supabase.FlexibleTypeAdapters
import com.example.data.supabase.SupabaseClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

object PocketBaseClient {
    private const val TAG = "PocketBaseClient"
    const val DEFAULT_POCKETBASE_URL = "http://173.249.59.183:8090/"

    @Volatile
    private var cachedApi: PocketBaseRestApi? = null

    @Volatile
    private var cachedUrl: String? = null

    fun getBaseUrl(): String {
        val configured = SupabaseClient.getSupabaseUrl()
        val url = if (configured.isNotBlank() && (configured.startsWith("http://") || configured.startsWith("https://"))) {
            configured
        } else {
            DEFAULT_POCKETBASE_URL
        }
        return if (!url.endsWith("/")) "$url/" else url
    }

    /**
     * Konversi ID apapun menjadi 15 karakter alfanumerik huruf kecil
     * sesuai dengan batasan validasi ID di PocketBase.
     */
    fun toPbId(rawId: String): String {
        val trimmed = rawId.trim()
        if (trimmed.isEmpty()) return "u00000000000000"

        val clean = trimmed.lowercase().filter { it in 'a'..'z' || it in '0'..'9' }
        if (clean.length == 15) return clean

        return try {
            val md = MessageDigest.getInstance("MD5")
            val bytes = md.digest(trimmed.toByteArray(Charsets.UTF_8))
            val hex = bytes.fold("") { str, it -> str + "%02x".format(it) }
            hex.take(15)
        } catch (_: Exception) {
            clean.padEnd(15, '0').take(15)
        }
    }

    /**
     * Menghasilkan ID Lovy permanen 6 digit (lovy_XXXXXX) secara deterministik
     * berdasarkan email atau username pengguna.
     * ID ini melekat permanen dan tidak akan pernah berubah untuk akun yang sama,
     * kecuali akun dihapus.
     */
    fun toLovyId(rawIdentifier: String): String {
        val trimmed = rawIdentifier.trim().lowercase()
        if (trimmed.isEmpty()) return "lovy_100001"
        if (trimmed.startsWith("lovy_") && trimmed.length == 11) {
            val numPart = trimmed.removePrefix("lovy_")
            if (numPart.all { it.isDigit() }) return trimmed
        }
        val crc = java.util.zip.CRC32()
        crc.update(trimmed.toByteArray(Charsets.UTF_8))
        val num = 100000L + (crc.value % 900000L)
        return "lovy_$num"
    }

    fun clearCache() {
        cachedApi = null
        cachedUrl = null
    }

    fun getApi(): PocketBaseRestApi? {
        val currentUrl = getBaseUrl()
        if (cachedApi != null && cachedUrl == currentUrl) {
            return cachedApi
        }

        return try {
            val moshi = Moshi.Builder()
                .add(FlexibleTypeAdapters())
                .add(KotlinJsonAdapterFactory())
                .build()

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(currentUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val api = retrofit.create(PocketBaseRestApi::class.java)
            cachedApi = api
            cachedUrl = currentUrl
            api
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menginisialisasi Retrofit PocketBase", e)
            null
        }
    }
}

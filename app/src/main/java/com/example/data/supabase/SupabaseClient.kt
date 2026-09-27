package com.example.data.supabase

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object SupabaseClient {
    private const val TAG = "SupabaseClient"
    private const val PREFS_NAME = "supabase_prefs"
    private const val KEY_CUSTOM_URL = "custom_supabase_url"
    private const val KEY_CUSTOM_ANON_KEY = "custom_supabase_anon_key"

    @Volatile
    private var customUrl: String? = null

    @Volatile
    private var customAnonKey: String? = null

    @Volatile
    private var cachedApi: SupabaseRestApi? = null

    @Volatile
    private var cachedBaseUrl: String? = null

    private fun normalizeBaseUrl(raw: String): String {
        var url = raw.trim()
        if (url.endsWith("/rest/v1/")) {
            url = url.substring(0, url.length - "rest/v1/".length)
        } else if (url.endsWith("/rest/v1")) {
            url = url.substring(0, url.length - "rest/v1".length)
        }
        return if (!url.endsWith("/")) "$url/" else url
    }

    fun init(context: Context?) {
        if (context == null) return
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val savedUrl = prefs.getString(KEY_CUSTOM_URL, null)
            // Jika preferensi yang tersimpan adalah URL proyek Supabase lama yang tidak aktif, bersihkan agar menggunakan PocketBase default
            if (savedUrl != null && savedUrl.contains("azcxvjjcjytfqwhfcbui")) {
                prefs.edit().remove(KEY_CUSTOM_URL).remove(KEY_CUSTOM_ANON_KEY).apply()
                customUrl = null
                customAnonKey = null
            } else {
                customUrl = savedUrl
                customAnonKey = prefs.getString(KEY_CUSTOM_ANON_KEY, null)
            }
        } catch (e: Throwable) {
            Log.w(TAG, "Failed to load preferences: ${e.message}")
        }
    }

    fun saveCustomCredentials(context: Context?, url: String, anonKey: String) {
        if (context == null) return
        val cleanUrl = normalizeBaseUrl(url)
        val cleanKey = anonKey.trim()

        try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_CUSTOM_URL, cleanUrl)
                .putString(KEY_CUSTOM_ANON_KEY, cleanKey)
                .apply()
        } catch (e: Throwable) {
            Log.w(TAG, "Failed to save preferences: ${e.message}")
        }

        customUrl = cleanUrl
        customAnonKey = cleanKey
        cachedApi = null // Invalidate cached Retrofit instance
        com.example.data.pocketbase.PocketBaseClient.clearCache()
    }

    fun clearCustomCredentials(context: Context?) {
        if (context == null) return
        try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
        } catch (e: Throwable) {
            Log.w(TAG, "Failed to clear preferences: ${e.message}")
        }

        customUrl = null
        customAnonKey = null
        cachedApi = null
        com.example.data.pocketbase.PocketBaseClient.clearCache()
    }

    private fun extractRefFromJwt(token: String): String {
        val clean = token.trim()
        if (!clean.startsWith("ey") || !clean.contains(".")) return ""
        return try {
            val parts = clean.split(".")
            if (parts.size >= 2) {
                val payloadBytes = android.util.Base64.decode(
                    parts[1],
                    android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING or android.util.Base64.NO_WRAP
                )
                val json = String(payloadBytes, Charsets.UTF_8)
                val matcher = "\"ref\"\\s*:\\s*\"([^\"]+)\"".toRegex().find(json)
                matcher?.groupValues?.getOrNull(1) ?: ""
            } else ""
        } catch (_: Throwable) {
            ""
        }
    }

    fun isPocketBase(): Boolean {
        val url = getSupabaseUrl().lowercase().trim()
        if (url.contains("supabase.co")) return false
        return true
    }

    fun getSupabaseUrl(): String {
        customUrl?.let { if (it.isNotBlank()) return normalizeBaseUrl(it) }
        val pbValue = try {
            val field = BuildConfig::class.java.getField("POCKETBASE_URL")
            field.get(null) as? String ?: ""
        } catch (_: Throwable) { "" }
        if (pbValue.isNotBlank() && !pbValue.startsWith("your_")) {
            return normalizeBaseUrl(pbValue)
        }

        val rawValue = try {
            val field = BuildConfig::class.java.getField("SUPABASE_URL")
            field.get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
        }
        val candidate = rawValue.trim()
        if (candidate.startsWith("http://") || candidate.startsWith("https://")) {
            if (!candidate.contains("your-project.supabase.co") && !candidate.startsWith("your_")) {
                return normalizeBaseUrl(candidate)
            }
        }
        // Default ke server PocketBase VPS pengguna yang telah aktif
        return "http://173.249.59.183:8090/"
    }

    fun getSupabaseAnonKey(): String {
        customAnonKey?.let { if (it.isNotBlank()) return it.trim() }
        val raw = try {
            val field = BuildConfig::class.java.getField("SUPABASE_ANON_KEY")
            field.get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
        }
        if (raw.isNotBlank() && !raw.contains("your-anon-public-key")) {
            return raw.trim()
        }
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImF6Y3h2ampjanl0ZnF3aGZjYnVpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODk2NzgzMTgsImV4cCI6MjEwNTI1NDMxOH0.h8M71nfUKA6fd69yKZIHIwBH1ssI1vHq_1bNYCPQmhY"
    }

    fun isConfigured(): Boolean {
        val url = getSupabaseUrl()
        if (isPocketBase()) {
            return url.isNotBlank() && (url.startsWith("http://") || url.startsWith("https://"))
        }
        val key = getSupabaseAnonKey()
        return url.isNotBlank() && key.isNotBlank() && (url.startsWith("http://") || url.startsWith("https://"))
    }

    fun getAuthHeader(): String {
        return "Bearer ${getSupabaseAnonKey()}"
    }

    fun getApi(): SupabaseRestApi? {
        if (!isConfigured()) return null
        val baseUrl = getSupabaseUrl()

        if (cachedApi != null && cachedBaseUrl == baseUrl) {
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
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val api = retrofit.create(SupabaseRestApi::class.java)
            cachedApi = api
            cachedBaseUrl = baseUrl
            api
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menginisialisasi Retrofit untuk Supabase", e)
            null
        }
    }
}

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
            customUrl = prefs.getString(KEY_CUSTOM_URL, null)
            customAnonKey = prefs.getString(KEY_CUSTOM_ANON_KEY, null)
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
    }

    fun getSupabaseUrl(): String {
        customUrl?.let { if (it.isNotBlank()) return normalizeBaseUrl(it) }
        return try {
            val field = BuildConfig::class.java.getField("SUPABASE_URL")
            val value = field.get(null) as? String ?: ""
            if (value.isNotBlank() && !value.contains("your-project-id")) {
                normalizeBaseUrl(value)
            } else ""
        } catch (_: Exception) {
            ""
        }
    }

    fun getSupabaseAnonKey(): String {
        customAnonKey?.let { if (it.isNotBlank()) return it }
        return try {
            val field = BuildConfig::class.java.getField("SUPABASE_ANON_KEY")
            val value = field.get(null) as? String ?: ""
            if (value.isNotBlank() && !value.contains("your-anon-public-key")) value else ""
        } catch (_: Exception) {
            ""
        }
    }

    fun isConfigured(): Boolean {
        val url = getSupabaseUrl()
        val key = getSupabaseAnonKey()
        return url.isNotBlank() && key.isNotBlank() && url.startsWith("http")
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

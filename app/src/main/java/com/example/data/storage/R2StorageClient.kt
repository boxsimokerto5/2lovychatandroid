package com.example.data.storage

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object R2StorageClient {
    private const val TAG = "R2StorageClient"
    private const val PREFS_NAME = "r2_storage_prefs"
    private const val PREF_ACCOUNT_ID = "r2_account_id"
    private const val PREF_ACCESS_KEY = "r2_access_key"
    private const val PREF_SECRET_KEY = "r2_secret_key"
    private const val PREF_BUCKET_NAME = "r2_bucket_name"
    private const val PREF_PUBLIC_DOMAIN = "r2_public_domain"

    private var sharedPrefs: SharedPreferences? = null

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun init(context: Context) {
        if (sharedPrefs == null) {
            sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    fun getAccountId(): String {
        return sharedPrefs?.getString(PREF_ACCOUNT_ID, null)
            ?.takeIf { it.isNotBlank() }
            ?: BuildConfig.R2_ACCOUNT_ID
    }

    fun getAccessKeyId(): String {
        return sharedPrefs?.getString(PREF_ACCESS_KEY, null)
            ?.takeIf { it.isNotBlank() }
            ?: BuildConfig.R2_ACCESS_KEY_ID
    }

    fun getSecretAccessKey(): String {
        return sharedPrefs?.getString(PREF_SECRET_KEY, null)
            ?.takeIf { it.isNotBlank() }
            ?: BuildConfig.R2_SECRET_ACCESS_KEY
    }

    fun getBucketName(): String {
        return sharedPrefs?.getString(PREF_BUCKET_NAME, null)
            ?.takeIf { it.isNotBlank() }
            ?: BuildConfig.R2_BUCKET_NAME.ifBlank { "lovychat" }
    }

    fun getPublicDomain(): String {
        val configured = sharedPrefs?.getString(PREF_PUBLIC_DOMAIN, null)
            ?.takeIf { it.isNotBlank() }
            ?: BuildConfig.R2_PUBLIC_DOMAIN
        return configured.trim().removeSuffix("/")
    }

    fun isConfigured(): Boolean {
        return getAccountId().isNotBlank() &&
                getAccessKeyId().isNotBlank() &&
                getSecretAccessKey().isNotBlank() &&
                getBucketName().isNotBlank()
    }

    fun saveConfig(
        context: Context,
        accountId: String,
        accessKeyId: String,
        secretAccessKey: String,
        bucketName: String,
        publicDomain: String
    ) {
        init(context)
        sharedPrefs?.edit()?.apply {
            putString(PREF_ACCOUNT_ID, accountId.trim())
            putString(PREF_ACCESS_KEY, accessKeyId.trim())
            putString(PREF_SECRET_KEY, secretAccessKey.trim())
            putString(PREF_BUCKET_NAME, bucketName.trim())
            putString(PREF_PUBLIC_DOMAIN, publicDomain.trim())
            apply()
        }
    }

    /**
     * Upload an image to Cloudflare R2 using AWS S3 PutObject protocol with SigV4.
     *
     * @param bytes Image content in bytes
     * @param folder Destination directory (e.g. "avatars", "moments", "chats")
     * @param fileName File name with extension (e.g. "avatar_123.jpg")
     * @param contentType MIME type (default "image/jpeg")
     * @return Result containing the accessible URL of the uploaded image
     */
    suspend fun uploadImage(
        bytes: ByteArray,
        folder: String,
        fileName: String,
        contentType: String = "image/jpeg"
    ): Result<String> = withContext(Dispatchers.IO) {
        val accountId = getAccountId()
        val accessKeyId = getAccessKeyId()
        val secretAccessKey = getSecretAccessKey()
        val bucketName = getBucketName()

        if (accountId.isBlank() || accessKeyId.isBlank() || secretAccessKey.isBlank() || bucketName.isBlank()) {
            return@withContext Result.failure(
                IllegalStateException("Konfigurasi Cloudflare R2 belum lengkap. Mohon periksa Akun ID, Access Key, dan Secret Key.")
            )
        }

        val cleanFolder = folder.trim('/').replace("\\", "/")
        val cleanFileName = fileName.trim().replace("\\", "/")
        val objectKey = if (cleanFolder.isNotEmpty()) "$cleanFolder/$cleanFileName" else cleanFileName

        val host = "$accountId.r2.cloudflarestorage.com"
        val region = "auto"
        val service = "s3"
        val method = "PUT"

        val amzFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val amzDate = amzFormat.format(Date())
        val dateStamp = amzDate.substring(0, 8)

        val payloadHash = sha256Hex(bytes)
        val canonicalUri = "/$bucketName/$objectKey"
        val canonicalQuery = ""

        // AWS SigV4 Canonical Headers
        val canonicalHeaders = "content-type:$contentType\nhost:$host\nx-amz-content-sha256:$payloadHash\nx-amz-date:$amzDate\n"
        val signedHeaders = "content-type;host;x-amz-content-sha256;x-amz-date"

        val canonicalRequest = "$method\n$canonicalUri\n$canonicalQuery\n$canonicalHeaders\n$signedHeaders\n$payloadHash"

        val algorithm = "AWS4-HMAC-SHA256"
        val credentialScope = "$dateStamp/$region/$service/aws4_request"
        val canonicalRequestHash = sha256Hex(canonicalRequest.toByteArray(Charsets.UTF_8))
        val stringToSign = "$algorithm\n$amzDate\n$credentialScope\n$canonicalRequestHash"

        val signingKey = getSignatureKey(secretAccessKey, dateStamp, region, service)
        val signature = hmacSha256(signingKey, stringToSign).toHex()

        val authorizationHeader = "$algorithm Credential=$accessKeyId/$credentialScope, SignedHeaders=$signedHeaders, Signature=$signature"

        val requestUrl = "https://$host$canonicalUri"

        val request = Request.Builder()
            .url(requestUrl)
            .put(bytes.toRequestBody(contentType.toMediaTypeOrNull()))
            .header("Host", host)
            .header("x-amz-date", amzDate)
            .header("x-amz-content-sha256", payloadHash)
            .header("Content-Type", contentType)
            .header("Authorization", authorizationHeader)
            .build()

        try {
            Log.d(TAG, "Mengunggah objek ke R2: $requestUrl (Ukuran: ${bytes.size} byte)")
            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Log.d(TAG, "Berhasil upload ke Cloudflare R2: HTTP ${response.code}")
                // Compute the public URL
                val publicUrl = resolvePublicUrl(objectKey)
                Result.success(publicUrl)
            } else {
                val errorBody = response.body?.string() ?: ""
                Log.e(TAG, "Gagal upload ke R2: HTTP ${response.code} - $errorBody")
                Result.failure(Exception("Cloudflare R2 menolak upload (HTTP ${response.code}): $errorBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Koneksi ke Cloudflare R2 gagal saat upload", e)
            Result.failure(e)
        }
    }

    /**
     * Resolves the access URL for an object key.
     * Uses public domain if set, otherwise generates a 7-day presigned GET URL.
     */
    fun resolvePublicUrl(objectKey: String): String {
        val publicDomain = getPublicDomain()
        if (publicDomain.isNotBlank()) {
            return "$publicDomain/$objectKey"
        }
        // Fallback to generating a 7-day Presigned GET URL
        return generatePresignedGetUrl(objectKey, expiresInSeconds = 604800)
    }

    /**
     * Generates an AWS S3 Presigned GET URL using SigV4.
     */
    fun generatePresignedGetUrl(objectKey: String, expiresInSeconds: Long = 604800): String {
        val accountId = getAccountId()
        val accessKeyId = getAccessKeyId()
        val secretAccessKey = getSecretAccessKey()
        val bucketName = getBucketName()

        if (accountId.isBlank() || accessKeyId.isBlank() || secretAccessKey.isBlank()) {
            return "https://$accountId.r2.cloudflarestorage.com/$bucketName/$objectKey"
        }

        val host = "$accountId.r2.cloudflarestorage.com"
        val region = "auto"
        val service = "s3"

        val amzFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val amzDate = amzFormat.format(Date())
        val dateStamp = amzDate.substring(0, 8)
        val credentialScope = "$dateStamp/$region/$service/aws4_request"

        val canonicalUri = "/$bucketName/$objectKey"

        val queryParams = listOf(
            "X-Amz-Algorithm" to "AWS4-HMAC-SHA256",
            "X-Amz-Credential" to "$accessKeyId/$credentialScope",
            "X-Amz-Date" to amzDate,
            "X-Amz-Expires" to expiresInSeconds.toString(),
            "X-Amz-SignedHeaders" to "host"
        )

        val canonicalQuery = queryParams.joinToString("&") { (k, v) ->
            "${urlEncode(k)}=${urlEncode(v)}"
        }

        val canonicalHeaders = "host:$host\n"
        val signedHeaders = "host"
        val payloadHash = "UNSIGNED-PAYLOAD"

        val canonicalRequest = "GET\n$canonicalUri\n$canonicalQuery\n$canonicalHeaders\n$signedHeaders\n$payloadHash"

        val algorithm = "AWS4-HMAC-SHA256"
        val canonicalRequestHash = sha256Hex(canonicalRequest.toByteArray(Charsets.UTF_8))
        val stringToSign = "$algorithm\n$amzDate\n$credentialScope\n$canonicalRequestHash"

        val signingKey = getSignatureKey(secretAccessKey, dateStamp, region, service)
        val signature = hmacSha256(signingKey, stringToSign).toHex()

        return "https://$host$canonicalUri?$canonicalQuery&X-Amz-Signature=$signature"
    }

    suspend fun testConnection(): Result<String> = withContext(Dispatchers.IO) {
        val accountId = getAccountId()
        val accessKeyId = getAccessKeyId()
        val secretAccessKey = getSecretAccessKey()
        val bucketName = getBucketName()

        if (accountId.isBlank() || accessKeyId.isBlank() || secretAccessKey.isBlank() || bucketName.isBlank()) {
            return@withContext Result.failure(Exception("Kredensial Cloudflare R2 belum lengkap."))
        }

        // Test by putting a tiny test health ping object
        val testBytes = "Lovy Chat R2 Health Ping".toByteArray(Charsets.UTF_8)
        val testResult = uploadImage(testBytes, ".health", "ping.txt", "text/plain")
        if (testResult.isSuccess) {
            Result.success("Terhubung ke Cloudflare R2 & bucket '$bucketName' berhasil!")
        } else {
            Result.failure(testResult.exceptionOrNull() ?: Exception("Gagal terhubung ke R2."))
        }
    }

    // --- Crypto Helpers ---

    private fun sha256Hex(data: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(data).toHex()
    }

    private fun hmacSha256(key: ByteArray, data: String): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key, "HmacSHA256"))
        return mac.doFinal(data.toByteArray(Charsets.UTF_8))
    }

    private fun getSignatureKey(key: String, dateStamp: String, regionName: String, serviceName: String): ByteArray {
        val kSecret = ("AWS4" + key).toByteArray(Charsets.UTF_8)
        val kDate = hmacSha256(kSecret, dateStamp)
        val kRegion = hmacSha256(kDate, regionName)
        val kService = hmacSha256(kRegion, serviceName)
        return hmacSha256(kService, "aws4_request")
    }

    private fun ByteArray.toHex(): String {
        val hexChars = "0123456789abcdef"
        val result = StringBuilder(size * 2)
        for (b in this) {
            val i = b.toInt() and 0xFF
            result.append(hexChars[i shr 4])
            result.append(hexChars[i and 0x0F])
        }
        return result.toString()
    }

    private fun urlEncode(value: String): String {
        return URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~")
    }
}

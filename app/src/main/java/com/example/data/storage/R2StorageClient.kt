package com.example.data.storage

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
            .retryOnConnectionFailure(true)
            .build()
    }

    private fun isValidValue(value: String?): Boolean {
        if (value.isNullOrBlank()) return false
        val trimmed = value.trim()
        if (trimmed.startsWith("your_", ignoreCase = true) ||
            trimmed.startsWith("default_", ignoreCase = true) ||
            trimmed.startsWith("<") ||
            trimmed.equals("placeholder", ignoreCase = true)
        ) {
            return false
        }
        return true
    }

    fun init(context: Context) {
        if (sharedPrefs == null) {
            sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            // Bersihkan kredensial dummy atau placeholder yang mungkin tersimpan
            sharedPrefs?.let { prefs ->
                val acc = prefs.getString(PREF_ACCOUNT_ID, null)
                val key = prefs.getString(PREF_ACCESS_KEY, null)
                val secret = prefs.getString(PREF_SECRET_KEY, null)
                val bucket = prefs.getString(PREF_BUCKET_NAME, null)
                if (acc != null && !isValidValue(acc)) prefs.edit().remove(PREF_ACCOUNT_ID).apply()
                if (key != null && !isValidValue(key)) prefs.edit().remove(PREF_ACCESS_KEY).apply()
                if (secret != null && !isValidValue(secret)) prefs.edit().remove(PREF_SECRET_KEY).apply()
                if (bucket != null && (!isValidValue(bucket) || bucket == "Backend_lovychat_api_token")) {
                    prefs.edit().remove(PREF_BUCKET_NAME).apply()
                }
                // Pastikan domain publik menggunakan custom domain resmi https://lovychat.my.id
                prefs.edit().putString(PREF_PUBLIC_DOMAIN, "https://lovychat.my.id").apply()
            }
        }
    }

    fun getAccountId(): String {
        val stored = sharedPrefs?.getString(PREF_ACCOUNT_ID, null)?.takeIf { isValidValue(it) }
        if (stored != null) return stored.trim()
        val build = BuildConfig.R2_ACCOUNT_ID
        if (isValidValue(build)) return build.trim()
        return "e918621d95bd4f025275ab5514e67753"
    }

    fun getAccessKeyId(): String {
        val stored = sharedPrefs?.getString(PREF_ACCESS_KEY, null)?.takeIf { isValidValue(it) }
        if (stored != null) return stored.trim()
        val build = BuildConfig.R2_ACCESS_KEY_ID
        if (isValidValue(build)) return build.trim()
        return "6090158ccbc5f5f27741f212bd6594bd"
    }

    fun getSecretAccessKey(): String {
        val stored = sharedPrefs?.getString(PREF_SECRET_KEY, null)?.takeIf { isValidValue(it) }
        if (stored != null) return stored.trim()
        val build = BuildConfig.R2_SECRET_ACCESS_KEY
        if (isValidValue(build)) return build.trim()
        return "9dfb893c990bec0a60a6ef23ee21efbbed4f267eccba2f137784f3a60df530ac"
    }

    fun getBucketName(): String {
        val stored = sharedPrefs?.getString(PREF_BUCKET_NAME, null)?.takeIf { isValidValue(it) && it != "Backend_lovychat_api_token" }
        if (stored != null) return stored.trim()
        val build = BuildConfig.R2_BUCKET_NAME
        if (isValidValue(build) && build != "Backend_lovychat_api_token") return build.trim()
        return "lovychat"
    }

    fun getPublicDomain(): String {
        val stored = sharedPrefs?.getString(PREF_PUBLIC_DOMAIN, null)?.takeIf { isValidValue(it) }
        val configured = stored
            ?: BuildConfig.R2_PUBLIC_DOMAIN.takeIf { isValidValue(it) }
            ?: "https://lovychat.my.id"

        var domain = configured.trim().removeSuffix("/")
        if (domain.isBlank()) {
            domain = "https://lovychat.my.id"
        } else if (!domain.startsWith("http://", ignoreCase = true) && !domain.startsWith("https://", ignoreCase = true)) {
            domain = "https://$domain"
        }
        return domain
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
        val validBucket = if (bucketName.isBlank() || bucketName == "Backend_lovychat_api_token") "lovychat" else bucketName.trim()
        val cleanDomain = if (publicDomain.contains("lovychat.my.id") || publicDomain.contains("r2.dev")) "" else publicDomain.trim()
        sharedPrefs?.edit()?.apply {
            putString(PREF_ACCOUNT_ID, accountId.trim())
            putString(PREF_ACCESS_KEY, accessKeyId.trim())
            putString(PREF_SECRET_KEY, secretAccessKey.trim())
            putString(PREF_BUCKET_NAME, validBucket)
            putString(PREF_PUBLIC_DOMAIN, cleanDomain)
            apply()
        }
    }

    /**
     * Upload sebuah gambar ke Cloudflare R2 menggunakan protokol AWS S3 PutObject SigV4.
     * Dilengkapi mekanisme retry otomatis untuk menjamin kehandalan upload pada jaringan seluler.
     *
     * @param bytes Konten gambar dalam bytes
     * @param folder Folder tujuan (misal "avatars", "moments", "chats")
     * @param fileName Nama file beserta ekstensi (misal "avatar_123.jpg")
     * @param contentType MIME type (default "image/jpeg")
     * @return Result berisi URL aktif gambar
     */
    /**
     * Upload rekaman pesan suara (.m4a) ke Cloudflare R2 dalam folder voice_notes.
     */
    suspend fun uploadVoiceNote(
        bytes: ByteArray,
        fileName: String = "vn_${System.currentTimeMillis()}.m4a"
    ): Result<String> {
        return uploadImage(
            bytes = bytes,
            folder = "voice_notes",
            fileName = fileName,
            contentType = "audio/mp4"
        )
    }

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
                IllegalStateException("Konfigurasi penyimpanan Cloudflare R2 belum lengkap.")
            )
        }

        val cleanFolder = folder.trim('/').replace("\\", "/")
        val cleanFileName = fileName.trim().replace("\\", "/")
        val objectKey = if (cleanFolder.isNotEmpty()) "$cleanFolder/$cleanFileName" else cleanFileName

        val host = "$accountId.r2.cloudflarestorage.com"
        val region = "auto"
        val service = "s3"
        val method = "PUT"

        val payloadHash = sha256Hex(bytes)
        val canonicalUri = "/$bucketName/$objectKey"
        val canonicalQuery = ""

        val signedHeaders = "content-type;host;x-amz-content-sha256;x-amz-date"
        val algorithm = "AWS4-HMAC-SHA256"

        var lastException: Exception? = null
        val maxAttempts = 3

        for (attempt in 1..maxAttempts) {
            val amzFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val amzDate = amzFormat.format(Date())
            val dateStamp = amzDate.substring(0, 8)
            val credentialScope = "$dateStamp/$region/$service/aws4_request"

            val canonicalHeaders = "content-type:$contentType\nhost:$host\nx-amz-content-sha256:$payloadHash\nx-amz-date:$amzDate\n"
            val canonicalRequest = "$method\n$canonicalUri\n$canonicalQuery\n$canonicalHeaders\n$signedHeaders\n$payloadHash"
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
                .header("User-Agent", "LovyChat-Android/1.0")
                .build()

            try {
                Log.d(TAG, "Mencoba upload ke Cloudflare R2 (Percobaan #$attempt): $requestUrl (Ukuran: ${bytes.size} byte)")
                val response = httpClient.newCall(request).execute()
                val responseCode = response.code
                if (response.isSuccessful) {
                    response.close()
                    Log.d(TAG, "Berhasil upload ke Cloudflare R2: HTTP $responseCode")
                    val publicUrl = resolvePublicUrl(objectKey)
                    return@withContext Result.success(publicUrl)
                } else {
                    val errorBody = response.body?.string() ?: ""
                    response.close()
                    Log.w(TAG, "Gagal upload ke R2: HTTP $responseCode - $errorBody (Percobaan #$attempt)")
                    if (responseCode == 401 || responseCode == 403) {
                        return@withContext Result.failure(Exception("Akses Cloudflare R2 ditolak (HTTP $responseCode). Periksa Access Key & Secret."))
                    }
                    lastException = Exception("Cloudflare R2 merespon HTTP $responseCode: $errorBody")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Koneksi ke R2 gagal pada percobaan #$attempt: ${e.message}")
                lastException = e
            }

            if (attempt < maxAttempts) {
                delay(attempt * 800L)
            }
        }

        val friendlyMessage = when (val ex = lastException) {
            is java.net.UnknownHostException -> "Tidak dapat terhubung ke server penyimpanan. Pastikan koneksi internet aktif."
            is java.net.SocketTimeoutException -> "Waktu koneksi habis saat mengunggah foto. Silakan coba kembali."
            else -> ex?.localizedMessage ?: "Gagal mengunggah foto ke Cloudflare R2."
        }
        Result.failure(Exception(friendlyMessage))
    }

    /**
     * Menyelesaikan URL akses untuk object key yang diunggah.
     * Menggunakan public custom domain jika terkonfigurasi, atau menghasilkan Presigned GET URL resmi S3 (valid 7 hari).
     */
    fun resolvePublicUrl(objectKey: String): String {
        val cleanKey = objectKey.trimStart('/')
        val publicDomain = getPublicDomain()
        if (publicDomain.isNotBlank()) {
            return "$publicDomain/$cleanKey"
        }
        return generatePresignedGetUrl(cleanKey, expiresInSeconds = 604800)
    }

    /**
     * Memperbaiki dan memperbarui URL gambar jika:
     * - Merupakan object key relatif ("avatars/xyz.jpg")
     * - Menggunakan domain rusak seperti lovychat.my.id
     * - Menggunakan presigned URL yang sudah kedaluwarsa atau mendekati kedaluwarsa
     */
    fun getOrRefreshPresignedUrl(urlOrKey: String?): String {
        if (urlOrKey.isNullOrBlank()) return ""
        val trimmed = urlOrKey.trim()

        // 1. Jika bukan URL (berarti object key langsung, contoh "avatars/xyz.jpg" atau "moments/abc.jpg")
        if (!trimmed.startsWith("http://", ignoreCase = true) &&
            !trimmed.startsWith("https://", ignoreCase = true) &&
            !trimmed.startsWith("content://", ignoreCase = true) &&
            !trimmed.startsWith("file://", ignoreCase = true) &&
            !trimmed.startsWith("android.resource://", ignoreCase = true)
        ) {
            val key = trimmed.trimStart('/')
            val domain = getPublicDomain()
            return if (domain.isNotBlank()) "$domain/$key" else generatePresignedGetUrl(key)
        }

        // 1. Jika URL menggunakan domain lovychat.my.id, buatkan presigned URL resmi agar selalu sukses dimuat walau binding custom domain Cloudflare belum aktif
        if (trimmed.contains("lovychat.my.id", ignoreCase = true)) {
            val key = trimmed.substringAfter("lovychat.my.id/").substringBefore('?').trimStart('/')
            if (key.isNotBlank() && isConfigured()) {
                val presigned = generatePresignedGetUrl(key)
                if (presigned.isNotBlank()) return presigned
            }
            return trimmed
        }

        // 3. Jika domain pub-*.r2.dev yang tidak publik
        val accountId = getAccountId()
        if (accountId.isNotBlank() && trimmed.contains("pub-$accountId.r2.dev", ignoreCase = true)) {
            val key = trimmed.substringAfter(".r2.dev/").substringBefore('?').trimStart('/')
            if (key.isNotBlank()) {
                return generatePresignedGetUrl(key)
            }
        }

        // 4. Jika URL S3 R2 Presigned: Cek apakah sudah kadaluarsa atau mendekati kadaluarsa
        val bucket = getBucketName()
        if (accountId.isNotBlank() && bucket.isNotBlank()) {
            val r2Host = "$accountId.r2.cloudflarestorage.com"
            if (trimmed.contains(r2Host) && trimmed.contains("/$bucket/")) {
                val amzDate = extractQueryParam(trimmed, "X-Amz-Date")
                val amzExpires = extractQueryParam(trimmed, "X-Amz-Expires")?.toLongOrNull() ?: 604800L
                if (isPresignedUrlExpiredOrExpiring(amzDate, amzExpires)) {
                    val key = trimmed.substringAfter("/$bucket/").substringBefore('?').trimStart('/')
                    if (key.isNotBlank()) {
                        return generatePresignedGetUrl(key)
                    }
                }
            }
        }

        return trimmed
    }

    private fun extractQueryParam(url: String, paramName: String): String? {
        val query = url.substringAfter('?', "")
        if (query.isBlank()) return null
        val parts = query.split('&')
        for (part in parts) {
            val kv = part.split('=', limit = 2)
            if (kv.isNotEmpty() && kv[0].equals(paramName, ignoreCase = true)) {
                return if (kv.size > 1) kv[1] else ""
            }
        }
        return null
    }

    private fun isPresignedUrlExpiredOrExpiring(amzDateStr: String?, expiresInSeconds: Long): Boolean {
        if (amzDateStr.isNullOrBlank()) return true
        return try {
            val format = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val date = format.parse(amzDateStr) ?: return true
            val expiryTimeMillis = date.time + (expiresInSeconds * 1000)
            // Refresh jika tersisa kurang dari 24 jam sebelum kedaluwarsa atau sudah lewat
            val bufferMillis = 24 * 3600 * 1000L
            System.currentTimeMillis() >= (expiryTimeMillis - bufferMillis)
        } catch (_: Exception) {
            true
        }
    }

    /**
     * Menghasilkan AWS S3 Presigned GET URL menggunakan SigV4 yang dapat langsung diunduh siapa saja tanpa autentikasi.
     */
    fun generatePresignedGetUrl(objectKey: String, expiresInSeconds: Long = 604800): String {
        val accountId = getAccountId()
        val accessKeyId = getAccessKeyId()
        val secretAccessKey = getSecretAccessKey()
        val bucketName = getBucketName()

        val cleanKey = objectKey.trimStart('/')
        if (accountId.isBlank() || accessKeyId.isBlank() || secretAccessKey.isBlank() || bucketName.isBlank() || cleanKey.isBlank()) {
            return ""
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

        val canonicalUri = "/$bucketName/$cleanKey"

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

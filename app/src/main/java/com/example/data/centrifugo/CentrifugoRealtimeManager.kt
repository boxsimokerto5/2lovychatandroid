package com.example.data.centrifugo

import android.util.Base64
import android.util.Log
import com.example.data.pocketbase.PocketBaseClient
import com.example.data.supabase.SupabaseMessageDto
import com.example.data.supabase.SupabaseRealtimeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

data class CentrifugoTypingEvent(
    val conversationId: String,
    val senderId: String,
    val isTyping: Boolean
)

/**
 * Centrifugo Realtime WebSocket Manager
 * Menangani koneksi WebSocket berkecepatan tinggi ke Centrifugo v3/v4/v5,
 * autentikasi JWT token_secret, langganan channel per-pengguna, dan publikasi pesan instan via HTTP API.
 */
object CentrifugoRealtimeManager {
    private const val TAG = "CentrifugoRealtime"

    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private val cmdIdCounter = AtomicInteger(1)
    private val isConnected = AtomicBoolean(false)
    private val isConnecting = AtomicBoolean(false)

    private var webSocket: WebSocket? = null
    private var currentUserId: String? = null
    private var reconnectJob: Job? = null

    private val _typingEvents = MutableSharedFlow<CentrifugoTypingEvent>(extraBufferCapacity = 64)
    val typingEvents: SharedFlow<CentrifugoTypingEvent> = _typingEvents.asSharedFlow()

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .pingInterval(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    fun getWsUrl(): String {
        val build = try {
            val field = com.example.BuildConfig::class.java.getField("CENTRIFUGO_WS_URL")
            field.get(null) as? String ?: ""
        } catch (_: Throwable) { "" }

        return if (build.isNotBlank() && !build.startsWith("your_")) build
        else "ws://173.249.59.183:8000/connection/websocket"
    }

    fun getApiUrl(): String {
        val build = try {
            val field = com.example.BuildConfig::class.java.getField("CENTRIFUGO_API_URL")
            field.get(null) as? String ?: ""
        } catch (_: Throwable) { "" }

        val url = if (build.isNotBlank() && !build.startsWith("your_")) build
        else "http://173.249.59.183:8000/api"
        return if (url.endsWith("/")) url else "$url/"
    }

    fun getTokenSecret(): String {
        val build = try {
            val field = com.example.BuildConfig::class.java.getField("CENTRIFUGO_TOKEN_SECRET")
            field.get(null) as? String ?: ""
        } catch (_: Throwable) { "" }

        return if (build.isNotBlank() && !build.startsWith("your_")) build
        else "lovi_chat"
    }

    fun getApiKey(): String {
        val build = try {
            val field = com.example.BuildConfig::class.java.getField("CENTRIFUGO_API_KEY")
            field.get(null) as? String ?: ""
        } catch (_: Throwable) { "" }

        return if (build.isNotBlank() && !build.startsWith("your_")) build
        else "geccko_creator"
    }

    /**
     * Membuat JWT Token terenkripsi HS256 untuk autentikasi koneksi Centrifugo.
     */
    fun generateConnectionJwt(userId: String): String {
        val secret = getTokenSecret()
        val headerJson = """{"alg":"HS256","typ":"JWT"}"""
        val exp = (System.currentTimeMillis() / 1000) + (30 * 24 * 3600L) // Berlaku 30 hari
        val payloadJson = """{"sub":"$userId","exp":$exp}"""

        val headerB64 = Base64.encodeToString(headerJson.toByteArray(Charsets.UTF_8), Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        val payloadB64 = Base64.encodeToString(payloadJson.toByteArray(Charsets.UTF_8), Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        val content = "$headerB64.$payloadB64"

        return try {
            val mac = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256")
            mac.init(secretKey)
            val signatureBytes = mac.doFinal(content.toByteArray(Charsets.UTF_8))
            val signatureB64 = Base64.encodeToString(signatureBytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            "$content.$signatureB64"
        } catch (e: Exception) {
            Log.e(TAG, "Gagal membuat JWT Centrifugo: ${e.message}", e)
            ""
        }
    }

    fun connect(userId: String?) {
        if (!userId.isNullOrBlank()) {
            currentUserId = userId
        }
        val targetUser = currentUserId ?: return

        if (isConnected.get() || isConnecting.get()) return
        isConnecting.set(true)

        val wsUrl = getWsUrl()
        Log.d(TAG, "Menghubungkan ke Centrifugo WebSocket: $wsUrl (user: $targetUser)")

        try {
            val request = Request.Builder().url(wsUrl).build()
            webSocket = httpClient.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(ws: WebSocket, response: Response) {
                    Log.i(TAG, "Koneksi WebSocket Centrifugo terbuka. Mengirim perintah otentikasi...")
                    val token = generateConnectionJwt(targetUser)
                    val connectCmd = JSONObject().apply {
                        put("id", cmdIdCounter.getAndIncrement())
                        put("connect", JSONObject().apply {
                            put("token", token)
                        })
                    }
                    ws.send(connectCmd.toString())
                }

                override fun onMessage(ws: WebSocket, text: String) {
                    val trimmed = text.trim()
                    // Tangani ping kosong bawaan Centrifugo
                    if (trimmed.isEmpty() || trimmed == "{}") {
                        ws.send("{}")
                        return
                    }
                    handleIncomingJson(ws, trimmed, targetUser)
                }

                override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                    Log.w(TAG, "Centrifugo WebSocket ditutup: $code / $reason")
                    isConnected.set(false)
                    isConnecting.set(false)
                }

                override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                    Log.w(TAG, "Centrifugo WebSocket error: ${t.message}")
                    isConnected.set(false)
                    isConnecting.set(false)
                    scheduleReconnect()
                }

                override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                    isConnected.set(false)
                    isConnecting.set(false)
                    scheduleReconnect()
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menginisialisasi WebSocket Centrifugo: ${e.message}", e)
            isConnecting.set(false)
            scheduleReconnect()
        }
    }

    private fun handleIncomingJson(ws: WebSocket, jsonStr: String, myUserId: String) {
        try {
            val root = JSONObject(jsonStr)

            // Respon Connect sukses
            if (root.has("connect")) {
                isConnected.set(true)
                isConnecting.set(false)
                reconnectJob?.cancel()
                Log.i(TAG, "Terhubung ke Centrifugo! Berlangganan channel obrolan pengguna...")

                // Berlangganan ke channel pribadi pengguna
                subscribeToUserChannels(ws, myUserId)
                return
            }

            // Pesan Broadcast / Push event
            if (root.has("push")) {
                val push = root.getJSONObject("push")
                val channel = push.optString("channel")
                val pub = push.optJSONObject("pub") ?: return
                val data = pub.optJSONObject("data") ?: return

                val eventType = data.optString("type")
                if (eventType == "typing") {
                    val convId = data.optString("conversation_id")
                    val senderId = data.optString("sender_id")
                    val isTyping = data.optBoolean("is_typing", false)
                    if (convId.isNotBlank() && senderId != myUserId) {
                        _typingEvents.tryEmit(CentrifugoTypingEvent(convId, senderId, isTyping))
                    }
                    return
                }

                // Pesan Obrolan Masuk
                val id = data.optString("id")
                val convId = data.optString("conversation_id")
                val senderId = data.optString("sender_id")

                if (convId.isNotBlank() && senderId.isNotBlank() && senderId != myUserId) {
                    val receiverId = data.optString("receiver_id").takeIf { it.isNotBlank() && it != "null" }
                    val text = data.optString("text")
                    val createdAt = data.optLong("created_at_ms", System.currentTimeMillis())
                    val imageUrl = data.optString("image_url").takeIf { it.isNotBlank() && it != "null" }
                    val isRead = data.optBoolean("is_read", false)
                    val replyToId = data.optString("reply_to_id").takeIf { it.isNotBlank() && it != "null" }
                    val replyToSender = data.optString("reply_to_sender").takeIf { it.isNotBlank() && it != "null" }
                    val replyToText = data.optString("reply_to_text").takeIf { it.isNotBlank() && it != "null" }
                    val reaction = data.optString("reaction").takeIf { it.isNotBlank() && it != "null" }

                    val dto = SupabaseMessageDto(
                        id = id,
                        conversationId = convId,
                        senderId = senderId,
                        receiverId = receiverId,
                        text = text,
                        createdAt = createdAt,
                        deletedForSender = false,
                        deletedForReceiver = false,
                        imageUrl = imageUrl,
                        isRead = isRead,
                        replyToId = replyToId,
                        replyToSender = replyToSender,
                        replyToText = replyToText,
                        reaction = reaction
                    )
                    Log.d(TAG, "Pesan instan diterima dari Centrifugo (channel: $channel): $id")
                    SupabaseRealtimeManager.emitIncomingMessage(dto)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal memproses pesan Centrifugo: ${e.message}")
        }
    }

    private fun subscribeToUserChannels(ws: WebSocket, userId: String) {
        val channels = mutableListOf("chat#$userId")
        val pbId = PocketBaseClient.toPbId(userId)
        if (pbId != userId && pbId.isNotBlank()) {
            channels.add("chat#$pbId")
        }

        channels.distinct().forEach { channelName ->
            val subCmd = JSONObject().apply {
                put("id", cmdIdCounter.getAndIncrement())
                put("subscribe", JSONObject().apply {
                    put("channel", channelName)
                })
            }
            ws.send(subCmd.toString())
            Log.d(TAG, "Mengirim permintaan langganan channel: $channelName")
        }
    }

    /**
     * Mengirim pesan chat instan ke penerima melalui HTTP Publish API Centrifugo.
     */
    fun publishChatMessage(
        messageId: String,
        conversationId: String,
        senderId: String,
        receiverId: String,
        text: String,
        imageUrl: String? = null,
        createdAtMs: Long = System.currentTimeMillis(),
        replyToId: String? = null,
        replyToSender: String? = null,
        replyToText: String? = null,
        reaction: String? = null
    ) {
        if (receiverId.isBlank()) return

        scope.launch {
            try {
                val payloadData = JSONObject().apply {
                    put("id", messageId)
                    put("conversation_id", conversationId)
                    put("sender_id", senderId)
                    put("receiver_id", receiverId)
                    put("text", text)
                    put("created_at_ms", createdAtMs)
                    imageUrl?.let { put("image_url", it) }
                    put("is_read", false)
                    replyToId?.let { put("reply_to_id", it) }
                    replyToSender?.let { put("reply_to_sender", it) }
                    replyToText?.let { put("reply_to_text", it) }
                    reaction?.let { put("reaction", it) }
                }

                // Kirim ke channel receiver ID raw dan juga versi PB id (jika berbeda)
                val targetChannels = mutableListOf("chat#$receiverId")
                val pbReceiverId = PocketBaseClient.toPbId(receiverId)
                if (pbReceiverId != receiverId && pbReceiverId.isNotBlank()) {
                    targetChannels.add("chat#$pbReceiverId")
                }

                targetChannels.distinct().forEach { channel ->
                    publishToChannel(channel, payloadData)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gagal mempublikasikan pesan chat ke Centrifugo", e)
            }
        }
    }

    /**
     * Mengirim event "sedang mengetik..." secara instan via Centrifugo.
     */
    fun publishTyping(conversationId: String, senderId: String, receiverId: String, isTyping: Boolean) {
        if (receiverId.isBlank()) return
        scope.launch {
            try {
                val payloadData = JSONObject().apply {
                    put("type", "typing")
                    put("conversation_id", conversationId)
                    put("sender_id", senderId)
                    put("is_typing", isTyping)
                }
                publishToChannel("chat#$receiverId", payloadData)
            } catch (_: Exception) {}
        }
    }

    private fun publishToChannel(channel: String, data: JSONObject) {
        val publishUrl = "${getApiUrl()}publish"
        val apiKey = getApiKey()

        val requestBody = JSONObject().apply {
            put("channel", channel)
            put("data", data)
        }

        val request = Request.Builder()
            .url(publishUrl)
            .addHeader("Authorization", "apikey $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()

        httpClient.newCall(request).execute().use { resp ->
            if (resp.isSuccessful) {
                Log.d(TAG, "Sukses publish ke Centrifugo channel: $channel")
            } else {
                Log.w(TAG, "Gagal publish ke Centrifugo ($channel): HTTP ${resp.code} - ${resp.body?.string()}")
            }
        }
    }

    private fun scheduleReconnect() {
        if (reconnectJob?.isActive == true) return
        reconnectJob = scope.launch {
            delay(4000L)
            if (!isConnected.get() && currentUserId != null) {
                connect(currentUserId)
            }
        }
    }

    fun disconnect() {
        reconnectJob?.cancel()
        try {
            webSocket?.close(1000, "App closed")
        } catch (_: Exception) {}
        webSocket = null
        isConnected.set(false)
        isConnecting.set(false)
    }

    fun isConnected(): Boolean = isConnected.get()
}

package com.example.data.supabase

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * SupabaseRealtimeManager
 *
 * Mengelola langganan WebSocket Realtime dua arah ke Supabase (Phoenix Channels).
 * Memastikan pesan chat yang dikirim langsung diterima secara instan (sub-second latency)
 * oleh kedua perangkat tanpa harus menunggu polling periodik.
 */
object SupabaseRealtimeManager {
    private const val TAG = "SupabaseRealtime"
    private const val TOPIC_CHAT_MESSAGES = "realtime:public:chat_messages"

    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var webSocket: WebSocket? = null
    private var heartbeatJob: Job? = null
    private var reconnectJob: Job? = null

    private val refCounter = AtomicInteger(1)
    private val isConnected = AtomicBoolean(false)
    private val isConnecting = AtomicBoolean(false)

    private var currentUserId: String? = null

    private val _incomingMessages = MutableSharedFlow<SupabaseMessageDto>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val incomingMessages: SharedFlow<SupabaseMessageDto> = _incomingMessages.asSharedFlow()

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS) // Keep-alive indefinitely for WebSocket
            .writeTimeout(12, TimeUnit.SECONDS)
            .pingInterval(20, TimeUnit.SECONDS) // OkHttp automatic ping-pong frames
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * Memulai koneksi Realtime WebSocket dan berlangganan ke tabel chat_messages
     */
    fun connect(userId: String?) {
        if (!userId.isNullOrBlank()) {
            currentUserId = userId
        }

        if (!SupabaseClient.isConfigured()) {
            Log.d(TAG, "Supabase belum terkonfigurasi, menunda koneksi Realtime.")
            return
        }

        if (isConnected.get() || isConnecting.get()) {
            return
        }

        isConnecting.set(true)
        val rawBase = SupabaseClient.getSupabaseUrl()
        val apiKey = SupabaseClient.getSupabaseAnonKey()

        val wsScheme = if (rawBase.startsWith("https://")) "wss://" else "ws://"
        val hostAndPath = rawBase
            .removePrefix("https://")
            .removePrefix("http://")
            .trimEnd('/')

        val wsUrl = "$wsScheme$hostAndPath/realtime/v1/websocket?apikey=$apiKey&vsn=1.0.0"
        Log.i(TAG, "Menghubungkan ke Supabase Realtime WebSocket...")

        val request = Request.Builder()
            .url(wsUrl)
            .addHeader("apikey", apiKey)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        try {
            webSocket = client.newWebSocket(request, createWebSocketListener())
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menginisialisasi WebSocket", e)
            isConnecting.set(false)
            scheduleReconnect()
        }
    }

    private fun createWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                Log.i(TAG, "Terhubung ke Supabase Realtime WebSocket!")
                isConnected.set(true)
                isConnecting.set(false)
                reconnectJob?.cancel()

                // Bergabung ke channel chat_messages dengan Postgres Changes & Broadcast
                joinChatChannel(ws)

                // Mulai detak jantung (heartbeat) Phoenix channel
                startHeartbeat(ws)
            }

            override fun onMessage(ws: WebSocket, text: String) {
                handleIncomingRawMessage(text)
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                Log.w(TAG, "WebSocket Realtime ditutup: $code / $reason")
                isConnected.set(false)
                isConnecting.set(false)
                stopHeartbeat()
                scheduleReconnect()
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.w(TAG, "WebSocket Realtime mengalami kegagalan: ${t.message}")
                isConnected.set(false)
                isConnecting.set(false)
                stopHeartbeat()
                scheduleReconnect()
            }
        }
    }

    private fun joinChatChannel(ws: WebSocket) {
        try {
            val ref = refCounter.incrementAndGet().toString()
            val joinPayload = JSONObject().apply {
                put("topic", TOPIC_CHAT_MESSAGES)
                put("event", "phx_join")
                put("ref", ref)

                val config = JSONObject().apply {
                    val broadcast = JSONObject().put("self", true)
                    put("broadcast", broadcast)

                    val postgresChanges = JSONArray().apply {
                        val filter = JSONObject().apply {
                            put("event", "*")
                            put("schema", "public")
                            put("table", "chat_messages")
                        }
                        put(filter)
                    }
                    put("postgres_changes", postgresChanges)
                }

                val payload = JSONObject().apply {
                    put("config", config)
                }
                put("payload", payload)
            }

            ws.send(joinPayload.toString())
            Log.d(TAG, "Langganan Realtime dikirim untuk $TOPIC_CHAT_MESSAGES")
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mengirim join payload", e)
        }
    }

    private fun startHeartbeat(ws: WebSocket) {
        heartbeatJob?.cancel()
        heartbeatJob = scope.launch {
            while (isActive && isConnected.get()) {
                delay(25000L) // Phoenix ping interval (25 detik)
                try {
                    val hb = JSONObject().apply {
                        put("topic", "phoenix")
                        put("event", "heartbeat")
                        put("payload", JSONObject())
                        put("ref", "hb_${refCounter.incrementAndGet()}")
                    }
                    ws.send(hb.toString())
                } catch (e: Exception) {
                    Log.w(TAG, "Gagal mengirim heartbeat: ${e.message}")
                    break
                }
            }
        }
    }

    private fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }

    private fun scheduleReconnect() {
        if (reconnectJob?.isActive == true) return
        reconnectJob = scope.launch {
            delay(4000L)
            if (!isConnected.get()) {
                Log.d(TAG, "Mencoba menghubungkan ulang WebSocket Realtime...")
                connect(currentUserId)
            }
        }
    }

    private fun handleIncomingRawMessage(jsonStr: String) {
        try {
            val root = JSONObject(jsonStr)
            val event = root.optString("event")
            val payload = root.optJSONObject("payload") ?: return

            var record: JSONObject? = null

            when (event) {
                "postgres_changes" -> {
                    val data = payload.optJSONObject("data")
                    record = data?.optJSONObject("record") ?: payload.optJSONObject("record")
                }
                "broadcast" -> {
                    record = payload.optJSONObject("payload") ?: payload
                }
                else -> {
                    if (payload.has("id") && payload.has("conversation_id")) {
                        record = payload
                    }
                }
            }

            if (record != null) {
                val id = record.optString("id")
                val convId = record.optString("conversation_id")
                val senderId = record.optString("sender_id")
                val receiverId = record.optString("receiver_id").takeIf { it.isNotBlank() && it != "null" }
                val text = record.optString("text")
                val createdAt = record.optLong("created_at", System.currentTimeMillis())
                val deletedForSender = record.optBoolean("deleted_for_sender", false)
                val deletedForReceiver = record.optBoolean("deleted_for_receiver", false)
                val imageUrl = record.optString("image_url").takeIf { it.isNotBlank() && it != "null" }
                val isRead = record.optBoolean("is_read", false)

                if (id.isNotBlank() && convId.isNotBlank() && senderId.isNotBlank()) {
                    val dto = SupabaseMessageDto(
                        id = id,
                        conversationId = convId,
                        senderId = senderId,
                        receiverId = receiverId,
                        text = text,
                        createdAt = createdAt,
                        deletedForSender = deletedForSender,
                        deletedForReceiver = deletedForReceiver,
                        imageUrl = imageUrl,
                        isRead = isRead
                    )
                    _incomingMessages.tryEmit(dto)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal memproses pesan Realtime: ${e.message}")
        }
    }

    /**
     * Menyiarkan pesan secara instan via WebSocket Broadcast ke semua perangkat di channel
     */
    fun broadcastChatMessage(message: SupabaseMessageDto): Boolean {
        val ws = webSocket ?: return false
        if (!isConnected.get()) return false

        return try {
            val record = JSONObject().apply {
                put("id", message.id)
                put("conversation_id", message.conversationId)
                put("sender_id", message.senderId)
                message.receiverId?.let { put("receiver_id", it) }
                put("text", message.text)
                put("created_at", message.createdAt)
                put("deleted_for_sender", message.deletedForSender ?: false)
                put("deleted_for_receiver", message.deletedForReceiver ?: false)
                message.imageUrl?.let { put("image_url", it) }
                put("is_read", message.isRead ?: false)
            }

            val broadcastPayload = JSONObject().apply {
                put("topic", TOPIC_CHAT_MESSAGES)
                put("event", "broadcast")
                put("ref", "bc_${refCounter.incrementAndGet()}")

                val inner = JSONObject().apply {
                    put("type", "broadcast")
                    put("event", "new_chat_message")
                    put("payload", record)
                }
                put("payload", inner)
            }

            ws.send(broadcastPayload.toString())
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengirim siaran Realtime: ${e.message}")
            false
        }
    }

    /**
     * Memutuskan koneksi WebSocket saat aplikasi ditutup
     */
    fun disconnect() {
        stopHeartbeat()
        reconnectJob?.cancel()
        try {
            webSocket?.close(1000, "App closed")
        } catch (_: Exception) {}
        webSocket = null
        isConnected.set(false)
        isConnecting.set(false)
    }
}

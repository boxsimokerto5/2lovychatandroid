package com.example.data.pocketbase

import android.util.Log
import com.example.data.supabase.SupabaseMessageDto
import com.example.data.supabase.SupabaseRealtimeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

object PocketBaseRealtimeManager {
    private const val TAG = "PBRealtime"
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var streamJob: Job? = null
    private var reconnectJob: Job? = null

    private val isConnected = AtomicBoolean(false)
    private val isConnecting = AtomicBoolean(false)
    private var currentUserId: String? = null
    private var activeCall: okhttp3.Call? = null

    private val sseClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS) // Indefinite SSE stream
            .retryOnConnectionFailure(true)
            .build()
    }

    fun connect(userId: String?) {
        if (!userId.isNullOrBlank()) {
            currentUserId = userId
        }

        if (isConnected.get() || isConnecting.get()) return

        isConnecting.set(true)
        streamJob?.cancel()
        streamJob = scope.launch {
            runSseStream()
        }
    }

    private suspend fun runSseStream() {
        val baseUrl = PocketBaseClient.getBaseUrl()
        val sseUrl = "${baseUrl}api/realtime"

        try {
            val request = Request.Builder()
                .url(sseUrl)
                .addHeader("Accept", "text/event-stream")
                .addHeader("Cache-Control", "no-cache")
                .build()

            val call = sseClient.newCall(request)
            activeCall = call
            val response: Response = call.execute()

            if (!response.isSuccessful) {
                Log.w(TAG, "Koneksi SSE PocketBase gagal: HTTP ${response.code}")
                isConnecting.set(false)
                isConnected.set(false)
                scheduleReconnect()
                return
            }

            isConnected.set(true)
            isConnecting.set(false)
            reconnectJob?.cancel()
            Log.i(TAG, "Terhubung ke PocketBase Realtime (SSE)!")

            val body = response.body ?: return
            val reader = BufferedReader(InputStreamReader(body.byteStream(), Charsets.UTF_8))

            var currentEvent = ""
            var currentData = StringBuilder()

            while (scope.isActive) {
                val line = reader.readLine() ?: break
                val trimmed = line.trim()

                if (trimmed.isEmpty()) {
                    // Sinyal akhir event dalam format SSE (\n\n)
                    if (currentData.isNotEmpty()) {
                        processSseEvent(currentEvent, currentData.toString().trim(), baseUrl)
                        currentEvent = ""
                        currentData = StringBuilder()
                    }
                } else if (trimmed.startsWith("event:")) {
                    currentEvent = trimmed.removePrefix("event:").trim()
                } else if (trimmed.startsWith("data:")) {
                    currentData.append(trimmed.removePrefix("data:").trim())
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Koneksi SSE terputus: ${e.message}")
        } finally {
            isConnected.set(false)
            isConnecting.set(false)
            scheduleReconnect()
        }
    }

    private fun processSseEvent(event: String, data: String, baseUrl: String) {
        try {
            if (event == "PB_CONNECT" || data.contains("clientId")) {
                val json = JSONObject(data)
                val clientId = json.optString("clientId")
                if (clientId.isNotBlank()) {
                    scope.launch {
                        subscribeToCollections(baseUrl, clientId)
                    }
                }
                return
            }

            // Pesan data record
            val root = JSONObject(data)
            val record = root.optJSONObject("record") ?: root
            val convId = record.optString("conversation_id")
            val senderId = record.optString("sender_id")

            if (convId.isNotBlank() && senderId.isNotBlank()) {
                val id = record.optString("id")
                val receiverId = record.optString("receiver_id").takeIf { it.isNotBlank() && it != "null" }
                val text = record.optString("text")
                val createdAt = record.optLong("created_at_ms", System.currentTimeMillis())
                val imageUrl = record.optString("image_url").takeIf { it.isNotBlank() && it != "null" }
                val isRead = record.optBoolean("is_read", false)
                val replyToId = record.optString("reply_to_id").takeIf { it.isNotBlank() && it != "null" }
                val replyToSender = record.optString("reply_to_sender").takeIf { it.isNotBlank() && it != "null" }
                val replyToText = record.optString("reply_to_text").takeIf { it.isNotBlank() && it != "null" }
                val reaction = record.optString("reaction").takeIf { it.isNotBlank() && it != "null" }

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
                SupabaseRealtimeManager.emitIncomingMessage(dto)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal memproses event SSE: ${e.message}")
        }
    }

    private suspend fun subscribeToCollections(baseUrl: String, clientId: String) {
        try {
            val subUrl = "${baseUrl}api/realtime"
            val bodyJson = JSONObject().apply {
                put("clientId", clientId)
                val subs = org.json.JSONArray().apply {
                    put("messages")
                }
                put("subscriptions", subs)
            }

            val request = Request.Builder()
                .url(subUrl)
                .post(bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            sseClient.newCall(request).execute().use { resp ->
                if (resp.isSuccessful) {
                    Log.d(TAG, "Langganan Realtime ke koleksi messages berhasil!")
                } else {
                    Log.w(TAG, "Gagal langganan realtime: HTTP ${resp.code}")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengirim permintaan langganan SSE", e)
        }
    }

    private fun scheduleReconnect() {
        if (reconnectJob?.isActive == true) return
        reconnectJob = scope.launch {
            delay(5000L)
            if (!isConnected.get()) {
                connect(currentUserId)
            }
        }
    }

    fun disconnect() {
        streamJob?.cancel()
        reconnectJob?.cancel()
        try {
            activeCall?.cancel()
        } catch (_: Exception) {}
        activeCall = null
        isConnected.set(false)
        isConnecting.set(false)
    }
}

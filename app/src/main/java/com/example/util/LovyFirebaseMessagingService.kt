package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.launch

class LovyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "LovyFCM"
        const val CHANNEL_ID = LovyNotificationHelper.CHANNEL_ID
        const val CHANNEL_NAME = LovyNotificationHelper.CHANNEL_NAME

        fun createNotificationChannel(context: Context) {
            LovyNotificationHelper.createNotificationChannel(context)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed FCM Token: $token")
        // Simpan token ke preferensi lokal
        val prefs = getSharedPreferences("lovy_fcm_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("fcm_token", token).apply()

        // Sinkronisasi otomatis ke Supabase jika user telah login
        try {
            val authPrefs = getSharedPreferences("lovy_auth_store", Context.MODE_PRIVATE)
            val sessionJson = authPrefs.getString("current_active_session", null)
            if (!sessionJson.isNullOrBlank()) {
                val json = org.json.JSONObject(sessionJson)
                val lovyId = json.optString("lovy_id", "")
                val isGuest = json.optBoolean("is_guest", false)
                if (lovyId.isNotBlank() && !isGuest && com.example.data.supabase.SupabaseClient.isConfigured()) {
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        try {
                            com.example.data.supabase.SupabaseRepository().updateUserFcmToken(lovyId, token)
                            Log.d(TAG, "onNewToken: Berhasil sinkronisasi token baru ke Supabase untuk $lovyId")
                        } catch (e: Exception) {
                            Log.w(TAG, "onNewToken: Gagal sinkronisasi token ke Supabase", e)
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            Log.w(TAG, "Error checking session in onNewToken", e)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        val conversationId = remoteMessage.data["conversationId"] ?: "chat_default"
        val messageId = remoteMessage.data["messageId"] ?: remoteMessage.data["id"] ?: ""
        val isDeleted = remoteMessage.data["deleted"] == "true"
        val body = remoteMessage.data["body"]
            ?: remoteMessage.notification?.body
            ?: "Anda menerima pesan baru"

        // 1. Jika ini sinyal penghapusan pesan, batalkan notifikasi yang mungkin aktif
        if (isDeleted || body == "__DELETED_FOR_EVERYONE__") {
            LovyNotificationHelper.cancelNotification(this, conversationId)
            return
        }

        // 2. Cek apakah obrolan ini pernah dihapus oleh pengguna dan pesan ini lebih lama dari waktu hapus
        val chatPrefs = getSharedPreferences("lovy_chat_prefs", Context.MODE_PRIVATE)
        val deletedTimestampsJson = chatPrefs.getString("deleted_conversations_map", null)
        if (!deletedTimestampsJson.isNullOrBlank()) {
            try {
                val json = org.json.JSONObject(deletedTimestampsJson)
                val deletedAt = json.optLong(conversationId, 0L)
                val msgTimestamp = remoteMessage.data["timestamp"]?.toLongOrNull() ?: System.currentTimeMillis()
                if (deletedAt > 0L && msgTimestamp <= deletedAt) {
                    Log.d(TAG, "Mengabaikan notifikasi untuk pesan dari obrolan yang sudah dihapus: $conversationId")
                    return
                }
            } catch (_: Exception) {}
        }

        // 3. Cek apakah pesan spesifik ini sudah ditandai terhapus
        val deletedMsgIds = chatPrefs.getStringSet("deleted_message_ids", emptySet()) ?: emptySet()
        if (messageId.isNotBlank() && deletedMsgIds.contains(messageId)) {
            Log.d(TAG, "Mengabaikan notifikasi untuk pesan ID yang telah dihapus: $messageId")
            return
        }

        // Baca title & body baik dari payload data maupun notification
        val title = remoteMessage.data["title"]
            ?: remoteMessage.notification?.title
            ?: "Pesan Baru di Lovy"
        val senderName = remoteMessage.data["senderName"] ?: title

        LovyNotificationHelper.showChatNotification(
            context = this,
            conversationId = conversationId,
            senderName = senderName,
            messageText = body
        )
    }
}

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
        const val CHANNEL_ID = "lovy_chat_messages"
        const val CHANNEL_NAME = "Pesan & Notifikasi Lovy Chat"

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifikasi pesan obrolan dan momen masuk"
                    enableLights(true)
                    enableVibration(true)
                }
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                notificationManager?.createNotificationChannel(channel)
            }
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

        // Baca title & body baik dari payload data maupun notification
        val title = remoteMessage.data["title"]
            ?: remoteMessage.notification?.title
            ?: "Pesan Baru di Lovy"
        val body = remoteMessage.data["body"]
            ?: remoteMessage.notification?.body
            ?: "Anda menerima pesan baru"
        val conversationId = remoteMessage.data["conversationId"]
        val senderName = remoteMessage.data["senderName"]

        sendNotification(title, body, conversationId, senderName)
    }

    private fun sendNotification(
        title: String,
        body: String,
        conversationId: String?,
        senderName: String?
    ) {
        createNotificationChannel(this)

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            if (!conversationId.isNullOrBlank()) {
                putExtra("extra_conversation_id", conversationId)
            }
            if (!senderName.isNullOrBlank()) {
                putExtra("extra_sender_name", senderName)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            (conversationId?.hashCode() ?: 0),
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CHANNEL_ID
        } else {
            CHANNEL_ID
        }
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), notificationBuilder.build())
    }
}

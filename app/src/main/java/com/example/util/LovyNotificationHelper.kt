package com.example.util

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R

object LovyNotificationHelper {

    private const val TAG = "LovyNotificationHelper"
    const val CHANNEL_ID = "lovy_chat_messages"
    const val CHANNEL_NAME = "Pesan & Notifikasi Lovy Chat"

    // Pola getar khas chat: jeda 0ms, getar 250ms, jeda 150ms, getar 250ms
    val VIBRATION_PATTERN = longArrayOf(0, 250, 150, 250)

    /**
     * Membuat NotificationChannel dengan konfigurasi getar & suara berprioritas tinggi.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT)
                .build()

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi pesan obrolan 2 arah, sapaan teman sekitar, dan momen"
                enableLights(true)
                lightColor = Color.parseColor("#00C853")
                enableVibration(true)
                vibrationPattern = VIBRATION_PATTERN
                setSound(defaultSoundUri, audioAttributes)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    /**
     * Mengirimkan notifikasi sistem lengkap dengan efek getar dan suara.
     */
    fun showChatNotification(
        context: Context,
        conversationId: String,
        senderName: String,
        messageText: String,
        partnerAvatarHex: Long = 0xFF4CAF50
    ) {
        try {
            // Pastikan channel telah dibuat
            createNotificationChannel(context)

            // Cek izin POST_NOTIFICATIONS untuk Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
                if (!hasPermission) {
                    Log.w(TAG, "Izin POST_NOTIFICATIONS belum diberikan oleh pengguna, hanya getar lokal")
                    vibrateChatNotification(context)
                    return
                }
            }

            // Intent membuka chat langsung ke pengguna yang mengirim pesan
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("extra_conversation_id", conversationId)
                putExtra("extra_sender_name", senderName)
            }

            val requestCode = (conversationId.hashCode() and 0xFFFF)
            val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(senderName)
                .setContentText(messageText)
                .setStyle(NotificationCompat.BigTextStyle().bigText(messageText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setVibrate(VIBRATION_PATTERN)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

            val notifId = (conversationId.hashCode() and 0x7FFFFFFF)
            notificationManager?.notify(notifId, notificationBuilder.build())

            // Picu getar langsung pada motor getar perangkat
            vibrateChatNotification(context)
        } catch (e: Throwable) {
            Log.e(TAG, "Gagal menampilkan notifikasi chat", e)
        }
    }

    /**
     * Membatalkan notifikasi sistem untuk percakapan tertentu (misal saat percakapan dibuka atau dihapus).
     */
    fun cancelNotification(context: Context, conversationId: String) {
        try {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            val notifId = (conversationId.hashCode() and 0x7FFFFFFF)
            notificationManager?.cancel(notifId)
        } catch (e: Throwable) {
            Log.w(TAG, "Gagal membatalkan notifikasi untuk conversation: $conversationId", e)
        }
    }

    /**
     * Membatalkan seluruh notifikasi obrolan.
     */
    fun cancelAllNotifications(context: Context) {
        try {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.cancelAll()
        } catch (e: Throwable) {
            Log.w(TAG, "Gagal membatalkan seluruh notifikasi", e)
        }
    }

    /**
     * Mengaktifkan motor getar perangkat secara langsung dengan pola notifikasi pesan masuk.
     */
    fun vibrateChatNotification(context: Context) {
        vibratePattern(context, VIBRATION_PATTERN)
    }

    /**
     * Getar halus (haptic feedback) saat pengguna sedang membuka obrolan dan ada pesan masuk baru,
     * atau saat berhasil mengirim pesan.
     */
    fun vibrateSubtle(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(50)
                    }
                }
            }
        } catch (e: Throwable) {
            Log.w(TAG, "Gagal memicu getar halus", e)
        }
    }

    /**
     * Menjalankan getaran dengan pola custom pada perangkat.
     */
    fun vibratePattern(context: Context, pattern: LongArray) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                if (vibrator != null && vibrator.hasVibrator()) {
                    val effect = VibrationEffect.createWaveform(pattern, -1)
                    vibrator.vibrate(effect)
                }
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val effect = VibrationEffect.createWaveform(pattern, -1)
                        vibrator.vibrate(effect)
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(pattern, -1)
                    }
                }
            }
        } catch (e: Throwable) {
            Log.w(TAG, "Gagal memicu getar perangkat", e)
        }
    }
}

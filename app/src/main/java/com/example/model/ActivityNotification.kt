package com.example.model

enum class NotificationCategory {
    NEARBY,       // Sapaan / Radar Teman Sekitar
    FRIEND,       // Permintaan Pertemanan Baru
    BOTTLE,       // Pesan Botol Samudra
    SYSTEM        // Info Akun & Sistem
}

data class ActivityNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val category: NotificationCategory = NotificationCategory.SYSTEM,
    val senderName: String? = null,
    val avatarUrl: String? = null,
    val avatarHex: Long? = null
)

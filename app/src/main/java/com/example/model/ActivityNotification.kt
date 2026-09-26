package com.example.model

import com.example.util.AppLanguage
import com.example.util.AppStrings

enum class NotificationCategory {
    NEARBY,       // Sapaan / Radar Teman Sekitar
    FRIEND,       // Permintaan Pertemanan Baru
    BOTTLE,       // Pesan Botol Samudra
    SYSTEM        // Info Akun & Sistem
}

data class ActivityNotification(
    val id: String,
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val category: NotificationCategory = NotificationCategory.SYSTEM,
    val senderName: String? = null,
    val avatarUrl: String? = null,
    val avatarHex: Long? = null,
    val translationKey: String? = null
) {
    fun getDisplayTitle(language: AppLanguage): String {
        return when {
            translationKey == "welcome" || id == "sys_welcome" || isWelcomePattern(title) -> {
                AppStrings.notifWelcomeTitle(language)
            }
            translationKey == "radar_active" || id == "sys_radar_active" || isRadarActivePattern(title) -> {
                AppStrings.notifRadarActiveTitle(language)
            }
            translationKey == "friend_request" || id.startsWith("friend_req_") || isFriendRequestPattern(title) -> {
                AppStrings.notifFriendRequestTitle(language)
            }
            translationKey == "bottle_caught" || id.startsWith("bottle_") || category == NotificationCategory.BOTTLE -> {
                AppStrings.notifBottleCaughtTitle(language)
            }
            translationKey == "nearby_greet" || id.startsWith("nearby_greet_") || id.startsWith("radar_greet_") -> {
                AppStrings.notifNearbyGreetTitle(language)
            }
            translationKey == "new_chat_message" || id.startsWith("chat_") || id.startsWith("msg_") -> {
                AppStrings.notifNewChatMessageTitle(language)
            }
            title.isNotBlank() -> title
            else -> AppStrings.notificationsTitle(language)
        }
    }

    fun getDisplayMessage(language: AppLanguage): String {
        return when {
            translationKey == "welcome" || id == "sys_welcome" || isWelcomePattern(title) -> {
                AppStrings.notifWelcomeDesc(language)
            }
            translationKey == "radar_active" || id == "sys_radar_active" || isRadarActivePattern(title) -> {
                AppStrings.notifRadarActiveDesc(language)
            }
            translationKey == "friend_request" || id.startsWith("friend_req_") || isFriendRequestPattern(title) -> {
                val name = senderName ?: extractName(message) ?: AppStrings.defaultFriendName(language)
                AppStrings.notifFriendRequestDesc(language, name)
            }
            translationKey == "bottle_caught" || id.startsWith("bottle_") || category == NotificationCategory.BOTTLE -> {
                val name = senderName ?: extractName(message) ?: AppStrings.defaultFriendName(language)
                AppStrings.notifBottleCaughtDesc(language, name)
            }
            translationKey == "nearby_greet" || id.startsWith("nearby_greet_") || id.startsWith("radar_greet_") -> {
                val name = senderName ?: extractName(message) ?: AppStrings.defaultFriendName(language)
                AppStrings.notifNearbyGreetDesc(language, name)
            }
            translationKey == "new_chat_message" || id.startsWith("chat_") || id.startsWith("msg_") -> {
                val name = senderName ?: extractName(message) ?: AppStrings.defaultFriendName(language)
                AppStrings.notifNewChatMessageDesc(language, name)
            }
            message.isNotBlank() -> message
            else -> ""
        }
    }

    private fun isWelcomePattern(text: String): Boolean {
        val lower = text.lowercase()
        return lower.contains("selamat datang") || lower.contains("welcome") ||
                lower.contains("환영") || lower.contains("欢迎") || lower.contains("ようこそ")
    }

    private fun isRadarActivePattern(text: String): Boolean {
        val lower = text.lowercase()
        return lower.contains("radar sekitar") || lower.contains("radar active") ||
                lower.contains("nearby radar") || lower.contains("레이더") || lower.contains("雷达")
    }

    private fun isFriendRequestPattern(text: String): Boolean {
        val lower = text.lowercase()
        return lower.contains("permintaan pertemanan") || lower.contains("friend request") ||
                lower.contains("친구 요청") || lower.contains("好友请求") || lower.contains("友達リクエスト")
    }

    private fun extractName(text: String): String? {
        val trimmed = text.trim()
        val suffixPatterns = listOf(
            " ingin berteman",
            " wants to be",
            " 想加您",
            " 님이 친구",
            " さんがあなた"
        )
        for (pattern in suffixPatterns) {
            val idx = trimmed.indexOf(pattern)
            if (idx > 0) {
                return trimmed.substring(0, idx).trim()
            }
        }
        return null
    }
}

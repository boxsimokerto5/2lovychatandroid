package com.example.model

enum class Gender {
    MALE, FEMALE
}

data class User(
    val id: String,
    val name: String,
    val gender: Gender,
    val age: Int = 22,
    val distanceMeters: Int = 100,
    val bio: String,
    val avatarColorHex: Long,
    val isOnline: Boolean = true,
    val city: String = "Jakarta Selatan",
    val avatarUrl: String? = null,
    val isFavorite: Boolean = false,
    val isSameCity: Boolean = false
) {
    val formattedDistance: String
        get() = if (distanceMeters < 1000) {
            "${distanceMeters}m"
        } else {
            String.format("%.1f km", distanceMeters / 1000.0)
        }
}

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val text: String,
    val timestamp: Long,
    val isFromMe: Boolean,
    val isRead: Boolean = false,
    val deletedForSender: Boolean = false,
    val deletedForReceiver: Boolean = false,
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val audioDurationSeconds: Int = 0,
    val replyToId: String? = null,
    val replyToSender: String? = null,
    val replyToText: String? = null,
    val reaction: String? = null
)

data class ChatConversation(
    val id: String,
    val partnerId: String,
    val partnerName: String,
    val partnerAvatarHex: Long,
    val partnerGender: Gender,
    val lastMessage: String,
    val lastTimestamp: Long,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true,
    val partnerAvatarUrl: String? = null,
    val lastMessageIsFromMe: Boolean = false,
    val lastMessageIsRead: Boolean = false,
    val partnerAge: Int = 22,
    val partnerDistanceMeters: Int = 120,
    val partnerCity: String? = null
) {
    val formattedDistance: String
        get() = if (partnerDistanceMeters < 1000) {
            "${partnerDistanceMeters}m"
        } else {
            String.format(java.util.Locale.US, "%.1f km", partnerDistanceMeters / 1000.0)
        }
}

data class BottleMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val senderGender: Gender,
    val avatarHex: Long,
    val content: String,
    val thrownTimestamp: Long,
    val locationHint: String = "Laut Jawa",
    val isFromMe: Boolean = false,
    val replyCount: Int = 0,
    val avatarUrl: String? = null
)

data class MomentItem(
    val id: String,
    val authorName: String,
    val authorAvatarHex: Long,
    val timeAgo: String,
    val content: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val commentsCount: Int = 0,
    val imageUrl: String? = null,
    val authorAvatarUrl: String? = null,
    val locationTag: String? = null,
    val isDeleted: Boolean = false,
    val authorId: String = ""
)

data class MomentComment(
    val id: String,
    val momentId: String,
    val authorId: String,
    val authorName: String,
    val authorAvatarHex: Long,
    val authorAvatarUrl: String? = null,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val timeAgo: String = "Baru saja"
)

data class NewFriendRequest(
    val id: String,
    val user: User,
    val greetingMessage: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isAccepted: Boolean = false,
    val isIgnored: Boolean = false
)

data class AppUpdateInfo(
    val minVersionCode: Int = 1,
    val latestVersionCode: Int = 1,
    val latestVersionName: String = "1.0",
    val title: String = "",
    val message: String = "",
    val changelog: List<String> = emptyList(),
    val isForceUpdate: Boolean = false
)

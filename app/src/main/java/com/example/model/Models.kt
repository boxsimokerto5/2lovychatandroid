package com.example.model

enum class Gender {
    MALE, FEMALE
}

data class User(
    val id: String,
    val name: String,
    val gender: Gender,
    val age: Int,
    val distanceMeters: Int,
    val bio: String,
    val avatarColorHex: Long,
    val isOnline: Boolean = true,
    val city: String = "Jakarta Selatan",
    val avatarUrl: String? = null,
    val isFavorite: Boolean = false
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
    val isRead: Boolean = true,
    val deletedForSender: Boolean = false,
    val deletedForReceiver: Boolean = false,
    val imageUrl: String? = null
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
    val partnerAvatarUrl: String? = null
)

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

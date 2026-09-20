package com.example.data.supabase

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SupabaseUserDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "gender") val gender: String, // "male", "female"
    @Json(name = "distance_meters") val distanceMeters: Int = 100,
    @Json(name = "bio") val bio: String = "",
    @Json(name = "avatar_hex") val avatarHex: Long = 0xFF2E7D32,
    @Json(name = "is_online") val isOnline: Boolean = true,
    @Json(name = "last_active_at") val lastActiveAt: Long = System.currentTimeMillis(),
    @Json(name = "avatar_url") val avatarUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class SupabaseBottleDto(
    @Json(name = "id") val id: String,
    @Json(name = "sender_id") val senderId: String,
    @Json(name = "sender_name") val senderName: String,
    @Json(name = "sender_gender") val senderGender: String,
    @Json(name = "content") val content: String,
    @Json(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @Json(name = "location_hint") val locationHint: String = "Lautan Nusantara",
    @Json(name = "avatar_hex") val avatarHex: Long = 0xFF00838F
)

@JsonClass(generateAdapter = true)
data class SupabaseMessageDto(
    @Json(name = "id") val id: String,
    @Json(name = "conversation_id") val conversationId: String,
    @Json(name = "sender_id") val senderId: String,
    @Json(name = "text") val text: String,
    @Json(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @Json(name = "deleted_for_sender") val deletedForSender: Boolean = false,
    @Json(name = "deleted_for_receiver") val deletedForReceiver: Boolean = false,
    @Json(name = "image_url") val imageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class SupabaseMomentDto(
    @Json(name = "id") val id: String,
    @Json(name = "author_id") val authorId: String,
    @Json(name = "author_name") val authorName: String,
    @Json(name = "content") val content: String,
    @Json(name = "likes_count") val likesCount: Int = 0,
    @Json(name = "comments_count") val commentsCount: Int = 0,
    @Json(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @Json(name = "author_avatar_hex") val authorAvatarHex: Long = 0xFFFB8C00,
    @Json(name = "image_url") val imageUrl: String? = null
)

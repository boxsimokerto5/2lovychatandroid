package com.example.data.supabase

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SupabaseUserDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "gender") val gender: String, // "male", "female"
    @Json(name = "distance_meters") val distanceMeters: Int? = 100,
    @Json(name = "bio") val bio: String? = "",
    @Json(name = "avatar_hex") val avatarHex: Long? = 0xFF2E7D32,
    @Json(name = "is_online") val isOnline: Boolean? = true,
    @Json(name = "last_active_at") val lastActiveAt: Long? = null,
    @Json(name = "avatar_url") val avatarUrl: String? = null,
    @Json(name = "city") val city: String? = null,
    @Json(name = "fcm_token") val fcmToken: String? = null
)

@JsonClass(generateAdapter = true)
data class SupabaseUserPresenceDto(
    @Json(name = "is_online") val isOnline: Boolean,
    @Json(name = "last_active_at") val lastActiveAt: Long
)

@JsonClass(generateAdapter = true)
data class SupabaseBottleDto(
    @Json(name = "id") val id: String,
    @Json(name = "sender_id") val senderId: String,
    @Json(name = "sender_name") val senderName: String,
    @Json(name = "sender_gender") val senderGender: String,
    @Json(name = "content") val content: String,
    @Json(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @Json(name = "location_hint") val locationHint: String? = "Lautan Nusantara",
    @Json(name = "avatar_hex") val avatarHex: Long? = 0xFF00838F,
    @Json(name = "avatar_url") val avatarUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class SupabaseMessageDto(
    @Json(name = "id") val id: String,
    @Json(name = "conversation_id") val conversationId: String,
    @Json(name = "sender_id") val senderId: String,
    @Json(name = "receiver_id") val receiverId: String? = null,
    @Json(name = "text") val text: String,
    @Json(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @Json(name = "deleted_for_sender") val deletedForSender: Boolean? = null,
    @Json(name = "deleted_for_receiver") val deletedForReceiver: Boolean? = null,
    @Json(name = "image_url") val imageUrl: String? = null,
    @Json(name = "audio_url") val audioUrl: String? = null,
    @Json(name = "audio_duration_seconds") val audioDurationSeconds: Int? = null,
    @Json(name = "is_read") val isRead: Boolean? = null,
    @Json(name = "reply_to_id") val replyToId: String? = null,
    @Json(name = "reply_to_sender") val replyToSender: String? = null,
    @Json(name = "reply_to_text") val replyToText: String? = null,
    @Json(name = "reaction") val reaction: String? = null
)

@JsonClass(generateAdapter = true)
data class SupabaseMomentDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "author_id") val authorId: String? = null,
    @Json(name = "author_name") val authorName: String? = null,
    @Json(name = "content") val content: String? = null,
    @Json(name = "likes_count") val likesCount: Int? = 0,
    @Json(name = "comments_count") val commentsCount: Int? = 0,
    @Json(name = "created_at") val createdAt: Long? = System.currentTimeMillis(),
    @Json(name = "author_avatar_hex") val authorAvatarHex: Long? = 0xFFFB8C00,
    @Json(name = "image_url") val imageUrl: String? = null,
    @Json(name = "author_avatar_url") val authorAvatarUrl: String? = null,
    @Json(name = "location_tag") val locationTag: String? = null
)

@JsonClass(generateAdapter = true)
data class SupabaseBasicMomentDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "author_id") val authorId: String? = null,
    @Json(name = "author_name") val authorName: String? = null,
    @Json(name = "content") val content: String? = null,
    @Json(name = "likes_count") val likesCount: Int? = 0,
    @Json(name = "created_at") val createdAt: Long? = System.currentTimeMillis(),
    @Json(name = "image_url") val imageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class SupabaseAccountDto(
    @Json(name = "id") val id: String,
    @Json(name = "username") val username: String,
    @Json(name = "password_hash") val passwordHash: String? = null,
    @Json(name = "display_name") val displayName: String? = null,
    @Json(name = "gender") val gender: String? = "FEMALE",
    @Json(name = "bio") val bio: String? = "",
    @Json(name = "avatar_url") val avatarUrl: String? = null,
    @Json(name = "google_id") val googleId: String? = null,
    @Json(name = "google_email") val googleEmail: String? = null,
    @Json(name = "created_at") val createdAt: Long? = System.currentTimeMillis(),
    @Json(name = "last_login_at") val lastLoginAt: Long? = System.currentTimeMillis(),
    @Json(name = "fcm_token") val fcmToken: String? = null
)

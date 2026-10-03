package com.example.data.pocketbase

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PocketBasePage<T>(
    @Json(name = "page") val page: Int = 1,
    @Json(name = "perPage") val perPage: Int = 30,
    @Json(name = "totalItems") val totalItems: Int = 0,
    @Json(name = "totalPages") val totalPages: Int = 0,
    @Json(name = "items") val items: List<T> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PocketBaseAuthResponse(
    @Json(name = "token") val token: String? = null,
    @Json(name = "record") val record: PocketBaseUserRecord? = null
)

@JsonClass(generateAdapter = true)
data class PocketBaseUserRecord(
    @Json(name = "id") val id: String,
    @Json(name = "username") val username: String? = null,
    @Json(name = "email") val email: String? = null,
    @Json(name = "emailVisibility") val emailVisibility: Boolean? = null,
    @Json(name = "verified") val verified: Boolean? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "gender") val gender: String? = null,
    @Json(name = "bio") val bio: String? = null,
    @Json(name = "avatar_hex") val avatarHex: Long? = 0xFF2E7D32,
    @Json(name = "is_online") val isOnline: Boolean? = true,
    @Json(name = "last_active_at") val lastActiveAt: Long? = null,
    @Json(name = "avatar_url") val avatarUrl: String? = null,
    @Json(name = "city") val city: String? = null,
    @Json(name = "fcm_token") val fcmToken: String? = null,
    @Json(name = "created") val created: String? = null,
    @Json(name = "updated") val updated: String? = null
)

@JsonClass(generateAdapter = true)
data class PocketBaseMessageRecord(
    @Json(name = "id") val id: String,
    @Json(name = "conversation_id") val conversationId: String,
    @Json(name = "sender_id") val senderId: String,
    @Json(name = "receiver_id") val receiverId: String? = null,
    @Json(name = "text") val text: String,
    @Json(name = "created_at_ms") val createdAtMs: Long? = null,
    @Json(name = "image_url") val imageUrl: String? = null,
    @Json(name = "audio_url") val audioUrl: String? = null,
    @Json(name = "audio_duration_seconds") val audioDurationSeconds: Int? = null,
    @Json(name = "is_read") val isRead: Boolean? = null,
    @Json(name = "reply_to_id") val replyToId: String? = null,
    @Json(name = "reply_to_sender") val replyToSender: String? = null,
    @Json(name = "reply_to_text") val replyToText: String? = null,
    @Json(name = "reaction") val reaction: String? = null,
    @Json(name = "created") val created: String? = null
)

@JsonClass(generateAdapter = true)
data class PocketBaseBottleRecord(
    @Json(name = "id") val id: String,
    @Json(name = "sender_id") val senderId: String? = null,
    @Json(name = "sender_name") val senderName: String? = null,
    @Json(name = "sender_gender") val senderGender: String? = null,
    @Json(name = "content") val content: String,
    @Json(name = "created_at_ms") val createdAtMs: Long? = null,
    @Json(name = "location_hint") val locationHint: String? = null,
    @Json(name = "avatar_hex") val avatarHex: Long? = null,
    @Json(name = "avatar_url") val avatarUrl: String? = null,
    @Json(name = "created") val created: String? = null
)

@JsonClass(generateAdapter = true)
data class PocketBaseMomentRecord(
    @Json(name = "id") val id: String,
    @Json(name = "author_id") val authorId: String? = null,
    @Json(name = "author_name") val authorName: String? = null,
    @Json(name = "content") val content: String? = null,
    @Json(name = "likes_count") val likesCount: Int? = 0,
    @Json(name = "comments_count") val commentsCount: Int? = 0,
    @Json(name = "created_at_ms") val createdAtMs: Long? = null,
    @Json(name = "author_avatar_hex") val authorAvatarHex: Long? = null,
    @Json(name = "image_url") val imageUrl: String? = null,
    @Json(name = "author_avatar_url") val authorAvatarUrl: String? = null,
    @Json(name = "location_tag") val locationTag: String? = null,
    @Json(name = "created") val created: String? = null
)

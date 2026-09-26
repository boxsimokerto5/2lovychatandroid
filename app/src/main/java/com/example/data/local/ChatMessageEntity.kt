package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.model.ChatMessage

/**
 * Entitas Room untuk menyimpan pesan obrolan secara permanen di HP pengguna.
 * Local-first architecture menjamin pesan selalu tersimpan aman di perangkat
 * pengguna walaupun database cloud gratisan dibersihkan secara berkala.
 */
@Entity(
    tableName = "local_chat_messages",
    indices = [
        Index(value = ["conversationId", "timestamp"]),
        Index(value = ["timestamp"])
    ]
)
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val text: String,
    val timestamp: Long,
    val isFromMe: Boolean,
    val isRead: Boolean = false,
    val deletedForSender: Boolean = false,
    val deletedForReceiver: Boolean = false,
    val imageUrl: String? = null,
    val replyToId: String? = null,
    val replyToSender: String? = null,
    val replyToText: String? = null,
    val reaction: String? = null
) {
    fun toDomain(): ChatMessage = ChatMessage(
        id = id,
        conversationId = conversationId,
        text = text,
        timestamp = timestamp,
        isFromMe = isFromMe,
        isRead = isRead,
        deletedForSender = deletedForSender,
        deletedForReceiver = deletedForReceiver,
        imageUrl = imageUrl,
        replyToId = replyToId,
        replyToSender = replyToSender,
        replyToText = replyToText,
        reaction = reaction
    )

    companion object {
        fun fromDomain(model: ChatMessage): ChatMessageEntity = ChatMessageEntity(
            id = model.id,
            conversationId = model.conversationId,
            text = model.text,
            timestamp = model.timestamp,
            isFromMe = model.isFromMe,
            isRead = model.isRead,
            deletedForSender = model.deletedForSender,
            deletedForReceiver = model.deletedForReceiver,
            imageUrl = model.imageUrl,
            replyToId = model.replyToId,
            replyToSender = model.replyToSender,
            replyToText = model.replyToText,
            reaction = model.reaction
        )
    }
}

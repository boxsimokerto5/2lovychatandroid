package com.example.data.local

import android.content.Context
import android.util.Log
import com.example.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository lokal untuk mengelola penyimpanan pesan obrolan di Room SQLite.
 * Menjamin riwayat pesan tersimpan permanen di HP pengguna sehingga membuka ruang
 * chat menjadi instan (0 detik) dan tidak membebani kuota bandwidth Supabase.
 */
class LocalChatRepository(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val chatMessageDao = db.chatMessageDao()

    companion object {
        private const val TAG = "LocalChatRepo"

        @Volatile
        private var INSTANCE: LocalChatRepository? = null

        fun getInstance(context: Context): LocalChatRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = LocalChatRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun getMessagesForConversation(conversationId: String): List<ChatMessage> = withContext(Dispatchers.IO) {
        try {
            chatMessageDao.getMessagesList(conversationId).map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "Gagal memuat pesan lokal untuk $conversationId", e)
            emptyList()
        }
    }

    suspend fun getAllMessages(): List<ChatMessage> = withContext(Dispatchers.IO) {
        try {
            chatMessageDao.getAllMessagesList().map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "Gagal memuat semua pesan lokal", e)
            emptyList()
        }
    }

    suspend fun getLatestTimestamp(conversationId: String): Long = withContext(Dispatchers.IO) {
        try {
            chatMessageDao.getLatestTimestamp(conversationId) ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    suspend fun saveMessage(message: ChatMessage) = withContext(Dispatchers.IO) {
        try {
            chatMessageDao.insertMessage(ChatMessageEntity.fromDomain(message))
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menyimpan pesan lokal ${message.id}", e)
        }
    }

    suspend fun saveMessages(messages: List<ChatMessage>) = withContext(Dispatchers.IO) {
        if (messages.isEmpty()) return@withContext
        try {
            chatMessageDao.insertMessages(messages.map { ChatMessageEntity.fromDomain(it) })
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menyimpan batch pesan lokal", e)
        }
    }

    suspend fun markDeletedForSender(messageId: String) = withContext(Dispatchers.IO) {
        try {
            chatMessageDao.markDeletedForSender(messageId)
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menandai pesan terhapus untuk sender $messageId", e)
        }
    }

    suspend fun markIncomingMessagesAsRead(conversationId: String) = withContext(Dispatchers.IO) {
        try {
            chatMessageDao.markIncomingMessagesAsRead(conversationId)
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menandai pesan terbaca untuk $conversationId", e)
        }
    }

    suspend fun markMessageAsRead(messageId: String) = withContext(Dispatchers.IO) {
        try {
            chatMessageDao.markMessageAsRead(messageId)
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menandai pesan terbaca untuk $messageId", e)
        }
    }

    suspend fun deleteMessage(messageId: String) = withContext(Dispatchers.IO) {
        try {
            chatMessageDao.deleteMessageById(messageId)
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menghapus pesan lokal $messageId", e)
        }
    }

    suspend fun clearConversation(conversationId: String) = withContext(Dispatchers.IO) {
        try {
            chatMessageDao.deleteMessagesForConversation(conversationId)
        } catch (e: Exception) {
            Log.e(TAG, "Gagal menghapus percakapan lokal $conversationId", e)
        }
    }
}

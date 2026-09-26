package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Query("SELECT * FROM local_chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesFlow(conversationId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM local_chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    suspend fun getMessagesList(conversationId: String): List<ChatMessageEntity>

    @Query("SELECT * FROM local_chat_messages ORDER BY timestamp ASC")
    suspend fun getAllMessagesList(): List<ChatMessageEntity>

    @Query("SELECT MAX(timestamp) FROM local_chat_messages WHERE conversationId = :conversationId")
    suspend fun getLatestTimestamp(conversationId: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("UPDATE local_chat_messages SET isRead = 1 WHERE conversationId = :conversationId AND isFromMe = 1")
    suspend fun markMessagesAsReadForConversation(conversationId: String)

    @Query("UPDATE local_chat_messages SET deletedForSender = 1 WHERE id = :messageId")
    suspend fun markDeletedForSender(messageId: String)

    @Query("UPDATE local_chat_messages SET isRead = 1 WHERE conversationId = :conversationId AND isFromMe = 0")
    suspend fun markIncomingMessagesAsRead(conversationId: String)

    @Query("UPDATE local_chat_messages SET isRead = 1 WHERE id = :messageId")
    suspend fun markMessageAsRead(messageId: String)

    @Query("DELETE FROM local_chat_messages WHERE id = :messageId")
    suspend fun deleteMessageById(messageId: String)

    @Query("DELETE FROM local_chat_messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesForConversation(conversationId: String)

    @Query("DELETE FROM local_chat_messages WHERE text LIKE '%Salam kenal dari fitur Teman Sekitar%'")
    suspend fun deleteAutomatedGreetings()

    @Query("DELETE FROM local_chat_messages")
    suspend fun clearAll()
}

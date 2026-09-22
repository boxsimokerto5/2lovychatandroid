package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatFriendDao {

    @Query("SELECT * FROM chat_friends ORDER BY lastChattedAt DESC")
    fun getAllFriendsFlow(): Flow<List<ChatFriendEntity>>

    @Query("SELECT * FROM chat_friends ORDER BY lastChattedAt DESC")
    suspend fun getAllFriends(): List<ChatFriendEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFriend(friend: ChatFriendEntity)

    @Query("DELETE FROM chat_friends WHERE id = :id")
    suspend fun deleteFriendById(id: String)

    @Query("DELETE FROM chat_friends WHERE id = :id OR name = :name")
    suspend fun deleteFriend(id: String, name: String)

    @Query("DELETE FROM chat_friends")
    suspend fun deleteAllFriends()

    @Query("DELETE FROM chat_friends WHERE id LIKE 'u%' OR name IN ('Siti Rahma', 'Rian Pratama', 'Nadia Putri', 'Dimas Anggara', 'Alya Zahra', 'Pengguna lovy', 'Rania Putri', 'Pengguna Lovy')")
    suspend fun deleteDummyFriends()

    @Query("SELECT COUNT(*) FROM chat_friends")
    suspend fun getFriendsCount(): Int
}

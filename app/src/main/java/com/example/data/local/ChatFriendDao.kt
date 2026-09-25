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

    @Query("UPDATE chat_friends SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("DELETE FROM chat_friends WHERE id = :id")
    suspend fun deleteFriendById(id: String)

    @Query("DELETE FROM chat_friends WHERE id = :id OR name = :name")
    suspend fun deleteFriend(id: String, name: String)

    @Query("DELETE FROM chat_friends")
    suspend fun deleteAllFriends()

    @Query("DELETE FROM chat_friends WHERE id LIKE 'u%' OR trim(name) = '' OR name IS NULL OR lower(name) IN ('siti rahma', 'rian pratama', 'nadia putri', 'dimas anggara', 'alya zahra', 'pengguna lovy', 'rania putri', 'clara monica', 'dimas danendra', 'clarissa aurelia', 'salma salsabil', 'tanpa nama', 'user tak bernama', 'pengguna')")
    suspend fun deleteDummyFriends()

    @Query("DELETE FROM chat_friends WHERE (trim(:myId) != '' AND id = :myId) OR (trim(:myName) != '' AND lower(trim(name)) = lower(trim(:myName))) OR (trim(:myDisplayName) != '' AND lower(trim(name)) = lower(trim(:myDisplayName))) OR (trim(:username) != '' AND lower(trim(name)) = lower(trim(:username))) OR id = 'me' OR id = 'current_user'")
    suspend fun deleteSelfFriend(myId: String, myName: String, myDisplayName: String, username: String)

    @Query("SELECT COUNT(*) FROM chat_friends")
    suspend fun getFriendsCount(): Int
}

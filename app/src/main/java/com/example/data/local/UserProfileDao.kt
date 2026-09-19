package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for UserProfile operations in Room.
 */
@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profiles WHERE id = :id LIMIT 1")
    fun getUserProfileFlow(id: String = "current_user"): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE id = :id LIMIT 1")
    suspend fun getUserProfile(id: String = "current_user"): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("DELETE FROM user_profiles WHERE id = :id")
    suspend fun deleteProfileById(id: String)
}

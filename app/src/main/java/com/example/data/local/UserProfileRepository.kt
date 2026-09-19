package com.example.data.local

import com.example.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository pattern isolating UserProfile local Room storage operations.
 */
class UserProfileRepository(private val userProfileDao: UserProfileDao) {

    val currentProfile: Flow<UserProfile?> = userProfileDao.getUserProfileFlow()

    suspend fun getProfile(id: String = "current_user"): UserProfile? {
        return userProfileDao.getUserProfile(id)
    }

    suspend fun saveProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(profile)
    }

    suspend fun deleteProfile(id: String = "current_user") {
        userProfileDao.deleteProfileById(id)
    }
}

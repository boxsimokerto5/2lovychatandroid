package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * UserProfile data model representing a user's local profile information.
 * Persisted in the local SQLite/Room database.
 */
@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey
    val id: String = "current_user",
    val displayName: String = "Pengguna Lovy",
    val bio: String = "Menjelajahi dunia dan mencari teman baru di Lovy Chat ✨",
    val profilePicture: String? = null,
    val email: String? = null,
    val lovyId: String = "lovy_889214",
    val city: String = "Jakarta Selatan",
    val gender: String = "FEMALE",
    val age: Int = 22,
    val updatedAt: Long = System.currentTimeMillis(),
    val lastActiveAt: Long = System.currentTimeMillis()
)

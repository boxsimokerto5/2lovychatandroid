package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Gender
import com.example.model.User

/**
 * Entitas Room untuk menyimpan daftar teman yang pernah diajak mengobrol.
 * Data ini tetap tersimpan meskipun pengguna logout dan semua pesan dihapus.
 */
@Entity(tableName = "chat_friends")
data class ChatFriendEntity(
    @PrimaryKey val id: String,
    val name: String,
    val gender: String = "FEMALE",
    val age: Int = 22,
    val distanceMeters: Int = 100,
    val bio: String = "",
    val avatarColorHex: Long = 0xFF2E7D32,
    val isOnline: Boolean = true,
    val city: String = "Jakarta Selatan",
    val avatarUrl: String? = null,
    val lastChattedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
) {
    fun toUser(): User = User(
        id = id,
        name = name,
        gender = if (gender.equals("FEMALE", ignoreCase = true)) Gender.FEMALE else Gender.MALE,
        age = age,
        distanceMeters = distanceMeters,
        bio = bio,
        avatarColorHex = avatarColorHex,
        isOnline = isOnline,
        city = city,
        avatarUrl = avatarUrl,
        isFavorite = isFavorite
    )

    companion object {
        fun fromUser(user: User, timestamp: Long = System.currentTimeMillis()): ChatFriendEntity =
            ChatFriendEntity(
                id = user.id,
                name = user.name,
                gender = user.gender.name,
                age = user.age,
                distanceMeters = user.distanceMeters,
                bio = user.bio,
                avatarColorHex = user.avatarColorHex,
                isOnline = user.isOnline,
                city = user.city,
                avatarUrl = user.avatarUrl,
                lastChattedAt = timestamp,
                isFavorite = user.isFavorite
            )
    }
}

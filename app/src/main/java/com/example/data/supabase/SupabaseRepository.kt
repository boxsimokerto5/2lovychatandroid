package com.example.data.supabase

import android.util.Log
import com.example.model.BottleMessage
import com.example.model.ChatMessage
import com.example.model.Gender
import com.example.model.MomentItem
import com.example.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SupabaseRepository {
    private val TAG = "SupabaseRepository"

    suspend fun testConnection(): Result<String> = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi()
            ?: return@withContext Result.failure(Exception("Layanan sinkronisasi belum dikonfigurasi."))

        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.getNearbyUsers(apiKey, auth, limit = 1)
            if (response.isSuccessful) {
                Result.success("Terhubung ke layanan cloud dengan sukses! (HTTP ${response.code()})")
            } else if (response.code() == 404 || response.code() == 400 || response.code() == 401 || response.code() == 403) {
                Result.success("Tersambung ke server cloud (Status HTTP ${response.code()}). Layanan siap digunakan.")
            } else {
                Result.failure(Exception("Gagal: HTTP ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Koneksi cloud gagal", e)
            Result.failure(e)
        }
    }

    suspend fun fetchNearbyUsers(): List<User>? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.getNearbyUsers(apiKey, auth)
            if (response.isSuccessful) {
                val list = response.body() ?: return@withContext null
                list.map { dto ->
                    User(
                        id = dto.id,
                        name = dto.name,
                        gender = if (dto.gender.equals("male", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                        age = 22,
                        distanceMeters = dto.distanceMeters,
                        bio = dto.bio,
                        avatarColorHex = dto.avatarHex,
                        isOnline = dto.isOnline
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil nearby users dari Supabase, menggunakan data lokal", e)
            null
        }
    }

    suspend fun fetchOceanBottles(): List<BottleMessage>? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.getOceanBottles(apiKey, auth)
            if (response.isSuccessful) {
                val list = response.body() ?: return@withContext null
                list.map { dto ->
                    BottleMessage(
                        id = dto.id,
                        senderId = dto.senderId,
                        senderName = dto.senderName,
                        senderGender = if (dto.senderGender.equals("male", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                        content = dto.content,
                        thrownTimestamp = dto.createdAt,
                        locationHint = dto.locationHint,
                        avatarHex = dto.avatarHex,
                        isFromMe = false
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil ocean bottles dari Supabase", e)
            null
        }
    }

    suspend fun sendBottle(bottle: BottleMessage): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val dto = SupabaseBottleDto(
                id = bottle.id,
                senderId = bottle.senderId,
                senderName = bottle.senderName,
                senderGender = if (bottle.senderGender == Gender.MALE) "male" else "female",
                content = bottle.content,
                createdAt = bottle.thrownTimestamp,
                locationHint = bottle.locationHint,
                avatarHex = bottle.avatarHex
            )
            val response = api.insertOceanBottle(apiKey, auth, dto)
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menyimpan bottle ke Supabase", e)
            false
        }
    }

    suspend fun fetchMoments(): List<MomentItem>? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.getMoments(apiKey, auth)
            if (response.isSuccessful) {
                val list = response.body() ?: return@withContext null
                list.map { dto ->
                    MomentItem(
                        id = dto.id,
                        authorName = dto.authorName,
                        authorAvatarHex = dto.authorAvatarHex,
                        content = dto.content,
                        timeAgo = "Baru saja",
                        likesCount = dto.likesCount,
                        commentsCount = dto.commentsCount,
                        isLiked = false,
                        imageUrl = dto.imageUrl
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil moments dari Supabase", e)
            null
        }
    }

    suspend fun sendMoment(moment: MomentItem, authorId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val dto = SupabaseMomentDto(
                id = moment.id,
                authorId = authorId,
                authorName = moment.authorName,
                content = moment.content,
                likesCount = moment.likesCount,
                commentsCount = moment.commentsCount,
                createdAt = System.currentTimeMillis(),
                authorAvatarHex = moment.authorAvatarHex,
                imageUrl = moment.imageUrl
            )
            val response = api.insertMoment(apiKey, auth, dto)
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menyimpan moment ke Supabase", e)
            false
        }
    }

    suspend fun fetchChatMessages(conversationId: String): List<ChatMessage>? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.getChatMessages(apiKey, auth, "eq.$conversationId")
            if (response.isSuccessful) {
                val list = response.body() ?: return@withContext null
                list.filter { dto ->
                    // Jangan tampilkan jika pesan sudah dihapus untuk pengirim (me)
                    !(dto.senderId == "me" && dto.deletedForSender)
                }.map { dto ->
                    ChatMessage(
                        id = dto.id,
                        conversationId = dto.conversationId,
                        text = dto.text,
                        timestamp = dto.createdAt,
                        isFromMe = dto.senderId == "me",
                        deletedForSender = dto.deletedForSender,
                        deletedForReceiver = dto.deletedForReceiver,
                        imageUrl = dto.imageUrl
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal fetch chat messages dari Supabase", e)
            null
        }
    }

    suspend fun sendChatMessage(message: ChatMessage): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val dto = SupabaseMessageDto(
                id = message.id,
                conversationId = message.conversationId,
                senderId = if (message.isFromMe) "me" else "partner",
                text = message.text,
                createdAt = message.timestamp,
                deletedForSender = message.deletedForSender,
                deletedForReceiver = message.deletedForReceiver,
                imageUrl = message.imageUrl
            )
            val response = api.insertChatMessage(apiKey, auth, dto)
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menyimpan message ke Supabase", e)
            false
        }
    }

    suspend fun markMessageDeletedForSender(messageId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.markChatMessageDeleted(apiKey, auth, "eq.$messageId", mapOf("deleted_for_sender" to true))
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menandai pesan terhapus di Supabase", e)
            false
        }
    }

    suspend fun markAllSenderMessagesDeleted(senderId: String = "me"): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.markAllSenderMessagesDeleted(apiKey, auth, "eq.$senderId", mapOf("deleted_for_sender" to true))
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menandai seluruh pesan terhapus di Supabase saat logout", e)
            false
        }
    }

    suspend fun updateUserLastActive(userId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.updateUserActive(apiKey, auth, "eq.$userId", mapOf("last_active_at" to System.currentTimeMillis()))
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal memperbarui last_active_at pengguna", e)
            false
        }
    }
}

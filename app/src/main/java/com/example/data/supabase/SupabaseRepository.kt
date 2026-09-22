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
            when (response.code()) {
                in 200..299 -> {
                    Result.success("Terhubung ke Supabase dengan sukses! (HTTP ${response.code()})")
                }
                401 -> {
                    Result.failure(Exception("Autentikasi gagal (HTTP 401). Periksa kembali token SUPABASE_ANON_KEY Anda."))
                }
                403 -> {
                    Result.failure(Exception("Akses ditolak (HTTP 403). Pastikan RLS Policy tabel diaktifkan di Supabase."))
                }
                404 -> {
                    Result.failure(Exception("Tabel 'nearby_users' belum ada (HTTP 404). Silakan salin & jalankan skrip SQL di SQL Editor Supabase."))
                }
                else -> {
                    Result.failure(Exception("Gagal: HTTP ${response.code()} - ${response.message()}"))
                }
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
                        distanceMeters = dto.distanceMeters ?: 100,
                        bio = dto.bio ?: "",
                        avatarColorHex = dto.avatarHex ?: 0xFF2E7D32,
                        isOnline = dto.isOnline ?: true
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
                        locationHint = dto.locationHint ?: "Lautan Nusantara",
                        avatarHex = dto.avatarHex ?: 0xFF00838F,
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
                        authorAvatarHex = dto.authorAvatarHex ?: 0xFFFB8C00,
                        content = dto.content,
                        timeAgo = "Baru saja",
                        likesCount = dto.likesCount ?: 0,
                        commentsCount = dto.commentsCount ?: 0,
                        isLiked = false,
                        imageUrl = dto.imageUrl,
                        authorId = dto.authorId
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

    suspend fun deleteMoment(momentId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.deleteMoment(apiKey, auth, "eq.$momentId")
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menghapus moment dari Supabase", e)
            false
        }
    }

    suspend fun fetchChatMessages(
        conversationId: String,
        currentUserId: String = "",
        partnerId: String = "",
        sinceTimestamp: Long = 0L
    ): List<ChatMessage>? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            // Dukung conversation_id 2 arah simetris maupun legacy format
            val filter = if (partnerId.isNotBlank() && currentUserId.isNotBlank()) {
                val legacy1 = "conv_$partnerId"
                val legacy2 = "conv_$currentUserId"
                if (conversationId != legacy1 && conversationId != legacy2) {
                    "in.($conversationId,$legacy1,$legacy2)"
                } else {
                    "in.($conversationId)"
                }
            } else {
                "eq.$conversationId"
            }

            var response = if (sinceTimestamp > 0L) {
                api.getDeltaChatMessages(apiKey, auth, filter, "gt.$sinceTimestamp")
            } else {
                api.getChatMessages(apiKey, auth, filter)
            }

            if (!response.isSuccessful) {
                response = if (sinceTimestamp > 0L) {
                    api.getDeltaChatMessages(apiKey, auth, "eq.$conversationId", "gt.$sinceTimestamp")
                } else {
                    api.getChatMessages(apiKey, auth, "eq.$conversationId")
                }
            }

            if (response.isSuccessful) {
                val list = response.body() ?: return@withContext null
                list.filter { dto ->
                    val isSentByMe = isSenderMe(dto.senderId, dto.receiverId, currentUserId)
                    val delForSender = dto.deletedForSender ?: false
                    val delForReceiver = dto.deletedForReceiver ?: false
                    if (isSentByMe && delForSender) return@filter false
                    if (!isSentByMe && delForReceiver) return@filter false
                    true
                }.map { dto ->
                    ChatMessage(
                        id = dto.id,
                        conversationId = conversationId,
                        text = dto.text,
                        timestamp = dto.createdAt,
                        isFromMe = isSenderMe(dto.senderId, dto.receiverId, currentUserId),
                        isRead = dto.isRead ?: false,
                        deletedForSender = dto.deletedForSender ?: false,
                        deletedForReceiver = dto.deletedForReceiver ?: false,
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

    private fun isSenderMe(senderId: String, receiverId: String?, currentUserId: String): Boolean {
        if (currentUserId.isNotBlank()) {
            if (senderId.equals(currentUserId, ignoreCase = true)) return true
            if (receiverId != null && receiverId.equals(currentUserId, ignoreCase = true)) return false
        }
        if (senderId.equals("me", ignoreCase = true)) {
            return receiverId == null || !receiverId.equals(currentUserId, ignoreCase = true)
        }
        return false
    }

    suspend fun sendChatMessage(
        message: ChatMessage,
        senderId: String = "me",
        receiverId: String? = null
    ): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val dto = SupabaseMessageDto(
                id = message.id,
                conversationId = message.conversationId,
                senderId = senderId,
                receiverId = receiverId,
                text = message.text,
                createdAt = message.timestamp,
                deletedForSender = message.deletedForSender,
                deletedForReceiver = message.deletedForReceiver,
                imageUrl = message.imageUrl,
                isRead = message.isRead
            )
            val response = api.insertChatMessage(apiKey, auth, dto)
            if (response.isSuccessful) {
                return@withContext true
            }

            // Fallback jika database Supabase versi lama belum memiliki kolom receiver_id/image_url/deleted flags
            if (!response.isSuccessful) {
                val coreDto = SupabaseMessageDto(
                    id = message.id,
                    conversationId = message.conversationId,
                    senderId = senderId,
                    receiverId = null,
                    text = message.text,
                    createdAt = message.timestamp,
                    deletedForSender = null,
                    deletedForReceiver = null,
                    imageUrl = null
                )
                val retryResp = api.insertChatMessage(apiKey, auth, coreDto)
                if (retryResp.isSuccessful) {
                    return@withContext true
                }
            }
            false
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menyimpan message ke Supabase", e)
            false
        }
    }

    suspend fun fetchRecentMessagesForUser(userId: String): List<SupabaseMessageDto>? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            // Coba query komprehensif (sender_id, receiver_id, atau percakapan terkait)
            val orResp = api.getRecentMessagesOr(
                apiKey,
                auth,
                "(sender_id.eq.$userId,receiver_id.eq.$userId,conversation_id.ilike.%25$userId%25)"
            )
            if (orResp.isSuccessful && orResp.body() != null) {
                return@withContext orResp.body()
            }

            // Fallback 1: ilike dengan URL wildcard SQL %
            val ilikeResp = api.getRecentMessages(apiKey, auth, "ilike.%25$userId%25")
            if (ilikeResp.isSuccessful && ilikeResp.body() != null) {
                return@withContext ilikeResp.body()
            }

            // Fallback 2: format legacy
            val response = api.getRecentMessages(apiKey, auth, "like.*$userId*")
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil pesan terbaru pengguna", e)
            null
        }
    }

    suspend fun registerOrUpdateUser(
        id: String,
        name: String,
        gender: Gender,
        bio: String,
        avatarHex: Long = 0xFFFB8C00,
        avatarUrl: String? = null
    ): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val dto = SupabaseUserDto(
                id = id,
                name = name,
                gender = if (gender == Gender.MALE) "male" else "female",
                distanceMeters = 100,
                bio = bio,
                avatarHex = avatarHex,
                isOnline = true,
                lastActiveAt = System.currentTimeMillis(),
                avatarUrl = avatarUrl
            )
            val response = api.upsertNearbyUser(apiKey, auth, dto)
            if (response.isSuccessful) {
                return@withContext true
            }

            // Fallback jika database Supabase belum memiliki kolom last_active_at / avatar_url
            val coreDto = SupabaseUserDto(
                id = id,
                name = name,
                gender = if (gender == Gender.MALE) "male" else "female",
                distanceMeters = 100,
                bio = bio,
                avatarHex = avatarHex,
                isOnline = true,
                lastActiveAt = null,
                avatarUrl = null
            )
            val retry = api.upsertNearbyUser(apiKey, auth, coreDto)
            retry.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal upsert nearby_user di Supabase", e)
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

    suspend fun markMessagesAsRead(conversationId: String, senderId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.markMessagesAsRead(apiKey, auth, "eq.$conversationId", "eq.$senderId", mapOf("is_read" to true))
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menandai pesan terbaca di Supabase", e)
            false
        }
    }

    suspend fun markMessageReadById(messageId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.markMessageReadById(apiKey, auth, "eq.$messageId", mapOf("is_read" to true))
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menandai pesan $messageId terbaca di Supabase", e)
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

    suspend fun findAccountByUsername(username: String): SupabaseAccountDto? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()
        val normalized = username.trim().lowercase()

        try {
            val response = api.getAccountByUsername(apiKey, auth, "eq.$normalized")
            if (response.isSuccessful) {
                response.body()?.firstOrNull()
            } else {
                Log.d(TAG, "findAccountByUsername code=${response.code()} msg=${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "findAccountByUsername error: ${e.message}")
            null
        }
    }

    suspend fun findAccountByGoogle(googleEmail: String): SupabaseAccountDto? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()
        val normalized = googleEmail.trim().lowercase()

        try {
            val response = api.getAccountByGoogle(apiKey, auth, "eq.$normalized")
            if (response.isSuccessful) {
                response.body()?.firstOrNull()
            } else {
                Log.d(TAG, "findAccountByGoogle code=${response.code()} msg=${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "findAccountByGoogle error: ${e.message}")
            null
        }
    }

    suspend fun registerOrUpdateAccount(account: SupabaseAccountDto): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.upsertAccount(apiKey, auth, account)
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "registerOrUpdateAccount error: ${e.message}")
            false
        }
    }

    suspend fun updateAccountLoginTime(accountId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.updateAccountLoginTime(apiKey, auth, "eq.$accountId", mapOf("last_login_at" to System.currentTimeMillis()))
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "updateAccountLoginTime error: ${e.message}")
            false
        }
    }
}

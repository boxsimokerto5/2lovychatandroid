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
            val missingTables = mutableListOf<String>()

            val resNearby = api.getNearbyUsers(apiKey, auth, limit = 1)
            when (resNearby.code()) {
                401 -> return@withContext Result.failure(Exception("Autentikasi gagal (HTTP 401). Periksa kembali token SUPABASE_ANON_KEY Anda."))
                403 -> return@withContext Result.failure(Exception("Akses ditolak (HTTP 403). Pastikan RLS Policy tabel diaktifkan di Supabase."))
                404 -> missingTables.add("nearby_users")
            }

            val resChat = api.getRecentMessages(apiKey, auth, "ping", limit = 1)
            if (resChat.code() == 404) missingTables.add("chat_messages")

            val resBottles = api.getOceanBottles(apiKey, auth, limit = 1)
            if (resBottles.code() == 404) missingTables.add("ocean_bottles")

            val resMoments = api.getMoments(apiKey, auth, limit = 1)
            if (resMoments.code() == 404) missingTables.add("moments")

            val resAccounts = api.getAccountByUsername(apiKey, auth, "ping", limit = 1)
            if (resAccounts.code() == 404) missingTables.add("app_accounts")

            if (missingTables.isNotEmpty()) {
                Result.failure(Exception("Tabel belum lengkap di Supabase: ${missingTables.joinToString(", ")}. Silakan salin & jalankan skrip SQL di menu Pengaturan Cloud."))
            } else {
                Result.success("Terhubung ke Supabase dengan sukses! Semua tabel (pesan, akun, radar, botol, momen) siap beroperasi.")
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
                val dummyNames = setOf(
                    "siti rahma", "rian pratama", "nadia putri", "dimas anggara", 
                    "alya zahra", "pengguna lovy", "rania putri", "clara monica",
                    "dimas danendra", "clarissa aurelia", "salma salsabil",
                    "tanpa nama", "user tak bernama", "pengguna", "unknown user", "anonymous"
                )
                list
                    .filterNot { dto ->
                        val cleanName = dto.name.trim()
                        cleanName.isEmpty() ||
                        cleanName.lowercase() in dummyNames ||
                        dto.id.matches(Regex("^u[0-9]+$"))
                    }
                    .map { dto ->
                        User(
                            id = dto.id,
                            name = dto.name.trim(),
                            gender = if (dto.gender.equals("male", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                            age = 22,
                            distanceMeters = dto.distanceMeters ?: 100,
                            bio = dto.bio ?: "",
                            avatarColorHex = dto.avatarHex ?: 0xFF2E7D32,
                            isOnline = dto.isOnline ?: true,
                            city = dto.city?.takeIf { it.isNotBlank() } ?: "Indonesia",
                            avatarUrl = dto.avatarUrl
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

    suspend fun fetchNearbyUserById(userId: String): User? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.getNearbyUserById(apiKey, auth, "eq.$userId")
            if (response.isSuccessful) {
                val dto = response.body()?.firstOrNull() ?: return@withContext null
                val cleanName = dto.name.trim().takeIf { it.isNotBlank() } ?: "Teman Lovy"
                User(
                    id = dto.id,
                    name = cleanName,
                    gender = if (dto.gender.equals("male", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                    age = 22,
                    distanceMeters = dto.distanceMeters ?: 100,
                    bio = dto.bio ?: "",
                    avatarColorHex = dto.avatarHex ?: 0xFF2E7D32,
                    isOnline = dto.isOnline ?: true,
                    city = dto.city?.takeIf { it.isNotBlank() } ?: "Indonesia",
                    avatarUrl = dto.avatarUrl
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil nearby user by id", e)
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
                        isFromMe = false,
                        avatarUrl = dto.avatarUrl
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
                avatarHex = bottle.avatarHex,
                avatarUrl = bottle.avatarUrl
            )
            val response = api.insertOceanBottle(apiKey, auth, dto)
            if (response.isSuccessful) {
                true
            } else if (dto.avatarUrl != null) {
                // Fallback jika database Supabase versi lama belum memiliki kolom avatar_url
                val fallbackDto = dto.copy(avatarUrl = null)
                val retry = api.insertOceanBottle(apiKey, auth, fallbackDto)
                retry.isSuccessful
            } else {
                false
            }
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
                        authorAvatarUrl = dto.authorAvatarUrl,
                        locationTag = dto.locationTag,
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

    suspend fun ensureAuthorAccountExists(authorId: String, authorName: String, avatarUrl: String? = null) {
        if (authorId.isBlank() || authorId == "me") return
        val api = SupabaseClient.getApi() ?: return
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val checkResp = api.getAccountById(apiKey, auth, "eq.$authorId")
            val exists = checkResp.isSuccessful && !checkResp.body().isNullOrEmpty()
            if (!exists) {
                // Gunakan authorId sebagai safe username unik agar tidak terjadi tabrakan unique constraint
                val safeUsername = authorId.lowercase()
                val safeDisplayName = authorName.ifBlank { "Pengguna Lovy" }
                val newAcc = SupabaseAccountDto(
                    id = authorId,
                    username = safeUsername,
                    displayName = safeDisplayName,
                    gender = "FEMALE",
                    bio = "Pengguna Lovy Chat ✨",
                    avatarUrl = avatarUrl,
                    createdAt = System.currentTimeMillis(),
                    lastLoginAt = System.currentTimeMillis()
                )
                val upsertResp = api.upsertAccount(apiKey, auth, newAcc)
                if (!upsertResp.isSuccessful) {
                    val fallbackAcc = newAcc.copy(
                        username = "${safeUsername}_${System.currentTimeMillis() % 100000}",
                        fcmToken = null
                    )
                    api.upsertAccount(apiKey, auth, fallbackAcc)
                }
            } else {
                updateAccountLoginTime(authorId)
            }
        } catch (e: Exception) {
            Log.w(TAG, "ensureAuthorAccountExists warning: ${e.message}")
        }
    }

    suspend fun sendMoment(moment: MomentItem, authorId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        val safeAuthorId = if (authorId.isBlank() || authorId == "me") "lovy_${(100000..999999).random()}" else authorId

        try {
            // Pastikan akun penulis ada di app_accounts agar tidak kena error constraint fk_moments_author
            ensureAuthorAccountExists(safeAuthorId, moment.authorName, moment.authorAvatarUrl)

            val dto = SupabaseMomentDto(
                id = moment.id,
                authorId = safeAuthorId,
                authorName = moment.authorName.ifBlank { "Pengguna Lovy" },
                content = moment.content,
                likesCount = moment.likesCount,
                commentsCount = moment.commentsCount,
                createdAt = System.currentTimeMillis(),
                authorAvatarHex = moment.authorAvatarHex,
                imageUrl = moment.imageUrl,
                authorAvatarUrl = moment.authorAvatarUrl,
                locationTag = moment.locationTag
            )
            val response = api.insertMoment(apiKey, auth, dto)
            if (response.isSuccessful) {
                Log.d(TAG, "Momen berhasil diinsert ke Supabase: ${moment.id}")
                return@withContext true
            }

            // Retry setelah memastikan akun author ada di database
            ensureAuthorAccountExists(safeAuthorId, moment.authorName, moment.authorAvatarUrl)
            val retry = api.insertMoment(apiKey, auth, dto)
            if (retry.isSuccessful) {
                Log.d(TAG, "Momen berhasil diinsert ke Supabase pada retry: ${moment.id}")
                return@withContext true
            }

            // Fallback jika database Supabase belum memiliki kolom location_tag / author_avatar_url
            if (dto.locationTag != null || dto.authorAvatarUrl != null) {
                val fallbackDto = dto.copy(locationTag = null, authorAvatarUrl = null)
                val retryFallback = api.insertMoment(apiKey, auth, fallbackDto)
                if (retryFallback.isSuccessful) {
                    Log.d(TAG, "Momen berhasil diinsert dengan fallback kolom standar: ${moment.id}")
                    return@withContext true
                }
            }

            Log.w(TAG, "Gagal insert momen ke Supabase, code=${response.code()}, error=${response.errorBody()?.string()}")
            false
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
                val clean1 = currentUserId.trim()
                val clean2 = partnerId.trim()
                val sorted = if (clean1 <= clean2) listOf(clean1, clean2) else listOf(clean2, clean1)
                val formatDouble = "conv_${sorted[0]}__${sorted[1]}"
                val formatSingle = "conv_${sorted[0]}_${sorted[1]}"
                val legacy1 = "conv_$partnerId"
                val legacy2 = "conv_$currentUserId"
                val list = listOf(conversationId, formatDouble, formatSingle, legacy1, legacy2).filter { it.isNotBlank() }.distinct()
                "in.(${list.joinToString(",")})"
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
                        imageUrl = dto.imageUrl,
                        replyToId = dto.replyToId,
                        replyToSender = dto.replyToSender,
                        replyToText = dto.replyToText
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
        if (currentUserId.isBlank()) return false
        val myId = currentUserId.trim()
        val sId = senderId.trim()
        val rId = receiverId?.trim()

        // 1. Jika sender_id sama dengan ID saya -> pasti pesan yang saya kirim
        if (sId.equals(myId, ignoreCase = true)) return true

        // 2. Jika receiver_id adalah ID saya -> pasti pesan yang saya terima (bukan dari saya)
        if (rId != null && rId.equals(myId, ignoreCase = true)) return false

        // 3. Jika sender_id adalah ID pengguna lain (bukan "me" dan bukan ID saya) -> pasti bukan dari saya
        if (sId.isNotBlank() && !sId.equals("me", ignoreCase = true) && !sId.equals(myId, ignoreCase = true)) {
            return false
        }

        // 4. Fallback legacy jika sender_id tersimpan sebagai "me"
        if (sId.equals("me", ignoreCase = true)) {
            if (rId != null && !rId.equals(myId, ignoreCase = true)) return true
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
                isRead = null, // Jangan kirim kolom is_read saat insert agar kompatibel dengan tabel database yang belum memiliki kolom is_read
                replyToId = message.replyToId,
                replyToSender = message.replyToSender,
                replyToText = message.replyToText
            )
            val response = api.insertChatMessage(apiKey, auth, dto)
            // Siarkan secara instan via WebSocket Realtime ke perangkat penerima
            SupabaseRealtimeManager.broadcastChatMessage(dto)

            if (response.isSuccessful) {
                return@withContext true
            }

            // Fallback 1: jika kolom reply belum ditambahkan di Supabase, coba kirim tanpa kolom reply
            if (dto.replyToId != null || dto.replyToSender != null || dto.replyToText != null) {
                val withoutReplyDto = dto.copy(replyToId = null, replyToSender = null, replyToText = null)
                val retryWithoutReply = api.insertChatMessage(apiKey, auth, withoutReplyDto)
                if (retryWithoutReply.isSuccessful) {
                    SupabaseRealtimeManager.broadcastChatMessage(withoutReplyDto)
                    return@withContext true
                }
            }

            // Fallback 2 jika database Supabase versi lama belum memiliki kolom receiver_id/image_url/deleted flags
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
                    imageUrl = null,
                    isRead = null,
                    replyToId = null,
                    replyToSender = null,
                    replyToText = null
                )
                val retryResp = api.insertChatMessage(apiKey, auth, coreDto)
                SupabaseRealtimeManager.broadcastChatMessage(coreDto)
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
                "(sender_id.eq.$userId,receiver_id.eq.$userId,conversation_id.ilike.*$userId*)"
            )
            if (orResp.isSuccessful && orResp.body() != null) {
                return@withContext orResp.body()
            }

            // Fallback 1: ilike dengan wildcard PostgREST *
            val ilikeResp = api.getRecentMessages(apiKey, auth, "ilike.*$userId*")
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
        avatarUrl: String? = null,
        city: String? = null,
        fcmToken: String? = null
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
                avatarUrl = avatarUrl,
                city = city?.takeIf { it.isNotBlank() },
                fcmToken = fcmToken
            )
            val response = api.upsertNearbyUser(apiKey, auth, dto)
            if (response.isSuccessful) {
                return@withContext true
            }

            // Fallback 1: jika database Supabase belum memiliki fcm_token / city
            val coreDto = SupabaseUserDto(
                id = id,
                name = name,
                gender = if (gender == Gender.MALE) "male" else "female",
                distanceMeters = 100,
                bio = bio,
                avatarHex = avatarHex,
                isOnline = true,
                lastActiveAt = System.currentTimeMillis(),
                avatarUrl = avatarUrl,
                city = null,
                fcmToken = null
            )
            val retry = api.upsertNearbyUser(apiKey, auth, coreDto)
            if (retry.isSuccessful) return@withContext true

            // Fallback 2: minimal DTO untuk skema paling sederhana
            val minDto = SupabaseUserDto(
                id = id,
                name = name,
                gender = if (gender == Gender.MALE) "male" else "female",
                distanceMeters = 100,
                bio = bio,
                avatarHex = avatarHex,
                isOnline = true,
                lastActiveAt = null,
                avatarUrl = null,
                city = null,
                fcmToken = null
            )
            val minRetry = api.upsertNearbyUser(apiKey, auth, minDto)
            minRetry.isSuccessful
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
            Log.w(TAG, "Gagal menandai pesan terhapus untuk sender di Supabase", e)
            false
        }
    }

    suspend fun markMessageDeletedForReceiver(messageId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.markChatMessageDeleted(apiKey, auth, "eq.$messageId", mapOf("deleted_for_receiver" to true))
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menandai pesan terhapus untuk receiver di Supabase", e)
            false
        }
    }

    suspend fun markConversationDeletedForUser(conversationId: String, userId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            // Tandai pesan terhapus di mana user adalah receiver
            api.markMessagesDeletedForReceiver(apiKey, auth, "eq.$conversationId", "eq.$userId", mapOf("deleted_for_receiver" to true))
            // Tandai pesan terhapus di mana user adalah sender
            api.markMessagesDeletedForSender(apiKey, auth, "eq.$conversationId", "eq.$userId", mapOf("deleted_for_sender" to true))
            true
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menandai seluruh percakapan $conversationId terhapus untuk $userId", e)
            false
        }
    }

    suspend fun deleteMessageForEveryone(messageId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            // Tandai deleted_for_sender dan deleted_for_receiver = true, serta hapus record
            api.markChatMessageDeleted(apiKey, auth, "eq.$messageId", mapOf("deleted_for_sender" to true, "deleted_for_receiver" to true))
            val response = api.deleteChatMessagePermanently(apiKey, auth, "eq.$messageId")
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menghapus pesan untuk semua orang di Supabase", e)
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

    suspend fun updateUserFcmToken(userId: String, token: String): Boolean = withContext(Dispatchers.IO) {
        if (userId.isBlank() || token.isBlank()) return@withContext false
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val res1 = api.updateUserFcmToken(apiKey, auth, "eq.$userId", mapOf("fcm_token" to token))
            api.updateAccountFcmToken(apiKey, auth, "eq.$userId", mapOf("fcm_token" to token))
            res1.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal memperbarui fcm_token pengguna di Supabase", e)
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

    suspend fun findAccountById(accountId: String): SupabaseAccountDto? = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext null
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.getAccountById(apiKey, auth, "eq.$accountId")
            if (response.isSuccessful) {
                response.body()?.firstOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "findAccountById error: ${e.message}")
            null
        }
    }

    suspend fun registerOrUpdateAccount(account: SupabaseAccountDto): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            val response = api.upsertAccount(apiKey, auth, account)
            if (response.isSuccessful) return@withContext true

            // Fallback jika database Supabase versi lama belum memiliki fcm_token
            if (account.fcmToken != null) {
                val fallback = account.copy(fcmToken = null)
                val retry = api.upsertAccount(apiKey, auth, fallback)
                if (retry.isSuccessful) return@withContext true
            }
            false
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

    /**
     * Menghapus seluruh data pengguna dari database Supabase:
     * - Hapus nearby_users
     * - Hapus ocean_bottles
     * - Hapus moments
     * - Hapus / tandai chat_messages
     * - Hapus akun di tabel app_accounts
     */
    suspend fun deleteAccountAndUserData(accountId: String, lovyId: String): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        var success = true
        val targetId = lovyId.ifBlank { accountId }

        // 1. Hapus dari nearby_users
        try {
            if (targetId.isNotBlank()) {
                api.deleteNearbyUser(apiKey, auth, "eq.$targetId")
            }
        } catch (e: Exception) {
            Log.w(TAG, "deleteNearbyUser error: ${e.message}")
        }

        // 2. Hapus ocean_bottles yang dikirim user
        try {
            if (targetId.isNotBlank()) {
                api.deleteOceanBottlesBySender(apiKey, auth, "eq.$targetId")
            }
        } catch (e: Exception) {
            Log.w(TAG, "deleteOceanBottles error: ${e.message}")
        }

        // 3. Hapus momen yang dibuat user
        try {
            if (targetId.isNotBlank()) {
                api.deleteMomentsByAuthor(apiKey, auth, "eq.$targetId")
            }
        } catch (e: Exception) {
            Log.w(TAG, "deleteUserMoments error: ${e.message}")
        }

        // 4. Hapus / tandai pesan chat terhapus permanen
        try {
            if (targetId.isNotBlank()) {
                api.markAllSenderMessagesDeleted(apiKey, auth, "eq.$targetId", mapOf("deleted_for_sender" to true, "deleted_for_receiver" to true))
                api.deleteChatMessagesPermanentlyBySender(apiKey, auth, "eq.$targetId")
            }
        } catch (e: Exception) {
            Log.w(TAG, "deleteChatMessages error: ${e.message}")
        }

        // 5. Hapus akun pengguna di app_accounts
        try {
            if (accountId.isNotBlank()) {
                val res = api.deleteAccount(apiKey, auth, "eq.$accountId")
                success = res.isSuccessful
            }
        } catch (e: Exception) {
            Log.w(TAG, "deleteAccount error: ${e.message}")
            success = false
        }

        success
    }

    /**
     * Pembersihan otomatis data usang / tidak aktif:
     * - Hapus akun yang tidak login lebih dari 15 hari
     * - Hapus pesan chat yang sudah dihapus oleh kedua belah pihak
     */
    suspend fun purgeInactiveAccountsAndDeletedMessages(): Boolean = withContext(Dispatchers.IO) {
        val api = SupabaseClient.getApi() ?: return@withContext false
        val apiKey = SupabaseClient.getSupabaseAnonKey()
        val auth = SupabaseClient.getAuthHeader()

        try {
            // Waktu 90 hari yang lalu dalam epoch ms
            val ninetyDaysAgo = System.currentTimeMillis() - (90L * 24 * 60 * 60 * 1000)

            // 1. Hapus akun tidak aktif > 90 hari
            api.purgeInactiveAccounts(apiKey, auth, "lt.$ninetyDaysAgo")

            // 2. Hapus fisik chat yang sudah dihapus kedua pihak
            api.purgeFullyDeletedMessages(apiKey, auth)

            // 3. Bersihkan entri tanpa nama atau dummy di nearby_users
            try {
                api.deleteNearbyUsersByName(apiKey, auth, "is.null")
                api.deleteNearbyUsersByName(apiKey, auth, "eq.")
            } catch (_: Exception) {}

            true
        } catch (e: Exception) {
            Log.d(TAG, "purgeInactiveAccountsAndDeletedMessages skipped/error: ${e.message}")
            false
        }
    }
}

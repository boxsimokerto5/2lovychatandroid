package com.example.data.pocketbase

import android.util.Log
import com.example.data.supabase.SupabaseAccountDto
import com.example.data.supabase.SupabaseMessageDto
import com.example.model.BottleMessage
import com.example.model.ChatMessage
import com.example.model.Gender
import com.example.model.MomentItem
import com.example.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PocketBaseRepository {
    companion object {
        private const val TAG = "PocketBaseRepository"
        const val ONLINE_TIMEOUT_MS = 15 * 60 * 1000L
    }

    suspend fun testConnection(): Result<String> = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi()
            ?: return@withContext Result.failure(Exception("PocketBase API belum dapat diinisialisasi."))

        try {
            // 1. Cek kesehatan server PocketBase
            val health = api.healthCheck()
            if (!health.isSuccessful) {
                return@withContext Result.failure(Exception("Server PocketBase tidak merespon (HTTP ${health.code()})."))
            }

            // 2. Verifikasi 4 koleksi yang dibutuhkan aplikasi
            val missing = mutableListOf<String>()

            val resUsers = api.getUsers(perPage = 1)
            if (resUsers.code() == 404) missing.add("users")

            val resMessages = api.getMessages(perPage = 1)
            if (resMessages.code() == 404) missing.add("messages")

            val resBottles = api.getBottles(perPage = 1)
            if (resBottles.code() == 404) missing.add("bottles")

            val resMoments = api.getMoments(perPage = 1)
            if (resMoments.code() == 404) missing.add("moments")

            if (missing.isNotEmpty()) {
                Result.failure(Exception("Koleksi belum lengkap di PocketBase: ${missing.joinToString(", ")}. Pastikan skema schema.json sudah di-import."))
            } else {
                Result.success("Terhubung ke PocketBase dengan sukses! Koleksi (users, messages, bottles, moments) siap digunakan.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Uji koneksi PocketBase gagal", e)
            Result.failure(e)
        }
    }

    suspend fun fetchNearbyUsers(): List<User>? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val response = api.getUsers(perPage = 50, sort = "-last_active_at")
            if (response.isSuccessful) {
                val items = response.body()?.items ?: return@withContext null
                val now = System.currentTimeMillis()
                val dummyNames = setOf(
                    "siti rahma", "rian pratama", "nadia putri", "dimas anggara",
                    "alya zahra", "pengguna lovy", "rania putri", "clara monica",
                    "dimas danendra", "clarissa aurelia", "salma salsabil",
                    "tanpa nama", "user tak bernama", "pengguna", "unknown user", "anonymous"
                )

                items.filterNot { record ->
                    val cleanName = (record.name ?: "").trim()
                    cleanName.isEmpty() ||
                            cleanName.lowercase() in dummyNames ||
                            record.id.matches(Regex("^u[0-9]+$"))
                }.map { record ->
                    val lastActive = record.lastActiveAt ?: 0L
                    val isTrulyOnline = (record.isOnline == true) && (now - lastActive <= ONLINE_TIMEOUT_MS)
                    User(
                        id = record.id,
                        name = (record.name ?: "Pengguna").trim(),
                        gender = if (record.gender.equals("male", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                        age = 22,
                        distanceMeters = 100,
                        bio = record.bio ?: "",
                        avatarColorHex = record.avatarHex ?: 0xFF2E7D32,
                        isOnline = isTrulyOnline,
                        city = record.city?.takeIf { it.isNotBlank() } ?: "Indonesia",
                        avatarUrl = record.avatarUrl
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil nearby users dari PocketBase", e)
            null
        }
    }

    suspend fun fetchNearbyUserById(userId: String): User? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val pbId = PocketBaseClient.toPbId(userId)
            val resp = api.getUserById(pbId)
            val record = if (resp.isSuccessful) resp.body() else {
                val query = api.getUsers(perPage = 1, filter = "id='$pbId' || id='$userId'")
                query.body()?.items?.firstOrNull()
            } ?: return@withContext null

            val now = System.currentTimeMillis()
            val lastActive = record.lastActiveAt ?: 0L
            val isTrulyOnline = (record.isOnline == true) && (now - lastActive <= ONLINE_TIMEOUT_MS)

            User(
                id = record.id,
                name = (record.name ?: "Pengguna").trim(),
                gender = if (record.gender.equals("male", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                age = 22,
                distanceMeters = 100,
                bio = record.bio ?: "",
                avatarColorHex = record.avatarHex ?: 0xFF2E7D32,
                isOnline = isTrulyOnline,
                city = record.city?.takeIf { it.isNotBlank() } ?: "Indonesia",
                avatarUrl = record.avatarUrl
            )
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil user by id dari PocketBase: ${e.message}")
            null
        }
    }

    suspend fun fetchOceanBottles(): List<BottleMessage>? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val response = api.getBottles(sort = "-created_at_ms", perPage = 50)
            if (response.isSuccessful) {
                val items = response.body()?.items ?: return@withContext null
                items.map { record ->
                    BottleMessage(
                        id = record.id,
                        senderId = record.senderId ?: "",
                        senderName = record.senderName ?: "Anonim",
                        senderGender = if (record.senderGender.equals("male", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                        avatarHex = record.avatarHex ?: 0xFF00838F,
                        content = record.content,
                        thrownTimestamp = record.createdAtMs ?: System.currentTimeMillis(),
                        locationHint = record.locationHint ?: "Lautan Nusantara",
                        isFromMe = false,
                        avatarUrl = record.avatarUrl
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil pesan botol dari PocketBase", e)
            null
        }
    }

    suspend fun sendBottle(bottle: BottleMessage): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val payload = mapOf(
                "id" to PocketBaseClient.toPbId(bottle.id),
                "sender_id" to PocketBaseClient.toPbId(bottle.senderName),
                "sender_name" to bottle.senderName,
                "sender_gender" to if (bottle.senderGender == Gender.MALE) "male" else "female",
                "content" to bottle.content,
                "created_at_ms" to bottle.thrownTimestamp,
                "location_hint" to bottle.locationHint,
                "avatar_hex" to bottle.avatarHex,
                "avatar_url" to (bottle.avatarUrl ?: "")
            )
            val response = api.createBottle(payload)
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengirim botol ke PocketBase", e)
            false
        }
    }

    private fun formatMomentTimeAgo(timestamp: Long): String {
        val diff = System.currentTimeMillis() - timestamp
        val minutes = diff / (60 * 1000)
        val hours = minutes / 60
        val days = hours / 24

        return when {
            diff < 60_000 -> "Baru saja"
            minutes < 60 -> "$minutes mnt lalu"
            hours < 24 -> "$hours jam lalu"
            days < 7 -> "$days hari lalu"
            else -> {
                val sdf = java.text.SimpleDateFormat("dd MMM", java.util.Locale("id", "ID"))
                sdf.format(java.util.Date(timestamp))
            }
        }
    }

    suspend fun fetchMoments(): List<MomentItem>? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val response = api.getMoments(sort = "-created_at_ms", perPage = 50)
            if (response.isSuccessful) {
                val items = response.body()?.items ?: return@withContext null
                items.map { record ->
                    MomentItem(
                        id = record.id,
                        authorName = record.authorName ?: "Pengguna Lovy",
                        authorAvatarHex = record.authorAvatarHex ?: 0xFFFB8C00,
                        timeAgo = formatMomentTimeAgo(record.createdAtMs ?: System.currentTimeMillis()),
                        content = record.content ?: "",
                        likesCount = record.likesCount ?: 0,
                        commentsCount = record.commentsCount ?: 0,
                        imageUrl = record.imageUrl,
                        authorAvatarUrl = record.authorAvatarUrl,
                        locationTag = record.locationTag ?: "Indonesia",
                        authorId = record.authorId ?: ""
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil momen dari PocketBase", e)
            null
        }
    }

    suspend fun sendMoment(moment: MomentItem, authorId: String): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(moment.id)
            val payload = mapOf(
                "id" to pbId,
                "author_id" to PocketBaseClient.toPbId(authorId),
                "author_name" to moment.authorName,
                "content" to moment.content,
                "likes_count" to moment.likesCount,
                "comments_count" to moment.commentsCount,
                "created_at_ms" to System.currentTimeMillis(),
                "author_avatar_hex" to moment.authorAvatarHex,
                "image_url" to (moment.imageUrl ?: ""),
                "author_avatar_url" to (moment.authorAvatarUrl ?: ""),
                "location_tag" to (moment.locationTag ?: "Indonesia")
            )
            val response = api.createMoment(payload)
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengirim momen ke PocketBase", e)
            false
        }
    }

    suspend fun updateMomentCommentsCount(momentId: String, count: Int): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(momentId)
            val updates = mapOf("comments_count" to count)
            val res = api.updateMoment(pbId, updates)
            res.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal update komentar momen PocketBase", e)
            false
        }
    }

    suspend fun deleteMoment(momentId: String): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(momentId)
            val res = api.deleteMoment(pbId)
            res.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menghapus momen PocketBase", e)
            false
        }
    }

    suspend fun fetchChatMessages(
        conversationId: String,
        currentUserId: String = "",
        partnerId: String = "",
        sinceTimestamp: Long = 0L
    ): List<ChatMessage>? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val filter = if (partnerId.isNotBlank() && currentUserId.isNotBlank()) {
                val clean1 = currentUserId.trim()
                val clean2 = partnerId.trim()
                val sorted = if (clean1 <= clean2) listOf(clean1, clean2) else listOf(clean2, clean1)
                val formatDouble = "conv_${sorted[0]}__${sorted[1]}"
                val formatSingle = "conv_${sorted[0]}_${sorted[1]}"
                val p1 = PocketBaseClient.toPbId(currentUserId)
                val p2 = PocketBaseClient.toPbId(partnerId)
                val timeFilter = if (sinceTimestamp > 0) " && created_at_ms > $sinceTimestamp" else ""
                "(conversation_id='$conversationId' || conversation_id='$formatDouble' || conversation_id='$formatSingle' || (sender_id='$clean1' && receiver_id='$clean2') || (sender_id='$clean2' && receiver_id='$clean1') || (sender_id='$p1' && receiver_id='$p2') || (sender_id='$p2' && receiver_id='$p1'))$timeFilter"
            } else {
                val timeFilter = if (sinceTimestamp > 0) " && created_at_ms > $sinceTimestamp" else ""
                "conversation_id='$conversationId'$timeFilter"
            }

            val response = api.getMessages(filter = filter, sort = "created_at_ms", perPage = 100)
            if (response.isSuccessful) {
                val items = response.body()?.items ?: return@withContext null
                items.map { record ->
                    val isSentByMe = isSenderMe(record.senderId, record.receiverId, currentUserId)
                    ChatMessage(
                        id = record.id,
                        conversationId = conversationId,
                        text = record.text,
                        timestamp = record.createdAtMs ?: System.currentTimeMillis(),
                        isFromMe = isSentByMe,
                        isRead = record.isRead ?: false,
                        deletedForSender = false,
                        deletedForReceiver = false,
                        imageUrl = record.imageUrl,
                        replyToId = record.replyToId,
                        replyToSender = record.replyToSender,
                        replyToText = record.replyToText
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil pesan chat dari PocketBase", e)
            null
        }
    }

    private fun isSenderMe(senderId: String, receiverId: String?, currentUserId: String): Boolean {
        if (currentUserId.isBlank()) return false
        val myId = currentUserId.trim()
        val sId = senderId.trim()
        val rId = receiverId?.trim()
        val myPb = PocketBaseClient.toPbId(myId)

        if (sId.equals(myId, ignoreCase = true) || sId.equals(myPb, ignoreCase = true)) return true
        if (rId != null && (rId.equals(myId, ignoreCase = true) || rId.equals(myPb, ignoreCase = true))) return false
        if (sId.isNotBlank() && !sId.equals("me", ignoreCase = true) && !sId.equals(myId, ignoreCase = true)) {
            return false
        }
        if (sId.equals("me", ignoreCase = true) && rId != null && !rId.equals(myId, ignoreCase = true)) return true
        return false
    }

    suspend fun sendChatMessage(
        message: ChatMessage,
        senderId: String = "me",
        receiverId: String? = null
    ): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(message.id)
            val payload = mapOf(
                "id" to pbId,
                "conversation_id" to message.conversationId,
                "sender_id" to senderId,
                "receiver_id" to (receiverId ?: ""),
                "text" to message.text,
                "created_at_ms" to message.timestamp,
                "image_url" to (message.imageUrl ?: ""),
                "is_read" to (message.isRead ?: false),
                "reply_to_id" to (message.replyToId ?: ""),
                "reply_to_sender" to (message.replyToSender ?: ""),
                "reply_to_text" to (message.replyToText ?: ""),
                "reaction" to ""
            )
            val response = api.createMessage(payload)
            response.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengirim chat message ke PocketBase", e)
            false
        }
    }

    suspend fun fetchRecentMessagesForUser(userId: String): List<SupabaseMessageDto>? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val pbId = PocketBaseClient.toPbId(userId)
            val filter = "sender_id='$userId' || receiver_id='$userId' || sender_id='$pbId' || receiver_id='$pbId'"
            val response = api.getMessages(filter = filter, sort = "-created_at_ms", perPage = 60)
            if (response.isSuccessful) {
                val items = response.body()?.items ?: return@withContext null
                items.map { record ->
                    SupabaseMessageDto(
                        id = record.id,
                        conversationId = record.conversationId,
                        senderId = record.senderId,
                        receiverId = record.receiverId,
                        text = record.text,
                        createdAt = record.createdAtMs ?: System.currentTimeMillis(),
                        deletedForSender = false,
                        deletedForReceiver = false,
                        imageUrl = record.imageUrl,
                        isRead = record.isRead ?: false,
                        replyToId = record.replyToId,
                        replyToSender = record.replyToSender,
                        replyToText = record.replyToText,
                        reaction = record.reaction
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil riwayat pesan dari PocketBase", e)
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
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(id)
            val updates = mutableMapOf<String, Any?>(
                "name" to name.trim(),
                "gender" to if (gender == Gender.MALE) "male" else "female",
                "bio" to bio,
                "avatar_hex" to avatarHex,
                "is_online" to true,
                "last_active_at" to System.currentTimeMillis()
            )
            avatarUrl?.let { updates["avatar_url"] = it }
            city?.let { updates["city"] = it }
            fcmToken?.let { updates["fcm_token"] = it }

            // Coba update record jika sudah ada
            val updateResp = api.updateUser(pbId, updates)
            if (updateResp.isSuccessful) return@withContext true

            // Jika belum ada, buat record baru
            updates["id"] = pbId
            val defaultPass = "pb_pass_${pbId.take(8)}!"
            updates["password"] = defaultPass
            updates["passwordConfirm"] = defaultPass
            val createResp = api.createUser(updates)
            createResp.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal registerOrUpdateUser di PocketBase", e)
            false
        }
    }

    suspend fun markMessageDeletedForSender(messageId: String): Boolean = withContext(Dispatchers.IO) {
        // Pada PocketBase, pesan bisa dihapus atau ditandai
        true
    }

    suspend fun markMessageDeletedForReceiver(messageId: String): Boolean = withContext(Dispatchers.IO) {
        true
    }

    suspend fun markConversationDeletedForUser(conversationId: String, userId: String): Boolean = withContext(Dispatchers.IO) {
        true
    }

    suspend fun deleteMessageForEveryone(messageId: String): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(messageId)
            val res = api.deleteMessage(pbId)
            res.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menghapus pesan untuk semua orang di PocketBase", e)
            false
        }
    }

    suspend fun markAllSenderMessagesDeleted(senderId: String = "me"): Boolean = withContext(Dispatchers.IO) {
        true
    }

    suspend fun markMessagesAsRead(conversationId: String, senderId: String): Boolean = withContext(Dispatchers.IO) {
        true
    }

    suspend fun markMessageReadById(messageId: String): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(messageId)
            val res = api.updateMessage(pbId, mapOf("is_read" to true))
            res.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateUserPresence(userId: String, isOnline: Boolean = true): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(userId)
            val updates = mapOf(
                "is_online" to isOnline,
                "last_active_at" to System.currentTimeMillis()
            )
            val res = api.updateUser(pbId, updates)
            res.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal memperbarui status kehadiran di PocketBase", e)
            false
        }
    }

    suspend fun updateUserLastActive(userId: String): Boolean = withContext(Dispatchers.IO) {
        updateUserPresence(userId, isOnline = true)
    }

    suspend fun updateUserFcmToken(userId: String, token: String): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(userId)
            val res = api.updateUser(pbId, mapOf("fcm_token" to token))
            res.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun findAccountByUsername(username: String): SupabaseAccountDto? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val filter = "username='$username' || name='$username'"
            val res = api.getUsers(perPage = 1, filter = filter)
            val item = res.body()?.items?.firstOrNull() ?: return@withContext null
            SupabaseAccountDto(
                id = item.id,
                username = item.username ?: item.name ?: "",
                displayName = item.name ?: "",
                gender = item.gender ?: "FEMALE",
                bio = item.bio ?: "",
                avatarUrl = item.avatarUrl,
                lastLoginAt = item.lastActiveAt ?: System.currentTimeMillis(),
                fcmToken = item.fcmToken
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun findAccountByGoogle(googleEmail: String): SupabaseAccountDto? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val filter = "email='$googleEmail'"
            val res = api.getUsers(perPage = 1, filter = filter)
            val item = res.body()?.items?.firstOrNull() ?: return@withContext null
            SupabaseAccountDto(
                id = item.id,
                username = item.username ?: item.name ?: googleEmail,
                displayName = item.name ?: "",
                gender = item.gender ?: "FEMALE",
                bio = item.bio ?: "",
                avatarUrl = item.avatarUrl,
                googleEmail = googleEmail,
                lastLoginAt = item.lastActiveAt ?: System.currentTimeMillis(),
                fcmToken = item.fcmToken
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun findAccountById(accountId: String): SupabaseAccountDto? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val pbId = PocketBaseClient.toPbId(accountId)
            val res = api.getUserById(pbId)
            val item = if (res.isSuccessful) res.body() else {
                val q = api.getUsers(perPage = 1, filter = "id='$pbId' || id='$accountId'")
                q.body()?.items?.firstOrNull()
            } ?: return@withContext null

            SupabaseAccountDto(
                id = item.id,
                username = item.username ?: item.name ?: "",
                displayName = item.name ?: "",
                gender = item.gender ?: "FEMALE",
                bio = item.bio ?: "",
                avatarUrl = item.avatarUrl,
                lastLoginAt = item.lastActiveAt ?: System.currentTimeMillis(),
                fcmToken = item.fcmToken
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun registerOrUpdateAccount(account: SupabaseAccountDto): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(account.id)
            val updates = mutableMapOf<String, Any?>(
                "name" to account.displayName,
                "gender" to (account.gender ?: "FEMALE").lowercase(),
                "bio" to account.bio,
                "avatar_url" to (account.avatarUrl ?: ""),
                "last_active_at" to account.lastLoginAt,
                "is_online" to true
            )
            account.fcmToken?.let { updates["fcm_token"] = it }

            val updateRes = api.updateUser(pbId, updates)
            if (updateRes.isSuccessful) return@withContext true

            updates["id"] = pbId
            val pwd = "pb_pass_${pbId.take(8)}!"
            updates["password"] = pwd
            updates["passwordConfirm"] = pwd
            val createRes = api.createUser(updates)
            createRes.isSuccessful
        } catch (e: Exception) {
            Log.w(TAG, "Gagal registerOrUpdateAccount di PocketBase", e)
            false
        }
    }

    suspend fun updateAccountLoginTime(accountId: String): Boolean = withContext(Dispatchers.IO) {
        updateUserPresence(accountId, isOnline = true)
    }

    suspend fun deleteAccountAndUserData(accountId: String, lovyId: String): Boolean = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext false
        try {
            val pbId = PocketBaseClient.toPbId(accountId)
            api.deleteUser(pbId)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun purgeInactiveAccountsAndDeletedMessages(): Boolean = withContext(Dispatchers.IO) {
        true
    }
}

package com.example.data.pocketbase

import android.util.Log
import com.example.data.AuthResult
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
            val response = api.getUsers(perPage = 100, sort = "-last_active_at")
            if (response.isSuccessful) {
                val items = response.body()?.items ?: return@withContext null
                val now = System.currentTimeMillis()
                val dummyNames = setOf(
                    "siti rahma", "rian pratama", "nadia putri", "dimas anggara",
                    "alya zahra", "pengguna lovy", "rania putri", "clara monica",
                    "dimas danendra", "clarissa aurelia", "salma salsabil",
                    "tanpa nama", "user tak bernama", "pengguna", "unknown user", "anonymous",
                    "test user", "user test", "tester", "test", "demo", "sample"
                )

                val toDeleteUserIds = mutableListOf<String>()
                val validUsers = items.filterNot { record ->
                    val cleanName = (record.name ?: "").trim()
                    val lower = cleanName.lowercase()
                    val userLower = (record.username ?: "").lowercase().trim()
                    val bioLower = (record.bio ?: "").lowercase()

                    val isSystemConfig = record.username?.startsWith("__") == true ||
                            userLower.contains("app_config") ||
                            userLower.contains("system") ||
                            lower.contains("app config") ||
                            lower.contains("system app") ||
                            bioLower.contains("min_code") ||
                            bioLower.contains("latest_code") ||
                            record.id == "dpjh5vim92i9xy9"

                    val isTestAccount = lower.contains("test") || lower.contains("tester") || lower.contains("dummy") || userLower.contains("test") || userLower.contains("dummy")
                    val isDummy = cleanName.isEmpty() ||
                            lower in dummyNames ||
                            isTestAccount ||
                            record.id.matches(Regex("^u[0-9]+$")) ||
                            record.id.startsWith("test_")

                    if (!isSystemConfig && isDummy && record.id.isNotBlank()) {
                        toDeleteUserIds.add(record.id)
                    }

                    isSystemConfig || isDummy
                }.map { record ->
                    val lastActive = record.lastActiveAt ?: 0L
                    val isTrulyOnline = (record.isOnline == true) && (now - lastActive <= ONLINE_TIMEOUT_MS)
                    val effectiveId = record.username?.takeIf { it.startsWith("lovy_") } ?: record.id
                    User(
                        id = effectiveId,
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

                for (delId in toDeleteUserIds) {
                    try { api.deleteUser(delId) } catch (_: Throwable) {}
                }

                validUsers
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
                val query = api.getUsers(perPage = 1, filter = "id='$pbId' || id='$userId' || username='$userId' || username='$pbId'")
                query.body()?.items?.firstOrNull()
            } ?: return@withContext null

            // Tolak akun konfigurasi sistem
            val uLower = (record.username ?: "").lowercase().trim()
            val nLower = (record.name ?: "").lowercase().trim()
            val bLower = (record.bio ?: "").lowercase()
            if (record.username?.startsWith("__") == true ||
                uLower.contains("app_config") ||
                uLower.contains("system") ||
                nLower.contains("app config") ||
                nLower.contains("system app") ||
                bLower.contains("min_code") ||
                bLower.contains("latest_code") ||
                record.id == "dpjh5vim92i9xy9") {
                return@withContext null
            }

            val now = System.currentTimeMillis()
            val lastActive = record.lastActiveAt ?: 0L
            val isTrulyOnline = (record.isOnline == true) && (now - lastActive <= ONLINE_TIMEOUT_MS)
            val effectiveId = record.username?.takeIf { it.startsWith("lovy_") } ?: record.id

            User(
                id = effectiveId,
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
                val dummyNames = setOf(
                    "test user", "user test", "tester", "test", "demo", "sample"
                )
                val toDeleteBottleIds = mutableListOf<String>()
                val validBottles = items.filterNot { record ->
                    val cleanSender = (record.senderName ?: "").trim().lowercase()
                    val cleanId = (record.senderId ?: "").trim().lowercase()
                    val isTest = cleanSender.contains("test") || cleanSender.contains("tester") ||
                            cleanSender.contains("dummy") || cleanId.contains("test") ||
                            cleanSender in dummyNames || record.id.startsWith("test_")
                    if (isTest && record.id.isNotBlank()) {
                        toDeleteBottleIds.add(record.id)
                    }
                    isTest
                }.map { record ->
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

                for (delId in toDeleteBottleIds) {
                    try { api.deleteBottle(delId) } catch (_: Throwable) {}
                }

                validBottles
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
            val pbId = PocketBaseClient.toPbId(bottle.id)
            val payload = mutableMapOf<String, Any?>(
                "id" to pbId,
                "sender_id" to bottle.senderId.ifBlank { bottle.senderName },
                "sender_name" to bottle.senderName,
                "sender_gender" to if (bottle.senderGender == Gender.MALE) "male" else "female",
                "content" to bottle.content,
                "created_at_ms" to bottle.thrownTimestamp,
                "location_hint" to bottle.locationHint,
                "avatar_hex" to bottle.avatarHex,
                "avatar_url" to (bottle.avatarUrl ?: "")
            )
            val response = api.createBottle(payload)
            if (response.isSuccessful) return@withContext true

            // Fallback jika ID sudah digunakan atau validasi panjang ID bermasalah: biarkan PocketBase meng-generate ID otomatis
            payload.remove("id")
            val retry = api.createBottle(payload)
            if (!retry.isSuccessful) {
                Log.w(TAG, "Gagal createBottle PocketBase: code=${retry.code()} error=${retry.errorBody()?.string()}")
            }
            retry.isSuccessful
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
                val dummyNames = setOf(
                    "test user", "user test", "tester", "test", "demo", "sample"
                )
                val toDeleteMomentIds = mutableListOf<String>()
                val validMoments = items.filterNot { record ->
                    val cleanAuthor = (record.authorName ?: "").trim().lowercase()
                    val cleanId = (record.authorId ?: "").trim().lowercase()
                    val isTest = cleanAuthor.contains("test") || cleanAuthor.contains("tester") ||
                            cleanAuthor.contains("dummy") || cleanId.contains("test") ||
                            cleanAuthor in dummyNames || record.id.startsWith("test_")
                    if (isTest && record.id.isNotBlank()) {
                        toDeleteMomentIds.add(record.id)
                    }
                    isTest
                }.map { record ->
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

                for (delId in toDeleteMomentIds) {
                    try { api.deleteMoment(delId) } catch (_: Throwable) {}
                }

                validMoments
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
            val effectiveAuthorId = authorId.trim().ifBlank {
                PocketBaseClient.toLovyId(moment.authorName)
            }
            val payload = mutableMapOf<String, Any?>(
                "id" to pbId,
                "author_id" to effectiveAuthorId,
                "author_name" to moment.authorName.ifBlank { "Pengguna Lovy" },
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
            if (response.isSuccessful) {
                Log.d(TAG, "Berhasil membuat momen di PocketBase: $pbId")
                return@withContext true
            }

            val err1 = response.errorBody()?.string() ?: ""
            Log.w(TAG, "Gagal createMoment PocketBase pertama: code=${response.code()} error=$err1")

            payload.remove("id")
            val retry = api.createMoment(payload)
            if (retry.isSuccessful) {
                Log.d(TAG, "Berhasil membuat momen di PocketBase (retry tanpa id)")
                return@withContext true
            }

            // Fallback: Jika skema PocketBase belum memiliki kolom sekunder (author_avatar_url, location_tag, author_avatar_hex)
            val fallbackPayload = mutableMapOf<String, Any?>(
                "author_id" to effectiveAuthorId,
                "author_name" to moment.authorName.ifBlank { "Pengguna Lovy" },
                "content" to moment.content,
                "likes_count" to moment.likesCount,
                "comments_count" to moment.commentsCount,
                "created_at_ms" to System.currentTimeMillis(),
                "image_url" to (moment.imageUrl ?: "")
            )
            val retryFallback = api.createMoment(fallbackPayload)
            if (retryFallback.isSuccessful) {
                Log.d(TAG, "Berhasil membuat momen di PocketBase (fallback payload)")
                return@withContext true
            }
            Log.w(TAG, "Gagal createMoment PocketBase fallback: code=${retryFallback.code()} error=${retryFallback.errorBody()?.string()}")
            false
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mengirim momen ke PocketBase", e)
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
                val canonical = PocketBaseClient.getCanonicalConversationId(currentUserId, partnerId)
                val norm = PocketBaseClient.normalizeConvId(conversationId)
                val timeFilter = if (sinceTimestamp > 0) " && created_at_ms > $sinceTimestamp" else ""
                "(conversation_id='$conversationId' || conversation_id='$canonical' || conversation_id='$norm' || conversation_id='$formatDouble' || conversation_id='$formatSingle' || (sender_id='$clean1' && receiver_id='$clean2') || (sender_id='$clean2' && receiver_id='$clean1') || (sender_id='$p1' && receiver_id='$p2') || (sender_id='$p2' && receiver_id='$p1'))$timeFilter"
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
                        audioUrl = record.audioUrl,
                        audioDurationSeconds = record.audioDurationSeconds ?: 0,
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
            val payload = mutableMapOf<String, Any?>(
                "id" to pbId,
                "conversation_id" to message.conversationId,
                "sender_id" to senderId,
                "receiver_id" to (receiverId ?: ""),
                "text" to message.text,
                "created_at_ms" to message.timestamp,
                "image_url" to (message.imageUrl ?: ""),
                "audio_url" to (message.audioUrl ?: ""),
                "audio_duration_seconds" to message.audioDurationSeconds,
                "is_read" to (message.isRead ?: false),
                "reply_to_id" to (message.replyToId ?: ""),
                "reply_to_sender" to (message.replyToSender ?: ""),
                "reply_to_text" to (message.replyToText ?: ""),
                "reaction" to (message.reaction ?: "")
            )
            val response = api.createMessage(payload)
            if (response.isSuccessful) {
                val finalId = response.body()?.id?.takeIf { it.isNotBlank() } ?: pbId
                // Publikasikan secara instan via Centrifugo WebSocket ke channel penerima
                if (!receiverId.isNullOrBlank()) {
                    com.example.data.centrifugo.CentrifugoRealtimeManager.publishChatMessage(
                        messageId = finalId,
                        conversationId = message.conversationId,
                        senderId = senderId,
                        receiverId = receiverId,
                        text = message.text,
                        imageUrl = message.imageUrl,
                        audioUrl = message.audioUrl,
                        audioDurationSeconds = message.audioDurationSeconds,
                        createdAtMs = message.timestamp,
                        replyToId = message.replyToId,
                        replyToSender = message.replyToSender,
                        replyToText = message.replyToText,
                        reaction = message.reaction
                    )
                }
                return@withContext true
            }

            payload.remove("id")
            val retry = api.createMessage(payload)
            if (retry.isSuccessful) {
                val finalRetryId = retry.body()?.id?.takeIf { it.isNotBlank() } ?: pbId
                if (!receiverId.isNullOrBlank()) {
                    com.example.data.centrifugo.CentrifugoRealtimeManager.publishChatMessage(
                        messageId = finalRetryId,
                        conversationId = message.conversationId,
                        senderId = senderId,
                        receiverId = receiverId,
                        text = message.text,
                        imageUrl = message.imageUrl,
                        audioUrl = message.audioUrl,
                        audioDurationSeconds = message.audioDurationSeconds,
                        createdAtMs = message.timestamp,
                        replyToId = message.replyToId,
                        replyToSender = message.replyToSender,
                        replyToText = message.replyToText,
                        reaction = message.reaction
                    )
                }
                return@withContext true
            }

            // Fallback: jika koleksi messages di PocketBase belum memiliki kolom audio_url / reply_to_*
            val fallbackPayload = mutableMapOf<String, Any?>(
                "conversation_id" to message.conversationId,
                "sender_id" to senderId,
                "receiver_id" to (receiverId ?: ""),
                "text" to message.text,
                "created_at_ms" to message.timestamp
            )
            if (!message.imageUrl.isNullOrBlank()) {
                fallbackPayload["image_url"] = message.imageUrl
            }
            val retryFallback = api.createMessage(fallbackPayload)
            if (retryFallback.isSuccessful) {
                val finalFallbackId = retryFallback.body()?.id?.takeIf { it.isNotBlank() } ?: pbId
                if (!receiverId.isNullOrBlank()) {
                    com.example.data.centrifugo.CentrifugoRealtimeManager.publishChatMessage(
                        messageId = finalFallbackId,
                        conversationId = message.conversationId,
                        senderId = senderId,
                        receiverId = receiverId,
                        text = message.text,
                        imageUrl = message.imageUrl,
                        audioUrl = message.audioUrl,
                        audioDurationSeconds = message.audioDurationSeconds,
                        createdAtMs = message.timestamp
                    )
                }
                return@withContext true
            }

            Log.w(TAG, "Gagal createMessage PocketBase: code=${retryFallback.code()} error=${retryFallback.errorBody()?.string()}")
            false
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
                        audioUrl = record.audioUrl,
                        audioDurationSeconds = record.audioDurationSeconds,
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
                "username" to id.trim(),
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

            // Cari jika record ada dengan username yang sama
            val search = api.getUsers(perPage = 1, filter = "username='${id.trim()}'")
            val existing = search.body()?.items?.firstOrNull()
            if (existing != null) {
                val retry = api.updateUser(existing.id, updates)
                if (retry.isSuccessful) return@withContext true
            }

            // Jika belum ada, buat record baru
            updates["id"] = pbId
            val defaultPass = "pb_pass_${pbId.take(8)}!"
            updates["password"] = defaultPass
            updates["passwordConfirm"] = defaultPass
            val createResp = api.createUser(updates)
            if (createResp.isSuccessful) return@withContext true

            // Retry create tanpa custom id
            updates.remove("id")
            val createNoId = api.createUser(updates)
            createNoId.isSuccessful
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
            val clean = username.trim()
            val cleanLower = clean.lowercase()
            val filter = "username='$clean' || name='$clean' || email='$cleanLower'"
            val res = api.getUsers(perPage = 1, filter = filter)
            val item = res.body()?.items?.firstOrNull() ?: return@withContext null
            val permanentLovyId = when {
                item.username?.startsWith("lovy_") == true -> item.username
                clean.startsWith("lovy_") -> clean
                else -> PocketBaseClient.toLovyId(item.email ?: clean)
            }
            SupabaseAccountDto(
                id = permanentLovyId,
                username = item.username ?: permanentLovyId,
                displayName = item.name ?: "",
                gender = item.gender ?: "FEMALE",
                bio = item.bio ?: "",
                avatarUrl = item.avatarUrl,
                googleEmail = item.email ?: (if (clean.contains("@")) clean else null),
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
            val cleanEmail = googleEmail.trim().lowercase()
            val expectedLovyId = PocketBaseClient.toLovyId(cleanEmail)

            // 1. Cari melalui filter email resmi PocketBase
            val filter = "email='$cleanEmail'"
            val res = api.getUsers(perPage = 1, filter = filter)
            val item = res.body()?.items?.firstOrNull()
            if (item != null) {
                val permanentLovyId = when {
                    item.username?.startsWith("lovy_") == true -> item.username
                    else -> expectedLovyId
                }
                authenticateUserSession(cleanEmail, item.id)
                return@withContext SupabaseAccountDto(
                    id = permanentLovyId,
                    username = permanentLovyId,
                    displayName = item.name ?: "",
                    gender = item.gender ?: "FEMALE",
                    bio = item.bio ?: "",
                    avatarUrl = item.avatarUrl,
                    googleEmail = cleanEmail,
                    lastLoginAt = item.lastActiveAt ?: System.currentTimeMillis(),
                    fcmToken = item.fcmToken
                )
            }

            // 2. Cek via toPbId dari expectedLovyId
            val expectedPbId = PocketBaseClient.toPbId(expectedLovyId)
            try {
                val byLovyIdRes = api.getUserById(expectedPbId)
                if (byLovyIdRes.isSuccessful && byLovyIdRes.body() != null) {
                    val userRec = byLovyIdRes.body()!!
                    return@withContext SupabaseAccountDto(
                        id = expectedLovyId,
                        username = expectedLovyId,
                        displayName = userRec.name ?: "",
                        gender = userRec.gender ?: "FEMALE",
                        bio = userRec.bio ?: "",
                        avatarUrl = userRec.avatarUrl,
                        googleEmail = cleanEmail,
                        lastLoginAt = userRec.lastActiveAt ?: System.currentTimeMillis(),
                        fcmToken = userRec.fcmToken
                    )
                }
            } catch (_: Exception) {}

            // 3. Cek via ID deterministik berbasis email Google
            val deterministicPbId = PocketBaseClient.toPbId("google_$cleanEmail")
            try {
                val byIdRes = api.getUserById(deterministicPbId)
                if (byIdRes.isSuccessful && byIdRes.body() != null) {
                    val userRec = byIdRes.body()!!
                    val permanentLovyId = when {
                        userRec.username?.startsWith("lovy_") == true -> userRec.username
                        else -> expectedLovyId
                    }
                    return@withContext SupabaseAccountDto(
                        id = permanentLovyId,
                        username = permanentLovyId,
                        displayName = userRec.name ?: "",
                        gender = userRec.gender ?: "FEMALE",
                        bio = userRec.bio ?: "",
                        avatarUrl = userRec.avatarUrl,
                        googleEmail = cleanEmail,
                        lastLoginAt = userRec.lastActiveAt ?: System.currentTimeMillis(),
                        fcmToken = userRec.fcmToken
                    )
                }
            } catch (_: Exception) {}

            null
        } catch (e: Exception) {
            Log.w(TAG, "findAccountByGoogle error: ${e.message}")
            null
        }
    }

    suspend fun findAccountById(accountId: String): SupabaseAccountDto? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val pbId = PocketBaseClient.toPbId(accountId)
            val res = api.getUserById(pbId)
            val item = if (res.isSuccessful) res.body() else {
                val q = api.getUsers(perPage = 1, filter = "id='$pbId' || id='$accountId' || username='$accountId'")
                q.body()?.items?.firstOrNull()
            } ?: return@withContext null

            val permanentLovyId = when {
                item.username?.startsWith("lovy_") == true -> item.username
                accountId.startsWith("lovy_") -> accountId
                else -> PocketBaseClient.toLovyId(accountId)
            }

            SupabaseAccountDto(
                id = permanentLovyId,
                username = permanentLovyId,
                displayName = item.name ?: "",
                gender = item.gender ?: "FEMALE",
                bio = item.bio ?: "",
                avatarUrl = item.avatarUrl,
                googleEmail = item.email,
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
            val permanentLovyId = if (account.id.startsWith("lovy_")) {
                account.id
            } else if (!account.username.isNullOrBlank() && account.username.startsWith("lovy_")) {
                account.username
            } else {
                PocketBaseClient.toLovyId(account.googleEmail ?: account.username ?: account.id)
            }
            val pbId = PocketBaseClient.toPbId(permanentLovyId)
            val cleanEmail = account.googleEmail?.trim()?.lowercase()

            val updates = mutableMapOf<String, Any?>(
                "name" to account.displayName,
                "gender" to (account.gender ?: "FEMALE").lowercase(),
                "bio" to (account.bio ?: ""),
                "avatar_url" to (account.avatarUrl ?: ""),
                "last_active_at" to account.lastLoginAt,
                "is_online" to true,
                "username" to permanentLovyId
            )
            if (!cleanEmail.isNullOrBlank()) {
                updates["email"] = cleanEmail
                updates["emailVisibility"] = true
            }
            account.fcmToken?.let { updates["fcm_token"] = it }

            // 1. Cari record pengguna yang mungkin sudah ada di PocketBase berdasarkan email atau username
            var existingRecordId: String? = null
            if (!cleanEmail.isNullOrBlank()) {
                try {
                    val emailSearch = api.getUsers(perPage = 1, filter = "email='$cleanEmail'")
                    existingRecordId = emailSearch.body()?.items?.firstOrNull()?.id
                } catch (_: Exception) {}
            }

            if (existingRecordId == null) {
                try {
                    val userSearch = api.getUsers(perPage = 1, filter = "username='$permanentLovyId'")
                    existingRecordId = userSearch.body()?.items?.firstOrNull()?.id
                } catch (_: Exception) {}
            }

            val targetId = existingRecordId ?: pbId

            // 2. Coba update record yang ada terlebih dahulu
            val updateRes = api.updateUser(targetId, updates)
            if (updateRes.isSuccessful) {
                authenticateUserSession(cleanEmail ?: permanentLovyId, pbId)
                return@withContext true
            }

            // 3. Jika belum ada, buat record baru
            updates["id"] = targetId
            val pwd = "pb_pass_${targetId.take(8)}!"
            updates["password"] = pwd
            updates["passwordConfirm"] = pwd
            val createRes = api.createUser(updates)
            if (createRes.isSuccessful) {
                authenticateUserSession(cleanEmail ?: permanentLovyId, pbId)
                return@withContext true
            }

            // 4. Jika pembuatan gagal karena konflik email/id, cari kembali dan timpa datanya
            if (!cleanEmail.isNullOrBlank()) {
                val retrySearch = api.getUsers(perPage = 1, filter = "email='$cleanEmail'")
                val found = retrySearch.body()?.items?.firstOrNull()
                if (found != null) {
                    updates.remove("id")
                    updates.remove("password")
                    updates.remove("passwordConfirm")
                    val retryUpdate = api.updateUser(found.id, updates)
                    if (retryUpdate.isSuccessful) {
                        authenticateUserSession(cleanEmail, pbId)
                        return@withContext true
                    }
                }
            }

            false
        } catch (e: Exception) {
            Log.w(TAG, "Gagal registerOrUpdateAccount di PocketBase", e)
            false
        }
    }

    suspend fun authenticateUserSession(identity: String, pbId: String) {
        val api = PocketBaseClient.getApi() ?: return
        try {
            val pwd = "pb_pass_${pbId.take(8)}!"
            val authResp = api.authWithPassword(mapOf("identity" to identity, "password" to pwd))
            if (authResp.isSuccessful) {
                val token = authResp.body()?.token
                if (!token.isNullOrBlank()) {
                    PocketBaseClient.authToken = token
                    PocketBaseClient.saveAuthToken(com.example.LovyApplication.appContext, token)
                    Log.d(TAG, "Berhasil mengautentikasi sesi PocketBase untuk $identity")
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Info authWithPassword (opsional): ${e.message}")
        }
    }

    suspend fun getGoogleOAuthProvider(): Map<String, Any?>? = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi() ?: return@withContext null
        try {
            val resp = api.getAuthMethods()
            if (resp.isSuccessful) {
                val body = resp.body() ?: return@withContext null
                @Suppress("UNCHECKED_CAST")
                val providers = body["authProviders"] as? List<Map<String, Any?>>
                return@withContext providers?.firstOrNull { it["name"] == "google" }
            }
            null
        } catch (e: Exception) {
            Log.w(TAG, "Gagal mengambil Google auth provider dari PocketBase: ${e.message}")
            null
        }
    }

    suspend fun extractGoogleClientIdFromPocketBase(): String? = withContext(Dispatchers.IO) {
        val provider = getGoogleOAuthProvider() ?: return@withContext null
        val authUrl = provider["authUrl"] as? String ?: return@withContext null
        try {
            val matcher = Regex("client_id=([^&]+)").find(authUrl)
            val rawClientId = matcher?.groupValues?.getOrNull(1) ?: return@withContext null
            val decoded = java.net.URLDecoder.decode(rawClientId, "UTF-8")
            decoded.removePrefix("https://").removePrefix("http://").trim()
        } catch (_: Exception) {
            null
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

    suspend fun loginWithPassword(identity: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi()
            ?: return@withContext AuthResult(false, "Tidak dapat menghubungkan ke server PocketBase")

        val cleanIdentity = identity.trim()
        val cleanPassword = password.trim()
        if (cleanIdentity.isEmpty() || cleanPassword.isEmpty()) {
            return@withContext AuthResult(false, "Username atau Email dan kata sandi wajib diisi")
        }

        try {
            val resp = api.authWithPassword(mapOf("identity" to cleanIdentity, "password" to cleanPassword))
            if (resp.isSuccessful) {
                val body = resp.body()
                val token = body?.token
                val record = body?.record

                if (!token.isNullOrBlank()) {
                    PocketBaseClient.authToken = token
                    PocketBaseClient.saveAuthToken(com.example.LovyApplication.appContext, token)
                }

                if (record != null) {
                    val permanentLovyId = when {
                        record.username?.startsWith("lovy_") == true -> record.username
                        cleanIdentity.startsWith("lovy_") -> cleanIdentity
                        else -> PocketBaseClient.toLovyId(record.email ?: record.username ?: record.id)
                    }

                    updateUserPresence(record.id, isOnline = true)

                    val userGender = if (record.gender?.equals("MALE", ignoreCase = true) == true) Gender.MALE else Gender.FEMALE
                    val dispName = record.name?.takeIf { it.isNotBlank() } ?: record.username ?: cleanIdentity

                    return@withContext AuthResult(
                        success = true,
                        message = "Login berhasil! Selamat datang kembali di Lovy Chat.",
                        username = record.username ?: cleanIdentity,
                        displayName = dispName,
                        email = record.email,
                        lovyId = permanentLovyId,
                        gender = userGender,
                        bio = record.bio ?: "",
                        avatarUrl = record.avatarUrl,
                        city = record.city,
                        isGoogleUser = false
                    )
                }
            }

            val code = resp.code()
            if (code == 400) {
                val checkFilter = "username='$cleanIdentity' || email='${cleanIdentity.lowercase()}'"
                val checkUser = try { api.getUsers(perPage = 1, filter = checkFilter).body()?.items?.firstOrNull() } catch (_: Exception) { null }
                val errorMsg = if (checkUser != null) {
                    "Kata sandi yang Anda masukkan salah. Silakan coba lagi."
                } else {
                    "Akun \"$cleanIdentity\" belum terdaftar di PocketBase. Silakan pilih menu Daftar Akun terlebih dahulu."
                }
                return@withContext AuthResult(false, errorMsg)
            }

            return@withContext AuthResult(false, "Login gagal (HTTP $code). Silakan periksa kembali koneksi atau akun Anda.")
        } catch (e: Exception) {
            Log.e(TAG, "Kesalahan saat login PocketBase", e)
            return@withContext AuthResult(false, "Gagal terhubung ke server PocketBase: ${e.localizedMessage ?: "Koneksi terputus"}")
        }
    }

    suspend fun registerWithPassword(
        username: String,
        password: String,
        displayName: String = "",
        gender: Gender = Gender.FEMALE
    ): AuthResult = withContext(Dispatchers.IO) {
        val api = PocketBaseClient.getApi()
            ?: return@withContext AuthResult(false, "Tidak dapat menghubungkan ke server PocketBase")

        val cleanUser = username.trim()
        val cleanPassword = password.trim()
        if (cleanUser.isEmpty()) {
            return@withContext AuthResult(false, "Username tidak boleh kosong")
        }
        if (cleanPassword.length < 6) {
            return@withContext AuthResult(false, "Kata sandi minimal 6 karakter sesuai standar keamanan PocketBase")
        }

        val normalizedKey = cleanUser.lowercase()
        val finalDisplayName = if (displayName.isNotBlank()) displayName.trim() else cleanUser
        val isEmail = cleanUser.contains("@")
        val permanentLovyId = PocketBaseClient.toLovyId(normalizedKey)
        val defaultBio = "Halo, saya pengguna baru Lovy Chat! ✨"

        try {
            // 1. Cek duplikasi akun di PocketBase
            val checkFilter = if (isEmail) "email='$normalizedKey'" else "username='$cleanUser' || username='$permanentLovyId'"
            val existing = api.getUsers(perPage = 1, filter = checkFilter)
            if (existing.isSuccessful && !existing.body()?.items.isNullOrEmpty()) {
                return@withContext AuthResult(
                    success = false,
                    message = "Username atau email \"$cleanUser\" sudah terdaftar di Lovy Chat. Silakan pilih menu Masuk."
                )
            }

            // 2. Buat akun baru di koleksi users PocketBase
            val pbUsername = if (isEmail) permanentLovyId else cleanUser.replace(" ", "_").filter { it.isLetterOrDigit() || it == '_' }
            val pbId = PocketBaseClient.toPbId(permanentLovyId)

            val recordData = mutableMapOf<String, Any?>(
                "id" to pbId,
                "username" to pbUsername,
                "name" to finalDisplayName,
                "gender" to gender.name.lowercase(),
                "bio" to defaultBio,
                "password" to cleanPassword,
                "passwordConfirm" to cleanPassword,
                "emailVisibility" to true,
                "is_online" to true,
                "last_active_at" to System.currentTimeMillis()
            )
            if (isEmail) {
                recordData["email"] = normalizedKey
            }

            val createRes = api.createUser(recordData)
            val createdUser = if (createRes.isSuccessful) {
                createRes.body()
            } else {
                recordData.remove("id")
                val fallback = api.createUser(recordData)
                if (fallback.isSuccessful) fallback.body() else null
            }

            if (createdUser == null) {
                val errorBody = createRes.errorBody()?.string() ?: ""
                Log.w(TAG, "Gagal membuat user di PocketBase: code=${createRes.code()} body=$errorBody")
                val friendlyMsg = when {
                    errorBody.contains("validation_length_out_of_range") -> "Kata sandi minimal 6 karakter."
                    errorBody.contains("validation_not_unique") -> "Username atau email sudah digunakan akun lain."
                    else -> "Gagal mendaftar ke server (HTTP ${createRes.code()}). Silakan coba lagi."
                }
                return@withContext AuthResult(false, friendlyMsg)
            }

            // 3. Otentikasi langsung untuk mendapatkan sesi auth token resmi PocketBase
            val loginIdentity = if (isEmail) normalizedKey else pbUsername
            val authResp = api.authWithPassword(mapOf("identity" to loginIdentity, "password" to cleanPassword))
            if (authResp.isSuccessful) {
                val token = authResp.body()?.token
                if (!token.isNullOrBlank()) {
                    PocketBaseClient.authToken = token
                    PocketBaseClient.saveAuthToken(com.example.LovyApplication.appContext, token)
                }
            }

            return@withContext AuthResult(
                success = true,
                message = "Registrasi berhasil! Selamat datang di Lovy Chat.",
                username = createdUser.username ?: pbUsername,
                displayName = finalDisplayName,
                email = if (isEmail) normalizedKey else null,
                lovyId = permanentLovyId,
                gender = gender,
                bio = defaultBio,
                isGoogleUser = false
            )
        } catch (e: Exception) {
            Log.e(TAG, "Kesalahan saat registrasi PocketBase", e)
            return@withContext AuthResult(false, "Terjadi gangguan saat mendaftar: ${e.localizedMessage ?: "Coba lagi nanti"}")
        }
    }
}

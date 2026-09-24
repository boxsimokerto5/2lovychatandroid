package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChatFriendEntity
import com.example.data.local.UserProfileRepository
import com.example.data.supabase.SupabaseClient
import com.example.data.supabase.SupabaseMessageDto
import com.example.data.supabase.SupabaseRealtimeManager
import com.example.data.supabase.SupabaseRepository
import com.example.model.BottleMessage
import com.example.model.ChatConversation
import com.example.model.ChatMessage
import com.example.model.Gender
import com.example.model.MomentItem
import com.example.model.User
import com.example.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import java.util.UUID
import com.example.data.AuthRepository
import com.example.data.AuthResult
import com.example.util.GoogleAuthHelper

sealed interface CurrentScreen {
    object Splash : CurrentScreen
    object Login : CurrentScreen
    object Main : CurrentScreen
    object Nearby : CurrentScreen
    object Bottle : CurrentScreen
    object Moments : CurrentScreen
    object SupabaseConfig : CurrentScreen
    object UserProfile : CurrentScreen
    object NewFriends : CurrentScreen
    data class ChatDetail(val conversationId: String, val partnerName: String, val partnerAvatarHex: Long) : CurrentScreen
}

data class LovyChatUiState(
    val currentTab: Int = 2, // Default to Temukan (matching the screenshot)
    val currentScreen: CurrentScreen = CurrentScreen.Splash,
    val isLoggedIn: Boolean = false,
    val isGuest: Boolean = false,
    val userProfile: UserProfile = UserProfile(),
    val nearbyUsers: List<User> = emptyList(),
    val chattedFriends: List<User> = emptyList(),
    val newFriendRequests: List<com.example.model.NewFriendRequest> = emptyList(),
    val ignoredNewFriendIds: Set<String> = emptySet(),

    val nearbyGenderFilter: Gender? = null,
    val nearbyOnlyOnlineFilter: Boolean = false,
    val isScanningNearby: Boolean = false,
    val conversations: List<ChatConversation> = emptyList(),
    val messagesMap: Map<String, List<ChatMessage>> = emptyMap(),
    val oceanBottles: List<BottleMessage> = emptyList(),
    val myBottles: List<BottleMessage> = emptyList(),
    val fishedBottles: List<BottleMessage> = emptyList(),
    val fishedBottle: BottleMessage? = null,
    val isFishing: Boolean = false,
    val moments: List<MomentItem> = emptyList(),
    val momentComments: Map<String, List<com.example.model.MomentComment>> = emptyMap(),
    val activeChatId: String? = null,
    val myName: String = "",
    val myBio: String = "",
    val myLovyId: String = "",
    // Supabase Connection State
    val isSupabaseConnected: Boolean = false,
    val supabaseUrl: String = "",
    val supabaseAnonKey: String = "",
    val isTestingConnection: Boolean = false,
    val connectionStatusMessage: String? = null,
    // Language and Geographic Settings
    val isLocalLanguageMode: Boolean = true,
    val language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    val detectedLocalLanguage: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    val detectedGeoArea: String = "Indonesia / Malaysia (ID/MY)",
    // Android Native GPS State
    val currentGpsLocation: com.example.util.UserGpsLocation? = null,
    val hasLocationPermission: Boolean = false,
    val isGpsEnabled: Boolean = true,
    // Nearby Search Expansion (Rewarded Ad trigger: Tier 0 = max 12, Tier 1 = max 30, Tier 2 = max 45, Tier 3 = max 70, Tier 4 = max 100, Tier 5 = max 125)
    val isNearbyExpanded: Boolean = false,
    val nearbyExpansionTier: Int = 0,
    // Blocked Users State (Prevents chats & hides from Around Me)
    val blockedUserIds: Set<String> = emptySet(),
    val blockedUserNames: Set<String> = emptySet(),
    // Cloudflare R2 State
    val isR2Configured: Boolean = false,
    val r2AccountId: String = "",
    val r2BucketName: String = "lovychat",
    val r2PublicDomain: String = "",
    val isUploadingPhoto: Boolean = false,
    val uploadProgressText: String? = null,
    val isRefreshingMoments: Boolean = false,
    // My Moments tracking (IDs of moments created by this user)
    val myMomentIds: Set<String> = emptySet(),
    val reportedMomentIds: Set<String> = emptySet(),
    // Privacy and Location Settings
    val isNearbyVisible: Boolean = true,
    val hideExactDistance: Boolean = false,
    val showOnlineStatus: Boolean = true,
    val fcmToken: String = "",
    val typingMap: Map<String, Boolean> = emptyMap()
)

class LovyChatViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs by lazy {
        getApplication<Application>().getSharedPreferences("lovy_chat_prefs", android.content.Context.MODE_PRIVATE)
    }
    private val supabaseRepo = SupabaseRepository()
    private val userProfileRepo by lazy {
        val app = getApplication<Application>()
        val db = AppDatabase.getInstance(app)
        UserProfileRepository(db.userProfileDao())
    }
    private val chatFriendDao by lazy {
        val app = getApplication<Application>()
        val db = AppDatabase.getInstance(app)
        db.chatFriendDao()
    }
    private val localChatRepo by lazy {
        com.example.data.local.LocalChatRepository.getInstance(getApplication<Application>())
    }
    private val authRepo by lazy {
        AuthRepository(getApplication<Application>(), supabaseRepo)
    }

    // --- Strategi Hemat Kuota Database untuk 100k+ Users (Smart Caching & Throttling) ---
    private var lastNearbyScanTime = 0L
    private var lastMomentsSyncTime = 0L
    private var lastBottlesSyncTime = 0L
    private var lastChatSyncMap = mutableMapOf<String, Long>()
    private var lastGpsUpdateTimestamp = 0L
    private var lastSyncedLat: Double? = null
    private var lastSyncedLon: Double? = null
    private var lastUserActivityTimestamp = 0L
    private var lastTypingSentTime = 0L
    private val typingTimeoutJobs = mutableMapOf<String, kotlinx.coroutines.Job>()

    companion object {
        // Cache data selama 3 menit untuk memangkas 80%+ query baca ke cloud
        private const val CACHE_DURATION_MS = 3 * 60 * 1000L
        // Pembaruan GPS di-throttle: hanya jika berpindah > 500m atau jeda > 10 menit
        private const val GPS_THROTTLE_MIN_DISTANCE_METERS = 500.0
        private const val GPS_THROTTLE_MIN_INTERVAL_MS = 10 * 60 * 1000L
        // User activity heartbeat di-throttle ke database cloud minimal jeda 5 menit
        private const val USER_ACTIVITY_THROTTLE_MS = 5 * 60 * 1000L
    }

    private val _uiState = MutableStateFlow(LovyChatUiState())
    val uiState: StateFlow<LovyChatUiState> = _uiState.asStateFlow()

    init {
        try {
            val ctx = try { application.applicationContext } catch (_: Throwable) { null } ?: application
            SupabaseClient.init(ctx)
            com.example.data.storage.R2StorageClient.init(ctx)
        } catch (_: Throwable) {
        }
        loadBlockedUsers()
        loadMyMoments()
        loadSavedBottles()
        loadPrivacySettings()
        refreshSupabaseState()
        refreshR2State()
        detectAndApplyGeoLanguage()
        checkInitialGpsLocation()
        observeUserProfile()
        observeChatFriends()
        updateUserActivity()
        initFirebaseMessaging()
        // Pulihkan sesi login jika sebelumnya pengguna sudah masuk
        try {
            val savedSession = authRepo.getSavedSession()
            if (savedSession != null && savedSession.isLoggedIn && !savedSession.isGuest) {
                clearDummyFriends()
                val sessionName = savedSession.displayName.takeIf { it.isNotBlank() && !it.equals("Pengguna Lovy", ignoreCase = true) }
                    ?: if (savedSession.username.contains("@")) {
                        savedSession.username.substringBefore("@").replaceFirstChar { it.uppercase() }
                    } else {
                        savedSession.username.ifBlank { "Pengguna" }
                    }
                val sessionEmail = savedSession.email ?: if (savedSession.username.contains("@")) savedSession.username else null
                val sessionLovyId = savedSession.lovyId.takeIf { it.isNotBlank() && it != "lovy_889214" } ?: "lovy_${(100000..999999).random()}"
                val sessionBio = savedSession.bio.takeIf { it != "Menjelajahi dunia dan mencari teman baru di Lovy Chat ✨" } ?: ""

                val restoredProfile = UserProfile(
                    id = "current_user",
                    displayName = sessionName,
                    bio = sessionBio,
                    profilePicture = savedSession.avatarUrl,
                    email = sessionEmail,
                    lovyId = sessionLovyId,
                    city = savedSession.city ?: "",
                    gender = savedSession.gender.name,
                    age = savedSession.age ?: 22
                )

                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        isGuest = false,
                        myName = sessionName,
                        myBio = sessionBio,
                        myLovyId = sessionLovyId,
                        userProfile = restoredProfile,
                        conversations = emptyList(),
                        messagesMap = emptyMap(),
                        oceanBottles = emptyList(),
                        nearbyUsers = emptyList()
                    )
                }
                loadMyMoments()

                // Tulis profil pulihan ke Room secara instan agar tidak kosong jika app baru diperbarui
                viewModelScope.launch {
                    try {
                        userProfileRepo.saveProfile(restoredProfile)
                    } catch (_: Exception) {}

                    // Sinkronisasi data akun terbaru dari Supabase di latar belakang
                    try {
                        if (SupabaseClient.isConfigured()) {
                            val cloudAccount = when {
                                sessionEmail != null -> supabaseRepo.findAccountByGoogle(sessionEmail)
                                savedSession.isGoogleUser && savedSession.username.contains("@") -> supabaseRepo.findAccountByGoogle(savedSession.username)
                                savedSession.username.isNotBlank() -> supabaseRepo.findAccountByUsername(savedSession.username)
                                else -> supabaseRepo.findAccountById(sessionLovyId)
                            }
                            if (cloudAccount != null) {
                                val cloudDisplayName = cloudAccount.displayName?.takeIf { it.isNotBlank() && !it.equals("Pengguna Lovy", ignoreCase = true) }
                                    ?: sessionName
                                val cloudAvatar = cloudAccount.avatarUrl ?: savedSession.avatarUrl
                                val cloudBio = cloudAccount.bio?.takeIf { it != "Menjelajahi dunia dan mencari teman baru di Lovy Chat ✨" } ?: sessionBio
                                val cloudEmail = cloudAccount.googleEmail ?: sessionEmail
                                val cloudGender = if (cloudAccount.gender?.equals("MALE", ignoreCase = true) == true) "MALE" else "FEMALE"

                                val fullySyncedProfile = restoredProfile.copy(
                                    displayName = cloudDisplayName,
                                    profilePicture = cloudAvatar,
                                    bio = cloudBio,
                                    email = cloudEmail,
                                    gender = cloudGender
                                )

                                userProfileRepo.saveProfile(fullySyncedProfile)
                                _uiState.update {
                                    it.copy(
                                        myName = cloudDisplayName,
                                        myBio = cloudBio,
                                        userProfile = fullySyncedProfile
                                    )
                                }
                                authRepo.saveSession(
                                    savedSession.copy(
                                        displayName = cloudDisplayName,
                                        avatarUrl = cloudAvatar,
                                        bio = cloudBio,
                                        email = cloudEmail
                                    )
                                )
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("LovyChatViewModel", "Sync cloud account on init background: ${e.message}")
                    }
                }
            } else if (savedSession != null && savedSession.isGuest) {
                authRepo.clearSession()
            }
        } catch (e: Exception) {
            Log.w("LovyChatViewModel", "Gagal memulihkan sesi login: ${e.message}")
        }
        // Coba sinkronisasi data awal jika Supabase sudah terkonfigurasi
        syncFromSupabase()
        refreshNewFriendRequests()
        syncFcmTokenToSupabase()
        if (!_uiState.value.isGuest && _uiState.value.myLovyId.isNotBlank()) {
            startIncomingChatPeriodicSync()
            startRealtimeChatSubscription()
        }
    }

    fun isDummyFriend(userId: String, userName: String): Boolean {
        val dummyNames = setOf(
            "siti rahma", "rian pratama", "nadia putri", "dimas anggara", 
            "alya zahra", "pengguna lovy", "rania putri", "clara monica",
            "dimas danendra", "clarissa aurelia", "salma salsabil"
        )
        val isMockId = userId.matches(Regex("^u[0-9]+$"))
        return isMockId || dummyNames.contains(userName.trim().lowercase())
    }

    private fun observeChatFriends() {
        viewModelScope.launch {
            try {
                chatFriendDao.getAllFriendsFlow().collect { friendEntities ->
                    val friends = friendEntities.map { it.toUser() }
                    val finalFriends = if (!_uiState.value.isGuest) {
                        friends.filterNot { isDummyFriend(it.id, it.name) }
                    } else {
                        friends
                    }
                    _uiState.update { it.copy(chattedFriends = finalFriends) }
                    refreshNewFriendRequests(finalFriends)
                }
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal memuat teman mengobrol", e)
            }
        }
    }

    fun refreshNewFriendRequests(currentFriends: List<User> = _uiState.value.chattedFriends) {
        val state = _uiState.value
        val friendIds = currentFriends.map { it.id }.toSet()
        val ignoredIds = state.ignoredNewFriendIds

        val requestsMap = state.newFriendRequests
            .filterNot { it.user.id in friendIds || it.user.id in ignoredIds }
            .associateBy { it.user.id }
            .toMutableMap()

        for (conv in state.conversations) {
            val partnerId = conv.partnerId
            if (partnerId.isBlank() || partnerId in friendIds || partnerId in ignoredIds) continue

            // Pengguna lain yang mengirimi pesan obrolan tapi belum ada di Kontak Saya
            if (!conv.lastMessageIsFromMe || conv.unreadCount > 0) {
                if (!requestsMap.containsKey(partnerId)) {
                    val candidateUser = state.nearbyUsers.find { it.id == partnerId }
                        ?: User(
                            id = partnerId,
                            name = conv.partnerName,
                            gender = conv.partnerGender,
                            age = conv.partnerAge,
                            distanceMeters = conv.partnerDistanceMeters,
                            bio = "Mengirimi Anda pesan obrolan di Lovy Chat",
                            avatarColorHex = conv.partnerAvatarHex,
                            avatarUrl = conv.partnerAvatarUrl,
                            city = conv.partnerCity ?: "Jakarta",
                            isOnline = conv.isOnline
                        )
                    requestsMap[partnerId] = com.example.model.NewFriendRequest(
                        id = partnerId,
                        user = candidateUser,
                        greetingMessage = conv.lastMessage,
                        timestamp = conv.lastTimestamp
                    )
                }
            }
        }

        val sortedList = requestsMap.values.sortedByDescending { it.timestamp }
        _uiState.update { it.copy(newFriendRequests = sortedList) }
    }

    fun acceptNewFriend(user: User) {
        recordFeatureClick()
        saveChatFriend(user)
        _uiState.update { state ->
            val updatedFriends = if (state.chattedFriends.any { it.id == user.id }) {
                state.chattedFriends
            } else {
                state.chattedFriends + user
            }
            state.copy(
                chattedFriends = updatedFriends,
                newFriendRequests = state.newFriendRequests.filterNot { it.user.id == user.id }
            )
        }
    }

    /**
     * Mencari pengguna berdasarkan kode QR / Barcode yang dipindai (baik kamera live atau unggah galeri).
     */
    fun searchUserByQrCode(rawCode: String, onResult: (User?) -> Unit) {
        val cleanId = com.example.util.QrCodeDecoder.extractUserId(rawCode)
        if (cleanId.isBlank()) {
            onResult(null)
            return
        }

        // 1. Cek di daftar teman yang sudah tersimpan
        val existingFriend = _uiState.value.chattedFriends.find {
            it.id.equals(cleanId, ignoreCase = true) || it.name.equals(cleanId, ignoreCase = true)
        }
        if (existingFriend != null) {
            onResult(existingFriend)
            return
        }

        // 2. Cek di daftar pengguna sekitar
        val nearbyMatch = _uiState.value.nearbyUsers.find {
            it.id.equals(cleanId, ignoreCase = true) || it.name.equals(cleanId, ignoreCase = true)
        }
        if (nearbyMatch != null) {
            onResult(nearbyMatch)
            return
        }

        // 3. Cek di daftar obrolan aktif
        val convMatch = _uiState.value.conversations.find {
            it.partnerId.equals(cleanId, ignoreCase = true) || it.partnerName.equals(cleanId, ignoreCase = true)
        }
        if (convMatch != null) {
            val convUser = User(
                id = convMatch.partnerId,
                name = convMatch.partnerName,
                gender = convMatch.partnerGender,
                age = convMatch.partnerAge,
                distanceMeters = convMatch.partnerDistanceMeters,
                bio = "Teman obrolan Lovy",
                avatarColorHex = convMatch.partnerAvatarHex,
                isOnline = convMatch.isOnline,
                avatarUrl = convMatch.partnerAvatarUrl,
                city = convMatch.partnerCity ?: "Indonesia"
            )
            onResult(convUser)
            return
        }

        // 4. Cari dari database Supabase jika tersambung
        if (com.example.data.supabase.SupabaseClient.isConfigured() && !_uiState.value.isGuest) {
            viewModelScope.launch(Dispatchers.IO) {
                val cloudUser = supabaseRepo.fetchNearbyUserById(cleanId)
                val accountUser = if (cloudUser == null) {
                    val acc = supabaseRepo.findAccountById(cleanId) ?: supabaseRepo.findAccountByUsername(cleanId)
                    if (acc != null) {
                        User(
                            id = acc.id,
                            name = acc.displayName?.ifBlank { acc.username } ?: acc.username,
                            gender = if (acc.gender.equals("male", true)) Gender.MALE else Gender.FEMALE,
                            age = 22,
                            distanceMeters = 100,
                            bio = acc.bio ?: "Pengguna Lovy Chat",
                            avatarColorHex = 0xFF00A86B,
                            isOnline = true,
                            city = "Indonesia",
                            avatarUrl = acc.avatarUrl
                        )
                    } else null
                } else null

                val foundUser = cloudUser ?: accountUser
                withContext(Dispatchers.Main) {
                    if (foundUser != null) {
                        onResult(foundUser)
                    } else {
                        val fallbackUser = User(
                            id = cleanId,
                            name = if (cleanId.startsWith("lovy_")) "Teman Lovy (${cleanId.takeLast(4)})" else cleanId,
                            gender = Gender.FEMALE,
                            age = 22,
                            distanceMeters = 100,
                            bio = "Teman ditemukan lewat pemindaian barcode",
                            avatarColorHex = 0xFF00A86B,
                            isOnline = true,
                            city = "Indonesia"
                        )
                        onResult(fallbackUser)
                    }
                }
            }
        } else {
            val fallbackUser = User(
                id = cleanId,
                name = if (cleanId.startsWith("lovy_")) "Teman Lovy (${cleanId.takeLast(4)})" else cleanId,
                gender = Gender.FEMALE,
                age = 22,
                distanceMeters = 100,
                bio = "Teman ditemukan lewat pemindaian barcode",
                avatarColorHex = 0xFF00A86B,
                isOnline = true,
                city = "Indonesia"
            )
            onResult(fallbackUser)
        }
    }

    fun ignoreNewFriend(userId: String) {
        recordFeatureClick()
        _uiState.update { state ->
            state.copy(
                ignoredNewFriendIds = state.ignoredNewFriendIds + userId,
                newFriendRequests = state.newFriendRequests.filterNot { it.user.id == userId }
            )
        }
    }

    fun simulateIncomingChatFromNewUser() {
        // Simulasi bot dihapus - hanya obrolan nyata 2 arah dari pengguna asli
    }

    fun saveChatFriend(user: User) {
        if (!_uiState.value.isGuest && isDummyFriend(user.id, user.name)) {
            // Abaikan penyimpanan user dummy jika pengguna sedang berada di akun asli
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Pertahankan status isFavorite jika teman sudah ditandai favorit sebelumnya
                val currentFriend = _uiState.value.chattedFriends.find { it.id == user.id }
                val isFav = user.isFavorite || (currentFriend?.isFavorite == true)
                chatFriendDao.insertOrUpdateFriend(ChatFriendEntity.fromUser(user.copy(isFavorite = isFav)))
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal menyimpan teman mengobrol", e)
            }
        }
    }

    fun toggleFavoriteFriend(user: User) {
        val newFavorite = !user.isFavorite
        recordFeatureClick()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatFriendDao.updateFavoriteStatus(user.id, newFavorite)
                val allFriends = chatFriendDao.getAllFriends()
                if (allFriends.none { it.id == user.id }) {
                    chatFriendDao.insertOrUpdateFriend(ChatFriendEntity.fromUser(user.copy(isFavorite = newFavorite)))
                }
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal update status favorit teman", e)
            }
        }
        _uiState.update { state ->
            val updated = state.chattedFriends.map { f ->
                if (f.id == user.id) f.copy(isFavorite = newFavorite) else f
            }
            val finalFriends = if (updated.any { it.id == user.id }) {
                updated
            } else {
                updated + user.copy(isFavorite = newFavorite)
            }
            state.copy(chattedFriends = finalFriends)
        }
    }

    fun setPartnerTyping(conversationId: String, isTyping: Boolean) {
        _uiState.update { state ->
            val updated = state.typingMap.toMutableMap()
            if (isTyping) {
                updated[conversationId] = true
            } else {
                updated.remove(conversationId)
            }
            state.copy(typingMap = updated)
        }
    }

    fun onUserTyping(conversationId: String, partnerId: String, isTyping: Boolean) {
        if (_uiState.value.isGuest) return
        val now = System.currentTimeMillis()
        // Kirim status mengetik: throttle 3 detik jika sedang mengetik, atau segera kirim jika berhenti
        if (!isTyping || now - lastTypingSentTime > 3000L) {
            lastTypingSentTime = now
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val msgText = if (isTyping) "__TYPING_START__" else "__TYPING_STOP__"
                    val ephemeralMsg = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        conversationId = conversationId,
                        text = msgText,
                        timestamp = now,
                        isFromMe = true,
                        deletedForSender = true,
                        deletedForReceiver = false
                    )
                    supabaseRepo.sendChatMessage(
                        message = ephemeralMsg,
                        senderId = _uiState.value.myLovyId,
                        receiverId = partnerId
                    )
                } catch (e: Exception) {
                    Log.w("LovyChatViewModel", "Gagal mengirim status mengetik", e)
                }
            }
        }
    }

    fun deleteChatFriend(userId: String, userName: String) {
        recordFeatureClick()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatFriendDao.deleteFriend(userId, userName)
                chatFriendDao.deleteFriendById(userId)
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal menghapus teman", e)
            }
        }
        _uiState.update { state ->
            val updated = state.chattedFriends.filterNot { 
                it.id == userId || it.name.equals(userName, ignoreCase = true) 
            }
            state.copy(chattedFriends = updated)
        }
    }

    fun clearDummyFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatFriendDao.deleteDummyFriends()
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal membersihkan kontak dummy", e)
            }
        }
        _uiState.update { state ->
            val updated = state.chattedFriends.filterNot { isDummyFriend(it.id, it.name) }
            state.copy(chattedFriends = updated)
        }
    }

    fun clearAllFriends() {
        recordFeatureClick()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatFriendDao.deleteAllFriends()
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal menghapus semua teman", e)
            }
        }
        _uiState.update { state ->
            state.copy(chattedFriends = emptyList())
        }
    }

    fun updateUserActivity() {
        if (_uiState.value.isGuest) return // Mode Tamu tidak mengirim heartbeat ke Supabase
        val now = System.currentTimeMillis()
        if (now - lastUserActivityTimestamp < USER_ACTIVITY_THROTTLE_MS) {
            // Abaikan heartbeat berulang jika belum lewat 5 menit (sangat menghemat kuota tulis)
            return
        }
        lastUserActivityTimestamp = now
        viewModelScope.launch(Dispatchers.IO) {
            try {
                supabaseRepo.updateUserLastActive(_uiState.value.myLovyId)
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal update last_active_at", e)
            }
        }
    }

    private fun initFirebaseMessaging() {
        try {
            com.example.util.LovyFirebaseMessagingService.createNotificationChannel(getApplication())
            val fcmPrefs = getApplication<Application>()
                .getSharedPreferences("lovy_fcm_prefs", android.content.Context.MODE_PRIVATE)
            val cachedToken = fcmPrefs.getString("fcm_token", null)
            if (!cachedToken.isNullOrBlank()) {
                _uiState.update { it.copy(fcmToken = cachedToken) }
                syncFcmTokenToSupabase(cachedToken)
            }
            com.google.firebase.messaging.FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && !task.result.isNullOrBlank()) {
                        val token = task.result
                        _uiState.update { it.copy(fcmToken = token) }
                        fcmPrefs.edit().putString("fcm_token", token).apply()
                        Log.d("LovyFCM", "Current FCM Token fetched: $token")
                        syncFcmTokenToSupabase(token)
                    }
                }
        } catch (e: Throwable) {
            Log.w("LovyFCM", "Inisialisasi FCM dilewati atau belum tersedia: ${e.message}")
        }
    }

    fun syncFcmTokenToSupabase(token: String = _uiState.value.fcmToken) {
        if (token.isBlank() || _uiState.value.isGuest || !SupabaseClient.isConfigured()) return
        val myId = _uiState.value.myLovyId
        if (myId.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                supabaseRepo.updateUserFcmToken(myId, token)
                Log.d("LovyFCM", "FCM token berhasil diperbarui di Supabase untuk: $myId")
            } catch (e: Exception) {
                Log.w("LovyFCM", "Gagal memperbarui FCM token di Supabase", e)
            }
        }
    }

    private fun loadBlockedUsers() {
        try {
            val savedIds = prefs.getStringSet("blocked_user_ids", emptySet()) ?: emptySet()
            val savedNames = prefs.getStringSet("blocked_user_names", emptySet()) ?: emptySet()
            val savedReportedMoments = prefs.getStringSet("reported_moment_ids", emptySet()) ?: emptySet()
            _uiState.update { current ->
                val filteredNearby = current.nearbyUsers.filterNot { u ->
                    savedIds.contains(u.id) || savedNames.any { n -> n.equals(u.name, ignoreCase = true) }
                }
                val filteredMoments = current.moments.filterNot { savedReportedMoments.contains(it.id) }
                current.copy(
                    blockedUserIds = savedIds,
                    blockedUserNames = savedNames,
                    reportedMomentIds = savedReportedMoments,
                    nearbyUsers = filteredNearby,
                    moments = filteredMoments
                )
            }
        } catch (_: Throwable) {
        }
    }

    private fun loadMyMoments() {
        try {
            val savedIds = prefs.getStringSet("my_moment_ids", emptySet()) ?: emptySet()
            val rawMomentsJson = prefs.getString("my_local_moments_json", null)
            val locallySavedMoments = if (!rawMomentsJson.isNullOrBlank()) {
                val arr = org.json.JSONArray(rawMomentsJson)
                val list = mutableListOf<MomentItem>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(
                        MomentItem(
                            id = obj.optString("id"),
                            authorName = obj.optString("authorName"),
                            authorAvatarHex = obj.optLong("authorAvatarHex", 0xFF00A86B),
                            content = obj.optString("content"),
                            timeAgo = obj.optString("timeAgo", "Baru saja"),
                            likesCount = obj.optInt("likesCount", 0),
                            commentsCount = obj.optInt("commentsCount", 0),
                            isLiked = false,
                            imageUrl = if (obj.has("imageUrl")) obj.optString("imageUrl").takeIf { it.isNotBlank() } else null,
                            authorAvatarUrl = if (obj.has("authorAvatarUrl")) obj.optString("authorAvatarUrl").takeIf { it.isNotBlank() } else null,
                            locationTag = if (obj.has("locationTag")) obj.optString("locationTag").takeIf { it.isNotBlank() } else null,
                            authorId = obj.optString("authorId", "me")
                        )
                    )
                }
                list
            } else emptyList()

            _uiState.update { 
                it.copy(
                    myMomentIds = savedIds,
                    moments = (locallySavedMoments + it.moments).distinctBy { m -> m.id }
                ) 
            }
        } catch (_: Throwable) {
        }
    }

    private fun saveMyLocalMoment(moment: MomentItem) {
        try {
            val rawMomentsJson = prefs.getString("my_local_moments_json", null)
            val currentList = if (!rawMomentsJson.isNullOrBlank()) {
                val arr = org.json.JSONArray(rawMomentsJson)
                val list = mutableListOf<org.json.JSONObject>()
                for (i in 0 until arr.length()) {
                    list.add(arr.getJSONObject(i))
                }
                list
            } else mutableListOf()

            // Buat json object untuk moment baru
            val obj = org.json.JSONObject().apply {
                put("id", moment.id)
                put("authorName", moment.authorName)
                put("authorAvatarHex", moment.authorAvatarHex)
                put("content", moment.content)
                put("timeAgo", moment.timeAgo)
                put("likesCount", moment.likesCount)
                put("commentsCount", moment.commentsCount)
                if (moment.imageUrl != null) put("imageUrl", moment.imageUrl)
                if (moment.authorAvatarUrl != null) put("authorAvatarUrl", moment.authorAvatarUrl)
                if (moment.locationTag != null) put("locationTag", moment.locationTag)
                put("authorId", moment.authorId)
            }
            // Sisipkan di posisi terdepan dan batasi 50 momen terakhir
            val updated = (listOf(obj) + currentList.filterNot { it.optString("id") == moment.id }).take(50)
            val newArr = org.json.JSONArray()
            updated.forEach { newArr.put(it) }
            prefs.edit().putString("my_local_moments_json", newArr.toString()).apply()
        } catch (_: Throwable) {
        }
    }

    private fun removeMyLocalMoment(momentId: String) {
        try {
            val rawMomentsJson = prefs.getString("my_local_moments_json", null) ?: return
            val arr = org.json.JSONArray(rawMomentsJson)
            val newArr = org.json.JSONArray()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                if (obj.optString("id") != momentId) {
                    newArr.put(obj)
                }
            }
            prefs.edit().putString("my_local_moments_json", newArr.toString()).apply()
        } catch (_: Throwable) {
        }
    }

    private fun loadSavedBottles() {
        try {
            val savedFished = loadFishedBottles()
            val savedMine = loadMyBottles()
            _uiState.update { 
                it.copy(
                    fishedBottles = savedFished,
                    myBottles = if (savedMine.isNotEmpty()) savedMine else it.myBottles
                ) 
            }
        } catch (_: Throwable) {
        }
    }

    private fun saveFishedBottles(bottles: List<BottleMessage>) {
        try {
            val jsonArray = org.json.JSONArray()
            for (b in bottles) {
                val obj = org.json.JSONObject()
                obj.put("id", b.id)
                obj.put("senderId", b.senderId)
                obj.put("senderName", b.senderName)
                obj.put("senderGender", b.senderGender.name)
                obj.put("avatarHex", b.avatarHex)
                obj.put("content", b.content)
                obj.put("thrownTimestamp", b.thrownTimestamp)
                obj.put("locationHint", b.locationHint)
                obj.put("isFromMe", b.isFromMe)
                obj.put("replyCount", b.replyCount)
                if (b.avatarUrl != null) obj.put("avatarUrl", b.avatarUrl)
                jsonArray.put(obj)
            }
            prefs.edit().putString("fished_bottles_json", jsonArray.toString()).apply()
        } catch (_: Throwable) {
        }
    }

    private fun loadFishedBottles(): List<BottleMessage> {
        try {
            val raw = prefs.getString("fished_bottles_json", null) ?: return emptyList()
            val jsonArray = org.json.JSONArray(raw)
            val list = mutableListOf<BottleMessage>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    BottleMessage(
                        id = obj.optString("id"),
                        senderId = obj.optString("senderId"),
                        senderName = obj.optString("senderName"),
                        senderGender = try { Gender.valueOf(obj.optString("senderGender", "MALE")) } catch (_: Throwable) { Gender.MALE },
                        avatarHex = obj.optLong("avatarHex", 0xFF00838F),
                        content = obj.optString("content"),
                        thrownTimestamp = obj.optLong("thrownTimestamp", System.currentTimeMillis()),
                        locationHint = obj.optString("locationHint", "Lautan Lovy"),
                        isFromMe = obj.optBoolean("isFromMe", false),
                        replyCount = obj.optInt("replyCount", 0),
                        avatarUrl = if (obj.has("avatarUrl")) obj.optString("avatarUrl") else null
                    )
                )
            }
            return list
        } catch (_: Throwable) {
            return emptyList()
        }
    }

    private fun saveMyBottles(bottles: List<BottleMessage>) {
        try {
            val jsonArray = org.json.JSONArray()
            for (b in bottles) {
                val obj = org.json.JSONObject()
                obj.put("id", b.id)
                obj.put("senderId", b.senderId)
                obj.put("senderName", b.senderName)
                obj.put("senderGender", b.senderGender.name)
                obj.put("avatarHex", b.avatarHex)
                obj.put("content", b.content)
                obj.put("thrownTimestamp", b.thrownTimestamp)
                obj.put("locationHint", b.locationHint)
                obj.put("isFromMe", b.isFromMe)
                obj.put("replyCount", b.replyCount)
                if (b.avatarUrl != null) obj.put("avatarUrl", b.avatarUrl)
                jsonArray.put(obj)
            }
            prefs.edit().putString("my_bottles_json", jsonArray.toString()).apply()
        } catch (_: Throwable) {
        }
    }

    private fun loadMyBottles(): List<BottleMessage> {
        try {
            val raw = prefs.getString("my_bottles_json", null) ?: return emptyList()
            val jsonArray = org.json.JSONArray(raw)
            val list = mutableListOf<BottleMessage>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    BottleMessage(
                        id = obj.optString("id"),
                        senderId = obj.optString("senderId"),
                        senderName = obj.optString("senderName"),
                        senderGender = try { Gender.valueOf(obj.optString("senderGender", "MALE")) } catch (_: Throwable) { Gender.MALE },
                        avatarHex = obj.optLong("avatarHex", 0xFF00A86B),
                        content = obj.optString("content"),
                        thrownTimestamp = obj.optLong("thrownTimestamp", System.currentTimeMillis()),
                        locationHint = obj.optString("locationHint", "Laut Nusantara"),
                        isFromMe = true,
                        replyCount = obj.optInt("replyCount", 0),
                        avatarUrl = if (obj.has("avatarUrl")) obj.optString("avatarUrl") else null
                    )
                )
            }
            return list
        } catch (_: Throwable) {
            return emptyList()
        }
    }

    private fun loadPrivacySettings() {
        try {
            val visible = prefs.getBoolean("pref_nearby_visible", true)
            val hideDist = prefs.getBoolean("pref_hide_exact_distance", false)
            val online = prefs.getBoolean("pref_show_online_status", true)
            _uiState.update {
                it.copy(
                    isNearbyVisible = visible,
                    hideExactDistance = hideDist,
                    showOnlineStatus = online
                )
            }
        } catch (_: Throwable) {
        }
    }

    fun setNearbyVisible(visible: Boolean) {
        try {
            prefs.edit().putBoolean("pref_nearby_visible", visible).apply()
        } catch (_: Throwable) {
        }
        _uiState.update { it.copy(isNearbyVisible = visible) }
    }

    fun setHideExactDistance(hide: Boolean) {
        try {
            prefs.edit().putBoolean("pref_hide_exact_distance", hide).apply()
        } catch (_: Throwable) {
        }
        _uiState.update { it.copy(hideExactDistance = hide) }
    }

    fun setShowOnlineStatus(show: Boolean) {
        try {
            prefs.edit().putBoolean("pref_show_online_status", show).apply()
        } catch (_: Throwable) {
        }
        _uiState.update { it.copy(showOnlineStatus = show) }
    }

    fun isUserBlocked(userId: String = "", userName: String = ""): Boolean {
        val state = _uiState.value
        if (userId.isNotBlank() && state.blockedUserIds.contains(userId)) return true
        if (userName.isNotBlank() && state.blockedUserNames.any { it.equals(userName, ignoreCase = true) }) return true
        return false
    }

    fun blockUser(userId: String, userName: String) {
        val currentIds = _uiState.value.blockedUserIds
        val currentNames = _uiState.value.blockedUserNames
        val newIds = if (userId.isNotBlank()) currentIds + userId else currentIds
        val newNames = if (userName.isNotBlank()) currentNames + userName else currentNames

        try {
            prefs.edit()
                .putStringSet("blocked_user_ids", newIds)
                .putStringSet("blocked_user_names", newNames)
                .apply()
        } catch (_: Throwable) {
        }

        _uiState.update { current ->
            val filteredNearby = current.nearbyUsers.filterNot { u ->
                (userId.isNotBlank() && u.id == userId) ||
                (userName.isNotBlank() && u.name.equals(userName, ignoreCase = true))
            }
            current.copy(
                blockedUserIds = newIds,
                blockedUserNames = newNames,
                nearbyUsers = filteredNearby
            )
        }
    }

    fun unblockUser(userId: String, userName: String) {
        val newIds = _uiState.value.blockedUserIds.filterNot { it == userId || (userId.isNotBlank() && it == userId) }.toSet()
        val newNames = _uiState.value.blockedUserNames.filterNot { it.equals(userName, ignoreCase = true) }.toSet()

        try {
            prefs.edit()
                .putStringSet("blocked_user_ids", newIds)
                .putStringSet("blocked_user_names", newNames)
                .apply()
        } catch (_: Throwable) {
        }

        _uiState.update { current ->
            current.copy(
                blockedUserIds = newIds,
                blockedUserNames = newNames
            )
        }
        refreshNearbyScan(forceRefresh = false)
    }

    /**
     * Melaporkan pengguna ke sistem moderasi Lovy Chat (Google Play UGC compliance).
     */
    fun reportUser(userId: String, userName: String, reason: String, notes: String, alsoBlock: Boolean = true) {
        Log.i("LovyChatViewModel", "User reported: id=$userId, name=$userName, reason=$reason, notes=$notes")
        try {
            val reportKey = "reported_user_${System.currentTimeMillis()}"
            prefs.edit().putString(reportKey, "target=$userName,id=$userId,reason=$reason,notes=$notes").apply()
        } catch (_: Throwable) {}

        if (alsoBlock) {
            blockUser(userId, userName)
        }
    }

    /**
     * Melaporkan postingan Momen yang melanggar aturan dan menyembunyikannya langsung dari feed pengguna.
     */
    fun reportMoment(momentId: String, authorName: String, reason: String, notes: String) {
        Log.i("LovyChatViewModel", "Moment reported: id=$momentId, author=$authorName, reason=$reason, notes=$notes")
        val currentReported = _uiState.value.reportedMomentIds + momentId
        try {
            prefs.edit()
                .putStringSet("reported_moment_ids", currentReported)
                .putString("report_moment_${momentId}_${System.currentTimeMillis()}", "author=$authorName,reason=$reason,notes=$notes")
                .apply()
        } catch (_: Throwable) {}

        _uiState.update { current ->
            current.copy(
                reportedMomentIds = currentReported,
                moments = current.moments.filterNot { it.id == momentId }
            )
        }
    }

    private fun enrichMomentsWithAvatars(
        moments: List<MomentItem>,
        overrideMyAvatar: String? = null
    ): List<MomentItem> {
        val myId = _uiState.value.myLovyId
        val myName = _uiState.value.myName
        val myAvatar = overrideMyAvatar?.takeIf { it.isNotBlank() }
            ?: _uiState.value.userProfile.profilePicture?.takeIf { it.isNotBlank() }
        val nearbyMap = _uiState.value.nearbyUsers.associateBy { it.id }

        return moments.map { moment ->
            if (!moment.authorAvatarUrl.isNullOrBlank()) {
                moment
            } else if ((myId.isNotBlank() && moment.authorId == myId) || (myName.isNotBlank() && moment.authorName == myName)) {
                if (!myAvatar.isNullOrBlank()) moment.copy(authorAvatarUrl = myAvatar) else moment
            } else {
                val user = nearbyMap[moment.authorId]
                if (user != null && !user.avatarUrl.isNullOrBlank()) {
                    moment.copy(authorAvatarUrl = user.avatarUrl)
                } else {
                    moment
                }
            }
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            try {
                userProfileRepo.currentProfile.collect { rawProfile ->
                    var profile = rawProfile
                    val savedSession = authRepo.getSavedSession()
                    val hasActiveSession = savedSession != null && savedSession.isLoggedIn && !savedSession.isGuest

                    if (profile != null) {
                        // Periksa apakah ada residu data dummy "Pengguna Lovy" atau "lovy_889214"
                        val isDummyName = profile.displayName.isBlank() || profile.displayName.equals("Pengguna Lovy", ignoreCase = true)
                        val isDummyLovyId = profile.lovyId.isBlank() || profile.lovyId == "lovy_889214"
                        val isDummyBio = profile.bio == "Menjelajahi dunia dan mencari teman baru di Lovy Chat ✨"
                        val isMissingEmail = profile.email.isNullOrBlank()

                        if (hasActiveSession && (isDummyName || isDummyLovyId || isDummyBio || isMissingEmail)) {
                            val healedName = if (isDummyName) {
                                savedSession.displayName.takeIf { it.isNotBlank() && !it.equals("Pengguna Lovy", ignoreCase = true) }
                                    ?: if (savedSession.username.contains("@")) savedSession.username.substringBefore("@").replaceFirstChar { it.uppercase() } else savedSession.username
                            } else profile.displayName

                            val healedLovyId = if (isDummyLovyId) {
                                savedSession.lovyId.takeIf { it.isNotBlank() && it != "lovy_889214" } ?: "lovy_${(100000..999999).random()}"
                            } else profile.lovyId

                            val healedBio = if (isDummyBio) {
                                savedSession.bio.takeIf { it != "Menjelajahi dunia dan mencari teman baru di Lovy Chat ✨" } ?: ""
                            } else profile.bio

                            val healedEmail = if (isMissingEmail) {
                                savedSession.email ?: if (savedSession.username.contains("@")) savedSession.username else null
                            } else profile.email

                            val healedAvatar = profile.profilePicture ?: savedSession.avatarUrl

                            val healedProfile = profile.copy(
                                displayName = healedName,
                                lovyId = healedLovyId,
                                bio = healedBio,
                                email = healedEmail,
                                profilePicture = healedAvatar
                            )
                            profile = healedProfile
                            viewModelScope.launch {
                                try {
                                    userProfileRepo.saveProfile(healedProfile)
                                } catch (_: Throwable) {}
                            }
                        }

                        _uiState.update {
                            val updated = it.copy(
                                userProfile = profile,
                                myName = profile.displayName,
                                myBio = profile.bio,
                                myLovyId = profile.lovyId
                            )
                            updated.copy(moments = enrichMomentsWithAvatars(updated.moments, profile.profilePicture))
                        }

                        if (!_uiState.value.isGuest && profile.lovyId.isNotBlank()) {
                            startIncomingChatPeriodicSync()
                            startRealtimeChatSubscription()
                        }

                        // Jika kota GPS nyata sudah terdeteksi dan profil masih default, sinkronkan otomatis
                        val detectedCity = _uiState.value.currentGpsLocation?.cityName
                        if (!detectedCity.isNullOrBlank() && (profile.city.isBlank() || profile.city.equals("Jakarta Selatan", ignoreCase = true))) {
                            updateCityFromGps(detectedCity)
                        }
                    } else {
                        // Seed initial profile in Room database tanpa data dummy
                        val initialName = if (hasActiveSession) {
                            savedSession.displayName.takeIf { it.isNotBlank() && !it.equals("Pengguna Lovy", ignoreCase = true) }
                                ?: if (savedSession.username.contains("@")) savedSession.username.substringBefore("@").replaceFirstChar { it.uppercase() } else savedSession.username
                        } else {
                            _uiState.value.myName.takeIf { it.isNotBlank() && !it.equals("Pengguna Lovy", ignoreCase = true) } ?: ""
                        }
                        val initialEmail = if (hasActiveSession) {
                            savedSession.email ?: if (savedSession.username.contains("@")) savedSession.username else null
                        } else null
                        val initialLovyId = if (hasActiveSession) {
                            savedSession.lovyId.takeIf { it.isNotBlank() && it != "lovy_889214" } ?: "lovy_${(100000..999999).random()}"
                        } else {
                            _uiState.value.myLovyId.takeIf { it.isNotBlank() && it != "lovy_889214" } ?: "lovy_${(100000..999999).random()}"
                        }
                        val initialCity = _uiState.value.currentGpsLocation?.cityName?.takeIf { it.isNotBlank() }
                            ?: savedSession?.city?.takeIf { it.isNotBlank() } ?: ""
                        val initialProfile = UserProfile(
                            id = "current_user",
                            displayName = initialName,
                            bio = savedSession?.bio?.takeIf { it != "Menjelajahi dunia dan mencari teman baru di Lovy Chat ✨" } ?: "",
                            profilePicture = savedSession?.avatarUrl,
                            email = initialEmail,
                            lovyId = initialLovyId,
                            city = initialCity,
                            gender = savedSession?.gender?.name ?: "FEMALE",
                            age = savedSession?.age ?: 22
                        )
                        userProfileRepo.saveProfile(initialProfile)
                    }
                }
            } catch (e: Throwable) {
                android.util.Log.w("LovyChatViewModel", "Error observing Room UserProfile: ${e.message}")
            }
        }
    }

    fun saveUserProfile(profile: UserProfile) {
        recordFeatureClick()
        _uiState.update {
            val updated = it.copy(
                userProfile = profile,
                myName = profile.displayName,
                myBio = profile.bio,
                myLovyId = profile.lovyId
            )
            updated.copy(moments = enrichMomentsWithAvatars(updated.moments, profile.profilePicture))
        }
        viewModelScope.launch {
            try {
                userProfileRepo.saveProfile(profile)
            } catch (e: Throwable) {
                android.util.Log.e("LovyChatViewModel", "Error saving UserProfile to Room: ${e.message}")
            }
            // Sinkronkan juga sesi SharedPreferences agar data auth selalu mutakhir
            try {
                val currentSession = authRepo.getSavedSession()
                if (currentSession != null && currentSession.isLoggedIn && !currentSession.isGuest) {
                    val updatedSession = currentSession.copy(
                        displayName = profile.displayName,
                        lovyId = profile.lovyId,
                        email = profile.email ?: currentSession.email,
                        bio = profile.bio,
                        avatarUrl = profile.profilePicture,
                        gender = if (profile.gender.equals("MALE", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                        city = profile.city,
                        age = profile.age
                    )
                    authRepo.saveSession(updatedSession)
                }
            } catch (e: Throwable) {
                android.util.Log.e("LovyChatViewModel", "Error updating session from UserProfile: ${e.message}")
            }
            syncUserProfileToSupabase()
        }
    }

    fun syncUserProfileToSupabase() {
        if (_uiState.value.isGuest) return
        if (!SupabaseClient.isConfigured()) return
        val profile = _uiState.value.userProfile
        val lovyId = _uiState.value.myLovyId
        val userGender = if (profile.gender.equals("MALE", ignoreCase = true)) Gender.MALE else Gender.FEMALE
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Sinkronkan ke tabel nearby_users di Supabase agar pengguna lain melihat info terbaru
                supabaseRepo.registerOrUpdateUser(
                    id = lovyId,
                    name = profile.displayName.ifBlank { _uiState.value.myName },
                    gender = userGender,
                    bio = profile.bio,
                    avatarHex = 0xFF4CAF50,
                    avatarUrl = profile.profilePicture?.takeIf { it.isNotBlank() },
                    city = profile.city,
                    fcmToken = _uiState.value.fcmToken.takeIf { it.isNotBlank() }
                )

                // 2. Sinkronkan ke tabel app_accounts di Supabase agar data akun login terbarukan
                val savedSession = authRepo.getSavedSession()
                val sessionUsername = savedSession?.username
                val userEmail = profile.email ?: savedSession?.email

                val currentAcc = (if (lovyId.isNotBlank()) supabaseRepo.findAccountById(lovyId) else null)
                    ?: (if (!userEmail.isNullOrBlank()) supabaseRepo.findAccountByGoogle(userEmail) else null)
                    ?: (if (!sessionUsername.isNullOrBlank()) supabaseRepo.findAccountByUsername(sessionUsername) else null)

                if (currentAcc != null) {
                    supabaseRepo.registerOrUpdateAccount(
                        currentAcc.copy(
                            displayName = profile.displayName.ifBlank { currentAcc.displayName },
                            avatarUrl = profile.profilePicture,
                            bio = profile.bio,
                            gender = profile.gender,
                            googleEmail = userEmail ?: currentAcc.googleEmail,
                            lastLoginAt = System.currentTimeMillis()
                        )
                    )
                } else if (lovyId.isNotBlank()) {
                    val newAcc = com.example.data.supabase.SupabaseAccountDto(
                        id = lovyId,
                        username = userEmail ?: sessionUsername ?: lovyId,
                        passwordHash = null,
                        displayName = profile.displayName.ifBlank { _uiState.value.myName },
                        gender = profile.gender,
                        bio = profile.bio,
                        avatarUrl = profile.profilePicture,
                        googleId = null,
                        googleEmail = userEmail,
                        createdAt = System.currentTimeMillis(),
                        lastLoginAt = System.currentTimeMillis(),
                        fcmToken = _uiState.value.fcmToken.takeIf { it.isNotBlank() }
                    )
                    supabaseRepo.registerOrUpdateAccount(newAcc)
                }
            } catch (e: Exception) {
                Log.d("LovyChatViewModel", "syncUserProfileToSupabase error: ${e.message}")
            }
        }
    }

    private fun detectAndApplyGeoLanguage() {
        val app = try { getApplication<Application>() } catch (_: Throwable) { null }
        val ctx = try { app?.applicationContext } catch (_: Throwable) { null } ?: app
        val detected = com.example.util.GeoLanguageDetector.detectLocalLanguage(ctx)
        val areaName = com.example.util.GeoLanguageDetector.getCountryOrRegionName(ctx)
        _uiState.update {
            it.copy(
                isLocalLanguageMode = true,
                detectedLocalLanguage = detected,
                language = detected,
                detectedGeoArea = areaName
            )
        }
    }

    fun setLanguage(language: com.example.util.AppLanguage) {
        recordFeatureClick()
        val app = try { getApplication<Application>() } catch (_: Throwable) { null }
        val ctx = try { app?.applicationContext } catch (_: Throwable) { null } ?: app

        if (language == com.example.util.AppLanguage.LOCAL) {
            val detected = com.example.util.GeoLanguageDetector.detectLocalLanguage(ctx)
            _uiState.update {
                it.copy(
                    isLocalLanguageMode = true,
                    language = detected,
                    detectedLocalLanguage = detected
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    language = language,
                    isLocalLanguageMode = (language != com.example.util.AppLanguage.ENGLISH)
                )
            }
        }
    }

    fun toggleLanguageMode() {
        recordFeatureClick()
        val current = _uiState.value
        if (current.isLocalLanguageMode) {
            setLanguage(com.example.util.AppLanguage.ENGLISH)
        } else {
            setLanguage(com.example.util.AppLanguage.LOCAL)
        }
    }

    fun recordFeatureClick() {
        com.example.util.AdManager.recordFeatureClick()
    }

    private fun refreshSupabaseState() {
        val configured = SupabaseClient.isConfigured()
        val url = SupabaseClient.getSupabaseUrl()
        val key = SupabaseClient.getSupabaseAnonKey()
        _uiState.update {
            it.copy(
                isSupabaseConnected = configured,
                supabaseUrl = url,
                supabaseAnonKey = key,
                connectionStatusMessage = if (configured) "Supabase terkonfigurasi: $url" else "Belum terkonfigurasi (menggunakan penyimpanan lokal)"
            )
        }
    }

    fun syncFromSupabase(forceRefresh: Boolean = false) {
        if (_uiState.value.isGuest) return // Mode Tamu tidak disinkronkan ke Supabase
        if (!SupabaseClient.isConfigured()) return
        val now = System.currentTimeMillis()
        if (!forceRefresh && now - lastNearbyScanTime < CACHE_DURATION_MS && now - lastMomentsSyncTime < CACHE_DURATION_MS) {
            // Data masih segar di cache (< 3 menit), jangan query database server
            return
        }

        viewModelScope.launch {
            if (forceRefresh || now - lastNearbyScanTime >= CACHE_DURATION_MS) {
                val remoteUsers = supabaseRepo.fetchNearbyUsers()
                if (remoteUsers != null) {
                    val myId = _uiState.value.myLovyId
                    val filtered = remoteUsers
                        .filterNot { it.id == myId || it.id == "current_user" }
                        .filterNot { isUserBlocked(it.id, it.name) }
                        .shuffled() // Diacak agar penemuan teman terasa dinamis & adil (misal 400m, 1km, 200m)
                    _uiState.update { it.copy(nearbyUsers = filtered) }
                    lastNearbyScanTime = System.currentTimeMillis()
                }
            }

            if (forceRefresh || now - lastBottlesSyncTime >= CACHE_DURATION_MS) {
                val remoteBottles = supabaseRepo.fetchOceanBottles()
                if (remoteBottles != null) {
                    _uiState.update { it.copy(oceanBottles = remoteBottles) }
                    lastBottlesSyncTime = System.currentTimeMillis()
                }
            }

            if (forceRefresh || now - lastMomentsSyncTime >= CACHE_DURATION_MS) {
                val remoteMoments = supabaseRepo.fetchMoments()
                if (remoteMoments != null) {
                    val myLocal = _uiState.value.moments.filter { it.id in _uiState.value.myMomentIds }
                    val merged = (myLocal + remoteMoments).distinctBy { it.id }
                    val enriched = enrichMomentsWithAvatars(merged)
                    _uiState.update { it.copy(moments = enriched) }
                    lastMomentsSyncTime = System.currentTimeMillis()
                }
            }

            syncIncomingChats()
        }
    }

    fun refreshMoments(force: Boolean = false) {
        val now = System.currentTimeMillis()
        if (!force && now - lastMomentsSyncTime < CACHE_DURATION_MS) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshingMoments = true) }
            try {
                // Memberikan delay minimal agar animasi perputaran tombol refresh tampak jelas
                kotlinx.coroutines.delay(650)

                var fetched = false
                if (!_uiState.value.isGuest && SupabaseClient.isConfigured()) {
                    val remoteMoments = supabaseRepo.fetchMoments()
                    if (remoteMoments != null) {
                        val myLocal = _uiState.value.moments.filter { it.id in _uiState.value.myMomentIds }
                        val merged = (myLocal + remoteMoments).distinctBy { it.id }
                        val enriched = enrichMomentsWithAvatars(merged)
                        _uiState.update { it.copy(moments = enriched) }
                        lastMomentsSyncTime = System.currentTimeMillis()
                        fetched = true
                    }
                }

                if (!fetched) {
                    lastMomentsSyncTime = System.currentTimeMillis()
                }
            } catch (e: Throwable) {
                android.util.Log.w("LovyChatViewModel", "Gagal menyegarkan momen: ${e.message}")
            } finally {
                _uiState.update { it.copy(isRefreshingMoments = false) }
            }
        }
    }

    fun saveSupabaseCredentials(url: String, anonKey: String) {
        SupabaseClient.saveCustomCredentials(getApplication<Application>().applicationContext, url, anonKey)
        refreshSupabaseState()
        testSupabaseConnection()
        syncFromSupabase()
    }

    fun clearSupabaseCredentials() {
        SupabaseClient.clearCustomCredentials(getApplication<Application>().applicationContext)
        refreshSupabaseState()
    }

    fun refreshR2State() {
        val app = try { getApplication<Application>() } catch (_: Throwable) { null }
        val ctx = try { app?.applicationContext } catch (_: Throwable) { null } ?: app
        if (ctx != null) {
            com.example.data.storage.R2StorageClient.init(ctx)
        }
        val configured = com.example.data.storage.R2StorageClient.isConfigured()
        val accountId = com.example.data.storage.R2StorageClient.getAccountId()
        val bucketName = com.example.data.storage.R2StorageClient.getBucketName()
        val publicDomain = com.example.data.storage.R2StorageClient.getPublicDomain()
        _uiState.update {
            it.copy(
                isR2Configured = configured,
                r2AccountId = accountId,
                r2BucketName = bucketName,
                r2PublicDomain = publicDomain
            )
        }
    }

    fun saveR2Credentials(
        accountId: String,
        accessKeyId: String,
        secretAccessKey: String,
        bucketName: String,
        publicDomain: String
    ) {
        val app = getApplication<Application>()
        com.example.data.storage.R2StorageClient.saveConfig(
            app.applicationContext,
            accountId,
            accessKeyId,
            secretAccessKey,
            bucketName,
            publicDomain
        )
        refreshR2State()
    }

    fun testR2Connection(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val res = com.example.data.storage.R2StorageClient.testConnection()
            res.onSuccess { msg ->
                refreshR2State()
                onResult(true, msg)
            }.onFailure { err ->
                onResult(false, err.message ?: "Koneksi R2 gagal")
            }
        }
    }

    fun testSupabaseConnection() {
        viewModelScope.launch {
            _uiState.update { it.copy(isTestingConnection = true, connectionStatusMessage = "Sedang menguji koneksi...") }
            val result = supabaseRepo.testConnection()
            result.onSuccess { msg ->
                _uiState.update {
                    it.copy(
                        isTestingConnection = false,
                        isSupabaseConnected = true,
                        connectionStatusMessage = msg
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isTestingConnection = false,
                        isSupabaseConnected = false,
                        connectionStatusMessage = "Gagal terhubung: ${err.localizedMessage}"
                    )
                }
            }
        }
    }

    fun onSplashFinished() {
        _uiState.update {
            if (it.isLoggedIn) {
                it.copy(currentScreen = CurrentScreen.Main)
            } else {
                it.copy(currentScreen = CurrentScreen.Login)
            }
        }
    }

    fun loginUser(name: String) {
        val finalName = if (name.isNotBlank()) name else _uiState.value.myName
        clearDummyFriends()
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                isGuest = false,
                myName = finalName,
                currentScreen = CurrentScreen.Main,
                // Mode Pengguna Asli: Pisahkan dari percakapan dummy tamu agar tidak bercampur
                conversations = emptyList(),
                messagesMap = emptyMap(),
                moments = emptyList(),
                oceanBottles = emptyList(),
                nearbyUsers = emptyList()
            )
        }
        updateUserActivity()
        syncFromSupabase(forceRefresh = true)
        syncUserProfileToSupabase()
    }

    suspend fun performRegister(username: String, password: String, gender: Gender): AuthResult {
        val result = authRepo.register(username = username, password = password, displayName = username, gender = gender)
        if (result.success) {
            val lovyId = result.lovyId ?: "lovy_${(100000..999999).random()}"
            val finalName = result.displayName ?: result.username ?: username
            clearDummyFriends()
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isGuest = false,
                    myName = finalName,
                    myLovyId = lovyId,
                    currentScreen = CurrentScreen.Main,
                    conversations = emptyList(),
                    messagesMap = emptyMap(),
                    moments = emptyList(),
                    oceanBottles = emptyList(),
                    nearbyUsers = emptyList()
                )
            }
            val detectedCity = _uiState.value.currentGpsLocation?.cityName?.takeIf { it.isNotBlank() } ?: ""
            val userEmail = result.email ?: if (username.contains("@")) username else null
            val newProfile = UserProfile(
                id = "current_user",
                displayName = finalName,
                bio = result.bio ?: "Halo, saya pengguna baru Lovy Chat! ✨",
                lovyId = lovyId,
                gender = gender.name,
                email = userEmail,
                city = detectedCity
            )
            saveUserProfile(newProfile)
            updateUserActivity()
            syncFromSupabase(forceRefresh = true)
            syncUserProfileToSupabase()
            startIncomingChatPeriodicSync()
            startRealtimeChatSubscription()
        }
        return result
    }

    suspend fun performLogin(username: String, password: String): AuthResult {
        val result = authRepo.login(username = username, password = password)
        if (result.success) {
            val lovyId = result.lovyId ?: _uiState.value.myLovyId
            val finalName = result.displayName ?: result.username ?: username
            clearDummyFriends()
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isGuest = false,
                    myName = finalName,
                    myLovyId = lovyId,
                    currentScreen = CurrentScreen.Main,
                    conversations = emptyList(),
                    messagesMap = emptyMap(),
                    moments = emptyList(),
                    oceanBottles = emptyList(),
                    nearbyUsers = emptyList()
                )
            }
            val existingProfile = _uiState.value.userProfile
            val userEmail = result.email ?: if (username.contains("@")) username else existingProfile.email
            val updatedProfile = existingProfile.copy(
                displayName = finalName,
                lovyId = lovyId,
                email = userEmail,
                gender = result.gender.name,
                bio = result.bio ?: existingProfile.bio,
                profilePicture = result.avatarUrl ?: existingProfile.profilePicture
            )
            saveUserProfile(updatedProfile)
            updateUserActivity()
            loadMyMoments()
            syncFromSupabase(forceRefresh = true)
            syncUserProfileToSupabase()
            startIncomingChatPeriodicSync()
            startRealtimeChatSubscription()
        }
        return result
    }

    suspend fun performGoogleLogin(googleUser: GoogleAuthHelper.GoogleUserResult): AuthResult {
        val result = authRepo.loginWithGoogle(googleUser)
        if (result.success) {
            val lovyId = result.lovyId ?: _uiState.value.myLovyId
            val finalName = result.displayName ?: googleUser.displayName
            clearDummyFriends()
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isGuest = false,
                    myName = finalName,
                    myLovyId = lovyId,
                    currentScreen = CurrentScreen.Main,
                    conversations = emptyList(),
                    messagesMap = emptyMap(),
                    moments = emptyList(),
                    oceanBottles = emptyList(),
                    nearbyUsers = emptyList()
                )
            }
            val existingProfile = _uiState.value.userProfile
            val updatedProfile = existingProfile.copy(
                displayName = finalName,
                lovyId = lovyId,
                email = googleUser.email,
                profilePicture = result.avatarUrl ?: googleUser.profilePictureUri ?: existingProfile.profilePicture,
                bio = result.bio ?: existingProfile.bio,
                gender = result.gender.name
            )
            saveUserProfile(updatedProfile)
            updateUserActivity()
            loadMyMoments()
            syncFromSupabase(forceRefresh = true)
            syncUserProfileToSupabase()
            startIncomingChatPeriodicSync()
            startRealtimeChatSubscription()
        }
        return result
    }

    fun loginAsGuest() {
        // Mode Tamu dihapus
    }

    fun logout() {
        periodicIncomingChatJob?.cancel()
        realtimeSubscriptionJob?.cancel()
        SupabaseRealtimeManager.disconnect()
        authRepo.clearSession()
        lastMomentsSyncTime = 0L
        lastNearbyScanTime = 0L
        lastBottlesSyncTime = 0L
        val wasRealUser = !_uiState.value.isGuest && _uiState.value.isLoggedIn
        if (wasRealUser) {
            val myId = _uiState.value.myLovyId
            // Tandai dan hapus semua pesan di Supabase untuk pengirim saat logout
            viewModelScope.launch {
                try {
                    supabaseRepo.markAllSenderMessagesDeleted(myId)
                } catch (e: Exception) {
                    Log.w("LovyChatViewModel", "Gagal membersihkan pesan di Supabase saat logout", e)
                }
            }
        }

        _uiState.update {
            it.copy(
                isLoggedIn = false,
                isGuest = false,
                currentScreen = CurrentScreen.Login,
                messagesMap = emptyMap(),
                activeChatId = null,
                conversations = emptyList(),
                moments = emptyList(),
                oceanBottles = emptyList(),
                nearbyUsers = emptyList()
            )
        }
    }

    /**
     * Menghapus akun dan seluruh data pengguna secara permanen (sesuai regulasi Google Play).
     */
    fun deleteAccount() {
        periodicIncomingChatJob?.cancel()
        realtimeSubscriptionJob?.cancel()
        SupabaseRealtimeManager.disconnect()
        val state = _uiState.value
        val myId = state.myLovyId
        val myName = state.myName
        val wasRealUser = !state.isGuest && state.isLoggedIn

        authRepo.deleteAccount(myName)

        if (wasRealUser) {
            viewModelScope.launch {
                try {
                    supabaseRepo.deleteAccountAndUserData(myId, myId)
                } catch (e: Exception) {
                    Log.w("LovyChatViewModel", "Gagal hapus data akun di Supabase: ${e.message}")
                }
            }
        }

        _uiState.update {
            it.copy(
                isLoggedIn = false,
                isGuest = false,
                currentScreen = CurrentScreen.Login,
                messagesMap = emptyMap(),
                activeChatId = null,
                conversations = emptyList(),
                moments = emptyList(),
                oceanBottles = emptyList(),
                nearbyUsers = emptyList(),
                myLovyId = "",
                myName = "",
                myBio = "",
                userProfile = UserProfile()
            )
        }
    }

    fun selectTab(tabIndex: Int) {
        recordFeatureClick()
        _uiState.update { it.copy(currentTab = tabIndex, currentScreen = CurrentScreen.Main) }
        if (tabIndex == 0 && !_uiState.value.isGuest) {
            syncIncomingChats()
        }
    }

    fun navigateTo(screen: CurrentScreen) {
        recordFeatureClick()
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun navigateBack() {
        recordFeatureClick()
        _uiState.update { it.copy(currentScreen = CurrentScreen.Main) }
    }

    fun setNearbyGenderFilter(gender: Gender?) {
        recordFeatureClick()
        _uiState.update { it.copy(nearbyGenderFilter = gender) }
    }

    fun setNearbyOnlyOnlineFilter(onlyOnline: Boolean) {
        recordFeatureClick()
        _uiState.update { it.copy(nearbyOnlyOnlineFilter = onlyOnline) }
    }

    private fun checkInitialGpsLocation() {
        val app = try { getApplication<Application>() } catch (_: Throwable) { null } ?: return
        val hasFine = androidx.core.content.ContextCompat.checkSelfPermission(
            app,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val hasCoarse = androidx.core.content.ContextCompat.checkSelfPermission(
            app,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val hasPermission = hasFine || hasCoarse
        _uiState.update { it.copy(hasLocationPermission = hasPermission) }
        if (hasPermission) {
            val isEnabled = com.example.util.AndroidGpsTracker.isLocationEnabled(app)
            val lastLoc = com.example.util.AndroidGpsTracker.getLastKnownLocation(app)
            _uiState.update {
                it.copy(
                    isGpsEnabled = isEnabled,
                    currentGpsLocation = lastLoc
                )
            }
            if (lastLoc != null && lastLoc.cityName.isNotBlank()) {
                updateCityFromGps(lastLoc.cityName)
            }
        }
    }

    fun updateCityFromGps(detectedCity: String, forceSync: Boolean = false) {
        val cleanCity = detectedCity.trim()
        if (cleanCity.isBlank()) return

        val currentProfile = _uiState.value.userProfile
        if (currentProfile.city != cleanCity || forceSync) {
            val updatedProfile = currentProfile.copy(
                city = cleanCity,
                updatedAt = System.currentTimeMillis()
            )
            _uiState.update { it.copy(userProfile = updatedProfile) }
            viewModelScope.launch {
                try {
                    userProfileRepo.saveProfile(updatedProfile)
                } catch (e: Throwable) {
                    android.util.Log.e("LovyChatViewModel", "Gagal menyimpan kota profil ke Room: ${e.message}")
                }
                syncUserProfileToSupabase()
            }
        }
    }

    fun refreshLocationFromGps() {
        val app = try { getApplication<Application>() } catch (_: Throwable) { null } ?: return
        viewModelScope.launch {
            val isEnabled = com.example.util.AndroidGpsTracker.isLocationEnabled(app)
            val loc = com.example.util.AndroidGpsTracker.getCurrentFreshLocation(app, timeoutMs = 4000L)
                ?: com.example.util.AndroidGpsTracker.getLastKnownLocation(app)
            if (loc != null) {
                _uiState.update {
                    it.copy(
                        isGpsEnabled = isEnabled,
                        currentGpsLocation = loc
                    )
                }
                updateGpsLocation(loc)
                if (loc.cityName.isNotBlank()) {
                    updateCityFromGps(loc.cityName, forceSync = true)
                }
                refreshNearbyScan(forceRefresh = true)
            } else {
                _uiState.update { it.copy(isGpsEnabled = isEnabled) }
            }
        }
    }

    fun updateLocationPermission(granted: Boolean) {
        _uiState.update { it.copy(hasLocationPermission = granted) }
        if (granted) {
            val app = try { getApplication<Application>() } catch (_: Throwable) { null }
            viewModelScope.launch {
                val isEnabled = com.example.util.AndroidGpsTracker.isLocationEnabled(app)
                val freshLoc = com.example.util.AndroidGpsTracker.getCurrentFreshLocation(app, timeoutMs = 3500L)
                    ?: com.example.util.AndroidGpsTracker.getLastKnownLocation(app)
                if (freshLoc != null) {
                    _uiState.update { 
                        it.copy(
                            isGpsEnabled = isEnabled,
                            currentGpsLocation = freshLoc
                        ) 
                    }
                    updateGpsLocation(freshLoc)
                }
                refreshNearbyScan(forceRefresh = true)
            }
        }
    }

    fun updateGpsLocation(location: com.example.util.UserGpsLocation) {
        _uiState.update { it.copy(currentGpsLocation = location) }
        if (location.cityName.isNotBlank()) {
            updateCityFromGps(location.cityName)
        }

        // --- GPS Throttling: Hanya sinkronkan ke cloud jika berpindah > 500 meter atau > 10 menit ---
        val now = System.currentTimeMillis()
        val prevLat = lastSyncedLat
        val prevLon = lastSyncedLon

        val shouldSyncGps = if (prevLat == null || prevLon == null) {
            true
        } else {
            val dist = com.example.util.AndroidGpsTracker.calculateDistanceMeters(
                prevLat, prevLon, location.latitude, location.longitude
            )
            dist >= GPS_THROTTLE_MIN_DISTANCE_METERS || (now - lastGpsUpdateTimestamp >= GPS_THROTTLE_MIN_INTERVAL_MS)
        }

        if (shouldSyncGps) {
            lastSyncedLat = location.latitude
            lastSyncedLon = location.longitude
            lastGpsUpdateTimestamp = now
            updateUserActivity()
            syncUserProfileToSupabase()
        }
    }

    fun expandNearbyUsers() {
        recordFeatureClick()
        _uiState.update { current ->
            val nextTier = (current.nearbyExpansionTier + 1).coerceAtMost(5)
            current.copy(
                nearbyExpansionTier = nextTier,
                isNearbyExpanded = nextTier >= 5
            )
        }
    }

    fun resetNearbyExpansion() {
        _uiState.update { it.copy(nearbyExpansionTier = 0, isNearbyExpanded = false) }
    }

    fun refreshNearbyScan(forceRefresh: Boolean = false) {
        recordFeatureClick()
        val now = System.currentTimeMillis()
        val isCacheValid = !forceRefresh && (now - lastNearbyScanTime < CACHE_DURATION_MS)

        viewModelScope.launch {
            _uiState.update { it.copy(isScanningNearby = true) }
            
            // Periksa dan ambil koordinat GPS Native terbaru jika izin ada
            val app = try { getApplication<Application>() } catch (_: Throwable) { null }
            val isGpsOn = com.example.util.AndroidGpsTracker.isLocationEnabled(app)
            
            // Dapatkan lokasi GPS aktif dan segar (aktifkan sensor sejenak, auto-off setelah dapat)
            val gpsLoc = if (forceRefresh || _uiState.value.currentGpsLocation == null) {
                com.example.util.AndroidGpsTracker.getCurrentFreshLocation(app, timeoutMs = 3500L)
                    ?: com.example.util.AndroidGpsTracker.getLastKnownLocation(app)
            } else {
                com.example.util.AndroidGpsTracker.getLastKnownLocation(app)
                    ?: com.example.util.AndroidGpsTracker.getCurrentFreshLocation(app, timeoutMs = 3500L)
            }

            if (gpsLoc != null) {
                _uiState.update { it.copy(isGpsEnabled = isGpsOn, currentGpsLocation = gpsLoc) }
                updateGpsLocation(gpsLoc)
                if (forceRefresh && gpsLoc.cityName.isNotBlank()) {
                    updateCityFromGps(gpsLoc.cityName, forceSync = true)
                }
            } else {
                _uiState.update { it.copy(isGpsEnabled = isGpsOn) }
            }

            delay(600)

            // HANYA gunakan pengguna nyata dari Supabase
            val remoteUsers = if (!isCacheValid || forceRefresh) supabaseRepo.fetchNearbyUsers() else _uiState.value.nearbyUsers
            lastNearbyScanTime = System.currentTimeMillis()
            val myId = _uiState.value.myLovyId
            val filtered = (remoteUsers ?: emptyList())
                .filterNot { it.id == myId || it.id == "current_user" }
                .filterNot { isUserBlocked(it.id, it.name) }
                .filterNot { isDummyFriend(it.id, it.name) }
            _uiState.update { it.copy(isScanningNearby = false, nearbyUsers = filtered) }
        }
    }

    fun getCanonicalConversationId(id1: String, id2: String): String {
        val clean1 = id1.trim()
        val clean2 = id2.trim()
        if (clean1.isEmpty() && clean2.isEmpty()) return "conv_chat"
        if (clean1.isEmpty()) return "conv_$clean2"
        if (clean2.isEmpty()) return "conv_$clean1"
        val sorted = if (clean1 <= clean2) listOf(clean1, clean2) else listOf(clean2, clean1)
        return "conv_${sorted[0]}__${sorted[1]}"
    }

    fun extractPartnerIdFromConvId(convId: String, myId: String): String {
        if (!convId.startsWith("conv_")) return convId
        val content = convId.removePrefix("conv_")
        val cleanMyId = myId.trim()

        // 1. Format separator ganda '__' (sangat presisi untuk ID yang mengandung underscore)
        if (content.contains("__")) {
            val parts = content.split("__")
            val partner = parts.firstOrNull { !it.equals(cleanMyId, ignoreCase = true) }
            if (!partner.isNullOrBlank()) return partner
        }

        // 2. Format single '_' jika myId berada di awal atau di akhir
        if (cleanMyId.isNotBlank()) {
            if (content.startsWith("${cleanMyId}__", ignoreCase = true)) {
                return content.substring(cleanMyId.length + 2)
            }
            if (content.startsWith("${cleanMyId}_", ignoreCase = true)) {
                return content.substring(cleanMyId.length + 1)
            }
            if (content.endsWith("__${cleanMyId}", ignoreCase = true)) {
                return content.substring(0, content.length - cleanMyId.length - 2)
            }
            if (content.endsWith("_${cleanMyId}", ignoreCase = true)) {
                return content.substring(0, content.length - cleanMyId.length - 1)
            }
        }

        // 3. Deteksi pola lovy ID: lovy_XXXXXX
        val lovyMatches = Regex("(lovy_[0-9a-zA-Z]+)").findAll(content).map { it.value }.toList()
        if (lovyMatches.size >= 2) {
            val partner = lovyMatches.firstOrNull { !it.equals(cleanMyId, ignoreCase = true) }
            if (!partner.isNullOrBlank()) return partner
        }

        // 4. Fallback legacy jika split 2 bagian
        val parts = content.split("_")
        if (parts.size == 2) {
            return parts.firstOrNull { !it.equals(cleanMyId, ignoreCase = true) } ?: parts[0]
        }
        return content
    }

    fun sayHiToUser(user: User) {
        if (isUserBlocked(user.id, user.name)) return
        recordFeatureClick()
        saveChatFriend(user)
        updateUserActivity()
        val convId = getCanonicalConversationId(_uiState.value.myLovyId, user.id)
        val existing = _uiState.value.conversations.find { it.id == convId || it.partnerId == user.id }
        val currentMsgs = _uiState.value.messagesMap[convId] ?: emptyList()
        if (existing != null && currentMsgs.isNotEmpty()) {
            openChat(convId, user.name, user.avatarColorHex)
            return
        }
        
        val greetingText = "Halo ${user.name}! Salam kenal dari fitur Teman Sekitar ya 👋"
        val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = convId,
            text = greetingText,
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )

        viewModelScope.launch(Dispatchers.IO) {
            localChatRepo.saveMessage(newMsg)
        }

        val updatedMessages = (_uiState.value.messagesMap[convId] ?: emptyList()) + newMsg
        val updatedConversations = if (existing != null) {
            _uiState.value.conversations.map {
                if (it.id == convId || it.id == existing.id) it.copy(id = convId, lastMessage = greetingText, lastTimestamp = System.currentTimeMillis()) else it
            }
        } else {
            listOf(
                ChatConversation(
                    id = convId,
                    partnerId = user.id,
                    partnerName = user.name,
                    partnerAvatarHex = user.avatarColorHex,
                    partnerGender = user.gender,
                    lastMessage = greetingText,
                    lastTimestamp = System.currentTimeMillis(),
                    unreadCount = 0,
                    isOnline = user.isOnline,
                    partnerAvatarUrl = user.avatarUrl,
                    partnerAge = user.age,
                    partnerDistanceMeters = user.distanceMeters,
                    partnerCity = user.city
                )
            ) + _uiState.value.conversations
        }

        _uiState.update {
            it.copy(
                conversations = updatedConversations,
                messagesMap = it.messagesMap + (convId to updatedMessages)
            )
        }

        // Kirim ke Supabase dengan sender_id dan receiver_id asli
        viewModelScope.launch {
            supabaseRepo.sendChatMessage(
                message = newMsg,
                senderId = _uiState.value.myLovyId,
                receiverId = user.id
            )
        }

        // Open chat directly
        openChat(convId, user.name, user.avatarColorHex)
    }

    fun openChat(conversationId: String, partnerName: String, partnerAvatarHex: Long) {
        recordFeatureClick()
        val conv = _uiState.value.conversations.find { it.id == conversationId }
        if (conv != null && _uiState.value.chattedFriends.any { it.id == conv.partnerId }) {
            saveChatFriend(
                User(
                    id = conv.partnerId,
                    name = conv.partnerName,
                    gender = conv.partnerGender,
                    age = conv.partnerAge,
                    distanceMeters = conv.partnerDistanceMeters,
                    bio = "Teman obrolan di Lovy Chat",
                    avatarColorHex = conv.partnerAvatarHex,
                    isOnline = conv.isOnline,
                    avatarUrl = conv.partnerAvatarUrl,
                    city = conv.partnerCity ?: "Jakarta Selatan"
                )
            )
        }

        _uiState.update {
            val updatedConvs = it.conversations.map { c ->
                if (c.id == conversationId) c.copy(unreadCount = 0) else c
            }
            it.copy(
                conversations = updatedConvs,
                activeChatId = conversationId,
                currentScreen = CurrentScreen.ChatDetail(conversationId, partnerName, partnerAvatarHex)
            )
        }

        val partnerId = conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
        markConversationAsRead(conversationId, partnerId)

        // Muat pesan dari cache lokal Room jika state di memori masih kosong agar instan
        if ((_uiState.value.messagesMap[conversationId] ?: emptyList()).isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val cached = localChatRepo.getMessagesForConversation(conversationId)
                if (cached.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        _uiState.update { state ->
                            val current = state.messagesMap[conversationId] ?: emptyList()
                            if (current.isEmpty()) {
                                state.copy(messagesMap = state.messagesMap + (conversationId to cached))
                            } else state
                        }
                    }
                }
            }
        }

        // Sinkronisasi pesan obrolan 2 arah secara langsung untuk pengguna asli
        if (!_uiState.value.isGuest) {
            pollChatMessages(conversationId, partnerId)
        }
    }

    fun markConversationAsRead(conversationId: String, partnerId: String = "") {
        val pid = partnerId.ifBlank {
            val conv = _uiState.value.conversations.find { it.id == conversationId }
            conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
        }

        // 1. Update status pesan masuk di memori UI menjadi terbaca
        _uiState.update { state ->
            val updatedConvs = state.conversations.map { c ->
                if (c.id == conversationId) c.copy(unreadCount = 0) else c
            }
            val msgs = state.messagesMap[conversationId]
            val updatedMsgsMap = if (!msgs.isNullOrEmpty()) {
                val updatedList = msgs.map { m ->
                    if (!m.isFromMe && !m.isRead) m.copy(isRead = true) else m
                }
                state.messagesMap + (conversationId to updatedList)
            } else {
                state.messagesMap
            }
            state.copy(conversations = updatedConvs, messagesMap = updatedMsgsMap)
        }

        // 2. Tandai terbaca di database lokal Room
        viewModelScope.launch(Dispatchers.IO) {
            localChatRepo?.markIncomingMessagesAsRead(conversationId)
        }

        // 3. Beri tahu server Supabase bahwa pesan dari partner telah dibaca (penerima melihat pesan)
        if (!_uiState.value.isGuest && SupabaseClient.isConfigured() && pid.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                supabaseRepo.markMessagesAsRead(conversationId, pid)
            }
        }
    }

    fun markSentMessagesAsReadLocal(conversationId: String) {
        _uiState.update { state ->
            val msgs = state.messagesMap[conversationId] ?: return@update state
            val updated = msgs.map { m ->
                if (m.isFromMe && !m.isRead) m.copy(isRead = true) else m
            }
            val lastMsg = updated.lastOrNull()
            val updatedConvs = state.conversations.map { c ->
                if (c.id == conversationId && lastMsg != null) {
                    c.copy(
                        lastMessageIsFromMe = lastMsg.isFromMe,
                        lastMessageIsRead = lastMsg.isRead
                    )
                } else c
            }
            state.copy(
                conversations = updatedConvs,
                messagesMap = state.messagesMap + (conversationId to updated)
            )
        }
    }

    fun pollChatMessages(conversationId: String, partnerId: String, forceFullSync: Boolean = false) {
        if (_uiState.value.isGuest) return
        if (!SupabaseClient.isConfigured()) return

        val myId = _uiState.value.myLovyId
        val currentMsgs = _uiState.value.messagesMap[conversationId] ?: emptyList()
        // Cek jika ada pesan kita yang belum dibaca agar sinkronisasi mengambil status is_read terbaru dari Supabase
        val hasUnreadSent = currentMsgs.any { it.isFromMe && !it.isRead }
        val sinceTimestamp = if (forceFullSync || currentMsgs.isEmpty() || hasUnreadSent) 0L else (currentMsgs.maxOfOrNull { it.timestamp } ?: 0L)

        viewModelScope.launch(Dispatchers.IO) {
            val remoteMsgs = supabaseRepo.fetchChatMessages(
                conversationId = conversationId,
                currentUserId = myId,
                partnerId = partnerId,
                sinceTimestamp = sinceTimestamp
            )
            if (!remoteMsgs.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    val typingSignals = remoteMsgs.filter { it.text.startsWith("__TYPING_") }
                    val actualChatMsgs = remoteMsgs.filterNot { it.text.startsWith("__TYPING_") }

                    if (typingSignals.isNotEmpty()) {
                        val latestSignal = typingSignals.maxByOrNull { it.timestamp }
                        if (latestSignal != null && !latestSignal.isFromMe) {
                            val now = System.currentTimeMillis()
                            if (latestSignal.text == "__TYPING_START__" && now - latestSignal.timestamp < 6000L) {
                                setPartnerTyping(conversationId, true)
                                typingTimeoutJobs[conversationId]?.cancel()
                                typingTimeoutJobs[conversationId] = viewModelScope.launch {
                                    delay(4000L)
                                    setPartnerTyping(conversationId, false)
                                }
                            } else if (latestSignal.text == "__TYPING_STOP__") {
                                setPartnerTyping(conversationId, false)
                            }
                        }
                    }

                    if (actualChatMsgs.any { !it.isFromMe }) {
                        setPartnerTyping(conversationId, false)
                    }

                    if (actualChatMsgs.isNotEmpty()) {
                        val isViewing = _uiState.value.activeChatId == conversationId
                        val processedMsgs = if (isViewing) {
                            actualChatMsgs.map { if (!it.isFromMe) it.copy(isRead = true) else it }
                        } else {
                            actualChatMsgs
                        }

                        val current = _uiState.value.messagesMap[conversationId] ?: emptyList()
                        val currentIds = current.map { it.id }.toSet()
                        val newPartnerMsgs = actualChatMsgs.filter { !it.isFromMe && !currentIds.contains(it.id) }

                        val msgMap = current.associateBy { it.id }.toMutableMap()
                        for (m in processedMsgs) {
                            msgMap[m.id] = m
                        }
                        val merged = msgMap.values
                            .filterNot { it.deletedForSender && it.isFromMe }
                            .sortedBy { it.timestamp }

                        val lastMsg = merged.lastOrNull()
                        val convExists = _uiState.value.conversations.any { it.id == conversationId }
                        val updatedConvs = if (convExists) {
                            _uiState.value.conversations.map { c ->
                                if (c.id == conversationId && lastMsg != null) {
                                    c.copy(
                                        lastMessage = lastMsg.text,
                                        lastTimestamp = lastMsg.timestamp,
                                        lastMessageIsFromMe = lastMsg.isFromMe,
                                        lastMessageIsRead = lastMsg.isRead
                                    )
                                } else c
                            }
                        } else if (lastMsg != null) {
                            val partnerUser = _uiState.value.nearbyUsers.find { it.id == partnerId }
                                ?: _uiState.value.chattedFriends.find { it.id == partnerId }
                            listOf(
                                ChatConversation(
                                    id = conversationId,
                                    partnerId = partnerId,
                                    partnerName = partnerUser?.name ?: "Pengguna ($partnerId)",
                                    partnerAvatarHex = partnerUser?.avatarColorHex ?: 0xFF4CAF50,
                                    partnerGender = partnerUser?.gender ?: Gender.FEMALE,
                                    lastMessage = lastMsg.text,
                                    lastTimestamp = lastMsg.timestamp,
                                    unreadCount = 0,
                                    isOnline = partnerUser?.isOnline ?: true,
                                    partnerAvatarUrl = partnerUser?.avatarUrl,
                                    lastMessageIsFromMe = lastMsg.isFromMe,
                                    lastMessageIsRead = lastMsg.isRead,
                                    partnerAge = partnerUser?.age ?: 22,
                                    partnerDistanceMeters = partnerUser?.distanceMeters ?: 300,
                                    partnerCity = partnerUser?.city
                                )
                            ) + _uiState.value.conversations
                        } else {
                            _uiState.value.conversations
                        }

                        if (newPartnerMsgs.isNotEmpty()) {
                            val appCtx = getApplication<Application>()
                            if (isViewing) {
                                com.example.util.LovyNotificationHelper.vibrateSubtle(appCtx)
                            } else {
                                val partnerName = updatedConvs.find { it.id == conversationId }?.partnerName ?: "Teman Lovy"
                                val latest = newPartnerMsgs.maxByOrNull { it.timestamp }
                                if (latest != null) {
                                    com.example.util.LovyNotificationHelper.showChatNotification(
                                        context = appCtx,
                                        conversationId = conversationId,
                                        senderName = partnerName,
                                        messageText = latest.text
                                    )
                                }
                            }
                        }

                        _uiState.update {
                            it.copy(
                                conversations = updatedConvs,
                                messagesMap = it.messagesMap + (conversationId to merged)
                            )
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            actualChatMsgs.forEach { localChatRepo.saveMessage(it) }
                        }

                        if (isViewing && actualChatMsgs.any { !it.isFromMe && !it.isRead }) {
                            viewModelScope.launch(Dispatchers.IO) {
                                supabaseRepo.markMessagesAsRead(conversationId, partnerId)
                                localChatRepo?.markIncomingMessagesAsRead(conversationId)
                            }
                        }
                    }
                }
            }
        }
    }

    fun openChatWithBottleSender(bottle: BottleMessage) {
        if (isUserBlocked(bottle.senderId, bottle.senderName)) return
        recordFeatureClick()
        val friendUser = User(
            id = bottle.senderId,
            name = bottle.senderName,
            gender = bottle.senderGender,
            age = 22,
            distanceMeters = 500,
            bio = "Penulis pesan botol",
            avatarColorHex = bottle.avatarHex,
            avatarUrl = bottle.avatarUrl
        )
        saveChatFriend(friendUser)
        updateUserActivity()

        val convId = getCanonicalConversationId(_uiState.value.myLovyId, bottle.senderId)
        val existing = _uiState.value.conversations.find { it.id == convId || it.partnerId == bottle.senderId }
        val greetingText = "Halo ${bottle.senderName}! Aku menemukan pesan botolmu: \"${bottle.content.take(30)}...\" 🍾🌊"
        val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = convId,
            text = greetingText,
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            localChatRepo.saveMessage(newMsg)
        }
        val updatedMessages = (_uiState.value.messagesMap[convId] ?: emptyList()) + newMsg
        val updatedConversations = if (existing != null) {
            _uiState.value.conversations.map {
                if (it.id == convId || it.id == existing.id) it.copy(id = convId, lastMessage = greetingText, lastTimestamp = System.currentTimeMillis()) else it
            }
        } else {
            listOf(
                ChatConversation(
                    id = convId,
                    partnerId = bottle.senderId,
                    partnerName = bottle.senderName,
                    partnerAvatarHex = bottle.avatarHex,
                    partnerGender = bottle.senderGender,
                    lastMessage = greetingText,
                    lastTimestamp = System.currentTimeMillis(),
                    unreadCount = 0,
                    isOnline = true,
                    partnerAvatarUrl = bottle.avatarUrl
                )
            ) + _uiState.value.conversations
        }

        _uiState.update {
            it.copy(
                fishedBottle = null,
                conversations = updatedConversations,
                messagesMap = it.messagesMap + (convId to updatedMessages)
            )
        }

        // Kirim langsung ke Supabase untuk pesan balasan botol
        viewModelScope.launch {
            supabaseRepo.sendChatMessage(
                message = newMsg,
                senderId = _uiState.value.myLovyId,
                receiverId = bottle.senderId
            )
        }

        openChat(convId, bottle.senderName, bottle.avatarHex)
    }

    fun deleteMessageForSender(conversationId: String, messageId: String) {
        deleteMessageForMe(conversationId, messageId)
    }

    /**
     * Hapus Pesan untuk Saya (Delete for Me)
     * Pesan dihapus dari tampilan pengguna saat ini saja (baik pesan kiriman sendiri maupun pesan dari lawan bicara).
     */
    fun deleteMessageForMe(conversationId: String, messageId: String) {
        val currentMsgs = _uiState.value.messagesMap[conversationId] ?: emptyList()
        val targetMsg = currentMsgs.find { it.id == messageId }
        
        val updatedMsgs = currentMsgs.map { msg ->
            if (msg.id == messageId) {
                if (msg.isFromMe) msg.copy(deletedForSender = true)
                else msg.copy(deletedForReceiver = true)
            } else msg
        }.filterNot { (it.deletedForSender && it.isFromMe) || (it.deletedForReceiver && !it.isFromMe) }

        val lastRemainingText = updatedMsgs.lastOrNull()?.text ?: "Tidak ada pesan"

        val updatedConvs = _uiState.value.conversations.map {
            if (it.id == conversationId) {
                it.copy(lastMessage = lastRemainingText)
            } else it
        }

        _uiState.update {
            it.copy(
                messagesMap = it.messagesMap + (conversationId to updatedMsgs),
                conversations = updatedConvs
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            localChatRepo.deleteMessage(messageId)
            if (!_uiState.value.isGuest) {
                if (targetMsg?.isFromMe == true) {
                    supabaseRepo.markMessageDeletedForSender(messageId)
                }
            }
        }
    }

    /**
     * Hapus Pesan untuk Semua Orang (Delete for Everyone)
     * Hanya berlaku untuk pesan yang dikirim oleh diri sendiri.
     * Menghapus pesan di HP sendiri dan menyiarkan perintah hapus ke teman lawan bicara sehingga
     * pesan di HP teman juga otomatis lenyap secara instan (Realtime).
     */
    fun deleteMessageForEveryone(conversationId: String, messageId: String) {
        val currentMsgs = _uiState.value.messagesMap[conversationId] ?: emptyList()
        val targetMsg = currentMsgs.find { it.id == messageId } ?: return

        // Hanya pesan dari diri sendiri yang dapat dihapus untuk semua orang
        if (!targetMsg.isFromMe) return

        val updatedMsgs = currentMsgs.filterNot { it.id == messageId }
        val lastRemainingText = updatedMsgs.lastOrNull()?.text ?: "Tidak ada pesan"

        val updatedConvs = _uiState.value.conversations.map {
            if (it.id == conversationId) {
                it.copy(lastMessage = lastRemainingText)
            } else it
        }

        _uiState.update {
            it.copy(
                messagesMap = it.messagesMap + (conversationId to updatedMsgs),
                conversations = updatedConvs
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            // Hapus dari database lokal Room
            localChatRepo.deleteMessage(messageId)

            if (!_uiState.value.isGuest) {
                // Siarkan broadcast event Realtime agar HP teman langsung menghapus pesan ini
                val deleteDto = com.example.data.supabase.SupabaseMessageDto(
                    id = messageId,
                    conversationId = conversationId,
                    senderId = _uiState.value.myLovyId,
                    text = "__DELETED_FOR_EVERYONE__",
                    createdAt = targetMsg.timestamp,
                    deletedForSender = true,
                    deletedForReceiver = true
                )
                com.example.data.supabase.SupabaseRealtimeManager.broadcastChatMessage(deleteDto)

                // Hapus dan tandai di database cloud Supabase
                supabaseRepo.deleteMessageForEveryone(messageId)
            }
        }
    }

    /**
     * Hapus obrolan terpilih dari daftar (multi-delete conversation).
     * Menghapus riwayat percakapan dari Room database lokal dan daftar obrolan di UI.
     */
    fun deleteConversations(conversationIds: Set<String>) {
        if (conversationIds.isEmpty()) return

        val remainingConvs = _uiState.value.conversations.filterNot { conversationIds.contains(it.id) }
        val remainingMessagesMap = _uiState.value.messagesMap.filterKeys { !conversationIds.contains(it) }

        _uiState.update {
            it.copy(
                conversations = remainingConvs,
                messagesMap = remainingMessagesMap
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            conversationIds.forEach { convId ->
                localChatRepo.clearConversation(convId)
            }
        }
    }

    fun sendMessage(
        conversationId: String,
        text: String,
        partnerName: String,
        imageUrl: String? = null,
        replyToId: String? = null,
        replyToSender: String? = null,
        replyToText: String? = null
    ) {
        if (text.isBlank() && imageUrl.isNullOrBlank()) return
        if (isUserBlocked(userName = partnerName)) return
        updateUserActivity()
        val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            text = text.trim(),
            timestamp = System.currentTimeMillis(),
            isFromMe = true,
            isRead = false,
            imageUrl = imageUrl,
            replyToId = replyToId,
            replyToSender = replyToSender,
            replyToText = replyToText
        )

        val previewText = if (imageUrl != null && text.isBlank()) "📷 [Foto]" else text.trim()

        val updatedMessages = (_uiState.value.messagesMap[conversationId] ?: emptyList()) + newMsg
        val updatedConversations = _uiState.value.conversations.map {
            if (it.id == conversationId) {
                it.copy(
                    lastMessage = previewText,
                    lastTimestamp = System.currentTimeMillis(),
                    lastMessageIsFromMe = true,
                    lastMessageIsRead = false
                )
            } else it
        }

        _uiState.update {
            it.copy(
                conversations = updatedConversations,
                messagesMap = it.messagesMap + (conversationId to updatedMessages)
            )
        }

        com.example.util.LovyNotificationHelper.vibrateSubtle(getApplication())

        viewModelScope.launch(Dispatchers.IO) {
            localChatRepo.saveMessage(newMsg)
        }

        // Sinkronisasi ke Supabase untuk obrolan 2 arah nyata
        val conv = _uiState.value.conversations.find { it.id == conversationId }
        val partnerId = conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
        onUserTyping(conversationId, partnerId, false)
        viewModelScope.launch {
            supabaseRepo.sendChatMessage(
                message = newMsg,
                senderId = _uiState.value.myLovyId,
                receiverId = partnerId
            )
        }
    }

    fun throwBottle(content: String): Boolean {
        if (content.isBlank()) return false
        recordFeatureClick()
        val newBottle = BottleMessage(
            id = UUID.randomUUID().toString(),
            senderId = "me",
            senderName = _uiState.value.myName,
            senderGender = Gender.MALE,
            avatarHex = 0xFF00A86B,
            content = content.trim(),
            thrownTimestamp = System.currentTimeMillis(),
            locationHint = "Laut Nusantara",
            isFromMe = true,
            replyCount = 0
        )
        val updatedMyBottles = listOf(newBottle) + _uiState.value.myBottles
        saveMyBottles(updatedMyBottles)
        _uiState.update {
            it.copy(
                myBottles = updatedMyBottles,
                oceanBottles = listOf(newBottle) + it.oceanBottles
            )
        }

        // Sinkronisasi ke Supabase (hanya untuk pengguna asli)
        if (!_uiState.value.isGuest) {
            viewModelScope.launch {
                supabaseRepo.sendBottle(newBottle)
            }
        }

        return true
    }

    fun fishBottle() {
        recordFeatureClick()
        viewModelScope.launch {
            _uiState.update { it.copy(isFishing = true, fishedBottle = null) }
            
            // Coba ambil botol terbaru dari Supabase jika ada (hanya pengguna asli)
            if (!_uiState.value.isGuest) {
                try {
                    val remoteBottles = supabaseRepo.fetchOceanBottles()
                    if (!remoteBottles.isNullOrEmpty()) {
                        _uiState.update { it.copy(oceanBottles = remoteBottles) }
                    }
                } catch (_: Throwable) {
                }
            }

            delay(1200)
            val allOcean = _uiState.value.oceanBottles.filter { !it.isFromMe }
            val currentFished = _uiState.value.fishedBottles
            
            // Prioritaskan botol di lautan yang belum pernah diambil
            val unfished = allOcean.filter { oceanB -> currentFished.none { it.id == oceanB.id } }
            val chosen = when {
                unfished.isNotEmpty() -> unfished.random()
                allOcean.isNotEmpty() -> allOcean.random()
                else -> null
            }

            if (chosen == null) {
                _uiState.update { it.copy(isFishing = false, fishedBottle = null) }
                return@launch
            }

            val updatedFished = if (currentFished.none { it.id == chosen.id }) {
                listOf(chosen) + currentFished
            } else {
                currentFished
            }
            saveFishedBottles(updatedFished)

            _uiState.update { 
                it.copy(
                    isFishing = false, 
                    fishedBottle = chosen,
                    fishedBottles = updatedFished
                ) 
            }
        }
    }

    fun dismissFishedBottle() {
        recordFeatureClick()
        _uiState.update { it.copy(fishedBottle = null) }
    }

    fun returnFishedBottleToOcean(bottle: BottleMessage) {
        recordFeatureClick()
        val updatedFished = _uiState.value.fishedBottles.filter { it.id != bottle.id }
        saveFishedBottles(updatedFished)
        _uiState.update { 
            it.copy(
                fishedBottles = updatedFished,
                fishedBottle = if (it.fishedBottle?.id == bottle.id) null else it.fishedBottle
            ) 
        }
    }

    fun toggleLikeMoment(momentId: String) {
        recordFeatureClick()
        _uiState.update { state ->
            val updated = state.moments.map { item ->
                if (item.id == momentId) {
                    val newLiked = !item.isLiked
                    val newCount = if (newLiked) item.likesCount + 1 else item.likesCount - 1
                    item.copy(isLiked = newLiked, likesCount = newCount)
                } else item
            }
            state.copy(moments = updated)
        }
    }

    fun addMomentComment(momentId: String, text: String) {
        if (text.isBlank()) return
        recordFeatureClick()
        val state = _uiState.value
        val myId = state.myLovyId.ifBlank { "me" }
        val newComment = com.example.model.MomentComment(
            id = UUID.randomUUID().toString(),
            momentId = momentId,
            authorId = myId,
            authorName = state.myName,
            authorAvatarHex = 0xFF00A86B,
            authorAvatarUrl = state.userProfile.profilePicture?.takeIf { it.isNotBlank() },
            text = text.trim(),
            timestamp = System.currentTimeMillis(),
            timeAgo = "Baru saja"
        )

        val existingComments = state.momentComments[momentId] ?: emptyList()
        val updatedComments = existingComments + newComment
        val updatedMap = state.momentComments + (momentId to updatedComments)

        val updatedMoments = state.moments.map { item ->
            if (item.id == momentId) {
                item.copy(commentsCount = updatedComments.size)
            } else item
        }

        _uiState.update {
            it.copy(
                moments = updatedMoments,
                momentComments = updatedMap
            )
        }
    }

    fun postMoment(content: String, imageUrl: String? = null, locationTag: String? = null) {
        if (content.isBlank()) return
        recordFeatureClick()
        val authorId = _uiState.value.myLovyId.ifBlank { "me" }
        val newMoment = MomentItem(
            id = UUID.randomUUID().toString(),
            authorName = _uiState.value.myName,
            authorAvatarHex = 0xFF00A86B,
            timeAgo = "Baru saja",
            content = content.trim(),
            likesCount = 0,
            isLiked = false,
            commentsCount = 0,
            imageUrl = imageUrl,
            authorAvatarUrl = null,
            locationTag = locationTag?.ifBlank { null }
                ?: _uiState.value.currentGpsLocation?.cityName?.ifBlank { null }
                ?: "Surabaya",
            authorId = authorId
        )
        val newMomentIds = _uiState.value.myMomentIds + newMoment.id
        try {
            prefs.edit().putStringSet("my_moment_ids", newMomentIds).apply()
            saveMyLocalMoment(newMoment)
        } catch (_: Throwable) {
        }
        _uiState.update { it.copy(moments = listOf(newMoment) + it.moments, myMomentIds = newMomentIds) }

        // Simpan ke Supabase (hanya jika bukan mode tamu)
        if (!_uiState.value.isGuest) {
            viewModelScope.launch {
                val ok = supabaseRepo.sendMoment(newMoment, authorId)
                if (ok) {
                    Log.d("LovyChatViewModel", "Momen berhasil disimpan ke server cloud Supabase: ${newMoment.id}")
                } else {
                    Log.w("LovyChatViewModel", "Gagal menyimpan momen ke server cloud Supabase: ${newMoment.id}")
                }
            }
        }
    }

    fun deleteMoment(momentId: String) {
        recordFeatureClick()
        val newMomentIds = _uiState.value.myMomentIds - momentId
        try {
            prefs.edit().putStringSet("my_moment_ids", newMomentIds).apply()
            removeMyLocalMoment(momentId)
        } catch (_: Throwable) {
        }
        _uiState.update { state ->
            state.copy(
                moments = state.moments.filter { it.id != momentId },
                myMomentIds = newMomentIds
            )
        }
        try {
            android.widget.Toast.makeText(getApplication(), "Momen berhasil dihapus", android.widget.Toast.LENGTH_SHORT).show()
        } catch (_: Throwable) {
        }
        // Hapus dari Supabase jika tersambung (hanya jika bukan mode tamu)
        if (!_uiState.value.isGuest) {
            viewModelScope.launch {
                try {
                    supabaseRepo.deleteMoment(momentId)
                } catch (e: Exception) {
                    Log.w("LovyChatViewModel", "Gagal menghapus momen di cloud: $momentId", e)
                }
            }
        }
    }

    // --- Cloudflare R2 Photo Upload Methods ---

    /**
     * Upload user profile photo to Cloudflare R2 and update profile state & database.
     */
    fun uploadProfilePhoto(
        uri: android.net.Uri,
        context: android.content.Context? = null,
        onComplete: ((Boolean, String?) -> Unit)? = null
    ) {
        val ctx = context ?: getApplication<Application>()
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUploadingPhoto = true,
                    uploadProgressText = "Mengunggah foto profil ke Cloudflare R2..."
                )
            }
            try {
                val bytes = com.example.util.ImageCompressor.compressImage(
                    context = ctx,
                    uri = uri,
                    maxDimension = 800,
                    quality = 85
                )
                if (bytes == null || bytes.isEmpty()) {
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    onComplete?.invoke(false, "Gagal memproses gambar.")
                    return@launch
                }

                val fileName = "avatar_${_uiState.value.myLovyId}_${System.currentTimeMillis()}.jpg"
                val result = com.example.data.storage.R2StorageClient.uploadImage(
                    bytes = bytes,
                    folder = "avatars",
                    fileName = fileName
                )

                if (result.isSuccess) {
                    val publicUrl = result.getOrThrow()
                    val updatedProfile = _uiState.value.userProfile.copy(profilePicture = publicUrl)
                    saveUserProfile(updatedProfile)
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    onComplete?.invoke(true, publicUrl)
                } else {
                    val err = result.exceptionOrNull()?.localizedMessage ?: "Gagal mengunggah ke Cloudflare R2"
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    onComplete?.invoke(false, err)
                }
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error upload profile photo", e)
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                onComplete?.invoke(false, e.localizedMessage)
            }
        }
    }

    /**
     * Kosongkan foto profil pengguna sehingga kembali ke logo default Lovy Chat.
     */
    fun clearProfilePhoto(onComplete: ((Boolean) -> Unit)? = null) {
        recordFeatureClick()
        val updatedProfile = _uiState.value.userProfile.copy(profilePicture = null)
        saveUserProfile(updatedProfile)
        try {
            authRepo.updateAvatarUrl(null)
        } catch (e: Exception) {
            Log.w("LovyChatViewModel", "Gagal update session avatar", e)
        }
        onComplete?.invoke(true)
    }

    /**
     * Post a moment with an optional photo uploaded to Cloudflare R2.
     */
    fun postMomentWithPhoto(
        content: String,
        uri: android.net.Uri?,
        locationTag: String? = null,
        context: android.content.Context? = null,
        onComplete: ((Boolean) -> Unit)? = null
    ) {
        if (content.isBlank()) {
            onComplete?.invoke(false)
            return
        }

        val ctx = context ?: getApplication<Application>()
        if (uri == null) {
            postMoment(content, null, locationTag)
            onComplete?.invoke(true)
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUploadingPhoto = true,
                    uploadProgressText = "Mengunggah foto momen..."
                )
            }
            try {
                val bytes = com.example.util.ImageCompressor.compressImage(
                    context = ctx,
                    uri = uri,
                    maxDimension = 1280,
                    quality = 85
                )
                var uploadedUrl: String? = null
                if (bytes != null && bytes.isNotEmpty()) {
                    val fileName = "moment_${UUID.randomUUID()}.jpg"
                    val result = com.example.data.storage.R2StorageClient.uploadImage(
                        bytes = bytes,
                        folder = "moments",
                        fileName = fileName
                    )
                    if (result.isSuccess) {
                        uploadedUrl = result.getOrNull()
                    } else {
                        Log.e("LovyChatViewModel", "Gagal upload gambar momen ke R2: ${result.exceptionOrNull()?.message}")
                    }
                }

                postMoment(content, uploadedUrl, locationTag)
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                val isSuccess = !uploadedUrl.isNullOrBlank()
                onComplete?.invoke(isSuccess)
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error posting moment with photo", e)
                // Fallback to text moment if image upload fails
                postMoment(content, null, locationTag)
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                onComplete?.invoke(false)
            }
        }
    }

    /**
     * Upload a photo to Cloudflare R2 and send it as a chat message.
     */
    fun sendPhotoMessage(
        conversationId: String,
        uri: android.net.Uri,
        partnerName: String,
        caption: String = "",
        context: android.content.Context? = null,
        replyToId: String? = null,
        replyToSender: String? = null,
        replyToText: String? = null
    ) {
        if (isUserBlocked(userName = partnerName)) return
        updateUserActivity()

        val ctx = context ?: getApplication<Application>()
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUploadingPhoto = true,
                    uploadProgressText = "Mengunggah foto chat ke Cloudflare R2..."
                )
            }
            try {
                val bytes = com.example.util.ImageCompressor.compressImage(
                    context = ctx,
                    uri = uri,
                    maxDimension = 1280,
                    quality = 85
                )
                if (bytes == null || bytes.isEmpty()) {
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    return@launch
                }

                val fileName = "chat_${UUID.randomUUID()}.jpg"
                val result = com.example.data.storage.R2StorageClient.uploadImage(
                    bytes = bytes,
                    folder = "chats/$conversationId",
                    fileName = fileName
                )

                if (result.isSuccess) {
                    val photoUrl = result.getOrThrow()
                    val displayText = caption.trim().ifBlank { "📷 Foto" }
                    val newMsg = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        conversationId = conversationId,
                        text = displayText,
                        timestamp = System.currentTimeMillis(),
                        isFromMe = true,
                        isRead = false,
                        imageUrl = photoUrl,
                        replyToId = replyToId,
                        replyToSender = replyToSender,
                        replyToText = replyToText
                    )

                    viewModelScope.launch(Dispatchers.IO) {
                        localChatRepo.saveMessage(newMsg)
                    }

                    val updatedMessages = (_uiState.value.messagesMap[conversationId] ?: emptyList()) + newMsg
                    val updatedConversations = _uiState.value.conversations.map {
                        if (it.id == conversationId) {
                            it.copy(
                                lastMessage = "📷 Foto",
                                lastTimestamp = System.currentTimeMillis(),
                                lastMessageIsFromMe = true,
                                lastMessageIsRead = false
                            )
                        } else it
                    }

                    _uiState.update {
                        it.copy(
                            conversations = updatedConversations,
                            messagesMap = it.messagesMap + (conversationId to updatedMessages),
                            isUploadingPhoto = false,
                            uploadProgressText = null
                        )
                    }

                    // Sinkronisasi ke Supabase untuk pesan foto
                    val conv = _uiState.value.conversations.find { it.id == conversationId }
                    val partnerId = conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
                    supabaseRepo.sendChatMessage(
                        message = newMsg,
                        senderId = _uiState.value.myLovyId,
                        receiverId = partnerId
                    )
                } else {
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                }
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error sending photo message", e)
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
            }
        }
    }

    private var periodicIncomingChatJob: kotlinx.coroutines.Job? = null
    private var realtimeSubscriptionJob: kotlinx.coroutines.Job? = null

    fun startRealtimeChatSubscription() {
        val myId = _uiState.value.myLovyId
        if (myId.isBlank() || _uiState.value.isGuest || !SupabaseClient.isConfigured()) return

        SupabaseRealtimeManager.connect(myId)

        realtimeSubscriptionJob?.cancel()
        realtimeSubscriptionJob = viewModelScope.launch(Dispatchers.IO) {
            SupabaseRealtimeManager.incomingMessages.collect { messageDto ->
                try {
                    val currentId = _uiState.value.myLovyId
                    val convId = messageDto.conversationId
                    val senderId = messageDto.senderId
                    val receiverId = messageDto.receiverId

                    val isRelevant = (receiverId != null && receiverId.equals(currentId, ignoreCase = true)) ||
                            senderId.equals(currentId, ignoreCase = true) ||
                            convId.contains(currentId)

                    if (isRelevant) {
                        withContext(Dispatchers.Main) {
                            // Cek jika pesan ini adalah perintah hapus untuk semua orang
                            if (messageDto.text == "__DELETED_FOR_EVERYONE__" || 
                                (messageDto.deletedForSender == true && messageDto.deletedForReceiver == true)) {
                                val currentMsgs = _uiState.value.messagesMap[convId] ?: emptyList()
                                val updatedMsgs = currentMsgs.filterNot { it.id == messageDto.id }
                                val lastRemaining = updatedMsgs.lastOrNull()?.text ?: "Tidak ada pesan"
                                _uiState.update { state ->
                                    val updatedConvs = state.conversations.map {
                                        if (it.id == convId) it.copy(lastMessage = lastRemaining) else it
                                    }
                                    state.copy(
                                        messagesMap = state.messagesMap + (convId to updatedMsgs),
                                        conversations = updatedConvs
                                    )
                                }
                                viewModelScope.launch(Dispatchers.IO) {
                                    localChatRepo.deleteMessage(messageDto.id)
                                }
                                return@withContext
                            }

                            processIncomingRecentMessages(listOf(messageDto), currentId)

                            // Jika user sedang aktif membuka percakapan ini dan pesan dari orang lain, tandai terbaca instan
                            if (_uiState.value.activeChatId == convId && !isSenderMe(senderId, receiverId, currentId)) {
                                val partnerId = extractPartnerIdFromConvId(convId, currentId)
                                viewModelScope.launch(Dispatchers.IO) {
                                    supabaseRepo.markMessagesAsRead(convId, partnerId)
                                    localChatRepo?.markIncomingMessagesAsRead(convId)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.w("LovyRealtime", "Error processing realtime chat message: ${e.message}")
                }
            }
        }
    }

    private fun startIncomingChatPeriodicSync() {
        periodicIncomingChatJob?.cancel()
        periodicIncomingChatJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                kotlinx.coroutines.delay(4500L)
                try {
                    val state = _uiState.value
                    if (!state.isGuest && SupabaseClient.isConfigured() && state.myLovyId.isNotBlank()) {
                        val recent = supabaseRepo.fetchRecentMessagesForUser(state.myLovyId)
                        if (!recent.isNullOrEmpty()) {
                            withContext(Dispatchers.Main) {
                                processIncomingRecentMessages(recent, state.myLovyId)
                            }
                        }
                    }
                } catch (_: Exception) {}
            }
        }
    }

    private fun isSenderMe(senderId: String, receiverId: String?, myId: String): Boolean {
        if (myId.isBlank()) return false
        val cleanMy = myId.trim()
        val s = senderId.trim()
        val r = receiverId?.trim()
        if (s.equals(cleanMy, ignoreCase = true)) return true
        if (r != null && r.equals(cleanMy, ignoreCase = true)) return false
        if (s.isNotBlank() && !s.equals("me", ignoreCase = true) && !s.equals(cleanMy, ignoreCase = true)) return false
        if (s.equals("me", ignoreCase = true) && r != null && !r.equals(cleanMy, ignoreCase = true)) return true
        return false
    }

    fun syncIncomingChats() {
        if (_uiState.value.isGuest) return
        if (!SupabaseClient.isConfigured()) return
        val myId = _uiState.value.myLovyId
        viewModelScope.launch(Dispatchers.IO) {
            val recent = supabaseRepo.fetchRecentMessagesForUser(myId)
            if (!recent.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    processIncomingRecentMessages(recent, myId)
                }
            }
        }
    }

    private fun processIncomingRecentMessages(recent: List<com.example.data.supabase.SupabaseMessageDto>, myId: String) {
        val grouped = recent.groupBy { it.conversationId }
        val currentConversations = _uiState.value.conversations.toMutableList()
        val currentMessages = _uiState.value.messagesMap.toMutableMap()

        for ((convId, dtoList) in grouped) {
            val partnerId = extractPartnerIdFromConvId(convId, myId).ifBlank {
                dtoList.firstNotNullOfOrNull { dto ->
                    if (dto.senderId != myId && dto.senderId != "me") dto.senderId
                    else if (dto.receiverId != null && dto.receiverId != myId) dto.receiverId
                    else null
                } ?: ""
            }
            if (partnerId.isBlank()) continue

            // Abaikan sinyal ephemeral mengetik dan pesan terhapus untuk semua orang
            val chatDtos = dtoList.filterNot { 
                it.text.startsWith("__TYPING_") || 
                it.text == "__DELETED_FOR_EVERYONE__" ||
                (it.deletedForSender == true && it.deletedForReceiver == true)
            }
            if (chatDtos.isEmpty()) {
                // Jika ada pesan deleted_for_everyone di batch ini, hapus dari list lokal
                val deletedIds = dtoList.filter { 
                    it.text == "__DELETED_FOR_EVERYONE__" || 
                    (it.deletedForSender == true && it.deletedForReceiver == true) 
                }.map { it.id }.toSet()
                if (deletedIds.isNotEmpty()) {
                    val existing = currentMessages[convId] ?: emptyList()
                    currentMessages[convId] = existing.filterNot { deletedIds.contains(it.id) }
                }
                continue
            }

            val sortedMsgs = chatDtos.sortedBy { it.createdAt }.map { dto ->
                ChatMessage(
                    id = dto.id,
                    conversationId = convId,
                    text = dto.text,
                    timestamp = dto.createdAt,
                    isFromMe = isSenderMe(dto.senderId, dto.receiverId, myId),
                    isRead = dto.isRead ?: false,
                    deletedForSender = dto.deletedForSender ?: false,
                    deletedForReceiver = dto.deletedForReceiver ?: false,
                    imageUrl = dto.imageUrl
                )
            }

            viewModelScope.launch(Dispatchers.IO) {
                sortedMsgs.forEach { localChatRepo.saveMessage(it) }
            }

            val existingMsgs = currentMessages[convId] ?: emptyList()
            val existingIds = existingMsgs.map { it.id }.toSet()
            val newIncomingMsgs = sortedMsgs.filter { !it.isFromMe && !existingIds.contains(it.id) }

            val msgMap = existingMsgs.associateBy { it.id }.toMutableMap()
            for (m in sortedMsgs) {
                msgMap[m.id] = m
            }
            val mergedMsgs = msgMap.values.sortedBy { it.timestamp }
            currentMessages[convId] = mergedMsgs

            val lastMsg = mergedMsgs.lastOrNull() ?: continue
            val existingConvIndex = currentConversations.indexOfFirst { it.id == convId || it.partnerId == partnerId }
            var resolvedPartnerName: String? = null
            if (existingConvIndex >= 0) {
                val old = currentConversations[existingConvIndex]
                resolvedPartnerName = old.partnerName
                currentConversations[existingConvIndex] = old.copy(
                    id = convId,
                    lastMessage = lastMsg.text,
                    lastTimestamp = lastMsg.timestamp,
                    lastMessageIsFromMe = lastMsg.isFromMe,
                    lastMessageIsRead = lastMsg.isRead
                )
            } else {
                val partnerUser = _uiState.value.nearbyUsers.find { it.id == partnerId } 
                    ?: _uiState.value.chattedFriends.find { it.id == partnerId }
                resolvedPartnerName = partnerUser?.name
                if (partnerUser != null) {
                    saveChatFriend(partnerUser)
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        val cloudUser = supabaseRepo.fetchNearbyUserById(partnerId)
                        if (cloudUser != null) {
                            saveChatFriend(cloudUser)
                            withContext(Dispatchers.Main) {
                                _uiState.update { state ->
                                    val updated = state.conversations.map { c ->
                                        if (c.partnerId == partnerId) {
                                            c.copy(
                                                partnerName = cloudUser.name,
                                                partnerAvatarUrl = cloudUser.avatarUrl,
                                                partnerGender = cloudUser.gender,
                                                partnerAvatarHex = cloudUser.avatarColorHex,
                                                partnerCity = cloudUser.city
                                            )
                                        } else c
                                    }
                                    state.copy(conversations = updated)
                                }
                            }
                        }
                    }
                }
                val newConv = ChatConversation(
                    id = convId,
                    partnerId = partnerId,
                    partnerName = partnerUser?.name ?: "Teman Lovy",
                    partnerAvatarHex = partnerUser?.avatarColorHex ?: 0xFF4CAF50,
                    partnerGender = partnerUser?.gender ?: Gender.FEMALE,
                    lastMessage = lastMsg.text,
                    lastTimestamp = lastMsg.timestamp,
                    unreadCount = if (!lastMsg.isFromMe && _uiState.value.activeChatId != convId) 1 else 0,
                    isOnline = partnerUser?.isOnline ?: true,
                    partnerAvatarUrl = partnerUser?.avatarUrl,
                    lastMessageIsFromMe = lastMsg.isFromMe,
                    lastMessageIsRead = lastMsg.isRead,
                    partnerAge = partnerUser?.age ?: 22,
                    partnerDistanceMeters = partnerUser?.distanceMeters ?: 350,
                    partnerCity = partnerUser?.city
                )
                currentConversations.add(0, newConv)
            }

            if (newIncomingMsgs.isNotEmpty()) {
                val latest = newIncomingMsgs.maxByOrNull { it.timestamp }
                if (latest != null) {
                    val appCtx = getApplication<Application>()
                    val isViewing = _uiState.value.activeChatId == convId
                    if (isViewing) {
                        com.example.util.LovyNotificationHelper.vibrateSubtle(appCtx)
                    } else {
                        val senderName = resolvedPartnerName ?: "Teman Lovy"
                        com.example.util.LovyNotificationHelper.showChatNotification(
                            context = appCtx,
                            conversationId = convId,
                            senderName = senderName,
                            messageText = latest.text
                        )
                    }
                }
            }
        }

        _uiState.update {
            it.copy(
                conversations = currentConversations,
                messagesMap = currentMessages
            )
        }
        refreshNewFriendRequests()
    }
}

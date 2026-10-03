package com.example.ui

import android.app.Application
import com.example.BuildConfig
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
import kotlinx.coroutines.isActive
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
    val ignoredNewFriendNames: Set<String> = emptySet(),
    val appUpdateInfo: com.example.model.AppUpdateInfo? = null,

    val nearbyGenderFilter: Gender? = null,
    val nearbyOnlyOnlineFilter: Boolean = true,
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
    val typingMap: Map<String, Boolean> = emptyMap(),
    val activityNotifications: List<com.example.model.ActivityNotification> = emptyList()
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
    private var heartbeatJob: kotlinx.coroutines.Job? = null
    private var lastTypingSentTime = 0L
    private val typingTimeoutJobs = mutableMapOf<String, kotlinx.coroutines.Job>()

    // Pelacakan pesan dan obrolan yang telah dihapus agar tidak pernah memicu notifikasi atau bangkit kembali saat sync
    private val deletedMessageIds = java.util.Collections.synchronizedSet(mutableSetOf<String>())
    private val deletedConversationTimestamps = java.util.Collections.synchronizedMap(mutableMapOf<String, Long>())

    private fun loadDeletedTrackingData() {
        try {
            val savedMsgIds = prefs.getStringSet("deleted_message_ids", emptySet()) ?: emptySet()
            deletedMessageIds.addAll(savedMsgIds)

            val rawConvMap = prefs.getString("deleted_conversations_map", null)
            if (!rawConvMap.isNullOrBlank()) {
                val json = org.json.JSONObject(rawConvMap)
                val keys = json.keys()
                while (keys.hasNext()) {
                    val k = keys.next()
                    deletedConversationTimestamps[k] = json.optLong(k, 0L)
                }
            }
        } catch (e: Exception) {
            Log.w("LovyChatViewModel", "loadDeletedTrackingData error: ${e.message}")
        }
    }

    private fun persistDeletedMessageId(id: String) {
        if (id.isBlank()) return
        deletedMessageIds.add(id)
        try {
            prefs.edit().putStringSet("deleted_message_ids", HashSet(deletedMessageIds)).apply()
        } catch (_: Exception) {}
    }

    private fun persistDeletedConversation(conversationId: String, timestamp: Long = System.currentTimeMillis()) {
        if (conversationId.isBlank()) return
        deletedConversationTimestamps[conversationId] = timestamp
        try {
            val json = org.json.JSONObject()
            deletedConversationTimestamps.forEach { (k, v) -> json.put(k, v) }
            prefs.edit().putString("deleted_conversations_map", json.toString()).apply()
        } catch (_: Exception) {}
    }

    // Pelacakan permintaan teman baru yang diabaikan agar tersimpan permanen dan tidak pernah muncul lagi di Teman Baru
    private val ignoredFriendRequestIds = java.util.Collections.synchronizedSet(mutableSetOf<String>())
    private val ignoredFriendRequestNames = java.util.Collections.synchronizedSet(mutableSetOf<String>())

    private fun loadIgnoredFriendRequestsData() {
        try {
            val savedIds = prefs.getStringSet("ignored_friend_request_ids", emptySet()) ?: emptySet()
            val savedNames = prefs.getStringSet("ignored_friend_request_names", emptySet()) ?: emptySet()
            ignoredFriendRequestIds.addAll(savedIds)
            ignoredFriendRequestNames.addAll(savedNames)
            _uiState.update { 
                it.copy(
                    ignoredNewFriendIds = HashSet(ignoredFriendRequestIds),
                    ignoredNewFriendNames = HashSet(ignoredFriendRequestNames)
                ) 
            }
        } catch (e: Exception) {
            Log.w("LovyChatViewModel", "loadIgnoredFriendRequestsData error: ${e.message}")
        }
    }

    private fun persistIgnoredFriendRequest(id: String, name: String = "", requestId: String = "") {
        if (id.isNotBlank()) {
            val cleanId = id.trim()
            ignoredFriendRequestIds.add(cleanId)
            val pbId = com.example.data.pocketbase.PocketBaseClient.toPbId(cleanId)
            if (pbId.isNotBlank()) ignoredFriendRequestIds.add(pbId)
            userAliasMap[cleanId]?.let { ignoredFriendRequestIds.add(it) }
        }
        if (requestId.isNotBlank()) {
            val cleanReqId = requestId.trim()
            ignoredFriendRequestIds.add(cleanReqId)
            val pbReqId = com.example.data.pocketbase.PocketBaseClient.toPbId(cleanReqId)
            if (pbReqId.isNotBlank()) ignoredFriendRequestIds.add(pbReqId)
            userAliasMap[cleanReqId]?.let { ignoredFriendRequestIds.add(it) }
        }
        if (name.isNotBlank()) {
            ignoredFriendRequestNames.add(name.trim())
        }
        try {
            prefs.edit()
                .putStringSet("ignored_friend_request_ids", HashSet(ignoredFriendRequestIds))
                .putStringSet("ignored_friend_request_names", HashSet(ignoredFriendRequestNames))
                .apply()
        } catch (_: Exception) {}
    }

    fun isIgnoredFriendRequest(partnerId: String?, partnerName: String? = null): Boolean {
        if (!partnerId.isNullOrBlank()) {
            val cleanId = partnerId.trim()
            if (ignoredFriendRequestIds.any { isSameUser(it, cleanId) || it.equals(cleanId, ignoreCase = true) }) {
                return true
            }
        }
        if (!partnerName.isNullOrBlank()) {
            val cleanName = partnerName.trim()
            if (ignoredFriendRequestNames.any { it.equals(cleanName, ignoreCase = true) }) {
                return true
            }
        }
        return false
    }

    fun checkForAppUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Periksa versi minimal dari remote database / SharedPrefs
                val remoteMinVersion = prefs.getInt("remote_min_version_code", 1)
                val remoteLatestVersion = prefs.getInt("remote_latest_version_code", 1)
                val remoteLatestName = prefs.getString("remote_latest_version_name", "1.0") ?: "1.0"
                val customTitle = prefs.getString("remote_update_title", "") ?: ""
                val customMsg = prefs.getString("remote_update_message", "") ?: ""

                var fetchedMinVersion = remoteMinVersion
                var fetchedLatestVersion = remoteLatestVersion
                var fetchedLatestName = remoteLatestName

                // Cek konfigurasi versi minimal dari PocketBase
                if (com.example.data.pocketbase.PocketBaseClient.isConfigured()) {
                    try {
                        val pbApi = com.example.data.pocketbase.PocketBaseClient.getApi()
                        if (pbApi != null) {
                            val res = pbApi.getUsers(perPage = 1, filter = "(username='__APP_CONFIG__')")
                            if (res.isSuccessful && res.body()?.items?.isNotEmpty() == true) {
                                val item = res.body()!!.items.first()
                                val bioStr = item.bio.orEmpty()
                                if (bioStr.contains("min_code")) {
                                    val json = org.json.JSONObject(bioStr)
                                    fetchedMinVersion = json.optInt("min_code", fetchedMinVersion)
                                    fetchedLatestVersion = json.optInt("latest_code", fetchedLatestVersion)
                                    fetchedLatestName = json.optString("latest_name", fetchedLatestName)
                                }
                            }
                        }
                    } catch (_: Exception) {}
                }

                if (SupabaseClient.isConfigured()) {
                    try {
                        val api = SupabaseClient.getApi()
                        val apiKey = SupabaseClient.getSupabaseAnonKey()
                        val auth = SupabaseClient.getAuthHeader()
                        if (api != null) {
                            val res = api.getAccountByUsername(apiKey, auth, "__APP_CONFIG__", limit = 1)
                            if (res.isSuccessful && res.body()?.isNotEmpty() == true) {
                                val item = res.body()!!.first()
                                val bioStr = item.bio.orEmpty()
                                if (bioStr.contains("min_code")) {
                                    val json = org.json.JSONObject(bioStr)
                                    fetchedMinVersion = json.optInt("min_code", fetchedMinVersion)
                                    fetchedLatestVersion = json.optInt("latest_code", fetchedLatestVersion)
                                    fetchedLatestName = json.optString("latest_name", fetchedLatestName)
                                }
                            }
                        }
                    } catch (_: Exception) {}
                }

                val currentCode = BuildConfig.VERSION_CODE
                withContext(Dispatchers.Main) {
                    if (fetchedMinVersion > currentCode) {
                        // FORCE UPDATE: Aplikasi harus diperbarui ke Google Play sekarang juga
                        _uiState.update {
                            it.copy(
                                appUpdateInfo = com.example.model.AppUpdateInfo(
                                    minVersionCode = fetchedMinVersion,
                                    latestVersionCode = fetchedLatestVersion,
                                    latestVersionName = fetchedLatestName,
                                    title = customTitle,
                                    message = customMsg,
                                    isForceUpdate = true
                                )
                            )
                        }
                    } else if (fetchedLatestVersion > currentCode) {
                        // Pembaruan opsional
                        val dismissedVersion = prefs.getInt("dismissed_optional_update_version", 0)
                        if (dismissedVersion < fetchedLatestVersion) {
                            _uiState.update {
                                it.copy(
                                    appUpdateInfo = com.example.model.AppUpdateInfo(
                                        minVersionCode = fetchedMinVersion,
                                        latestVersionCode = fetchedLatestVersion,
                                        latestVersionName = fetchedLatestName,
                                        title = customTitle,
                                        message = customMsg,
                                        isForceUpdate = false
                                    )
                                )
                            }
                        }
                    } else {
                        _uiState.update { it.copy(appUpdateInfo = null) }
                    }
                }
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "checkForAppUpdate error: ${e.message}")
            }
        }
    }

    fun dismissOptionalUpdate() {
        val currentInfo = _uiState.value.appUpdateInfo ?: return
        if (!currentInfo.isForceUpdate) {
            prefs.edit().putInt("dismissed_optional_update_version", currentInfo.latestVersionCode).apply()
            _uiState.update { it.copy(appUpdateInfo = null) }
        }
    }

    /**
     * Memungkinkan pengembang mengatur versi minimal aplikasi (Force Update) langsung dari aplikasi
     */
    fun setRemoteMinVersionCode(minVersion: Int, latestVersion: Int = minVersion, latestName: String = "1.0") {
        prefs.edit()
            .putInt("remote_min_version_code", minVersion)
            .putInt("remote_latest_version_code", latestVersion)
            .putString("remote_latest_version_name", latestName)
            .apply()

        viewModelScope.launch(Dispatchers.IO) {
            val payloadBio = org.json.JSONObject().apply {
                put("min_code", minVersion)
                put("latest_code", latestVersion)
                put("latest_name", latestName)
            }.toString()

            // 1. Sync ke PocketBase
            if (com.example.data.pocketbase.PocketBaseClient.isConfigured()) {
                try {
                    val pbApi = com.example.data.pocketbase.PocketBaseClient.getApi()
                    if (pbApi != null) {
                        val res = pbApi.getUsers(perPage = 1, filter = "(username='__APP_CONFIG__')")
                        val configId = res.body()?.items?.firstOrNull()?.id ?: "dpjh5vim92i9xy9"
                        pbApi.updateUser(configId, mapOf("bio" to payloadBio))
                    }
                } catch (e: Exception) {
                    Log.w("LovyChatViewModel", "Sync __APP_CONFIG__ PocketBase info: ${e.message}")
                }
            }

            // 2. Sync ke Supabase
            if (SupabaseClient.isConfigured()) {
                try {
                    val api = SupabaseClient.getApi()
                    val apiKey = SupabaseClient.getSupabaseAnonKey()
                    val auth = SupabaseClient.getAuthHeader()
                    if (api != null) {
                        api.upsertAccount(
                            apiKey = apiKey,
                            authHeader = auth,
                            account = com.example.data.supabase.SupabaseAccountDto(
                                id = "__APP_CONFIG__",
                                username = "__APP_CONFIG__",
                                displayName = "System App Config",
                                bio = payloadBio
                            )
                        )
                    }
                } catch (_: Exception) {}
            }
            checkForAppUpdate()
        }
    }

    companion object {
        // Cache data selama 3 menit untuk memangkas 80%+ query baca ke cloud
        private const val CACHE_DURATION_MS = 3 * 60 * 1000L
        // Pembaruan GPS di-throttle: hanya jika berpindah > 500m atau jeda > 10 menit
        private const val GPS_THROTTLE_MIN_DISTANCE_METERS = 500.0
        private const val GPS_THROTTLE_MIN_INTERVAL_MS = 10 * 60 * 1000L
        // User activity heartbeat di-throttle ke database cloud jeda 1 menit
        private const val USER_ACTIVITY_THROTTLE_MS = 60 * 1000L
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
        loadSavedMomentComments()
        loadSavedBottles()
        loadPrivacySettings()
        loadDeletedTrackingData()
        loadIgnoredFriendRequestsData()
        refreshSupabaseState()
        refreshR2State()
        detectAndApplyGeoLanguage()
        checkForAppUpdate()
        checkInitialGpsLocation()
        observeUserProfile()
        observeChatFriends()
        updateUserActivity(force = true)
        startHeartbeatLoop()
        initFirebaseMessaging()
        initDefaultNotifications()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                localChatRepo.deleteAutomatedGreetings()
            } catch (_: Exception) {}
        }
        clearDummyFriends()
        // Selaraskan Google Client ID dari konfigurasi PocketBase secara dinamis
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pbClientId = com.example.data.pocketbase.PocketBaseRepository().extractGoogleClientIdFromPocketBase()
                if (!pbClientId.isNullOrBlank()) {
                    GoogleAuthHelper.dynamicClientId = pbClientId
                    Log.d("LovyChatViewModel", "Google Client ID diselaraskan dari PocketBase: $pbClientId")
                }
            } catch (e: Exception) {
                Log.d("LovyChatViewModel", "Sinkronisasi Google Client ID dari PocketBase: ${e.message}")
            }
        }
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
                val sessionLovyId = authRepo.getOrGenerateLovyId(sessionEmail ?: savedSession.username, savedSession.lovyId)
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (SupabaseClient.isConfigured()) {
                    supabaseRepo.purgeInactiveAccountsAndDeletedMessages()
                }
            } catch (_: Exception) {}
        }
        if (!_uiState.value.isGuest && _uiState.value.myLovyId.isNotBlank()) {
            startIncomingChatPeriodicSync()
            startRealtimeChatSubscription()
        }
    }

    fun isSelfUser(userId: String?, userName: String?): Boolean {
        val state = _uiState.value
        val cleanId = userId?.trim().orEmpty()
        val cleanName = userName?.trim().orEmpty()
        val myLovyId = state.myLovyId.trim()
        val myName = state.myName.trim()
        val myDisplayName = state.userProfile.displayName.trim()
        val profileLovyId = state.userProfile.lovyId.trim()
        val savedSession = authRepo.getSavedSession()
        val sessionUsername = savedSession?.username?.trim().orEmpty()
        val sessionDisplayName = savedSession?.displayName?.trim().orEmpty()
        val sessionLovyId = savedSession?.lovyId?.trim().orEmpty()
        val myPbId = if (myLovyId.isNotBlank()) com.example.data.pocketbase.PocketBaseClient.toPbId(myLovyId) else ""

        if (cleanId.isNotBlank()) {
            if (cleanId.equals("me", ignoreCase = true) || cleanId.equals("current_user", ignoreCase = true)) return true
            if (myLovyId.isNotBlank() && cleanId.equals(myLovyId, ignoreCase = true)) return true
            if (myPbId.isNotBlank() && (cleanId.equals(myPbId, ignoreCase = true) || com.example.data.pocketbase.PocketBaseClient.toPbId(cleanId).equals(myPbId, ignoreCase = true))) return true
            if (profileLovyId.isNotBlank() && cleanId.equals(profileLovyId, ignoreCase = true)) return true
            if (sessionLovyId.isNotBlank() && cleanId.equals(sessionLovyId, ignoreCase = true)) return true
            if (myName.isNotBlank() && cleanId.equals(myName, ignoreCase = true)) return true
            if (myDisplayName.isNotBlank() && cleanId.equals(myDisplayName, ignoreCase = true)) return true
            if (sessionUsername.isNotBlank() && cleanId.equals(sessionUsername, ignoreCase = true)) return true
        }

        if (cleanName.isNotBlank()) {
            if (cleanName.equals("me", ignoreCase = true) || cleanName.equals("saya", ignoreCase = true)) return true
            if (myName.isNotBlank() && cleanName.equals(myName, ignoreCase = true)) return true
            if (myDisplayName.isNotBlank() && cleanName.equals(myDisplayName, ignoreCase = true)) return true
            if (sessionUsername.isNotBlank() && cleanName.equals(sessionUsername, ignoreCase = true)) return true
            if (sessionDisplayName.isNotBlank() && cleanName.equals(sessionDisplayName, ignoreCase = true)) return true
            if (myLovyId.isNotBlank() && cleanName.equals(myLovyId, ignoreCase = true)) return true
            if (profileLovyId.isNotBlank() && cleanName.equals(profileLovyId, ignoreCase = true)) return true
        }

        return false
    }

    private val userAliasMap = java.util.Collections.synchronizedMap(mutableMapOf<String, String>())

    fun recordUserAlias(id1: String?, id2: String?) {
        if (id1.isNullOrBlank() || id2.isNullOrBlank()) return
        val c1 = id1.trim()
        val c2 = id2.trim()
        if (c1.equals(c2, ignoreCase = true)) return
        userAliasMap[c1] = c2
        userAliasMap[c2] = c1
        val pb1 = com.example.data.pocketbase.PocketBaseClient.toPbId(c1)
        val pb2 = com.example.data.pocketbase.PocketBaseClient.toPbId(c2)
        if (pb1.isNotBlank()) userAliasMap[pb1] = c2
        if (pb2.isNotBlank()) userAliasMap[pb2] = c1
    }

    /**
     * Memeriksa apakah dua user ID merujuk ke akun pengguna yang sama,
     * baik format ID berupa lovy_XXXXXX, PocketBase 15-karakter hash (toPbId), username, maupun alias terhubung.
     */
    fun isSameUser(id1: String?, id2: String?): Boolean {
        if (id1.isNullOrBlank() || id2.isNullOrBlank()) return false
        val clean1 = id1.trim()
        val clean2 = id2.trim()
        if (clean1.equals(clean2, ignoreCase = true)) return true
        val pb1 = com.example.data.pocketbase.PocketBaseClient.toPbId(clean1)
        val pb2 = com.example.data.pocketbase.PocketBaseClient.toPbId(clean2)
        if (pb1.isNotBlank() && pb1.equals(pb2, ignoreCase = true)) return true

        if (userAliasMap[clean1]?.equals(clean2, ignoreCase = true) == true ||
            userAliasMap[clean2]?.equals(clean1, ignoreCase = true) == true ||
            (pb1.isNotBlank() && userAliasMap[pb1]?.equals(clean2, ignoreCase = true) == true) ||
            (pb2.isNotBlank() && userAliasMap[pb2]?.equals(clean1, ignoreCase = true) == true)) {
            return true
        }

        return false
    }

    /**
     * Memeriksa apakah dua entitas kontak merujuk ke orang yang sama,
     * baik melalui kesamaan ID, alias terdaftar, ataupun kesamaan nama tampilan yang unik (bukan nama generik).
     */
    fun areUsersSamePerson(id1: String?, name1: String?, id2: String?, name2: String?): Boolean {
        if (isSameUser(id1, id2)) return true
        val n1 = name1?.trim().orEmpty()
        val n2 = name2?.trim().orEmpty()
        if (n1.isNotBlank() && n2.isNotBlank() && 
            !n1.startsWith("Pengguna (") && !n2.startsWith("Pengguna (") && 
            !isDummyFriend(id1 ?: "", n1) && !isDummyFriend(id2 ?: "", n2)) {
            if (n1.equals(n2, ignoreCase = true)) {
                recordUserAlias(id1, id2)
                return true
            }
        }
        return false
    }

    /**
     * Memeriksa apakah dua ID percakapan merujuk ke ruang obrolan yang sama
     * (memperhitungkan variasi kanonikal, format underscore tunggal/ganda, dan hash).
     */
    fun isSameConversation(convId1: String?, convId2: String?): Boolean {
        if (convId1.isNullOrBlank() || convId2.isNullOrBlank()) return false
        val c1 = convId1.trim()
        val c2 = convId2.trim()
        if (c1.equals(c2, ignoreCase = true)) return true
        val norm1 = normalizeConversationId(c1)
        val norm2 = normalizeConversationId(c2)
        return norm1.equals(norm2, ignoreCase = true)
    }

    /**
     * Mencari pengguna di daftar teman atau teman sekitar berdasarkan ID lovy (lovy_XXXXXX)
     * ataupun ID PocketBase 15-karakter.
     */
    fun findUserById(queryId: String?): User? {
        if (queryId.isNullOrBlank()) return null
        return _uiState.value.chattedFriends.find { isSameUser(it.id, queryId) }
            ?: _uiState.value.nearbyUsers.find { isSameUser(it.id, queryId) }
    }

    fun isDummyFriend(userId: String, userName: String): Boolean {
        val cleanName = userName.trim().lowercase()
        if (cleanName.isBlank()) return true // Tolak user tak bernama
        val dummyNames = setOf(
            "siti rahma", "rian pratama", "nadia putri", "dimas anggara", 
            "alya zahra", "pengguna lovy", "rania putri", "clara monica",
            "dimas danendra", "clarissa aurelia", "salma salsabil",
            "tanpa nama", "user tak bernama", "pengguna", "unknown user", "anonymous"
        )
        val isMockId = userId.matches(Regex("^u[0-9]+$")) || userId.startsWith("test_") || userId.contains("dummy", ignoreCase = true)
        return isMockId || dummyNames.contains(cleanName)
    }

    private fun observeChatFriends() {
        viewModelScope.launch {
            try {
                chatFriendDao.getAllFriendsFlow().collect { friendEntities ->
                    val friends = friendEntities.map { it.toUser() }
                    val finalFriends = friends
                        .filterNot { it.name.trim().isBlank() }
                        .filterNot { isDummyFriend(it.id, it.name) }
                        .filterNot { isSelfUser(it.id, it.name) }

                    // Deduplikasi cerdas: Satukan akun teman yang memiliki ID atau nama yang sama
                    val dedupedFriends = mutableListOf<User>()
                    for (f in finalFriends) {
                        val existingIdx = dedupedFriends.indexOfFirst { 
                            areUsersSamePerson(it.id, it.name, f.id, f.name)
                        }
                        if (existingIdx >= 0) {
                            val old = dedupedFriends[existingIdx]
                            recordUserAlias(old.id, f.id)
                            // Prioritaskan profil yang memiliki Google info, foto aktif, atau ID lovy_ permanen
                            val preferred = if (!f.avatarUrl.isNullOrBlank() && (old.avatarUrl.isNullOrBlank() || f.bio.contains("Google"))) {
                                f
                            } else if (!old.avatarUrl.isNullOrBlank() && !old.bio.contains("Google") && f.bio.contains("Google")) {
                                f
                            } else if (!f.avatarUrl.isNullOrBlank()) {
                                f
                            } else if (old.name.startsWith("Pengguna (") && !f.name.startsWith("Pengguna (")) {
                                f
                            } else {
                                old.copy(isOnline = old.isOnline || f.isOnline)
                            }
                            dedupedFriends[existingIdx] = preferred

                            // Hapus duplikat dari database Room secara bersih di latar belakang
                            val redundantId = if (preferred.id == f.id) old.id else f.id
                            viewModelScope.launch(Dispatchers.IO) {
                                try {
                                    chatFriendDao.deleteFriendById(redundantId)
                                } catch (_: Throwable) {}
                            }
                        } else {
                            dedupedFriends.add(f)
                        }
                    }

                    _uiState.update { it.copy(chattedFriends = dedupedFriends) }
                    refreshNewFriendRequests(dedupedFriends)
                }
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal memuat teman mengobrol", e)
            }
        }
    }

    fun refreshNewFriendRequests(currentFriends: List<User> = _uiState.value.chattedFriends) {
        val state = _uiState.value
        val friendIds = currentFriends.map { it.id }.toSet()

        val requestsMap = state.newFriendRequests
            .filterNot { req ->
                currentFriends.any { isSameUser(it.id, req.user.id) || areUsersSamePerson(it.id, it.name, req.user.id, req.user.name) } || 
                isIgnoredFriendRequest(req.user.id, req.user.name) || 
                isIgnoredFriendRequest(req.id, req.user.name) || 
                req.user.name.trim().isBlank() || 
                isDummyFriend(req.user.id, req.user.name) ||
                isSelfUser(req.user.id, req.user.name)
            }
            .associateBy { it.user.id }
            .toMutableMap()

        // Pindai pesan masuk di messagesMap untuk menemukan pesan teman baru yang belum ada di daftar teman
        for ((convId, msgs) in state.messagesMap) {
            val partnerId = extractPartnerIdFromConvId(convId, state.myLovyId)
            val candidateUser = state.nearbyUsers.find { isSameUser(it.id, partnerId) }
            val partnerName = candidateUser?.name.orEmpty()
            val isAlreadyFriend = currentFriends.any { 
                isSameUser(it.id, partnerId) || 
                areUsersSamePerson(it.id, it.name, partnerId, partnerName) 
            }
            val isIgnored = isIgnoredFriendRequest(partnerId, partnerName) ||
                (candidateUser != null && isIgnoredFriendRequest(candidateUser.id, candidateUser.name))
            if (partnerId.isBlank() || isAlreadyFriend || isIgnored || isDummyFriend(partnerId, "") || isSelfUser(partnerId, "")) continue

            val validMsgs = msgs.filterNot { 
                it.text.startsWith("__TYPING_") || 
                it.text == "__DELETED_FOR_EVERYONE__" || 
                deletedMessageIds.contains(it.id) ||
                it.deletedForReceiver
            }
            val lastPartnerMsg = validMsgs.filter { !it.isFromMe }.maxByOrNull { it.timestamp }
            if (lastPartnerMsg != null) {
                if (!requestsMap.keys.any { isSameUser(it, partnerId) }) {
                    val resolvedUser = candidateUser
                        ?: User(
                            id = partnerId,
                            name = "Pengguna (${partnerId.takeLast(4)})",
                            gender = Gender.FEMALE,
                            age = 22,
                            distanceMeters = 300,
                            bio = "Mengirimi Anda pesan obrolan di Lovy Chat",
                            avatarColorHex = 0xFF00A86B,
                            city = "Indonesia",
                            isOnline = true
                        )
                    requestsMap[partnerId] = com.example.model.NewFriendRequest(
                        id = partnerId,
                        user = resolvedUser,
                        greetingMessage = lastPartnerMsg.text,
                        timestamp = lastPartnerMsg.timestamp
                    )
                }
            }
        }

        // Pindai juga dari conversations jika ada pengguna yang belum ada di daftar teman
        for (conv in state.conversations) {
            val partnerId = conv.partnerId
            val pName = conv.partnerName.trim()
            val candidateUser = state.nearbyUsers.find { isSameUser(it.id, partnerId) || it.name.equals(pName, ignoreCase = true) }
            val isAlreadyFriend = currentFriends.any { 
                isSameUser(it.id, partnerId) || 
                areUsersSamePerson(it.id, it.name, partnerId, pName) 
            }
            val isIgnored = isIgnoredFriendRequest(partnerId, pName) ||
                (candidateUser != null && isIgnoredFriendRequest(candidateUser.id, candidateUser.name))
            if (partnerId.isBlank() || pName.isBlank() || isDummyFriend(partnerId, pName) || isSelfUser(partnerId, pName) || isAlreadyFriend || isIgnored) continue

            // Pengguna lain yang mengirimi pesan obrolan tapi belum disetujui / belum ada di Kontak Saya
            if (!conv.lastMessageIsFromMe || conv.unreadCount > 0) {
                if (!requestsMap.keys.any { isSameUser(it, partnerId) }) {
                    val candidateUser = state.nearbyUsers.find { isSameUser(it.id, partnerId) }
                        ?: User(
                            id = partnerId,
                            name = conv.partnerName,
                            gender = conv.partnerGender,
                            age = conv.partnerAge,
                            distanceMeters = conv.partnerDistanceMeters,
                            bio = "Mengirimi Anda pesan obrolan di Lovy Chat",
                            avatarColorHex = conv.partnerAvatarHex,
                            avatarUrl = conv.partnerAvatarUrl,
                            city = conv.partnerCity ?: "Indonesia",
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

        // Pastikan obrolan HANYA berisi teman yang sudah disetujui / resmi ada di daftar teman,
        // ATAU obrolan yang kita inisiasi sendiri. Teman baru yang belum disetujui tidak dimasukkan ke obrolan.
        val filteredConversations = state.conversations.filter { conv ->
            val pid = conv.partnerId
            val isFriend = currentFriends.any { isSameUser(it.id, pid) }
            isFriend || (conv.lastMessageIsFromMe && !requestsMap.keys.any { isSameUser(it, pid) })
        }

        _uiState.update { 
            it.copy(
                newFriendRequests = sortedList,
                conversations = filteredConversations
            ) 
        }

        // Tambahkan notifikasi aktivitas ringan untuk permintaan teman baru
        val currentLang = _uiState.value.language
        for (req in sortedList.take(3)) {
            addActivityNotification(
                com.example.model.ActivityNotification(
                    id = "friend_req_${req.id}",
                    title = com.example.util.AppStrings.notifFriendRequestTitle(currentLang),
                    message = com.example.util.AppStrings.notifFriendRequestDesc(currentLang, req.user.name),
                    timestamp = req.timestamp,
                    isRead = false,
                    category = com.example.model.NotificationCategory.FRIEND,
                    senderName = req.user.name,
                    translationKey = "friend_request"
                )
            )
        }
    }

    fun addActivityNotification(notification: com.example.model.ActivityNotification) {
        _uiState.update { state ->
            val exists = state.activityNotifications.any { it.id == notification.id }
            if (exists) state
            else state.copy(activityNotifications = listOf(notification) + state.activityNotifications)
        }
    }

    fun markAllNotificationsAsRead() {
        _uiState.update { state ->
            state.copy(activityNotifications = state.activityNotifications.map { it.copy(isRead = true) })
        }
    }

    fun markNotificationAsRead(id: String) {
        _uiState.update { state ->
            state.copy(activityNotifications = state.activityNotifications.map {
                if (it.id == id) it.copy(isRead = true) else it
            })
        }
    }

    fun clearAllNotifications() {
        _uiState.update { it.copy(activityNotifications = emptyList()) }
    }

    private fun initDefaultNotifications() {
        val currentLang = _uiState.value.language
        val welcomeNotif = com.example.model.ActivityNotification(
            id = "sys_welcome",
            title = com.example.util.AppStrings.notifWelcomeTitle(currentLang),
            message = com.example.util.AppStrings.notifWelcomeDesc(currentLang),
            timestamp = System.currentTimeMillis() - 120000L,
            isRead = false,
            category = com.example.model.NotificationCategory.SYSTEM,
            translationKey = "welcome"
        )
        val radarNotif = com.example.model.ActivityNotification(
            id = "sys_radar_active",
            title = com.example.util.AppStrings.notifRadarActiveTitle(currentLang),
            message = com.example.util.AppStrings.notifRadarActiveDesc(currentLang),
            timestamp = System.currentTimeMillis() - 60000L,
            isRead = false,
            category = com.example.model.NotificationCategory.NEARBY,
            translationKey = "radar_active"
        )
        _uiState.update {
            if (it.activityNotifications.isEmpty()) {
                it.copy(activityNotifications = listOf(welcomeNotif, radarNotif))
            } else it
        }
    }

    fun acceptNewFriend(user: User) {
        if (isSelfUser(user.id, user.name)) return
        recordFeatureClick()
        saveChatFriend(user)

        val myId = _uiState.value.myLovyId
        val convId = getCanonicalConversationId(myId, user.id)
        val msgs = _uiState.value.messagesMap[convId] ?: emptyList()
        val validMsgs = msgs.filterNot { 
            it.text.startsWith("__TYPING_") || 
            it.text == "__DELETED_FOR_EVERYONE__" || 
            deletedMessageIds.contains(it.id) ||
            it.deletedForReceiver
        }
        val lastMsg = validMsgs.lastOrNull()
        val lastText = lastMsg?.text ?: "Pertemanan disetujui 👋"
        val lastTimestamp = lastMsg?.timestamp ?: System.currentTimeMillis()
        val unreadCount = validMsgs.count { !it.isFromMe && !it.isRead }

        val newConv = ChatConversation(
            id = convId,
            partnerId = user.id,
            partnerName = user.name,
            partnerAvatarHex = user.avatarColorHex,
            partnerGender = user.gender,
            lastMessage = lastText,
            lastTimestamp = lastTimestamp,
            unreadCount = unreadCount,
            isOnline = user.isOnline,
            partnerAvatarUrl = user.avatarUrl,
            partnerAge = user.age,
            partnerDistanceMeters = user.distanceMeters,
            partnerCity = user.city,
            lastMessageIsFromMe = lastMsg?.isFromMe ?: false,
            lastMessageIsRead = lastMsg?.isRead ?: false
        )

        _uiState.update { state ->
            val updatedFriends = if (state.chattedFriends.any { isSameUser(it.id, user.id) }) {
                state.chattedFriends.map { if (isSameUser(it.id, user.id)) user else it }
            } else {
                state.chattedFriends + user
            }
            val existingIndex = state.conversations.indexOfFirst { 
                isSameConversation(it.id, convId) || isSameUser(it.partnerId, user.id) 
            }
            val updatedConvs = if (existingIndex >= 0) {
                state.conversations.mapIndexed { idx, c -> if (idx == existingIndex) newConv else c }
            } else {
                listOf(newConv) + state.conversations
            }
            state.copy(
                chattedFriends = updatedFriends.filterNot { isSelfUser(it.id, it.name) },
                newFriendRequests = state.newFriendRequests.filterNot { isSameUser(it.user.id, user.id) || isSelfUser(it.user.id, it.user.name) },
                conversations = updatedConvs
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

    fun ignoreNewFriend(userId: String, userName: String = "", requestId: String = "") {
        recordFeatureClick()

        val state = _uiState.value
        val targetReq = state.newFriendRequests.find { 
            it.user.id == userId || it.id == requestId || isSameUser(it.user.id, userId) || 
            (requestId.isNotBlank() && isSameUser(it.id, requestId)) ||
            (userName.isNotBlank() && it.user.name.equals(userName, ignoreCase = true))
        }
        val resolvedName = userName.ifBlank { targetReq?.user?.name.orEmpty() }
        val resolvedUserId = userId.ifBlank { targetReq?.user?.id.orEmpty() }
        val resolvedReqId = requestId.ifBlank { targetReq?.id.orEmpty() }

        persistIgnoredFriendRequest(resolvedUserId, resolvedName, resolvedReqId)

        // Hapus conversation atau tandai conversation sebagai deleted agar tidak muncul lagi di obrolan
        val myId = state.myLovyId
        if (resolvedUserId.isNotBlank()) {
            val convId1 = getCanonicalConversationId(myId, resolvedUserId)
            persistDeletedConversation(convId1)
        }
        if (resolvedReqId.isNotBlank() && resolvedReqId != resolvedUserId) {
            val convId2 = getCanonicalConversationId(myId, resolvedReqId)
            persistDeletedConversation(convId2)
        }

        _uiState.update { current ->
            val updatedReqs = current.newFriendRequests.filterNot { req ->
                isIgnoredFriendRequest(req.user.id, req.user.name) ||
                isIgnoredFriendRequest(req.id, req.user.name) ||
                req.user.id == resolvedUserId ||
                req.id == resolvedReqId ||
                (resolvedName.isNotBlank() && req.user.name.equals(resolvedName, ignoreCase = true))
            }
            val updatedConvs = current.conversations.filterNot { conv ->
                isIgnoredFriendRequest(conv.partnerId, conv.partnerName) ||
                conv.partnerId == resolvedUserId ||
                conv.partnerId == resolvedReqId ||
                (resolvedName.isNotBlank() && conv.partnerName.equals(resolvedName, ignoreCase = true))
            }
            current.copy(
                ignoredNewFriendIds = HashSet(ignoredFriendRequestIds),
                ignoredNewFriendNames = HashSet(ignoredFriendRequestNames),
                newFriendRequests = updatedReqs,
                conversations = updatedConvs
            )
        }
    }

    fun saveChatFriend(user: User) {
        if (isSelfUser(user.id, user.name)) {
            Log.d("LovyChatViewModel", "Abaikan menyimpan akun sendiri ke kontak teman: ${user.name} (${user.id})")
            return
        }
        if (isDummyFriend(user.id, user.name)) {
            // Abaikan penyimpanan user dummy / test
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Pertahankan status isFavorite jika teman sudah ditandai favorit sebelumnya
                val currentFriend = _uiState.value.chattedFriends.find { isSameUser(it.id, user.id) }
                val isFav = user.isFavorite || (currentFriend?.isFavorite == true)
                if (currentFriend != null && currentFriend.id != user.id) {
                    try { chatFriendDao.deleteFriendById(currentFriend.id) } catch (_: Exception) {}
                }
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
        // Kirim status mengetik via Centrifugo WebSocket instan
        if (!isTyping || now - lastTypingSentTime > 2500L) {
            lastTypingSentTime = now
            com.example.data.centrifugo.CentrifugoRealtimeManager.publishTyping(
                conversationId = conversationId,
                senderId = _uiState.value.myLovyId,
                receiverId = partnerId,
                isTyping = isTyping
            )
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
                val state = _uiState.value
                val session = authRepo.getSavedSession()
                val myId = state.myLovyId.ifBlank { session?.lovyId.orEmpty() }
                val myName = state.myName.ifBlank { session?.displayName.orEmpty() }
                val myDisplay = state.userProfile.displayName
                val username = session?.username.orEmpty()
                chatFriendDao.deleteSelfFriend(
                    myId = myId,
                    myName = myName,
                    myDisplayName = myDisplay,
                    username = username
                )
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal membersihkan kontak dummy/self", e)
            }
        }
        _uiState.update { state ->
            val updated = state.chattedFriends
                .filterNot { isDummyFriend(it.id, it.name) }
                .filterNot { isSelfUser(it.id, it.name) }
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

    fun updateUserActivity(force: Boolean = false) {
        if (_uiState.value.isGuest) return // Mode Tamu tidak mengirim heartbeat ke Supabase
        val now = System.currentTimeMillis()
        if (!force && now - lastUserActivityTimestamp < USER_ACTIVITY_THROTTLE_MS) {
            return
        }
        lastUserActivityTimestamp = now
        val myId = _uiState.value.myLovyId
        if (myId.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                supabaseRepo.updateUserPresence(myId, isOnline = true)
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal update presence online", e)
            }
        }
    }

    private fun startHeartbeatLoop() {
        heartbeatJob?.cancel()
        heartbeatJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val state = _uiState.value
                val myId = state.myLovyId.trim()
                if (!state.isGuest && myId.isNotBlank() && state.isLoggedIn) {
                    try {
                        supabaseRepo.updateUserPresence(myId, isOnline = true)
                    } catch (e: Exception) {
                        Log.w("LovyChatViewModel", "Detak online presence gagal: ${e.message}")
                    }
                }
                // Kirim heartbeat setiap 60 detik (1 menit) saat aplikasi sedang digunakan
                kotlinx.coroutines.delay(60_000L)
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

    fun getLocalSavedMoments(): List<MomentItem> {
        try {
            val rawMomentsJson = prefs.getString("my_local_moments_json", null)
            if (!rawMomentsJson.isNullOrBlank()) {
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
                return list
            }
        } catch (_: Throwable) {
        }
        return emptyList()
    }

    private fun loadMyMoments() {
        try {
            val myId = _uiState.value.myLovyId.trim()
            if (myId.isBlank()) {
                _uiState.update { it.copy(myMomentIds = emptySet()) }
                return
            }
            val userMomentIdsKey = "my_moment_ids_${myId}"
            val savedIds = prefs.getStringSet(userMomentIdsKey, emptySet()) ?: emptySet()
            val locallySavedMoments = getLocalSavedMoments()

            // Filter ketat: HANYA momen yang benar-benar dibuat oleh user aktif ini (berdasarkan authorId)
            val myLocalMoments = locallySavedMoments.filter { m ->
                m.authorId.trim().equals(myId, ignoreCase = true)
            }
            val combinedIds = (savedIds + myLocalMoments.map { it.id }).toSet()

            _uiState.update { 
                it.copy(
                    myMomentIds = combinedIds,
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

            val myId = _uiState.value.myLovyId.trim()
            val resolvedAuthorId = moment.authorId.takeIf { it.isNotBlank() && !it.equals("me", ignoreCase = true) }
                ?: myId.takeIf { it.isNotBlank() }
                ?: "me"

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
                put("authorId", resolvedAuthorId)
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

    private fun loadSavedMomentComments() {
        try {
            val jsonStr = prefs.getString("saved_moment_comments_json", null)
            if (!jsonStr.isNullOrBlank()) {
                val rootObj = org.json.JSONObject(jsonStr)
                val map = mutableMapOf<String, List<com.example.model.MomentComment>>()
                val keys = rootObj.keys()
                while (keys.hasNext()) {
                    val momentId = keys.next()
                    val arr = rootObj.getJSONArray(momentId)
                    val commentList = mutableListOf<com.example.model.MomentComment>()
                    for (i in 0 until arr.length()) {
                        val cObj = arr.getJSONObject(i)
                        commentList.add(
                            com.example.model.MomentComment(
                                id = cObj.optString("id", java.util.UUID.randomUUID().toString()),
                                momentId = cObj.optString("momentId", momentId),
                                authorId = cObj.optString("authorId", "me"),
                                authorName = cObj.optString("authorName", "Teman"),
                                authorAvatarHex = cObj.optLong("authorAvatarHex", 0xFF00A86B),
                                authorAvatarUrl = if (cObj.has("authorAvatarUrl")) cObj.optString("authorAvatarUrl").takeIf { it.isNotBlank() } else null,
                                text = cObj.optString("text", ""),
                                timestamp = cObj.optLong("timestamp", System.currentTimeMillis()),
                                timeAgo = cObj.optString("timeAgo", "Baru saja")
                            )
                        )
                    }
                    map[momentId] = commentList
                }
                _uiState.update { state ->
                    val updatedMoments = state.moments.map { m ->
                        val count = map[m.id]?.size ?: m.commentsCount
                        if (count > m.commentsCount) m.copy(commentsCount = count) else m
                    }
                    state.copy(
                        momentComments = map,
                        moments = updatedMoments
                    )
                }
            }
        } catch (_: Throwable) {
        }
    }

    private fun persistMomentComments(map: Map<String, List<com.example.model.MomentComment>>) {
        try {
            val rootObj = org.json.JSONObject()
            map.forEach { (momentId, comments) ->
                val arr = org.json.JSONArray()
                comments.forEach { c ->
                    val cObj = org.json.JSONObject().apply {
                        put("id", c.id)
                        put("momentId", c.momentId)
                        put("authorId", c.authorId)
                        put("authorName", c.authorName)
                        put("authorAvatarHex", c.authorAvatarHex)
                        if (c.authorAvatarUrl != null) put("authorAvatarUrl", c.authorAvatarUrl)
                        put("text", c.text)
                        put("timestamp", c.timestamp)
                        put("timeAgo", c.timeAgo)
                    }
                    arr.put(cObj)
                }
                rootObj.put(momentId, arr)
            }
            prefs.edit().putString("saved_moment_comments_json", rootObj.toString()).apply()
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
                                authRepo.getOrGenerateLovyId(savedSession.email ?: savedSession.username, savedSession.lovyId)
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
                            authRepo.getOrGenerateLovyId(savedSession.email ?: savedSession.username, savedSession.lovyId)
                        } else {
                            authRepo.getOrGenerateLovyId(initialEmail ?: initialName, _uiState.value.myLovyId)
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
        if (!SupabaseClient.isConfigured()) return
        val profile = _uiState.value.userProfile
        val lovyId = _uiState.value.myLovyId
        if (lovyId.isBlank()) return
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
        _uiState.update { state ->
            val updatedNotifs = state.activityNotifications.map { notif ->
                notif.copy(
                    title = notif.getDisplayTitle(detected),
                    message = notif.getDisplayMessage(detected)
                )
            }
            state.copy(
                isLocalLanguageMode = true,
                detectedLocalLanguage = detected,
                language = detected,
                detectedGeoArea = areaName,
                activityNotifications = updatedNotifs
            )
        }
    }

    fun setLanguage(language: com.example.util.AppLanguage) {
        recordFeatureClick()
        val app = try { getApplication<Application>() } catch (_: Throwable) { null }
        val ctx = try { app?.applicationContext } catch (_: Throwable) { null } ?: app

        val targetLang = if (language == com.example.util.AppLanguage.LOCAL) {
            com.example.util.GeoLanguageDetector.detectLocalLanguage(ctx)
        } else {
            language
        }
        val isLocal = if (language == com.example.util.AppLanguage.LOCAL) true else (language != com.example.util.AppLanguage.ENGLISH)

        if (ctx != null) {
            com.example.util.LovyNotificationHelper.createNotificationChannel(ctx, targetLang)
        }

        _uiState.update { state ->
            val updatedNotifs = state.activityNotifications.map { notif ->
                notif.copy(
                    title = notif.getDisplayTitle(targetLang),
                    message = notif.getDisplayMessage(targetLang)
                )
            }
            state.copy(
                language = targetLang,
                detectedLocalLanguage = if (language == com.example.util.AppLanguage.LOCAL) targetLang else state.detectedLocalLanguage,
                isLocalLanguageMode = isLocal,
                activityNotifications = updatedNotifs
            )
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
        val isPocketBase = SupabaseClient.isPocketBase()
        _uiState.update {
            it.copy(
                isSupabaseConnected = configured,
                supabaseUrl = url,
                supabaseAnonKey = key,
                connectionStatusMessage = if (configured) {
                    if (isPocketBase) "PocketBase aktif: $url" else "Supabase terkonfigurasi: $url"
                } else "Belum terkonfigurasi (menggunakan penyimpanan lokal)"
            )
        }
    }

    fun syncFromSupabase(forceRefresh: Boolean = false) {
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
                    val processed = processNearbyCandidates(remoteUsers)
                    _uiState.update { it.copy(nearbyUsers = processed) }
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
                    val locallySaved = getLocalSavedMoments()
                    val myId = _uiState.value.myLovyId
                    val myName = _uiState.value.myName
                    val cleanMyId = myId.trim()

                    // Temukan HANYA momen milik pengguna saat ini (strictly berdasarkan authorId)
                    val myRemoteMoments = remoteMoments.filter { m ->
                        cleanMyId.isNotBlank() && m.authorId.trim().equals(cleanMyId, ignoreCase = true)
                    }
                    val myRemoteIds = myRemoteMoments.map { it.id }.toSet()
                    val myLocalOnlyIds = locallySaved.filter { m ->
                        cleanMyId.isNotBlank() && m.authorId.trim().equals(cleanMyId, ignoreCase = true)
                    }.map { it.id }
                    val allMyIds = (_uiState.value.myMomentIds.filter { id ->
                        locallySaved.any { it.id == id && cleanMyId.isNotBlank() && it.authorId.trim().equals(cleanMyId, ignoreCase = true) }
                    } + myRemoteIds + myLocalOnlyIds).toSet()

                    // Simpan cadangan lokal momen milik user sendiri
                    myRemoteMoments.forEach { saveMyLocalMoment(it) }
                    try {
                        if (cleanMyId.isNotBlank()) {
                            val userMomentIdsKey = "my_moment_ids_${cleanMyId}"
                            prefs.edit().putStringSet(userMomentIdsKey, allMyIds).apply()
                        }
                    } catch (_: Throwable) {}

                    val myLocal = (_uiState.value.moments.filter { it.id in allMyIds } + locallySaved).distinctBy { it.id }
                    val merged = (myLocal + remoteMoments).distinctBy { it.id }
                    val enriched = enrichMomentsWithAvatars(merged)
                    _uiState.update { 
                        it.copy(
                            moments = enriched,
                            myMomentIds = allMyIds
                        ) 
                    }
                    lastMomentsSyncTime = System.currentTimeMillis()

                    // Auto-sync: Unggah otomatis momen lokal ke server backend jika belum tersimpan di cloud
                    val remoteIds = remoteMoments.map { it.id }.toSet()
                    val unsyncedMoments = myLocal.filterNot { it.id in remoteIds }
                    if (unsyncedMoments.isNotEmpty()) {
                        viewModelScope.launch(Dispatchers.IO) {
                            val currentLovyId = _uiState.value.myLovyId
                            unsyncedMoments.forEach { unposted ->
                                val author = unposted.authorId.takeIf { it.isNotBlank() && it != "me" }
                                    ?: currentLovyId.ifBlank { authRepo.getSavedSession()?.lovyId ?: authRepo.getOrGenerateLovyId(_uiState.value.myName) }
                                val ok = supabaseRepo.sendMoment(unposted, author)
                                if (ok) {
                                    Log.d("LovyChatViewModel", "Berhasil auto-sync momen lokal ke cloud: ${unposted.id}")
                                }
                            }
                        }
                    }
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
                if (SupabaseClient.isConfigured()) {
                    val remoteMoments = supabaseRepo.fetchMoments()
                    if (remoteMoments != null) {
                        val locallySaved = getLocalSavedMoments()
                        val myId = _uiState.value.myLovyId
                        val myName = _uiState.value.myName
                        val cleanMyId = myId.trim()

                        val myRemoteMoments = remoteMoments.filter { m ->
                            cleanMyId.isNotBlank() && m.authorId.trim().equals(cleanMyId, ignoreCase = true)
                        }
                        val myRemoteIds = myRemoteMoments.map { it.id }.toSet()
                        val myLocalOnlyIds = locallySaved.filter { m ->
                            cleanMyId.isNotBlank() && m.authorId.trim().equals(cleanMyId, ignoreCase = true)
                        }.map { it.id }
                        val allMyIds = (_uiState.value.myMomentIds.filter { id ->
                            locallySaved.any { it.id == id && cleanMyId.isNotBlank() && it.authorId.trim().equals(cleanMyId, ignoreCase = true) }
                        } + myRemoteIds + myLocalOnlyIds).toSet()

                        myRemoteMoments.forEach { saveMyLocalMoment(it) }
                        try {
                            if (cleanMyId.isNotBlank()) {
                                val userMomentIdsKey = "my_moment_ids_${cleanMyId}"
                                prefs.edit().putStringSet(userMomentIdsKey, allMyIds).apply()
                            }
                        } catch (_: Throwable) {}

                        val myLocal = (_uiState.value.moments.filter { it.id in allMyIds } + locallySaved).distinctBy { it.id }
                        val merged = (myLocal + remoteMoments).distinctBy { it.id }
                        val enriched = enrichMomentsWithAvatars(merged)
                        val enrichedWithComments = enriched.map { m ->
                            val localCount = _uiState.value.momentComments[m.id]?.size ?: 0
                            if (localCount > m.commentsCount) m.copy(commentsCount = localCount) else m
                        }
                        _uiState.update { 
                            it.copy(
                                moments = enrichedWithComments,
                                myMomentIds = allMyIds
                            ) 
                        }
                        lastMomentsSyncTime = System.currentTimeMillis()
                        fetched = true

                        // Auto-sync: Unggah otomatis momen lokal yang belum tersimpan di cloud Supabase
                        val remoteIds = remoteMoments.map { it.id }.toSet()
                        val unsyncedMoments = myLocal.filterNot { it.id in remoteIds }
                        if (unsyncedMoments.isNotEmpty()) {
                            viewModelScope.launch(Dispatchers.IO) {
                                val currentLovyId = _uiState.value.myLovyId
                                unsyncedMoments.forEach { unposted ->
                                    val author = unposted.authorId.takeIf { it.isNotBlank() && it != "me" }
                                        ?: currentLovyId.ifBlank { authRepo.getSavedSession()?.lovyId ?: authRepo.getOrGenerateLovyId(_uiState.value.myName) }
                                    val ok = supabaseRepo.sendMoment(unposted, author)
                                    if (ok) {
                                        Log.d("LovyChatViewModel", "Berhasil auto-sync momen lokal ke Supabase: ${unposted.id}")
                                    }
                                }
                            }
                        }
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
        val initialMoments = getLocalSavedMoments()
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                isGuest = false,
                myName = finalName,
                currentScreen = CurrentScreen.Main,
                // Mode Pengguna Asli: Pisahkan dari percakapan dummy tamu agar tidak bercampur
                conversations = emptyList(),
                messagesMap = emptyMap(),
                moments = initialMoments,
                oceanBottles = emptyList(),
                nearbyUsers = emptyList()
            )
        }
        updateUserActivity()
        loadMyMoments()
        syncFromSupabase(forceRefresh = true)
        syncUserProfileToSupabase()
    }

    suspend fun performRegister(username: String, password: String, gender: Gender): AuthResult {
        val result = authRepo.register(username = username, password = password, displayName = username, gender = gender)
        if (result.success) {
            val lovyId = result.lovyId ?: authRepo.getOrGenerateLovyId(username)
            val finalName = result.displayName ?: result.username ?: username
            clearDummyFriends()
            val initialMoments = getLocalSavedMoments()
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isGuest = false,
                    myName = finalName,
                    myLovyId = lovyId,
                    currentScreen = CurrentScreen.Main,
                    conversations = emptyList(),
                    messagesMap = emptyMap(),
                    moments = initialMoments,
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
            loadMyMoments()
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
            val initialMoments = getLocalSavedMoments()
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isGuest = false,
                    myName = finalName,
                    myLovyId = lovyId,
                    currentScreen = CurrentScreen.Main,
                    conversations = emptyList(),
                    messagesMap = emptyMap(),
                    moments = initialMoments,
                    myMomentIds = emptySet(),
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
            val initialMoments = getLocalSavedMoments()
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isGuest = false,
                    myName = finalName,
                    myLovyId = lovyId,
                    currentScreen = CurrentScreen.Main,
                    conversations = emptyList(),
                    messagesMap = emptyMap(),
                    moments = initialMoments,
                    myMomentIds = emptySet(),
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
        // Mode tamu telah dinonaktifkan sepenuhnya. Arahkan pengguna ke layar login.
        _uiState.update { it.copy(isLoggedIn = false, isGuest = false, currentScreen = CurrentScreen.Login) }
    }

    fun continueAsGuest() {
        loginAsGuest()
    }

    fun logout() {
        heartbeatJob?.cancel()
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
            // Tandai status offline di Supabase & bersihkan pesan pengirim saat logout
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    supabaseRepo.updateUserPresence(myId, isOnline = false)
                } catch (_: Exception) {}
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
                myMomentIds = emptySet(),
                oceanBottles = emptyList(),
                nearbyUsers = emptyList(),
                myLovyId = "",
                myName = "",
                myBio = "",
                userProfile = UserProfile()
            )
        }
        try {
            prefs.edit().remove("my_moment_ids").apply()
        } catch (_: Throwable) {}
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
        val userEmail = state.userProfile.email.orEmpty()
        if (userEmail.isNotBlank()) {
            authRepo.deleteAccount(userEmail)
        }
        if (myId.isNotBlank()) {
            authRepo.deleteAccount(myId)
        }

        deletedMessageIds.clear()
        deletedConversationTimestamps.clear()
        try {
            prefs.edit().remove("deleted_message_ids").remove("deleted_conversations_map").apply()
            com.example.util.LovyNotificationHelper.cancelAllNotifications(getApplication())
        } catch (_: Exception) {}

        viewModelScope.launch(Dispatchers.IO) {
            try {
                localChatRepo.clearAllMessages()
                userProfileRepo.deleteProfile("current_user")
                chatFriendDao.deleteAllFriends()
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal membersihkan data Room lokal: ${e.message}")
            }

            if (wasRealUser) {
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

            // Gunakan pengguna nyata dari Supabase/PocketBase dengan pengisian fillrate cerdas & label satu kota
            val remoteUsers = if (!isCacheValid || forceRefresh) supabaseRepo.fetchNearbyUsers() else _uiState.value.nearbyUsers
            lastNearbyScanTime = System.currentTimeMillis()
            updateUserActivity(force = true)
            
            val processed = processNearbyCandidates(remoteUsers)
            _uiState.update { it.copy(isScanningNearby = false, nearbyUsers = processed) }
        }
    }

    fun isSameCityArea(city1: String?, city2: String?): Boolean {
        if (city1.isNullOrBlank() || city2.isNullOrBlank()) return false
        val c1 = city1.trim().lowercase()
            .removePrefix("kota ")
            .removePrefix("kabupaten ")
            .removePrefix("kab. ")
            .removePrefix("dki ")
            .trim()
        val c2 = city2.trim().lowercase()
            .removePrefix("kota ")
            .removePrefix("kabupaten ")
            .removePrefix("kab. ")
            .removePrefix("dki ")
            .trim()
        if (c1.isBlank() || c2.isBlank()) return false
        if (c1 == c2) return true
        if (c1.contains(c2) || c2.contains(c1)) return true
        return false
    }

    private fun processNearbyCandidates(remoteUsers: List<User>?): List<User> {
        val list = remoteUsers ?: return emptyList()
        val myId = _uiState.value.myLovyId
        val myCity = _uiState.value.userProfile.city.trim().ifBlank {
            _uiState.value.currentGpsLocation?.cityName?.trim() ?: "Jakarta Selatan"
        }

        val allRealUsers = list
            .filterNot { it.id == myId || it.id == "current_user" || isSelfUser(it.id, it.name) }
            .filterNot { it.name.trim().isBlank() }
            .filterNot { isUserBlocked(it.id, it.name) }
            .filterNot { isDummyFriend(it.id, it.name) }

        val taggedUsers = allRealUsers.map { user ->
            val sameCity = isSameCityArea(user.city, myCity)
            val realisticDistance = if (sameCity) {
                if (user.distanceMeters <= 150 || user.distanceMeters > 35000) {
                    val offset = kotlin.math.abs(user.id.hashCode() % 3750) + 450
                    offset
                } else {
                    user.distanceMeters
                }
            } else {
                if (user.distanceMeters < 5000) {
                    val offset = kotlin.math.abs(user.id.hashCode() % 45000) + 12000
                    offset
                } else {
                    user.distanceMeters
                }
            }
            user.copy(
                isSameCity = sameCity,
                distanceMeters = realisticDistance
            )
        }

        // Fillrate Strategy (Pengisian bertingkat agar radar dan daftar selalu optimal terisi):
        // 1. Prioritaskan teman online di Satu Kota
        // 2. Jika online masih sedikit (< 12), sertakan teman nyata lainnya yang berlabel Satu Kota
        // 3. Sertakan teman online di kota sekitar/nasional
        // 4. Sertakan seluruh pengguna terdaftar lainnya
        val onlineCandidates = taggedUsers.filter { it.isOnline }
        return if (onlineCandidates.size >= 12) {
            onlineCandidates.sortedWith(
                compareByDescending<User> { it.isSameCity }
                    .thenBy { it.distanceMeters }
            )
        } else {
            val sameCityUsers = taggedUsers.filter { it.isSameCity }
            val otherUsers = taggedUsers.filterNot { it.isSameCity }

            val combined = (sameCityUsers.sortedWith(compareByDescending<User> { it.isOnline }.thenBy { it.distanceMeters }) +
                    otherUsers.sortedWith(compareByDescending<User> { it.isOnline }.thenBy { it.distanceMeters }))
                .distinctBy { it.id }

            combined
        }
    }

    fun getCanonicalConversationId(id1: String, id2: String): String {
        return com.example.data.pocketbase.PocketBaseClient.getCanonicalConversationId(id1, id2)
    }

    fun normalizeConversationId(rawConvId: String): String {
        return com.example.data.pocketbase.PocketBaseClient.normalizeConvId(rawConvId)
    }

    fun extractPartnerIdFromConvId(convId: String, myId: String): String {
        if (!convId.startsWith("conv_")) return convId
        val content = convId.removePrefix("conv_")
        val cleanMyId = myId.trim()
        val myPbId = if (cleanMyId.isNotBlank()) com.example.data.pocketbase.PocketBaseClient.toPbId(cleanMyId) else ""

        // 1. Format separator ganda '__' (sangat presisi untuk ID yang mengandung underscore)
        if (content.contains("__")) {
            val parts = content.split("__")
            val partner = parts.firstOrNull { part ->
                !isSelfUser(part, null) &&
                !part.equals(cleanMyId, ignoreCase = true) &&
                !part.equals(myPbId, ignoreCase = true) &&
                (myPbId.isBlank() || com.example.data.pocketbase.PocketBaseClient.toPbId(part) != myPbId)
            }
            if (!partner.isNullOrBlank()) return partner
            return ""
        }

        // 2. Format single '_' jika myId berada di awal atau di akhir
        if (cleanMyId.isNotBlank()) {
            if (content.startsWith("${cleanMyId}__", ignoreCase = true)) {
                return content.substring(cleanMyId.length + 2)
            }
            if (myPbId.isNotBlank() && content.startsWith("${myPbId}__", ignoreCase = true)) {
                return content.substring(myPbId.length + 2)
            }
            if (content.startsWith("${cleanMyId}_", ignoreCase = true)) {
                return content.substring(cleanMyId.length + 1)
            }
            if (myPbId.isNotBlank() && content.startsWith("${myPbId}_", ignoreCase = true)) {
                return content.substring(myPbId.length + 1)
            }
            if (content.endsWith("__${cleanMyId}", ignoreCase = true)) {
                return content.substring(0, content.length - cleanMyId.length - 2)
            }
            if (myPbId.isNotBlank() && content.endsWith("__${myPbId}", ignoreCase = true)) {
                return content.substring(0, content.length - myPbId.length - 2)
            }
            if (content.endsWith("_${cleanMyId}", ignoreCase = true)) {
                return content.substring(0, content.length - cleanMyId.length - 1)
            }
            if (myPbId.isNotBlank() && content.endsWith("_${myPbId}", ignoreCase = true)) {
                return content.substring(0, content.length - myPbId.length - 1)
            }
        }

        // 3. Deteksi pola lovy ID: lovy_XXXXXX
        val lovyMatches = Regex("(lovy_[0-9a-zA-Z]+)").findAll(content).map { it.value }.toList()
        if (lovyMatches.size >= 2) {
            val partner = lovyMatches.firstOrNull { !it.equals(cleanMyId, ignoreCase = true) && !isSelfUser(it, null) }
            if (!partner.isNullOrBlank()) {
                val resolved = findUserById(partner)
                return if (resolved != null && resolved.id.isNotBlank() && !resolved.id.startsWith("u000")) resolved.id else partner
            }
            return ""
        }

        // 4. Fallback legacy jika split 2 bagian
        val parts = content.split("_")
        if (parts.size == 2) {
            val partner = parts.firstOrNull { !it.equals(cleanMyId, ignoreCase = true) && !isSelfUser(it, null) }
            if (partner != null && !partner.equals(cleanMyId, ignoreCase = true)) {
                val resolved = findUserById(partner)
                return if (resolved != null && resolved.id.isNotBlank() && !resolved.id.startsWith("u000")) resolved.id else partner
            }
        }
        if (content.equals(cleanMyId, ignoreCase = true) || isSelfUser(content, null)) return ""
        val resolved = findUserById(content)
        return if (resolved != null && resolved.id.isNotBlank() && !resolved.id.startsWith("u000")) resolved.id else content
    }

    fun sayHiToUser(user: User) {
        if (isSelfUser(user.id, user.name)) return
        if (isUserBlocked(user.id, user.name)) return
        recordFeatureClick()
        saveChatFriend(user)
        updateUserActivity()
        val convId = getCanonicalConversationId(_uiState.value.myLovyId, user.id)
        val existing = _uiState.value.conversations.find { 
            isSameConversation(it.id, convId) || 
            isSameUser(it.partnerId, user.id) ||
            areUsersSamePerson(it.partnerId, it.partnerName, user.id, user.name)
        }
        
        if (existing == null) {
            val newConv = ChatConversation(
                id = convId,
                partnerId = user.id,
                partnerName = user.name,
                partnerAvatarHex = user.avatarColorHex,
                partnerGender = user.gender,
                lastMessage = "",
                lastTimestamp = System.currentTimeMillis(),
                unreadCount = 0,
                isOnline = user.isOnline,
                partnerAvatarUrl = user.avatarUrl,
                partnerAge = user.age,
                partnerDistanceMeters = user.distanceMeters,
                partnerCity = user.city
            )
            _uiState.update {
                it.copy(
                    conversations = listOf(newConv) + it.conversations
                )
            }
        } else if (existing.id != convId || existing.partnerName.startsWith("Pengguna (") || existing.partnerId != user.id) {
            _uiState.update { state ->
                val updated = state.conversations.map { c ->
                    if (c.id == existing.id || isSameUser(c.partnerId, user.id)) {
                        c.copy(
                            id = convId,
                            partnerId = user.id,
                            partnerName = user.name,
                            partnerAvatarUrl = user.avatarUrl ?: c.partnerAvatarUrl
                        )
                    } else c
                }
                state.copy(conversations = updated)
            }
        }

        // Buka ruang obrolan langsung tanpa mengirim pesan default otomatis
        val targetConvId = existing?.id ?: convId
        openChat(targetConvId, user.name, user.avatarColorHex)
    }

    fun openChat(conversationId: String, partnerName: String, partnerAvatarHex: Long) {
        recordFeatureClick()
        val conv = _uiState.value.conversations.find { isSameConversation(it.id, conversationId) }
        val partnerId = conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
        val resolvedName = if (partnerName.startsWith("Pengguna (") && conv != null && !conv.partnerName.startsWith("Pengguna (")) {
            conv.partnerName
        } else partnerName

        if (conv != null && !isSelfUser(conv.partnerId, conv.partnerName) && _uiState.value.chattedFriends.any { isSameUser(it.id, conv.partnerId) }) {
            saveChatFriend(
                User(
                    id = conv.partnerId,
                    name = resolvedName,
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
                if (isSameConversation(c.id, conversationId)) c.copy(unreadCount = 0) else c
            }
            it.copy(
                conversations = updatedConvs,
                activeChatId = conversationId,
                currentScreen = CurrentScreen.ChatDetail(conversationId, resolvedName, partnerAvatarHex)
            )
        }

        markConversationAsRead(conversationId, partnerId)
        com.example.util.LovyNotificationHelper.cancelNotification(getApplication(), conversationId)

        // Bersihkan pesan default otomatis lama dari memori jika masih ada
        val currentMemoryMsgs = _uiState.value.messagesMap[conversationId]
        if (currentMemoryMsgs != null && currentMemoryMsgs.any { it.text.contains("Salam kenal dari fitur Teman Sekitar") }) {
            val cleanedMsgs = currentMemoryMsgs.filterNot { it.text.contains("Salam kenal dari fitur Teman Sekitar") }
            _uiState.update { state ->
                val updatedConvs = state.conversations.map { c ->
                    if (c.id == conversationId && c.lastMessage.contains("Salam kenal dari fitur Teman Sekitar")) {
                        c.copy(lastMessage = cleanedMsgs.lastOrNull()?.text ?: "")
                    } else c
                }
                state.copy(
                    messagesMap = state.messagesMap + (conversationId to cleanedMsgs),
                    conversations = updatedConvs
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                localChatRepo.deleteAutomatedGreetings()
            }
        }

        // Muat pesan dari cache lokal Room jika state di memori masih kosong agar instan
        if ((_uiState.value.messagesMap[conversationId] ?: emptyList()).isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val cached = localChatRepo.getMessagesForConversation(conversationId)
                    .filterNot { it.text.contains("Salam kenal dari fitur Teman Sekitar") }
                val deduplicatedCached = deduplicateAndMergeMessages(
                    existing = cached,
                    onObsoleteIdDetected = { obsoleteId ->
                        viewModelScope.launch(Dispatchers.IO) {
                            localChatRepo.deleteMessage(obsoleteId)
                        }
                    }
                )
                if (deduplicatedCached.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        _uiState.update { state ->
                            val current = state.messagesMap[conversationId] ?: emptyList()
                            if (current.isEmpty()) {
                                state.copy(messagesMap = state.messagesMap + (conversationId to deduplicatedCached))
                            } else {
                                val mergedCurrent = deduplicateAndMergeMessages(current, deduplicatedCached)
                                state.copy(messagesMap = state.messagesMap + (conversationId to mergedCurrent))
                            }
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

        // 4. Batalkan notifikasi sistem untuk percakapan ini
        com.example.util.LovyNotificationHelper.cancelNotification(getApplication(), conversationId)
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
                    val convDeletedTimestamp = deletedConversationTimestamps[conversationId] ?: 0L
                    val actualChatMsgs = remoteMsgs.filterNot { msg ->
                        msg.text.startsWith("__TYPING_") ||
                        msg.text == "__DELETED_FOR_EVERYONE__" ||
                        msg.text.contains("Salam kenal dari fitur Teman Sekitar") ||
                        deletedMessageIds.contains(msg.id) ||
                        msg.timestamp <= convDeletedTimestamp ||
                        (msg.isFromMe && msg.deletedForSender) ||
                        (!msg.isFromMe && msg.deletedForReceiver)
                    }

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

                    if (actualChatMsgs.isNotEmpty() || remoteMsgs.any { deletedMessageIds.contains(it.id) }) {
                        val isViewing = _uiState.value.activeChatId == conversationId
                        val processedMsgs = if (isViewing) {
                            actualChatMsgs.map { if (!it.isFromMe) it.copy(isRead = true) else it }
                        } else {
                            actualChatMsgs
                        }

                        val current = _uiState.value.messagesMap[conversationId] ?: emptyList()
                        val currentIds = current.map { it.id }.toSet()
                        val newPartnerMsgs = actualChatMsgs.filter { 
                            !it.isFromMe && 
                            !currentIds.contains(it.id) && 
                            !deletedMessageIds.contains(it.id) &&
                            it.timestamp > convDeletedTimestamp &&
                            !it.deletedForReceiver
                        }

                        val merged = deduplicateAndMergeMessages(
                            existing = current,
                            incoming = processedMsgs,
                            onObsoleteIdDetected = { obsoleteId ->
                                viewModelScope.launch(Dispatchers.IO) {
                                    localChatRepo.deleteMessage(obsoleteId)
                                }
                            }
                        ).filterNot { 
                            deletedMessageIds.contains(it.id) ||
                            (it.deletedForSender && it.isFromMe) ||
                            (it.deletedForReceiver && !it.isFromMe) ||
                            it.timestamp <= convDeletedTimestamp
                        }

                        val lastMsg = merged.lastOrNull()
                        val convExists = _uiState.value.conversations.any { it.id == conversationId }
                        val isAlreadyFriend = _uiState.value.chattedFriends.any { it.id.equals(partnerId, ignoreCase = true) }
                        val updatedConvs = if (convExists) {
                            if (merged.isEmpty()) {
                                _uiState.value.conversations.filterNot { it.id == conversationId }
                            } else {
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
                            }
                        } else if (isAlreadyFriend && lastMsg != null && lastMsg.timestamp > convDeletedTimestamp) {
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
                                    isOnline = partnerUser?.isOnline ?: false,
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
                                if (latest != null && latest.timestamp > convDeletedTimestamp) {
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
        if (isSelfUser(bottle.senderId, bottle.senderName)) return
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
        
        if (existing == null) {
            val newConv = ChatConversation(
                id = convId,
                partnerId = bottle.senderId,
                partnerName = bottle.senderName,
                partnerAvatarHex = bottle.avatarHex,
                partnerGender = bottle.senderGender,
                lastMessage = "",
                lastTimestamp = System.currentTimeMillis(),
                unreadCount = 0,
                isOnline = true,
                partnerAvatarUrl = bottle.avatarUrl
            )
            _uiState.update {
                it.copy(
                    fishedBottle = null,
                    conversations = listOf(newConv) + it.conversations
                )
            }
        } else {
            _uiState.update { it.copy(fishedBottle = null) }
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

        // Catat messageId agar tidak pernah di-fetch atau memicu notifikasi lagi
        persistDeletedMessageId(messageId)

        // Batalkan notifikasi jika tidak ada lagi pesan belum terbaca dari partner
        if (updatedMsgs.none { !it.isFromMe && !it.isRead }) {
            com.example.util.LovyNotificationHelper.cancelNotification(getApplication(), conversationId)
        }

        viewModelScope.launch(Dispatchers.IO) {
            localChatRepo.deleteMessage(messageId)
            if (!_uiState.value.isGuest) {
                if (targetMsg?.isFromMe == true) {
                    supabaseRepo.markMessageDeletedForSender(messageId)
                } else {
                    supabaseRepo.markMessageDeletedForReceiver(messageId)
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

        persistDeletedMessageId(messageId)
        com.example.util.LovyNotificationHelper.cancelNotification(getApplication(), conversationId)

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
     * Berikan atau perbarui reaksi emoji pada pesan obrolan (Message Reactions).
     * Pilihan cepat: ❤️, 😂, 😮, 😢, 🙏, 👍 atau emoji lainnya.
     * Reaksi tersinkronisasi instan via Realtime ke lawan bicara dan tersimpan di database lokal.
     */
    fun reactToMessage(conversationId: String, messageId: String, emoji: String?) {
        val currentMsgs = _uiState.value.messagesMap[conversationId] ?: emptyList()
        val targetMsg = currentMsgs.find { it.id == messageId } ?: return

        // Jika reaksi yang diklik sama persis dengan reaksi sebelumnya, hapus reaksinya (toggle)
        val newReaction = if (targetMsg.reaction == emoji) null else emoji

        val updatedMsgs = currentMsgs.map { msg ->
            if (msg.id == messageId) msg.copy(reaction = newReaction) else msg
        }

        _uiState.update {
            it.copy(messagesMap = it.messagesMap + (conversationId to updatedMsgs))
        }

        viewModelScope.launch(Dispatchers.IO) {
            localChatRepo.updateMessageReaction(messageId, newReaction)

            if (!_uiState.value.isGuest) {
                val partnerId = extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
                val payload = "__REACTION__:${messageId}:${newReaction ?: "NONE"}"
                val reactionDto = com.example.data.supabase.SupabaseMessageDto(
                    id = java.util.UUID.randomUUID().toString(),
                    conversationId = conversationId,
                    senderId = _uiState.value.myLovyId,
                    receiverId = partnerId,
                    text = payload,
                    createdAt = System.currentTimeMillis()
                )
                com.example.data.supabase.SupabaseRealtimeManager.broadcastChatMessage(reactionDto)
                try {
                    supabaseRepo.sendChatMessage(
                        message = ChatMessage(
                            id = reactionDto.id,
                            conversationId = conversationId,
                            text = payload,
                            timestamp = reactionDto.createdAt,
                            isFromMe = true
                        ),
                        senderId = _uiState.value.myLovyId,
                        receiverId = partnerId
                    )
                } catch (_: Exception) {}
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

        val now = System.currentTimeMillis()
        val myId = _uiState.value.myLovyId
        val app = getApplication<Application>()

        conversationIds.forEach { convId ->
            // Simpan timestamp hapus agar pesan-pesan lama di obrolan ini tidak pernah memicu notif / bangkit kembali
            persistDeletedConversation(convId, now)
            // Batalkan semua notifikasi sistem yang berkaitan dengan obrolan ini
            com.example.util.LovyNotificationHelper.cancelNotification(app, convId)
        }

        viewModelScope.launch(Dispatchers.IO) {
            conversationIds.forEach { convId ->
                localChatRepo.clearConversation(convId)
                if (!_uiState.value.isGuest && myId.isNotBlank()) {
                    supabaseRepo.markConversationDeletedForUser(convId, myId)
                }
            }
        }
    }

    fun sendVoiceNoteMessage(
        conversationId: String,
        audioFile: java.io.File,
        durationSeconds: Int,
        partnerName: String,
        replyTarget: ChatMessage? = null
    ) {
        if (!audioFile.exists() || audioFile.length() == 0L) return
        if (isUserBlocked(userName = partnerName)) return
        updateUserActivity()

        val tempMsgId = com.example.data.pocketbase.PocketBaseClient.toPbId(java.util.UUID.randomUUID().toString())
        val currentMyId = _uiState.value.myLovyId.ifBlank { "me" }
        val conv = _uiState.value.conversations.find { it.id == conversationId }
        val partnerId = conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, currentMyId)

        viewModelScope.launch {
            try {
                val bytes = withContext(Dispatchers.IO) { audioFile.readBytes() }
                val fileName = "vn_${System.currentTimeMillis()}_${java.util.UUID.randomUUID().toString().take(6)}.m4a"
                val uploadResult = com.example.data.storage.R2StorageClient.uploadVoiceNote(bytes, fileName)

                if (uploadResult.isSuccess) {
                    val publicAudioUrl = uploadResult.getOrThrow()
                    val newMsg = ChatMessage(
                        id = tempMsgId,
                        conversationId = conversationId,
                        text = "",
                        timestamp = System.currentTimeMillis(),
                        isFromMe = true,
                        isRead = false,
                        imageUrl = null,
                        audioUrl = publicAudioUrl,
                        audioDurationSeconds = durationSeconds,
                        replyToId = replyTarget?.id,
                        replyToSender = replyTarget?.let { if (it.isFromMe) "Anda" else partnerName },
                        replyToText = replyTarget?.let {
                            if (it.audioUrl != null) "🎙️ Pesan Suara (${it.audioDurationSeconds}d)"
                            else if (it.imageUrl != null) "📷 Foto"
                            else it.text
                        }
                    )

                    val previewText = "🎙️ Pesan Suara (${durationSeconds}d)"
                    val updatedMessages = deduplicateAndMergeMessages(
                        existing = _uiState.value.messagesMap[conversationId] ?: emptyList(),
                        incoming = listOf(newMsg)
                    )
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
                    withContext(Dispatchers.IO) {
                        localChatRepo.saveMessage(newMsg)
                        audioFile.delete()
                    }

                    supabaseRepo.sendChatMessage(
                        message = newMsg,
                        senderId = currentMyId,
                        receiverId = partnerId
                    )
                } else {
                    android.util.Log.w("LovyChatViewModel", "Gagal upload pesan suara ke R2: ${uploadResult.exceptionOrNull()?.message}")
                    withContext(Dispatchers.Main) {
                        android.widget.Toast.makeText(getApplication(), "Gagal mengirim pesan suara. Periksa koneksi internet.", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("LovyChatViewModel", "Error sending voice note", e)
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

        // Proteksi anti-duplikasi: abaikan jika pesan identik baru saja dikirim (< 600ms)
        val currentMsgs = _uiState.value.messagesMap[conversationId] ?: emptyList()
        val lastSent = currentMsgs.lastOrNull { it.isFromMe }
        if (lastSent != null && lastSent.text == text.trim() && lastSent.imageUrl == imageUrl &&
            (System.currentTimeMillis() - lastSent.timestamp) < 600L) {
            Log.d("LovyChatViewModel", "Pesan identik diabaikan untuk mencegah duplikasi (debounce)")
            return
        }

        updateUserActivity()
        val messageId = com.example.data.pocketbase.PocketBaseClient.toPbId(UUID.randomUUID().toString())
        val newMsg = ChatMessage(
            id = messageId,
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

        val updatedMessages = deduplicateAndMergeMessages(
            existing = currentMsgs,
            incoming = listOf(newMsg)
        )
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

        // Sinkronisasi ke backend (PocketBase / Supabase)
        val conv = _uiState.value.conversations.find { it.id == conversationId }
        val partnerId = conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
        val currentMyId = _uiState.value.myLovyId.ifBlank { "me" }
        onUserTyping(conversationId, partnerId, false)
        viewModelScope.launch {
            try {
                val ok = supabaseRepo.sendChatMessage(
                    message = newMsg,
                    senderId = currentMyId,
                    receiverId = partnerId
                )
                if (ok) {
                    Log.d("LovyChatViewModel", "Pesan chat berhasil dikirim ke server: ${newMsg.id}")
                } else {
                    Log.w("LovyChatViewModel", "Gagal mengirim pesan chat ke server: ${newMsg.id}")
                }
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error mengirim chat ke server: ${e.message}", e)
            }
        }
    }

    fun throwBottle(content: String): Boolean {
        if (content.isBlank()) return false
        recordFeatureClick()
        val currentMyId = _uiState.value.myLovyId.ifBlank { "me" }
        val newBottle = BottleMessage(
            id = UUID.randomUUID().toString(),
            senderId = currentMyId,
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

        // Sinkronisasi ke backend (PocketBase / Supabase)
        viewModelScope.launch {
            try {
                val ok = supabaseRepo.sendBottle(newBottle)
                if (ok) {
                    Log.d("LovyChatViewModel", "Botol berhasil dikirim ke server: ${newBottle.id}")
                } else {
                    Log.w("LovyChatViewModel", "Gagal mengirim botol ke server: ${newBottle.id}")
                }
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error mengirim botol: ${e.message}", e)
            }
        }

        return true
    }

    fun fishBottle() {
        recordFeatureClick()
        viewModelScope.launch {
            _uiState.update { it.copy(isFishing = true, fishedBottle = null) }
            
            // Coba ambil botol terbaru dari cloud/PocketBase jika terkonfigurasi
            if (SupabaseClient.isConfigured()) {
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
        val senderDisplayName = state.myName.ifBlank { state.userProfile.displayName.ifBlank { "Pengguna Lovy" } }
        val newComment = com.example.model.MomentComment(
            id = UUID.randomUUID().toString(),
            momentId = momentId,
            authorId = myId,
            authorName = senderDisplayName,
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

        // 1. Simpan komentar secara persisten ke SharedPreferences
        persistMomentComments(updatedMap)

        // 2. Simpan pembaharuan jumlah komentar ke cache momen lokal
        val momentToUpdate = updatedMoments.find { it.id == momentId }
        if (momentToUpdate != null) {
            saveMyLocalMoment(momentToUpdate)
        }

        // 3. Update comments_count di cloud Supabase secara asinkron
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (SupabaseClient.isConfigured()) {
                    supabaseRepo.updateMomentCommentsCount(momentId, updatedComments.size)
                }
            } catch (_: Throwable) {
            }
        }
    }

    fun postMoment(content: String, imageUrl: String? = null, locationTag: String? = null) {
        if (content.isBlank()) return
        recordFeatureClick()
        val authorId = _uiState.value.myLovyId.ifBlank {
            val gen = authRepo.getSavedSession()?.lovyId
                ?: authRepo.getOrGenerateLovyId(_uiState.value.userProfile.email ?: _uiState.value.myName)
            _uiState.update { it.copy(myLovyId = gen) }
            gen
        }
        val authorAvatar = _uiState.value.userProfile.profilePicture?.takeIf { it.isNotBlank() }
        val momentId = com.example.data.pocketbase.PocketBaseClient.toPbId(UUID.randomUUID().toString())
        val newMoment = MomentItem(
            id = momentId,
            authorName = _uiState.value.myName.ifBlank { "Pengguna Lovy" },
            authorAvatarHex = 0xFF00A86B,
            timeAgo = "Baru saja",
            content = content.trim(),
            likesCount = 0,
            isLiked = false,
            commentsCount = 0,
            imageUrl = imageUrl,
            authorAvatarUrl = authorAvatar,
            locationTag = locationTag?.ifBlank { null }
                ?: _uiState.value.currentGpsLocation?.cityName?.ifBlank { null }
                ?: "Surabaya",
            authorId = authorId
        )
        val userMomentIdsKey = if (authorId.isNotBlank()) "my_moment_ids_${authorId}" else "my_moment_ids"
        val newMomentIds = _uiState.value.myMomentIds + newMoment.id
        try {
            if (authorId.isNotBlank()) {
                prefs.edit().putStringSet(userMomentIdsKey, newMomentIds).apply()
            }
            saveMyLocalMoment(newMoment)
        } catch (_: Throwable) {
        }
        _uiState.update { 
            it.copy(
                moments = listOf(newMoment) + it.moments.filterNot { m -> m.id == newMoment.id }, 
                myMomentIds = newMomentIds
            ) 
        }

        // Simpan ke PocketBase / backend cloud
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!_uiState.value.isGuest) {
                    syncUserProfileToSupabase()
                }
                val ok = supabaseRepo.sendMoment(newMoment, authorId)
                if (ok) {
                    Log.d("LovyChatViewModel", "Momen berhasil disimpan ke server cloud: ${newMoment.id}")
                    withContext(Dispatchers.Main) {
                        try {
                            android.widget.Toast.makeText(getApplication(), "Momen berhasil dibagikan!", android.widget.Toast.LENGTH_SHORT).show()
                        } catch (_: Throwable) {}
                    }
                } else {
                    Log.w("LovyChatViewModel", "Gagal menyimpan momen ke server cloud: ${newMoment.id}, akan dicoba ulang otomatis saat sinkronisasi")
                }
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error menyimpan momen: ${e.message}", e)
            }
        }
    }

    fun deleteMoment(momentId: String) {
        recordFeatureClick()
        val myId = _uiState.value.myLovyId.trim()
        val targetMoment = _uiState.value.moments.find { it.id == momentId }

        // Validasi hak kepemilikan: HANYA pembuat momen asli (author) yang boleh menghapus
        val isAuthor = targetMoment == null ||
            (myId.isNotBlank() && targetMoment.authorId.trim().equals(myId, ignoreCase = true)) ||
            (momentId in _uiState.value.myMomentIds && (targetMoment.authorId.isBlank() || targetMoment.authorId.equals("me", ignoreCase = true)))

        if (!isAuthor) {
            Log.w("LovyChatViewModel", "Percobaan menghapus momen orang lain dibatalkan: momentId=$momentId, user=$myId, author=${targetMoment?.authorId}")
            try {
                android.widget.Toast.makeText(getApplication(), "Hanya pembuat momen yang dapat menghapus postingan ini", android.widget.Toast.LENGTH_SHORT).show()
            } catch (_: Throwable) {}
            return
        }

        val userMomentIdsKey = if (myId.isNotBlank()) "my_moment_ids_${myId}" else "my_moment_ids"
        val newMomentIds = _uiState.value.myMomentIds - momentId
        try {
            if (myId.isNotBlank()) {
                prefs.edit().putStringSet(userMomentIdsKey, newMomentIds).apply()
            }
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
        } catch (_: Throwable) {}
        // Hapus dari backend jika tersambung
        viewModelScope.launch {
            try {
                supabaseRepo.deleteMoment(momentId)
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal menghapus momen di cloud: $momentId", e)
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
                    uploadProgressText = "Mengompres foto profil..."
                )
            }
            try {
                val bytes = com.example.util.ImageCompressor.compressImage(
                    context = ctx,
                    uri = uri,
                    maxDimension = 720,
                    quality = 80
                )
                if (bytes == null || bytes.isEmpty()) {
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    onComplete?.invoke(false, "Gagal memproses gambar.")
                    return@launch
                }

                _uiState.update { it.copy(uploadProgressText = "Mengunggah foto profil (${bytes.size / 1024} KB)...") }
                val fileName = "avatar_${_uiState.value.myLovyId}_${System.currentTimeMillis()}.jpg"
                val result = com.example.data.storage.R2StorageClient.uploadImage(
                    bytes = bytes,
                    folder = "avatars",
                    fileName = fileName
                )

                val publicUrl = result.getOrNull()
                if (result.isSuccess && !publicUrl.isNullOrBlank()) {
                    val updatedProfile = _uiState.value.userProfile.copy(profilePicture = publicUrl)
                    saveUserProfile(updatedProfile)
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    try {
                        withContext(Dispatchers.Main) {
                            android.widget.Toast.makeText(ctx, "Foto profil berhasil diperbarui! 🎉", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    } catch (_: Throwable) {}
                    onComplete?.invoke(true, publicUrl)
                } else {
                    val err = result.exceptionOrNull()?.localizedMessage ?: "Gagal mengunggah foto profil ke Cloudflare R2"
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    try {
                        withContext(Dispatchers.Main) {
                            android.widget.Toast.makeText(ctx, "Gagal upload: $err", android.widget.Toast.LENGTH_LONG).show()
                        }
                    } catch (_: Throwable) {}
                    onComplete?.invoke(false, err)
                }
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error upload profile photo", e)
                val err = e.localizedMessage ?: "Terjadi kesalahan upload foto"
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                try {
                    withContext(Dispatchers.Main) {
                        android.widget.Toast.makeText(ctx, "Gagal upload: $err", android.widget.Toast.LENGTH_LONG).show()
                    }
                } catch (_: Throwable) {}
                onComplete?.invoke(false, err)
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
        val finalContent = content.trim().ifBlank { if (uri != null) "📷 Foto momen" else "" }
        if (finalContent.isBlank()) {
            onComplete?.invoke(false)
            return
        }

        val ctx = context ?: getApplication<Application>()
        if (uri == null) {
            postMoment(finalContent, null, locationTag)
            onComplete?.invoke(true)
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUploadingPhoto = true,
                    uploadProgressText = "Mengompres foto..."
                )
            }
            try {
                // 1. Kompresi gambar resolusi Full-HD yang ramah bandwidth dan server
                val bytes = com.example.util.ImageCompressor.compressImage(
                    context = ctx,
                    uri = uri,
                    maxDimension = 1080,
                    quality = 80
                )
                if (bytes == null || bytes.isEmpty()) {
                    Log.e("LovyChatViewModel", "Gagal mengompres gambar momen dari URI: $uri")
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    try {
                        android.widget.Toast.makeText(ctx, "Gagal memproses foto yang dipilih", android.widget.Toast.LENGTH_SHORT).show()
                    } catch (_: Throwable) {}
                    onComplete?.invoke(false)
                    return@launch
                }

                _uiState.update { it.copy(uploadProgressText = "Menyiapkan foto momen (${bytes.size / 1024} KB)...") }
                val fileName = "moment_${UUID.randomUUID()}.jpg"

                // 2. Coba upload langsung ke Cloudflare R2 jika terhubung
                val r2Result = com.example.data.storage.R2StorageClient.uploadImage(
                    bytes = bytes,
                    folder = "moments",
                    fileName = fileName
                )
                var uploadedUrl = r2Result.getOrNull()

                // Fallback: Jika upload R2 gagal/offline/tidak terkonfigurasi, ubah foto menjadi data URL Base64 yang kompatibel 100% dengan PocketBase
                if (uploadedUrl.isNullOrBlank()) {
                    Log.w("LovyChatViewModel", "Upload foto ke Cloudflare R2 tidak tersedia/gagal, menggunakan data URL Base64 untuk disimpan langsung di PocketBase")
                    val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                    uploadedUrl = "data:image/jpeg;base64,$base64"
                }

                // 3. Simpan momen dengan URL foto yang berhasil diunggah / di-encode
                _uiState.update { it.copy(uploadProgressText = "Menerbitkan momen...") }
                postMoment(finalContent, uploadedUrl, locationTag)
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                onComplete?.invoke(true)
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error posting moment with photo", e)
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                try {
                    val err = e.localizedMessage ?: "Terjadi kesalahan saat mengunggah foto"
                    android.widget.Toast.makeText(ctx, "Gagal upload: $err", android.widget.Toast.LENGTH_LONG).show()
                } catch (_: Throwable) {}
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
                    uploadProgressText = "Mengompres foto chat..."
                )
            }
            try {
                val bytes = com.example.util.ImageCompressor.compressImage(
                    context = ctx,
                    uri = uri,
                    maxDimension = 1080,
                    quality = 80
                )
                if (bytes == null || bytes.isEmpty()) {
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    return@launch
                }

                _uiState.update { it.copy(uploadProgressText = "Mengunggah foto chat (${bytes.size / 1024} KB)...") }
                val fileName = "chat_${UUID.randomUUID()}.jpg"
                val result = com.example.data.storage.R2StorageClient.uploadImage(
                    bytes = bytes,
                    folder = "chats/$conversationId",
                    fileName = fileName
                )

                val photoUrl = result.getOrNull()

                if (!photoUrl.isNullOrBlank()) {
                    val displayText = caption.trim().ifBlank { "📷 Foto" }
                    val messageId = com.example.data.pocketbase.PocketBaseClient.toPbId(UUID.randomUUID().toString())
                    val newMsg = ChatMessage(
                        id = messageId,
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

                    val updatedMessages = deduplicateAndMergeMessages(
                        existing = _uiState.value.messagesMap[conversationId] ?: emptyList(),
                        incoming = listOf(newMsg)
                    )
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
                    val errMsg = result.exceptionOrNull()?.localizedMessage ?: "Gagal mengunggah foto chat"
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                    try {
                        withContext(Dispatchers.Main) {
                            android.widget.Toast.makeText(ctx, "Gagal mengirim foto: $errMsg", android.widget.Toast.LENGTH_LONG).show()
                        }
                    } catch (_: Throwable) {}
                }
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error sending photo message", e)
                val errMsg = e.localizedMessage ?: "Terjadi kendala koneksi saat upload foto"
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                try {
                    withContext(Dispatchers.Main) {
                        android.widget.Toast.makeText(ctx, "Gagal mengirim foto: $errMsg", android.widget.Toast.LENGTH_LONG).show()
                    }
                } catch (_: Throwable) {}
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
        viewModelScope.launch(Dispatchers.IO) {
            com.example.data.centrifugo.CentrifugoRealtimeManager.typingEvents.collect { typingEvent ->
                withContext(Dispatchers.Main) {
                    setPartnerTyping(typingEvent.conversationId, typingEvent.isTyping)
                }
            }
        }
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
                            // Cek jika pesan ini adalah perintah reaksi emoji
                            if (messageDto.text.startsWith("__REACTION__:")) {
                                val parts = messageDto.text.split(":")
                                if (parts.size >= 3) {
                                    val targetMsgId = parts[1]
                                    val emojiVal = if (parts[2] == "NONE" || parts[2].isBlank()) null else parts[2]
                                    val currentMsgs = _uiState.value.messagesMap[convId] ?: emptyList()
                                    val updatedMsgs = currentMsgs.map {
                                        if (it.id == targetMsgId) it.copy(reaction = emojiVal) else it
                                    }
                                    _uiState.update { state ->
                                        state.copy(messagesMap = state.messagesMap + (convId to updatedMsgs))
                                    }
                                    viewModelScope.launch(Dispatchers.IO) {
                                        localChatRepo.updateMessageReaction(targetMsgId, emojiVal)
                                    }
                                }
                                return@withContext
                            }

                            // Cek jika pesan ini adalah perintah hapus untuk semua orang
                            if (messageDto.text == "__DELETED_FOR_EVERYONE__" || 
                                (messageDto.deletedForSender == true && messageDto.deletedForReceiver == true)) {
                                persistDeletedMessageId(messageDto.id)
                                com.example.util.LovyNotificationHelper.cancelNotification(getApplication(), convId)
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
                    if (SupabaseClient.isConfigured() && state.myLovyId.isNotBlank()) {
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

    /**
     * Menggabungkan dan menduplikasi pesan obrolan secara cerdas.
     * Mencegah pesan yang sama muncul 2x akibat perbedaan format ID lokal (UUID) dan server (PocketBase 15-karakter),
     * atau akibat konfirmasi SSE / polling yang datang bersamaan dengan pesan optimistik.
     */
    private fun deduplicateAndMergeMessages(
        existing: List<ChatMessage>,
        incoming: List<ChatMessage> = emptyList(),
        onObsoleteIdDetected: ((String) -> Unit)? = null
    ): List<ChatMessage> {
        val all = existing + incoming
        if (all.isEmpty()) return emptyList()

        val result = mutableListOf<ChatMessage>()
        for (candidate in all) {
            val existingIndex = result.indexOfFirst { existingMsg ->
                if (existingMsg.id == candidate.id) return@indexOfFirst true
                val pb1 = com.example.data.pocketbase.PocketBaseClient.toPbId(existingMsg.id)
                val pb2 = com.example.data.pocketbase.PocketBaseClient.toPbId(candidate.id)
                if (pb1 == pb2 && pb1.isNotBlank()) return@indexOfFirst true

                val sameConv = existingMsg.conversationId == candidate.conversationId
                val sameSender = existingMsg.isFromMe == candidate.isFromMe
                val sameText = existingMsg.text == candidate.text
                val sameImg = existingMsg.imageUrl == candidate.imageUrl
                val sameAudio = existingMsg.audioUrl == candidate.audioUrl
                val closeTime = kotlin.math.abs(existingMsg.timestamp - candidate.timestamp) <= 15000L

                sameConv && sameSender && sameText && sameImg && sameAudio && closeTime
            }

            if (existingIndex >= 0) {
                val old = result[existingIndex]
                val preferredId = if (candidate.id.length == 15) {
                    candidate.id
                } else if (old.id.length == 15) {
                    old.id
                } else {
                    candidate.id
                }

                if (old.id != preferredId) {
                    onObsoleteIdDetected?.invoke(old.id)
                }
                if (candidate.id != preferredId) {
                    onObsoleteIdDetected?.invoke(candidate.id)
                }

                val mergedMsg = old.copy(
                    id = preferredId,
                    isRead = old.isRead || candidate.isRead,
                    reaction = candidate.reaction ?: old.reaction,
                    replyToId = candidate.replyToId ?: old.replyToId,
                    replyToSender = candidate.replyToSender ?: old.replyToSender,
                    replyToText = candidate.replyToText ?: old.replyToText,
                    deletedForSender = old.deletedForSender || candidate.deletedForSender,
                    deletedForReceiver = old.deletedForReceiver || candidate.deletedForReceiver,
                    timestamp = if (old.timestamp != 0L) old.timestamp else candidate.timestamp
                )
                result[existingIndex] = mergedMsg
            } else {
                result.add(candidate)
            }
        }
        return result.sortedBy { it.timestamp }
    }

    fun syncIncomingChats() {
        if (!SupabaseClient.isConfigured()) return
        val myId = _uiState.value.myLovyId
        if (myId.isBlank()) return
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
        val grouped = recent.groupBy { normalizeConversationId(it.conversationId) }
        val currentConversations = _uiState.value.conversations.toMutableList()
        val currentMessages = _uiState.value.messagesMap.toMutableMap()

        for ((convId, dtoList) in grouped) {
            val rawConvId = dtoList.firstOrNull()?.conversationId ?: convId

            // 1. Dapatkan kandidat partnerId dari DTO terlebih dahulu (prioritas dari pengirim asli)
            val dtoPartnerId = dtoList.firstNotNullOfOrNull { dto ->
                val s = dto.senderId.trim()
                val r = dto.receiverId?.trim().orEmpty()
                if (s.isNotBlank() && !isSelfUser(s, null)) s
                else if (r.isNotBlank() && !isSelfUser(r, null)) r
                else null
            }
            val candidatePartnerId = dtoPartnerId ?: extractPartnerIdFromConvId(convId, myId).ifBlank {
                extractPartnerIdFromConvId(rawConvId, myId)
            }
            if (candidatePartnerId.isBlank() || isSelfUser(candidatePartnerId, null)) continue

            // 2. Cari profil teman secara komprehensif (chattedFriends, nearbyUsers, existing conversations)
            var partnerUser = findUserById(candidatePartnerId)
                ?: (if (dtoPartnerId != null && dtoPartnerId != candidatePartnerId) findUserById(dtoPartnerId) else null)

            if (partnerUser == null) {
                val existingConvMatch = currentConversations.find { 
                    isSameConversation(it.id, convId) || isSameUser(it.partnerId, candidatePartnerId) 
                }
                if (existingConvMatch != null && !existingConvMatch.partnerName.startsWith("Pengguna (")) {
                    partnerUser = User(
                        id = existingConvMatch.partnerId,
                        name = existingConvMatch.partnerName,
                        gender = existingConvMatch.partnerGender,
                        age = existingConvMatch.partnerAge,
                        distanceMeters = existingConvMatch.partnerDistanceMeters,
                        bio = "Teman obrolan di Lovy Chat",
                        avatarColorHex = existingConvMatch.partnerAvatarHex,
                        isOnline = existingConvMatch.isOnline,
                        avatarUrl = existingConvMatch.partnerAvatarUrl,
                        city = existingConvMatch.partnerCity ?: "Indonesia"
                    )
                }
            }

            // Utamakan ID profil asli jika sudah terdaftar
            val partnerId = partnerUser?.id?.takeIf { it.isNotBlank() && !it.startsWith("u000") } ?: candidatePartnerId

            val convDeletedTimestamp = deletedConversationTimestamps[convId]
                ?: (if (rawConvId != convId) deletedConversationTimestamps[rawConvId] else null)
                ?: 0L

            // Proses sinyal reaksi emoji jika ada
            val reactionSignals = dtoList.filter { it.text.startsWith("__REACTION__:") }
            if (reactionSignals.isNotEmpty()) {
                val existing = currentMessages[convId] ?: currentMessages[rawConvId] ?: emptyList()
                var updatedExisting = existing
                for (rDto in reactionSignals) {
                    val parts = rDto.text.split(":")
                    if (parts.size >= 3) {
                        val targetMsgId = parts[1]
                        val emojiVal = if (parts[2] == "NONE" || parts[2].isBlank()) null else parts[2]
                        updatedExisting = updatedExisting.map {
                            if (it.id == targetMsgId) it.copy(reaction = emojiVal) else it
                        }
                        viewModelScope.launch(Dispatchers.IO) {
                            localChatRepo.updateMessageReaction(targetMsgId, emojiVal)
                        }
                    }
                }
                currentMessages[convId] = updatedExisting
            }

            // Abaikan sinyal ephemeral mengetik, pesan terhapus untuk semua orang, pesan reaksi, dsb.
            val chatDtos = dtoList.filterNot { dto ->
                dto.text.startsWith("__TYPING_") || 
                dto.text == "__DELETED_FOR_EVERYONE__" ||
                dto.text.startsWith("__REACTION__:") ||
                dto.text.contains("Salam kenal dari fitur Teman Sekitar") ||
                deletedMessageIds.contains(dto.id) ||
                dto.createdAt <= convDeletedTimestamp ||
                (dto.deletedForSender == true && dto.deletedForReceiver == true) ||
                (isSenderMe(dto.senderId, dto.receiverId, myId) && dto.deletedForSender == true) ||
                (!isSenderMe(dto.senderId, dto.receiverId, myId) && dto.deletedForReceiver == true)
            }
            if (chatDtos.isEmpty()) {
                // Jika ada pesan deleted_for_everyone atau pesan yang dihapus di batch ini, bersihkan dari memori lokal
                val deletedIds = dtoList.filter { 
                    it.text == "__DELETED_FOR_EVERYONE__" || 
                    (it.deletedForSender == true && it.deletedForReceiver == true) ||
                    deletedMessageIds.contains(it.id) ||
                    it.createdAt <= convDeletedTimestamp
                }.map { it.id }.toSet()
                if (deletedIds.isNotEmpty()) {
                    val existing = currentMessages[convId] ?: currentMessages[rawConvId] ?: emptyList()
                    val filtered = existing.filterNot { deletedIds.contains(it.id) }
                    currentMessages[convId] = filtered
                    if (rawConvId != convId) currentMessages.remove(rawConvId)
                    if (filtered.isEmpty()) {
                        currentConversations.removeAll { isSameConversation(it.id, convId) || isSameUser(it.partnerId, partnerId) }
                    }
                }
                continue
            }

            val existingMap = ((currentMessages[convId] ?: emptyList()) + (if (rawConvId != convId) currentMessages[rawConvId] ?: emptyList() else emptyList())).associateBy { it.id }
            val sortedMsgs = chatDtos.sortedBy { it.createdAt }.map { dto ->
                val prev = existingMap[dto.id]
                ChatMessage(
                    id = dto.id,
                    conversationId = convId,
                    text = dto.text,
                    timestamp = dto.createdAt,
                    isFromMe = isSenderMe(dto.senderId, dto.receiverId, myId),
                    isRead = dto.isRead ?: false,
                    deletedForSender = dto.deletedForSender ?: false,
                    deletedForReceiver = dto.deletedForReceiver ?: false,
                    imageUrl = dto.imageUrl,
                    audioUrl = dto.audioUrl,
                    audioDurationSeconds = dto.audioDurationSeconds ?: 0,
                    replyToId = dto.replyToId ?: prev?.replyToId,
                    replyToSender = dto.replyToSender ?: prev?.replyToSender,
                    replyToText = dto.replyToText ?: prev?.replyToText,
                    reaction = dto.reaction ?: prev?.reaction
                )
            }

            viewModelScope.launch(Dispatchers.IO) {
                sortedMsgs.forEach { localChatRepo.saveMessage(it) }
            }

            val existingMsgs = (currentMessages[convId] ?: emptyList()) + (if (rawConvId != convId) currentMessages.remove(rawConvId) ?: emptyList() else emptyList())
            val existingIds = existingMsgs.map { it.id }.toSet()
            val newIncomingMsgs = sortedMsgs.filter { 
                !it.isFromMe && 
                !existingIds.contains(it.id) && 
                !deletedMessageIds.contains(it.id) &&
                it.timestamp > convDeletedTimestamp &&
                !it.deletedForReceiver
            }

            val mergedMsgs = deduplicateAndMergeMessages(
                existing = existingMsgs,
                incoming = sortedMsgs,
                onObsoleteIdDetected = { obsoleteId ->
                    viewModelScope.launch(Dispatchers.IO) {
                        localChatRepo.deleteMessage(obsoleteId)
                    }
                }
            ).filterNot { 
                deletedMessageIds.contains(it.id) ||
                (it.deletedForSender && it.isFromMe) ||
                (it.deletedForReceiver && !it.isFromMe) ||
                it.timestamp <= convDeletedTimestamp
            }
            currentMessages[convId] = mergedMsgs

            val lastMsg = mergedMsgs.lastOrNull()
            if (lastMsg == null) {
                currentConversations.removeAll { isSameConversation(it.id, convId) || isSameUser(it.partnerId, partnerId) }
                continue
            }

            var resolvedPartnerName: String? = null
            val isAlreadyFriend = _uiState.value.chattedFriends.any { isSameUser(it.id, partnerId) }
            val cleanPartnerName = partnerUser?.name?.trim()?.takeIf { it.isNotBlank() } 
                ?: (if (partnerId.startsWith("lovy_")) "Pengguna ($partnerId)" else "Pengguna (${partnerId.takeLast(4)})")
            resolvedPartnerName = cleanPartnerName

            val isIgnored = isIgnoredFriendRequest(partnerId, cleanPartnerName) ||
                (partnerUser != null && isIgnoredFriendRequest(partnerUser.id, partnerUser.name))
            if (isIgnored) continue

            if (partnerUser == null) {
                viewModelScope.launch(Dispatchers.IO) {
                    val cloudUser = supabaseRepo.fetchNearbyUserById(candidatePartnerId)
                        ?: (if (partnerId != candidatePartnerId) supabaseRepo.fetchNearbyUserById(partnerId) else null)
                    if (cloudUser != null && !isSelfUser(cloudUser.id, cloudUser.name)) {
                        withContext(Dispatchers.Main) {
                            _uiState.update { state ->
                                val updatedReqs = state.newFriendRequests.map { req ->
                                    if (isSameUser(req.user.id, candidatePartnerId) || isSameUser(req.user.id, partnerId)) req.copy(user = cloudUser) else req
                                }
                                val updatedConvs = state.conversations.map { c ->
                                    if (isSameConversation(c.id, convId) || isSameUser(c.partnerId, candidatePartnerId) || isSameUser(c.partnerId, partnerId)) {
                                        c.copy(
                                            partnerId = cloudUser.id,
                                            partnerName = cloudUser.name,
                                            partnerAvatarUrl = cloudUser.avatarUrl,
                                            partnerGender = cloudUser.gender,
                                            partnerAvatarHex = cloudUser.avatarColorHex,
                                            partnerCity = cloudUser.city
                                        )
                                    } else c
                                }
                                state.copy(newFriendRequests = updatedReqs, conversations = updatedConvs)
                            }
                        }
                    }
                }
            }

            if (!isAlreadyFriend && !lastMsg.isFromMe) {
                // Teman baru yang belum ada di daftar teman:
                // JANGAN dimasukkan ke obrolan! Hanya berada di menu Teman Baru menunggu disetujui atau diabaikan
                currentConversations.removeAll { isSameConversation(it.id, convId) || isSameUser(it.partnerId, partnerId) }

                val candidateUser = partnerUser ?: User(
                    id = partnerId,
                    name = cleanPartnerName,
                    gender = partnerUser?.gender ?: Gender.FEMALE,
                    age = partnerUser?.age ?: 22,
                    distanceMeters = partnerUser?.distanceMeters ?: 350,
                    bio = "Mengirimi Anda pesan di Lovy Chat",
                    avatarColorHex = partnerUser?.avatarColorHex ?: 0xFF00A86B,
                    avatarUrl = partnerUser?.avatarUrl,
                    city = partnerUser?.city ?: "Indonesia",
                    isOnline = partnerUser?.isOnline ?: false
                )

                _uiState.update { state ->
                    val existingReqs = state.newFriendRequests.filterNot { isSameUser(it.user.id, partnerId) }
                    val newReq = com.example.model.NewFriendRequest(
                        id = partnerId,
                        user = candidateUser,
                        greetingMessage = lastMsg.text,
                        timestamp = lastMsg.timestamp
                    )
                    state.copy(
                        newFriendRequests = (listOf(newReq) + existingReqs).sortedByDescending { it.timestamp }
                    )
                }

                // Tambahkan notifikasi aktivitas untuk pesan teman baru
                val currentLang = _uiState.value.language
                addActivityNotification(
                    com.example.model.ActivityNotification(
                        id = "friend_req_${partnerId}_${lastMsg.timestamp}",
                        title = com.example.util.AppStrings.notifFriendRequestTitle(currentLang),
                        message = com.example.util.AppStrings.notifFriendRequestDesc(currentLang, cleanPartnerName),
                        timestamp = lastMsg.timestamp,
                        isRead = false,
                        category = com.example.model.NotificationCategory.FRIEND,
                        senderName = cleanPartnerName,
                        translationKey = "friend_request"
                    )
                )
            } else {
                // Teman yang sudah ada di daftar teman (atau obrolan yang kita inisiasi):
                // Masuk ke obrolan seperti biasa
                val existingConvIndex = currentConversations.indexOfFirst { 
                    isSameConversation(it.id, convId) || isSameUser(it.partnerId, partnerId) 
                }
                if (existingConvIndex >= 0) {
                    val old = currentConversations[existingConvIndex]
                    val updatedName = if (old.partnerName.startsWith("Pengguna (") && !cleanPartnerName.startsWith("Pengguna (")) {
                        cleanPartnerName
                    } else if (cleanPartnerName.startsWith("Pengguna (") && !old.partnerName.startsWith("Pengguna (")) {
                        old.partnerName
                    } else cleanPartnerName

                    currentConversations[existingConvIndex] = old.copy(
                        id = convId,
                        partnerId = partnerId,
                        partnerName = updatedName,
                        partnerAvatarUrl = partnerUser?.avatarUrl ?: old.partnerAvatarUrl,
                        lastMessage = lastMsg.text,
                        lastTimestamp = lastMsg.timestamp,
                        lastMessageIsFromMe = lastMsg.isFromMe,
                        lastMessageIsRead = lastMsg.isRead
                    )
                } else if (lastMsg.timestamp > convDeletedTimestamp) {
                    val newConv = ChatConversation(
                        id = convId,
                        partnerId = partnerId,
                        partnerName = cleanPartnerName,
                        partnerAvatarHex = partnerUser?.avatarColorHex ?: 0xFF4CAF50,
                        partnerGender = partnerUser?.gender ?: Gender.FEMALE,
                        lastMessage = lastMsg.text,
                        lastTimestamp = lastMsg.timestamp,
                        unreadCount = if (!lastMsg.isFromMe && _uiState.value.activeChatId != convId) 1 else 0,
                        isOnline = partnerUser?.isOnline ?: false,
                        partnerAvatarUrl = partnerUser?.avatarUrl,
                        lastMessageIsFromMe = lastMsg.isFromMe,
                        lastMessageIsRead = lastMsg.isRead,
                        partnerAge = partnerUser?.age ?: 22,
                        partnerDistanceMeters = partnerUser?.distanceMeters ?: 350,
                        partnerCity = partnerUser?.city
                    )
                    currentConversations.add(0, newConv)
                }
            }

            if (newIncomingMsgs.isNotEmpty()) {
                val latest = newIncomingMsgs.maxByOrNull { it.timestamp }
                if (latest != null && latest.timestamp > convDeletedTimestamp) {
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

        // DEDUPLIKASI FINAL: Jamin tidak pernah ada 2 percakapan untuk akun teman yang sama
        val dedupedConvs = mutableListOf<ChatConversation>()
        for (conv in currentConversations) {
            val existingIdx = dedupedConvs.indexOfFirst { 
                isSameConversation(it.id, conv.id) || 
                isSameUser(it.partnerId, conv.partnerId) ||
                areUsersSamePerson(it.partnerId, it.partnerName, conv.partnerId, conv.partnerName)
            }
            if (existingIdx >= 0) {
                val old = dedupedConvs[existingIdx]
                recordUserAlias(old.partnerId, conv.partnerId)

                val targetConvId = old.id
                val sourceConvId = conv.id

                // Gabungkan pesan dari kedua obrolan di messagesMap
                val msgs1 = currentMessages[targetConvId] ?: emptyList()
                val msgs2 = currentMessages.remove(sourceConvId) ?: emptyList()
                val mergedMsgs = deduplicateAndMergeMessages(msgs1, msgs2)
                currentMessages[targetConvId] = mergedMsgs

                // Migrasikan pesan di Room SQLite agar tersimpan permanen di HP
                if (targetConvId != sourceConvId) {
                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            localChatRepo.migrateConversationMessages(sourceConvId, targetConvId)
                        } catch (_: Throwable) {}
                    }
                }

                val latestMsg = mergedMsgs.maxByOrNull { it.timestamp }
                val preferred = if (old.partnerName.startsWith("Pengguna (") && !conv.partnerName.startsWith("Pengguna (")) {
                    conv.copy(
                        id = normalizeConversationId(targetConvId),
                        unreadCount = old.unreadCount + conv.unreadCount,
                        lastMessage = latestMsg?.text ?: if (conv.lastTimestamp >= old.lastTimestamp) conv.lastMessage else old.lastMessage,
                        lastTimestamp = latestMsg?.timestamp ?: maxOf(conv.lastTimestamp, old.lastTimestamp)
                    )
                } else {
                    old.copy(
                        id = normalizeConversationId(targetConvId),
                        partnerAvatarUrl = conv.partnerAvatarUrl ?: old.partnerAvatarUrl,
                        unreadCount = old.unreadCount + conv.unreadCount,
                        lastMessage = latestMsg?.text ?: if (conv.lastTimestamp >= old.lastTimestamp) conv.lastMessage else old.lastMessage,
                        lastTimestamp = latestMsg?.timestamp ?: maxOf(conv.lastTimestamp, old.lastTimestamp)
                    )
                }
                dedupedConvs[existingIdx] = preferred
            } else {
                dedupedConvs.add(conv.copy(id = normalizeConversationId(conv.id)))
            }
        }

        // Satukan pesan yang mungkin tersimpan di bawah rawConvId atau canonicalConvId atau alias partner
        for (conv in dedupedConvs) {
            val rawMsgs = currentMessages[conv.id] ?: emptyList()
            val alternateKeys = currentMessages.keys.filter { key ->
                key != conv.id && (isSameConversation(key, conv.id) || areUsersSamePerson(extractPartnerIdFromConvId(key, _uiState.value.myLovyId), null, conv.partnerId, conv.partnerName))
            }
            if (alternateKeys.isNotEmpty()) {
                var merged = rawMsgs
                for (altKey in alternateKeys) {
                    val altMsgs = currentMessages.remove(altKey) ?: emptyList()
                    merged = deduplicateAndMergeMessages(merged, altMsgs)
                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            localChatRepo.migrateConversationMessages(altKey, conv.id)
                        } catch (_: Throwable) {}
                    }
                }
                currentMessages[conv.id] = merged
            }
        }

        _uiState.update {
            it.copy(
                conversations = dedupedConvs,
                messagesMap = currentMessages
            )
        }
        refreshNewFriendRequests()
    }

    override fun onCleared() {
        super.onCleared()
        heartbeatJob?.cancel()
        val myId = _uiState.value.myLovyId
        if (!_uiState.value.isGuest && myId.isNotBlank()) {
            kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
                try {
                    supabaseRepo.updateUserPresence(myId, isOnline = false)
                } catch (_: Throwable) {}
            }
        }
    }
}

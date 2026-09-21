package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockDataSource
import com.example.data.local.AppDatabase
import com.example.data.local.ChatFriendEntity
import com.example.data.local.UserProfileRepository
import com.example.data.supabase.SupabaseClient
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
    data class ChatDetail(val conversationId: String, val partnerName: String, val partnerAvatarHex: Long) : CurrentScreen
}

data class LovyChatUiState(
    val currentTab: Int = 2, // Default to Temukan (matching the screenshot)
    val currentScreen: CurrentScreen = CurrentScreen.Splash,
    val isLoggedIn: Boolean = false,
    val isGuest: Boolean = false,
    val userProfile: UserProfile = UserProfile(),
    val nearbyUsers: List<User> = MockDataSource.initialNearbyUsers,
    val chattedFriends: List<User> = emptyList(),

    val nearbyGenderFilter: Gender? = null,
    val isScanningNearby: Boolean = false,
    val conversations: List<ChatConversation> = MockDataSource.initialConversations,
    val messagesMap: Map<String, List<ChatMessage>> = MockDataSource.initialMessages,
    val oceanBottles: List<BottleMessage> = MockDataSource.oceanBottles,
    val myBottles: List<BottleMessage> = emptyList(),
    val fishedBottles: List<BottleMessage> = emptyList(),
    val fishedBottle: BottleMessage? = null,
    val isFishing: Boolean = false,
    val moments: List<MomentItem> = MockDataSource.initialMoments,
    val activeChatId: String? = null,
    val myName: String = "Pengguna Lovy",
    val myBio: String = "Menjelajahi dunia dan mencari teman baru di Lovy Chat ✨",
    val myLovyId: String = "lovy_889214",
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
    // My Moments tracking (IDs of moments created by this user)
    val myMomentIds: Set<String> = emptySet(),
    // Privacy and Location Settings
    val isNearbyVisible: Boolean = true,
    val hideExactDistance: Boolean = false,
    val showOnlineStatus: Boolean = true,
    val fcmToken: String = ""
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
        observeUserProfile()
        observeChatFriends()
        updateUserActivity()
        initFirebaseMessaging()
        // Pulihkan sesi login jika sebelumnya pengguna sudah masuk
        try {
            val savedSession = authRepo.getSavedSession()
            if (savedSession != null && savedSession.isLoggedIn) {
                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        isGuest = savedSession.isGuest,
                        myName = savedSession.displayName.ifBlank { savedSession.username },
                        myLovyId = savedSession.lovyId
                    )
                }
            }
        } catch (e: Exception) {
            Log.w("LovyChatViewModel", "Gagal memulihkan sesi login: ${e.message}")
        }
        // Coba sinkronisasi data awal jika Supabase sudah terkonfigurasi
        syncFromSupabase()
    }

    private fun observeChatFriends() {
        viewModelScope.launch {
            try {
                chatFriendDao.getAllFriendsFlow().collect { friendEntities ->
                    val friends = friendEntities.map { it.toUser() }
                    _uiState.update { it.copy(chattedFriends = friends) }
                }
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal memuat teman mengobrol", e)
            }
        }
    }

    fun saveChatFriend(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatFriendDao.insertOrUpdateFriend(ChatFriendEntity.fromUser(user))
            } catch (e: Exception) {
                Log.w("LovyChatViewModel", "Gagal menyimpan teman mengobrol", e)
            }
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
            com.google.firebase.messaging.FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && !task.result.isNullOrBlank()) {
                        val token = task.result
                        _uiState.update { it.copy(fcmToken = token) }
                        val fcmPrefs = getApplication<Application>()
                            .getSharedPreferences("lovy_fcm_prefs", android.content.Context.MODE_PRIVATE)
                        fcmPrefs.edit().putString("fcm_token", token).apply()
                        Log.d("LovyFCM", "Current FCM Token fetched: $token")
                    }
                }
        } catch (e: Throwable) {
            Log.w("LovyFCM", "Inisialisasi FCM dilewati atau belum tersedia: ${e.message}")
        }
    }

    private fun loadBlockedUsers() {
        try {
            val savedIds = prefs.getStringSet("blocked_user_ids", emptySet()) ?: emptySet()
            val savedNames = prefs.getStringSet("blocked_user_names", emptySet()) ?: emptySet()
            _uiState.update { current ->
                val filteredNearby = current.nearbyUsers.filterNot { u ->
                    savedIds.contains(u.id) || savedNames.any { n -> n.equals(u.name, ignoreCase = true) }
                }
                current.copy(
                    blockedUserIds = savedIds,
                    blockedUserNames = savedNames,
                    nearbyUsers = filteredNearby
                )
            }
        } catch (_: Throwable) {
        }
    }

    private fun loadMyMoments() {
        try {
            val savedIds = prefs.getStringSet("my_moment_ids", emptySet()) ?: emptySet()
            _uiState.update { it.copy(myMomentIds = savedIds) }
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

    private fun observeUserProfile() {
        viewModelScope.launch {
            try {
                userProfileRepo.currentProfile.collect { profile ->
                    if (profile != null) {
                        _uiState.update {
                            it.copy(
                                userProfile = profile,
                                myName = profile.displayName,
                                myBio = profile.bio,
                                myLovyId = profile.lovyId
                            )
                        }
                    } else {
                        // Seed initial profile in Room database
                        val initialProfile = UserProfile(
                            id = "current_user",
                            displayName = _uiState.value.myName,
                            bio = _uiState.value.myBio,
                            lovyId = _uiState.value.myLovyId,
                            city = "Jakarta Selatan"
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
            it.copy(
                userProfile = profile,
                myName = profile.displayName,
                myBio = profile.bio,
                myLovyId = profile.lovyId
            )
        }
        viewModelScope.launch {
            try {
                userProfileRepo.saveProfile(profile)
            } catch (e: Throwable) {
                android.util.Log.e("LovyChatViewModel", "Error saving UserProfile to Room: ${e.message}")
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
            supabaseRepo.registerOrUpdateUser(
                id = lovyId,
                name = profile.displayName.ifBlank { _uiState.value.myName },
                gender = userGender,
                bio = profile.bio,
                avatarHex = 0xFF4CAF50,
                avatarUrl = profile.profilePicture?.takeIf { it.isNotBlank() }
            )
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
                if (!remoteUsers.isNullOrEmpty()) {
                    _uiState.update { it.copy(nearbyUsers = remoteUsers) }
                    lastNearbyScanTime = System.currentTimeMillis()
                }
            }

            if (forceRefresh || now - lastBottlesSyncTime >= CACHE_DURATION_MS) {
                val remoteBottles = supabaseRepo.fetchOceanBottles()
                if (!remoteBottles.isNullOrEmpty()) {
                    _uiState.update { it.copy(oceanBottles = remoteBottles) }
                    lastBottlesSyncTime = System.currentTimeMillis()
                }
            }

            if (forceRefresh || now - lastMomentsSyncTime >= CACHE_DURATION_MS) {
                val remoteMoments = supabaseRepo.fetchMoments()
                if (!remoteMoments.isNullOrEmpty()) {
                    _uiState.update { it.copy(moments = remoteMoments) }
                    lastMomentsSyncTime = System.currentTimeMillis()
                }
            }

            syncIncomingChats()
        }
    }

    fun refreshMoments(force: Boolean = false) {
        if (_uiState.value.isGuest) return
        val now = System.currentTimeMillis()
        if (!force && now - lastMomentsSyncTime < CACHE_DURATION_MS) return
        viewModelScope.launch {
            val remoteMoments = supabaseRepo.fetchMoments()
            if (!remoteMoments.isNullOrEmpty()) {
                _uiState.update { it.copy(moments = remoteMoments) }
                lastMomentsSyncTime = System.currentTimeMillis()
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
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                isGuest = false,
                myName = finalName,
                currentScreen = CurrentScreen.Main,
                // Mode Pengguna Asli: Pisahkan dari percakapan dummy tamu agar tidak bercampur
                conversations = emptyList(),
                messagesMap = emptyMap()
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
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isGuest = false,
                    myName = finalName,
                    myLovyId = lovyId,
                    currentScreen = CurrentScreen.Main,
                    conversations = emptyList(),
                    messagesMap = emptyMap()
                )
            }
            val newProfile = UserProfile(
                id = "current_user",
                displayName = finalName,
                bio = result.bio ?: "Halo, saya pengguna baru Lovy Chat! ✨",
                lovyId = lovyId,
                gender = gender.name,
                city = "Jakarta Selatan"
            )
            saveUserProfile(newProfile)
            updateUserActivity()
            syncFromSupabase(forceRefresh = true)
            syncUserProfileToSupabase()
        }
        return result
    }

    suspend fun performLogin(username: String, password: String): AuthResult {
        val result = authRepo.login(username = username, password = password)
        if (result.success) {
            val lovyId = result.lovyId ?: _uiState.value.myLovyId
            val finalName = result.displayName ?: result.username ?: username
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isGuest = false,
                    myName = finalName,
                    myLovyId = lovyId,
                    currentScreen = CurrentScreen.Main,
                    conversations = emptyList(),
                    messagesMap = emptyMap()
                )
            }
            val existingProfile = _uiState.value.userProfile
            val updatedProfile = existingProfile.copy(
                displayName = finalName,
                lovyId = lovyId,
                gender = result.gender.name,
                bio = result.bio ?: existingProfile.bio,
                profilePicture = result.avatarUrl ?: existingProfile.profilePicture
            )
            saveUserProfile(updatedProfile)
            updateUserActivity()
            syncFromSupabase(forceRefresh = true)
            syncUserProfileToSupabase()
        }
        return result
    }

    suspend fun performGoogleLogin(googleUser: GoogleAuthHelper.GoogleUserResult): AuthResult {
        val result = authRepo.loginWithGoogle(googleUser)
        if (result.success) {
            val lovyId = result.lovyId ?: _uiState.value.myLovyId
            val finalName = result.displayName ?: googleUser.displayName
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isGuest = false,
                    myName = finalName,
                    myLovyId = lovyId,
                    currentScreen = CurrentScreen.Main,
                    conversations = emptyList(),
                    messagesMap = emptyMap()
                )
            }
            val existingProfile = _uiState.value.userProfile
            val updatedProfile = existingProfile.copy(
                displayName = finalName,
                lovyId = lovyId,
                email = googleUser.email,
                profilePicture = result.avatarUrl ?: googleUser.profilePictureUri ?: existingProfile.profilePicture,
                bio = result.bio ?: existingProfile.bio
            )
            saveUserProfile(updatedProfile)
            updateUserActivity()
            syncFromSupabase(forceRefresh = true)
            syncUserProfileToSupabase()
        }
        return result
    }

    fun loginAsGuest() {
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                isGuest = true,
                myName = "Tamu Lovy",
                currentScreen = CurrentScreen.Main,
                // Mode Tamu: Memuat percakapan dan pesan simulasi demo lokal (sandbox)
                conversations = MockDataSource.initialConversations,
                messagesMap = MockDataSource.initialMessages,
                oceanBottles = MockDataSource.oceanBottles,
                moments = MockDataSource.initialMoments
            )
        }
    }

    fun logout() {
        authRepo.clearSession()
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
                conversations = emptyList()
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

    fun updateLocationPermission(granted: Boolean) {
        _uiState.update { it.copy(hasLocationPermission = granted) }
        if (granted) {
            val app = try { getApplication<Application>() } catch (_: Throwable) { null }
            val isEnabled = com.example.util.AndroidGpsTracker.isLocationEnabled(app)
            val lastLoc = com.example.util.AndroidGpsTracker.getLastKnownLocation(app)
            _uiState.update { 
                it.copy(
                    isGpsEnabled = isEnabled,
                    currentGpsLocation = lastLoc
                ) 
            }
            refreshNearbyScan(forceRefresh = false)
        }
    }

    fun updateGpsLocation(location: com.example.util.UserGpsLocation) {
        _uiState.update { it.copy(currentGpsLocation = location) }

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
            val gpsLoc = com.example.util.AndroidGpsTracker.getLastKnownLocation(app)
            _uiState.update { it.copy(isGpsEnabled = isGpsOn, currentGpsLocation = gpsLoc ?: it.currentGpsLocation) }

            delay(600)

            // Coba ambil dari Supabase jika ada & cache sudah kadaluwarsa atau diminta paksa (hanya untuk pengguna asli)
            val remoteUsers = if (!isCacheValid && !_uiState.value.isGuest) supabaseRepo.fetchNearbyUsers() else null
            if (!remoteUsers.isNullOrEmpty()) {
                lastNearbyScanTime = System.currentTimeMillis()
                val filtered = remoteUsers.filterNot { isUserBlocked(it.id, it.name) }
                _uiState.update { it.copy(isScanningNearby = false, nearbyUsers = filtered) }
            } else {
                val currentLoc = _uiState.value.currentGpsLocation
                val sourceList = if (_uiState.value.nearbyUsers.isNotEmpty()) _uiState.value.nearbyUsers else MockDataSource.initialNearbyUsers
                val updated = sourceList
                    .filterNot { isUserBlocked(it.id, it.name) }
                    .mapIndexed { index, user ->
                    val calculatedDistance = if (currentLoc != null) {
                        // Hitung jarak dinamis berbasis koordinat GPS nyata pengguna
                        val targetLat = currentLoc.latitude + (index * 0.0018) + ((-5..5).random() * 0.0002)
                        val targetLon = currentLoc.longitude + (index * 0.0015) + ((-5..5).random() * 0.0002)
                        com.example.util.AndroidGpsTracker.calculateDistanceMeters(
                            currentLoc.latitude,
                            currentLoc.longitude,
                            targetLat,
                            targetLon
                        ).coerceAtLeast(35)
                    } else {
                        val variation = (-20..30).random()
                        (user.distanceMeters + variation).coerceAtLeast(40)
                    }
                    user.copy(distanceMeters = calculatedDistance)
                }.sortedBy { it.distanceMeters }
                _uiState.update { it.copy(isScanningNearby = false, nearbyUsers = updated) }
            }
        }
    }

    fun getCanonicalConversationId(id1: String, id2: String): String {
        val clean1 = id1.trim()
        val clean2 = id2.trim()
        if (clean1.isEmpty() && clean2.isEmpty()) return "conv_chat"
        if (clean1.isEmpty()) return "conv_$clean2"
        if (clean2.isEmpty()) return "conv_$clean1"
        val sorted = if (clean1 <= clean2) listOf(clean1, clean2) else listOf(clean2, clean1)
        return "conv_${sorted[0]}_${sorted[1]}"
    }

    fun extractPartnerIdFromConvId(convId: String, myId: String): String {
        if (!convId.startsWith("conv_")) return convId
        val content = convId.removePrefix("conv_")
        val parts = content.split("_")
        if (parts.size >= 2) {
            return parts.firstOrNull { !it.equals(myId, ignoreCase = true) } ?: parts[0]
        }
        return content
    }

    fun sayHiToUser(user: User) {
        if (isUserBlocked(user.id, user.name)) return
        recordFeatureClick()
        saveChatFriend(user)
        updateUserActivity()
        val convId = if (_uiState.value.isGuest) {
            "conv_${user.id}"
        } else {
            getCanonicalConversationId(_uiState.value.myLovyId, user.id)
        }
        val existing = _uiState.value.conversations.find { it.id == convId || it.partnerId == user.id }
        
        val greetingText = "Halo ${user.name}! Salam kenal dari fitur Teman Sekitar ya 👋"
        val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = convId,
            text = greetingText,
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )

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
                    partnerAvatarUrl = user.avatarUrl
                )
            ) + _uiState.value.conversations
        }

        _uiState.update {
            it.copy(
                conversations = updatedConversations,
                messagesMap = it.messagesMap + (convId to updatedMessages)
            )
        }

        if (_uiState.value.isGuest) {
            // Mode Tamu: Tidak pernah kirim ke Supabase, gunakan auto reply lokal
            scheduleAutoReply(convId, user.name)
        } else {
            // Mode Pengguna Asli: Kirim ke Supabase dengan sender_id dan receiver_id
            viewModelScope.launch {
                supabaseRepo.sendChatMessage(
                    message = newMsg,
                    senderId = _uiState.value.myLovyId,
                    receiverId = user.id
                )
            }
        }

        // Open chat directly
        openChat(convId, user.name, user.avatarColorHex)
    }

    fun openChat(conversationId: String, partnerName: String, partnerAvatarHex: Long) {
        recordFeatureClick()
        val conv = _uiState.value.conversations.find { it.id == conversationId }
        if (conv != null) {
            saveChatFriend(
                User(
                    id = conv.partnerId,
                    name = conv.partnerName,
                    gender = conv.partnerGender,
                    age = 22,
                    distanceMeters = 100,
                    bio = "Teman obrolan di Lovy Chat",
                    avatarColorHex = conv.partnerAvatarHex,
                    isOnline = conv.isOnline,
                    avatarUrl = conv.partnerAvatarUrl
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

        // Sinkronisasi pesan obrolan 2 arah secara langsung untuk pengguna asli
        if (!_uiState.value.isGuest) {
            val partnerId = conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
            pollChatMessages(conversationId, partnerId)
        }
    }

    fun pollChatMessages(conversationId: String, partnerId: String, forceFullSync: Boolean = false) {
        if (_uiState.value.isGuest) return
        if (!SupabaseClient.isConfigured()) return

        val myId = _uiState.value.myLovyId
        val currentMsgs = _uiState.value.messagesMap[conversationId] ?: emptyList()
        // Jika sudah ada pesan dan bukan forceFullSync, minta hanya pesan baru setelah pesan terakhir
        val sinceTimestamp = if (forceFullSync || currentMsgs.isEmpty()) 0L else (currentMsgs.maxOfOrNull { it.timestamp } ?: 0L)

        viewModelScope.launch(Dispatchers.IO) {
            val remoteMsgs = supabaseRepo.fetchChatMessages(
                conversationId = conversationId,
                currentUserId = myId,
                partnerId = partnerId,
                sinceTimestamp = sinceTimestamp
            )
            if (!remoteMsgs.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    val current = _uiState.value.messagesMap[conversationId] ?: emptyList()
                    val merged = (current + remoteMsgs)
                        .filterNot { it.deletedForSender && it.isFromMe }
                        .distinctBy { it.id }
                        .sortedBy { it.timestamp }

                    val lastMsg = merged.lastOrNull()
                    val updatedConvs = _uiState.value.conversations.map { c ->
                        if (c.id == conversationId && lastMsg != null) {
                            c.copy(lastMessage = lastMsg.text, lastTimestamp = lastMsg.timestamp)
                        } else c
                    }

                    _uiState.update {
                        it.copy(
                            conversations = updatedConvs,
                            messagesMap = it.messagesMap + (conversationId to merged)
                        )
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

        val convId = if (_uiState.value.isGuest) {
            "conv_${bottle.senderId}"
        } else {
            getCanonicalConversationId(_uiState.value.myLovyId, bottle.senderId)
        }
        val existing = _uiState.value.conversations.find { it.id == convId || it.partnerId == bottle.senderId }
        val greetingText = "Halo ${bottle.senderName}! Aku menemukan pesan botolmu: \"${bottle.content.take(30)}...\" 🍾🌊"
        val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = convId,
            text = greetingText,
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )
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

        if (_uiState.value.isGuest) {
            scheduleAutoReply(convId, bottle.senderName)
        } else {
            viewModelScope.launch {
                supabaseRepo.sendChatMessage(
                    message = newMsg,
                    senderId = _uiState.value.myLovyId,
                    receiverId = bottle.senderId
                )
            }
        }

        openChat(convId, bottle.senderName, bottle.avatarHex)
    }

    fun deleteMessageForSender(conversationId: String, messageId: String) {
        val currentMsgs = _uiState.value.messagesMap[conversationId] ?: emptyList()
        val updatedMsgs = currentMsgs.map { msg ->
            if (msg.id == messageId) {
                msg.copy(deletedForSender = true)
            } else msg
        }.filterNot { it.deletedForSender && it.isFromMe }

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

        if (!_uiState.value.isGuest) {
            viewModelScope.launch {
                supabaseRepo.markMessageDeletedForSender(messageId)
            }
        }
    }

    fun sendMessage(conversationId: String, text: String, partnerName: String) {
        if (text.isBlank()) return
        if (isUserBlocked(userName = partnerName)) return
        updateUserActivity()
        val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            text = text.trim(),
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )

        val updatedMessages = (_uiState.value.messagesMap[conversationId] ?: emptyList()) + newMsg
        val updatedConversations = _uiState.value.conversations.map {
            if (it.id == conversationId) {
                it.copy(lastMessage = text.trim(), lastTimestamp = System.currentTimeMillis())
            } else it
        }

        _uiState.update {
            it.copy(
                conversations = updatedConversations,
                messagesMap = it.messagesMap + (conversationId to updatedMessages)
            )
        }

        if (_uiState.value.isGuest) {
            // Mode Tamu: HANYA lokal, JANGAN pernah kirim ke Supabase!
            // Mesin generator jawab otomatis hanya melayani mode tamu untuk simulasi interaktif
            scheduleAutoReply(conversationId, partnerName)
        } else {
            // Mode Pengguna Asli: Sinkronisasi ke Supabase untuk obrolan 2 arah
            val conv = _uiState.value.conversations.find { it.id == conversationId }
            val partnerId = conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
            viewModelScope.launch {
                supabaseRepo.sendChatMessage(
                    message = newMsg,
                    senderId = _uiState.value.myLovyId,
                    receiverId = partnerId
                )
            }
        }
    }

    private fun scheduleAutoReply(conversationId: String, partnerName: String) {
        // Hanya aktif untuk Mode Tamu
        if (!_uiState.value.isGuest) return

        viewModelScope.launch {
            delay(2000)
            if (isUserBlocked(userName = partnerName)) return@launch
            val replyTexts = listOf(
                "Halo! Senang bisa terhubung denganmu di Lovy Chat 😊",
                "Salam kenal juga ya! Kamu lagi ada kegiatan apa hari ini?",
                "Wah asyik! Semoga harimu menyenangkan dan ceria selalu ya!",
                "Halo! Baru buka Lovy Chat nih, makasih udah menyapa ya 🙏",
                "Keren banget! Senang berkenalan denganmu!"
            )
            val replyMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                text = replyTexts.random(),
                timestamp = System.currentTimeMillis(),
                isFromMe = false
            )

            val curMsgs = _uiState.value.messagesMap[conversationId] ?: emptyList()
            val updatedConvs = _uiState.value.conversations.map {
                if (it.id == conversationId) {
                    it.copy(
                        lastMessage = replyMsg.text,
                        lastTimestamp = replyMsg.timestamp,
                        unreadCount = if (_uiState.value.activeChatId == conversationId) 0 else it.unreadCount + 1
                    )
                } else it
            }

            _uiState.update {
                it.copy(
                    conversations = updatedConvs,
                    messagesMap = it.messagesMap + (conversationId to (curMsgs + replyMsg))
                )
            }

            // PENTING: Mode Tamu tidak pernah mengirim balasan simulasi ke Supabase
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
                else -> MockDataSource.oceanBottles.first()
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
        } catch (_: Throwable) {
        }
        _uiState.update { it.copy(moments = listOf(newMoment) + it.moments, myMomentIds = newMomentIds) }

        // Simpan ke Supabase (hanya jika bukan mode tamu)
        if (!_uiState.value.isGuest) {
            viewModelScope.launch {
                supabaseRepo.sendMoment(newMoment, authorId)
            }
        }
    }

    fun deleteMoment(momentId: String) {
        recordFeatureClick()
        val newMomentIds = _uiState.value.myMomentIds - momentId
        try {
            prefs.edit().putStringSet("my_moment_ids", newMomentIds).apply()
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
                    uploadedUrl = result.getOrNull()
                }

                postMoment(content, uploadedUrl, locationTag)
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                onComplete?.invoke(true)
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
        context: android.content.Context? = null
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
                        imageUrl = photoUrl
                    )

                    val updatedMessages = (_uiState.value.messagesMap[conversationId] ?: emptyList()) + newMsg
                    val updatedConversations = _uiState.value.conversations.map {
                        if (it.id == conversationId) {
                            it.copy(lastMessage = "📷 Foto", lastTimestamp = System.currentTimeMillis())
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

                    if (_uiState.value.isGuest) {
                        // Mode Tamu: auto-reply lokal untuk foto
                        schedulePhotoAutoReply(conversationId, partnerName)
                    } else {
                        // Mode Pengguna Asli: Sinkronisasi ke Supabase
                        val conv = _uiState.value.conversations.find { it.id == conversationId }
                        val partnerId = conv?.partnerId ?: extractPartnerIdFromConvId(conversationId, _uiState.value.myLovyId)
                        supabaseRepo.sendChatMessage(
                            message = newMsg,
                            senderId = _uiState.value.myLovyId,
                            receiverId = partnerId
                        )
                    }
                } else {
                    _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
                }
            } catch (e: Exception) {
                Log.e("LovyChatViewModel", "Error sending photo message", e)
                _uiState.update { it.copy(isUploadingPhoto = false, uploadProgressText = null) }
            }
        }
    }

    private fun schedulePhotoAutoReply(conversationId: String, partnerName: String) {
        // Hanya aktif untuk Mode Tamu
        if (!_uiState.value.isGuest) return

        viewModelScope.launch {
            delay(2500)
            if (isUserBlocked(userName = partnerName)) return@launch
            val photoReplies = listOf(
                "Wah fotonya bagus banget! 😍📸",
                "Keren banget fotonya! Suka deh liatnya ✨",
                "Makasih udah berbagi fotonya ya! Bagus banget! 😊",
                "Wah menarik banget! Diambil di mana tuh fotonya? 🌸",
                "Foto yang cantik! Senang ngobrol sama kamu 👍"
            )
            val replyMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                text = photoReplies.random(),
                timestamp = System.currentTimeMillis(),
                isFromMe = false
            )

            val curMsgs = _uiState.value.messagesMap[conversationId] ?: emptyList()
            val updatedConvs = _uiState.value.conversations.map {
                if (it.id == conversationId) {
                    it.copy(
                        lastMessage = replyMsg.text,
                        lastTimestamp = replyMsg.timestamp,
                        unreadCount = if (_uiState.value.activeChatId == conversationId) 0 else it.unreadCount + 1
                    )
                } else it
            }

            _uiState.update {
                it.copy(
                    conversations = updatedConvs,
                    messagesMap = it.messagesMap + (conversationId to (curMsgs + replyMsg))
                )
            }

            // PENTING: Mode Tamu tidak mengirim balasan foto simulasi ke Supabase
        }
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
            val partnerId = extractPartnerIdFromConvId(convId, myId)
            if (partnerId.isBlank()) continue

            val sortedMsgs = dtoList.sortedBy { it.createdAt }.map { dto ->
                ChatMessage(
                    id = dto.id,
                    conversationId = convId,
                    text = dto.text,
                    timestamp = dto.createdAt,
                    isFromMe = dto.senderId.equals(myId, ignoreCase = true) || (dto.senderId.equals("me", ignoreCase = true) && !dto.receiverId.equals(myId, ignoreCase = true)),
                    deletedForSender = dto.deletedForSender,
                    deletedForReceiver = dto.deletedForReceiver,
                    imageUrl = dto.imageUrl
                )
            }

            val existingMsgs = currentMessages[convId] ?: emptyList()
            val mergedMsgs = (existingMsgs + sortedMsgs).distinctBy { it.id }.sortedBy { it.timestamp }
            currentMessages[convId] = mergedMsgs

            val lastMsg = mergedMsgs.lastOrNull() ?: continue
            val existingConvIndex = currentConversations.indexOfFirst { it.id == convId || it.partnerId == partnerId }
            if (existingConvIndex >= 0) {
                val old = currentConversations[existingConvIndex]
                currentConversations[existingConvIndex] = old.copy(
                    id = convId,
                    lastMessage = lastMsg.text,
                    lastTimestamp = lastMsg.timestamp
                )
            } else {
                val partnerUser = _uiState.value.nearbyUsers.find { it.id == partnerId }
                val newConv = ChatConversation(
                    id = convId,
                    partnerId = partnerId,
                    partnerName = partnerUser?.name ?: "Pengguna $partnerId",
                    partnerAvatarHex = partnerUser?.avatarColorHex ?: 0xFF4CAF50,
                    partnerGender = partnerUser?.gender ?: Gender.FEMALE,
                    lastMessage = lastMsg.text,
                    lastTimestamp = lastMsg.timestamp,
                    unreadCount = if (!lastMsg.isFromMe && _uiState.value.activeChatId != convId) 1 else 0,
                    isOnline = partnerUser?.isOnline ?: true,
                    partnerAvatarUrl = partnerUser?.avatarUrl
                )
                currentConversations.add(0, newConv)
            }
        }

        _uiState.update {
            it.copy(
                conversations = currentConversations,
                messagesMap = currentMessages
            )
        }
    }
}

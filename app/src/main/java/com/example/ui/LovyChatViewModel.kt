package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockDataSource
import com.example.data.supabase.SupabaseClient
import com.example.data.supabase.SupabaseRepository
import com.example.model.BottleMessage
import com.example.model.ChatConversation
import com.example.model.ChatMessage
import com.example.model.Gender
import com.example.model.MomentItem
import com.example.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

sealed interface CurrentScreen {
    object Splash : CurrentScreen
    object Login : CurrentScreen
    object Main : CurrentScreen
    object Nearby : CurrentScreen
    object Bottle : CurrentScreen
    object Moments : CurrentScreen
    object SupabaseConfig : CurrentScreen
    data class ChatDetail(val conversationId: String, val partnerName: String, val partnerAvatarHex: Long) : CurrentScreen
}

data class LovyChatUiState(
    val currentTab: Int = 2, // Default to Temukan (matching the screenshot)
    val currentScreen: CurrentScreen = CurrentScreen.Splash,
    val isLoggedIn: Boolean = false,
    val nearbyUsers: List<User> = MockDataSource.initialNearbyUsers,
    val nearbyGenderFilter: Gender? = null,
    val isScanningNearby: Boolean = false,
    val conversations: List<ChatConversation> = MockDataSource.initialConversations,
    val messagesMap: Map<String, List<ChatMessage>> = MockDataSource.initialMessages,
    val oceanBottles: List<BottleMessage> = MockDataSource.oceanBottles,
    val myBottles: List<BottleMessage> = emptyList(),
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
    val language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    val detectedGeoArea: String = "ID/MY",
    // Android Native GPS State
    val currentGpsLocation: com.example.util.UserGpsLocation? = null,
    val hasLocationPermission: Boolean = false,
    val isGpsEnabled: Boolean = true
)

class LovyChatViewModel(application: Application) : AndroidViewModel(application) {
    private val supabaseRepo = SupabaseRepository()
    private val _uiState = MutableStateFlow(LovyChatUiState())
    val uiState: StateFlow<LovyChatUiState> = _uiState.asStateFlow()

    init {
        try {
            val ctx = try { application.applicationContext } catch (_: Throwable) { null } ?: application
            SupabaseClient.init(ctx)
        } catch (_: Throwable) {
        }
        refreshSupabaseState()
        detectAndApplyGeoLanguage()
        // Coba sinkronisasi data awal jika Supabase sudah terkonfigurasi
        syncFromSupabase()
    }

    private fun detectAndApplyGeoLanguage() {
        val app = try { getApplication<Application>() } catch (_: Throwable) { null }
        val ctx = try { app?.applicationContext } catch (_: Throwable) { null } ?: app
        val detected = com.example.util.GeoLanguageDetector.detectLanguage(ctx)
        _uiState.update {
            it.copy(
                language = detected,
                detectedGeoArea = if (detected == com.example.util.AppLanguage.INDONESIAN) "Indonesia / Malaysia (ID/MY)" else "Global / Other Area"
            )
        }
    }

    fun setLanguage(language: com.example.util.AppLanguage) {
        _uiState.update { it.copy(language = language) }
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

    fun syncFromSupabase() {
        if (!SupabaseClient.isConfigured()) return
        viewModelScope.launch {
            val remoteUsers = supabaseRepo.fetchNearbyUsers()
            if (!remoteUsers.isNullOrEmpty()) {
                _uiState.update { it.copy(nearbyUsers = remoteUsers) }
            }

            val remoteBottles = supabaseRepo.fetchOceanBottles()
            if (!remoteBottles.isNullOrEmpty()) {
                _uiState.update { it.copy(oceanBottles = remoteBottles) }
            }

            val remoteMoments = supabaseRepo.fetchMoments()
            if (!remoteMoments.isNullOrEmpty()) {
                _uiState.update { it.copy(moments = remoteMoments) }
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
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                myName = if (name.isNotBlank()) name else it.myName,
                currentScreen = CurrentScreen.Main
            )
        }
    }

    fun loginAsGuest() {
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                myName = "Tamu Lovy",
                currentScreen = CurrentScreen.Main
            )
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                currentScreen = CurrentScreen.Login
            )
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(currentTab = tabIndex, currentScreen = CurrentScreen.Main) }
    }

    fun navigateTo(screen: CurrentScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun navigateBack() {
        _uiState.update { it.copy(currentScreen = CurrentScreen.Main) }
    }

    fun setNearbyGenderFilter(gender: Gender?) {
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
            refreshNearbyScan()
        }
    }

    fun updateGpsLocation(location: com.example.util.UserGpsLocation) {
        _uiState.update { it.copy(currentGpsLocation = location) }
    }

    fun refreshNearbyScan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isScanningNearby = true) }
            
            // Periksa dan ambil koordinat GPS Native terbaru jika izin ada
            val app = try { getApplication<Application>() } catch (_: Throwable) { null }
            val isGpsOn = com.example.util.AndroidGpsTracker.isLocationEnabled(app)
            val gpsLoc = com.example.util.AndroidGpsTracker.getLastKnownLocation(app)
            _uiState.update { it.copy(isGpsEnabled = isGpsOn, currentGpsLocation = gpsLoc ?: it.currentGpsLocation) }

            delay(1000)

            // Coba ambil dari Supabase jika ada
            val remoteUsers = supabaseRepo.fetchNearbyUsers()
            if (!remoteUsers.isNullOrEmpty()) {
                _uiState.update { it.copy(isScanningNearby = false, nearbyUsers = remoteUsers) }
            } else {
                val currentLoc = _uiState.value.currentGpsLocation
                val updated = MockDataSource.initialNearbyUsers.mapIndexed { index, user ->
                    val calculatedDistance = if (currentLoc != null) {
                        // Hitung jarak dinamis berbasis koordinat GPS nyata pengguna (offset simulasi bertahap)
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

    fun sayHiToUser(user: User) {
        val convId = "conv_${user.id}"
        val existing = _uiState.value.conversations.find { it.partnerId == user.id }
        
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
                if (it.id == convId) it.copy(lastMessage = greetingText, lastTimestamp = System.currentTimeMillis()) else it
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
                    isOnline = user.isOnline
                )
            ) + _uiState.value.conversations
        }

        _uiState.update {
            it.copy(
                conversations = updatedConversations,
                messagesMap = it.messagesMap + (convId to updatedMessages)
            )
        }

        // Kirim ke Supabase di background jika tersambung
        viewModelScope.launch {
            supabaseRepo.sendChatMessage(newMsg)
        }

        // Open chat directly
        openChat(convId, user.name, user.avatarColorHex)

        // Schedule auto response
        scheduleAutoReply(convId, user.name)
    }

    fun openChat(conversationId: String, partnerName: String, partnerAvatarHex: Long) {
        _uiState.update {
            val updatedConvs = it.conversations.map { conv ->
                if (conv.id == conversationId) conv.copy(unreadCount = 0) else conv
            }
            it.copy(
                conversations = updatedConvs,
                activeChatId = conversationId,
                currentScreen = CurrentScreen.ChatDetail(conversationId, partnerName, partnerAvatarHex)
            )
        }

        // Sinkronisasi pesan obrolan jika ada di Supabase
        viewModelScope.launch {
            val remoteMsgs = supabaseRepo.fetchChatMessages(conversationId)
            if (!remoteMsgs.isNullOrEmpty()) {
                val current = _uiState.value.messagesMap[conversationId] ?: emptyList()
                val merged = (current + remoteMsgs).distinctBy { it.id }.sortedBy { it.timestamp }
                _uiState.update {
                    it.copy(messagesMap = it.messagesMap + (conversationId to merged))
                }
            }
        }
    }

    fun openChatWithBottleSender(bottle: BottleMessage) {
        val convId = "conv_${bottle.senderId}"
        val existing = _uiState.value.conversations.find { it.partnerId == bottle.senderId }
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
                if (it.id == convId) it.copy(lastMessage = greetingText, lastTimestamp = System.currentTimeMillis()) else it
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
                    isOnline = true
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

        viewModelScope.launch {
            supabaseRepo.sendChatMessage(newMsg)
        }

        openChat(convId, bottle.senderName, bottle.avatarHex)
        scheduleAutoReply(convId, bottle.senderName)
    }

    fun sendMessage(conversationId: String, text: String, partnerName: String) {
        if (text.isBlank()) return
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

        // Sinkronisasi ke Supabase
        viewModelScope.launch {
            supabaseRepo.sendChatMessage(newMsg)
        }

        scheduleAutoReply(conversationId, partnerName)
    }

    private fun scheduleAutoReply(conversationId: String, partnerName: String) {
        viewModelScope.launch {
            delay(2000)
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

            // Simpan balasan ke Supabase
            supabaseRepo.sendChatMessage(replyMsg)
        }
    }

    fun throwBottle(content: String): Boolean {
        if (content.isBlank()) return false
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
        _uiState.update {
            it.copy(
                myBottles = listOf(newBottle) + it.myBottles,
                oceanBottles = listOf(newBottle) + it.oceanBottles
            )
        }

        // Sinkronisasi ke Supabase
        viewModelScope.launch {
            supabaseRepo.sendBottle(newBottle)
        }

        return true
    }

    fun fishBottle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isFishing = true, fishedBottle = null) }
            
            // Coba ambil botol terbaru dari Supabase
            val remoteBottles = supabaseRepo.fetchOceanBottles()
            if (!remoteBottles.isNullOrEmpty()) {
                _uiState.update { it.copy(oceanBottles = remoteBottles) }
            }

            delay(1200)
            val available = _uiState.value.oceanBottles.filter { !it.isFromMe }
            val chosen = if (available.isNotEmpty()) available.random() else MockDataSource.oceanBottles.first()
            _uiState.update { it.copy(isFishing = false, fishedBottle = chosen) }
        }
    }

    fun dismissFishedBottle() {
        _uiState.update { it.copy(fishedBottle = null) }
    }

    fun toggleLikeMoment(momentId: String) {
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

    fun postMoment(content: String) {
        if (content.isBlank()) return
        val newMoment = MomentItem(
            id = UUID.randomUUID().toString(),
            authorName = _uiState.value.myName,
            authorAvatarHex = 0xFF00A86B,
            timeAgo = "Baru saja",
            content = content.trim(),
            likesCount = 0,
            isLiked = false,
            commentsCount = 0
        )
        _uiState.update { it.copy(moments = listOf(newMoment) + it.moments) }

        // Simpan ke Supabase
        viewModelScope.launch {
            supabaseRepo.sendMoment(newMoment, "me")
        }
    }
}

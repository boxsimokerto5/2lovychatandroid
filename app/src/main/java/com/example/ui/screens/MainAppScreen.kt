package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.User
import com.example.ui.CurrentScreen
import com.example.ui.LovyChatViewModel
import com.example.ui.components.IronSourceBannerView
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralMedium

@Composable
fun MainAppScreen(
    viewModel: LovyChatViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle back button when on child screens
    BackHandler(enabled = uiState.currentScreen !is CurrentScreen.Main && uiState.currentScreen !is CurrentScreen.Login && uiState.currentScreen !is CurrentScreen.Splash) {
        viewModel.navigateBack()
    }

    when (val screen = uiState.currentScreen) {
        is CurrentScreen.Splash -> {
            SplashScreen(
                language = uiState.language,
                onPermissionResult = { granted -> viewModel.updateLocationPermission(granted) },
                onSplashFinished = { viewModel.onSplashFinished() }
            )
        }
        is CurrentScreen.Login -> {
            LoginScreen(
                language = uiState.language,
                detectedGeoArea = uiState.detectedGeoArea,
                isLocalMode = uiState.isLocalLanguageMode,
                onLanguageChange = { viewModel.setLanguage(it) },
                onPerformLogin = { username, password -> viewModel.performLogin(username, password) },
                onPerformRegister = { username, password, gender -> viewModel.performRegister(username, password, gender) },
                onPerformGoogleLogin = { googleUser -> viewModel.performGoogleLogin(googleUser) },
                onGuestLogin = { viewModel.loginAsGuest() }
            )
        }
        is CurrentScreen.Nearby -> {
            NearbyScreen(
                users = uiState.nearbyUsers,
                selectedGenderFilter = uiState.nearbyGenderFilter,
                selectedOnlyOnlineFilter = uiState.nearbyOnlyOnlineFilter,
                isScanning = uiState.isScanningNearby,
                isExpanded = uiState.isNearbyExpanded,
                nearbyExpansionTier = uiState.nearbyExpansionTier,
                currentGpsLocation = uiState.currentGpsLocation,
                hasLocationPermission = uiState.hasLocationPermission,
                isGpsEnabled = uiState.isGpsEnabled,
                hideExactDistance = uiState.hideExactDistance,
                language = uiState.language,
                isUserBlocked = { id, name -> viewModel.isUserBlocked(id, name) },
                onBlockUser = { viewModel.blockUser(it.id, it.name) },
                onUnblockUser = { viewModel.unblockUser(it.id, it.name) },
                moments = uiState.moments,
                onPermissionResult = { granted -> viewModel.updateLocationPermission(granted) },
                onBack = { viewModel.navigateBack() },
                onFilterChange = { viewModel.setNearbyGenderFilter(it) },
                onOnlyOnlineFilterChange = { viewModel.setNearbyOnlyOnlineFilter(it) },
                onRefreshScan = { viewModel.refreshNearbyScan(forceRefresh = true) },
                onSayHi = { viewModel.sayHiToUser(it) },
                onExpandNearby = { viewModel.expandNearbyUsers() }
            )
        }
        is CurrentScreen.Bottle -> {
            BottleScreen(
                fishedBottles = uiState.fishedBottles,
                myBottles = uiState.myBottles,
                oceanBottles = uiState.oceanBottles,
                fishedBottle = uiState.fishedBottle,
                isFishing = uiState.isFishing,
                onBack = { viewModel.navigateBack() },
                onThrowBottle = { viewModel.throwBottle(it) },
                onFishBottle = { viewModel.fishBottle() },
                onDismissFishedBottle = { viewModel.dismissFishedBottle() },
                onReleaseFishedBottle = { viewModel.returnFishedBottleToOcean(it) },
                onReplyBottle = { viewModel.openChatWithBottleSender(it) }
            )
        }
        is CurrentScreen.Moments -> {
            MomentsScreen(
                moments = uiState.moments,
                myMomentIds = uiState.myMomentIds,
                currentUserId = uiState.myLovyId,
                currentUserName = uiState.myName,
                currentGpsLocation = uiState.currentGpsLocation,
                onBack = { viewModel.navigateBack() },
                onToggleLike = { viewModel.toggleLikeMoment(it) },
                onPostMoment = { viewModel.postMoment(it) },
                onPostMomentWithDetails = { content, img, loc ->
                    viewModel.postMoment(content, img, loc)
                },
                onPostMomentWithPhotoUri = { content, uri, loc ->
                    viewModel.postMomentWithPhoto(content, uri, loc)
                },
                onDeleteMoment = { momentId ->
                    viewModel.deleteMoment(momentId)
                },
                onRefresh = {
                    viewModel.refreshMoments(force = true)
                },
                isUploadingPhoto = uiState.isUploadingPhoto,
                uploadProgressText = uiState.uploadProgressText
            )
        }
        is CurrentScreen.SupabaseConfig -> {
            SupabaseConfigScreen(
                currentUrl = uiState.supabaseUrl,
                currentAnonKey = uiState.supabaseAnonKey,
                isConnected = uiState.isSupabaseConnected,
                connectionStatusMessage = uiState.connectionStatusMessage,
                isTestingConnection = uiState.isTestingConnection,
                onBack = { viewModel.navigateBack() },
                onSaveCredentials = { url, key -> viewModel.saveSupabaseCredentials(url, key) },
                onTestConnection = { viewModel.testSupabaseConnection() },
                onClearCredentials = { viewModel.clearSupabaseCredentials() }
            )
        }
        is CurrentScreen.ChatDetail -> {
            val messages = uiState.messagesMap[screen.conversationId] ?: emptyList()
            val conv = uiState.conversations.find { it.id == screen.conversationId }
            val partnerUser = uiState.nearbyUsers.find {
                it.name.equals(screen.partnerName, ignoreCase = true) || it.id == conv?.partnerId
            }
            val partnerMoments = uiState.moments.filter {
                it.authorName.equals(screen.partnerName, ignoreCase = true) && !it.isDeleted
            }
            ChatDetailScreen(
                conversationId = screen.conversationId,
                partnerName = screen.partnerName,
                partnerAvatarHex = screen.partnerAvatarHex,
                partnerAvatarUrl = conv?.partnerAvatarUrl,
                partnerBio = partnerUser?.bio ?: "Senang berteman dan mencari cerita seru di Lovy Chat ✨",
                partnerCity = partnerUser?.city ?: "Jakarta Selatan",
                partnerDistance = partnerUser?.formattedDistance ?: "500m",
                partnerGender = partnerUser?.gender ?: conv?.partnerGender ?: com.example.model.Gender.FEMALE,
                partnerAge = partnerUser?.age ?: 22,
                partnerMoments = partnerMoments,
                onToggleLikeMoment = { momentId -> viewModel.toggleLikeMoment(momentId) },
                messages = messages,
                onBack = { viewModel.navigateBack() },
                onSendMessage = { text ->
                    viewModel.sendMessage(screen.conversationId, text, screen.partnerName)
                },
                onSendPhotoMessage = { uri, caption ->
                    viewModel.sendPhotoMessage(screen.conversationId, uri, screen.partnerName, caption)
                },
                isUploadingPhoto = uiState.isUploadingPhoto,
                uploadProgressText = uiState.uploadProgressText,
                onDeleteMessageForSender = { messageId ->
                    viewModel.deleteMessageForSender(screen.conversationId, messageId)
                },
                onPollMessages = {
                    val partnerId = conv?.partnerId ?: viewModel.extractPartnerIdFromConvId(screen.conversationId, uiState.myLovyId)
                    viewModel.pollChatMessages(screen.conversationId, partnerId)
                },
                isPartnerTyping = uiState.typingMap[screen.conversationId] == true,
                onUserTyping = { isTyping ->
                    val partnerId = conv?.partnerId ?: viewModel.extractPartnerIdFromConvId(screen.conversationId, uiState.myLovyId)
                    viewModel.onUserTyping(screen.conversationId, partnerId, isTyping)
                }
            )
        }
        is CurrentScreen.UserProfile -> {
            UserProfileScreen(
                userProfile = uiState.userProfile,
                onBack = { viewModel.navigateBack() },
                onSaveProfile = { profile ->
                    viewModel.saveUserProfile(profile)
                },
                isUploadingPhoto = uiState.isUploadingPhoto,
                uploadProgressText = uiState.uploadProgressText,
                onUploadPhoto = { uri ->
                    viewModel.uploadProfilePhoto(uri)
                },
                onClearPhoto = {
                    viewModel.clearProfilePhoto()
                },
                currentGpsLocation = uiState.currentGpsLocation,
                hasLocationPermission = uiState.hasLocationPermission,
                isGpsEnabled = uiState.isGpsEnabled,
                onRefreshLocation = { viewModel.refreshLocationFromGps() },
                onPermissionResult = { granted -> viewModel.updateLocationPermission(granted) }
            )
        }
        is CurrentScreen.Main -> {
            Scaffold(
                bottomBar = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // ironSource Banner Ad location - merapat ke frame bottom navigation bar
                        IronSourceBannerView(applyNavigationBarsPadding = false)
                        LovyBottomNavigationBar(
                            selectedTab = uiState.currentTab,
                            language = uiState.language,
                            onSelectTab = { viewModel.selectTab(it) }
                        )
                    }
                },
                modifier = modifier.fillMaxSize()
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (uiState.currentTab) {
                        0 -> ChatsTabScreen(
                            conversations = uiState.conversations,
                            onOpenChat = { conv ->
                                viewModel.openChat(conv.id, conv.partnerName, conv.partnerAvatarHex)
                            },
                            onStartNewChat = {
                                viewModel.refreshNearbyScan(forceRefresh = false)
                                viewModel.navigateTo(CurrentScreen.Nearby)
                            },
                            onRefresh = {
                                viewModel.syncIncomingChats()
                            }
                        )
                        1 -> {
                            val friendsList = remember(uiState.isGuest, uiState.chattedFriends, uiState.nearbyUsers, uiState.conversations) {
                                val baseList = if (uiState.isGuest) {
                                    // Mode Tamu: Tampilkan daftar demo agar tamu bisa menjelajahi UI
                                    val chattedIds = uiState.chattedFriends.map { it.id }.toSet()
                                    uiState.chattedFriends + uiState.nearbyUsers.filterNot { it.id in chattedIds }
                                } else {
                                    // Mode Asli: HANYA tampilkan kontak nyata yang pernah diajak mengobrol atau berteman (tanpa user dummy)
                                    uiState.chattedFriends.filterNot { viewModel.isDummyFriend(it.id, it.name) }
                                }
                                // Sinkronkan status online terkini dari radar pengguna sekitar dan obrolan
                                val onlineIds = uiState.nearbyUsers.filter { it.isOnline }.map { it.id }.toSet()
                                val onlinePartnerIds = uiState.conversations.filter { it.isOnline }.map { it.partnerId }.toSet()
                                val onlineNames = (uiState.nearbyUsers.filter { it.isOnline }.map { it.name.lowercase() } +
                                        uiState.conversations.filter { it.isOnline }.map { it.partnerName.lowercase() }).toSet()

                                baseList.map { friend ->
                                    val isNowOnline = friend.isOnline ||
                                            friend.id in onlineIds ||
                                            friend.id in onlinePartnerIds ||
                                            friend.name.lowercase() in onlineNames
                                    if (isNowOnline != friend.isOnline) friend.copy(isOnline = isNowOnline) else friend
                                }.sortedWith(
                                    compareByDescending<User> { it.isFavorite }
                                        .thenByDescending { it.isOnline }
                                        .thenBy { it.name.lowercase() }
                                )
                            }
                            FriendsTabScreen(
                                friends = friendsList,
                                onSelectFriend = { user ->
                                    viewModel.sayHiToUser(user)
                                },
                                onNavigateToNearby = {
                                    viewModel.refreshNearbyScan(forceRefresh = false)
                                    viewModel.navigateTo(CurrentScreen.Nearby)
                                },
                                onDeleteFriend = { user ->
                                    viewModel.deleteChatFriend(user.id, user.name)
                                },
                                onClearAllFriends = {
                                    viewModel.clearAllFriends()
                                },
                                onToggleFavorite = { user ->
                                    viewModel.toggleFavoriteFriend(user)
                                }
                            )
                        }
                        2 -> DiscoverTabScreen(
                            language = uiState.language,
                            onNavigateToNearby = {
                                viewModel.refreshNearbyScan(forceRefresh = false)
                                viewModel.navigateTo(CurrentScreen.Nearby)
                            },
                            onNavigateToBottle = {
                                viewModel.navigateTo(CurrentScreen.Bottle)
                            },
                            onNavigateToMoments = {
                                viewModel.refreshMoments(force = false)
                                viewModel.navigateTo(CurrentScreen.Moments)
                            }
                        )
                        3 -> ProfileTabScreen(
                            myName = uiState.myName,
                            myBio = uiState.myBio,
                            myLovyId = uiState.myLovyId,
                            isGuest = uiState.isGuest,
                            profilePicture = uiState.userProfile.profilePicture,
                            isSupabaseConnected = uiState.isSupabaseConnected,
                            language = uiState.language,
                            detectedGeoArea = uiState.detectedGeoArea,
                            isLocalMode = uiState.isLocalLanguageMode,
                            isNearbyVisible = uiState.isNearbyVisible,
                            hideExactDistance = uiState.hideExactDistance,
                            showOnlineStatus = uiState.showOnlineStatus,
                            hasLocationPermission = uiState.hasLocationPermission,
                            isGpsEnabled = uiState.isGpsEnabled,
                            onToggleNearbyVisible = { viewModel.setNearbyVisible(it) },
                            onToggleHideExactDistance = { viewModel.setHideExactDistance(it) },
                            onToggleShowOnlineStatus = { viewModel.setShowOnlineStatus(it) },
                            onLocationPermissionChanged = { viewModel.updateLocationPermission(it) },
                            onLanguageChange = { viewModel.setLanguage(it) },
                            onNavigateToUserProfile = {
                                viewModel.navigateTo(CurrentScreen.UserProfile)
                            },
                            onNavigateToBottle = {
                                viewModel.navigateTo(CurrentScreen.Bottle)
                            },
                            onNavigateToMoments = {
                                viewModel.navigateTo(CurrentScreen.Moments)
                            },
                            blockedUserNames = uiState.blockedUserNames,
                            onUnblockUser = { userName ->
                                viewModel.unblockUser(userId = "", userName = userName)
                            },
                            onLogout = {
                                viewModel.logout()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LovyBottomNavigationBar(
    selectedTab: Int,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onSelectTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = NeutralBorder, thickness = 0.6.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LovyBottomNavItem(
                index = 0,
                label = com.example.util.AppStrings.tabChat(language),
                icon = Icons.Default.ChatBubbleOutline,
                isSelected = selectedTab == 0,
                onClick = { onSelectTab(0) },
                testTag = "nav_tab_chats"
            )

            LovyBottomNavItem(
                index = 1,
                label = com.example.util.AppStrings.tabFriends(language),
                icon = Icons.Default.PeopleOutline,
                isSelected = selectedTab == 1,
                onClick = { onSelectTab(1) },
                testTag = "nav_tab_friends"
            )

            LovyBottomNavItem(
                index = 2,
                label = com.example.util.AppStrings.tabDiscover(language),
                icon = Icons.Default.Explore,
                isSelected = selectedTab == 2,
                hasAccentDot = true,
                onClick = { onSelectTab(2) },
                testTag = "nav_tab_discover"
            )

            LovyBottomNavItem(
                index = 3,
                label = com.example.util.AppStrings.tabProfile(language),
                icon = Icons.Default.PersonOutline,
                isSelected = selectedTab == 3,
                onClick = { onSelectTab(3) },
                testTag = "nav_tab_me"
            )
        }
    }
}

@Composable
fun LovyBottomNavItem(
    index: Int,
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    hasAccentDot: Boolean = false,
    onClick: () -> Unit,
    testTag: String
) {
    val activeColor = EmeraldGreen
    val inactiveColor = Color(0xFF8F9CA8)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) activeColor else inactiveColor,
                modifier = Modifier.size(24.dp)
            )

            if (hasAccentDot && isSelected) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(activeColor)
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) activeColor else inactiveColor
        )
    }
}

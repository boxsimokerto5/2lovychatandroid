package com.example.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.example.ui.components.IronSourceBannerView
import com.example.ui.components.LevelPlayNativeAdCard
import com.example.ui.components.LovyAvatar
import com.example.ui.components.NearbyRadarView
import com.example.ui.components.UserProfileBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.model.Gender
import com.example.model.User
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground
import com.example.util.AdManager
import com.example.util.AppStrings

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyScreen(
    users: List<User>,
    selectedGenderFilter: Gender?,
    selectedOnlyOnlineFilter: Boolean = false,
    isScanning: Boolean,
    isExpanded: Boolean = false,
    nearbyExpansionTier: Int = 0,
    currentGpsLocation: com.example.util.UserGpsLocation? = null,
    hasLocationPermission: Boolean = false,
    isGpsEnabled: Boolean = true,
    hideExactDistance: Boolean = false,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    isUserBlocked: (String, String) -> Boolean = { _, _ -> false },
    onBlockUser: (User) -> Unit = {},
    onUnblockUser: (User) -> Unit = {},
    onReportUser: ((User, String, String, Boolean) -> Unit)? = null,
    moments: List<com.example.model.MomentItem> = emptyList(),
    momentComments: Map<String, List<com.example.model.MomentComment>> = emptyMap(),
    onAddComment: ((momentId: String, text: String) -> Unit)? = null,
    onToggleLikeMoment: ((String) -> Unit)? = null,
    onPermissionResult: (Boolean) -> Unit = {},
    onBack: () -> Unit,
    onFilterChange: (Gender?) -> Unit,
    onOnlyOnlineFilterChange: (Boolean) -> Unit = {},
    onRefreshScan: () -> Unit,
    onSayHi: (User) -> Unit,
    onExpandNearby: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedUserForProfile by remember { mutableStateOf<User?>(null) }
    var viewingAvatarPhoto by remember { mutableStateOf<Pair<String, String>?>(null) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        val granted = fineGranted || coarseGranted
        onPermissionResult(granted)
        if (granted) {
            onRefreshScan()
        }
    }

    val isPermissionGrantedInitially = remember {
        val fineCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        fineCheck == PackageManager.PERMISSION_GRANTED || coarseCheck == PackageManager.PERMISSION_GRANTED
    }

    var showLocationDisclosure by remember {
        mutableStateOf(!hasLocationPermission && !isPermissionGrantedInitially)
    }

    if (showLocationDisclosure) {
        com.example.ui.components.PermissionDisclosureDialog(
            type = com.example.ui.components.DisclosureType.LOCATION,
            language = language,
            onConfirm = {
                showLocationDisclosure = false
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            },
            onDismiss = {
                showLocationDisclosure = false
            }
        )
    }

    LaunchedEffect(Unit) {
        val fineCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fineCheck == PackageManager.PERMISSION_GRANTED || coarseCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionResult(true)
            onRefreshScan()
        }
    }

    val filteredUsers = remember(users, selectedGenderFilter, selectedOnlyOnlineFilter, isUserBlocked) {
        val unblocked = users.filterNot { isUserBlocked(it.id, it.name) }
        val genderFiltered = if (selectedGenderFilter == null) unblocked
        else unblocked.filter { it.gender == selectedGenderFilter }
        if (selectedOnlyOnlineFilter) {
            genderFiltered.filter { it.isOnline }
        } else {
            genderFiltered
        }
    }

    val displayedLimit = when (nearbyExpansionTier) {
        0 -> 12
        1 -> 30
        2 -> 45
        3 -> 70
        4 -> 100
        else -> 125
    }

    val displayedUsers = remember(filteredUsers, nearbyExpansionTier) {
        filteredUsers.take(displayedLimit)
    }

    val isFullyExpanded = nearbyExpansionTier >= 5 || displayedUsers.size >= filteredUsers.size
    val hasHiddenUsers = !isFullyExpanded && filteredUsers.size > displayedUsers.size
    val nextTargetLimit = when (nearbyExpansionTier) {
        0 -> minOf(30, filteredUsers.size)
        1 -> minOf(45, filteredUsers.size)
        2 -> minOf(70, filteredUsers.size)
        3 -> minOf(100, filteredUsers.size)
        else -> minOf(125, filteredUsers.size)
    }
    val hiddenCount = (nextTargetLimit - displayedUsers.size).coerceAtLeast(0)
    var isRadarView by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = com.example.util.AppStrings.nearbyTitle(language),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("nearby_btn_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = com.example.util.AppStrings.btnBack(language),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isRadarView = !isRadarView },
                        modifier = Modifier.testTag("nearby_btn_toggle_view")
                    ) {
                        Icon(
                            imageVector = if (isRadarView) Icons.AutoMirrored.Filled.FormatListBulleted else Icons.Default.Radar,
                            contentDescription = if (isRadarView) com.example.util.AppStrings.nearbyUserList(language) else com.example.util.AppStrings.nearbyInteractiveRadar(language),
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onRefreshScan,
                        modifier = Modifier.testTag("nearby_btn_refresh")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = com.example.util.AppStrings.nearbyScanAgain(language),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldGreen
                )
            )
        },
        bottomBar = {
            IronSourceBannerView(applyNavigationBarsPadding = true)
        },
        containerColor = ScreenBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Scanning progress
            AnimatedVisibility(visible = isScanning) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = EmeraldGreen,
                    trackColor = EmeraldGreenLight
                )
            }

            // Status Lokasi GPS Terkini Pengguna (Otomatis & Hemat Baterai)
            val detectedCity = currentGpsLocation?.cityName?.takeIf { it.isNotBlank() } ?: com.example.util.AppStrings.profileGpsDetecting(language)
            Surface(
                color = Color(0xFFF1F8E9),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val fineCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        val coarseCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        if (fineCheck != PackageManager.PERMISSION_GRANTED && coarseCheck != PackageManager.PERMISSION_GRANTED) {
                            showLocationDisclosure = true
                        } else {
                            onRefreshScan()
                        }
                    }
                    .testTag("bar_nearby_current_gps")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = com.example.util.AppStrings.radarCenterLabel(language),
                            fontSize = 11.5.sp,
                            color = NeutralMedium
                        )
                        Text(
                            text = detectedCity,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark,
                            maxLines = 1
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 6.dp)
                    ) {
                        Text(
                            text = if (isScanning) com.example.util.AppStrings.nearbySearchingSignal(language) else com.example.util.AppStrings.btnRefreshGps(language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmeraldGreen
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = com.example.util.AppStrings.btnRefreshGps(language),
                            tint = EmeraldGreen,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            // Baris 1: Toggle Tab (Radar vs. Daftar) - Dipadatkan & Dinaikkan ke atas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isRadarView) EmeraldGreen else Color.Transparent)
                            .clickable { isRadarView = true }
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                            .testTag("tab_mode_radar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Radar,
                                contentDescription = null,
                                tint = if (isRadarView) Color.White else EmeraldGreen,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = com.example.util.AppStrings.nearbyInteractiveRadar(language),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isRadarView) Color.White else EmeraldGreen
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (!isRadarView) EmeraldGreen else Color.Transparent)
                            .clickable { isRadarView = false }
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                            .testTag("tab_mode_list"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                                contentDescription = null,
                                tint = if (!isRadarView) Color.White else EmeraldGreen,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = com.example.util.AppStrings.nearbyUserList(language),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isRadarView) Color.White else EmeraldGreen
                            )
                        }
                    }
                }
            }

            // Baris 2: Filter Chips Bar - Horizontal scroll lancar, padding rapat, tidak terjepit
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = NeutralMedium,
                    modifier = Modifier.size(16.dp)
                )

                FilterChip(
                    selected = selectedGenderFilter == null,
                    onClick = { onFilterChange(null) },
                    label = { 
                        Text(
                            text = com.example.util.AppStrings.filterAll(language),
                            maxLines = 1,
                            softWrap = false,
                            fontSize = 11.5.sp
                        ) 
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldGreen,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_all")
                )

                FilterChip(
                    selected = selectedGenderFilter == Gender.FEMALE,
                    onClick = { onFilterChange(Gender.FEMALE) },
                    label = { 
                        Text(
                            text = com.example.util.AppStrings.filterFemale(language),
                            maxLines = 1,
                            softWrap = false,
                            fontSize = 11.5.sp
                        ) 
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Female,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFEC407A),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_female")
                )

                FilterChip(
                    selected = selectedGenderFilter == Gender.MALE,
                    onClick = { onFilterChange(Gender.MALE) },
                    label = { 
                        Text(
                            text = com.example.util.AppStrings.filterMale(language),
                            maxLines = 1,
                            softWrap = false,
                            fontSize = 11.5.sp
                        ) 
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Male,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1976D2),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_male")
                )

                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp)
                        .background(Color(0xFFE0E0E0))
                )

                FilterChip(
                    selected = selectedOnlyOnlineFilter,
                    onClick = { onOnlyOnlineFilterChange(!selectedOnlyOnlineFilter) },
                    label = { 
                        Text(
                            text = "🟢 ${com.example.util.AppStrings.nearbyOnlineOnlyFilter(language)}",
                            maxLines = 1,
                            softWrap = false,
                            fontSize = 11.5.sp,
                            fontWeight = if (selectedOnlyOnlineFilter) FontWeight.Bold else FontWeight.Medium
                        ) 
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2E7D32),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_online_only")
                )
            }

            if (isRadarView) {
                // Tampilan Radar Interaktif
                NearbyRadarView(
                    users = displayedUsers,
                    totalNearbyCount = filteredUsers.size,
                    isExpanded = isExpanded,
                    nearbyExpansionTier = nearbyExpansionTier,
                    hideExactDistance = hideExactDistance,
                    onSayHi = onSayHi,
                    onUserClick = { user -> selectedUserForProfile = user },
                    onExpandNearby = onExpandNearby,
                    language = language,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                // Hint radar text
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    val radarInfo = if (selectedOnlyOnlineFilter) {
                        com.example.util.AppStrings.nearbyStatusOnlineText(language, displayedUsers.size, filteredUsers.size, hasHiddenUsers)
                    } else {
                        com.example.util.AppStrings.nearbyStatusAllText(language, displayedUsers.size, filteredUsers.size, !hasHiddenUsers)
                    }
                    Text(
                        text = radarInfo,
                        fontSize = 12.sp,
                        color = NeutralMedium
                    )
                }

                // User list
                if (displayedUsers.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "📡", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (selectedOnlyOnlineFilter) {
                                    com.example.util.AppStrings.nearbyEmptyOnlineTitle(language)
                                } else {
                                    com.example.util.AppStrings.nearbyEmptyGeneralTitle(language)
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (selectedOnlyOnlineFilter) {
                                    com.example.util.AppStrings.nearbyEmptyOnlineDesc(language)
                                } else {
                                    com.example.util.AppStrings.nearbyEmptyGeneralDesc(language)
                                },
                                fontSize = 13.sp,
                                color = NeutralMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onRefreshScan,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = com.example.util.AppStrings.nearbyScanNow(language),
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                if (displayedUsers.isEmpty()) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp, horizontal = 24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Radar,
                                contentDescription = null,
                                tint = EmeraldGreen.copy(alpha = 0.6f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = if (selectedOnlyOnlineFilter) {
                                    com.example.util.AppStrings.nearbyEmptyOnlineTitle(language)
                                } else {
                                    com.example.util.AppStrings.nearbyEmptyGeneralTitle(language)
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (selectedOnlyOnlineFilter) {
                                    com.example.util.AppStrings.nearbyEmptyOnlineDesc(language)
                                } else {
                                    com.example.util.AppStrings.nearbyEmptyGeneralDesc(language)
                                },
                                fontSize = 13.sp,
                                color = NeutralMedium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Button(
                                onClick = onRefreshScan,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(com.example.util.AppStrings.nearbyScanAgain(language), fontSize = 13.sp)
                            }
                        }
                    }
                } else {
                    displayedUsers.forEachIndexed { index, user ->
                    item(key = user.id) {
                        NearbyUserCard(
                            user = user,
                            hideExactDistance = hideExactDistance,
                            language = language,
                            onAvatarClick = {
                                if (!user.avatarUrl.isNullOrBlank()) {
                                    viewingAvatarPhoto = Pair(user.name, user.avatarUrl)
                                } else {
                                    selectedUserForProfile = user
                                }
                            },
                            onClick = { selectedUserForProfile = user },
                            onSayHi = { onSayHi(user) }
                        )
                    }

                    // Sisipkan Iklan Native yang elegan setelah profil ke-4
                    if (index == 3) {
                        item(key = "native_ad_nearby_$index") {
                            LevelPlayNativeAdCard(
                                testTag = "native_ad_nearby_$index"
                            )
                        }
                    }
                }
                }

                // Tombol "Cari Lebih Banyak" yang memicu Iklan Reward
                if (hasHiddenUsers) {
                    item {
                        val cardTitle = com.example.util.AppStrings.nearbyUnlockMoreTitle(language, hiddenCount, nearbyExpansionTier, nextTargetLimit)
                        val cardDesc = com.example.util.AppStrings.nearbyUnlockMoreDesc(language, nearbyExpansionTier)
                        val toastMsg = com.example.util.AppStrings.nearbyUnlockSuccessToast(language, nearbyExpansionTier, nextTargetLimit)
                        val btnText = com.example.util.AppStrings.nearbyWatchAdButton(language, nearbyExpansionTier)

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .testTag("card_load_more_nearby")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFF3E0))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SmartDisplay,
                                        contentDescription = null,
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = cardTitle,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = cardDesc,
                                    fontSize = 12.sp,
                                    color = NeutralMedium,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        val activity = context.findActivity()
                                        val adLaunched = AdManager.showRewardedVideo(activity = activity) {
                                            onExpandNearby()
                                            Toast.makeText(
                                                context,
                                                toastMsg,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        if (!adLaunched) {
                                            // Jika iklan sedang dipersiapkan atau belum tersedia, berikan info dan tetap buka
                                            Toast.makeText(
                                                context,
                                                com.example.util.AppStrings.nearbyUnlockingUsersToast(language),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            onExpandNearby()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("btn_load_more_nearby")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = btnText,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else if (isFullyExpanded && filteredUsers.size > 12) {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = com.example.util.AppStrings.nearbyAllUsersDisplayed(language, displayedUsers.size),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldGreen
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            }
        }
    }

    selectedUserForProfile?.let { user ->
        UserProfileBottomSheet(
            user = user,
            existingMoments = moments,
            isBlocked = isUserBlocked(user.id, user.name),
            momentComments = momentComments,
            onAddComment = onAddComment,
            onToggleLikeMoment = onToggleLikeMoment,
            language = language,
            onDismiss = { selectedUserForProfile = null },
            onSayHi = onSayHi,
            onBlockUser = { onBlockUser(user) },
            onUnblockUser = { onUnblockUser(user) },
            onReportUser = { reason, notes, alsoBlock ->
                onReportUser?.invoke(user, reason, notes, alsoBlock)
            }
        )
    }

    // Zoomable Photo Viewer saat foto avatar pengguna di daftar sekitar diklik
    viewingAvatarPhoto?.let { (userName, photoUrl) ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = photoUrl,
            title = "Foto Profil $userName",
            onDismiss = { viewingAvatarPhoto = null }
        )
    }
}
}

@Composable
fun NearbyUserCard(
    user: User,
    hideExactDistance: Boolean = false,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onAvatarClick: (() -> Unit)? = null,
    onClick: () -> Unit = {},
    onSayHi: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("user_card_${user.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar (Compact 46dp) - Klik untuk zoom jika ada foto profil
            val hasPhoto = !user.avatarUrl.isNullOrBlank()
            LovyAvatar(
                name = user.name,
                avatarColorHex = user.avatarColorHex,
                avatarUrl = user.avatarUrl,
                size = 46.dp,
                fontSize = 18.sp,
                isOnline = user.isOnline,
                modifier = if (hasPhoto && onAvatarClick != null) {
                    Modifier
                        .clip(CircleShape)
                        .clickable { onAvatarClick() }
                } else Modifier
            )

            Spacer(modifier = Modifier.width(10.dp))

            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = user.name.ifBlank { "Pengguna Lovy" },
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Gender & Age tag
                    val badgeColor = if (user.gender == Gender.FEMALE) Color(0xFFFF4081) else Color(0xFF1976D2)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeColor
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                            modifier = Modifier.padding(horizontal = 4.5.dp, vertical = 1.dp)
                        ) {
                            Icon(
                                imageVector = if (user.gender == Gender.FEMALE) Icons.Default.Female else Icons.Default.Male,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = "${user.age}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Bio
                Text(
                    text = user.bio,
                    fontSize = 11.5.sp,
                    color = NeutralMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Distance & Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = if (hideExactDistance) user.city else "${user.formattedDistance} • ${user.city}",
                        fontSize = 11.sp,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Say Hi button (Compact & Neat)
            OutlinedButton(
                onClick = onSayHi,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = EmeraldGreen
                ),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = androidx.compose.ui.graphics.SolidColor(EmeraldGreen)
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("btn_say_hi_${user.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.WavingHand,
                    contentDescription = com.example.util.AppStrings.sayHi(language),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = com.example.util.AppStrings.sayHi(language),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

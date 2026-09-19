package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyScreen(
    users: List<User>,
    selectedGenderFilter: Gender?,
    isScanning: Boolean,
    currentGpsLocation: com.example.util.UserGpsLocation? = null,
    hasLocationPermission: Boolean = false,
    isGpsEnabled: Boolean = true,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onPermissionResult: (Boolean) -> Unit = {},
    onBack: () -> Unit,
    onFilterChange: (Gender?) -> Unit,
    onRefreshScan: () -> Unit,
    onSayHi: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        onPermissionResult(fineGranted || coarseGranted)
    }

    LaunchedEffect(Unit) {
        val fineCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fineCheck == PackageManager.PERMISSION_GRANTED || coarseCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionResult(true)
        }
    }

    val filteredUsers = remember(users, selectedGenderFilter) {
        if (selectedGenderFilter == null) users
        else users.filter { it.gender == selectedGenderFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pengguna di Sekitar",
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
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onRefreshScan,
                        modifier = Modifier.testTag("nearby_btn_refresh")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Pindai Ulang",
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
            IronSourceBannerView()
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

            // Android Native GPS Status Banner
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (hasLocationPermission) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (hasLocationPermission) EmeraldGreen.copy(alpha = 0.15f) else Color(0xFFFF9800).copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = if (hasLocationPermission) Icons.Default.GpsFixed else Icons.Default.LocationOff,
                                contentDescription = null,
                                tint = if (hasLocationPermission) EmeraldGreen else Color(0xFFE65100),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (hasLocationPermission) "GPS Android Native Aktif" else "Izin Lokasi Belum Aktif",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Text(
                                text = if (currentGpsLocation != null) {
                                    currentGpsLocation.readableLocation + " (Akurasi: ±${currentGpsLocation.accuracy.toInt()}m)"
                                } else if (hasLocationPermission) {
                                    "Mencari sinyal satelit GPS..."
                                } else {
                                    "Aktifkan GPS agar jarak teman di sekitar akurat"
                                },
                                fontSize = 11.sp,
                                color = NeutralMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (!hasLocationPermission) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp).testTag("btn_request_gps")
                        ) {
                            Text("Izinkan", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Filter Chips Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = NeutralMedium,
                    modifier = Modifier.size(18.dp)
                )

                FilterChip(
                    selected = selectedGenderFilter == null,
                    onClick = { onFilterChange(null) },
                    label = { Text("Semua") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldGreen,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_all")
                )

                FilterChip(
                    selected = selectedGenderFilter == Gender.FEMALE,
                    onClick = { onFilterChange(Gender.FEMALE) },
                    label = { Text("Hanya Wanita") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Female,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
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
                    label = { Text("Hanya Pria") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Male,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1976D2),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_male")
                )
            }

            // Hint radar text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Ditemukan ${filteredUsers.size} orang dalam radius 2 km",
                    fontSize = 12.sp,
                    color = NeutralMedium
                )
            }

            // User list
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredUsers, key = { it.id }) { user ->
                    NearbyUserCard(
                        user = user,
                        onSayHi = { onSayHi(user) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun NearbyUserCard(
    user: User,
    onSayHi: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("user_card_${user.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(modifier = Modifier.size(54.dp)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(user.avatarColorHex))
                ) {
                    Text(
                        text = user.name.take(1),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (user.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(CircleShape)
                            .background(EmeraldGreen)
                            .border(2.dp, Color.White, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = user.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Gender & Age tag
                    val badgeColor = if (user.gender == Gender.FEMALE) Color(0xFFFF4081) else Color(0xFF2196F3)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeColor)
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (user.gender == Gender.FEMALE) Icons.Default.Female else Icons.Default.Male,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${user.age}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Bio
                Text(
                    text = user.bio,
                    fontSize = 12.5.sp,
                    color = NeutralMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Distance & Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "${user.formattedDistance} • ${user.city}",
                        fontSize = 11.5.sp,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Say Hi button
            OutlinedButton(
                onClick = onSayHi,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = EmeraldGreen
                ),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = androidx.compose.ui.graphics.SolidColor(EmeraldGreen)
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("btn_say_hi_${user.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.WavingHand,
                    contentDescription = "Sapa",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sapa",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

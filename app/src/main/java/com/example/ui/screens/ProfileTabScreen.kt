package com.example.ui.screens

import android.Manifest
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground

@Composable
fun ProfileTabScreen(
    myName: String,
    myBio: String,
    myLovyId: String,
    isGuest: Boolean = false,
    isSupabaseConnected: Boolean = true,
    profilePicture: String? = null,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    detectedGeoArea: String = "ID/MY",
    isLocalMode: Boolean = true,
    isNearbyVisible: Boolean = true,
    hideExactDistance: Boolean = false,
    showOnlineStatus: Boolean = true,
    hasLocationPermission: Boolean = false,
    isGpsEnabled: Boolean = true,
    onToggleNearbyVisible: (Boolean) -> Unit = {},
    onToggleHideExactDistance: (Boolean) -> Unit = {},
    onToggleShowOnlineStatus: (Boolean) -> Unit = {},
    onLocationPermissionChanged: (Boolean) -> Unit = {},
    onLanguageChange: (com.example.util.AppLanguage) -> Unit = {},
    onNavigateToUserProfile: () -> Unit = {},
    onNavigateToBottle: () -> Unit,
    onNavigateToMoments: () -> Unit,
    onNavigateToSupabaseConfig: () -> Unit = {},
    blockedUserNames: Set<String> = emptySet(),
    onUnblockUser: (String) -> Unit = {},
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLanguagePicker by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showBlockedUsersDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Profile Card with Emerald Background (Clickable to open UserProfileScreen)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EmeraldGreen)
                .clickable { onNavigateToUserProfile() }
                .padding(top = 32.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
                .testTag("banner_profile_header")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    if (!profilePicture.isNullOrBlank()) {
                        AsyncImage(
                            model = profilePicture,
                            contentDescription = myName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_lovy_logo),
                            contentDescription = "Logo Lovy Chat",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = myName,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (isGuest) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color.White.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Mode Tamu (Lokal)",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "ID Lovy: $myLovyId",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = myBio,
                        fontSize = 11.5.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ketuk untuk lihat detail profil →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFC8E6C9)
                    )
                }

                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "QR Code",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Menu Section 1
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.Person,
                    iconTint = EmeraldGreen,
                    title = "Detail Profil Pengguna",
                    subtitle = "Nama tampilan, bio, foto & info akun",
                    onClick = onNavigateToUserProfile
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.CameraAlt,
                    iconTint = Color(0xFFFB8C00),
                    title = "Momen Saya",
                    subtitle = "Koleksi foto dan cerita harianmu",
                    onClick = onNavigateToMoments
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Waves,
                    iconTint = Color(0xFF00ACC1),
                    title = "Botol Lautan Saya",
                    subtitle = "Daftar botol yang pernah kamu lempar",
                    onClick = onNavigateToBottle
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Profile Menu Section 2
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    iconTint = EmeraldGreen,
                    title = "Privasi & Lokasi",
                    subtitle = if (!isNearbyVisible) "Mode Penyamaran aktif" else if (hideExactDistance) "Jarak persis disembunyikan" else "Visibilitas sekitar aktif",
                    onClick = { showPrivacyDialog = true }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Block,
                    iconTint = Color(0xFFE53935),
                    title = "Pengguna Diblokir",
                    subtitle = if (blockedUserNames.isEmpty()) "Tidak ada pengguna diblokir" else "${blockedUserNames.size} pengguna diblokir",
                    onClick = { showBlockedUsersDialog = true }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Language,
                    iconTint = Color(0xFF5C6BC0),
                    title = com.example.util.AppStrings.languageSetting(language),
                    subtitle = if (isLocalMode) "LO (Lokal: ${language.displayName}) • $detectedGeoArea" else "EN (English Global)",
                    onClick = { showLanguagePicker = true }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFF78909C),
                    title = com.example.util.AppStrings.menuAbout(language),
                    subtitle = "Versi 1.0.0 (${com.example.util.AppStrings.motto(language)})",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    iconTint = Color(0xFFE53935),
                    title = com.example.util.AppStrings.menuLogout(language),
                    subtitle = com.example.util.AppStrings.menuLogoutSub(language),
                    onClick = onLogout
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showBlockedUsersDialog) {
        BlockedUsersDialog(
            blockedUserNames = blockedUserNames,
            onUnblockUser = onUnblockUser,
            onDismiss = { showBlockedUsersDialog = false }
        )
    }

    if (showLanguagePicker) {
        AlertDialog(
            onDismissRequest = { showLanguagePicker = false },
            title = {
                Text(
                    text = "Pilih Bahasa / Language (LO - EN)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = NeutralDark
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Sistem LO - EN otomatis mendeteksi bahasa lokal negara manapun di seluruh dunia (Cina, Jepang, Arab, Indonesia, dll).",
                        fontSize = 12.sp,
                        color = NeutralMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 1. LO - Local Auto
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isLocalMode) EmeraldGreen.copy(alpha = 0.12f) else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageChange(com.example.util.AppLanguage.LOCAL)
                                showLanguagePicker = false
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🌐 LO (Lokal Otomatis Negara)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = if (isLocalMode) EmeraldGreen else NeutralDark
                                )
                                Text(
                                    text = "Bahasa terdeteksi: ${language.displayName} (${language.nativeName})",
                                    fontSize = 11.5.sp,
                                    color = NeutralMedium
                                )
                                Text(
                                    text = "Lokasi: $detectedGeoArea",
                                    fontSize = 11.sp,
                                    color = EmeraldGreen
                                )
                            }
                            if (isLocalMode) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // 2. EN - English
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (!isLocalMode && language == com.example.util.AppLanguage.ENGLISH) EmeraldGreen.copy(alpha = 0.12f) else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageChange(com.example.util.AppLanguage.ENGLISH)
                                showLanguagePicker = false
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🇬🇧 EN (English Global)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = if (!isLocalMode && language == com.example.util.AppLanguage.ENGLISH) EmeraldGreen else NeutralDark
                                )
                                Text(
                                    text = "International language mode",
                                    fontSize = 11.5.sp,
                                    color = NeutralMedium
                                )
                            }
                            if (!isLocalMode && language == com.example.util.AppLanguage.ENGLISH) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Uji Coba Langsung Bahasa Negara Lain:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = NeutralDark
                    )

                    // Daftar cepat uji negara lain
                    val testLanguages = listOf(
                        com.example.util.AppLanguage.CHINESE to "🇨🇳 Cina / Chinese (中文)",
                        com.example.util.AppLanguage.JAPANESE to "🇯🇵 Jepang / Japanese (日本語)",
                        com.example.util.AppLanguage.ARABIC to "🇸🇦 Arab / Arabic (العربية)",
                        com.example.util.AppLanguage.KOREAN to "🇰🇷 Korea / Korean (한국어)",
                        com.example.util.AppLanguage.INDONESIAN to "🇮🇩 Indonesia (Bahasa Indonesia)",
                        com.example.util.AppLanguage.SPANISH to "🇪🇸 Spanyol / Spanish (Español)",
                        com.example.util.AppLanguage.FRENCH to "🇫🇷 Prancis / French (Français)",
                        com.example.util.AppLanguage.GERMAN to "🇩🇪 Jerman / German (Deutsch)",
                        com.example.util.AppLanguage.RUSSIAN to "🇷🇺 Rusia / Russian (Русский)"
                    )

                    testLanguages.forEach { (lang, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (language == lang && !isLocalMode) Color(0xFFE0F2F1) else Color.Transparent)
                                .clickable {
                                    onLanguageChange(lang)
                                    showLanguagePicker = false
                                }
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                color = if (language == lang && !isLocalMode) EmeraldGreen else NeutralDark,
                                modifier = Modifier.weight(1f)
                            )
                            if (language == lang && !isLocalMode) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguagePicker = false }) {
                    Text("Tutup", color = EmeraldGreen, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showPrivacyDialog) {
        PrivacyLocationDialog(
            isNearbyVisible = isNearbyVisible,
            hideExactDistance = hideExactDistance,
            showOnlineStatus = showOnlineStatus,
            hasLocationPermission = hasLocationPermission,
            isGpsEnabled = isGpsEnabled,
            onToggleNearbyVisible = onToggleNearbyVisible,
            onToggleHideExactDistance = onToggleHideExactDistance,
            onToggleShowOnlineStatus = onToggleShowOnlineStatus,
            onLocationPermissionChanged = onLocationPermissionChanged,
            onDismiss = { showPrivacyDialog = false }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeutralDark
            )
            Text(
                text = subtitle,
                fontSize = 11.5.sp,
                color = NeutralMedium
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFB0BEC5),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun PrivacyLocationDialog(
    isNearbyVisible: Boolean,
    hideExactDistance: Boolean,
    showOnlineStatus: Boolean,
    hasLocationPermission: Boolean,
    isGpsEnabled: Boolean,
    onToggleNearbyVisible: (Boolean) -> Unit,
    onToggleHideExactDistance: (Boolean) -> Unit,
    onToggleShowOnlineStatus: (Boolean) -> Unit,
    onLocationPermissionChanged: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        onLocationPermissionChanged(granted)
        if (granted) {
            Toast.makeText(context, "Izin lokasi berhasil diaktifkan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Izin lokasi belum diberikan", Toast.LENGTH_SHORT).show()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = EmeraldGreen.copy(alpha = 0.15f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = "Privasi & Lokasi",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = NeutralDark
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Radar Around Me Visibility (Ghost Mode)
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (isNearbyVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Tampilkan Saya di Sekitar",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = isNearbyVisible,
                                onCheckedChange = onToggleNearbyVisible,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = EmeraldGreen,
                                    checkedTrackColor = EmeraldGreen.copy(alpha = 0.35f)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isNearbyVisible)
                                "Profil Anda aktif dan dapat ditemukan oleh pengguna lain di radar 'Di Sekitar Saya'."
                            else
                                "Mode Penyamaran aktif. Profil Anda disembunyikan dari radar pencarian orang sekitar.",
                            fontSize = 11.5.sp,
                            color = NeutralMedium,
                            lineHeight = 16.sp
                        )
                    }
                }

                // 2. Hide Exact Distance
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (hideExactDistance) Icons.Default.LocationOff else Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (hideExactDistance) Color(0xFFE53935) else EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Sembunyikan Jarak Persis",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = hideExactDistance,
                                onCheckedChange = onToggleHideExactDistance,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = EmeraldGreen,
                                    checkedTrackColor = EmeraldGreen.copy(alpha = 0.35f)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (hideExactDistance)
                                "Jarak meter/km disembunyikan. Orang lain hanya dapat melihat nama kota/wilayah Anda."
                            else
                                "Pengguna lain dapat melihat perkiraan jarak meter atau kilometer dari lokasi Anda.",
                            fontSize = 11.5.sp,
                            color = NeutralMedium,
                            lineHeight = 16.sp
                        )
                    }
                }

                // 3. Online Status Visibility
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Tampilkan Status Online",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = showOnlineStatus,
                                onCheckedChange = onToggleShowOnlineStatus,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = EmeraldGreen,
                                    checkedTrackColor = EmeraldGreen.copy(alpha = 0.35f)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Menampilkan tanda online ketika Anda sedang aktif membuka Lovy Chat.",
                            fontSize = 11.5.sp,
                            color = NeutralMedium,
                            lineHeight = 16.sp
                        )
                    }
                }

                // 4. GPS Hardware & System Permission
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (hasLocationPermission) Icons.Default.GpsFixed else Icons.Default.GpsOff,
                                contentDescription = null,
                                tint = if (hasLocationPermission) EmeraldGreen else Color(0xFFE53935),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Izin Lokasi & GPS Perangkat",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark
                                )
                                Text(
                                    text = if (hasLocationPermission) "Izin GPS diberikan • Aktif" else "Izin lokasi belum diberikan",
                                    fontSize = 11.5.sp,
                                    color = if (hasLocationPermission) EmeraldGreen else Color(0xFFE53935),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        if (!hasLocationPermission) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Izinkan Akses GPS", fontSize = 12.sp, color = Color.White)
                            }
                        }

                        if (!isGpsEnabled) {
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedButton(
                                onClick = {
                                    try {
                                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                    } catch (_: Throwable) {
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Buka Pengaturan Lokasi HP", fontSize = 12.sp, color = NeutralDark)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Toast.makeText(context, "Pengaturan privasi & lokasi diperbarui", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Selesai", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun BlockedUsersDialog(
    blockedUserNames: Set<String>,
    onUnblockUser: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Daftar Pengguna Diblokir",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = NeutralDark
                )
            }
        },
        text = {
            if (blockedUserNames.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Tidak ada pengguna yang diblokir",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = NeutralMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pengguna yang Anda blokir di ruang chat akan muncul di sini.",
                            fontSize = 12.sp,
                            color = NeutralMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Pengguna di bawah ini tidak dapat mengirimi Anda pesan atau melihat Anda di Sekitar Saya:",
                        fontSize = 12.sp,
                        color = NeutralMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    blockedUserNames.forEach { blockedName ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ScreenBackground),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFEBEE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = blockedName.take(1).uppercase(),
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE53935),
                                            fontSize = 15.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = blockedName,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = NeutralDark,
                                        maxLines = 1
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        onUnblockUser(blockedName)
                                        Toast.makeText(context, "Blokir untuk $blockedName dibuka", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("btn_unblock_$blockedName")
                                ) {
                                    Text(
                                        text = "Buka Blokir",
                                        fontSize = 12.sp,
                                        color = EmeraldGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Tutup", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

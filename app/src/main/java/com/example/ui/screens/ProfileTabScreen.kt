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
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
    onDeleteAccount: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showLanguagePicker by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showBlockedUsersDialog by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showAboutAppDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showMyQrCodeDialog by remember { mutableStateOf(false) }
    var viewingPhotoUrl by remember { mutableStateOf<String?>(null) }
    var secretDevClickCount by remember { mutableIntStateOf(0) }
    var lastSecretClickTime by remember { mutableLongStateOf(0L) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Profile Card with Emerald Background (Compact & Neat)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EmeraldGreen)
                .clickable { onNavigateToUserProfile() }
                .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .testTag("banner_profile_header")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar (Compact 58dp)
                val hasPhoto = !profilePicture.isNullOrBlank()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .then(
                            if (hasPhoto) {
                                Modifier.clickable { viewingPhotoUrl = profilePicture }
                            } else Modifier
                        )
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

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = myName,
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ID Lovy: $myLovyId",
                        fontSize = 11.5.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = myBio,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Ketuk untuk lihat detail profil →",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFC8E6C9)
                    )
                }

                // Tombol Kode QR Profil Saya
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { showMyQrCodeDialog = true }
                        .testTag("btn_my_qr_code")
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "Buka Kode QR Saya",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Profile Menu Section 1
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
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

        Spacer(modifier = Modifier.height(8.dp))

        // Profile Menu Section 2
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
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
                    icon = Icons.Default.Notifications,
                    iconTint = EmeraldGreen,
                    title = "Uji Notifikasi & Getar",
                    subtitle = "Tekan untuk tes suara pop-up & getaran perangkat",
                    onClick = {
                        com.example.util.LovyNotificationHelper.showChatNotification(
                            context = context,
                            conversationId = "test_notification_id",
                            senderName = "Lovy Chat 💬",
                            messageText = "Notifikasi & efek getar berhasil berfungsi optimal! 📳✨"
                        )
                        Toast.makeText(context, "Memicu notifikasi & efek getar pesan baru 🔔📳", Toast.LENGTH_SHORT).show()
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Security,
                    iconTint = Color(0xFF0288D1),
                    title = "Kebijakan Privasi & Ketentuan",
                    subtitle = "Panduan izin lokasi, kamera, data iklan & akun",
                    onClick = { showPrivacyPolicyDialog = true }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFF00B0FF),
                    title = com.example.util.AppStrings.menuAbout(language),
                    subtitle = "Versi 1.0.0 (${com.example.util.AppStrings.motto(language)})",
                    onClick = { showAboutAppDialog = true }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    iconTint = Color(0xFFE53935),
                    title = com.example.util.AppStrings.menuLogout(language),
                    subtitle = com.example.util.AppStrings.menuLogoutSub(language),
                    onClick = onLogout
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.DeleteForever,
                    iconTint = Color(0xFFD32F2F),
                    title = com.example.util.AppStrings.menuDeleteAccount(language),
                    subtitle = com.example.util.AppStrings.menuDeleteAccountSub(language),
                    onClick = { showDeleteAccountDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = com.example.util.AppStrings.deleteAccountDialogTitle(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFFD32F2F)
                )
            },
            text = {
                Text(
                    text = com.example.util.AppStrings.deleteAccountDialogDesc(language),
                    fontSize = 13.5.sp,
                    color = NeutralDark,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountDialog = false
                        Toast.makeText(context, "Akun dan seluruh data Anda telah berhasil dihapus.", Toast.LENGTH_LONG).show()
                        onDeleteAccount()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = com.example.util.AppStrings.deleteAccountConfirmButton(language),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteAccountDialog = false }
                ) {
                    Text(
                        text = com.example.util.AppStrings.deleteAccountCancelButton(language),
                        color = NeutralMedium
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
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

    if (showPrivacyPolicyDialog) {
        PrivacyPolicyDialog(onDismiss = { showPrivacyPolicyDialog = false })
    }

    if (showAboutAppDialog) {
        AboutAppDialog(
            onDismiss = { showAboutAppDialog = false },
            onOpenPrivacyPolicy = {
                showAboutAppDialog = false
                showPrivacyPolicyDialog = true
            },
            onOpenServerConfig = {
                showAboutAppDialog = false
                onNavigateToSupabaseConfig()
            }
        )
    }

    // Zoomable Photo Viewer Dialog untuk foto profil sendiri
    viewingPhotoUrl?.let { photoUrl ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = photoUrl,
            title = "Foto Profil $myName",
            onDismiss = { viewingPhotoUrl = null }
        )
    }

    // Dialog Kode QR Profil Pengguna (Berisi ID Lovy asli dan scannable)
    if (showMyQrCodeDialog) {
        com.example.ui.components.MyQrCodeDialog(
            name = myName,
            lovyId = myLovyId,
            avatarUrl = profilePicture,
            onDismiss = { showMyQrCodeDialog = false }
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
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeutralDark
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
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

    var showLocationDisclosure by remember { mutableStateOf(false) }

    if (showLocationDisclosure) {
        com.example.ui.components.PermissionDisclosureDialog(
            type = com.example.ui.components.DisclosureType.LOCATION,
            onConfirm = {
                showLocationDisclosure = false
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            },
            onDismiss = {
                showLocationDisclosure = false
            }
        )
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
                                    showLocationDisclosure = true
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

@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit
) {
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
                    text = "Kebijakan Privasi & Ketentuan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
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
                Text(
                    text = "Lovy Chat berkomitmen melindungi privasi data dan keamanan pengguna sesuai standar Google Play Developer Policy.",
                    fontSize = 13.sp,
                    color = NeutralMedium,
                    lineHeight = 18.sp
                )

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "📍 1. Penggunaan Izin Lokasi (GPS)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = "• Lokasi hanya diakses saat aplikasi sedang aktif dibuka (Foreground).\n• Digunakan semata-mata untuk fitur 'Pengguna Sekitar' (Radar Teman).\n• Anda dapat mengaktifkan Mode Penyamaran atau menyembunyikan jarak persis kapan saja di menu Privasi & Lokasi.",
                            fontSize = 12.sp,
                            color = NeutralDark,
                            lineHeight = 17.sp
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "📷 2. Penggunaan Izin Kamera",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = "• Digunakan untuk memindai Barcode / QR Code teman secara instan.\n• Digunakan untuk mengambil foto profil atau gambar obrolan secara langsung jika Anda memilih menggunakan kamera.\n• Kamera tidak pernah merekam di latar belakang.",
                            fontSize = 12.sp,
                            color = NeutralDark,
                            lineHeight = 17.sp
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "📢 3. Layanan Iklan & ID Iklan (AD_ID)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = "• Aplikasi menggunakan Google Play Advertising ID (AD_ID) melalui SDK Unity/ironSource untuk menayangkan banner iklan.\n• Data periklanan dikelola sesuai pedoman privasi Google Play.",
                            fontSize = 12.sp,
                            color = NeutralDark,
                            lineHeight = 17.sp
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "🛡️ 4. Konten Pengguna & Anti-Pelecehan (UGC)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = "• Lovy Chat melarang segala bentuk spam, pornografi, ujaran kebencian, dan pelecehan.\n• Disediakan tombol Laporkan dan Blokir pada setiap profil teman, obrolan, dan momen.\n• Pengguna yang melanggar akan ditindak tegas.",
                            fontSize = 12.sp,
                            color = NeutralDark,
                            lineHeight = 17.sp
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "🗑️ 5. Hak Hapus Akun & Data (Account Deletion)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = "• Anda berhak menghapus akun dan seluruh riwayat obrolan serta data profil kapan saja melalui tombol 'Hapus Akun Permanen' di halaman Profil.",
                            fontSize = 12.sp,
                            color = NeutralDark,
                            lineHeight = 17.sp
                        )
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
                Text("Saya Mengerti", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun AboutAppDialog(
    onDismiss: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    onOpenServerConfig: () -> Unit
) {
    val context = LocalContext.current
    var devClickCount by remember { mutableIntStateOf(0) }
    var lastDevClick by remember { mutableLongStateOf(0L) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // App Logo with Soft Elevation
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = Color.White,
                    shadowElevation = 6.dp,
                    modifier = Modifier.size(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_lovy_logo),
                        contentDescription = "Logo Lovy Chat",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(22.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // App Title & Tagline
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Lovy Chat",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NeutralDark
                    )

                    // Version Tag with Secret Tap (5x) for server settings
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = EmeraldGreen.copy(alpha = 0.12f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                val now = System.currentTimeMillis()
                                if (now - lastDevClick < 800L) {
                                    devClickCount++
                                    if (devClickCount >= 5) {
                                        devClickCount = 0
                                        Toast.makeText(context, "Mode Pengembang: Pengaturan Server ☁️", Toast.LENGTH_SHORT).show()
                                        onOpenServerConfig()
                                    }
                                } else {
                                    devClickCount = 1
                                }
                                lastDevClick = now
                            }
                    ) {
                        Text(
                            text = "Versi 1.0.0 Resmi (2026)",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "Teman baru, obrolan seru di sekitarmu ✨",
                        fontSize = 12.5.sp,
                        color = NeutralMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Overview Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Text(
                        text = "Lovy Chat adalah platform obrolan sosial modern yang memudahkan kamu menemukan teman baru di sekitar, berbagi momen harian, dan bertukar cerita secara cepat, aman, dan menyenangkan.",
                        fontSize = 12.5.sp,
                        color = NeutralDark,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Core Features List
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Fitur Unggulan",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )

                    AboutFeatureRow(
                        iconEmoji = "📍",
                        title = "Radar Teman Sekitar",
                        desc = "Temukan teman terdekat berbasis GPS dengan kendali jarak dan privasi penyamaran."
                    )
                    AboutFeatureRow(
                        iconEmoji = "💬",
                        title = "Pesan Cepat & Realtime",
                        desc = "Kirim pesan teks & foto instan dengan tanda centang status pesan terbaca."
                    )
                    AboutFeatureRow(
                        iconEmoji = "📷",
                        title = "Pindai Barcode & QR Code",
                        desc = "Tambah teman langsung dalam sekejap tanpa repot mengetik nomor atau ID."
                    )
                    AboutFeatureRow(
                        iconEmoji = "🌊",
                        title = "Botol Lautan (Drift Bottle)",
                        desc = "Lempar pesan acak melintasi lautan untuk terhubung dengan teman baru."
                    )
                    AboutFeatureRow(
                        iconEmoji = "📸",
                        title = "Momen & Cerita",
                        desc = "Bagikan foto dan status harian dengan suka serta komentar dari teman."
                    )
                }

                // Security & Policy Badge
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldGreen.copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "🛡️ Privasi & Keamanan Terpercaya",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                        Text(
                            text = "Seluruh koneksi menggunakan enkripsi aman HTTPS/TLS. Dilengkapi sistem pemblokiran pengguna, pelaporan pelanggaran, dan penghapusan akun permanen mandiri.",
                            fontSize = 11.5.sp,
                            color = NeutralDark,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Developer & Copyright
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "Dikembangkan oleh Geccko Creator",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Text(
                        text = "© 2026 Lovy Chat. Hak cipta dilindungi undang-undang.",
                        fontSize = 10.5.sp,
                        color = NeutralMedium
                    )
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
        },
        dismissButton = {
            TextButton(
                onClick = onOpenPrivacyPolicy
            ) {
                Text("Kebijakan Privasi", color = EmeraldGreen, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    )
}

@Composable
private fun AboutFeatureRow(
    iconEmoji: String,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ScreenBackground, RoundedCornerShape(10.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = iconEmoji, fontSize = 16.sp)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralDark
            )
            Text(
                text = desc,
                fontSize = 11.5.sp,
                color = NeutralMedium,
                lineHeight = 16.sp
            )
        }
    }
}

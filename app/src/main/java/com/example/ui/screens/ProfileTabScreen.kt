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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.util.AppShareHelper

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
                            error = painterResource(id = R.drawable.ic_lovy_logo),
                            fallback = painterResource(id = R.drawable.ic_lovy_logo),
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
                        text = "${com.example.util.AppStrings.lovyIdLabel(language)}: $myLovyId",
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
                        text = com.example.util.AppStrings.profileTapToView(language),
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
                        contentDescription = com.example.util.AppStrings.qrCodeButtonDesc(language),
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
                    title = com.example.util.AppStrings.menuUserProfile(language),
                    subtitle = com.example.util.AppStrings.menuUserProfileSub(language),
                    onClick = onNavigateToUserProfile
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.CameraAlt,
                    iconTint = Color(0xFFFB8C00),
                    title = com.example.util.AppStrings.menuMyMoments(language),
                    subtitle = com.example.util.AppStrings.menuMyMomentsSub(language),
                    onClick = onNavigateToMoments
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Waves,
                    iconTint = Color(0xFF00ACC1),
                    title = com.example.util.AppStrings.menuMyBottles(language),
                    subtitle = com.example.util.AppStrings.menuMyBottlesSub(language),
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
                    title = com.example.util.AppStrings.menuPrivacyLocation(language),
                    subtitle = if (!isNearbyVisible) com.example.util.AppStrings.privacyIncognitoActive(language) else if (hideExactDistance) com.example.util.AppStrings.privacyExactDistanceHidden(language) else com.example.util.AppStrings.privacyNearbyVisible(language),
                    onClick = { showPrivacyDialog = true }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Block,
                    iconTint = Color(0xFFE53935),
                    title = com.example.util.AppStrings.menuBlockedUsers(language),
                    subtitle = if (blockedUserNames.isEmpty()) com.example.util.AppStrings.blockedUsersEmpty(language) else com.example.util.AppStrings.blockedUsersCount(language, blockedUserNames.size),
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
                    icon = Icons.Default.Security,
                    iconTint = Color(0xFF0288D1),
                    title = com.example.util.AppStrings.menuPrivacyPolicy(language),
                    subtitle = com.example.util.AppStrings.menuPrivacyPolicySub(language),
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
                    icon = Icons.Default.Star,
                    iconTint = Color(0xFFFFA000),
                    title = com.example.util.AppStrings.menuRateApp(language),
                    subtitle = com.example.util.AppStrings.menuRateAppSub(language),
                    onClick = { AppShareHelper.openPlayStoreRating(context) }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Share,
                    iconTint = EmeraldGreen,
                    title = com.example.util.AppStrings.menuShareApp(language),
                    subtitle = com.example.util.AppStrings.menuShareAppSub(language),
                    onClick = { AppShareHelper.shareApp(context, language) }
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
            language = language,
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
                        text = com.example.util.AppStrings.languagePickerTestOtherLangs(language),
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
                    Text(com.example.util.AppStrings.commonClose(language), color = EmeraldGreen, fontWeight = FontWeight.Bold)
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
            language = language,
            onDismiss = { showPrivacyDialog = false }
        )
    }

    if (showPrivacyPolicyDialog) {
        PrivacyPolicyDialog(
            language = language,
            onDismiss = { showPrivacyPolicyDialog = false }
        )
    }

    if (showAboutAppDialog) {
        AboutAppDialog(
            language = language,
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
            title = "${com.example.util.AppStrings.profilePhotoSection(language)}: $myName",
            onDismiss = { viewingPhotoUrl = null }
        )
    }

    // Dialog Kode QR Profil Pengguna (Berisi ID Lovy asli dan scannable)
    if (showMyQrCodeDialog) {
        com.example.ui.components.MyQrCodeDialog(
            name = myName,
            lovyId = myLovyId,
            avatarUrl = profilePicture,
            language = language,
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
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
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
            language = language,
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
                    text = com.example.util.AppStrings.privacyLocationTitle(language),
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
                                text = com.example.util.AppStrings.privacyShowMeNearby(language),
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
                            text = com.example.util.AppStrings.privacyShowMeNearbyDesc(language, isNearbyVisible),
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
                                text = com.example.util.AppStrings.privacyHideExactDistance(language),
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
                            text = com.example.util.AppStrings.privacyHideExactDistanceDesc(language, hideExactDistance),
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
                                text = com.example.util.AppStrings.privacyShowOnlineStatus(language),
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
                            text = com.example.util.AppStrings.privacyShowOnlineStatusDesc(language),
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
                                    text = com.example.util.AppStrings.privacyGpsPermissionTitle(language),
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark
                                )
                                Text(
                                    text = com.example.util.AppStrings.privacyGpsStatus(language, hasLocationPermission),
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
                                Text(com.example.util.AppStrings.privacyGrantGpsButton(language), fontSize = 12.sp, color = Color.White)
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
                                Text(com.example.util.AppStrings.privacyOpenGpsSettings(language), fontSize = 12.sp, color = NeutralDark)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Toast.makeText(context, com.example.util.AppStrings.privacySettingsUpdatedToast(language), Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(com.example.util.AppStrings.commonDone(language), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedUsersDialog(
    blockedUserNames: Set<String>,
    onUnblockUser: (String) -> Unit,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 6.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFDADCE0))
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("blocked_users_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        ) {
            // Header Bar: Icon, Title & Google Maps-style circular close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFEBEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = null,
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = com.example.util.AppStrings.blockedUsersTitle(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = NeutralDark
                        )
                        if (blockedUserNames.isNotEmpty()) {
                            Text(
                                text = com.example.util.AppStrings.blockedUsersCount(language, blockedUserNames.size),
                                fontSize = 12.sp,
                                color = NeutralMedium
                            )
                        }
                    }
                }

                // Circular close button (Google Maps style)
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F3F4))
                        .testTag("btn_close_blocked_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = com.example.util.AppStrings.commonClose(language),
                        tint = NeutralDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            HorizontalDivider(
                color = NeutralBorder.copy(alpha = 0.5f),
                thickness = 0.8.dp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Content Area
            if (blockedUserNames.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(EmeraldGreen.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = com.example.util.AppStrings.blockedUsersEmptyTitle(language),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = com.example.util.AppStrings.blockedUsersEmptyDesc(language),
                            fontSize = 13.sp,
                            color = NeutralMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
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
                        text = com.example.util.AppStrings.blockedUsersNotice(language),
                        fontSize = 12.sp,
                        color = NeutralMedium,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    blockedUserNames.forEach { blockedName ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = ScreenBackground),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFEBEE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = blockedName.take(1).uppercase(),
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE53935),
                                            fontSize = 16.sp
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
                                        Toast.makeText(context, com.example.util.AppStrings.blockedUserUnblockedToast(language, blockedName), Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = EmeraldGreen
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.5f)),
                                    modifier = Modifier.testTag("btn_unblock_$blockedName")
                                ) {
                                    Text(
                                        text = com.example.util.AppStrings.btnUnblock(language),
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyDialog(
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 6.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFDADCE0))
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("privacy_policy_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 20.dp)
        ) {
            // Header Bar: Shield Icon, Title, and Google Maps circular close button (X)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(EmeraldGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = com.example.util.AppStrings.privacyPolicyDialogTitle(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NeutralDark
                    )
                }

                // Circular close button (Google Maps style)
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F3F4))
                        .testTag("btn_close_privacy_policy_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = com.example.util.AppStrings.commonClose(language),
                        tint = NeutralDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            HorizontalDivider(
                color = NeutralBorder.copy(alpha = 0.5f),
                thickness = 0.8.dp,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = com.example.util.AppStrings.privacyPolicyDialogIntro(language),
                    fontSize = 13.sp,
                    color = NeutralMedium,
                    lineHeight = 18.sp
                )

                // 1. Location (GPS)
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec1Title(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec1Content(language),
                            fontSize = 12.5.sp,
                            color = NeutralDark,
                            lineHeight = 18.sp
                        )
                    }
                }

                // 2. Camera
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec2Title(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec2Content(language),
                            fontSize = 12.5.sp,
                            color = NeutralDark,
                            lineHeight = 18.sp
                        )
                    }
                }

                // 3. Advertising & AD_ID
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec3Title(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec3Content(language),
                            fontSize = 12.5.sp,
                            color = NeutralDark,
                            lineHeight = 18.sp
                        )
                    }
                }

                // 4. UGC & Safety
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec4Title(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec4Content(language),
                            fontSize = 12.5.sp,
                            color = NeutralDark,
                            lineHeight = 18.sp
                        )
                    }
                }

                // 5. Account & Data Deletion
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec5Title(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = com.example.util.AppStrings.privacyPolicySec5Content(language),
                            fontSize = 12.5.sp,
                            color = NeutralDark,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action button "Saya Mengerti" / "I Understand"
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_privacy_policy_understand")
            ) {
                Text(
                    text = com.example.util.AppStrings.privacyPolicyUnderstandButton(language),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppDialog(
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onDismiss: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    onOpenServerConfig: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var devClickCount by remember { mutableIntStateOf(0) }
    var lastDevClick by remember { mutableLongStateOf(0L) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 6.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFDADCE0))
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("about_app_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 20.dp)
        ) {
            // Header Bar: Small App Logo + Title + Google Maps style circular Close Button (X)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_lovy_logo),
                            contentDescription = "Logo Lovy Chat",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = com.example.util.AppStrings.aboutAppDialogTitle(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NeutralDark
                    )
                }

                // Circular close button (Google Maps style)
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F3F4))
                        .testTag("btn_close_about_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = com.example.util.AppStrings.commonClose(language),
                        tint = NeutralDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            HorizontalDivider(
                color = NeutralBorder.copy(alpha = 0.5f),
                thickness = 0.8.dp,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(modifier = Modifier.height(2.dp))

                // Big App Logo with Elevation
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = Color.White,
                    shadowElevation = 6.dp,
                    modifier = Modifier.size(76.dp)
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
                            text = com.example.util.AppStrings.aboutAppVersionBadge(language),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = com.example.util.AppStrings.aboutAppTagline(language),
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
                        text = com.example.util.AppStrings.aboutAppOverview(language),
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
                        text = com.example.util.AppStrings.aboutAppCoreFeaturesTitle(language),
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )

                    AboutFeatureRow(
                        iconEmoji = "📍",
                        title = com.example.util.AppStrings.aboutAppFeat1Title(language),
                        desc = com.example.util.AppStrings.aboutAppFeat1Desc(language)
                    )
                    AboutFeatureRow(
                        iconEmoji = "💬",
                        title = com.example.util.AppStrings.aboutAppFeat2Title(language),
                        desc = com.example.util.AppStrings.aboutAppFeat2Desc(language)
                    )
                    AboutFeatureRow(
                        iconEmoji = "📷",
                        title = com.example.util.AppStrings.aboutAppFeat3Title(language),
                        desc = com.example.util.AppStrings.aboutAppFeat3Desc(language)
                    )
                    AboutFeatureRow(
                        iconEmoji = "🌊",
                        title = com.example.util.AppStrings.aboutAppFeat4Title(language),
                        desc = com.example.util.AppStrings.aboutAppFeat4Desc(language)
                    )
                    AboutFeatureRow(
                        iconEmoji = "📸",
                        title = com.example.util.AppStrings.aboutAppFeat5Title(language),
                        desc = com.example.util.AppStrings.aboutAppFeat5Desc(language)
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
                            text = com.example.util.AppStrings.aboutAppSecurityTitle(language),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                        Text(
                            text = com.example.util.AppStrings.aboutAppSecurityDesc(language),
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
                        text = com.example.util.AppStrings.aboutAppDevCredit(language),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Text(
                        text = com.example.util.AppStrings.aboutAppCopyright(language),
                        fontSize = 10.5.sp,
                        color = NeutralMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Rating & Share Quick Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { AppShareHelper.openPlayStoreRating(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF8E1)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD54F)),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_about_rate_playstore")
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = com.example.util.AppStrings.aboutAppRateButton(language),
                        color = Color(0xFFE65100),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                    )
                }

                Button(
                    onClick = { AppShareHelper.shareApp(context, language) },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_about_share_app")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = com.example.util.AppStrings.aboutAppShareButton(language),
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                    )
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onOpenPrivacyPolicy,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldGreen),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_about_privacy_policy")
                ) {
                    Text(
                        text = com.example.util.AppStrings.aboutAppPrivacyPolicyButton(language),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_about_close")
                ) {
                    Text(
                        text = com.example.util.AppStrings.aboutAppCloseButton(language),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
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

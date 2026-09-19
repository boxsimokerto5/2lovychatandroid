package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    isSupabaseConnected: Boolean,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    detectedGeoArea: String = "ID/MY",
    onLanguageChange: (com.example.util.AppLanguage) -> Unit = {},
    onNavigateToBottle: () -> Unit,
    onNavigateToMoments: () -> Unit,
    onNavigateToSupabaseConfig: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Profile Card with Emerald Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EmeraldGreen)
                .padding(top = 32.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
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
                    Text(
                        text = myName.take(1),
                        color = EmeraldGreen,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = myName,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
                    icon = if (isSupabaseConnected) Icons.Default.CloudDone else Icons.Default.Storage,
                    iconTint = if (isSupabaseConnected) EmeraldGreen else Color(0xFF00897B),
                    title = "Database Supabase",
                    subtitle = if (isSupabaseConnected) "Tersambung ke Cloud • Data sinkron" else "Konfigurasi URL & API Key Cloud",
                    onClick = onNavigateToSupabaseConfig
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    iconTint = EmeraldGreen,
                    title = "Privasi & Lokasi",
                    subtitle = "Atur jarak dan izin visibilitas sekitar",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = NeutralBorder, thickness = 0.6.dp)
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    iconTint = Color(0xFF5C6BC0),
                    title = com.example.util.AppStrings.languageSetting(language),
                    subtitle = if (language == com.example.util.AppLanguage.INDONESIAN) "Bahasa Indonesia • Wilayah ID/MY" else "English • Region Global",
                    onClick = {
                        val nextLang = if (language == com.example.util.AppLanguage.INDONESIAN) com.example.util.AppLanguage.ENGLISH else com.example.util.AppLanguage.INDONESIAN
                        onLanguageChange(nextLang)
                    }
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

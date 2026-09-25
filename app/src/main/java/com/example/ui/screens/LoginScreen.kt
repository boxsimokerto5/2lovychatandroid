package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.util.AppLanguage
import com.example.util.AppStrings
import com.example.util.GoogleAuthHelper
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    language: AppLanguage = AppLanguage.INDONESIAN,
    detectedGeoArea: String = "ID/MY",
    isLocalMode: Boolean = true,
    onLanguageChange: (AppLanguage) -> Unit = {},
    onPerformLogin: suspend (username: String, password: String) -> com.example.data.AuthResult = { _, _ -> com.example.data.AuthResult(false, "") },
    onPerformRegister: suspend (username: String, password: String, gender: com.example.model.Gender) -> com.example.data.AuthResult = { _, _, _ -> com.example.data.AuthResult(false, "") },
    onPerformGoogleLogin: suspend (googleUser: GoogleAuthHelper.GoogleUserResult) -> com.example.data.AuthResult,
    onGuestLogin: () -> Unit = {},
    onNavigateToSupabaseConfig: () -> Unit = {},
    onRequestPermissionSetup: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isGoogleLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun performGoogleSignIn() {
        if (isGoogleLoading) return
        isGoogleLoading = true
        scope.launch {
            val result = GoogleAuthHelper.signInWithGoogle(context)
            result.onSuccess { googleUser ->
                val authResult = onPerformGoogleLogin(googleUser)
                isGoogleLoading = false
                snackbarHostState.showSnackbar(authResult.message)
            }.onFailure { exception ->
                isGoogleLoading = false
                val errorMsg = exception.message ?: if (language == AppLanguage.INDONESIAN) {
                    "Gagal menghubungkan akun Google"
                } else {
                    "Failed to connect Google account"
                }
                snackbarHostState.showSnackbar(errorMsg)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            val density = LocalDensity.current
            var headerHeightPx by remember { mutableIntStateOf(0) }
            val headerHeightDp = with(density) { headerHeightPx.toDp() }
            val minCardHeight = if (headerHeightPx > 0) {
                (maxHeight - headerHeightDp).coerceAtLeast(0.dp)
            } else {
                (maxHeight - 140.dp).coerceAtLeast(0.dp)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(EmeraldGreen)
                    .verticalScroll(rememberScrollState())
            ) {
                // Compact Emerald Header with Integrated Language Switcher
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            headerHeightPx = coordinates.size.height
                        }
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF004D40),
                                    EmeraldGreen
                                )
                            )
                        )
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 14.dp)
                ) {
                    // Top-right compact language toggle pill (LO vs EN)
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.22f))
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isLoActive = isLocalMode && language != AppLanguage.ENGLISH
                        val loDisplayLabel = when (language) {
                            AppLanguage.CHINESE -> "LO (中文)"
                            AppLanguage.JAPANESE -> "LO (日)"
                            AppLanguage.ARABIC -> "LO (ع)"
                            AppLanguage.INDONESIAN -> "LO (ID)"
                            AppLanguage.SPANISH -> "LO (ES)"
                            AppLanguage.KOREAN -> "LO (한)"
                            AppLanguage.FRENCH -> "LO (FR)"
                            AppLanguage.GERMAN -> "LO (DE)"
                            AppLanguage.RUSSIAN -> "LO (RU)"
                            AppLanguage.PORTUGUESE -> "LO (PT)"
                            else -> "LO"
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isLoActive) Color.White.copy(alpha = 0.95f) 
                                    else Color.Transparent
                                )
                                .clickable { onLanguageChange(AppLanguage.LOCAL) }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                .testTag("btn_lang_local")
                        ) {
                            Text(
                                text = loDisplayLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isLoActive) Color(0xFF004D40) else Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (!isLoActive) Color.White.copy(alpha = 0.95f) 
                                    else Color.Transparent
                                )
                                .clickable { onLanguageChange(AppLanguage.ENGLISH) }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                .testTag("btn_lang_en")
                        ) {
                            Text(
                                text = "EN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isLoActive) Color(0xFF004D40) else Color.White
                            )
                        }
                    }

                    // Centered App Branding
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Lovy Chat Icon",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Lovy Chat",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = "New friends, fun friends",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFB2DFDB),
                            letterSpacing = 0.2.sp
                        )
                    }
                }

                // Clean White Card Container with Top Rounded Corners
                Card(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = minCardHeight)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = minCardHeight)
                            .padding(horizontal = 22.dp, vertical = 18.dp)
                            .navigationBarsPadding(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            // Header Title & Subtitle
                            Text(
                                text = AppStrings.googleSignInTitle(language),
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = AppStrings.googleSignInSubtitle(language),
                                fontSize = 12.5.sp,
                                color = NeutralMedium,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Benefit Cards / Highlights for Permanent ID
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF6FAF7),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Highlight 1: Permanent ID & Moments
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE8F5E9))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CloudDone,
                                                contentDescription = null,
                                                tint = EmeraldGreen,
                                                modifier = Modifier.size(19.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = AppStrings.googleSignInBenefit1(language),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = NeutralDark
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = AppStrings.googleSignInBenefit1Desc(language),
                                                fontSize = 11.5.sp,
                                                color = NeutralMedium,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }

                                    // Highlight 2: Password-Free & Secure
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE3F2FD))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.VerifiedUser,
                                                contentDescription = null,
                                                tint = Color(0xFF1976D2),
                                                modifier = Modifier.size(19.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = AppStrings.googleSignInBenefit2(language),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = NeutralDark
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = AppStrings.googleSignInBenefit2Desc(language),
                                                fontSize = 11.5.sp,
                                                color = NeutralMedium,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }

                                    // Highlight 3: Instant Profile
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFFF3E0))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AccountCircle,
                                                contentDescription = null,
                                                tint = Color(0xFFFB8C00),
                                                modifier = Modifier.size(19.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = AppStrings.googleSignInBenefit3(language),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = NeutralDark
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = AppStrings.googleSignInBenefit3Desc(language),
                                                fontSize = 11.5.sp,
                                                color = NeutralMedium,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom Section: Google Sign-In Action & Privacy
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        ) {
                            // Primary Google Sign-In Button
                            OutlinedButton(
                                onClick = { performGoogleSignIn() },
                                enabled = !isGoogleLoading,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = NeutralDark
                                ),
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                                    brush = Brush.linearGradient(
                                        listOf(
                                            Color(0xFF4285F4),
                                            Color(0xFF34A853),
                                            Color(0xFFFBBC05),
                                            Color(0xFFEA4335)
                                        )
                                    ),
                                    width = 1.3.dp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("btn_google_login")
                            ) {
                                if (isGoogleLoading) {
                                    CircularProgressIndicator(
                                        color = Color(0xFF4285F4),
                                        strokeWidth = 2.5.dp,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = if (language == AppLanguage.INDONESIAN) "Menghubungkan akun Google..." else "Connecting Google account...",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = NeutralMedium
                                    )
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFF1F3F4))
                                        ) {
                                            Text(
                                                text = "G",
                                                fontWeight = FontWeight.Black,
                                                fontSize = 14.sp,
                                                color = Color(0xFF4285F4)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = AppStrings.googleSignInButton(language),
                                            fontSize = 14.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeutralDark
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Trust Badge
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = AppStrings.googleSignInTrustBadge(language),
                                    fontSize = 11.sp,
                                    color = NeutralMedium,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Privacy and Permissions Setup
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { onRequestPermissionSetup() }
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                                    .testTag("btn_login_permission_setup")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.INDONESIAN) "Izin Akses & Privasi Aplikasi" else "App Permissions & Privacy",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldGreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

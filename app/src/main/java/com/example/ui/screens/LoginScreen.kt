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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    detectedGeoArea: String = "ID/MY",
    isLocalMode: Boolean = true,
    onLanguageChange: (com.example.util.AppLanguage) -> Unit = {},
    onLoginSuccess: (name: String) -> Unit,
    onGuestLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }
    var isSignUpMode by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun performGoogleSignIn() {
        if (isGoogleLoading || isSubmitting) return
        isGoogleLoading = true
        scope.launch {
            val result = com.example.util.GoogleAuthHelper.signInWithGoogle(context)
            isGoogleLoading = false
            result.onSuccess { googleUser ->
                snackbarHostState.showSnackbar("Selamat datang, ${googleUser.displayName}!")
                onLoginSuccess(googleUser.displayName)
            }.onFailure { exception ->
                val errorMsg = exception.message ?: "Gagal login dengan Google"
                snackbarHostState.showSnackbar(errorMsg)
            }
        }
    }

    fun performSubmit() {
        val trimmedUser = usernameInput.trim()
        if (trimmedUser.isEmpty()) {
            scope.launch {
                snackbarHostState.showSnackbar("Silakan masukkan nama atau username Anda")
            }
            return
        }

        if (passwordInput.length < 4) {
            scope.launch {
                snackbarHostState.showSnackbar("Kata sandi minimal 4 karakter")
            }
            return
        }

        isSubmitting = true
        scope.launch {
            delay(700)
            isSubmitting = false
            onLoginSuccess(trimmedUser)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ScreenBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            // Compact Emerald Header with Integrated Language Switcher
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF004D40),
                                EmeraldGreen
                            )
                        )
                    )
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 12.dp)
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
                    val isLoActive = isLocalMode && language != com.example.util.AppLanguage.ENGLISH
                    val loDisplayLabel = when (language) {
                        com.example.util.AppLanguage.CHINESE -> "LO (中文)"
                        com.example.util.AppLanguage.JAPANESE -> "LO (日)"
                        com.example.util.AppLanguage.ARABIC -> "LO (ع)"
                        com.example.util.AppLanguage.INDONESIAN -> "LO (ID)"
                        com.example.util.AppLanguage.SPANISH -> "LO (ES)"
                        com.example.util.AppLanguage.KOREAN -> "LO (한)"
                        com.example.util.AppLanguage.FRENCH -> "LO (FR)"
                        com.example.util.AppLanguage.GERMAN -> "LO (DE)"
                        com.example.util.AppLanguage.RUSSIAN -> "LO (RU)"
                        com.example.util.AppLanguage.PORTUGUESE -> "LO (PT)"
                        else -> "LO"
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isLoActive) Color.White.copy(alpha = 0.95f) 
                                else Color.Transparent
                            )
                            .clickable { onLanguageChange(com.example.util.AppLanguage.LOCAL) }
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
                            .clickable { onLanguageChange(com.example.util.AppLanguage.ENGLISH) }
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
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Lovy Chat Icon",
                            tint = EmeraldGreen,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Lovy Chat",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "New friends, fun friends",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFB2DFDB),
                        letterSpacing = 0.2.sp
                    )
                }
            }

            // Clean Form Card Fitting 1 Screen
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 14.dp)
                ) {
                    // Mode Title & Subtitle
                    Text(
                        text = if (isSignUpMode) com.example.util.AppStrings.createAccount(language) else com.example.util.AppStrings.welcomeBack(language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Text(
                        text = if (isSignUpMode) com.example.util.AppStrings.signUpSubtitle(language) else com.example.util.AppStrings.loginSubtitle(language),
                        fontSize = 12.sp,
                        color = NeutralMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Username / Name Field
                    Text(
                        text = com.example.util.AppStrings.usernameLabel(language, isSignUpMode),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = usernameInput,
                        onValueChange = { usernameInput = it },
                        placeholder = { Text(com.example.util.AppStrings.usernamePlaceholder(language), fontSize = 13.sp) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreen,
                            focusedLabelColor = EmeraldGreen
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_username")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Password Field
                    Text(
                        text = com.example.util.AppStrings.passwordLabel(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        placeholder = { Text(com.example.util.AppStrings.passwordPlaceholder(language), fontSize = 13.sp) },
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { isPasswordVisible = !isPasswordVisible },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (isPasswordVisible) "Sembunyikan sandi" else "Tampilkan sandi",
                                    tint = NeutralMedium,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                performSubmit()
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreen,
                            focusedLabelColor = EmeraldGreen
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_password")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Primary Button (Login / Register)
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            performSubmit()
                        },
                        enabled = !isSubmitting && !isGoogleLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_login_submit")
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = com.example.util.AppStrings.btnLogin(language, isSignUpMode),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Separator "atau" / "or"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = NeutralBorder, thickness = 0.6.dp)
                        Text(
                            text = if (language == com.example.util.AppLanguage.INDONESIAN) "atau" else "or",
                            fontSize = 11.5.sp,
                            color = NeutralMedium,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = NeutralBorder, thickness = 0.6.dp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Google One-Click Login Button
                    OutlinedButton(
                        onClick = { performGoogleSignIn() },
                        enabled = !isGoogleLoading && !isSubmitting,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = NeutralDark
                        ),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF4285F4), Color(0xFF34A853), Color(0xFFFBBC05), Color(0xFFEA4335))
                            ),
                            width = 1.1.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("btn_google_login")
                    ) {
                        if (isGoogleLoading) {
                            CircularProgressIndicator(
                                color = Color(0xFF4285F4),
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF1F3F4))
                                ) {
                                    Text(
                                        text = "G",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = Color(0xFF4285F4)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == com.example.util.AppLanguage.INDONESIAN) "Lanjutkan dengan Akun Google" else "Continue with Google Account",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NeutralDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Quick Guest / Explore Button
                    OutlinedButton(
                        onClick = onGuestLogin,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("btn_guest_login")
                    ) {
                        Text(
                            text = com.example.util.AppStrings.btnGuest(language),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Toggle Login / Sign Up
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isSignUpMode) com.example.util.AppStrings.alreadyHaveAccount(language) else com.example.util.AppStrings.dontHaveAccount(language),
                            fontSize = 12.5.sp,
                            color = NeutralMedium
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (isSignUpMode) com.example.util.AppStrings.signInAction(language) else com.example.util.AppStrings.signUpAction(language),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen,
                            modifier = Modifier
                                .clickable { isSignUpMode = !isSignUpMode }
                                .testTag("btn_toggle_auth_mode")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtle Footer Motto Reminder
                    Text(
                        text = "✨ Lovy Chat — ${com.example.util.AppStrings.motto(language)} ✨",
                        fontSize = 11.sp,
                        color = NeutralMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

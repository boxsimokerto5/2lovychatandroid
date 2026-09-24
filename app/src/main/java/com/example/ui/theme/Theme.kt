package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = EmeraldGreen,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = EmeraldGreenDark,
    secondary = AccentCyan,
    background = ScreenBackground,
    surface = androidx.compose.ui.graphics.Color.White,
    onBackground = NeutralDark,
    onSurface = NeutralDark,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EmeraldGreen,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = EmeraldGreenLight,
    secondary = AccentCyan,
    background = ScreenBackground,
    surface = androidx.compose.ui.graphics.Color.White,
    onBackground = NeutralDark,
    onSurface = NeutralDark,
  )

@Composable
fun LovyChatTheme(
  darkTheme: Boolean = false, // Tetap gunakan palet terang beraksen zamrud agar teks selalu kontras dan jelas di semua perangkat
  // For Lovy Chat brand identity, default dynamicColor to false so our emerald branding shines
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

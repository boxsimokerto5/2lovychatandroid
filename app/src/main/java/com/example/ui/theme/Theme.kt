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
    background = androidx.compose.ui.graphics.Color(0xFF121212),
    surface = androidx.compose.ui.graphics.Color(0xFF1E1E1E),
    onSurface = androidx.compose.ui.graphics.Color.White,
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
  darkTheme: Boolean = isSystemInDarkTheme(),
  // For Lovy Chat brand identity, default dynamicColor to false so our emerald branding shines
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

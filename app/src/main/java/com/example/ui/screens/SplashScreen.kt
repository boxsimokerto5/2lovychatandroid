package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.EmeraldGreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onPermissionResult: (Boolean) -> Unit = {},
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }
    val pulse = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        val fineCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fineCheck == PackageManager.PERMISSION_GRANTED || coarseCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionResult(true)
        }
    }

    LaunchedEffect(Unit) {
        // Fade & Scale in
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )
        // Gentle pulse
        pulse.animateTo(
            targetValue = 1.06f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 900),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(2200)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF004D40),
                        EmeraldGreen,
                        Color(0xFF004D40)
                    )
                )
            )
            .testTag("screen_splash"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale.value * pulse.value)
                .alpha(alpha.value)
        ) {
            // App Logo Icon Container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Lovy Chat Logo",
                    tint = EmeraldGreen,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "Lovy Chat",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // User Requested Motto
            Text(
                text = "New friends, fun friends",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFB2DFDB),
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Subtle loader
            CircularProgressIndicator(
                color = Color.White.copy(alpha = 0.7f),
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(24.dp)
            )
        }

        // Bottom Brand / Version Hint
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .alpha(alpha.value)
        ) {
            Icon(
                imageVector = Icons.Default.Waves,
                contentDescription = null,
                tint = Color(0xFF80CBC4),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = " " + com.example.util.AppStrings.splashSub(language),
                fontSize = 12.sp,
                color = Color(0xFFB2DFDB)
            )
        }
    }
}

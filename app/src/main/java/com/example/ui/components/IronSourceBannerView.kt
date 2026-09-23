package com.example.ui.components

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralMedium
import com.example.util.AdManager
import com.ironsource.mediationsdk.IronSourceBannerLayout

/**
 * Clean, responsive Banner Ad Composable powered by ironSource LevelPlay.
 * Neatly styled with a subtle divider, standard 50dp ad slot, and discreet sponsored tag.
 * Safely creates, attaches, and disposes of the ironSource Banner.
 */
@Composable
fun IronSourceBannerView(
    modifier: Modifier = Modifier,
    applyNavigationBarsPadding: Boolean = false
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return

    var bannerLayout by remember { mutableStateOf<IronSourceBannerLayout?>(null) }
    var isLoaded by remember { mutableStateOf(false) }
    var retryTrigger by remember { mutableStateOf(0) }
    val isSdkInitialized by AdManager.isSdkInitialized.collectAsState()

    androidx.compose.runtime.LaunchedEffect(activity, isSdkInitialized, retryTrigger) {
        if (!isLoaded) {
            // Berikan jeda kecil untuk memastikan handshake ironSource SDK tuntas
            if (!isSdkInitialized) {
                kotlinx.coroutines.delay(1200L)
            }
            bannerLayout?.let {
                AdManager.destroyBanner(it)
                bannerLayout = null
            }
            val banner = AdManager.createBanner(
                activity = activity,
                onBannerLoaded = {
                    isLoaded = true
                },
                onBannerFailed = { error ->
                    isLoaded = false
                    android.util.Log.w("IronSourceBannerView", "Banner failed: $error, retrying in 12s...")
                }
            )
            bannerLayout = banner
            // Jika dalam 12 detik belum load, coba request ulang secara bersih
            kotlinx.coroutines.delay(12000L)
            if (!isLoaded) {
                retryTrigger++
            }
        }
    }

    DisposableEffect(activity) {
        onDispose {
            AdManager.destroyBanner(bannerLayout)
            bannerLayout = null
        }
    }

    AnimatedVisibility(
        visible = isLoaded && bannerLayout != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        bannerLayout?.let { banner ->
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .then(if (applyNavigationBarsPadding) Modifier.navigationBarsPadding() else Modifier)
                    .testTag("iron_source_banner_container"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(color = NeutralBorder, thickness = 0.5.dp)

                // Subtle sponsored indicator badge (rapat & minimalis)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFFEEEEEE))
                            .padding(horizontal = 4.dp, vertical = 0.5.dp)
                    ) {
                        Text(
                            text = "IKLAN",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeutralMedium,
                            letterSpacing = 0.4.sp
                        )
                    }
                }

                // Banner ad content area (standard 320x50 banner)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        factory = { ctx ->
                            FrameLayout(ctx).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                try {
                                    (banner.parent as? ViewGroup)?.removeView(banner)
                                    addView(banner)
                                } catch (e: Throwable) {
                                    android.util.Log.w("IronSourceBannerView", "Error attaching banner view", e)
                                }
                            }
                        },
                        update = { container ->
                            try {
                                if (banner.parent != container) {
                                    (banner.parent as? ViewGroup)?.removeView(banner)
                                    container.removeAllViews()
                                    container.addView(banner)
                                }
                            } catch (e: Throwable) {
                                android.util.Log.w("IronSourceBannerView", "Error updating banner view", e)
                            }
                        }
                    )
                }
            }
        }
    }
}

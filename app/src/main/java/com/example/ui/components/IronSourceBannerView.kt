package com.example.ui.components

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.NeutralBorder
import com.example.util.AdManager
import com.ironsource.mediationsdk.IronSourceBannerLayout

/**
 * Clean, responsive Banner Ad Composable powered by ironSource LevelPlay.
 * Safely creates, renders, and disposes of the ironSource Banner.
 */
@Composable
fun IronSourceBannerView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return

    var bannerLayout by remember { mutableStateOf<IronSourceBannerLayout?>(null) }
    var isLoaded by remember { mutableStateOf(false) }

    DisposableEffect(activity) {
        val banner = AdManager.createBanner(
            activity = activity,
            onBannerLoaded = {
                isLoaded = true
            },
            onBannerFailed = { _ ->
                isLoaded = false
            }
        )
        bannerLayout = banner

        onDispose {
            AdManager.destroyBanner(banner)
            bannerLayout = null
        }
    }

    if (isLoaded && bannerLayout != null) {
        val banner = bannerLayout!!
        androidx.compose.foundation.layout.Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(color = NeutralBorder, thickness = 0.6.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFF9FAFB)),
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

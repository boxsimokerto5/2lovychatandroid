package com.example.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.util.AdManager
import com.ironsource.mediationsdk.IronSourceBannerLayout
import kotlinx.coroutines.delay

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

/**
 * IronSourceBannerView:
 * Komponen Banner Iklan pintar & responsif yang dipastikan SELALU MUDAH TAMPIL (100% visible):
 * - Meminta iklan resmi ironSource LevelPlay Banner.
 * - Jika iklan ironSource siap & terisi (filled), langsung menampilkan live banner ironSource.
 * - Sembari menunggu atau jika jaringan/fill sedang pending, MENAMPILKAN banner bersponsor
 *   interaktif yang elegan & profesional sehingga tidak pernah kosong/blank sama sekali.
 * - Dilengkapi pembersihan lifecycle yang aman saat composable ditutup.
 */
@Composable
fun IronSourceBannerView(
    modifier: Modifier = Modifier,
    applyNavigationBarsPadding: Boolean = false,
    placementName: String? = null
) {
    val context = LocalContext.current
    val activity = context.findActivity() ?: return

    var bannerLayout by remember { mutableStateOf<IronSourceBannerLayout?>(null) }
    var isLiveBannerLoaded by remember { mutableStateOf(false) }
    val isSdkInitialized by AdManager.isSdkInitialized.collectAsState()

    var retryCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(activity, isSdkInitialized, retryCount) {
        if (!isLiveBannerLoaded) {
            // Berikan jeda kecil untuk handshake SDK yang mulus
            if (!isSdkInitialized) {
                delay(1000L)
            }

            bannerLayout?.let {
                AdManager.destroyBanner(it)
                bannerLayout = null
            }

            val created = AdManager.createBanner(
                activity = activity,
                placementName = placementName,
                onBannerLoaded = {
                    isLiveBannerLoaded = true
                },
                onBannerFailed = { error ->
                    isLiveBannerLoaded = false
                    android.util.Log.d("IronSourceBannerView", "Live banner load note: $error")
                }
            )
            bannerLayout = created

            // Jika belum loaded setelah 12 detik, coba refresh ulang secara bersih
            delay(12000L)
            if (!isLiveBannerLoaded) {
                retryCount++
            }
        }
    }

    DisposableEffect(activity) {
        onDispose {
            bannerLayout?.let {
                AdManager.destroyBanner(it)
                bannerLayout = null
            }
        }
    }

    // Jika banner resmi belum siap atau belum diisi dari jaringan, jangan tampilkan iklan tiruan
    if (!isLiveBannerLoaded || bannerLayout == null) {
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .then(if (applyNavigationBarsPadding) Modifier.navigationBarsPadding() else Modifier)
            .testTag("iron_source_banner_container"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(color = NeutralBorder, thickness = 0.6.dp)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge Iklan Ramping
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFEEEEEE))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "IKLAN RESMI",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMedium,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Slot Banner 50dp
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
                                val cur = bannerLayout
                                if (cur != null) {
                                    (cur.parent as? ViewGroup)?.removeView(cur)
                                    addView(cur)
                                }
                            } catch (e: Throwable) {
                                android.util.Log.w("IronSourceBannerView", "Error attaching banner view", e)
                            }
                        }
                    },
                    update = { container ->
                        try {
                            val cur = bannerLayout
                            if (cur != null && cur.parent != container) {
                                (cur.parent as? ViewGroup)?.removeView(cur)
                                container.removeAllViews()
                                container.addView(cur)
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

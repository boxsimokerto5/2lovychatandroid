package com.example.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color as AndroidColor
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.util.AdManager
import com.example.util.SponsoredAdContent
import com.ironsource.mediationsdk.ads.nativead.LevelPlayMediaView
import com.ironsource.mediationsdk.ads.nativead.LevelPlayNativeAd
import com.ironsource.mediationsdk.ads.nativead.NativeAdLayout

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

private class NativeViewsHolder(
    val titleView: TextView,
    val advertiserView: TextView,
    val bodyView: TextView,
    val iconView: ImageView,
    val ctaView: TextView,
    val mediaView: LevelPlayMediaView
)

/**
 * LevelPlayNativeAdCard:
 * Komponen Native Ad resmi ironSource LevelPlay untuk Lovy Chat:
 * - Meminta iklan native dari ironSource mediation.
 * - Mengikat (binding) data iklan asli (title, advertiser, body, callToAction, media view) secara akurat.
 * - Jika iklan live masih menunggu pengisian (fill-pending), menampilkan kartu bersponsor
 *   cantik dengan gaya yang menyatu harmonis dengan feed momen dan radar pengguna sekitar.
 * - Memastikan iklan SELALU MUDAH TAMPIL tanpa ruang kosong yang membingungkan pengguna.
 */
@Composable
fun LevelPlayNativeAdCard(
    modifier: Modifier = Modifier,
    placementName: String? = null,
    testTag: String = "iron_source_native_ad_card"
) {
    val context = LocalContext.current
    val activity = context.findActivity() ?: return

    var nativeAd by remember { mutableStateOf<LevelPlayNativeAd?>(null) }
    var isLiveAdLoaded by remember { mutableStateOf(false) }
    val isSdkInitialized by AdManager.isSdkInitialized.collectAsState()

    LaunchedEffect(activity, isSdkInitialized) {
        if (!isLiveAdLoaded) {
            val ad = AdManager.createNativeAd(
                activity = activity,
                placementName = placementName,
                onAdLoaded = { loadedAd ->
                    nativeAd = loadedAd
                    isLiveAdLoaded = true
                },
                onAdFailed = {
                    isLiveAdLoaded = false
                }
            )
            nativeAd = ad
        }
    }

    DisposableEffect(activity) {
        onDispose {
            nativeAd?.let {
                AdManager.destroyNativeAd(it)
                nativeAd = null
            }
            isLiveAdLoaded = false
        }
    }

    // Jika native ad resmi belum dimuat dari ironSource, jangan tampilkan iklan tiruan
    if (!isLiveAdLoaded || nativeAd == null) {
        return
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag(testTag)
    ) {
        // Official ironSource LevelPlay Native Ad View
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
                    factory = { ctx ->
                        val density = ctx.resources.displayMetrics.density
                        fun dpToPx(dp: Int): Int = (dp * density).toInt()

                        NativeAdLayout(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )

                            val rootLayout = LinearLayout(ctx).apply {
                                orientation = LinearLayout.VERTICAL
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                            }

                            // Header Layout
                            val headerLayout = LinearLayout(ctx).apply {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER_VERTICAL
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    bottomMargin = dpToPx(10)
                                }
                            }

                            val iconView = ImageView(ctx).apply {
                                layoutParams = LinearLayout.LayoutParams(dpToPx(42), dpToPx(42)).apply {
                                    rightMargin = dpToPx(10)
                                }
                                scaleType = ImageView.ScaleType.CENTER_CROP
                                background = GradientDrawable().apply {
                                    shape = GradientDrawable.OVAL
                                    setColor(AndroidColor.parseColor("#E0F2F1"))
                                }
                                clipToOutline = true
                            }
                            headerLayout.addView(iconView)
                            setIconView(iconView)

                            val infoLayout = LinearLayout(ctx).apply {
                                orientation = LinearLayout.VERTICAL
                                layoutParams = LinearLayout.LayoutParams(
                                    0,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    1f
                                )
                            }

                            val titleRow = LinearLayout(ctx).apply {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER_VERTICAL
                            }

                            val adBadge = TextView(ctx).apply {
                                text = "AD"
                                textSize = 9.5f
                                setTypeface(typeface, Typeface.BOLD)
                                setTextColor(AndroidColor.parseColor("#00897B"))
                                setPadding(dpToPx(5), dpToPx(1), dpToPx(5), dpToPx(1))
                                background = GradientDrawable().apply {
                                    cornerRadius = dpToPx(4).toFloat()
                                    setColor(AndroidColor.parseColor("#E0F2F1"))
                                }
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    rightMargin = dpToPx(6)
                                }
                            }
                            titleRow.addView(adBadge)

                            val titleView = TextView(ctx).apply {
                                textSize = 14f
                                setTypeface(typeface, Typeface.BOLD)
                                setTextColor(AndroidColor.parseColor("#1F2937"))
                                maxLines = 1
                                ellipsize = android.text.TextUtils.TruncateAt.END
                            }
                            titleRow.addView(titleView)
                            setTitleView(titleView)
                            infoLayout.addView(titleRow)

                            val advertiserView = TextView(ctx).apply {
                                textSize = 11.5f
                                setTextColor(AndroidColor.parseColor("#6B7280"))
                                maxLines = 1
                                ellipsize = android.text.TextUtils.TruncateAt.END
                            }.apply {
                                text = "Bersponsor"
                            }
                            setAdvertiserView(advertiserView)
                            infoLayout.addView(advertiserView)

                            headerLayout.addView(infoLayout)
                            rootLayout.addView(headerLayout)

                            // Body text
                            val bodyView = TextView(ctx).apply {
                                textSize = 12.5f
                                setTextColor(AndroidColor.parseColor("#374151"))
                                maxLines = 3
                                ellipsize = android.text.TextUtils.TruncateAt.END
                                setLineSpacing(dpToPx(2).toFloat(), 1f)
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    bottomMargin = dpToPx(10)
                                }
                            }
                            setBodyView(bodyView)
                            rootLayout.addView(bodyView)

                            // Media view
                            val mediaView = LevelPlayMediaView(ctx).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    dpToPx(150)
                                ).apply {
                                    bottomMargin = dpToPx(12)
                                }
                                background = GradientDrawable().apply {
                                    cornerRadius = dpToPx(12).toFloat()
                                    setColor(AndroidColor.parseColor("#F9FAFB"))
                                }
                                clipToOutline = true
                            }
                            setMediaView(mediaView)
                            rootLayout.addView(mediaView)

                            // CTA button
                            val ctaView = TextView(ctx).apply {
                                text = "Kunjungi"
                                textSize = 13.5f
                                setTypeface(typeface, Typeface.BOLD)
                                setTextColor(AndroidColor.WHITE)
                                gravity = Gravity.CENTER
                                background = GradientDrawable().apply {
                                    cornerRadius = dpToPx(12).toFloat()
                                    colors = intArrayOf(
                                        AndroidColor.parseColor("#00897B"),
                                        AndroidColor.parseColor("#004D40")
                                    )
                                    orientation = GradientDrawable.Orientation.LEFT_RIGHT
                                }
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    dpToPx(42)
                                )
                            }
                            setCallToActionView(ctaView)
                            rootLayout.addView(ctaView)

                            addView(rootLayout)

                            // Simpan holder view ke dalam tag layout agar dapat diperbarui di update block
                            tag = NativeViewsHolder(
                                titleView = titleView,
                                advertiserView = advertiserView,
                                bodyView = bodyView,
                                iconView = iconView,
                                ctaView = ctaView,
                                mediaView = mediaView
                            )
                        }
                    },
                    update = { nativeAdLayout ->
                        nativeAd?.let { ad ->
                            try {
                                val holder = nativeAdLayout.tag as? NativeViewsHolder
                                if (holder != null) {
                                    if (!ad.title.isNullOrBlank()) {
                                        holder.titleView.text = ad.title
                                    }
                                    if (!ad.advertiser.isNullOrBlank()) {
                                        holder.advertiserView.text = ad.advertiser
                                    }
                                    if (!ad.body.isNullOrBlank()) {
                                        holder.bodyView.text = ad.body
                                    }
                                    if (!ad.callToAction.isNullOrBlank()) {
                                        holder.ctaView.text = ad.callToAction
                                    }
                                    val iconDrawable = ad.icon?.drawable
                                    if (iconDrawable != null) {
                                        holder.iconView.setImageDrawable(iconDrawable)
                                    }
                                }

                                nativeAdLayout.registerNativeAdViews(ad)
                            } catch (e: Throwable) {
                                android.util.Log.w("LevelPlayNativeAdCard", "Error updating native ad view", e)
                            }
                        }
                    }
                )
    }
}

@Composable
private fun SponsoredNativeCardFallback(
    ad: SponsoredAdContent,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(ad.primaryColorHex), Color(ad.secondaryColorHex))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = ad.iconEmoji, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0F2F1))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "IKLAN",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen,
                            letterSpacing = 0.4.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = ad.advertiser,
                        fontSize = 12.sp,
                        color = NeutralMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Sponsor",
                        tint = EmeraldGreen,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = ad.title,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Body Description
        Text(
            text = ad.description,
            fontSize = 13.sp,
            color = Color(0xFF374151),
            lineHeight = 18.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Media Banner Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(ad.primaryColorHex).copy(alpha = 0.85f),
                            Color(ad.secondaryColorHex)
                        )
                    )
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = ad.iconEmoji,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ad.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = ad.category,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // CTA Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(ad.primaryColorHex), Color(ad.secondaryColorHex))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ad.callToAction,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

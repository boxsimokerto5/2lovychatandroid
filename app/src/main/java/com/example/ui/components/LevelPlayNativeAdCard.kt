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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.util.AdManager
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

/**
 * LevelPlayNativeAdCard:
 * Komponen Native Ad ironSource resmi untuk Lovy Chat.
 * Menampilkan tata letak kartu bersponsor yang rapi, elegan, dan membaur harmonis
 * dengan kartu momen dan kartu pengguna sekitar, lengkap dengan lencana "Bersponsor / Ad",
 * judul pengiklan, deskripsi, icon, media player/view, dan tombol aksi (CTA).
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
    var isLoaded by remember { mutableStateOf(false) }

    DisposableEffect(activity) {
        val ad = AdManager.createNativeAd(
            activity = activity,
            placementName = placementName,
            onAdLoaded = { loadedAd ->
                nativeAd = loadedAd
                isLoaded = true
            },
            onAdFailed = {
                isLoaded = false
            }
        )

        onDispose {
            AdManager.destroyNativeAd(ad)
            nativeAd = null
            isLoaded = false
        }
    }

    AnimatedVisibility(
        visible = isLoaded && nativeAd != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .testTag(testTag)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                factory = { ctx ->
                    val density = ctx.resources.displayMetrics.density
                    fun dpToPx(dp: Int): Int = (dp * density).toInt()

                    // NativeAdLayout dari ironSource LevelPlay
                    NativeAdLayout(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )

                        // Kontainer vertikal utama
                        val rootLayout = LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        }

                        // 1. Header: Badge Iklan + Icon Pengiklan + Nama Pengiklan / Judul
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

                        // Icon pengiklan
                        val iconView = ImageView(ctx).apply {
                            id = View.generateViewId()
                            layoutParams = LinearLayout.LayoutParams(dpToPx(42), dpToPx(42)).apply {
                                rightMargin = dpToPx(10)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            background = GradientDrawable().apply {
                                shape = GradientDrawable.OVAL
                                setColor(AndroidColor.parseColor("#F5F5F5"))
                            }
                            clipToOutline = true
                        }
                        headerLayout.addView(iconView)
                        setIconView(iconView)

                        // Kolom Info Pengiklan
                        val infoLayout = LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1f
                            )
                        }

                        // Baris Judul & Badge Sponsored
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
                            id = View.generateViewId()
                            textSize = 14f
                            setTypeface(typeface, Typeface.BOLD)
                            setTextColor(AndroidColor.parseColor("#1F2937"))
                            maxLines = 1
                            ellipsize = android.text.TextUtils.TruncateAt.END
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        }
                        titleRow.addView(titleView)
                        setTitleView(titleView)
                        infoLayout.addView(titleRow)

                        // Nama Pengiklan / Advertiser
                        val advertiserView = TextView(ctx).apply {
                            id = View.generateViewId()
                            textSize = 11.5f
                            setTextColor(AndroidColor.parseColor("#6B7280"))
                            maxLines = 1
                            ellipsize = android.text.TextUtils.TruncateAt.END
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            ).apply {
                                topMargin = dpToPx(2)
                            }
                        }
                        advertiserView.text = "Bersponsor"
                        setAdvertiserView(advertiserView)
                        infoLayout.addView(advertiserView)

                        headerLayout.addView(infoLayout)
                        rootLayout.addView(headerLayout)

                        // 2. Body Text (Deskripsi Promosi)
                        val bodyView = TextView(ctx).apply {
                            id = View.generateViewId()
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

                        // 3. LevelPlayMediaView (Media Player / Banner Kreatif)
                        val mediaView = LevelPlayMediaView(ctx).apply {
                            id = View.generateViewId()
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                dpToPx(160)
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

                        // 4. Tombol Call To Action (CTA)
                        val ctaView = TextView(ctx).apply {
                            id = View.generateViewId()
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
                    }
                },
                update = { nativeAdLayout ->
                    nativeAd?.let { ad ->
                        try {
                            // Update views content with ironSource LevelPlay Native Ad data
                            val title = ad.title
                            if (!title.isNullOrBlank()) {
                                (nativeAdLayout.findViewById<TextView?>(View.generateViewId()))
                            }

                            // Daftarkan komponen view ke SDK ironSource untuk interaksi dan impresi otomatis
                            nativeAdLayout.registerNativeAdViews(ad)
                        } catch (e: Throwable) {
                            android.util.Log.w("LevelPlayNativeAdCard", "Error updating native ad view", e)
                        }
                    }
                }
            )
        }
    }
}

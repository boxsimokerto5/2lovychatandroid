package com.example.util

import android.app.Activity
import android.util.Log
import com.example.LovyApplication
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.ads.nativead.LevelPlayNativeAd
import com.ironsource.mediationsdk.ads.nativead.LevelPlayNativeAdListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Data model for rich sponsored ad content when ad mediation is in fill-pending or fallback mode.
 */
data class SponsoredAdContent(
    val id: String,
    val title: String,
    val advertiser: String,
    val description: String,
    val callToAction: String,
    val iconEmoji: String = "✨",
    val category: String = "Sponsor Resmi",
    val primaryColorHex: Long = 0xFF00897B,
    val secondaryColorHex: Long = 0xFF004D40,
    val targetUrl: String? = null
)

/**
 * Status information for all ad units to allow real-time debugging and display.
 */
data class AdStatusInfo(
    val isSdkInitialized: Boolean,
    val isInterstitialReady: Boolean,
    val isRewardedReady: Boolean,
    val featureClicksCount: Int,
    val featureClicksThreshold: Int,
    val totalImpressionsCount: Int
)

/**
 * Manages ironSource (Unity LevelPlay) Ads integration:
 * - SDK Initialization with App Key
 * - Banner Ad creation, loading, automatic refresh, and disposal
 * - Interstitial Ads loading, smart threshold triggering (20 clicks/actions), and display
 * - Rewarded Video Ads display with reward callback & seamless fallback
 * - Comprehensive Native Ad setup and robust state handling
 */
object AdManager {
    private const val TAG = "AdManager"

    private val adScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // App Key from the user's ironSource / LevelPlay dashboard for Lovy Chat
    val IRONSOURCE_APP_KEY: String
        get() {
            val build = try {
                val field = com.example.BuildConfig::class.java.getField("IRONSOURCE_APP_KEY")
                field.get(null) as? String ?: ""
            } catch (_: Throwable) { "" }
            return if (build.isNotBlank() && !build.startsWith("your_")) build
            else "283361415"
        }

    // ironSource Ad Unit IDs
    const val AD_UNIT_NATIVE_ID = "2f06kx1nra7a3jny"
    const val AD_UNIT_BANNER_ID = "yuqh9gyjbtqwd7lw"
    const val AD_UNIT_INTERSTITIAL_ID = "677846ihi3c83989"
    const val AD_UNIT_REWARDED_ID = "gyh6pbj3hekz8bku"

    private var isInitialized = false
    private val _isSdkInitialized = MutableStateFlow(false)
    val isSdkInitialized: StateFlow<Boolean> = _isSdkInitialized.asStateFlow()

    private var currentActivityRef: java.lang.ref.WeakReference<Activity>? = null

    private val _isInterstitialReady = MutableStateFlow(false)
    val isInterstitialReady: StateFlow<Boolean> = _isInterstitialReady.asStateFlow()

    private val _isRewardedReady = MutableStateFlow(false)
    val isRewardedReady: StateFlow<Boolean> = _isRewardedReady.asStateFlow()

    private var onUserRewardedCallback: (() -> Unit)? = null

    // Fallback sponsored ad dialog states (for seamless presentation when mediation has no fill)
    private val _activeInterstitialAd = MutableStateFlow<SponsoredAdContent?>(null)
    val activeInterstitialAd: StateFlow<SponsoredAdContent?> = _activeInterstitialAd.asStateFlow()

    private val _activeRewardedAd = MutableStateFlow<SponsoredAdContent?>(null)
    val activeRewardedAd: StateFlow<SponsoredAdContent?> = _activeRewardedAd.asStateFlow()

    private var totalImpressions = 0

    // Iklan palsu/tiruan telah dihapus sepenuhnya sesuai permintaan pengguna
    val SPONSORED_FALLBACK_ADS = emptyList<SponsoredAdContent>()

    fun getNextSponsoredAd(): SponsoredAdContent? = null

    /**
     * Update or refresh current active Activity reference.
     */
    fun updateCurrentActivity(activity: Activity?) {
        currentActivityRef = if (activity != null) java.lang.ref.WeakReference(activity) else null
    }

    /**
     * Initialize ironSource SDK with Lovy Chat App Key.
     * Must be called in Activity onCreate.
     */
    fun init(activity: Activity) {
        updateCurrentActivity(activity)
        if (isInitialized) return

        try {
            Log.d(TAG, "Initializing ironSource SDK with App Key: $IRONSOURCE_APP_KEY")

            // Ambil dan tampilkan GAID untuk mempermudah pendaftaran Test Device di Dashboard ironSource
            adScope.launch(Dispatchers.IO) {
                try {
                    val adInfo = com.google.android.gms.ads.identifier.AdvertisingIdClient.getAdvertisingIdInfo(activity)
                    val gaid = adInfo.id
                    Log.i(TAG, "📌 [ironSource Test Device GAID]: $gaid")
                    Log.i(TAG, "💡 Untuk memunculkan iklan test di ironSource, daftarkan GAID di LevelPlay Dashboard: Settings -> Test Devices")
                } catch (e: Throwable) {
                    Log.d(TAG, "Note: GAID tidak dapat dibaca di emulator/perangkat tanpa Google Play Services: ${e.message}")
                }
            }

            // Setup listeners for Interstitial and Rewarded Video
            setupInterstitialListener()
            setupRewardedVideoListener()

            // Set metadata flags to assist ad fill for live production
            try {
                IronSource.setMetaData("is_child_directed", "false")
                IronSource.setMetaData("is_deviceid_optout", "false")
                IronSource.setMetaData("Google_Family_Policy", "false")
                if (com.example.BuildConfig.DEBUG) {
                    IronSource.setMetaData("is_test_suite", "enable")
                }
            } catch (e: Throwable) {
                Log.w(TAG, "Could not set metadata: ${e.message}")
            }

            // Initialize Yandex Mobile Ads to warm up mediation network
            try {
                com.yandex.mobile.ads.common.MobileAds.initialize(activity) {
                    Log.d(TAG, "Yandex Mobile Ads SDK initialized for ironSource mediation")
                }
            } catch (t: Throwable) {
                Log.d(TAG, "Yandex Mobile Ads init note: ${t.message}")
            }

            // Initialize Meta Audience Network (Facebook Ads) to warm up mediation network
            try {
                com.facebook.ads.AudienceNetworkAds.initialize(activity)
                Log.d(TAG, "Meta Audience Network SDK initialized for ironSource mediation")
            } catch (t: Throwable) {
                Log.d(TAG, "Meta Audience Network init note: ${t.message}")
            }

            // Initialize IronSource with Banner, Interstitial, Rewarded Video, and Native Ad
            IronSource.init(
                activity,
                IRONSOURCE_APP_KEY,
                {
                    Log.d(TAG, "IronSource initialization completed successfully via listener")
                    isInitialized = true
                    _isSdkInitialized.value = true
                    // Automatically load interstitial once initialized
                    loadInterstitial()
                },
                IronSource.AD_UNIT.BANNER,
                IronSource.AD_UNIT.INTERSTITIAL,
                IronSource.AD_UNIT.REWARDED_VIDEO,
                IronSource.AD_UNIT.NATIVE_AD
            )

            // Tandai initialized secara internal agar tidak dipanggil berulang
            isInitialized = true

            // Set initialized flow to true after brief delay as fail-safe in case listener is asynchronous
            adScope.launch {
                delay(1500L)
                if (!_isSdkInitialized.value) {
                    _isSdkInitialized.value = true
                    loadInterstitial()
                }
            }

            // Track network state to automatically resume ads on reconnection
            try {
                IronSource.shouldTrackNetworkState(activity, true)
                com.ironsource.mediationsdk.integration.IntegrationHelper.validateIntegration(activity)
            } catch (t: Throwable) {
                Log.w(TAG, "Integration validation note: ${t.message}")
            }

            // Automatically load interstitial
            loadInterstitial()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing ironSource SDK: ${e.message}", e)
            _isSdkInitialized.value = true
        }
    }

    /**
     * Create and load a Banner ad view for the given Activity.
     */
    fun createBanner(
        activity: Activity,
        placementName: String? = null,
        onBannerLoaded: () -> Unit = {},
        onBannerFailed: (String) -> Unit = {}
    ): IronSourceBannerLayout? {
        return try {
            val bannerLayout = IronSource.createBanner(activity, ISBannerSize.BANNER)
            var hasRetriedAlternative = false
            val primaryPlacement = placementName?.takeIf { it.isNotBlank() } ?: AD_UNIT_BANNER_ID.takeIf { it.isNotBlank() }

            bannerLayout.levelPlayBannerListener = object : LevelPlayBannerListener {
                override fun onAdLoaded(adInfo: AdInfo) {
                    Log.d(TAG, "Banner ad loaded successfully: ${adInfo.adNetwork}")
                    totalImpressions++
                    onBannerLoaded()
                }

                override fun onAdLoadFailed(error: IronSourceError) {
                    val helpTip = when (error.errorCode) {
                        508, 510 -> " (No Fill / Stok Iklan Kosong: Di ironSource mode test harus didaftarkan per-device GAID di LevelPlay Dashboard > Settings > Test Devices)"
                        520 -> " (Ad Unit Banner / Placement belum aktif atau nama placement tidak cocok di dashboard ironSource)"
                        else -> ""
                    }
                    Log.w(TAG, "Banner ad load failed: ${error.errorMessage} [code: ${error.errorCode}]$helpTip")

                    // Coba alternatif placement jika sebelumnya gagal karena placement name
                    if (!hasRetriedAlternative) {
                        hasRetriedAlternative = true
                        if (primaryPlacement != null) {
                            Log.d(TAG, "Mencoba loadBanner ulang tanpa placement name (DefaultBanner)...")
                            try {
                                IronSource.loadBanner(bannerLayout)
                                return
                            } catch (_: Throwable) {}
                        } else if (AD_UNIT_BANNER_ID.isNotBlank()) {
                            Log.d(TAG, "Mencoba loadBanner ulang dengan AD_UNIT_BANNER_ID ($AD_UNIT_BANNER_ID)...")
                            try {
                                IronSource.loadBanner(bannerLayout, AD_UNIT_BANNER_ID)
                                return
                            } catch (_: Throwable) {}
                        }
                    }

                    onBannerFailed(error.errorMessage)
                }

                override fun onAdClicked(adInfo: AdInfo) {
                    Log.d(TAG, "Banner ad clicked")
                }

                override fun onAdLeftApplication(adInfo: AdInfo) {
                    Log.d(TAG, "Banner ad caused user to leave app")
                }

                override fun onAdScreenPresented(adInfo: AdInfo) {
                    Log.d(TAG, "Banner ad screen presented")
                }

                override fun onAdScreenDismissed(adInfo: AdInfo) {
                    Log.d(TAG, "Banner ad screen dismissed")
                }
            }

            if (primaryPlacement != null) {
                IronSource.loadBanner(bannerLayout, primaryPlacement)
            } else {
                IronSource.loadBanner(bannerLayout)
            }
            bannerLayout
        } catch (e: Exception) {
            Log.e(TAG, "Error creating banner: ${e.message}", e)
            onBannerFailed(e.message ?: "Failed to create banner")
            null
        }
    }

    /**
     * Destroy a banner view safely.
     */
    fun destroyBanner(banner: IronSourceBannerLayout?) {
        if (banner != null) {
            try {
                IronSource.destroyBanner(banner)
                Log.d(TAG, "Banner destroyed")
            } catch (e: Exception) {
                Log.e(TAG, "Error destroying banner: ${e.message}", e)
            }
        }
    }

    /**
     * Create and load a LevelPlay Native Ad for the given Activity.
     */
    fun createNativeAd(
        activity: Activity,
        placementName: String? = null,
        onAdLoaded: (LevelPlayNativeAd) -> Unit = {},
        onAdFailed: (String) -> Unit = {}
    ): LevelPlayNativeAd? {
        return try {
            val builder = LevelPlayNativeAd.Builder()
                .withActivity(activity)

            if (!placementName.isNullOrBlank() && placementName != AD_UNIT_NATIVE_ID) {
                builder.withPlacementName(placementName)
            }

            builder.withListener(object : LevelPlayNativeAdListener {
                override fun onAdLoaded(nativeAd: LevelPlayNativeAd?, adInfo: AdInfo?) {
                    Log.d(TAG, "Native ad loaded successfully: ${adInfo?.adNetwork}")
                    totalImpressions++
                    if (nativeAd != null) {
                        onAdLoaded(nativeAd)
                    }
                }

                override fun onAdLoadFailed(nativeAd: LevelPlayNativeAd?, error: IronSourceError?) {
                    Log.w(TAG, "Native ad load failed: ${error?.errorMessage} (code: ${error?.errorCode})")
                    onAdFailed(error?.errorMessage ?: "Native ad load failed")
                }

                override fun onAdClicked(nativeAd: LevelPlayNativeAd?, adInfo: AdInfo?) {
                    Log.d(TAG, "Native ad clicked")
                }

                override fun onAdImpression(nativeAd: LevelPlayNativeAd?, adInfo: AdInfo?) {
                    Log.d(TAG, "Native ad impression recorded")
                }
            })

            val nativeAd = builder.build()
            nativeAd.loadAd()
            Log.d(TAG, "LevelPlayNativeAd load request sent")
            nativeAd
        } catch (e: Throwable) {
            Log.e(TAG, "Error creating native ad: ${e.message}", e)
            onAdFailed(e.message ?: "Unknown error")
            null
        }
    }

    /**
     * Safely destroy a LevelPlay Native Ad.
     */
    fun destroyNativeAd(nativeAd: LevelPlayNativeAd?) {
        if (nativeAd != null) {
            try {
                nativeAd.destroyAd()
                Log.d(TAG, "Native ad destroyed")
            } catch (e: Throwable) {
                Log.e(TAG, "Error destroying native ad: ${e.message}", e)
            }
        }
    }

    // Threshold so interstitial ads trigger after 15 user actions/nav as requested
    const val CLICKS_THRESHOLD_FOR_INTERSTITIAL = 15
    private var _featureClickCount = 0
    val featureClickCount: Int get() = _featureClickCount

    /**
     * Records a user feature click (navigation, tabs, bottle fishing, moment posting, radar).
     * When count reaches threshold (15), triggers a real ironSource Interstitial ad.
     * Lancar dan tidak membuang kuota jika iklan sedang dalam proses pemuatan singkat.
     */
    fun recordFeatureClick(activity: Activity? = null): Boolean {
        _featureClickCount++
        Log.d(TAG, "Feature click recorded: $_featureClickCount / $CLICKS_THRESHOLD_FOR_INTERSTITIAL")

        // Preload proactively when getting close to threshold
        if (_featureClickCount >= CLICKS_THRESHOLD_FOR_INTERSTITIAL - 3) {
            loadInterstitial()
        }

        if (_featureClickCount >= CLICKS_THRESHOLD_FOR_INTERSTITIAL) {
            val act = activity ?: currentActivityRef?.get()
            val shown = if (IronSource.isInterstitialReady()) {
                val res = showInterstitial(activity = act, fallbackIfUnavailable = false)
                if (res) {
                    _featureClickCount = 0
                }
                res
            } else {
                Log.d(TAG, "Threshold $CLICKS_THRESHOLD_FOR_INTERSTITIAL aksi tercapai, memuat ulang iklan ironSource di latar...")
                loadInterstitial()
                // Jangan reset counter ke 0 agar saat aksi berikutnya langsung tampil begitu iklan siap
                false
            }
            return shown
        }
        return false
    }

    fun resetFeatureClickCount() {
        _featureClickCount = 0
    }

    /**
     * Preload an Interstitial ad.
     */
    fun loadInterstitial() {
        try {
            IronSource.loadInterstitial()
            Log.d(TAG, "Loading Interstitial ad from ironSource...")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Interstitial: ${e.message}", e)
        }
    }

    /**
     * Show an Interstitial ad strictly from ironSource LevelPlay without fake fallbacks.
     */
    fun showInterstitial(
        activity: Activity? = null,
        placementName: String? = null,
        fallbackIfUnavailable: Boolean = false
    ): Boolean {
        val act = activity ?: currentActivityRef?.get()
        return try {
            if (IronSource.isInterstitialReady()) {
                totalImpressions++
                if (act != null) {
                    if (placementName != null) {
                        IronSource.showInterstitial(act, placementName)
                    } else {
                        IronSource.showInterstitial(act)
                    }
                } else {
                    if (placementName != null) {
                        IronSource.showInterstitial(placementName)
                    } else {
                        IronSource.showInterstitial()
                    }
                }
                Log.d(TAG, "ironSource Interstitial displayed successfully")
                true
            } else {
                Log.d(TAG, "ironSource Interstitial belum siap di memori, memuat ulang tanpa iklan palsu")
                loadInterstitial()
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing Interstitial: ${e.message}", e)
            false
        }
    }

    fun showInterstitial(placementName: String? = null): Boolean {
        return showInterstitial(activity = null, placementName = placementName, fallbackIfUnavailable = false)
    }

    /**
     * Force-show a real ironSource Interstitial ad on demand.
     */
    fun forceShowInterstitial(activity: Activity? = null, onClosed: () -> Unit = {}): Boolean {
        val act = activity ?: currentActivityRef?.get()
        if (IronSource.isInterstitialReady()) {
            _featureClickCount = 0
            return showInterstitial(activity = act, fallbackIfUnavailable = false)
        } else {
            loadInterstitial()
            val ctx = act ?: LovyApplication.appContext
            try {
                android.widget.Toast.makeText(ctx, "Iklan sedang disiapkan dari jaringan ironSource...", android.widget.Toast.LENGTH_SHORT).show()
            } catch (_: Throwable) {}
            return false
        }
    }

    fun dismissInterstitialDialog() {
        _activeInterstitialAd.value = null
        loadInterstitial()
    }

    /**
     * Show a Rewarded Video ad strictly from ironSource LevelPlay.
     */
    fun showRewardedVideo(
        activity: Activity? = null,
        placementName: String? = null,
        onRewarded: () -> Unit
    ): Boolean {
        onUserRewardedCallback = onRewarded
        val act = activity ?: currentActivityRef?.get()
        return try {
            if (IronSource.isRewardedVideoAvailable()) {
                totalImpressions++
                if (act != null) {
                    if (placementName != null) {
                        IronSource.showRewardedVideo(act, placementName)
                    } else {
                        IronSource.showRewardedVideo(act)
                    }
                } else {
                    if (placementName != null) {
                        IronSource.showRewardedVideo(placementName)
                    } else {
                        IronSource.showRewardedVideo()
                    }
                }
                Log.d(TAG, "ironSource Rewarded Video launched")
                true
            } else {
                Log.d(TAG, "ironSource Rewarded video unavailable from network")
                val ctx = act ?: LovyApplication.appContext
                try {
                    android.widget.Toast.makeText(ctx, "Iklan video belum siap, silakan coba sesaat lagi", android.widget.Toast.LENGTH_SHORT).show()
                } catch (_: Throwable) {}
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing Rewarded Video: ${e.message}", e)
            false
        }
    }

    fun showRewardedVideo(onRewarded: () -> Unit, placementName: String? = null): Boolean {
        return showRewardedVideo(activity = null, placementName = placementName, onRewarded = onRewarded)
    }

    fun dismissRewardedDialog(claimReward: Boolean) {
        _activeRewardedAd.value = null
        if (claimReward) {
            onUserRewardedCallback?.invoke()
        }
        onUserRewardedCallback = null
    }

    /**
     * Returns a summary of current ad state for monitoring and UI test cards.
     */
    fun getAdStatus(): AdStatusInfo {
        return AdStatusInfo(
            isSdkInitialized = _isSdkInitialized.value,
            isInterstitialReady = _isInterstitialReady.value || IronSource.isInterstitialReady(),
            isRewardedReady = _isRewardedReady.value || IronSource.isRewardedVideoAvailable(),
            featureClicksCount = _featureClickCount,
            featureClicksThreshold = CLICKS_THRESHOLD_FOR_INTERSTITIAL,
            totalImpressionsCount = totalImpressions
        )
    }

    private fun setupInterstitialListener() {
        IronSource.setLevelPlayInterstitialListener(object : LevelPlayInterstitialListener {
            override fun onAdReady(adInfo: AdInfo) {
                Log.d(TAG, "Interstitial ad ready from: ${adInfo.adNetwork}")
                _isInterstitialReady.value = true
            }

            override fun onAdLoadFailed(error: IronSourceError) {
                Log.w(TAG, "Interstitial load failed: ${error.errorMessage} (code: ${error.errorCode})")
                _isInterstitialReady.value = false
                // Auto-retry with backoff after 8 seconds
                adScope.launch {
                    delay(8000L)
                    loadInterstitial()
                }
            }

            override fun onAdOpened(adInfo: AdInfo) {
                Log.d(TAG, "Interstitial opened")
            }

            override fun onAdShowSucceeded(adInfo: AdInfo) {
                Log.d(TAG, "Interstitial show succeeded")
            }

            override fun onAdShowFailed(error: IronSourceError, adInfo: AdInfo) {
                Log.w(TAG, "Interstitial show failed: ${error.errorMessage}")
                _isInterstitialReady.value = false
                loadInterstitial()
            }

            override fun onAdClicked(adInfo: AdInfo) {
                Log.d(TAG, "Interstitial clicked")
            }

            override fun onAdClosed(adInfo: AdInfo) {
                Log.d(TAG, "Interstitial closed. Preloading next one immediately...")
                _isInterstitialReady.value = false
                loadInterstitial()
            }
        })
    }

    private fun setupRewardedVideoListener() {
        IronSource.setLevelPlayRewardedVideoListener(object : LevelPlayRewardedVideoListener {
            override fun onAdAvailable(adInfo: AdInfo) {
                Log.d(TAG, "Rewarded video available from: ${adInfo.adNetwork}")
                _isRewardedReady.value = true
            }

            override fun onAdUnavailable() {
                Log.d(TAG, "Rewarded video unavailable from network")
                _isRewardedReady.value = false
            }

            override fun onAdOpened(adInfo: AdInfo) {
                Log.d(TAG, "Rewarded video opened")
            }

            override fun onAdShowFailed(error: IronSourceError, adInfo: AdInfo) {
                Log.w(TAG, "Rewarded video show failed: ${error.errorMessage}")
                _isRewardedReady.value = false
                onUserRewardedCallback = null
            }

            override fun onAdClicked(placement: Placement, adInfo: AdInfo) {
                Log.d(TAG, "Rewarded video clicked: ${placement.placementName}")
            }

            override fun onAdRewarded(placement: Placement, adInfo: AdInfo) {
                Log.d(TAG, "User rewarded on placement: ${placement.placementName}")
                onUserRewardedCallback?.invoke()
                onUserRewardedCallback = null
            }

            override fun onAdClosed(adInfo: AdInfo) {
                Log.d(TAG, "Rewarded video closed")
            }
        })
    }
}

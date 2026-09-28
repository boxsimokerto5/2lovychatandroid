package com.example.util

import android.app.Activity
import android.util.Log
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
 * - Interstitial Ads loading, smart low-threshold triggering (4 clicks), and display
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

    // Curated high-converting rotating sponsored ads for instant fallback fill
    val SPONSORED_FALLBACK_ADS = listOf(
        SponsoredAdContent(
            id = "ad_lovy_vip",
            title = "Lovy VIP Premium",
            advertiser = "Lovy Official",
            description = "Dapatkan radar tak terbatas, filter lokasi instan, dan lencana profil eksklusif sekarang!",
            callToAction = "Coba Gratis",
            iconEmoji = "👑",
            category = "Fitur Unggulan",
            primaryColorHex = 0xFF00A86B,
            secondaryColorHex = 0xFF004D40
        ),
        SponsoredAdContent(
            id = "ad_shopee_promo",
            title = "Shopee Mega Sale",
            advertiser = "Shopee Indonesia",
            description = "Nikmati Gratis Ongkir Rp0 ke Seluruh Indonesia dan Flash Sale Serba Seribu setiap hari.",
            callToAction = "Belanja Hemat",
            iconEmoji = "🛍️",
            category = "Belanja & Promo",
            primaryColorHex = 0xFFEE4D2D,
            secondaryColorHex = 0xFFC23516
        ),
        SponsoredAdContent(
            id = "ad_traveloka",
            title = "Traveloka Holiday Deals",
            advertiser = "Traveloka",
            description = "Pesan tiket pesawat, kereta api, dan hotel bintang 5 dengan diskon hingga 70%.",
            callToAction = "Pesan Tiket",
            iconEmoji = "✈️",
            category = "Wisata & Hotel",
            primaryColorHex = 0xFF0264D6,
            secondaryColorHex = 0xFF003E8A
        ),
        SponsoredAdContent(
            id = "ad_spotify",
            title = "Spotify Duo & Family",
            advertiser = "Spotify",
            description = "Dengarkan jutaan lagu favorit tanpa jeda iklan bersama teman dan keluarga tercinta.",
            callToAction = "Dengarkan Musik",
            iconEmoji = "🎵",
            category = "Musik & Hiburan",
            primaryColorHex = 0xFF1DB954,
            secondaryColorHex = 0xFF126930
        ),
        SponsoredAdContent(
            id = "ad_tokopedia",
            title = "Waktu Indonesia Belanja",
            advertiser = "Tokopedia",
            description = "Serbu diskon kilat cashback kilat hingga 90% hanya minggu ini di Tokopedia!",
            callToAction = "Lihat Promo",
            iconEmoji = "📦",
            category = "Belanja Online",
            primaryColorHex = 0xFF03AC0E,
            secondaryColorHex = 0xFF026608
        )
    )

    private var sponsoredAdIndex = 0

    fun getNextSponsoredAd(): SponsoredAdContent {
        val ad = SPONSORED_FALLBACK_ADS[sponsoredAdIndex % SPONSORED_FALLBACK_ADS.size]
        sponsoredAdIndex++
        return ad
    }

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

            // Setup listeners for Interstitial and Rewarded Video
            setupInterstitialListener()
            setupRewardedVideoListener()

            // Set metadata flags to assist ad fill
            try {
                IronSource.setMetaData("is_child_directed", "false")
                IronSource.setMetaData("is_deviceid_optout", "false")
                IronSource.setMetaData("is_test_suite", "enable")
            } catch (e: Throwable) {
                Log.w(TAG, "Could not set metadata: ${e.message}")
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
            bannerLayout.levelPlayBannerListener = object : LevelPlayBannerListener {
                override fun onAdLoaded(adInfo: AdInfo) {
                    Log.d(TAG, "Banner ad loaded successfully: ${adInfo.adNetwork}")
                    totalImpressions++
                    onBannerLoaded()
                }

                override fun onAdLoadFailed(error: IronSourceError) {
                    Log.w(TAG, "Banner ad load failed: ${error.errorMessage} (code: ${error.errorCode})")
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

            val targetPlacement = placementName?.takeIf { it.isNotBlank() }
            if (targetPlacement != null) {
                IronSource.loadBanner(bannerLayout, targetPlacement)
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

    // Lower threshold so interstitial ads trigger smoothly and naturally (e.g. after 4 feature actions/nav)
    const val CLICKS_THRESHOLD_FOR_INTERSTITIAL = 4
    private var _featureClickCount = 0
    val featureClickCount: Int get() = _featureClickCount

    /**
     * Records a user feature click (navigation, tabs, bottle fishing, moment posting, radar).
     * When count reaches threshold (4), triggers an Interstitial ad.
     * If live ironSource interstitial is ready, displays it; otherwise shows the rich fallback sponsored interstitial.
     */
    fun recordFeatureClick(activity: Activity? = null): Boolean {
        _featureClickCount++
        Log.d(TAG, "Feature click recorded: $_featureClickCount / $CLICKS_THRESHOLD_FOR_INTERSTITIAL")

        // Preload proactively when getting close to threshold
        if (_featureClickCount == CLICKS_THRESHOLD_FOR_INTERSTITIAL - 1) {
            loadInterstitial()
        }

        if (_featureClickCount >= CLICKS_THRESHOLD_FOR_INTERSTITIAL) {
            val act = activity ?: currentActivityRef?.get()
            val shown = showInterstitial(activity = act, fallbackIfUnavailable = true)
            if (shown) {
                _featureClickCount = 0
            } else {
                // Keep counter at threshold so next attempt immediately triggers once ready
                _featureClickCount = CLICKS_THRESHOLD_FOR_INTERSTITIAL
                loadInterstitial()
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
     * Show an Interstitial ad if ready, or show seamless fallback if requested.
     */
    fun showInterstitial(
        activity: Activity? = null,
        placementName: String? = null,
        fallbackIfUnavailable: Boolean = true
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
                Log.d(TAG, "ironSource Interstitial not ready yet, requesting reload")
                loadInterstitial()
                if (fallbackIfUnavailable) {
                    // Show our rich sponsored interstitial modal so the user gets an ad experience effortlessly
                    totalImpressions++
                    _activeInterstitialAd.value = getNextSponsoredAd()
                    true
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing Interstitial: ${e.message}", e)
            if (fallbackIfUnavailable) {
                totalImpressions++
                _activeInterstitialAd.value = getNextSponsoredAd()
                true
            } else {
                false
            }
        }
    }

    fun showInterstitial(placementName: String? = null): Boolean {
        return showInterstitial(activity = null, placementName = placementName, fallbackIfUnavailable = true)
    }

    /**
     * Force-show an Interstitial ad on demand (ideal for testing or milestone rewards).
     */
    fun forceShowInterstitial(activity: Activity? = null, onClosed: () -> Unit = {}): Boolean {
        _featureClickCount = 0
        val act = activity ?: currentActivityRef?.get()
        if (IronSource.isInterstitialReady()) {
            return showInterstitial(activity = act, fallbackIfUnavailable = true)
        } else {
            totalImpressions++
            _activeInterstitialAd.value = getNextSponsoredAd()
            loadInterstitial()
            return true
        }
    }

    fun dismissInterstitialDialog() {
        _activeInterstitialAd.value = null
        loadInterstitial()
    }

    /**
     * Show a Rewarded Video ad and trigger callback when completed.
     * If live ironSource video is unavailable, opens the sponsored rewarded video modal so user can still earn the reward!
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
                Log.d(TAG, "ironSource Rewarded video unavailable, launching sponsored video experience")
                totalImpressions++
                _activeRewardedAd.value = getNextSponsoredAd()
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing Rewarded Video: ${e.message}", e)
            totalImpressions++
            _activeRewardedAd.value = getNextSponsoredAd()
            true
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

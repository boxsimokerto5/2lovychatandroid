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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages ironSource (Unity LevelPlay) Ads integration:
 * - SDK Initialization with App Key
 * - Banner Ad creation, loading, and disposal
 * - Interstitial Ads loading and display
 * - Rewarded Video Ads display with reward callback
 */
object AdManager {
    private const val TAG = "AdManager"

    // App Key from the user's ironSource / LevelPlay dashboard for Lovy Chat
    const val IRONSOURCE_APP_KEY = "283361415"

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
            } catch (e: Throwable) {
                Log.w(TAG, "Could not set metadata: ${e.message}")
            }

            // Initialize IronSource with Banner, Interstitial, Rewarded Video, and Native Ad
            IronSource.init(
                activity,
                IRONSOURCE_APP_KEY,
                {
                    Log.d(TAG, "IronSource initialization completed via listener")
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

            // Automatically load interstitial in the background
            loadInterstitial()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing ironSource SDK: ${e.message}", e)
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
     * If placementName is null or not set, LevelPlay loads the default Native ad placement.
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

            // Only set placement name if it's explicitly provided and not the ad unit ID string
            if (!placementName.isNullOrBlank() && placementName != AD_UNIT_NATIVE_ID) {
                builder.withPlacementName(placementName)
            }

            builder.withListener(object : LevelPlayNativeAdListener {
                override fun onAdLoaded(nativeAd: LevelPlayNativeAd?, adInfo: AdInfo?) {
                    Log.d(TAG, "Native ad loaded successfully: ${adInfo?.adNetwork}")
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

    const val CLICKS_THRESHOLD_FOR_INTERSTITIAL = 20
    private var _featureClickCount = 0
    val featureClickCount: Int get() = _featureClickCount

    /**
     * Records a user feature click (navigation, buttons, filters, fishing, throwing, etc.).
     * EXCLUDES typing or chatting to ensure smooth messaging experience.
     * When count reaches 20, attempts to show an Interstitial ad and resets counter.
     */
    fun recordFeatureClick(activity: Activity? = null): Boolean {
        _featureClickCount++
        Log.d(TAG, "Feature click recorded: $_featureClickCount / $CLICKS_THRESHOLD_FOR_INTERSTITIAL")
        if (_featureClickCount >= CLICKS_THRESHOLD_FOR_INTERSTITIAL) {
            _featureClickCount = 0
            val act = activity ?: currentActivityRef?.get()
            val shown = showInterstitial(activity = act)
            if (!shown) {
                // Ensure interstitial is preloaded for next time
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
            Log.d(TAG, "Loading Interstitial ad...")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Interstitial: ${e.message}", e)
        }
    }

    /**
     * Show an Interstitial ad if ready.
     */
    fun showInterstitial(activity: Activity? = null, placementName: String? = null): Boolean {
        return try {
            val act = activity ?: currentActivityRef?.get()
            if (IronSource.isInterstitialReady()) {
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
                true
            } else {
                Log.d(TAG, "Interstitial not ready yet, requesting reload")
                loadInterstitial()
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing Interstitial: ${e.message}", e)
            false
        }
    }

    fun showInterstitial(placementName: String? = null): Boolean {
        return showInterstitial(activity = null, placementName = placementName)
    }

    /**
     * Show a Rewarded Video ad and trigger callback when completed.
     */
    fun showRewardedVideo(
        activity: Activity? = null,
        placementName: String? = null,
        onRewarded: () -> Unit
    ): Boolean {
        return try {
            if (IronSource.isRewardedVideoAvailable()) {
                onUserRewardedCallback = onRewarded
                if (activity != null) {
                    if (placementName != null) {
                        IronSource.showRewardedVideo(activity, placementName)
                    } else {
                        IronSource.showRewardedVideo(activity)
                    }
                } else {
                    if (placementName != null) {
                        IronSource.showRewardedVideo(placementName)
                    } else {
                        IronSource.showRewardedVideo()
                    }
                }
                true
            } else {
                Log.d(TAG, "Rewarded video not available yet")
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

    private fun setupInterstitialListener() {
        IronSource.setLevelPlayInterstitialListener(object : LevelPlayInterstitialListener {
            override fun onAdReady(adInfo: AdInfo) {
                Log.d(TAG, "Interstitial ad ready")
                _isInterstitialReady.value = true
            }

            override fun onAdLoadFailed(error: IronSourceError) {
                Log.w(TAG, "Interstitial load failed: ${error.errorMessage}")
                _isInterstitialReady.value = false
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
                Log.d(TAG, "Interstitial closed. Preloading next one...")
                _isInterstitialReady.value = false
                loadInterstitial()
            }
        })
    }

    private fun setupRewardedVideoListener() {
        IronSource.setLevelPlayRewardedVideoListener(object : LevelPlayRewardedVideoListener {
            override fun onAdAvailable(adInfo: AdInfo) {
                Log.d(TAG, "Rewarded video available")
                _isRewardedReady.value = true
            }

            override fun onAdUnavailable() {
                Log.d(TAG, "Rewarded video unavailable")
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

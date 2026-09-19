package com.example.util

import android.app.Activity
import android.util.Log
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
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

    private var isInitialized = false

    private val _isInterstitialReady = MutableStateFlow(false)
    val isInterstitialReady: StateFlow<Boolean> = _isInterstitialReady.asStateFlow()

    private val _isRewardedReady = MutableStateFlow(false)
    val isRewardedReady: StateFlow<Boolean> = _isRewardedReady.asStateFlow()

    private var onUserRewardedCallback: (() -> Unit)? = null

    /**
     * Initialize ironSource SDK with Lovy Chat App Key.
     * Must be called in Activity onCreate.
     */
    fun init(activity: Activity) {
        if (isInitialized) return

        try {
            Log.d(TAG, "Initializing ironSource SDK with App Key: $IRONSOURCE_APP_KEY")

            // Setup listeners for Interstitial and Rewarded Video
            setupInterstitialListener()
            setupRewardedVideoListener()

            // Initialize IronSource with Banner, Interstitial, and Rewarded Video
            IronSource.init(
                activity,
                IRONSOURCE_APP_KEY,
                IronSource.AD_UNIT.BANNER,
                IronSource.AD_UNIT.INTERSTITIAL,
                IronSource.AD_UNIT.REWARDED_VIDEO
            )

            isInitialized = true
            Log.d(TAG, "ironSource SDK initialized successfully")

            // Automatically load interstitial in the background
            loadInterstitial()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing ironSource SDK: ${e.message}", e)
        }
    }

    /**
     * Create and load a Banner ad view for the given Activity.
     */
    fun createBanner(activity: Activity, onBannerLoaded: () -> Unit = {}, onBannerFailed: (String) -> Unit = {}): IronSourceBannerLayout? {
        return try {
            val bannerLayout = IronSource.createBanner(activity, ISBannerSize.BANNER)
            bannerLayout.levelPlayBannerListener = object : LevelPlayBannerListener {
                override fun onAdLoaded(adInfo: AdInfo) {
                    Log.d(TAG, "Banner ad loaded successfully")
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

            IronSource.loadBanner(bannerLayout)
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
    fun showInterstitial(placementName: String? = null): Boolean {
        return try {
            if (IronSource.isInterstitialReady()) {
                if (placementName != null) {
                    IronSource.showInterstitial(placementName)
                } else {
                    IronSource.showInterstitial()
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

    /**
     * Show a Rewarded Video ad and trigger callback when completed.
     */
    fun showRewardedVideo(onRewarded: () -> Unit, placementName: String? = null): Boolean {
        return try {
            if (IronSource.isRewardedVideoAvailable()) {
                onUserRewardedCallback = onRewarded
                if (placementName != null) {
                    IronSource.showRewardedVideo(placementName)
                } else {
                    IronSource.showRewardedVideo()
                }
                true
            } else {
                Log.d(TAG, "Rewarded video not available")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing Rewarded Video: ${e.message}", e)
            false
        }
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

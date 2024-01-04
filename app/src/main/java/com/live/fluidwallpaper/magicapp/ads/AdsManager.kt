package com.live.fluidwallpaper.magicapp.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.ads.control.ads.ITGAd
import com.ads.control.ads.ITGAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApInterstitialAd
import com.ads.control.ads.wrapper.ApNativeAd
import com.live.fluidwallpaper.magicapp.BuildConfig
import com.live.fluidwallpaper.magicapp.R
import com.live.fluidwallpaper.magicapp.ads.RemoteConfigUtils.getOnInterDesign
import com.live.fluidwallpaper.magicapp.ads.RemoteConfigUtils.getOnInterSetWallPaper
import com.live.fluidwallpaper.magicapp.ads.RemoteConfigUtils.getOnInterWallPaper
import com.live.fluidwallpaper.magicapp.ads.RemoteConfigUtils.getOnNativeLanguage
import com.live.fluidwallpaper.magicapp.ui.bases.ext.isNetwork

object AdsManager {

    @SuppressLint("StaticFieldLeak")
    var nativeAdLanguage: ApNativeAd? = null

    @SuppressLint("StaticFieldLeak")
    var nativeAdOnBoarding: ApNativeAd? = null

    @SuppressLint("StaticFieldLeak")
    var nativeAdTheme: ApNativeAd? = null

    @SuppressLint("StaticFieldLeak")
    var nativeAdLoading: ApNativeAd? = null

    @SuppressLint("StaticFieldLeak")
    var nativeAdHome: ApNativeAd? = null

    var mInterstitialAdSetWallpaper: ApInterstitialAd? = null
    var mInterstitialAdWallpaper: ApInterstitialAd? = null
    var mInterstitialAdDesign: ApInterstitialAd? = null
    var preLoadNativeListener: com.live.fluidwallpaper.magicapp.ads.PreLoadNativeListener? = null


    fun setPreLoadNativeCallback(listener: com.live.fluidwallpaper.magicapp.ads.PreLoadNativeListener) {
        preLoadNativeListener = listener
    }

    fun loadNativeLanguage(activity: Activity, isFirst: Boolean) {
        if (nativeAdLanguage == null) {
            if (getOnNativeLanguage() && activity.isNetwork()) {
                ITGAd.getInstance().loadNativeAdResultCallback(activity,
                    if (isFirst) BuildConfig.admob_native_language else BuildConfig.admob_native_language_2,
                    R.layout.layout_native_language,
                    object : ITGAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            nativeAdLanguage = nativeAd
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeSuccess()
                            }
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeFail()
                            }
                        }
                    })
            } else {
                preLoadNativeListener?.onLoadNativeFail()
            }
        }
    }

    fun loadNativeOnBoarding(activity: Activity, isFirst: Boolean) {
        if (nativeAdOnBoarding == null) {
            if (RemoteConfigUtils.getOnNativeOnBoarding() && activity.isNetwork()) {
                ITGAd.getInstance().loadNativeAdResultCallback(activity,
                    if (isFirst) BuildConfig.admob_native_on_boarding else BuildConfig.admob_native_on_boarding_2,
                    R.layout.layout_native_on_boarding,
                    object : ITGAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            nativeAdOnBoarding = nativeAd
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeSuccess()
                            }
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeFail()
                            }
                        }
                    })
            } else {
                preLoadNativeListener?.onLoadNativeFail()
            }
        }
    }


    fun loadNativeTheme(activity: Activity, isFirst: Boolean = false) {
        if (nativeAdTheme == null) {
            if (RemoteConfigUtils.getOnNativeTheme() && activity.isNetwork()) {
                ITGAd.getInstance().loadNativeAdResultCallback(activity,
                    if (isFirst) BuildConfig.admob_native_theme else BuildConfig.admob_native_theme,
                    R.layout.layout_native_preset_live,
                    object : ITGAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            nativeAdTheme = nativeAd
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeSuccess()
                            }
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeFail()
                            }
                        }
                    })
            } else {
                preLoadNativeListener?.onLoadNativeFail()
            }
        }
    }


    fun loadBanner(activity: AppCompatActivity, id: String, frAds: FrameLayout, bool: Boolean) {
        if (isNetwork(activity) && bool) {
            ITGAd.getInstance().loadBanner(activity, id)
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadInterSetWallpaper(activity: Activity) {
        if (getOnInterSetWallPaper() && isNetwork(activity)) {
            if (mInterstitialAdSetWallpaper == null) {
                Log.d("DuongDx", "load")
                ITGAd.getInstance().getInterstitialAds(activity,
                    BuildConfig.admob_inter_set_wallpaper,
                    object : ITGAdCallback() {
                        override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                            super.onInterstitialLoad(interstitialAd)
                            mInterstitialAdSetWallpaper = interstitialAd
                        }
                    })
            }
        }
    }

    fun loadInterWallpaper(activity: Activity) {
        if (getOnInterWallPaper() && isNetwork(activity)) {
            if (mInterstitialAdDesign == null) {
                ITGAd.getInstance().getInterstitialAds(activity,
                    BuildConfig.admob_inter_wallpaper,
                    object : ITGAdCallback() {
                        override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                            super.onInterstitialLoad(interstitialAd)
                            mInterstitialAdWallpaper = interstitialAd
                        }
                    })
            }
        }
    }

    fun loadInterDesign(activity: Activity) {
        if (getOnInterDesign() && isNetwork(activity)) {
            if (mInterstitialAdDesign == null) {
                ITGAd.getInstance().getInterstitialAds(activity,
                    BuildConfig.admob_inter_design,
                    object : ITGAdCallback() {
                        override fun onInterstitialLoad(interstitialAd: ApInterstitialAd?) {
                            super.onInterstitialLoad(interstitialAd)
                            mInterstitialAdDesign = interstitialAd
                        }
                    })
            }
        }
    }

    fun loadNativeLoading(activity: Activity) {
        if (nativeAdLoading == null) {
            if (RemoteConfigUtils.getOnNativeLoading()) {
                ITGAd.getInstance().loadNativeAdResultCallback(activity,
                    BuildConfig.admob_native_loading,
                    R.layout.layout_native_loading,
                    object : ITGAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            nativeAdLoading = nativeAd
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeSuccess()
                            }
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeFail()
                            }
                        }
                    })
            }
        }
    }
    fun loadNativeHome(activity: Activity) {
        if (nativeAdHome == null) {
            if (RemoteConfigUtils.getOnNativeHome()) {
                ITGAd.getInstance().loadNativeAdResultCallback(activity,
                    BuildConfig.admob_native_home,
                    R.layout.layout_native_small_50dp,
                    object : ITGAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            nativeAdHome = nativeAd
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeSuccess()
                            }
                        }

                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeFail()
                            }
                        }
                    })
            }
        }
    }



}

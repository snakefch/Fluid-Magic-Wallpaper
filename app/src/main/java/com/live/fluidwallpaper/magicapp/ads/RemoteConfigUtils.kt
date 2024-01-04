package com.live.fluidwallpaper.magicapp.ads

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.live.fluidwallpaper.magicapp.BuildConfig


object RemoteConfigUtils {

    private const val TAG = "RemoteConfigUtils"

    private const val ON_NATIVE_INTER_SPLASH = "on_native_inter_splash"
    private const val ON_NATIVE_LANGUAGE = "on_native_language"
    private const val ON_NATIVE_ON_BOARDING = "on_native_on_boarding"
    private const val ON_NATIVE_PERMISSION = "on_native_permission"
    private const val ON_BANNER_HOME = "on_banner_home"
    private const val ON_COLLAPS_BANNER_HOME = "on_collaps_banner_home"
    private const val ON_TIME_SHOW_INTER = "on_time_show_inter"
    private const val ON_INTER_WALLPAPER = "on_inter_wallpaper"
    private const val ON_INTER_SET_WALLPAPER = "on_inter_set_wallpaper"
    private const val ON_INTER_DESIGN = "on_inter_design"

    private const val ON_NATIVE_THEME = "on_native_theme"
    private const val ON_BANNER_ALL = "on_banner_all"
    private const val ON_NATIVE_HOME = "on_native_home"
    private const val ON_NATIVE_LIVE_LOADING = "on_native_live_loading"
    private const val ON_IS_SHOW_RATE = "on_is_show_rate"


    var completed = false
    private val DEFAULTS: HashMap<String, Any> = hashMapOf(
        RemoteConfigUtils.ON_NATIVE_LANGUAGE to true,
        RemoteConfigUtils.ON_NATIVE_ON_BOARDING to true,
        RemoteConfigUtils.ON_NATIVE_PERMISSION to true,
        RemoteConfigUtils.ON_NATIVE_INTER_SPLASH to true,
        RemoteConfigUtils.ON_INTER_WALLPAPER to true,
        RemoteConfigUtils.ON_TIME_SHOW_INTER to 25,
        RemoteConfigUtils.ON_BANNER_HOME to true,
        RemoteConfigUtils.ON_INTER_SET_WALLPAPER to true,
        RemoteConfigUtils.ON_INTER_DESIGN to true,
        RemoteConfigUtils.ON_COLLAPS_BANNER_HOME to true,
        RemoteConfigUtils.ON_NATIVE_THEME to true,
        RemoteConfigUtils.ON_BANNER_ALL to true,
        RemoteConfigUtils.ON_NATIVE_HOME to true,
        RemoteConfigUtils.ON_NATIVE_LIVE_LOADING to true,
        RemoteConfigUtils.ON_IS_SHOW_RATE to true,

        )

    interface Listener {
        fun loadSuccess()
    }

    lateinit var listener: RemoteConfigUtils.Listener
    private lateinit var remoteConfig: FirebaseRemoteConfig

    fun init(mListener: RemoteConfigUtils.Listener) {
        RemoteConfigUtils.listener = mListener
        RemoteConfigUtils.remoteConfig =
            RemoteConfigUtils.getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {
        RemoteConfigUtils.remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                0
            } else {
                60 * 60
            }
        }
        RemoteConfigUtils.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(RemoteConfigUtils.DEFAULTS)
            fetchAndActivate().addOnCompleteListener {
                RemoteConfigUtils.listener.loadSuccess()
                RemoteConfigUtils.completed = true
            }
        }
        return RemoteConfigUtils.remoteConfig
    }


    //native language
    fun getOnNativeLanguage(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                false
            } else {
                RemoteConfigUtils.remoteConfig.getBoolean(RemoteConfigUtils.ON_NATIVE_LANGUAGE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getOnNativeOnBoarding(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                false
            } else {
                RemoteConfigUtils.remoteConfig.getBoolean(RemoteConfigUtils.ON_NATIVE_ON_BOARDING)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getOnInterSplash(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                false
            } else {
                RemoteConfigUtils.remoteConfig.getBoolean(RemoteConfigUtils.ON_NATIVE_INTER_SPLASH)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getOnInterWallPaper(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                return false
            } else {
                RemoteConfigUtils.remoteConfig.getBoolean(RemoteConfigUtils.ON_INTER_WALLPAPER)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getOnInterSetWallPaper(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                return false
            } else {
                RemoteConfigUtils.remoteConfig.getBoolean(RemoteConfigUtils.ON_INTER_SET_WALLPAPER)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getOnTimeShowInter(): Int {
        try {
            return if (!RemoteConfigUtils.completed) {
                return 25
            } else {
                RemoteConfigUtils.remoteConfig!!.getDouble(RemoteConfigUtils.ON_TIME_SHOW_INTER).toInt()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return 25
    }

    fun getOnBannerHome(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                return false
            } else {
                RemoteConfigUtils.remoteConfig!!.getBoolean(RemoteConfigUtils.ON_BANNER_HOME)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getOnCollapseBanner(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                return false
            } else {
                RemoteConfigUtils.remoteConfig!!.getBoolean(RemoteConfigUtils.ON_COLLAPS_BANNER_HOME)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

        fun getOnNativeTheme(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                return false
            } else {
                RemoteConfigUtils.remoteConfig!!.getBoolean(RemoteConfigUtils.ON_NATIVE_THEME)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }


    fun getOnBannerAll(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                return false
            } else {
                RemoteConfigUtils.remoteConfig!!.getBoolean(RemoteConfigUtils.ON_BANNER_ALL)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getOnNativeHome(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                return false
            } else {
                RemoteConfigUtils.remoteConfig!!.getBoolean(RemoteConfigUtils.ON_NATIVE_HOME)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getOnNativeLoading(): Boolean {
        try {
            return if (!RemoteConfigUtils.completed) {
                return false
            } else {
                RemoteConfigUtils.remoteConfig!!.getBoolean(RemoteConfigUtils.ON_NATIVE_LIVE_LOADING)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getIsShowRate(): Boolean {
        return try {
            if (!RemoteConfigUtils.completed) {
                false
            } else {
                RemoteConfigUtils.remoteConfig.getBoolean(
                    RemoteConfigUtils.ON_IS_SHOW_RATE
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getOnInterDesign(): Boolean {
        return try {
            if (!RemoteConfigUtils.completed) {
                false
            } else {
                RemoteConfigUtils.remoteConfig.getBoolean(
                    RemoteConfigUtils.ON_INTER_DESIGN
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
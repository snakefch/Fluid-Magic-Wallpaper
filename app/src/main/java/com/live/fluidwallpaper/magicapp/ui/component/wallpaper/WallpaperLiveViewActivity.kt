package com.live.fluidwallpaper.magicapp.ui.component.wallpaper

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.PackageManager
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.work.impl.Scheduler
import com.ads.control.ads.ITGAd
import com.facebook.shimmer.ShimmerFrameLayout
import com.live.fluidwallpaper.magicapp.R
import com.magicfluids.Config
import com.magicfluids.NativeInterface

import com.live.fluidwallpaper.magicapp.ads.AdsManager
import com.live.fluidwallpaper.magicapp.ads.PreLoadNativeListener
import com.live.fluidwallpaper.magicapp.app.AppConstants
import com.live.fluidwallpaper.magicapp.databinding.ActivityWallpaperLiveViewBinding
import com.live.fluidwallpaper.magicapp.models.PresetModel
import com.live.fluidwallpaper.magicapp.ui.bases.BaseActivity
import com.live.fluidwallpaper.magicapp.ui.bases.ext.click
import com.live.fluidwallpaper.magicapp.ui.bases.ext.goneView
import com.live.fluidwallpaper.magicapp.ui.bases.ext.visibleView
import com.live.fluidwallpaper.magicapp.ui.component.dialog.DialogLoading
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.GLES20Renderer
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.OrientationSensor
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsController
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage
import com.live.fluidwallpaper.magicapp.utils.Routes
import com.live.fluidwallpaper.magicapp.utils.TypePresetModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WallpaperLiveViewActivity : BaseActivity<ActivityWallpaperLiveViewBinding>(),
    com.live.fluidwallpaper.magicapp.ads.PreLoadNativeListener {

    private var presetModel: PresetModel? = null
    private lateinit var orientationSensor: com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.OrientationSensor
    private var mGLSurfaceView: GLSurfaceView? = null
    private var nativeInterface: NativeInterface? = null
    private var renderer: GLES20Renderer? = null
    private var settingsController: com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsController? = null
    private var shimmerSmall: ShimmerFrameLayout? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            shareImage()
        } else {
            // Explain to the user that the feature is unavailable because the
            // feature requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }


    @Volatile
    var activePause = false

    override fun getLayoutActivity(): Int = R.layout.activity_wallpaper_live_view
    override fun initViews() {
        super.initViews()
        shimmerSmall = findViewById(R.id.shimmer_small)
        showDialogLoading()
        loadConfigPreset()
        loadDataSettingController()
    }

    private fun showDialogLoading() {
        DialogLoading(this@WallpaperLiveViewActivity, onFinishedLoading = {
            showNativeMain()
        }).show()
    }

    @SuppressLint("RestrictedApi")
    private fun loadDataSettingController() {
        lifecycleScope.launch(Dispatchers.IO) {
            settingsController =
                com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsController()
            mBinding.surfaceView.preserveEGLContextOnPause = wantToPreserveEGLContext()
            mGLSurfaceView = mBinding.surfaceView
            nativeInterface = NativeInterface()
            nativeInterface?.setAssetManager(assets)
            orientationSensor =
                com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.OrientationSensor(
                    this@WallpaperLiveViewActivity,
                    application
                )
            mGLSurfaceView?.setEGLContextClientVersion(2)
            val gLSurfaceView = mGLSurfaceView
            val gLES20Renderer = GLES20Renderer(
                this@WallpaperLiveViewActivity, null, nativeInterface!!, orientationSensor
            )

            renderer = gLES20Renderer
            gLSurfaceView!!.setRenderer(gLES20Renderer)
            renderer!!.setInitialScreenSize(300, Scheduler.MAX_GREEDY_SCHEDULER_LIMIT)
            nativeInterface?.onCreate(300, Scheduler.MAX_GREEDY_SCHEDULER_LIMIT, false)
            onSettingsChanged()
        }
    }

    private fun shareImage() {
        showCreateLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            renderer?.orderScreenshot()
        }
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.imageBack.setOnClickListener {
            finish()
        }

        mBinding.tvLiveGotoSettings.click {
             moveToPresetActivity()
        }

        mBinding.imageSetting.click {
           moveToPresetActivity()
        }
        mBinding.imageShare.setOnClickListener {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) -> {
                        shareImage()
                    }

                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestPermissionLauncher.launch(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    }
                }
            } else {
                shareImage()
            }

        }

    }

    private fun moveToPresetActivity() {
        Routes.startPresetActivity(this, presetModel!!, false)
    }

    private fun applySettingsToLwp(z: Boolean, i: Int) {
        if (z) {
            Config.LWPCurrent.copyValuesFrom(Config.Current)
        } else {
            com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage.loadConfigFromInternalPreset(presetModel?.name, assets, Config.LWPCurrent)

        }
        com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage.saveSessionConfig(
            this, Config.LWPCurrent, com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage.SETTINGS_LWP_NAME
        )
        Config.LWPCurrent.ReloadRequired = true
        Config.LWPCurrent.ReloadRequiredPreview = true

    }

    private fun loadConfigPreset() {

        if (intent.hasExtra(AppConstants.KEY_PRESET_MODEL)) {
            presetModel =
                intent.getParcelableExtra<PresetModel>(AppConstants.KEY_PRESET_MODEL) as PresetModel
            if (presetModel?.typePresetModel == TypePresetModel.CUSTOM) {
                com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage.loadConfigPresetCustom(presetModel?.pathFluidCustom, Config.Current)
            } else
                com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage.loadConfigFromInternalPreset(presetModel?.name, assets, Config.Current)
        } else {
            nativeInterface?.randomizeConfig(Config.Current)
        }

    }

    private fun onSettingsChanged(presetModel: String? = "") {
        nativeInterface?.updateConfig(Config.Current)
    }

    private fun wantToPreserveEGLContext(): Boolean {
        val memoryInfo = ActivityManager.MemoryInfo()
        (getSystemService(ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        val j = memoryInfo.totalMem / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED
        val z = j > 3000
        return z
    }

    override fun onResume() {
        super.onResume()
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            mGLSurfaceView?.onResume()
            nativeInterface?.onResume()
        }, 200)

    }

    override fun onLoadNativeSuccess() {
        showNativeMain()
    }

    override fun onLoadNativeFail() {
        if (com.live.fluidwallpaper.magicapp.ads.AdsManager.nativeAdTheme != null) {
            mBinding.frAds.visibleView()
        } else {
            mBinding.frAds.goneView()
        }
    }

    var showAds = false

    private fun showNativeMain() {
        if (com.live.fluidwallpaper.magicapp.ads.AdsManager.nativeAdTheme != null && !showAds) {
            mBinding.frAds.visibleView()
            showAds = true
            try {
                ITGAd.getInstance().populateNativeAdView(
                    this, com.live.fluidwallpaper.magicapp.ads.AdsManager.nativeAdTheme, mBinding.frAds, shimmerSmall
                )
            } catch (_: Exception) {

            }
        } else {
            mBinding.frAds.goneView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeInterface?.onDestroy()

    }

}
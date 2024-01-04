package com.live.fluidwallpaper.magicapp.ui.component.livewallpaper

import com.live.fluidwallpaper.magicapp.R
import com.live.fluidwallpaper.magicapp.databinding.ActivityLwpBinding
import com.live.fluidwallpaper.magicapp.ui.bases.BaseActivity
import com.magicfluids.Config
import com.magicfluids.NativeInterface

class LiveWallpaperActivity : BaseActivity<ActivityLwpBinding>() {
    private var ntv: NativeInterface? = null

    private fun initSettings() {
        com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.QualitySetting.init()
        com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage.loadSessionConfig(
            this,
            Config.LWPCurrent,
            com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage.SETTINGS_LWP_NAME
        )

    }
    override fun initViews() {
        super.initViews()
        if (com.live.fluidwallpaper.magicapp.services.WallpaperService.mostRecentEngine != null) {
            ntv = com.live.fluidwallpaper.magicapp.services.WallpaperService.mostRecentEngine.ntv
        } else {
            initSettings()
        }
        setContentView(R.layout.activity_lwp)

    }


    override fun getLayoutActivity(): Int = R.layout.activity_lwp


}
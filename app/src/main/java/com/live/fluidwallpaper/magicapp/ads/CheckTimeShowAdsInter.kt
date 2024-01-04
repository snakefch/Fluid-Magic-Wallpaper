package com.live.fluidwallpaper.magicapp.ads

/**
 * Created by TuyenNguyen on 18/01/2023.
 */

object CheckTimeShowAdsInter {
    private var lastShow: Long = 0
    val isTimeShow: Boolean
        get() {
            var isTimeShow = true

            var time = if (RemoteConfigUtils.getOnTimeShowInter() > 20) {
                RemoteConfigUtils.getOnTimeShowInter()
            } else {
                20
            }

            if (com.live.fluidwallpaper.magicapp.ads.CheckTimeShowAdsInter.lastShow > System.currentTimeMillis() - time * 1000) {
                isTimeShow = false
            }
            return isTimeShow
        }

    fun logShowed() {
        com.live.fluidwallpaper.magicapp.ads.CheckTimeShowAdsInter.lastShow = System.currentTimeMillis()
    }
}
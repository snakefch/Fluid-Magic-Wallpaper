package com.live.fluidwallpaper.magicapp.ui.component.main

import androidx.recyclerview.widget.GridLayoutManager
import com.ads.control.ads.ITGAd
import com.ads.control.ads.ITGAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.funtion.AdCallback
import com.ads.control.util.AppConstant
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.live.fluidwallpaper.magicapp.BuildConfig
import com.live.fluidwallpaper.magicapp.R
import com.live.fluidwallpaper.magicapp.ads.RemoteConfigUtils
import com.live.fluidwallpaper.magicapp.app.AppConstants
import com.live.fluidwallpaper.magicapp.databinding.ActivityMainBinding
import com.live.fluidwallpaper.magicapp.models.PresetModel
import com.live.fluidwallpaper.magicapp.models.Status
import com.live.fluidwallpaper.magicapp.ui.bases.BaseActivity
import com.live.fluidwallpaper.magicapp.ui.bases.ext.goneView
import com.live.fluidwallpaper.magicapp.ui.bases.ext.isNetwork
import com.live.fluidwallpaper.magicapp.ui.bases.ext.showRateDialog
import com.live.fluidwallpaper.magicapp.ui.bases.ext.visibleView
import com.live.fluidwallpaper.magicapp.ui.component.main.adapter.WallpaperAdapter
import com.live.fluidwallpaper.magicapp.utils.CommonData
import com.live.fluidwallpaper.magicapp.utils.ConnectionLiveData
import com.live.fluidwallpaper.magicapp.utils.EasyPreferences.set
import com.live.fluidwallpaper.magicapp.utils.Routes
import com.live.fluidwallpaper.magicapp.utils.SharePrefUtils
import com.live.fluidwallpaper.magicapp.utils.TypePresetModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var listPresetModelTotal = arrayListOf<PresetModel>()
    private lateinit var wallpaperAdapter: WallpaperAdapter

    override fun getLayoutActivity(): Int = R.layout.activity_main

    override fun onResume() {
        super.onResume()

        ConnectionLiveData(this).observe(this) { isNetwork ->
            if (isNetwork) {
                Timber.d("network on")
            } else {
                Timber.d("network off")
            }
        }

        val listNew = arrayListOf<PresetModel>()
        listNew.clear()
        listNew.addAll(CommonData.getListPreset())
        listNew.addAll(CommonData.getListPresetCustom(this))
        if (listNew.size > listPresetModelTotal.filter { it.typePresetModel != TypePresetModel.ADS }.size) {
            loadDataPreset()
        }

        wallpaperAdapter.setCheckNewItem(prefs.getString(AppConstants.KEY_NAME_EFFECT, "") ?: "")

    }

    override fun initViews() {
        prefs[AppConstants.KEY_SELECT_LANGUAGE] = true
        prefs[AppConstants.KEY_FIRST_ON_BOARDING] = true

        loadDataPreset()
        if (intent.extras?.getBoolean(AppConstants.KEY_SET_WALLPAPER_SUCCESS, false) == true) {
            if (!SharePrefUtils.getBoolean(
                    AppConstants.IS_RATED,
                    false
                ) && !SharePrefUtils.getBoolean(AppConstants.IS_FIRST_RATED, false)
            ) {
                if (RemoteConfigUtils.getIsShowRate()) {
                    showRateDialog(this@MainActivity, false)
                    SharePrefUtils.putBoolean(AppConstants.IS_FIRST_RATED, true)

                } else {
                    showSuccessDialog()


                }
            } else {
                showSuccessDialog()
            }
        }
        super.initViews()
        initAdsBanner()
        com.live.fluidwallpaper.magicapp.ads.AdsManager.loadInterWallpaper(this)
        com.live.fluidwallpaper.magicapp.ads.AdsManager.loadNativeTheme(this)
        com.live.fluidwallpaper.magicapp.ads.AdsManager.loadNativeLoading(this)
        com.live.fluidwallpaper.magicapp.ads.AdsManager.loadInterDesign(this)

        initAdapterPager()
    }


    private fun moveToPresetActivity(presetModel: PresetModel) {
        Routes.startPresetLiveActivity(this, presetModel)
    }

    private fun initAdapterPager() {
        val titles = listOf(
            getString(R.string.my_design),
            getString(R.string.txt_trending),
            getString(R.string.txt_new),
            getString(
                R.string.txt_hot
            )
        )

        for (title in titles) {
            val tab: TabLayout.Tab = mBinding.tabMain.newTab()
            tab.text = title
            mBinding.tabMain.addTab(tab)
        }




        mBinding.tabMain.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        wallpaperAdapter.filterTypePosition(TypePresetModel.CUSTOM)

                        if (listPresetModelTotal.any { it.typePresetModel == TypePresetModel.CUSTOM }) {
                            mBinding.layoutEmpty.goneView()
                        } else mBinding.layoutEmpty.visibleView()

                        mBinding.ivAdd.visibleView()
                    }

                    1 -> {
                        wallpaperAdapter.filterTypePosition(TypePresetModel.TRENDING)
                        mBinding.layoutEmpty.goneView()
                        mBinding.ivAdd.goneView()
                    }

                    2 -> {
                        wallpaperAdapter.filterTypePosition(TypePresetModel.NEW)
                        mBinding.layoutEmpty.goneView()
                        mBinding.ivAdd.goneView()
                    }


                    3 -> {
                        wallpaperAdapter.filterTypePosition(TypePresetModel.HOT)
                        mBinding.layoutEmpty.goneView()
                        mBinding.ivAdd.goneView()
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        mBinding.tabMain.getTabAt(1)?.select()

    }

    private fun initAdsBanner() {
        if (RemoteConfigUtils.getOnBannerHome()) {
            if (RemoteConfigUtils.getOnCollapseBanner()) {
                ITGAd.getInstance().loadCollapsibleBanner(this,
                    BuildConfig.admob_banner_home,
                    AppConstant.CollapsibleGravity.BOTTOM,
                    object : AdCallback() {})
            } else {
                com.live.fluidwallpaper.magicapp.ads.AdsManager.loadBanner(
                    this,
                    BuildConfig.admob_banner_home,
                    mBinding.frBanner,
                    RemoteConfigUtils.getOnBannerHome()
                )
            }

        } else {
            mBinding.frBanner.removeAllViews()

        }
    }


    private fun loadDataPreset() {
        wallpaperAdapter = WallpaperAdapter(presetName = prefs.getString(
            AppConstants.KEY_NAME_EFFECT, "Floating Flames"
        ) ?: "", onClickItemSound = { presetModel, position ->


            if (com.live.fluidwallpaper.magicapp.ads.AdsManager.mInterstitialAdWallpaper != null && com.live.fluidwallpaper.magicapp.ads.CheckTimeShowAdsInter.isTimeShow && com.live.fluidwallpaper.magicapp.ads.AdsManager.mInterstitialAdWallpaper!!.isReady) {

                ITGAd.getInstance().forceShowInterstitial(
                    this, com.live.fluidwallpaper.magicapp.ads.AdsManager.mInterstitialAdWallpaper, object : ITGAdCallback() {
                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            moveToPresetActivity(presetModel)
                        }

                        override fun onAdClosed() {
                            super.onAdClosed()
                            com.live.fluidwallpaper.magicapp.ads.CheckTimeShowAdsInter.logShowed()
                            moveToPresetActivity(presetModel)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            moveToPresetActivity(presetModel)
                        }
                    }, true
                )

            } else {
                moveToPresetActivity(presetModel)
            }

        })
        listPresetModelTotal.clear()
        listPresetModelTotal.addAll(CommonData.getListPreset())
        listPresetModelTotal.addAll(CommonData.getListPresetCustom(this@MainActivity))
        addNativeToList(listPresetModelTotal)

        mBinding.rcvMainPreset.apply {
            layoutManager =
                GridLayoutManager(
                    this@MainActivity,
                    3,
                    GridLayoutManager.VERTICAL,
                    false
                ).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (listPresetModelTotal[position].typePresetModel == TypePresetModel.ADS) 3 else 1 //put your condition here
                        }
                    }
                }
            setHasFixedSize(true)
            adapter = wallpaperAdapter
        }

        wallpaperAdapter.submitData(listPresetModelTotal)
        wallpaperAdapter.setMList(listPresetModelTotal)

        wallpaperAdapter.filterTypePosition(TypePresetModel.TRENDING)

        mBinding.layoutEmpty.goneView()
        mBinding.ivAdd.goneView()


    }

    private fun addNativeToList(listPresetModelTotal: ArrayList<PresetModel>) {
        val itemsToAdd = ArrayList<PresetModel>()

        for (i in 0 until listPresetModelTotal.size) {
            itemsToAdd.add(listPresetModelTotal[i])
            if (isNetwork()) {
                if ((i + 1) % 9 == 0) {
                    val newItem = PresetModel(
                        R.drawable.bg_preset_vip1,
                        "Ads",
                        Status.FREE,
                        false,
                        typePresetModel = TypePresetModel.ADS
                    )
                    itemsToAdd.add(newItem)
                }
            }
        }
        listPresetModelTotal.clear()
        listPresetModelTotal.addAll(itemsToAdd)
    }

    override fun onClickViews() {
        super.onClickViews()

        mBinding.imvSettings.setOnClickListener {
            Routes.startSettingActivity(this)
        }


        mBinding.ivAdd.setOnClickListener {

            if (com.live.fluidwallpaper.magicapp.ads.AdsManager.mInterstitialAdDesign != null && com.live.fluidwallpaper.magicapp.ads.CheckTimeShowAdsInter.isTimeShow && com.live.fluidwallpaper.magicapp.ads.AdsManager.mInterstitialAdDesign!!.isReady) {

                ITGAd.getInstance().forceShowInterstitial(
                    this, com.live.fluidwallpaper.magicapp.ads.AdsManager.mInterstitialAdDesign, object : ITGAdCallback() {
                        override fun onAdFailedToLoad(adError: ApAdError?) {
                            super.onAdFailedToLoad(adError)
                            Routes.startCustomWallpaperActivity(this@MainActivity)
                        }

                        override fun onAdClosed() {
                            super.onAdClosed()
                            com.live.fluidwallpaper.magicapp.ads.CheckTimeShowAdsInter.logShowed()
                            Routes.startCustomWallpaperActivity(this@MainActivity)
                        }

                        override fun onAdFailedToShow(adError: ApAdError?) {
                            super.onAdFailedToShow(adError)
                            Routes.startCustomWallpaperActivity(this@MainActivity)
                        }
                    }, true
                )

            } else {
                Routes.startCustomWallpaperActivity(this@MainActivity)
            }
        }

    }

}

package com.live.fluidwallpaper.magicapp.ui.component.setting

import android.annotation.SuppressLint
import android.widget.Toast
import com.live.fluidwallpaper.magicapp.BuildConfig
import com.live.fluidwallpaper.magicapp.R
import com.live.fluidwallpaper.magicapp.ads.RemoteConfigUtils
import com.live.fluidwallpaper.magicapp.app.AppConstants
import com.live.fluidwallpaper.magicapp.databinding.ActivitySettingBinding
import com.live.fluidwallpaper.magicapp.ui.bases.BaseActivity
import com.live.fluidwallpaper.magicapp.ui.bases.ext.click
import com.live.fluidwallpaper.magicapp.ui.bases.ext.showRateDialog
import com.live.fluidwallpaper.magicapp.utils.SharePrefUtils

class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    override fun getLayoutActivity(): Int = R.layout.activity_setting

    override fun onClickViews() {
        super.onClickViews()

        mBinding.imvBackSettings.setOnClickListener {
            finish()
        }
        mBinding.layoutShare.click {
            shareApp()
        }

        mBinding.layoutMoreApp.setOnClickListener {
            if (RemoteConfigUtils.getIsShowRate()) {
                if (!SharePrefUtils.getBoolean(AppConstants.IS_RATED, false)) {
                    showRateDialog(this, false)
                } else {
                    Toast.makeText(this, getString(R.string.text_thank_you), Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, getString(R.string.text_thank_you), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    override fun initViews() {
        super.initViews()
        mBinding.textVersion.text =
            resources.getString(R.string.txt_version, BuildConfig.VERSION_NAME)
    }
}
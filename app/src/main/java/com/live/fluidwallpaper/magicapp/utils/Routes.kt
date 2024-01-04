package com.live.fluidwallpaper.magicapp.utils

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.Bundle
import com.live.fluidwallpaper.magicapp.app.AppConstants
import com.live.fluidwallpaper.magicapp.models.PresetModel
import com.live.fluidwallpaper.magicapp.ui.component.create_done.CreateDoneActivity
import com.live.fluidwallpaper.magicapp.ui.component.main.MainActivity
import com.live.fluidwallpaper.magicapp.ui.component.language.LanguageActivity
import com.live.fluidwallpaper.magicapp.ui.component.onboarding.OnBoardingActivity
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.WallpaperActivity
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.WallpaperLiveViewActivity
import com.live.fluidwallpaper.magicapp.ui.component.setting.SettingActivity
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.custom.CustomWallpaperActivity


object Routes {
    fun startMainActivity(fromActivity: Activity) =
        Intent(fromActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(AppConstants.KEY_TRACKING_SCREEN_FROM, fromActivity::class.java.simpleName)
            fromActivity.startActivity(this)
        }

    fun startMainActivity(fromActivity: Service, bundle: Bundle?) =
        Intent(fromActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(AppConstants.KEY_TRACKING_SCREEN_FROM, fromActivity::class.java.simpleName)
            bundle?.let { putExtras(it) }
            fromActivity.startActivity(this)
        }

    fun startSettingActivity(fromActivity: Activity) =
        Intent(fromActivity, SettingActivity::class.java).apply {
            putExtra(AppConstants.KEY_TRACKING_SCREEN_FROM, fromActivity::class.java.simpleName)
            fromActivity.startActivity(this)
        }


    fun startOnBoardingActivity(fromActivity: Activity) =
        Intent(fromActivity, OnBoardingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(AppConstants.KEY_TRACKING_SCREEN_FROM, fromActivity::class.java.simpleName)
            fromActivity.startActivity(this)

        }

    fun startLanguageActivity(fromActivity: Activity, bundle: Bundle?) =
        Intent(fromActivity, LanguageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(AppConstants.KEY_TRACKING_SCREEN_FROM, fromActivity::class.java.simpleName)
            bundle?.let { putExtras(it) }
            fromActivity.startActivity(this)

        }


    fun startPresetActivity(fromActivity: Activity, presetModel: PresetModel, isCustom: Boolean) =
        Intent(fromActivity, WallpaperActivity::class.java).apply {
            putExtra(AppConstants.KEY_TRACKING_SCREEN_FROM, fromActivity::class.java.simpleName)
            putExtra(AppConstants.KEY_PRESET_MODEL, presetModel)
            putExtra(AppConstants.KEY_IS_CUSTOM, isCustom)
            fromActivity.startActivity(this)

        }

    fun startPresetLiveActivity(fromActivity: Activity, presetModel: PresetModel) =
        Intent(fromActivity, WallpaperLiveViewActivity::class.java).apply {
            putExtra(AppConstants.KEY_TRACKING_SCREEN_FROM, fromActivity::class.java.simpleName)
            putExtra(AppConstants.KEY_PRESET_MODEL, presetModel)
            fromActivity.startActivity(this)

        }

    fun startCustomWallpaperActivity(fromActivity: Activity) =
        Intent(fromActivity, CustomWallpaperActivity::class.java).apply {
            putExtra(AppConstants.KEY_TRACKING_SCREEN_FROM, fromActivity::class.java.simpleName)
            fromActivity.startActivity(this)

        }

    fun startCreateDoneActivity(fromActivity: Activity, presetModel: PresetModel) =
        Intent(fromActivity, CreateDoneActivity::class.java).apply {
            putExtra(AppConstants.KEY_TRACKING_SCREEN_FROM, fromActivity::class.java.simpleName)
            putExtra(AppConstants.KEY_PRESET_MODEL, presetModel)
            fromActivity.startActivity(this)

        }

    fun addTrackingMoveScreen(fromActivity: String, toActivity: String) {
        ITGTrackingHelper.fromScreenToScreen(fromActivity, toActivity)
    }


}
package com.live.fluidwallpaper.magicapp.models

import android.content.res.AssetManager
import android.os.Parcelable
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage
import com.live.fluidwallpaper.magicapp.utils.TypePresetModel
import com.magicfluids.Config
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PresetModel(
    val imagePreset: Int,
    val name: String,
    val isLock: Status,
    val isNew: Boolean,
    var isSelected: Boolean? = false,
    var pathFluidCustom : String = "",
    var pathImageCustom : String = "",
    var typePresetModel: TypePresetModel? = TypePresetModel.ALL
) : Parcelable {

    fun fillConfig(config: Config?, assetManager: AssetManager?) {
        com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids.SettingsStorage.loadConfigFromInternalPreset(name, assetManager, config)
    }

}


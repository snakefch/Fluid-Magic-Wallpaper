package com.live.fluidwallpaper.magicapp.ui.component.main.adapter

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.ads.control.ads.ITGAd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.live.fluidwallpaper.magicapp.R

import com.live.fluidwallpaper.magicapp.ads.AdsManager
import com.live.fluidwallpaper.magicapp.databinding.ItemWallpaperBinding
import com.live.fluidwallpaper.magicapp.models.PresetModel
import com.live.fluidwallpaper.magicapp.models.Status
import com.live.fluidwallpaper.magicapp.ui.bases.BaseRecyclerView
import com.live.fluidwallpaper.magicapp.ui.bases.ext.click
import com.live.fluidwallpaper.magicapp.ui.bases.ext.goneView
import com.live.fluidwallpaper.magicapp.ui.bases.ext.isNetwork
import com.live.fluidwallpaper.magicapp.ui.bases.ext.visibleView
import com.live.fluidwallpaper.magicapp.ui.component.main.MainActivity
import com.live.fluidwallpaper.magicapp.utils.TypePresetModel


class WallpaperAdapter(
    var presetName: String, val onClickItemSound: (PresetModel, Int) -> Unit
) : BaseRecyclerView<PresetModel>() {
    private val mListData = ArrayList<PresetModel>()

    override fun getItemLayout() = R.layout.item_wallpaper

    override fun submitData(newData: List<PresetModel>) {
        val diffResult = DiffUtil.calculateDiff(WallpaperDiffUtil(newData, list))
        list.clear()
        list.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setCheckNewItem(presetName: String) {
        this.presetName = presetName
        notifyDataSetChanged()
    }

    override fun setData(binding: ViewDataBinding, item: PresetModel, layoutPosition: Int) {
        if (binding is ItemWallpaperBinding) {
            context?.let { ctx ->

                if (item.typePresetModel == TypePresetModel.ADS) {
                    binding.lBgr.goneView()
                    binding.relayAds.visibleView()
                    if (com.live.fluidwallpaper.magicapp.ads.AdsManager.nativeAdHome != null) {
                        binding.frAds.visibleView()

                        try {
                            ITGAd.getInstance().populateNativeAdView(
                                context as MainActivity,
                                com.live.fluidwallpaper.magicapp.ads.AdsManager.nativeAdHome,
                                binding.frAds,
                                binding.layoutShimmer.shimmerNativeLarge
                            )
                        } catch (_: Exception) {

                        }
                    } else {
                        binding.frAds.goneView()
                    }
                } else {
                    binding.lBgr.visibleView()
                    binding.relayAds.goneView()

                    if (item.typePresetModel == TypePresetModel.CUSTOM) {
                        Glide.with(ctx).load(item.pathImageCustom).into(binding.imagePreset)
                    } else {
                        Glide.with(ctx).load(item.imagePreset).diskCacheStrategy(
                            DiskCacheStrategy.DATA
                        ).into(binding.imagePreset)
                    }


                    binding.textNamePreset.text = item.name
                    if (list[layoutPosition].name == presetName) {
                        binding.checkItemSelected.visibleView()
                        binding.imvApplySelected.visibleView()
                    } else {
                        binding.checkItemSelected.goneView()
                        binding.imvApplySelected.goneView()
                    }
                }

            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onResizeViews(binding: ViewDataBinding) {
        super.onResizeViews(binding)
    }

    override fun onClickViews(binding: ViewDataBinding, obj: PresetModel, layoutPosition: Int) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemWallpaperBinding) {
            binding.root.click { v: View? ->
                if (obj.typePresetModel != TypePresetModel.ADS) {
                    onClickItemSound(obj, layoutPosition)
                }
            }

        }
    }


    fun setMList(list: List<PresetModel>) {
        mListData.clear()
        mListData.addAll(list)
    }

    fun filterTypePosition(type: TypePresetModel) {
        when (type) {

            TypePresetModel.CUSTOM -> {
                submitData(addItemAds(mListData.filter { it.typePresetModel == TypePresetModel.CUSTOM }))
            }

            TypePresetModel.NEW -> {
                submitData(addItemAds(mListData.filter { it.typePresetModel == TypePresetModel.NEW }))
            }

            TypePresetModel.TRENDING -> {
                submitData(addItemAds(mListData.filter { it.typePresetModel == TypePresetModel.TRENDING }))
            }

            TypePresetModel.HOT -> {
                submitData(addItemAds(mListData.filter { it.typePresetModel == TypePresetModel.HOT }))
            }

            TypePresetModel.ALL -> {
                submitData(mListData)
            }

            TypePresetModel.ADS -> {

            }
        }
    }

    private fun addItemAds(listData: List<PresetModel>): List<PresetModel> {

        val itemsToAdd = ArrayList<PresetModel>()

        for (i in listData.indices) {
            itemsToAdd.add(listData[i])
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
        return itemsToAdd

    }


}
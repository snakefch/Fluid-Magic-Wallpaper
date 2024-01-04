package com.live.fluidwallpaper.magicapp.ui.component

import android.content.Context
import android.graphics.Bitmap
import com.live.fluidwallpaper.magicapp.R

import com.live.fluidwallpaper.magicapp.databinding.DialogShareImageBinding
import com.live.fluidwallpaper.magicapp.ui.bases.BaseDialog

class DialogShare(context: Context, private val bitmap: Bitmap?) :
    BaseDialog<DialogShareImageBinding>(context) {
    override fun getLayoutDialog(): Int = R.layout.dialog_share_image
    override fun initViews() {
        super.initViews()
        if (bitmap != null) {
            mBinding.imageShare.setImageBitmap(bitmap)

        }
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.imvClose.setOnClickListener {
            dismiss()
        }
    }

}
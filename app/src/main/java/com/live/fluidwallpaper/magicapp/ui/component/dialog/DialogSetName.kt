package com.live.fluidwallpaper.magicapp.ui.component.dialog

import android.app.Activity
import com.live.fluidwallpaper.magicapp.R
import com.live.fluidwallpaper.magicapp.databinding.DialogSetNameBinding
import com.live.fluidwallpaper.magicapp.ui.bases.BaseDialog
import com.live.fluidwallpaper.magicapp.ui.bases.ext.showKeyboard

class DialogSetName(val context: Activity, val onCreatePreset: (String) -> Unit) :
    BaseDialog<DialogSetNameBinding>(context) {
    override fun getLayoutDialog(): Int = R.layout.dialog_set_name

    override fun initViews() {
        super.initViews()
        context.showKeyboard(mBinding.edtFileName)

    }
    override fun onClickViews() {
        super.onClickViews()

        mBinding.tvCreate.setOnClickListener {

            if (mBinding.edtFileName.text.toString().isNotEmpty()){
                onCreatePreset.invoke(mBinding.edtFileName.text.toString())
            }
        }
    }

}
package com.mobile.massiveapp.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.mobile.massiveapp.databinding.DialogLoadingCustomBinding
import pl.droidsonroids.gif.GifDrawable

class BaseDialogLoadingCustom (
    private val mActivity: Activity,
    private val title: String,
    private val gif: GifDrawable
) {
    private lateinit var binding: DialogLoadingCustomBinding
    private lateinit var isDialog: AlertDialog

    fun startLoading(){
        binding = DialogLoadingCustomBinding.inflate(LayoutInflater.from(mActivity))
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(binding.root)
        builder.setCancelable(false)

        binding.gifImageView.setImageDrawable(gif)
        binding.txvTitle.text = title
        isDialog = builder.create()
        isDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        isDialog.show()
    }


    fun updateProgress(progress: Int, message: String, maxLength: Int){
        binding.progressBar.max = maxLength
        binding.progressBar.progress = progress
        binding.txvDialogLoadingTitle.text = "Sincronizando $message..."
    }

    fun onDismiss(){
        isDialog.dismiss()
    }

}
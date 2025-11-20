package com.mobile.massiveapp.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.graphics.PorterDuff
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.DialogLoadingBinding

class BaseDialogLoading(
    private val mActivity: Activity,
    private val title: String = "Sincronizando Datos Maestros..."
) {
    private lateinit var binding: DialogLoadingBinding
    private lateinit var isDialog: AlertDialog


    fun startLoading(){
        binding = DialogLoadingBinding.inflate(LayoutInflater.from(mActivity))
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(binding.root)
        builder.setCancelable(false)

        binding.txvTitle.text = title
        binding.progressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(mActivity, R.color.color_massive_deep_blue_v2),
            PorterDuff.Mode.SRC_IN
        )
        isDialog = builder.create()
        isDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        isDialog.show()
    }


    fun updateProgress(progress: Int, message: String, maxLength: Int){
        binding.progressBar.max = maxLength
        binding.progressBar.progress = progress
        binding.txvDialogLoadingTitle.text = "Sincronizando $message..."
        binding.txvPorcentaje.text = "${progress}/${maxLength} "

        if (progress == maxLength-1){
            binding.txvDialogLoadingTitle.text = message
            binding.progressBar.isIndeterminate = true
            binding.txvPorcentaje.isVisible = false
        } else {
            binding.txvDialogLoadingTitle.text = "Guardando $message"
            binding.progressBar.isIndeterminate = false
            binding.txvPorcentaje.isVisible = true
        }

    }

    fun onDismiss(){
        isDialog.dismiss()
    }

    fun updateTitle(title: String){
        binding.txvDialogLoadingTitle.text = title
    }

}
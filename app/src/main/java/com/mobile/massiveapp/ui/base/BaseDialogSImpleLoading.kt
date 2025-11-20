package com.mobile.massiveapp.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.mobile.massiveapp.databinding.DialogSimpleLoadingBinding

class BaseDialogSImpleLoading (
    private val mActivity: Activity
) {
    private lateinit var binding: DialogSimpleLoadingBinding
    private lateinit var isDialog: AlertDialog

    fun startLoading(titulo: String = "Cargando..."){
        binding = DialogSimpleLoadingBinding.inflate(LayoutInflater.from(mActivity))
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(binding.root)
        builder.setCancelable(false)

        binding.txvTituloLoadingValue.text = titulo

        isDialog = builder.create()
        isDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        isDialog.show()
    }


    fun onDismiss(){
        isDialog.dismiss()
    }

}
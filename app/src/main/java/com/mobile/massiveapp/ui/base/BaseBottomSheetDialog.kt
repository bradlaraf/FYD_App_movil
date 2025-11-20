package com.mobile.massiveapp.ui.base

import android.app.Activity
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.BottomSheetDialogDireccionBinding
import com.mobile.massiveapp.ui.adapters.DialogBottomItemAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class BaseBottomSheetDialog(
    private val mActivity: Activity,
    private val titulo: String,
    private val valor: String
) {
    private lateinit var binding: BottomSheetDialogDireccionBinding
    private lateinit var bottomDialog: BottomSheetDialog
    private lateinit var dialogBottomItemAdapter: DialogBottomItemAdapter

    fun showBottomSheetDialog(listaValores: List<HashMap<String, String>>){
        binding = BottomSheetDialogDireccionBinding.inflate(mActivity.layoutInflater)

        dialogBottomItemAdapter = DialogBottomItemAdapter(listaValores){}
        binding.rvDialogBottom.adapter = dialogBottomItemAdapter
        binding.txvDialogDireccionDireccion.text = titulo
        binding.txvDialogDireccionTipoDireccion.text = valor

        bottomDialog = BottomSheetDialog(mActivity, R.style.AppBottomSheetDialog)
        bottomDialog.setContentView(binding.root)
        bottomDialog.show()
    }
}
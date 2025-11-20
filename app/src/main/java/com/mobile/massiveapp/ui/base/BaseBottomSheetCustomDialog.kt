package com.mobile.massiveapp.ui.base

import android.app.Activity
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.BottomSheetDialogDireccionBinding
import com.mobile.massiveapp.ui.adapters.DialogBottonItemCustomAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class BaseBottomSheetCustomDialog (
    private val imageResource: Int,
    private val mActivity: Activity,
    private val titulo: String,
    private val valor: String
) {
    private lateinit var binding: BottomSheetDialogDireccionBinding
    private lateinit var bottomDialog: BottomSheetDialog
    private lateinit var dialogBottomItemAdapter: DialogBottonItemCustomAdapter

    fun showBottomSheetDialog(listaValores: List<HashMap<String, Pair<Int, String>>>){
        binding = BottomSheetDialogDireccionBinding.inflate(mActivity.layoutInflater)

        dialogBottomItemAdapter = DialogBottonItemCustomAdapter(listaValores){}
        binding.rvDialogBottom.adapter = dialogBottomItemAdapter
        binding.txvDialogDireccionDireccion.text = titulo
        binding.txvDialogDireccionTipoDireccion.text = valor
        binding.imvIconBS.setImageResource(imageResource)

        bottomDialog = BottomSheetDialog(mActivity, R.style.AppBottomSheetDialog)
        bottomDialog.setContentView(binding.root)
        bottomDialog.show()
    }
}
package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mobile.massiveapp.databinding.DialogInformativeSimpleBinding

class BaseSimpleInformativeDialog(
    private val titulo: String,
    private val mensaje: String
): DialogFragment() {
    private lateinit var binding: DialogInformativeSimpleBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogInformativeSimpleBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.txvSimpleDialogTitle.text = titulo
        binding.txvSimpleDialogBody.text = mensaje

        binding.dialogBtnOkay.setOnClickListener {
            dismiss()
        }

        //Seteo visual para que se vean los bordes redondeados
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}
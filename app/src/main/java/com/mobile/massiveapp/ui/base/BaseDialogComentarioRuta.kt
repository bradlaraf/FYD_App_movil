package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.mobile.massiveapp.databinding.DialogComentarioRutaBinding

class BaseDialogComentarioRuta(
    private val comentarioActual: String,
    private val onAceptar: (String) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogComentarioRutaBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogComentarioRutaBinding.inflate(layoutInflater)
        binding.edtComentarioInput.setText(comentarioActual)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        val dialog = builder.create()

        binding.dialogBtnOkay.setOnClickListener {
            onAceptar.invoke(binding.edtComentarioInput.text.toString())
            dismiss()
        }

        binding.dialogBtnCancel.setOnClickListener {
            dismiss()
        }

        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        showKeyboard()
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun showKeyboard() {
        binding.edtComentarioInput.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

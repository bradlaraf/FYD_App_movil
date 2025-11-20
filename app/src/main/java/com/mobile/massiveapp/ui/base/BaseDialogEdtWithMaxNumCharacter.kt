package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.mobile.massiveapp.databinding.DialogNuevoClienteInputBinding

class BaseDialogEdtWithMaxNumCharacter (
    private val maxNumber: Int,
    private val textEditable: String,
    private val onRegisterClickListener: (String) -> Unit
): DialogFragment() {

    private lateinit var binding: DialogNuevoClienteInputBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNuevoClienteInputBinding.inflate(LayoutInflater.from(context))
        binding.edtDialogValorInput.inputType = InputType.TYPE_CLASS_NUMBER
        binding.edtDialogValorInput.setText(textEditable)
        binding.edtDialogValorInput.filters = arrayOf(android.text.InputFilter.LengthFilter(4))
        if(binding.edtDialogValorInput.text.toString().trim().isNotEmpty()){
            binding.edtDialogValorInput.addTextChangedListener { editable->
                val editableSafe: Int = editable.toString().toIntOrNull()?:0
                if(editableSafe >= maxNumber){
                    binding.edtDialogValorInput.error = "$maxNumber unidades como máximo"
                }
            }
        }
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()

        binding.dialogBtnOkay.setOnClickListener {

            if (binding.edtDialogValorInput.text.toString().isEmpty()){
                binding.edtDialogValorInput.error = "Campo requerido"

            } else if(binding.edtDialogValorInput.text.toString().toInt() > maxNumber){
                binding.edtDialogValorInput.error = "$maxNumber unidades como máximo"

            } else {
                dismiss()
                onRegisterClickListener.invoke(binding.edtDialogValorInput.text.toString())
            }
        }
        binding.dialogBtnCancel.setOnClickListener {
            dismiss()
        }

        //Seteo visual para que se vean los bordes redondeados

        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        showKeyboard()
        return dialog

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun showKeyboard() {
        binding.edtDialogValorInput.requestFocus()
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }



}
package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.mobile.massiveapp.databinding.DialogNuevoClienteInputBinding

class BaseDialogEdt(
    private val textEditable: String,
    private val onRegisterClickListener: (String) -> Unit
):DialogFragment() {
    constructor(
        textEditable: String,
        maxLenght: Int,
        onRegisterClickListener: (String) -> Unit
    ):this (textEditable, onRegisterClickListener
    ){
        this.maxLenght = maxLenght
    }


    private lateinit var binding: DialogNuevoClienteInputBinding
    private var maxLenght: Int = -1


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNuevoClienteInputBinding.inflate(layoutInflater)
        binding.edtDialogValorInput.setText(textEditable)


        binding.edtDialogValorInput.addTextChangedListener { editable->
            if(editable.toString().length < maxLenght && maxLenght != -1){
                //Set Maximo de caracteres
                binding.edtDialogValorInput.filters = arrayOf(android.text.InputFilter.LengthFilter(maxLenght))
                binding.edtDialogValorInput.error = "Mínimo $maxLenght"
            } else {
                binding.edtDialogValorInput.error = null
            }
        }
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()

        binding.dialogBtnOkay.setOnClickListener {
            onRegisterClickListener.invoke(binding.edtDialogValorInput.text.toString())
            if (binding.edtDialogValorInput.text.toString().isEmpty()){
                binding.edtDialogValorInput.error = "Campo requerido"
                return@setOnClickListener
            }
            dismiss()
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
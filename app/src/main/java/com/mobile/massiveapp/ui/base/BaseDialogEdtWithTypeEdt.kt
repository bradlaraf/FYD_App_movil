package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.mobile.massiveapp.databinding.DialogNuevoClienteInputBinding

class BaseDialogEdtWithTypeEdt(
    private val tipo: String,
    private val textEditable: String,
    private val titulo: String = "Ingrese el valor",
    private val onRegisterClickListener: (String) -> Unit
):DialogFragment() {
    private lateinit var binding: DialogNuevoClienteInputBinding
    private val EDT_UNABLED = -1
    private var maxLenght: Int = EDT_UNABLED

    constructor(tipo: String,
                textEditable: String,
                maxLenght: Int,
                onRegisterClickListener: (String) -> Unit):this(tipo, textEditable, "", onRegisterClickListener
    ){
        this.maxLenght = maxLenght
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNuevoClienteInputBinding.inflate(layoutInflater)
        setDefaultUi()
        binding.edtDialogValorInput.setText(textEditable)
        if(maxLenght != EDT_UNABLED){
            binding.edtDialogValorInput.filters = arrayOf(android.text.InputFilter.LengthFilter(maxLenght))

            binding.edtDialogValorInput.addTextChangedListener { editable->
                binding.edtDialogValorInput.error = "Mínimo $maxLenght dígitos"
            }
        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.edtDialogValorInput.inputType = when(tipo){
            "text" -> InputType.TYPE_CLASS_TEXT
            "email" -> 33
            "phone" -> InputType.TYPE_CLASS_NUMBER
            "decimal" -> InputType.TYPE_CLASS_PHONE
            else -> 1
        }


        binding.dialogBtnOkay.setOnClickListener {
            if (binding.edtDialogValorInput.text.toString().trim().isEmpty()){
                binding.edtDialogValorInput.error = "Campo requerido"
                return@setOnClickListener
            }
            if (!binding.edtDialogValorInput.text.isNullOrEmpty()){
                if (maxLenght != EDT_UNABLED  && binding.edtDialogValorInput.text!!.length < maxLenght){
                    binding.edtDialogValorInput.error = "$maxLenght digitos como mínimo"
                    return@setOnClickListener
                }
            }
            if (tipo == "decimal"){
                val montoDouble = binding.edtDialogValorInput.text.toString().toDoubleOrNull()?:-11.0
                if (montoDouble == -11.0){
                    binding.edtDialogValorInput.error = "Formato incorrecto"
                    return@setOnClickListener
                }
            }
            onRegisterClickListener.invoke(binding.edtDialogValorInput.text.toString())
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

    private fun setDefaultUi() {
        binding.txvDialogTitle.text = titulo
        binding.edtDialogValorContainer.hint = titulo
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
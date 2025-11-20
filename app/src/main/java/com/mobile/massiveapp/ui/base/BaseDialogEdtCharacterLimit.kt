package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.mobile.massiveapp.databinding.DialogNuevoClienteInputBinding

class BaseDialogEdtCharacterLimit  (
    private val textEditable: String,
    private val titulo: String = "Ingrese el valor",
    private val onRegisterClickListener: (String) -> Unit
): DialogFragment() {

    private lateinit var binding: DialogNuevoClienteInputBinding
    private val MAX_NUMBER_CHARACTER = 250

    // Filtro personalizado para permitir solo letras, números y .,:-_()/ caracteres
    private val characterFilter = InputFilter { source, start, end, dest, dstart, dend ->
        null
        /*
        val regex = Regex("[a-zA-Z0-9.,:_() 'ñÑ%/-]*")
        if (source.toString().matches(regex)) {
            null // Permite el texto
        } else {
            "" // Bloquea el texto no permitido
        }*/
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNuevoClienteInputBinding.inflate(layoutInflater)
        binding.edtDialogValorInput.inputType = InputType.TYPE_CLASS_TEXT
        binding.edtDialogValorInput.setText(textEditable)
        binding.txvDialogTitle.text = titulo
        binding.edtDialogValorContainer.hint = titulo

        // Aplicar el filtro de caracteres permitidos
        binding.edtDialogValorInput.filters = arrayOf(characterFilter)

        // Resto del código...

        binding.edtDialogValorInput.addTextChangedListener { editable ->
            val numberCharacters = editable.toString().trim().toCharArray().size
            if (numberCharacters <= MAX_NUMBER_CHARACTER) {
                val restante = MAX_NUMBER_CHARACTER - numberCharacters
                binding.edtDialogValorInput.error = "$restante caracteres restantes"
            } else {
                val exceso = numberCharacters - MAX_NUMBER_CHARACTER
                binding.edtDialogValorInput.error = "$exceso caracteres excedidos"
            }
        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()

        binding.dialogBtnOkay.setOnClickListener {
            if (binding.edtDialogValorInput.text.toString().isEmpty()) {
                binding.edtDialogValorInput.error = "Campo requerido"
            } else if (binding.edtDialogValorInput.text.toString().toCharArray().size > MAX_NUMBER_CHARACTER) {
                binding.edtDialogValorInput.error = "$MAX_NUMBER_CHARACTER como máximo"
            } else {
                dismiss()
                onRegisterClickListener.invoke(binding.edtDialogValorInput.text.toString())
            }
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
package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mobile.massiveapp.databinding.DialogChecklistBaseBinding


class BaseDialogChecklistWithId (
    private var opciones: List<String>,
    private val onRegisterClickListener: (String, Int) -> Unit
):DialogFragment() {


    constructor(checkSelected: String,
                opciones: List<String>,
                onRegisterClickListener: (String, Int) -> Unit,

    ):this(opciones, onRegisterClickListener){
        this.checkSelected = checkSelected
    }


    private lateinit var binding: DialogChecklistBaseBinding
    private var optionSelected: String = ""
    private var checkSelected: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogChecklistBaseBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val radioGroup = binding.rgOptionsDialog
        for (i in 0 until opciones.size){
            val radioButton = RadioButton(context)
            radioButton.text = opciones[i]
            radioButton.id = i
            if (opciones[i].uppercase() == checkSelected.uppercase()){
                radioButton.isChecked = true
                optionSelected = opciones[i]
            }
            radioGroup.addView(radioButton)
        }



        binding.rgOptionsDialog.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            optionSelected = radioButton.text.toString()
        }

        binding.dialogBtnOkay.setOnClickListener {
            onRegisterClickListener.invoke(optionSelected, binding.rgOptionsDialog.checkedRadioButtonId)
            if (optionSelected.isEmpty()){
                Toast.makeText(context, "Selecciona una opción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dismiss()
        }



        binding.dialogBtnCancel.setOnClickListener {
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog

    }
}
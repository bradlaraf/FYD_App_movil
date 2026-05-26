package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.DialogFragment
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.DialogDireccionClienteBinding
import com.mobile.massiveapp.databinding.ItemDialogDireccionBinding
import com.mobile.massiveapp.domain.model.DoDireccion

class BaseDialogDireccionCliente(
    private val checkSelected: String,
    private val direcciones: List<DoDireccion>,
    private val onSeleccionar: (DoDireccion, Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogDireccionClienteBinding
    private val radioButtons = mutableListOf<RadioButton>()
    private var indexSeleccionado: Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDireccionClienteBinding.inflate(LayoutInflater.from(context))

        direcciones.forEachIndexed { index, direccion ->
            val itemBinding = ItemDialogDireccionBinding.inflate(
                LayoutInflater.from(context), binding.llDirecciones, false
            )

            itemBinding.txvDireccionTipo.text = mapTipo(direccion.AdresType)
            DrawableCompat.setTint(
                DrawableCompat.wrap(itemBinding.txvDireccionTipo.background).mutate(),
                colorParaTipo(direccion.AdresType)
            )
            itemBinding.txvDireccionCalle.text = direccion.Street

            if (direccion.Street.uppercase() == checkSelected.uppercase()) {
                itemBinding.rbDireccion.isChecked = true
                indexSeleccionado = index
            }

            itemBinding.root.setOnClickListener {
                radioButtons.forEach { it.isChecked = false }
                itemBinding.rbDireccion.isChecked = true
                indexSeleccionado = index
            }

            radioButtons.add(itemBinding.rbDireccion)
            binding.llDirecciones.addView(itemBinding.root)
        }

        binding.dialogBtnOkay.setOnClickListener {
            if (indexSeleccionado == -1) {
                Toast.makeText(context, "Selecciona una dirección", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            onSeleccionar(direcciones[indexSeleccionado], indexSeleccionado)
            dismiss()
        }

        binding.dialogBtnCancel.setOnClickListener { dismiss() }

        val dialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    private fun mapTipo(adresType: String) = when (adresType) {
        "B" -> "Fiscal"
        "S" -> "Entrega"
        else -> adresType
    }

    private fun colorParaTipo(adresType: String) = when (adresType) {
        "B" -> ContextCompat.getColor(requireContext(), R.color.color_blue)
        "S" -> ContextCompat.getColor(requireContext(), R.color.color_green_dark)
        else -> ContextCompat.getColor(requireContext(), R.color.color_gris_oscuro)
    }
}

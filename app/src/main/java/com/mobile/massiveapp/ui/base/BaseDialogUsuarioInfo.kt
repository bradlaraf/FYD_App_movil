package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import com.mobile.massiveapp.databinding.ItemUsuarioContentSelectableBinding
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.ui.adapters.NuevoUsuarioAdapter

class BaseDialogUsuarioInfo(
    private val listaValores: List<DoNuevoUsuarioItem>,
    private val titulo: String,
    val onAcceptButton:(List<DoNuevoUsuarioItem>) -> Unit
): DialogFragment(){
    private lateinit var binding: ItemUsuarioContentSelectableBinding
    private lateinit var userAdapter: NuevoUsuarioAdapter
    private var selectClicked = true


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ItemUsuarioContentSelectableBinding.inflate(layoutInflater)
        binding.txvUsuarioDialogTitle.text = titulo

        userAdapter = NuevoUsuarioAdapter(listaValores){}
        binding.rvUsuariosInfo.adapter = userAdapter

        //Button Select All
        binding.btnSelectAll.setOnClickListener {
            userAdapter.selectAll(selectClicked)
            selectClicked = !selectClicked
        }


        binding.searchViewUsuarios.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val usuarioInfoFiltrada = listaValores.filter { it.Name.contains(newText.toString(), ignoreCase = true) }
                userAdapter.updateData(usuarioInfoFiltrada)
                return true
            }
        })

        binding.btnUsuarioAceptar.setOnClickListener {
            binding.searchViewUsuarios.setQuery("",false)

            onAcceptButton(userAdapter.getAllData())
            dismiss()
        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        return builder.create()
    }
}
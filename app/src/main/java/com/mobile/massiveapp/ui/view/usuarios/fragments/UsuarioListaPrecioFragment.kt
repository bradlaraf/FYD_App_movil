package com.mobile.massiveapp.ui.view.usuarios.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentUsuarioListaPrecioBinding
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.ui.adapters.InfoUsuarioAdapter
import com.mobile.massiveapp.ui.adapters.NuevoUsuarioAdapter
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsuarioListaPrecioFragment : Fragment() {
    private var _binding: FragmentUsuarioListaPrecioBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvAdapter: InfoUsuarioAdapter
    private lateinit var nuevoUsuarioAdapter: NuevoUsuarioAdapter
    private val usuariosViewModel: UsuariosViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuarioListaPrecioBinding.inflate(inflater, container, false)
        rvAdapter = InfoUsuarioAdapter(emptyList()){}
        nuevoUsuarioAdapter = NuevoUsuarioAdapter(emptyList()){}
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usuariosViewModel.dataGetAllUsuarioListaPrecios.observe(viewLifecycleOwner){ listaPrecios->
            binding.rvUsuarioListaPrecios.adapter = rvAdapter
            val usuarioListaPreciosHash = listaPrecios.map { hashMapOf(it.ListName to "${it.ListNum}") }
            rvAdapter.updateData(usuarioListaPreciosHash)

            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val listaFiltrada = listaPrecios.filter { it.ListName.contains(newText, ignoreCase = true) }
                rvAdapter.updateData(listaFiltrada.map { hashMapOf(it.ListName to "${it.ListNum}") })
            }
        }

        usuariosViewModel.dataGetAllUsuarioListaPreciosCreacion.observe(viewLifecycleOwner){ listaPreciosCreacion->
            binding.rvUsuarioListaPrecios.adapter = nuevoUsuarioAdapter
            nuevoUsuarioAdapter.updateData(listaPreciosCreacion)
        }

        providerViewModel.dataGetContador.observeOnce(viewLifecycleOwner){
            if (it == 2){
                providerViewModel.saveListaPrecios(getListaPreciosChecked())
                providerViewModel.saveContador(3)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun getListaPreciosChecked():List<DoNuevoUsuarioItem> {
        return nuevoUsuarioAdapter.getAllData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
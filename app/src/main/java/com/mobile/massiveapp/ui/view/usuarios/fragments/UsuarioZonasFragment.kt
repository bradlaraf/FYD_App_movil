package com.mobile.massiveapp.ui.view.usuarios.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentUsuarioZonasBinding
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.ui.adapters.InfoUsuarioAdapter
import com.mobile.massiveapp.ui.adapters.NuevoUsuarioAdapter
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsuarioZonasFragment : Fragment() {
    private var _binding: FragmentUsuarioZonasBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvAdapter: InfoUsuarioAdapter
    private lateinit var nuevoUsuarioAdapter: NuevoUsuarioAdapter
    private val usuariosViewModel: UsuariosViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuarioZonasBinding.inflate(inflater, container, false)
        rvAdapter = InfoUsuarioAdapter(emptyList()){}
        nuevoUsuarioAdapter = NuevoUsuarioAdapter(emptyList()){}
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usuariosViewModel.dataGetAllUsuarioZonas.observe(viewLifecycleOwner){ zonas->
            binding.rvUsuarioZonas.adapter = rvAdapter
            val usuarioZonasHash = zonas.map { hashMapOf(it.Name to it.Code) }
            rvAdapter.updateData(usuarioZonasHash)

            //Busqueda
            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val listaFiltrada = zonas.filter { it.Name.contains(newText, ignoreCase = true) }
                rvAdapter.updateData(listaFiltrada.map { hashMapOf(it.Name to it.Code) })
            }
        }

        usuariosViewModel.dataGetAllUsuarioZonasCreacion.observe(viewLifecycleOwner){ zonasCreacion->
            binding.rvUsuarioZonas.adapter = nuevoUsuarioAdapter
            nuevoUsuarioAdapter.updateData(zonasCreacion)
        }

        providerViewModel.dataGetContador.observeOnce(viewLifecycleOwner){
            if (it == 5){
                providerViewModel.saveZonas(getZonasChecked())
                providerViewModel.saveContador(6)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun getZonasChecked():List<DoNuevoUsuarioItem> {
        return nuevoUsuarioAdapter.getAllData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
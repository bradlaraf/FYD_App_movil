package com.mobile.massiveapp.ui.view.usuarios.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentUsuarioAlmacenesBinding
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.ui.adapters.InfoUsuarioAdapter
import com.mobile.massiveapp.ui.adapters.NuevoUsuarioAdapter
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UsuarioAlmacenesFragment : Fragment() {
    private lateinit var rvAdapter: InfoUsuarioAdapter
    private lateinit var nuevoUsuarioAdapter: NuevoUsuarioAdapter
    private var _binding: FragmentUsuarioAlmacenesBinding? = null
    private val binding get() = _binding!!
    private val usuariosViewModel: UsuariosViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuarioAlmacenesBinding.inflate(inflater, container, false)
        rvAdapter = InfoUsuarioAdapter(emptyList()){}
        nuevoUsuarioAdapter = NuevoUsuarioAdapter(emptyList()){}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usuariosViewModel.dataGetAllUsuarioAlmacenes.observe(viewLifecycleOwner){ almacenes->
            binding.rvAlmacenes.adapter = rvAdapter
            val listaHashAlmacenes = almacenes.map { hashMapOf(it.WhsName to it.WhsCode) }
            rvAdapter.updateData(listaHashAlmacenes)

            //Busqueda
            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val listaFiltrada = almacenes.filter { it.WhsName.contains(newText,ignoreCase = true) }
                rvAdapter.updateData(listaFiltrada.map { hashMapOf(it.WhsName to it.WhsCode)  })
            }
        }

        usuariosViewModel.dataGetAllUsuarioAlmacenesCreacion.observe(viewLifecycleOwner){ almacenesCreacion->
            binding.rvAlmacenes.adapter = nuevoUsuarioAdapter
            nuevoUsuarioAdapter.updateData(almacenesCreacion)
        }

        providerViewModel.dataGetContador.observeOnce(viewLifecycleOwner){
            if (it == 1){
                providerViewModel.saveAlmacenes(getAlmacenesChecked())
                providerViewModel.saveContador(2)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun getAlmacenesChecked():List<DoNuevoUsuarioItem> {
        return nuevoUsuarioAdapter.getAllData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
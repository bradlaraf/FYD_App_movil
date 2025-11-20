package com.mobile.massiveapp.ui.view.usuarios.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentUsuarioGrupoSocioBinding
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.ui.adapters.InfoUsuarioAdapter
import com.mobile.massiveapp.ui.adapters.NuevoUsuarioAdapter
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsuarioGrupoSocioFragment : Fragment() {
    private var _binding: FragmentUsuarioGrupoSocioBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvAdapter: InfoUsuarioAdapter
    private lateinit var nuevoUsuarioAdapter: NuevoUsuarioAdapter
    private val usuariosViewModel: UsuariosViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuarioGrupoSocioBinding.inflate(inflater, container, false)
        rvAdapter = InfoUsuarioAdapter(emptyList()){}
        nuevoUsuarioAdapter = NuevoUsuarioAdapter(emptyList()){}

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        usuariosViewModel.dataGetAllUsuarioGrupoSocios.observe(viewLifecycleOwner){ grupoSocios->
            binding.rvUsuarioGrupoSocios.adapter = rvAdapter

            val usuarioGSHash = grupoSocios.map { hashMapOf(it.GroupName to it.GroupCode) }
            rvAdapter.updateData(usuarioGSHash)

            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val listaFiltrada = grupoSocios.filter { it.GroupName.contains(newText, ignoreCase = true) }
                rvAdapter.updateData(listaFiltrada.map { hashMapOf(it.GroupName to it.GroupCode) })
            }
        }

        usuariosViewModel.dataGetAllUsuarioGrupoSociosCreacion.observe(viewLifecycleOwner){ grupoSociosCreacion->
            binding.rvUsuarioGrupoSocios.adapter = nuevoUsuarioAdapter
            nuevoUsuarioAdapter.updateData(grupoSociosCreacion)
        }

        providerViewModel.dataGetContador.observeOnce(viewLifecycleOwner){
            if (it == 4){
                providerViewModel.saveGrupoSocios(getGrupoSociosChecked())
                providerViewModel.saveContador(5)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun getGrupoSociosChecked():List<DoNuevoUsuarioItem> {
        return nuevoUsuarioAdapter.getAllData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
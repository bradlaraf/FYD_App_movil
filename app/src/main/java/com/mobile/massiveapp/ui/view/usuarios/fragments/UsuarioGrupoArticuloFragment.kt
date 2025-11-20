package com.mobile.massiveapp.ui.view.usuarios.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentUsuarioGrupoArticuloBinding
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.ui.adapters.InfoUsuarioAdapter
import com.mobile.massiveapp.ui.adapters.NuevoUsuarioAdapter
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsuarioGrupoArticuloFragment : Fragment() {
    private var _binding: FragmentUsuarioGrupoArticuloBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvAdapter: InfoUsuarioAdapter
    private lateinit var nuevoUsuarioAdapter: NuevoUsuarioAdapter
    private val usuariosViewModel: UsuariosViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuarioGrupoArticuloBinding.inflate(inflater, container, false)
        rvAdapter = InfoUsuarioAdapter(emptyList()){}
        nuevoUsuarioAdapter = NuevoUsuarioAdapter(emptyList()){}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usuariosViewModel.dataGetAllUsuarioGrupoArticulos.observe(viewLifecycleOwner){ grupoArticulos->
            binding.rvUsuarioGrupoArticulo.adapter = rvAdapter
            val usuarioGAHash = grupoArticulos.map { hashMapOf(it.ItmsGrpNam to it.ItmsGrpCod) }
            rvAdapter.updateData(usuarioGAHash)

            //Busqueda
            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val listaFiltrada = grupoArticulos.filter { it.ItmsGrpNam.contains(newText, ignoreCase = true) }
                rvAdapter.updateData(listaFiltrada.map { hashMapOf(it.ItmsGrpNam to it.ItmsGrpCod) })
            }
        }

        usuariosViewModel.dataGetAllUsuarioGrupoArticulosCreacion.observe(viewLifecycleOwner){ grupoArticuloCreacion->
            binding.rvUsuarioGrupoArticulo.adapter = nuevoUsuarioAdapter
            /*binding.bubbleScrollBar.attachToRecyclerView(binding.rvUsuarioGrupoArticulo)
            binding.bubbleScrollBar.bubbleTextProvider = BubbleTextProvider {
                position-> StringBuilder(grupoArticuloCreacion[position].Name.substring(0,1)).toString()
            }*/
            nuevoUsuarioAdapter.updateData(grupoArticuloCreacion)
        }

        providerViewModel.dataGetContador.observeOnce(viewLifecycleOwner){
            if (it == 3){
                providerViewModel.saveGrupoArticulos(getGrupoArticulosChecked())
                providerViewModel.saveContador(4)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun getGrupoArticulosChecked():List<DoNuevoUsuarioItem> {
        return nuevoUsuarioAdapter.getAllData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
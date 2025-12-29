package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentArticulosConStockBinding
import com.mobile.massiveapp.ui.adapters.ArticuloSeleccionableAdapter
import com.mobile.massiveapp.ui.adapters.ArticulosSeleccionablesAdapter
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticulosConStockFragment : Fragment() {

    private var _binding: FragmentArticulosConStockBinding? = null
    private val binding get() = _binding!!
    private val articuloViewModel: ArticuloViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private lateinit var articuloSeleccionableAdapter: ArticulosSeleccionablesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticulosConStockBinding.inflate(inflater, container, false)

        articuloSeleccionableAdapter = ArticulosSeleccionablesAdapter(emptyList()){ articulo->
            articuloViewModel.saveArticuloSeleccionado(articulo)
        }
        binding.rvSeleccionarArticuloCStock.adapter = articuloSeleccionableAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        articuloViewModel.dataGetArticulosConStock.observe(viewLifecycleOwner){ listaArticulos->
            try {
                articuloSeleccionableAdapter.updateData(listaArticulos)


                providerViewModel.data.observe(viewLifecycleOwner){ newText->
                    val articulosFiltrados = listaArticulos.filter { articulo->
                        articulo.ItemName.contains(newText, ignoreCase = true) ||
                                articulo.ItemCode.contains(newText, ignoreCase = true)
                    }

                    articuloSeleccionableAdapter.updateDataForSearching(articulosFiltrados)
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
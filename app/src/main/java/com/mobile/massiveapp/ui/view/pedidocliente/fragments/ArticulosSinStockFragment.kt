package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentArticulosSinStockBinding
import com.mobile.massiveapp.ui.adapters.ArticuloAdapter
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticulosSinStockFragment : Fragment() {
    private var _binding: FragmentArticulosSinStockBinding? = null
    private val binding get() = _binding!!
    private val articuloViewModel: ArticuloViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private lateinit var articuloAdapter: ArticuloAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticulosSinStockBinding.inflate(inflater, container, false)

        articuloAdapter = ArticuloAdapter(emptyList()){ articulo->
        }
        binding.rvSeleccionarArticuloSinStock.adapter = articuloAdapter

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        
        articuloViewModel.dataGetArticulosSinStock.observe(viewLifecycleOwner){ listaArticulos->
            try {
                articuloAdapter.updateData(listaArticulos)

                providerViewModel.data.observe(viewLifecycleOwner){ newText->
                    val articulosFiltrados = listaArticulos.filter { articulo->
                        articulo.ItemName.contains(newText, ignoreCase = true) ||
                                articulo.ItemCode.contains(newText, ignoreCase = true)
                    }

                    articuloAdapter.updateDataForSearching(articulosFiltrados)
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
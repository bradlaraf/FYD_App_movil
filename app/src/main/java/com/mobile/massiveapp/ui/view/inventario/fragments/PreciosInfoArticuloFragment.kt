package com.mobile.massiveapp.ui.view.inventario.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentPreciosInfoArticuloBinding
import com.mobile.massiveapp.ui.adapters.ArticuloPrecioAdapter
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreciosInfoArticuloFragment : Fragment() {
    private var _binding: FragmentPreciosInfoArticuloBinding? = null
    private val binding get() = _binding!!
    private val articuloViewModel: ArticuloViewModel by activityViewModels()
    private val generalViewModel: GeneralViewModel by activityViewModels()
    private lateinit var articuloPrecioAdapter: ArticuloPrecioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreciosInfoArticuloBinding.inflate(inflater, container, false)
            //Inicializa el adapater
        articuloPrecioAdapter = ArticuloPrecioAdapter(emptyList()){ articuloPrecioSeleccionado->

        }
        binding.rvArticuloInfoPrecios.adapter = articuloPrecioAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        generalViewModel.getAllGeneralMonedas()
        generalViewModel.dataGetAllGeneralMonedas.observe(viewLifecycleOwner) { monedas ->
            try {
                SendData.instance.simboloMoneda = monedas[0].CurrName
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        articuloViewModel.dataGetAllArticuloPreciosPorItemCode.observe(viewLifecycleOwner){ listaArticuloPrecios->
            try {
                articuloPrecioAdapter.updateData(listaArticuloPrecios)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar la informacion del articulo", Toast.LENGTH_SHORT).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PreciosInfoArticuloFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
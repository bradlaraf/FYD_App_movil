package com.mobile.massiveapp.ui.view.facturas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentInfoFacturaDetalleBinding
import com.mobile.massiveapp.ui.adapters.FacturasDetalleAdapter
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFacturaDetalleFragment : Fragment() {
    private var _binding: FragmentInfoFacturaDetalleBinding? = null
    private val binding get() = _binding!!
    private lateinit var facturaDetalleAdapter: FacturasDetalleAdapter
    private val facturasViewModel: SocioFacturasViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoFacturaDetalleBinding.inflate(inflater, container, false
        )
        facturaDetalleAdapter = FacturasDetalleAdapter(emptyList()){

        }
        binding.rvFacturasDetalle.adapter = facturaDetalleAdapter

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        facturasViewModel.dataGetAllFacturasDetalle.observe(viewLifecycleOwner){ listaDetalles->
            facturaDetalleAdapter.updateData(listaDetalles)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFacturaDetalleFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
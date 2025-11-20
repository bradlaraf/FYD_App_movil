package com.mobile.massiveapp.ui.view.facturas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentInfoFacturaLogisticaBinding
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InfoFacturaLogisticaFragment : Fragment() {
    private var _binding: FragmentInfoFacturaLogisticaBinding? = null
    private val binding get() = _binding!!
    private val facturasViewModel: SocioFacturasViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoFacturaLogisticaBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        facturasViewModel.dataGetFacturaPorDocEntry.observe(viewLifecycleOwner){ factura->
            binding.txvDireccionEntregaValue.text = factura.Address2
            binding.txvDireccionFiscalValue.text = factura.Address
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFacturaLogisticaFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
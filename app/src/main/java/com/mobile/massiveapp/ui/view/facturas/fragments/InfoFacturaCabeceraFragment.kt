package com.mobile.massiveapp.ui.view.facturas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentInfoFacturaCabeceraBinding
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InfoFacturaCabeceraFragment : Fragment() {
    private var _binding: FragmentInfoFacturaCabeceraBinding? = null
    private val binding get() = _binding!!
    private val facturasViewModel: SocioFacturasViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoFacturaCabeceraBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        facturasViewModel.dataGetFacturaPorDocEntry.observe(viewLifecycleOwner){ factura->
            binding.txvNumeroDocumentoValue.text = "${factura.FolioPref}-${factura.FolioNum}"
            binding.txvSocioDeNegocioValue.text = factura.CardName
            binding.txvNumeroDocumentoClienteValue.text = factura.LicTradNum
            if (factura.Indicator == "01"){
                binding.txvTipoDeComprobanteValue.text = "Factura"
            } else if (factura.Indicator == "03"){
                binding.txvTipoDeComprobanteValue.text = "Boleta"
            } else {
                binding.txvTipoDeComprobanteValue.text = "Otro"
            }
            binding.txvMonedaValue.text = factura.DocCur
            binding.txvFechaEmisionValue.text = factura.DocDate
            binding.txvFechaVencimientoValue.text = factura.DocDueDate
            binding.txvComentariosValue.text = factura.Comments

            binding.txvPendienteDePagoValue.text = "${SendData.instance.simboloMoneda}${factura.PaidToDate}"
            binding.txvTotalValue.text = "${SendData.instance.simboloMoneda}${factura.DocTotal}"

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFacturaCabeceraFragment().apply {

            }
    }
}
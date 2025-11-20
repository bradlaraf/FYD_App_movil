package com.mobile.massiveapp.ui.view.cobranzas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mobile.massiveapp.databinding.FragmentInfoCobranzaCabeceraBinding
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoCobranzaCabeceraFragment : Fragment() {
    private var _binding: FragmentInfoCobranzaCabeceraBinding? = null
    private val binding get() = _binding!!
    private val cobranzaViewModel: CobranzaViewModel by activityViewModels()
    private val generalViewModel: GeneralViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoCobranzaCabeceraBinding.inflate(inflater, container, false)
        generalViewModel.getAllGeneralVendedores()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            //LiveData cabecera de la cobranza
        cobranzaViewModel.dataGetPagoCabeceraPorAccDocEntry.observe(viewLifecycleOwner){ cobranza->
            try {
                val montoTotal = when (cobranza.TypePayment){
                    "E"->{ cobranza.CashSum }
                    "C"->{ cobranza.CheckSum }
                    "T"->{ cobranza.TrsfrSum }
                    "D"->{ cobranza.TrsfrSum }
                    else->{0.0}
                }
                val tipoPago = when(cobranza.TypePayment){
                    "E"->{ "Efectivo" }
                    "C"->{ "Cheque" }
                    "T"->{ "Transferencia" }
                    "D"->{ "Depósito" }
                    else-> ""
                }

                val cuentaPago = when(cobranza.TypePayment){
                    "E"->{ cobranza.CashAcct }
                    "C"->{ cobranza.CheckAct }
                    "T"->{ cobranza.TrsfrAcct }
                    "D"->{ cobranza.TrsfrAcct }
                    else-> ""
                }

                generalViewModel.dataGetAllGeneralVendedores.observe(viewLifecycleOwner){ vendedores->
                    binding.txvInfoCobranzaEmpleadoDpVentasValue.text = vendedores.filter {
                        it.SlpCode == (cobranza.CounterRef.toIntOrNull() ?: -1)
                    }.firstOrNull()?.SlpName?:"--"
                }

                binding.txvInfoCobranzaNumDocValue.text = cobranza.AccDocEntry
                binding.txvInfoCobranzaGlosaValue.text = cobranza.JrnlMemo
                binding.txvInfoCobranzaClienteValue.text = cobranza.CardName
                binding.txvInfoCobranzaFechaEmisionValue.text = cobranza.DocDate
                binding.txvInfoCobranzaFechaPagoValue.text = cobranza.TaxDate
                binding.txvInfoCobranzaMonedaValue.text = cobranza.DocCurr
                binding.txvInfoCobranzaComentariosValue.text = cobranza.Comments
                binding.txvInfoCobranzaTipoPagoValue.text = tipoPago
                binding.txvInfoCobranzaNumeroCuentaValue.text = cuentaPago
                binding.txvInfoCobranzaMontoTotalValue.text = "${montoTotal.format(2)}"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setEmpleadoDeVentas(counterRef: String):String {
        var empleado = ""



        return empleado
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }













    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoCobranzaCabeceraFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
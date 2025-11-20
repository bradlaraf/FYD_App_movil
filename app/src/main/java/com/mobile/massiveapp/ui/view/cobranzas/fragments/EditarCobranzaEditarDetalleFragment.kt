package com.mobile.massiveapp.ui.view.cobranzas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentEditarCobranzaEditarDetalleBinding
import com.mobile.massiveapp.ui.base.BaseDialogEdt
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.actualizarPagoDetalle
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditarCobranzaEditarDetalleFragment : Fragment() {
    private var _binding: FragmentEditarCobranzaEditarDetalleBinding? = null
    private val binding get() = _binding!!
    private var hashInfo = HashMap<String, Any>()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private val cobranzaViewModel: CobranzaViewModel by activityViewModels()
    private val facturasViewModel: SocioFacturasViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarCobranzaEditarDetalleBinding.inflate(inflater, container, false)

        //Click monto de pago
        binding.clNuevoPagoDetalleMonto.setOnClickListener {
            BaseDialogEdt(
                binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim()
            ){ monto->
                binding.txvNuevoPagoDetalleMontoValue.text = "S/${monto.toDouble()}"
                hashInfo["monto"] = monto.toDouble()
            }.show(parentFragmentManager, "BaseDialogEdt")
        }

        //Click en elegir Pedido
        binding.imvArrowElegirFactura.isVisible = false
        /*binding.clNuevoPagoDetallePedido.setOnClickListener {
            Intent(requireContext(), BuscarFacturaActivity::class.java)
                .putExtra("cardCode", hashInfo["cardCode"].toString())
                .also { startForPedidoSeleccionadoResult.launch(it) }
        }*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            //Datos del Usuario
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(viewLifecycleOwner){ usuario->
            hashInfo["usuarioCode"] = usuario.Code
        }

            //Monto del detalle a editar
        cobranzaViewModel.dataGetCobranzaDetallePorAccDocEntryYLineNum.observe(viewLifecycleOwner){
            try {
                binding.txvNuevoPagoDetalleMontoValue.text = "S/${it.SumApplied}"

                hashInfo["montoActual"] = it.SumApplied
                hashInfo["docLine"] = it.DocLine
                hashInfo["docNum"] = it.DocNum
                hashInfo["fechaCreacion"] = it.AccCreateDate
                hashInfo["horaCreacion"] = it.AccCreateHour
                hashInfo["docEntry"] = it.DocEntry
                hashInfo["accDocEntry"] = it.AccDocEntry
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

            //LiveData factura del Pago Detalle
        facturasViewModel.dataGetFacturaPorDocEntry.observe(viewLifecycleOwner){ factura->
            try {
                binding.txvNuevoPagoDetalleFacturaValue.text = "${factura.FolioPref} - ${factura.FolioNum}"

                hashInfo["docEntry"] = factura.DocEntry
                hashInfo["montoAPagar"] = factura.PaidToDate
                hashInfo["instId"] = factura.InstlmntID
                hashInfo["cardCode"] = factura.CardCode
            } catch (e: Exception){
                e.printStackTrace()
            }
        }



        super.onViewCreated(view, savedInstanceState)
    }




    val startForPedidoSeleccionadoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == AppCompatActivity.RESULT_OK){
            val data = result.data
            val facuturaDocEntry = data?.getIntExtra("docEntry", 0)

            if (facuturaDocEntry != null){
                facturasViewModel.getFacturaPorDocEntry(
                    hashInfo["docEntry"].toString()
                )
            }
        }
    }


    fun savePagoDetalle() {
        if(
            binding.txvNuevoPagoDetalleFacturaValue.text.isNotEmpty() &&
            binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim().toDouble().format(1) > 0.0
        ){

            cobranzaViewModel.saveCobranzaDetalle(
                actualizarPagoDetalle(
                    monto =             binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim().toDouble().format(2),
                    accDocEntry =       SendData.instance.accDocEntryDoc,
                    docLine =           hashInfo["docLine"] as Int,
                    docEntryFactura =   hashInfo["docEntry"] as Int,
                    instId =            hashInfo["instId"] as Int,
                    usuario =           hashInfo["usuarioCode"] as String,
                    accAction =         "U",
                    fechaCreacion =     hashInfo["fechaCreacion"] as String,
                    horaCreacion =      hashInfo["horaCreacion"] as String,
                    fechaEdicion =      getFechaActual(),
                    horaEdicion =       getHoraActual(),
                    docNum =            hashInfo["docNum"] as Int
                )
            )

            facturasViewModel.savePaidToDateToEdit(
                docEntry = hashInfo["docEntry"].toString(),
                paidToDate = getMontoAActualizar()
            )

        } else {
            Toast.makeText(requireContext(), "Seleccione una factura", Toast.LENGTH_SHORT).show()
        }


    }

    fun getMontoAActualizar():Double{
        val result = binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim().toDouble() - hashInfo["montoActual"] as Double
        return result
    }











    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarCobranzaEditarDetalleFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
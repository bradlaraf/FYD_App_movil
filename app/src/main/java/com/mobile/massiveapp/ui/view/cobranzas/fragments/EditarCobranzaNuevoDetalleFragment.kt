package com.mobile.massiveapp.ui.view.cobranzas.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentEditarCobranzaNuevoDetalleBinding
import com.mobile.massiveapp.ui.base.BaseDialogEdt
import com.mobile.massiveapp.ui.view.cobranzas.BuscarFacturaActivity
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.agregarPagoDetalle
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditarCobranzaNuevoDetalleFragment : Fragment() {
    private var _binding: FragmentEditarCobranzaNuevoDetalleBinding? = null
    private val binding get() = _binding!!
    private var hashInfo = HashMap<String, Any>()
    private val cobranzaViewModel: CobranzaViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private val facturasViewModel: SocioFacturasViewModel by activityViewModels()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarCobranzaNuevoDetalleBinding.inflate(inflater, container, false)

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
        binding.clNuevoPagoDetallePedido.setOnClickListener {
            Intent(requireContext(), BuscarFacturaActivity::class.java)
                .putExtra("cardCode", hashInfo["cardCode"].toString())
                .also { startForPedidoSeleccionadoResult.launch(it) }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Datos del Usuario
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(viewLifecycleOwner){ usuario->
            try {
                hashInfo["usuarioCode"] = usuario.Code
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        providerViewModel.docLine.observe(viewLifecycleOwner){ docLine-> hashInfo["docLine"] = docLine }
        providerViewModel.cardCode.observe(viewLifecycleOwner){ cardCode-> hashInfo["cardCode"] = cardCode }

    }







    val startForPedidoSeleccionadoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == AppCompatActivity.RESULT_OK){
            val data = result.data
            val folio = data?.getStringExtra("folio")
            val facuturaDocEntry = data?.getIntExtra("docEntry", 0)
            val montoTotal = data?.getDoubleExtra("docTotal", 0.0)
            val montoAPagar = data?.getDoubleExtra("paidToDate", 0.0)
            val instId = data?.getIntExtra("instId", 0)

            if (facuturaDocEntry != null && montoTotal != null && montoAPagar != null && instId != null){
                binding.txvNuevoPagoDetalleMontoValue.text = "S/$montoAPagar"
                binding.txvNuevoPagoDetalleFacturaValue.text = folio
                hashInfo["docEntry"] = facuturaDocEntry
                hashInfo["montoAPagar"] = montoAPagar
                hashInfo["instId"] = instId
                hashInfo["paidToDateEdicion"] = montoAPagar

            }
        }
    }






    fun savePagoDetalle() {
        if(
            binding.txvNuevoPagoDetalleFacturaValue.text.isNotEmpty() &&
            binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim().toDouble().format(2) > 0.00
        ){

            cobranzaViewModel.saveCobranzaDetalle(
                agregarPagoDetalle(
                    monto =             binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim().toDouble().format(2),
                    accDocEntry =       SendData.instance.accDocEntryDoc,
                    docLine =           hashInfo["docLine"] as Int + 1000,
                    docEntryFactura =   hashInfo["docEntry"] as Int,
                    instId =            hashInfo["instId"] as Int,
                    usuario =           hashInfo["usuarioCode"] as String
                )
            )


            facturasViewModel.savePaidToDateToEdit(
                docEntry = hashInfo["docEntry"].toString(),
                paidToDate = binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim().toDouble()
            )

        } else {
            Toast.makeText(requireContext(), "Seleccione una factura", Toast.LENGTH_SHORT).show()
        }
    }












    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarCobranzaNuevoDetalleFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
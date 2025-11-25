package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentPedidoClienteInfoCabeceraBinding
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioContactoViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PedidoClienteInfoCabeceraFragment : Fragment() {
    private var _binding: FragmentPedidoClienteInfoCabeceraBinding? = null
    private val binding get() = _binding!!
    private val pedidoViewModel: PedidoViewModel by activityViewModels()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private val socioContactosViewModel: SocioContactoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoClienteInfoCabeceraBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.clPedidoClienteNumDoc.isVisible = false
            //LiveData del pedido seleccionado
        pedidoViewModel.dataGetPedidoCabeceraInfo.observe(viewLifecycleOwner){ pedido->
            try {
                binding.txvPedidoCabeceraEmpleadoDpVentasValue.text =       pedido.SlpName
                binding.txvPedidoCabeceraNumDocValue.text =                 pedido.AccDocEntry
                binding.txvPedidoCabeceraInterlocutorComValue.text =        pedido.CardName
                binding.txvPedidoCabeceraMonedaValue.text =                 pedido.DocCur
                binding.txvPedidoCabeceraFechaContabilizacionValue.text =   pedido.DocDate
                binding.txvPedidoCabeceraFechaEntregaValue.text =           pedido.DocDueDate
                binding.txvPedidoCabeceraFechaDocumentoValue.text =         pedido.DocDueDate
                binding.txvPedidoCabeceraTotalValue.text =                  pedido.DocTotal.toString()
                binding.txvPedidoCabeceraTotalAntesDescuentoValue.text =    pedido.DocTotal.toString()
                binding.txvPedidoCabeceraPorcentajeDescuentoValue.text =    "0.00"
                binding.txvPedidoCabeceraDescuentoValue.text =              "0.00"
                binding.txvPedidoCabeceraImpuestoValue.text =               pedido.VatSum.toString()
                binding.txvPedidoCabeceraComentariosValue.text =            pedido.Comments
                binding.txvPedidoCabeceraStatusValue.text =                 if (pedido.DocStatus =="O") "Abierto" else "Cerrado"
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
            //LiveData de los contactos del socio
        socioContactosViewModel.dataGetSocioContactosPorCardCode.observe(viewLifecycleOwner){ listaContactos->
            try {
                binding.txvPedidoCabeceraPersonaContactoValue.text = listaContactos[0].Name

                binding.clPedidoClienteInfoPersonaContacto.setOnClickListener {
                    BaseDialogChecklistWithId(
                        binding.txvPedidoCabeceraPersonaContactoValue.text.toString(),
                        listaContactos.map { it.Name }
                    ){ contactoSeleccionado, id->
                        binding.txvPedidoCabeceraPersonaContactoValue.text = contactoSeleccionado
                    }.show(childFragmentManager, "dialogo")

                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

            //LiveData del usuario
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(viewLifecycleOwner){ usuario->
            try {
                /*binding.txvPedidoCabeceraEmpleadoDpVentasValue.text = usuario.Name*/
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
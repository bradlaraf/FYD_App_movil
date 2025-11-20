package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentPedidoClienteInfoLogisticaBinding
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel

class PedidoClienteInfoLogisticaFragment : Fragment() {
    private var _binding: FragmentPedidoClienteInfoLogisticaBinding? = null
    private val binding get() = _binding!!
    private val pedidoViewModel: PedidoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoClienteInfoLogisticaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            //LiveData del pedido cabecera
        pedidoViewModel.dataGetPedidoPorAccDocEntry.observe(viewLifecycleOwner){ pedido->
            try {
                binding.txvPedidoInfoDstinatarioFactura.text = pedido.Address
                binding.txvPedidoInfoDireccionEntrega.text = pedido.Address2
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PedidoClienteInfoLogisticaFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
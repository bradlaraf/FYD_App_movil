package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentPedidoClienteInfoFinanzasBinding
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel

class PedidoClienteInfoFinanzasFragment : Fragment() {
    private var _binding: FragmentPedidoClienteInfoFinanzasBinding? = null
    private val binding get() = _binding!!
    private val pedidoViewModel: PedidoViewModel by activityViewModels()
    private val generalViewModel: GeneralViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoClienteInfoFinanzasBinding.inflate(inflater, container, false)
        generalViewModel.getAllGeneralIndicadores()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            //LiveData del pedido cabecera
        pedidoViewModel.dataGetPedidoPorAccDocEntry.observe(viewLifecycleOwner){ pedido->
            generalViewModel.dataGetAllGeneralIndicadores.observe(viewLifecycleOwner){ indicadores->
                val indicadorDoc = indicadores.filter {
                    it.Code == pedido.Indicator
                }.firstOrNull()?.Name?:"OTRO"

                binding.txvPedidoInfoIndicadorValue.text = indicadorDoc
            }

            try {
                generalViewModel.getCondicionPorGroupNum(pedido.GroupNum)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        generalViewModel.dataGetCondicionPorGroupNum.observe(viewLifecycleOwner){ condicionDoc->
            binding.txvPedidoInfoCondicionPagoValue.text = condicionDoc.PymntGroup
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
            PedidoClienteInfoFinanzasFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
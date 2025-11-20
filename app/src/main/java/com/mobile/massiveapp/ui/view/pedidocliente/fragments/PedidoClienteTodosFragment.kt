package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentPedidoClienteTodosBinding
import com.mobile.massiveapp.ui.adapters.PedidoClienteAdapter
import com.mobile.massiveapp.ui.view.pedidocliente.PedidoClienteInfoActivity
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PedidoClienteTodosFragment : Fragment() {
    private lateinit var pedidoAdapter: PedidoClienteAdapter
    private var _binding: FragmentPedidoClienteTodosBinding? = null
    private val binding get() = _binding!!
    private var pedidosDeHoy: Boolean = true
    private val pedidoViewModel: PedidoViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoClienteTodosBinding.inflate(inflater, container, false)

            //Inicializacion del adapter
        pedidoAdapter = PedidoClienteAdapter(emptyList()){ pedido->
            Intent(requireContext(), PedidoClienteInfoActivity::class.java)
                .putExtra("accDocEntry", pedido.AccDocEntry)
                .putExtra("cardCode", pedido.CardCode)
                .putExtra("fechaDoc", pedido.DocDate)
                .also { startActivity(it) }

        }
        binding.rvTodosPedidos.adapter = pedidoAdapter


        //Control del ProgressBar
        pedidoViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it
        }

            //SWIPE REFRESH
        binding.swipe.setOnRefreshListener {
            if (pedidosDeHoy){
                pedidoViewModel.getAllPedidosCliente()
                pedidoViewModel.getAllPedidosNoMigrados()
            } else {
                pedidoViewModel.getPedidosDeAyer()
            }
        }

        if (pedidosDeHoy){
            binding.txvFechaPedidos.text = "Pedidos de hoy"
            pedidoViewModel.setPedidosHoy(pedidosDeHoy)
        }

        //Btn click set fecha pedidos
        binding.btnFechaPedidos.setOnClickListener {
            pedidosDeHoy = !pedidosDeHoy
            setTextoFecha(pedidosDeHoy)

            if (pedidosDeHoy){
                pedidoViewModel.getAllPedidosCliente()
            } else {
                pedidoViewModel.getPedidosDeAyer()
            }

        }




            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            //LiveData pedidos de HOY
        pedidoViewModel.dataGetAllPedidosCliente.observe(viewLifecycleOwner){ pedidos->
            try {
                pedidoAdapter.updateData(pedidos.filter { it.CANCELED == "N" })
                binding.swipe.isRefreshing = false

                providerViewModel.data.observe(viewLifecycleOwner){ newText->
                    val pedidosFiltrados = pedidos.filter { pedido-> pedido.CardName.contains(newText, true) && pedido.CANCELED == "N" }
                    pedidoAdapter.updateData(pedidosFiltrados)
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        //LiveData pedidos de AYER
        pedidoViewModel.dataGetPedidosDeAyer.observe(viewLifecycleOwner){ pedidos->
            try {
                pedidoAdapter.updateData(pedidos.filter { it.CANCELED == "N" })
                binding.swipe.isRefreshing = false

                providerViewModel.data.observe(viewLifecycleOwner){ newText->
                    val pedidosFiltrados = pedidos.filter { pedido-> pedido.CardName.contains(newText, true) && pedido.CANCELED == "N" }
                    pedidoAdapter.updateData(pedidosFiltrados)
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        //LiveData set pedidos de hoy
        pedidoViewModel.dataSetPedidosHoy.observe(viewLifecycleOwner){
            if (it){
                pedidosDeHoy = it
                setTextoFecha(true)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private fun setTextoFecha(infoDeHoy: Boolean) {
        if (infoDeHoy){
            binding.txvFechaPedidos.setBackgroundColor(resources.getColor(R.color.color_green_light, null))
            binding.txvFechaPedidos.text = "Pedidos de hoy"
        } else {
            binding.txvFechaPedidos.setBackgroundColor(resources.getColor(R.color.color_red_light, null))
            binding.txvFechaPedidos.text = "Pedidos de ayer"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PedidoClienteTodosFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
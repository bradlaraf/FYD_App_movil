package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentPedidoClienteCanceladosBinding
import com.mobile.massiveapp.ui.adapters.PedidoClienteAdapter
import com.mobile.massiveapp.ui.view.pedidocliente.PedidoClienteInfoActivity
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel

class PedidoClienteCanceladosFragment : Fragment() {
    private lateinit var pedidoAdapter: PedidoClienteAdapter
    private var _binding: FragmentPedidoClienteCanceladosBinding? = null
    private val binding get() = _binding!!
    private val pedidoViewModel: PedidoViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoClienteCanceladosBinding.inflate(inflater, container, false)

        //Inicializacion del adapter
        pedidoAdapter = PedidoClienteAdapter(emptyList()){ pedido->
            providerViewModel.saveAccDocEntry(pedido.AccDocEntry)
            Intent(requireContext(), PedidoClienteInfoActivity::class.java).also {
                it.putExtra("accDocEntry", pedido.AccDocEntry)
                it.putExtra("cardCode", pedido.CardCode)
                startActivity(it)
            }
        }
        binding.rvPedidosCancelados.adapter = pedidoAdapter

        //Control del ProgressBar
        pedidoViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it
        }

        //SWIPE REFRESH
        binding.swipe.setOnRefreshListener {
            pedidoViewModel.getPedidosCancelados()
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //LiveData de todos los pedidos
        pedidoViewModel.dataGetPedidosCancelados.observe(viewLifecycleOwner){ pedidos->
            try {
                binding.swipe.isRefreshing = false

                if (pedidos.isEmpty()){
                    throw Exception("No hay pedidos")
                }

                pedidoAdapter.updateData(pedidos)

                providerViewModel.data.observe(viewLifecycleOwner){ newText->
                    val pedidosFiltrados = pedidos.filter { pedido-> pedido.CardName.contains(newText, true) }
                    pedidoAdapter.updateData(pedidosFiltrados)
                }
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
            PedidoClienteTodosFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentPedidoClientePendientesBinding
import com.mobile.massiveapp.ui.adapters.PedidoClienteAdapter
import com.mobile.massiveapp.ui.view.pedidocliente.NuevoPedidoClienteActivity
import com.mobile.massiveapp.ui.view.pedidocliente.PedidoClienteInfoActivity
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel

class PedidoClientePendientesFragment : Fragment() {
    private lateinit var pedidoAdapter: PedidoClienteAdapter
    private var _binding: FragmentPedidoClientePendientesBinding? = null
    private val binding get() = _binding!!
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private val pedidoViewModel: PedidoViewModel by activityViewModels()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoClientePendientesBinding.inflate(inflater, container, false)
            //Inicializacion del adapter
        pedidoAdapter = PedidoClienteAdapter(emptyList()){ pedidosNoMigrado->
            pedidoViewModel.deleteAllPedidoDetallesParaEditar(pedidosNoMigrado.AccDocEntry)
            pedidoViewModel.dataDeleteAllPedidoDetallesParaEditar.observeOnce(viewLifecycleOwner){
                Intent(requireContext(), PedidoClienteInfoActivity::class.java).also {
                    it.putExtra("accDocEntry", pedidosNoMigrado.AccDocEntry)
                    it.putExtra("cardCode", pedidosNoMigrado.CardCode)
                    startActivity(it)
                }
            }
        }

        binding.rvPedidosNoMigrados.adapter = pedidoAdapter

            //Btn Add
        binding.btnAdd.setOnClickListener {
            pedidoViewModel.deleteAllPedidoDetalleSinCabecera()
            startForNuevoPedidoResult.launch(Intent(
                requireContext(), NuevoPedidoClienteActivity::class.java)
            )
        }

            //Get Usuario
        usuarioViewModel.getUsuarioFromDatabase()

            //Control del ProgressBar
        pedidoViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it
        }

        //SWIPE REFRESH
        binding.swipe.setOnRefreshListener {
            pedidoViewModel.getAllPedidosNoMigrados()
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pedidoViewModel.dataGetAllPedidosNoMigrados.observe(viewLifecycleOwner){ listaPedidosNoMigrados->
            pedidoAdapter.updateData(listaPedidosNoMigrados)
            binding.swipe.isRefreshing = false

                //LiveData SearchView
            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val pedidosFiltrados = listaPedidosNoMigrados.filter { pedido-> pedido.CardName.contains(newText, true) }
                pedidoAdapter.updateData(pedidosFiltrados)
            }
        }

        //LiveData Usuario
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(viewLifecycleOwner){
            if (it.CanCreate == "N"){
                binding.btnAdd.isVisible = false
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    val startForNuevoPedidoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == AppCompatActivity.RESULT_OK){
            pedidoViewModel.setPedidosHoy(true)
            pedidoViewModel.getAllPedidosNoMigrados()
            pedidoViewModel.getAllPedidosCliente()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PedidoClientePendientesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
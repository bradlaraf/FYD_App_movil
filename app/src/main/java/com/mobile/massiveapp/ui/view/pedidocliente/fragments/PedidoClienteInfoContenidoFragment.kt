package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentPedidoClienteInfoContenidoBinding
import com.mobile.massiveapp.ui.adapters.PedidoDetalleAdapter
import com.mobile.massiveapp.ui.base.BaseBottomSheetCustomDialog
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel

class PedidoClienteInfoContenidoFragment : Fragment() {
    private var _binding: FragmentPedidoClienteInfoContenidoBinding? = null
    private val binding get() = _binding!!
    private val pedidoViewModel: PedidoViewModel by activityViewModels()
    private lateinit var pedidoDetalleAdapter: PedidoDetalleAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoClienteInfoContenidoBinding.inflate(inflater, container, false)

            //Inicializacion del adapter
        pedidoDetalleAdapter = PedidoDetalleAdapter(emptyList()){ pedidoSeleccionado->
            pedidoViewModel.getPedidoDetallesInfo(pedidoSeleccionado.AccDocEntry, pedidoSeleccionado.LineNum)
        }
        binding.rvPedidosDetalle.adapter = pedidoDetalleAdapter
            //Manejo del loading
        pedidoViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            //LiveData de todos los pedidos detalle
        pedidoViewModel.dataGetAllPedidoDetallePorAccDocEntry.observe(viewLifecycleOwner){ pedidosDetalle->
            try {
                val listaFiltrada = pedidosDetalle.filter { it.LineNum < 1000 }
                pedidoDetalleAdapter.updateData(listaFiltrada)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        pedidoViewModel.dataGetPedidoDetallesInfo.observe(viewLifecycleOwner){ pedidoSeleccionado->
            BaseBottomSheetCustomDialog(
                R.drawable.icon_detail,
                requireActivity(),
                "Cantidad: ${pedidoSeleccionado.Cantidad}",
                "Unidad : ${pedidoSeleccionado.UnidadMedida}",
            ).showBottomSheetDialog(
                listOf(
                    hashMapOf("Código" to Pair(R.drawable.icon_id, pedidoSeleccionado.ItemCode)),
                    hashMapOf("Descripción" to Pair(R.drawable.icon_description, pedidoSeleccionado.ItemName)),
                    hashMapOf("Unidad de medida" to Pair(R.drawable.icon_box, pedidoSeleccionado.UnidadMedida)),
                    hashMapOf("Almacén" to Pair(R.drawable.icon_almacen, pedidoSeleccionado.Almacen)),
                    hashMapOf("Cantidad" to Pair(R.drawable.icon_number_one, pedidoSeleccionado.Cantidad.toString())),
                    hashMapOf("Precio" to Pair(R.drawable.icon_money, pedidoSeleccionado.Price.toString())),
                    hashMapOf("Total" to Pair(R.drawable.icon_total_factura, pedidoSeleccionado.LineTotal.toString())),
                )
            ){}
        }



        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {

        fun newInstance(param1: String, param2: String) =
            PedidoClienteInfoContenidoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
package com.mobile.massiveapp.ui.view.inventario.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentCantidadesInfoArticuloBinding
import com.mobile.massiveapp.domain.model.DoArticuloInfo
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CantidadesInfoArticuloFragment : Fragment() {
    private var _binding: FragmentCantidadesInfoArticuloBinding? = null
    private val binding get() = _binding!!
    private val articuloViewModel: ArticuloViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCantidadesInfoArticuloBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        articuloViewModel.dataGetArticuloInfo.observe(viewLifecycleOwner) { articuloinfo->
            try {
                binding.txvArticuloInfoAlmacenValue.text = articuloinfo.WhsName
                binding.txvArticuloInfoStockValue.text = articuloinfo.OnHand.toString()
                binding.txvArticuloInfoComprometidoValue.text = articuloinfo.IsCommited.toString()
                binding.txvArticuloInfoSolicitadoValue.text = articuloinfo.OnOrder.toString()
                binding.txvArticuloInfoDisponibleValue.text = "${getDisponible(articuloinfo)}"

                binding.txvArticuloInfoUnidadMedidaValue.text = articuloinfo.UnidadMedida
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar la informacion del articulo", Toast.LENGTH_SHORT).show()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getDisponible(articulo: DoArticuloInfo):Double {
        val disponible = (articulo.OnHand + articulo.OnOrder) - articulo.IsCommited
        return disponible.format(6)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CantidadesInfoArticuloFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
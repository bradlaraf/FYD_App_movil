package com.mobile.massiveapp.ui.view.inventario.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentGeneralInfoArticuloBinding
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralInfoArticuloFragment : Fragment() {
    private var _binding: FragmentGeneralInfoArticuloBinding? = null
    private val binding get() = _binding!!
    private val articuloViewModel: ArticuloViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentGeneralInfoArticuloBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        articuloViewModel.dataGetArticuloInfo.observe(viewLifecycleOwner){ articuloInfo->
            try {
                binding.txvArticuloInfoCodigoValue.text = articuloInfo.ItemCode
                binding.txvArticuloInfoDescripcionValue.text = articuloInfo.ItemName
                binding.txvArticuloInfoFabricanteValue.text = articuloInfo.FirmName
                binding.txvArticuloInfoGrupoArticuloValue.text = articuloInfo.ItmsGrpNam
                binding.txvArticuloInfoGrupoUnidadMedidaValue.text = articuloInfo.UgpName
                binding.txvArticuloInfoUnidadMedidaVentaValue.text = articuloInfo.UnidadMedida
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar la informacion del articulo", Toast.LENGTH_SHORT).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GeneralInfoArticuloFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
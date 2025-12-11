package com.mobile.massiveapp.ui.view.cobranzas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentInfoCobranzaContenidoBinding
import com.mobile.massiveapp.ui.adapters.PagoDetalleAdapter
import com.mobile.massiveapp.ui.base.BaseBottomSheetCustomDialog
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoCobranzaContenidoFragment : Fragment() {
    private var _binding: FragmentInfoCobranzaContenidoBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagoDetalleAdapter: PagoDetalleAdapter
    private val cobranzaViewModel: CobranzaViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoCobranzaContenidoBinding.inflate(inflater, container, false)

            //Set Adapter
        pagoDetalleAdapter = PagoDetalleAdapter(emptyList()){ pagoDetalleSeleccionado->
            BaseBottomSheetCustomDialog(
                R.drawable.icon_bill,
                requireActivity(),
                "Numero Documento: ",
                pagoDetalleSeleccionado.DocEntry.toString()
            ).showBottomSheetDialog(
                listOf(
                    hashMapOf("Fecha" to Pair(R.drawable.icon_calendar ,pagoDetalleSeleccionado.AccCreateDate)),
                    hashMapOf("Monto" to Pair(R.drawable.icon_money, pagoDetalleSeleccionado.SumApplied.toString())),
                )
            ){}
        }
        binding.rvInfoCobranzaContenido.adapter = pagoDetalleAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            //LiveData Pagos Detalle
        cobranzaViewModel.dataGetAllPagoDetalleXAccDocEntry.observe(viewLifecycleOwner){ listaPagosDetalle->
            try {
                pagoDetalleAdapter.updateData(listaPagosDetalle)
            } catch (e: Exception){
                e.printStackTrace()
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
            InfoCobranzaContenidoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
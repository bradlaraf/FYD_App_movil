package com.mobile.massiveapp.ui.view.cobranzas.fragments

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
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentCobranzasAprobadosBinding
import com.mobile.massiveapp.ui.adapters.CobranzasAdapter
import com.mobile.massiveapp.ui.view.cobranzas.InfoCobranzaActivity
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CobranzasAprobadosFragment : Fragment() {
    private var _binding: FragmentCobranzasAprobadosBinding? = null
    private val binding get() = _binding!!
    private val cobranzaViewModel: CobranzaViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private var cobranzasDeHoy: Boolean = true
    private lateinit var cobranzasAdapter: CobranzasAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCobranzasAprobadosBinding.inflate(inflater, container, false)
            //Inicializacion del adapter
        cobranzasAdapter = CobranzasAdapter(emptyList()){ cobranzaSeleccionada->

            cobranzaViewModel.deleteAllPagoDetalleParaEditar(cobranzaSeleccionada.AccDocEntry)
            cobranzaViewModel.dataDeleteAllPagoDetalleParaEditar.observeOnce(viewLifecycleOwner){

                Intent(requireContext(), InfoCobranzaActivity::class.java)
                    .putExtra("accDocEntry", cobranzaSeleccionada.AccDocEntry)
                    .putExtra("fechaDoc", cobranzaSeleccionada.DocDate)
                    .also { startForEdicionCobranzaResult.launch(it) }
            }

        }
        binding.rvCobranzasAprobados.adapter = cobranzasAdapter

        //Control del ProgressBar
        cobranzaViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it
        }

            //Control del swipe refresh
        binding.swipe.setOnRefreshListener {
            if (cobranzasDeHoy){
                cobranzaViewModel.getAllPagosCabeceraAprobados()
            } else {
                cobranzaViewModel.getCobranzasDeAyer()
            }

        }


        setTextoFecha(cobranzasDeHoy)

        //Btn click set fecha pedidos
        binding.btnFechaPedidos.setOnClickListener {
            cobranzasDeHoy = !cobranzasDeHoy
            setTextoFecha(cobranzasDeHoy)

            if (cobranzasDeHoy){
                cobranzaViewModel.getAllPagosCabeceraAprobados()
            } else {
                cobranzaViewModel.getCobranzasDeAyer()
            }

        }


        return binding.root
    }

    private fun setTextoFecha(infoDeHoy: Boolean) {
        if (infoDeHoy){
            binding.txvFechaPedidos.setBackgroundColor(resources.getColor(R.color.color_green_light, null))
            binding.txvFechaPedidos.text = "Cobranzas de hoy"
        } else {
            binding.txvFechaPedidos.setBackgroundColor(resources.getColor(R.color.color_red_light, null))
            binding.txvFechaPedidos.text = "Cobranzas de ayer"
        }
    }

    val startForEdicionCobranzaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            cobranzaViewModel.getAllPagosCabeceraAprobados()
            cobranzaViewModel.getAllPagosCabeceraNoMigrados()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            //LiveData de los pagos de HOY
        cobranzaViewModel.dataGetAllPedidosCabeceraSinDetalle.observe(viewLifecycleOwner){ listaPagos->
            binding.swipe.isRefreshing = false

            try {
                val listaSinCancelados = listaPagos.filter { it.Canceled != "Y" }
                cobranzasAdapter.updateData(listaSinCancelados)
            } catch (e: Exception){
                e.printStackTrace()
            }


            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val cobranzasFiltradas = listaPagos.filter { cobranza-> cobranza.CardName.contains(newText, true)  && cobranza.Canceled == "N"}
                cobranzasAdapter.updateData(cobranzasFiltradas)
            }
        }

        //LiveData de los pagos de AYER
        cobranzaViewModel.dataGetCobranzasDeAyer.observe(viewLifecycleOwner){ listaPagos->
            binding.swipe.isRefreshing = false

            try {
                val listaSinCancelados = listaPagos.filter { it.Canceled != "Y" }
                cobranzasAdapter.updateData(listaSinCancelados)
            } catch (e: Exception){
                e.printStackTrace()
            }



            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val cobranzasFiltradas = listaPagos.filter { cobranza-> cobranza.CardName.contains(newText, true)  && cobranza.Canceled == "N"}
                cobranzasAdapter.updateData(cobranzasFiltradas)
            }
        }

        //LiveData set Cobranzas de hoy
        cobranzaViewModel.dataSetConbranzasHoy.observe(viewLifecycleOwner){
            if (it){
                cobranzasDeHoy = it
                setTextoFecha(true)
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
            CobranzasAprobadosFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
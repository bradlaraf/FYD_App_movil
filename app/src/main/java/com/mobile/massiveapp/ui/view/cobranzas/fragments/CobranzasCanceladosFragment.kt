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
import com.mobile.massiveapp.databinding.FragmentCobranzasCanceladosBinding
import com.mobile.massiveapp.ui.adapters.CobranzasAdapter
import com.mobile.massiveapp.ui.view.cobranzas.InfoCobranzaActivity
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel


class CobranzasCanceladosFragment : Fragment(){
    private lateinit var cobranzasAdapter: CobranzasAdapter
    private val cobranzaViewModel: CobranzaViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private var _binding: FragmentCobranzasCanceladosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCobranzasCanceladosBinding.inflate(inflater, container, false)
        //Inicializacion del adapter
        cobranzasAdapter = CobranzasAdapter(emptyList()){ cobranzaSeleccionada->

            cobranzaViewModel.deleteAllPagoDetalleParaEditar(cobranzaSeleccionada.AccDocEntry)
            cobranzaViewModel.dataDeleteAllPagoDetalleParaEditar.observeOnce(viewLifecycleOwner){
                Intent(requireContext(), InfoCobranzaActivity::class.java)
                    .putExtra("accDocEntry", cobranzaSeleccionada.AccDocEntry)
                    .also { startForEdicionCobranzaResult.launch(it) }
            }

        }
        binding.rvCobranzasCancelados.adapter = cobranzasAdapter

        //Control del ProgressBar
        cobranzaViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it
        }

        //Control del swipe refresh
        binding.swipe.setOnRefreshListener {
            cobranzaViewModel.getCobranzasCanceladas()
        }


        return binding.root
    }

    val startForEdicionCobranzaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            cobranzaViewModel.getCobranzasCanceladas()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //LiveData de los pagos
        cobranzaViewModel.dataGetCobranzasCanceladas.observe(viewLifecycleOwner){ listaPagos->
            binding.swipe.isRefreshing = false
            try {
                cobranzasAdapter.updateData(listaPagos)
            } catch (e: Exception){
                e.printStackTrace()
            }



            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val cobranzasFiltradas = listaPagos.filter { cobranza-> cobranza.CardName.contains(newText, true) }
                cobranzasAdapter.updateData(cobranzasFiltradas)
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
package com.mobile.massiveapp.ui.view.manifiesto.info.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentManifiestoBinding
import com.mobile.massiveapp.ui.adapters.LiquidacionPagoViewAdapter
import com.mobile.massiveapp.ui.view.manifiesto.cobranza.EditarPagoManifiestoActivity
import com.mobile.massiveapp.ui.view.util.showMessage
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManifiestoFragment : Fragment() {
    private var _binding:FragmentManifiestoBinding? = null
    private val binding get() = _binding!!
    private val manifiestoViewModel: ManifiestoViewModel by activityViewModels()
    private lateinit var liquidacionPagoAdapter: LiquidacionPagoViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManifiestoBinding.inflate(inflater, container, false)
        liquidacionPagoAdapter = LiquidacionPagoViewAdapter(emptyList()){ pago->
            if (pago.Canceled != "Y"){
                Intent(requireContext(), EditarPagoManifiestoActivity::class.java)
                    .putExtra("accDocEntry", pago.AccDocEntry)
                    //.putExtra("montoPendienteCobrar", binding.txvPendienteCobrar.text.toString().toDouble())
                    .also { startActivity(it)  }
            }
        }
        binding.rvPagos.adapter = liquidacionPagoAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    manifiestoViewModel.dataGetPagosManifiesto.collect{ listaPagos->
                        liquidacionPagoAdapter.updateData(listaPagos)
                    }
                }
            }
        }
        /*val docEntry = activity?.intent?.getIntExtra("docEntry", 0)?:-1
        manifiestoViewModel.getAllPagosXManifiesto(docEntry)
        manifiestoViewModel.dataGetAllPagosXManifiesto.observe(viewLifecycleOwner){ listaPagos->
            liquidacionPagoAdapter.updateData(listaPagos)
        }*/
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
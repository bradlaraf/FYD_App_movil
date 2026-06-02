package com.mobile.massiveapp.ui.view.manifiesto.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mobile.massiveapp.databinding.FragmentManifiestoSinSaldoBinding
import com.mobile.massiveapp.domain.model.DoManifiestoView
import com.mobile.massiveapp.ui.adapters.ManifiestoAdapter
import com.mobile.massiveapp.ui.view.manifiesto.cobranza.VerCobranzasManifiestoActivity
import com.mobile.massiveapp.ui.view.manifiesto.info.ManifiestoInfoActivity
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManifiestoSinSaldoFragment : Fragment() {
    private var _binding: FragmentManifiestoSinSaldoBinding? = null
    private val binding get() = _binding!!
    private lateinit var manifiestoAdapter: ManifiestoAdapter
    private val manifiestoViewModel: ManifiestoViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private var listaManifiestos: List<DoManifiestoView> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManifiestoSinSaldoBinding.inflate(inflater, container, false)

        manifiestoAdapter = ManifiestoAdapter(listOf(),
            onClickListener = { manifiesto ->
                SendData.instance.docEntry = manifiesto.DocEntry
                Intent(requireContext(), ManifiestoInfoActivity::class.java)
                    .putExtra("docEntry", manifiesto.DocEntry)
                    .also { startActivity(it) }
            },
            onVerPagosClickListener = { manifiesto ->
                Intent(requireContext(), VerCobranzasManifiestoActivity::class.java)
                    .putExtra("docEntry", manifiesto.DocEntry)
                    .also { startActivity(it) }
            }
        )
        binding.rvManifiesto.adapter = manifiestoAdapter

        binding.swipe.setOnRefreshListener { binding.swipe.isRefreshing = false }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                manifiestoViewModel.dataGetManifiestosCancelados.collectLatest { lista ->
                    listaManifiestos = lista
                    manifiestoAdapter.updateData(lista)
                    binding.swipe.isRefreshing = false
                }
            }
        }

        providerViewModel.data.observe(viewLifecycleOwner) { query ->
            val filtrados = listaManifiestos.filter {
                it.DocEntry.toString().contains(query, ignoreCase = true) ||
                it.FechaSalida.contains(query, ignoreCase = true)
            }
            manifiestoAdapter.updateData(filtrados)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

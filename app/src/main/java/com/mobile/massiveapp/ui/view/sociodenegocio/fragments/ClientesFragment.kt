package com.mobile.massiveapp.ui.view.sociodenegocio.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mobile.massiveapp.databinding.FragmentClientesBinding
import com.mobile.massiveapp.ui.adapters.SocioScreenAdapter
import com.mobile.massiveapp.ui.view.sociodenegocio.InfoSocioNegocioActivity
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientesFragment : Fragment() {
    private var _binding: FragmentClientesBinding? = null
    private val binding get() = _binding!!
    private val socioViewModel: SocioViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private lateinit var socioAdapter: SocioScreenAdapter

    private var isLoading = false
    private var isClickable = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientesBinding.inflate(inflater, container, false)

        socioAdapter = SocioScreenAdapter(emptyList()){ socioElegido->
            if (isClickable) {
                isClickable = false
                Intent(requireContext(), InfoSocioNegocioActivity::class.java)
                    .putExtra("cardCode", socioElegido.CardCode)
                    .also { startActivity(it) }
                    .also { isClickable = true }
            }
        }

        binding.rvCLientesMigrados.adapter = socioAdapter

            //Control del ProgressBar
        socioViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it
        }

            //Control del swipe refresh
        binding.swipe.setOnRefreshListener {
            if (!isLoading) {
                isLoading = true
                socioViewModel.getAllSociosToMainScreen()
                socioViewModel.getAllSocioNegocioNoMigrados()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        socioViewModel.dataGetAllSociosForMainScreen.observe(viewLifecycleOwner){ listaSnMigrados->
            try {


                socioAdapter.updateData(listaSnMigrados)
                binding.swipe.isRefreshing = false


                providerViewModel.data.observe(viewLifecycleOwner){ newText->
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(200)
                        val sociosFiltered = withContext(Dispatchers.IO) {
                            listaSnMigrados.filter { socio->
                                val cardNameLimpiado = socio.CardName.trim()
                                    .replace(",", "")
                                    .replace("´", "")
                                    .replace("?", "")
                                    .replace("°", "")
                                cardNameLimpiado.trim().contains(newText.trim(), ignoreCase = true)
                            }
                        }
                        socioAdapter.updateDataForSearching(sociosFiltered)

                    }
                }
            } catch (e: Exception){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } finally {
            isLoading = false
        }
        }

        socioViewModel.dataSearchSociosView.observe(viewLifecycleOwner){
            socioAdapter.updateData(it)
        }

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun newInstance() =
            ClientesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
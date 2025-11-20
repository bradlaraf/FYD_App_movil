package com.mobile.massiveapp.ui.view.sociodenegocio.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mobile.massiveapp.databinding.FragmentLeadBinding
import com.mobile.massiveapp.ui.adapters.SocioAdapter
import com.mobile.massiveapp.ui.view.sociodenegocio.InfoSocioNegocioActivity
import com.mobile.massiveapp.ui.view.sociodenegocio.NuevoSocioNegocioActivity
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeadFragment : Fragment() {
    private var _binding: FragmentLeadBinding? = null
    private val binding get() = _binding!!
    private val socioViewModel: SocioViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var socioAdapter: SocioAdapter
    private var isLoading = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeadBinding.inflate(inflater, container, false)

            //Inicializacion del adapter
        socioAdapter = SocioAdapter(emptyList()){ socioNoMigradosElegido->
            Intent(requireContext(), InfoSocioNegocioActivity::class.java)
                .putExtra("cardCode", socioNoMigradosElegido.CardCode)
                .also { startActivity(it) }
        }
        binding.rvClientesNoMigrados.adapter = socioAdapter


            //Control del swipe refresh
        binding.swipe.setOnRefreshListener {
            if (!isLoading) {
                isLoading = true
                socioViewModel.sendAllLeadsToWService()
            }
        }


        binding.btnAdd.setOnClickListener {
            socioViewModel.deleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNList()
        }


        usuarioViewModel.getUsuarioFromDatabase()
        //Get Usuario


            //Control del progressbar
        socioViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        socioViewModel.dataSendAllLeadsToWService.observe(viewLifecycleOwner){
            socioViewModel.getAllSocioNegocioNoMigrados()
        }

        socioViewModel.dataGetAllSocioNegocioNoMigrados.observe(viewLifecycleOwner){ listaSnNoMigrados->
            try {
                socioAdapter.updateData(listaSnNoMigrados)
                binding.swipe.isRefreshing = false

                providerViewModel.data.observe(viewLifecycleOwner){ newText->
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(200)
                        val sociosFiltered = withContext(Dispatchers.IO) {
                            listaSnNoMigrados.filter { socio->
                                val cardNameLimpiado = socio.CardName.trim()
                                    .replace(",", "")
                                    .replace("´", "")
                                    .replace("?", "")
                                    .replace("°", "")
                                cardNameLimpiado.contains(newText, ignoreCase = true)
                            }
                        }
                        socioAdapter.updateData(sociosFiltered)
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }

        usuarioViewModel.dataGetUsuarioFromDatabase.observe(viewLifecycleOwner){
            if (it.CanCreate == "Y" || it.SuperUser == "Y"){
                binding.btnAdd.isVisible = true
            }
        }


        socioViewModel.dataDeleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNList.observe(viewLifecycleOwner){ success->
            if (success){
                Intent(requireContext(), NuevoSocioNegocioActivity::class.java)
                    . also { startForNewClienteResult.launch(it) }
            }
        }


        super.onViewCreated(view, savedInstanceState)

    }

    val startForNewClienteResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK){
            socioViewModel.getAllSocioNegocioNoMigrados()
            socioViewModel.dataGetAllSocioNegocioNoMigrados.observeOnce(viewLifecycleOwner){ listaSnNoMigrados->
                socioAdapter.updateData(listaSnNoMigrados)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
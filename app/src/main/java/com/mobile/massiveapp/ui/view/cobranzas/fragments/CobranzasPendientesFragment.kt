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
import com.mobile.massiveapp.MassiveApp.Companion.prefs
import com.mobile.massiveapp.databinding.FragmentCobranzasPendientesBinding
import com.mobile.massiveapp.ui.adapters.CobranzasAdapter
import com.mobile.massiveapp.ui.view.cobranzas.InfoCobranzaActivity
import com.mobile.massiveapp.ui.view.cobranzas.NuevaCobranzaActivity
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CobranzasPendientesFragment : Fragment() {
    private lateinit var cobranzasAdapter: CobranzasAdapter
    private var _binding: FragmentCobranzasPendientesBinding? = null
    private val binding get() = _binding!!
    private val cobranzaViewModel: CobranzaViewModel by activityViewModels()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCobranzasPendientesBinding.inflate(inflater, container, false)
            //Inicializacion del adapter
        cobranzasAdapter = CobranzasAdapter(emptyList()){ pagoNoMigradoSeleccionado->
            cobranzaViewModel.deleteAllPagoDetalleParaEditar(pagoNoMigradoSeleccionado.AccDocEntry)
            cobranzaViewModel.dataDeleteAllPagoDetalleParaEditar.observeOnce(viewLifecycleOwner){
                Intent(requireContext(), InfoCobranzaActivity::class.java)
                    .putExtra("accDocEntry", pagoNoMigradoSeleccionado.AccDocEntry)
                    .also { startForEdicionCobranzaResult.launch(it) }
            }

        }
        binding.rvCobranzasPendientes.adapter = cobranzasAdapter

            //Btn Add
        binding.btnAdd.setOnClickListener {
            cobranzaViewModel.deleteAllPagoDetalleSinCabecera()
            /*startForNuevoPedidoResult.launch(
                Intent(requireContext(), NuevaCobranzaActivity::class.java)
            )*/
        }

            //Get Usuario
        usuarioViewModel.getUsuarioFromDatabase()

            //Control del ProgressBar
        cobranzaViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = it
        }

            //Control del swipe refresh
        binding.swipe.setOnRefreshListener {
            cobranzaViewModel.getAllPagosCabeceraNoMigrados()
        }

        return binding.root
    }

    val startForEdicionCobranzaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            cobranzaViewModel.getAllPagosCabeceraAprobados()
            cobranzaViewModel.getAllPagosCabeceraNoMigrados()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            //LiveData de pagos No Migrados
        cobranzaViewModel.dataGetAllPedidosCabeceraNoMigrados.observe(viewLifecycleOwner){ listaPagosNoMigrados->
            cobranzasAdapter.updateData(listaPagosNoMigrados)
            binding.swipe.isRefreshing = false

            providerViewModel.data.observe(viewLifecycleOwner){ newText->
                val cobranzasFiltradas = listaPagosNoMigrados.filter { cobranza-> cobranza.CardName.contains(newText, true) }
                cobranzasAdapter.updateData(cobranzasFiltradas)
            }
        }



            //LiveData Usuario
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(viewLifecycleOwner){
            if (it.CanCreate == "N"){
                binding.btnAdd.visibility = View.GONE
            }
        }


        //LiveData Eliminacion de pagos detalle sin cabecera
        cobranzaViewModel.dataDeleteAllPagoDetalleSinCabecera.observe(viewLifecycleOwner) { deleteSuccsess->
            if (deleteSuccsess){
                prefs.wipe()
                startForNuevaCobranzaResult.launch(Intent(requireContext(), NuevaCobranzaActivity::class.java))
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }


    val startForNuevaCobranzaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            cobranzaViewModel.getAllPagosCabeceraAprobados()
            cobranzaViewModel.getAllPagosCabeceraNoMigrados()
            cobranzaViewModel.setCobranzasHoy(true)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CobranzasPendientesFragment().apply {
                }
    }
}
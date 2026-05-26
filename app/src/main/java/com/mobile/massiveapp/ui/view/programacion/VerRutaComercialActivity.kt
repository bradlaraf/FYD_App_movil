package com.mobile.massiveapp.ui.view.programacion

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mobile.massiveapp.databinding.ActivityVerRutaComercialBinding
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleAdminView
import com.mobile.massiveapp.domain.model.toAdminView
import com.mobile.massiveapp.ui.adapters.RutaComercialAdminAdapter
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.RutaComercialViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerRutaComercialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerRutaComercialBinding
    private val rutaComercialViewModel: RutaComercialViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private lateinit var adminAdapter: RutaComercialAdminAdapter
    private var listaDetalles: List<DoRutaComercialDetalleAdminView> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerRutaComercialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Ver Ruta Comercial"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val accDocEntry = intent.getStringExtra("accDocEntry") ?: ""
        rutaComercialViewModel.cargarRuta(accDocEntry)

        setDefaultUi()
        setData()
    }

    private fun setDefaultUi() {
        adminAdapter = RutaComercialAdminAdapter(emptyList()) { }
        binding.rvVerClientes.adapter = adminAdapter
    }

    private fun setData() {
        generalViewModel.getAllGeneralVendedores()

        rutaComercialViewModel.dataGetRutaComercial.observe(this) { ruta ->
            binding.txvVerFechaRutaValue.text = ruta?.DocDate ?: ""
            binding.txvVerComentariosValue.text = ruta?.Comments ?: ""

            generalViewModel.dataGetAllGeneralVendedores.observeOnce(this) { vendedores ->
                val vendedor = vendedores.firstOrNull { it.SlpCode == (ruta?.SlpCode ?: -1) }
                binding.txvVerVendedorValue.text = vendedor?.SlpName ?: ""
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rutaComercialViewModel.detalleFlow.collectLatest { lista ->
                    listaDetalles = lista.map { it.toAdminView() }
                    adminAdapter.updateData(listaDetalles)
                    binding.txvVerClientesValue.text =
                        "${lista.size} ${if (lista.size == 1) "cliente" else "clientes"}"
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

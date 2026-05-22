package com.mobile.massiveapp.ui.view.programacion.user

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mobile.massiveapp.databinding.ActivityRutaComercialConfirmacionBinding
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.ui.adapters.RutaComercialConfirmacionAdapter
import com.mobile.massiveapp.ui.base.BaseDialogComentarioRuta
import com.mobile.massiveapp.ui.viewmodel.RutaComercialViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RutaComercialConfirmacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRutaComercialConfirmacionBinding
    private val rutaComercialViewModel: RutaComercialViewModel by viewModels()
    private lateinit var confirmacionAdapter: RutaComercialConfirmacionAdapter
    private var listaDetalles: List<DoRutaComercialDetalleView> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRutaComercialConfirmacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Confirmación de Ruta"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val accDocEntry = intent.getStringExtra("accDocEntry") ?: ""
        rutaComercialViewModel.initAccDocEntry(accDocEntry)
        rutaComercialViewModel.cargarRuta(accDocEntry)

        setDefaultUi()
        setData()
    }

    private fun setDefaultUi() {
        confirmacionAdapter = RutaComercialConfirmacionAdapter(emptyList()) { detalle ->
            abrirDialogComentario(detalle)
        }
        binding.rvConfClientes.adapter = confirmacionAdapter
    }

    private fun setData() {
        rutaComercialViewModel.dataGetRutaComercial.observe(this) { ruta ->
            binding.txvConfFechaRutaValue.text = ruta?.FechaRuta ?: ""
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rutaComercialViewModel.detalleFlow.collectLatest { lista ->
                    listaDetalles = lista
                    confirmacionAdapter.updateData(lista)
                }
            }
        }
    }

    private fun abrirDialogComentario(detalle: DoRutaComercialDetalleView) {
        BaseDialogComentarioRuta(
            ""
        ) { comentario ->
            // TODO: guardar comentario
        }.show(supportFragmentManager, "ComentarioRutaDialog")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

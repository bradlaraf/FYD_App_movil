package com.mobile.massiveapp.ui.view.programacion.user

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mobile.massiveapp.databinding.ActivityRutaComercialConfirmacionBinding
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.ui.adapters.RutaComercialConfirmacionAdapter
import com.mobile.massiveapp.ui.base.BaseDialogComentarioRuta
import com.mobile.massiveapp.ui.view.util.obtenerUbicacion
import com.mobile.massiveapp.ui.view.util.showMessage
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRutaComercialConfirmacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Confirmación de Ruta"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val accDocEntry = intent.getStringExtra("accDocEntry") ?: ""
        rutaComercialViewModel.initAccDocEntry(accDocEntry)
        rutaComercialViewModel.cargarRuta(accDocEntry)

        setDefaultUi()
        setData()
    }

    private fun setDefaultUi() {
        (binding.rvConfClientes.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        confirmacionAdapter = RutaComercialConfirmacionAdapter(emptyList()) { detalle ->

            BaseDialogComentarioRuta(
                detalle.Comments
            ) { comentario ->
                obtenerUbicacion(
                    context = this,
                    fusedLocationClient = fusedLocationClient,
                    onResult = { lat, lon ->
                        rutaComercialViewModel.saveConfirmacionRuta(detalle = detalle, comentario = comentario, latitud = lat, longitud = lon)
                    },
                    onError = { mensaje ->
                        showMessage(this, mensaje)
                    }
                )
            }.show(supportFragmentManager, "ComentarioRutaDialog")
        }
        binding.rvConfClientes.adapter = confirmacionAdapter

        binding.bubbleScrollBar.attachToRecyclerView(binding.rvConfClientes)
    }

    private fun setData() {
        rutaComercialViewModel.dataGetRutaComercial.observe(this) { ruta ->
            binding.txvConfFechaRutaValue.text = ruta?.DocDate ?: ""
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rutaComercialViewModel.detalleFlow.collectLatest { lista ->
                    listaDetalles = lista
                    confirmacionAdapter.updateData(lista)
                    if (lista.isNotEmpty()) {
                        binding.rvConfClientes.post {
                            binding.bubbleScrollBar.visibility = View.VISIBLE
                            binding.bubbleScrollBar.attachToRecyclerView(binding.rvConfClientes)
                        }
                    }
                }
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

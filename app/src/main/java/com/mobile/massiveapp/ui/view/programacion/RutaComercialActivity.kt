package com.mobile.massiveapp.ui.view.programacion

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mobile.massiveapp.MassiveApp.Companion.prefsRutaComercial
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityRutaComercialBinding
import com.mobile.massiveapp.domain.model.DoRutaComercialView
import com.mobile.massiveapp.ui.adapters.RutaComercialAdapter
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.programacion.user.RutaComercialConfirmacionActivity
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.view.util.getCodigoDeDocumentoActual
import com.mobile.massiveapp.ui.view.util.mostrarCalendarioRangoMaterial
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.RutaComercialViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RutaComercialActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityRutaComercialBinding
    private val rutaComercialViewModel: RutaComercialViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private lateinit var searchViewHelper: SearchViewHelper
    private lateinit var rutaComercialAdapter: RutaComercialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRutaComercialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDefaultUi()
        setData()
    }

    private fun setData() {
        var listaRutas: List<DoRutaComercialView> = emptyList()
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            binding.btnAdd.isVisible = usuario.SuperUser == "Y"
        }

        rutaComercialViewModel.saveFechasFiltro()

        rutaComercialViewModel.dataSaveFechasFiltro.observe(this){
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    rutaComercialViewModel.dataGetAllRutas.collect { lista ->
                        listaRutas = lista
                        rutaComercialAdapter.updateData(lista)
                    }
                }
            }
        }

        providerViewModel.data.observe(this) { newText ->
            val filtradas = listaRutas.filter { ruta ->
                ruta.NombreVendedor.contains(newText, true) ||
                ruta.FechaRuta.contains(newText, true)
            }
            rutaComercialAdapter.updateData(filtradas)
        }
    }

    private fun setDefaultUi() {
        rutaComercialAdapter = RutaComercialAdapter(emptyList()) { ruta ->
            prefsRutaComercial.saveAccDocEntry(ruta.AccDocEntry)
            usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->

                if (usuario.SuperUser == "Y"){
                    Intent(this, EditarRutaComercialActivity::class.java)
                        .putExtra("accDocEntry", ruta.AccDocEntry)
                        .also { startActivity(it) }
                } else {
                    Intent(this, RutaComercialConfirmacionActivity::class.java)
                        .putExtra("accDocEntry", ruta.AccDocEntry)
                        .also { startActivity(it) }
                }
            }

        }
        binding.rvRutaComercial.adapter = rutaComercialAdapter
        prefsRutaComercial
        binding.swipe.setOnRefreshListener { binding.swipe.isRefreshing = false }
        binding.btnAdd.setOnClickListener {
            prefsRutaComercial.saveAccDocEntry(getCodigoDeDocumentoActual(this))
            Intent(this, NuevaRutaComercialActivity::class.java)
                .also { startActivity(it) }
        }
        //Fecha Rutas
        binding.btnFechaRutas.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mostrarCalendarioRangoMaterial(
                    this,
                    prefsRutaComercial.getFechaInicio(),
                    prefsRutaComercial.getFechaFin()
                ) { diaI, mesI, yearI, diaF, mesF, yearF ->
                    prefsRutaComercial.saveFechaInicio("$yearI-$mesI-$diaI")
                    prefsRutaComercial.saveFechaFin("$yearF-$mesF-$diaF")
                    rutaComercialViewModel.saveFechasFiltro()
                }
            }
        }
    }






    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu.findItem(R.id.app_bar_add)?.isVisible = false
        menu.findItem(R.id.app_bar_expand)?.isVisible = false


        searchViewHelper = SearchViewHelper(menu, "Buscar Ruta...", { newText->
            providerViewModel.saveData(newText)
        },{textSubmit-> })
        searchViewHelper.setOnDismiss {}

        return true
    }
}

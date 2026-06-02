package com.mobile.massiveapp.ui.view.manifiesto

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import com.mobile.massiveapp.MassiveApp.Companion.prefsManifiesto
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityManiefiestoBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMManifiestoListaAdapter
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.view.util.mostrarCalendarioRangoMaterial
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManifiestoActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityManiefiestoBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var searchViewHelper: SearchViewHelper
    private val manifiestoViewModel: ManifiestoViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by viewModels()
    private val tabTitle = arrayOf("Con Saldo", "Sin Saldo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManiefiestoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setDefaultUi()
    }

    private fun setDefaultUi() {
        val pager = binding.viewPagerManifiesto
        val tl = binding.tabLayoutManifiesto
        pager.adapter = FMManifiestoListaAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        binding.btnFechaManifiesto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mostrarCalendarioRangoMaterial(
                    this,
                    prefsManifiesto.getFechaInicio(),
                    prefsManifiesto.getFechaFin()
                ) { diaI, mesI, yearI, diaF, mesF, yearF ->
                    prefsManifiesto.saveFechaInicio("$yearI-$mesI-$diaI")
                    prefsManifiesto.saveFechaFin("$yearF-$mesF-$diaF")
                    manifiestoViewModel.actualizarFechas()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            Toast.makeText(this, "Encienda la ubicación", Toast.LENGTH_SHORT).show()
            return
        }

        if (!fineLocationGranted && !coarseLocationGranted) {
            Toast.makeText(this, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Toast.makeText(this, "Lat: ${location.latitude}\nLon: ${location.longitude}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_add)?.isVisible = false

        searchViewHelper = SearchViewHelper(menu, "Buscar Manifiesto", { newText ->
            providerViewModel.saveData(newText)
        }, {})
        return super.onCreateOptionsMenu(menu)
    }
}

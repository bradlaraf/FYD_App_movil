package com.mobile.massiveapp.ui.view.manifiesto

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mobile.massiveapp.databinding.ActivityManiefiestoBinding
import com.mobile.massiveapp.ui.adapters.ManifiestoAdapter
import com.mobile.massiveapp.ui.view.facturas.FacturasActivity
import com.mobile.massiveapp.ui.view.manifiesto.info.ManifiestoInfoActivity
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManifiestoActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityManiefiestoBinding
    private lateinit var manifiestoAdapter: ManifiestoAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val manifiestoViewModel: ManifiestoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManiefiestoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Ubicacion
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setDefaultUi()
        setValoresIniciales()

    }

    private fun setDefaultUi() {
        manifiestoAdapter = ManifiestoAdapter(listOf(),
            onClickListener = {manifiesto->
                Intent(this, ManifiestoInfoActivity::class.java)
                    .putExtra("coductor", manifiesto.U_MSV_MA_CON)
                    .putExtra("vehiculo", manifiesto.U_MSV_MA_TRANSPNO)
                    .also { startActivity(it)  }},
            onLongPressListener = {view, manifiesto->})

        binding.rvManifiesto.adapter = manifiestoAdapter

        binding.btnTest.setOnClickListener {
            getCurrentLocation()
        }
    }

    private fun setValoresIniciales() {
        manifiestoViewModel.getAllManifiestos()
        manifiestoViewModel.dataGetAllManifiestos.observe(this){ listaManifiestos->
            try {
                manifiestoAdapter.updateData(listaManifiestos)
                binding.swipe.isRefreshing = false
            } catch (e: Exception){
                e.printStackTrace()

            }
        }
        binding.swipe.setOnRefreshListener {
            manifiestoViewModel.getAllManifiestos()
        }
    }

    @SuppressLint("MissingPermission") // Lo usamos porque ya pedimos permisos antes
    private fun getCurrentLocation() {

        // Primero verifica permisos
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = this.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

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

        // Obtener la ubicación actual
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {

                val lat = location.latitude
                val lon = location.longitude

                Toast.makeText(
                    this,
                    "Lat: $lat\nLon: $lon",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
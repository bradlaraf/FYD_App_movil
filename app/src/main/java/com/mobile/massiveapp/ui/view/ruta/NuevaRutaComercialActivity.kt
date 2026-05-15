package com.mobile.massiveapp.ui.view.ruta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.massiveapp.databinding.ActivityNuevaRutaComercialBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NuevaRutaComercialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevaRutaComercialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaRutaComercialBinding.inflate(layoutInflater)
        title = "Nueva Ruta Comercial"
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

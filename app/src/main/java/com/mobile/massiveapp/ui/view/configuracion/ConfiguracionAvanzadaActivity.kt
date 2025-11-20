package com.mobile.massiveapp.ui.view.configuracion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.massiveapp.databinding.ActivityConfiguracionAvanzadaBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfiguracionAvanzadaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracionAvanzadaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionAvanzadaBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
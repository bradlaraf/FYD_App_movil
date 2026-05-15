package com.mobile.massiveapp.ui.view.ruta

import android.content.Intent
import android.os.Bundle
import com.mobile.massiveapp.databinding.ActivityRutaComercialBinding
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RutaComercialActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityRutaComercialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRutaComercialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDefaultUi()
    }

    private fun setDefaultUi() {
        binding.fabNuevaRuta.setOnClickListener {
            Intent(this, NuevaRutaComercialActivity::class.java).also { startActivity(it) }
        }
    }
}

package com.mobile.massiveapp.ui.view.manifiesto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.databinding.ActivityManiefiestoBinding
import com.mobile.massiveapp.ui.adapters.ManifiestoAdapter
import com.mobile.massiveapp.ui.view.facturas.FacturasActivity
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel

class ManifiestoActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityManiefiestoBinding
    private lateinit var manifiestoAdapter: ManifiestoAdapter
    private val manifiestoViewModel: ManifiestoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManiefiestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDefaultUi()
        setValoresIniciales()

    }

    private fun setDefaultUi() {
        manifiestoAdapter = ManifiestoAdapter(listOf(),
            onClickListener = {manifiesto->
                Intent(this, FacturasActivity::class.java)
                    .putExtra("coductor", manifiesto.Conductor)
                    .putExtra("vehiculo", manifiesto.Vehiculo)
                    .also { startActivity(it)  }},
            onLongPressListener = {view, manifiesto->})

        binding.rvManifiesto.adapter = manifiestoAdapter
    }

    private fun setValoresIniciales() {
        manifiestoViewModel.getAllManifiestos()
        manifiestoViewModel.dataGetAllManifiestos.observe(this){ listaManifiestos->
            try {
                manifiestoAdapter.updateData(listaManifiestos)
                binding.swipe.isRefreshing = false
            } catch (e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        binding.swipe.setOnRefreshListener {
            manifiestoViewModel.getAllManifiestos()
        }
    }
}
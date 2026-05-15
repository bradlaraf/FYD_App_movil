package com.mobile.massiveapp.ui.view.manifiesto.cobranza

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity

import com.mobile.massiveapp.databinding.ActivityVerCobranzasManifiestoBinding
import com.mobile.massiveapp.ui.adapters.LiquidacionPagoAdapter
import com.mobile.massiveapp.ui.adapters.LiquidacionPagoViewAdapter
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerCobranzasManifiestoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerCobranzasManifiestoBinding
    private val manifiestoViewModel: ManifiestoViewModel by viewModels()
    private lateinit var liquidacionPagoAdapter: LiquidacionPagoViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerCobranzasManifiestoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        setDefaultUI()
        setDefaultInfo()
    }

    private fun setDefaultUI() {
        liquidacionPagoAdapter = LiquidacionPagoViewAdapter(emptyList()){ pago->

        }
        binding.rvPagos.adapter = liquidacionPagoAdapter
    }


    private fun setDefaultInfo() {
        val docEntry = intent.getIntExtra("docEntry", 0)
        manifiestoViewModel.getInfoManifiesto(docEntry)
        manifiestoViewModel.dataGetInfoManifiesto.observe(this){ infoManifiestoDocumento ->
            binding.txvNumeroManifiestoValue.text = infoManifiestoDocumento.DocEntry.toString()
            binding.txvFechaSalidaManifiestoValue.text = infoManifiestoDocumento.FechaSalida
            binding.txvMonedaManifiestoValue.text = infoManifiestoDocumento.Moneda
            binding.txvMontoPendienteManifiestoValue.text = infoManifiestoDocumento.MontoPendiente.toString()
            binding.txvMontoCobradoManifiestoValue.text = infoManifiestoDocumento.MontoCobrado.toString()

        }

        manifiestoViewModel.getAllPagosXManifiesto(docEntry)
        manifiestoViewModel.dataGetAllPagosXManifiesto.observe(this){ listaPagos->
            liquidacionPagoAdapter.updateData(listaPagos)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
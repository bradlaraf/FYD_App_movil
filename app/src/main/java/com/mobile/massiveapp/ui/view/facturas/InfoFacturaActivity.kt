package com.mobile.massiveapp.ui.view.facturas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.databinding.ActivityInfoFacturaBinding
import com.mobile.massiveapp.ui.adapters.fragment.FmFacturaInfoAdapter
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFacturaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoFacturaBinding
    private var tabTitle = arrayOf("Cabecera","Detalle" ,"Logística")
    private val generalViewModel: GeneralViewModel by viewModels()
    private val facturasViewModel: SocioFacturasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoFacturaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val pager = binding.viewPagerFacturas
        val tl = binding.tabLayoutFacturas
        pager.adapter = FmFacturaInfoAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        generalViewModel.getAllGeneralMonedas()
        generalViewModel.dataGetAllGeneralMonedas.observe(this) { monedas ->
            try {
                SendData.instance.simboloMoneda = monedas[0].CurrName
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        facturasViewModel.getFacturaPorDocEntry(
            intent.getIntExtra("docEntry", 0).toString()
        )

        facturasViewModel.getAllFacturaDetallePorDocEntry(
            intent.getIntExtra("docEntry", 0)
        )
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
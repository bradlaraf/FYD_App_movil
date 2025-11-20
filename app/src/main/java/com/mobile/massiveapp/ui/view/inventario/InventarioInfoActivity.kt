package com.mobile.massiveapp.ui.view.inventario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.databinding.ActivityInventarioInfoBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMArticuloInfoAdapter
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InventarioInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventarioInfoBinding
    private val articuloViewModel: ArticuloViewModel by viewModels()
    private var tabTitle = arrayOf("General", "Cantidades", "Precios")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventarioInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getAllArticuloInfo()
        setTabLayout()
    }






    private fun getAllArticuloInfo() {
        try {

            val itemCode = intent.getStringExtra("itemCode").toString()

            articuloViewModel.getArticuloInfo(itemCode)
            articuloViewModel.getAllArticuloPreciosPorItemCode(itemCode)
        } catch (e: Exception){
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            Timber.d("Error: ${e.message}")
        }
    }




    private fun setTabLayout() {
        try {
            val pager = binding.viewPager2
            val tl = binding.tabLayoutArticulos
            pager.adapter = FMArticuloInfoAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tl, pager){ tab, position ->
                tab.text = tabTitle[position]
            }.attach()
        } catch (e: Exception){
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            Timber.d("Error: ${e.message}")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
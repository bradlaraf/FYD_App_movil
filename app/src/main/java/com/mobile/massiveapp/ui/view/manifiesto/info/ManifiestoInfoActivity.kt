package com.mobile.massiveapp.ui.view.manifiesto.info

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityManifiestoInfoBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMManifiestoAdapter
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ManifiestoInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManifiestoInfoBinding
    private lateinit var searchViewHelper: SearchViewHelper
    private val manifiestoViewModel: ManifiestoViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by viewModels()
    private var tabTitle = arrayOf("Facturas", "Pagos")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManifiestoInfoBinding.inflate(layoutInflater)
        title  = "Manifiesto #" + intent.getIntExtra("docEntry", -1).toString()
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTabLayout()
        setDefaultUi()
        setContent()
    }

    private fun setTabLayout() {
        try {
            val pager = binding.viewPagerConductor
            val tl = binding.tabLayoutManifiesto
            pager.adapter = FMManifiestoAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tl, pager){ tab, position ->
                tab.text = tabTitle[position]
            }.attach()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun setContent() {
        manifiestoViewModel.getAllManifiestoDocumento(intent.getIntExtra("docEntry", -1))
    }

    private fun setDefaultUi() {

    }





    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Se sete la buscqueda de items
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_add)?.isVisible = false

        searchViewHelper = SearchViewHelper(menu, "Buscar factura",{ newText->
            providerViewModel.saveData(newText)
        },{textSubmit->})
        searchViewHelper.setOnDismiss {}

        return super.onCreateOptionsMenu(menu)
    }
}
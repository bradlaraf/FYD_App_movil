package com.mobile.massiveapp.ui.view.sociodenegocio

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivitySocioNegocioBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMClientesAdapter
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class SocioNegocioActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivitySocioNegocioBinding
    private lateinit var searchViewHelper: SearchViewHelper
    private val socioViewModel: SocioViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var tabTitle = arrayOf("Clientes", "Leads")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocioNegocioBinding.inflate(layoutInflater)
        setContentView(binding.root)


            //Get All SocioNegocio
        socioViewModel.getAllSociosToMainScreen()
        socioViewModel.getAllSocioNegocioNoMigrados()


            //Set TABLAYOUT
        val pager = binding.viewPagerClientes
        val tl = binding.tabLayoutClientes
        pager.adapter = FMClientesAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()


    }















    /*------------------------BARRA DE TITULO - NAV-------------------------------*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_add)?.isVisible = false


        searchViewHelper = SearchViewHelper(menu, "Buscar cliente", { newText ->
            providerViewModel.saveData(newText.trim())
        },{textSubmit-> })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.app_bar_add -> {
                socioViewModel.deleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNList()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
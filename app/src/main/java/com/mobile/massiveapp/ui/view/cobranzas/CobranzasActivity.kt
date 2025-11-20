package com.mobile.massiveapp.ui.view.cobranzas

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityCobranzasBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMCobranzasAdapter
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CobranzasActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityCobranzasBinding
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by viewModels()
    private lateinit var searchViewHelper: SearchViewHelper
    private var tabTitle = arrayOf("Aprobados", "Pendientes", "Cancelados")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCobranzasBinding.inflate(layoutInflater)
        setContentView(binding.root)



            //Seteo del boton de navegacion entre fragments
        cobranzaViewModel.getAllPagosCabeceraNoMigrados()
        cobranzaViewModel.getAllPagosCabeceraAprobados()
        cobranzaViewModel.getCobranzasCanceladas()

        val pager = binding.viewPagerCobranzas
        val tl = binding.tabLayoutCobranzas
        binding.viewPagerCobranzas.adapter = FMCobranzasAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        cobranzaViewModel.dataGetAllPedidosCabeceraSinDetalle.observe(this){
            pager.setCurrentItem(0, true)
        }


    }




    val startForNuevoPedidoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            cobranzaViewModel.getAllPagosCabeceraAprobados()
            cobranzaViewModel.getAllPagosCabeceraNoMigrados()
        }
    }










    /*--------------NAVBAR------------*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_add)?.isVisible = false

        searchViewHelper = SearchViewHelper(menu, "Buscar Cobranza",{ newText->
            providerViewModel.saveData(newText)
        },{textSubmit->})

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_add -> {
                cobranzaViewModel.deleteAllPagoDetalleSinCabecera()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
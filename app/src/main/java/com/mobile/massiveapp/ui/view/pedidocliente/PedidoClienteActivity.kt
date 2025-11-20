package com.mobile.massiveapp.ui.view.pedidocliente

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.mobile.massiveapp.MassiveApp.Companion.prefsPedido
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityPedidoClienteBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMPedidosClienteAdapter
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.viewmodel.ConfiguracionViewModel
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PedidoClienteActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityPedidoClienteBinding
    private lateinit var searchViewHelper: SearchViewHelper
    private val providerViewModel: ProviderViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()
    private val configuracionViewModel: ConfiguracionViewModel by viewModels()
    private var pedidosDeHoy: Boolean = true
    private var tabTitle = arrayOf("Aprobados", "Pendientes","Cancelados")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDefaultUi()
        setValoresIniciales()


    }

    private fun setValoresIniciales() {
        configuracionViewModel.getConfiguracionActual()
        configuracionViewModel.dataGetConfiguracionActual.observe(this){ configuracion->
            prefsPedido.saveMaximoLineas(configuracion.LimiteLineasPedido)
        }

        //Get All Pedidos
        pedidoViewModel.getAllPedidosCliente()
        pedidoViewModel.getAllPedidosNoMigrados()
        pedidoViewModel.getPedidosCancelados()

    }

    private fun setDefaultUi() {
        //Seteo del boton de navegacion entre fragments
        val pager = binding.viewPagerPedidos
        val tl = binding.tabLayoutPedidos
        pager.adapter = FMPedidosClienteAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        pedidoViewModel.dataGetAllPedidosCliente.observe(this){
            pager.setCurrentItem(0, true)
        }
    }







    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)

        //Get Usuario
        menu?.findItem(R.id.app_bar_add)?.isVisible = false

        searchViewHelper = SearchViewHelper(menu, "Buscar pedido",{ newText->
            providerViewModel.saveData(newText)
        },{textSubmit->})
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_add -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
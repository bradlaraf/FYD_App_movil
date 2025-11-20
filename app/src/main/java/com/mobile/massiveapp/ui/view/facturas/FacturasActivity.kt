package com.mobile.massiveapp.ui.view.facturas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityFacturasBinding
import com.mobile.massiveapp.domain.model.DoFacturaView
import com.mobile.massiveapp.ui.adapters.FacturasAdapter
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.base.BaseSimpleInformativeDialog
import com.mobile.massiveapp.ui.view.cobranzas.NuevaCobranzaActivity
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FacturasActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityFacturasBinding
    private lateinit var searchViewHelper: SearchViewHelper
    private lateinit var facturasAdapter: FacturasAdapter
    private val generalViewModel: GeneralViewModel by viewModels()
    private val socioFacturasViewModel: SocioFacturasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacturasBinding.inflate(layoutInflater)
        setContentView(binding.root)


            //ADAPTER
        facturasAdapter = FacturasAdapter(listOf(),
            onClickListener = { articulo->
            Intent(this, InfoFacturaActivity::class.java)
                .putExtra("docEntry", articulo.DocEntry)
                .putExtra("cardCode", articulo.CardCode)
                .also { startActivity(it)  }},
            onLongPressListener = { view, factura ->
                showPopupMenu(view, factura)
            })

        binding.rvFacturas.adapter = facturasAdapter

            //Set Simbolo Moneda
        generalViewModel.getAllGeneralMonedas()
        generalViewModel.dataGetAllGeneralMonedas.observe(this) { monedas ->
            try {
                SendData.instance.simboloMoneda = monedas[0].CurrName
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

            //Pogressbar
        val loadingDialog = BaseDialogSImpleLoading(this)
        socioFacturasViewModel.isLoading.observe(this){
            if (it){
                loadingDialog.startLoading()
            }else {
                loadingDialog.onDismiss()
            }
        }

            //GET ALL FACTURAS
        socioFacturasViewModel.getallFacturas()
        socioFacturasViewModel.dataGetAllFacturas.observe(this){ listaFacturas->
            try {
                if (listaFacturas.isEmpty()){
                    throw Exception("No hay facturas")
                }
                facturasAdapter.updateData(listaFacturas)
                binding.swipe.isRefreshing = false
            } catch (e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.swipe.setOnRefreshListener {
            socioFacturasViewModel.getallFacturas()
        }
    }





    private fun showPopupMenu(view: View, factura: DoFacturaView) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_facturas_editar_cobrar, popupMenu.menu)

        popupMenu.menu?.findItem(R.id.nav_item_editar_socio)?.isVisible = false



        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.nav_item_info_factura -> {
                    Intent(this, InfoFacturaActivity::class.java)
                        .putExtra("docEntry", factura.DocEntry)
                        .putExtra("cardCode", factura.CardCode)
                        .also { startActivity(it) }
                    true
                }

                R.id.nav_item_cobrar_factura ->{
                    if (factura.PaidToDate <= 0.0){
                        BaseSimpleInformativeDialog(
                            titulo = "Factura Pagada",
                            mensaje ="Esta factura ya no tiene deuda"
                        ).show(supportFragmentManager, "mensaje")
                    } else {
                        Intent(this, NuevaCobranzaActivity::class.java)
                            .putExtra("docEntry", factura.DocEntry)
                            .putExtra("cardCode", factura.CardCode)
                            .also { startActivity(it) }
                    }
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }


















    /**************************NAVBAR*****************************/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_add)?.isVisible = false

        searchViewHelper = SearchViewHelper(menu, "Buscar Factura",{ newText->
            socioFacturasViewModel.dataGetAllFacturas.observe(this){ listaFacturas->
                try {
                    val facturasFiltradas = listaFacturas.filter { factura-> factura.CardName.contains(newText, true) }
                    facturasAdapter.updateData(facturasFiltradas)
                } catch (e: Exception){
                    throw e
                }
            }
        },{textSubmit->})
        return super.onCreateOptionsMenu(menu)
    }
}
package com.mobile.massiveapp.ui.view.pedidocliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityBuscarClienteBinding
import com.mobile.massiveapp.domain.model.DoClienteScreen
import com.mobile.massiveapp.domain.model.DoSocioNuevoAwait
import com.mobile.massiveapp.ui.adapters.SocioSeleccionablePedidoAdapter
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class BuscarClienteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuscarClienteBinding
    private lateinit var socioAdapter: SocioSeleccionablePedidoAdapter
    private lateinit var searchViewHelper: SearchViewHelper
    private var setListNum: Boolean = false
    private val socioViewModel: SocioViewModel by viewModels()
    private var cardCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

            //Control del ProgressBar
        socioViewModel.isLoading.observe(this){
            binding.progressBar.isVisible = it
        }


        if (intent.getBooleanExtra("clienteParaPago", false)){
            socioViewModel.getAllSocioNegocioConFacturas()
            title = "Buscar socio de negocio"

        } else {
            setListNum = true
            socioViewModel.getAllSN()
        }



            //Se inicializa el adapter
        socioAdapter = SocioSeleccionablePedidoAdapter(emptyList(), setListNum) { socio->
            selectedItem(socio)
        }
        binding.rvSocioNegocioBuscar.adapter = socioAdapter



        //LiveData de todos los Socios
        socioViewModel.dataGetAllSN.observe(this) { currentSocios ->
            try {
                socioAdapter.updateData(currentSocios)
            } catch (e: Exception){
                throw Exception("Error al actualizar los datos del adapter")
            }
        }



        //LiveData Socios con Facturas
        socioViewModel.dataGetAllSocioNegocioConFacturas.observe(this){ listaSociosConPedidos->
            try {
                socioAdapter.updateData(listaSociosConPedidos)
            } catch (e: Exception){
                throw Exception("Error al actualizar los datos del adapter")
            }
        }

        socioViewModel.dataSearchSociosView.observe(this){ sociosBusqueda->
            socioAdapter.updateData(sociosBusqueda)
        }


    }















    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (cardCode.isNotEmpty()){
            setResult(RESULT_OK, Intent()
                .putExtra("cardCode", cardCode)
            )
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Debe seleccionar un cliente", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)


        searchViewHelper = SearchViewHelper(menu, "Buscar Cliente",{ newTextChange->

            socioViewModel.dataGetAllSN.observe(this) { listaSocios ->
                lifecycleScope.launch {
                    delay(200)
                    val sociosFiltrados = listaSocios.filter { socio->

                        val cardNameLimpiado = socio.CardName.trim()
                            .replace(",", "")
                            .replace("´", "")
                            .replace("?", "")
                            .replace("°", "")

                        cardNameLimpiado.contains(newTextChange, ignoreCase = true) ||
                                socio.LicTradNum.contains(newTextChange, ignoreCase = true)
                    }
                    socioAdapter.updateDataForSearching(sociosFiltrados)
                }
            }

            socioViewModel.dataGetAllSocioNegocioConFacturas.observe(this){ listaSociosConPedidos->
                lifecycleScope.launch {
                    val sociosFiltrados = listaSociosConPedidos.filter { socio->
                        socio.CardName.lowercase().contains(newTextChange) ||
                        socio.LicTradNum.lowercase().contains(newTextChange)
                    }
                    socioAdapter.updateDataForSearching(sociosFiltrados)
                }
            }
        }, {textSubmit-> })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> {
            }

                //Se le cambió el icono a check, pero el id es el mismo
            R.id.app_bar_add -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

        //Cuando se selecciona un cliente
    private fun selectedItem(onItemSelected: Any) {
        when (onItemSelected) {
            is DoClienteScreen -> {
                cardCode = onItemSelected.CardCode
            }

            is DoSocioNuevoAwait -> {
                cardCode = onItemSelected.CardCode
            }
        }
    }
}


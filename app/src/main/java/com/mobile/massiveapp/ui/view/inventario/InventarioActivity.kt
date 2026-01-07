package com.mobile.massiveapp.ui.view.inventario

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.ui.adapters.ArticuloAdapter
import com.mobile.massiveapp.databinding.ActivityInventarioBinding
import com.mobile.massiveapp.ui.adapters.ArticuloInventarioFYDAdapter
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventarioActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityInventarioBinding
    private lateinit var articuloAdapter: ArticuloInventarioFYDAdapter
    private lateinit var searchViewHelper: SearchViewHelper
    private val articuloViewModel: ArticuloViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allocateActivityTitle("Inventario")
        binding = ActivityInventarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swipe.setOnRefreshListener {
            articuloViewModel.getAllArticulosSm()
        }

            //Se inicializa el adapter
        articuloAdapter = ArticuloInventarioFYDAdapter(emptyList()){ articulo->

            Intent(this, InventarioInfoActivity::class.java).
            apply {
                putExtra("itemCode", articulo.ItemCode)
                startActivity(this)
            }
        }
        binding.rvArticulos.adapter = articuloAdapter

            //Control del ProgressBar
        val loadingDialog = BaseDialogSImpleLoading(this)
        articuloViewModel.isLoading.observe(this)  {
            if (it){
                loadingDialog.startLoading()
            } else{
                loadingDialog.onDismiss()
            }
        }


            //Se traen todos los articulos
        articuloViewModel.getArticulosConStock()
        articuloViewModel.dataGetArticulosConStock.observe(this) { listaArticulos ->
            try {
                binding.swipe.isRefreshing = false

                if ( listaArticulos.isNotEmpty() ){
                    articuloAdapter.updateData(listaArticulos)

                } else {
                    Toast.makeText(this, "No hay articulos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception){
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }










    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
            //Se sete la buscqueda de items
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_add)?.isVisible = false

        searchViewHelper = SearchViewHelper(menu, "Buscar Articulo", { newText->
            articuloViewModel.dataGetArticulosConStock.observe(this) { listaArticulos ->

                val articulosFiltrados = listaArticulos.filter { articulo->
                    articulo.ItemName.contains(newText, ignoreCase = true) ||
                            articulo.ItemCode.contains(newText, ignoreCase = true)
                }
                articuloAdapter.updateDataForSearching(articulosFiltrados)

            }
        },{textSubmit-> })
        searchViewHelper.setOnDismiss {}

        return super.onCreateOptionsMenu(menu)
    }

}
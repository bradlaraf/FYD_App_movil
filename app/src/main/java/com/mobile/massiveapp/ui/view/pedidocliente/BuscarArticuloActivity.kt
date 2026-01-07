package com.mobile.massiveapp.ui.view.pedidocliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityBuscarArticuloBinding
import com.mobile.massiveapp.domain.model.DoArticuloInventario
import com.mobile.massiveapp.ui.adapters.ArticuloSeleccionableAdapter
import com.mobile.massiveapp.ui.base.BaseBottomSheetCustomDialog
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuscarArticuloActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarArticuloBinding
    private lateinit var articuloSeleccionableAdapter: ArticuloSeleccionableAdapter
    private lateinit var searchViewHelper: SearchViewHelper
    private val articuloViewModel: ArticuloViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var articuloSeleccionado: DoArticuloInventario? = null
    private var codigoAlmacen = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            codigoAlmacen = usuario.DefaultWarehouse
        }

            //Se controla el ProgressBar
        val loadingDialog = BaseDialogSImpleLoading(this)
        articuloViewModel.isLoading.observe(this){
            if (it){
                loadingDialog.startLoading()
            } else {
                loadingDialog.onDismiss()
            }
        }

            //Se inicializa el adapter
        articuloSeleccionableAdapter = ArticuloSeleccionableAdapter(emptyList()){ articulo->
            articuloSeleccionado = articulo
        }
        binding.rvInventarioBuscar.adapter = articuloSeleccionableAdapter

            //Se traen los articulos
        articuloViewModel.getAllArticulosSm()
        articuloViewModel.dataGetAllArticulosSm.observe(this){ listaArticulos->
            try {
                articuloSeleccionableAdapter.updateData(listaArticulos)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }



        articuloViewModel.dataGetArticuloCantidadesPorItemCodeYWhsCode.observe(this){ cantidadesArticulo->
            articuloViewModel.dataGetAllArticuloPreciosPorItemCode.observeOnce(this){ listaPrecios->
                try {
                    var lista: List<HashMap<String, Pair<Int, String>>> = emptyList()
                    if (listaPrecios.isNotEmpty()){
                        lista = listOf(
                            hashMapOf("Ver Imagen" to Pair(R.drawable.icon_image, "Ver imagen")),
                            hashMapOf("Descripción" to Pair(R.drawable.icon_description, articuloSeleccionado!!.ItemName)),
                            hashMapOf("Stock" to Pair(R.drawable.icon_inventario, cantidadesArticulo.OnHand.format(2).toString())),
                            hashMapOf("Comprometido" to Pair(R.drawable.icon_comprometido, cantidadesArticulo.OnHand.format(2).toString())),
                            hashMapOf("Solicitado" to Pair(R.drawable.icon_solicitado, cantidadesArticulo.OnOrder.toString())),
                            hashMapOf("Disponible" to Pair(R.drawable.icon_disponible, "${cantidadesArticulo.OnHand - cantidadesArticulo.IsCommited + cantidadesArticulo.OnOrder}")),
                            hashMapOf(listaPrecios[0].ListName to Pair(R.drawable.icon_number_one, listaPrecios[0].Price.toString())),
                        )

                    } else {
                        lista = listOf(
                            hashMapOf("Ver Imagen" to Pair(R.drawable.icon_image, "Ver imagen")),
                            hashMapOf("Descripción" to Pair(R.drawable.icon_description, articuloSeleccionado!!.ItemName)),
                            hashMapOf("Stock" to Pair(R.drawable.icon_inventario, cantidadesArticulo.OnHand.format(2).toString())),
                            hashMapOf("Comprometido" to Pair(R.drawable.icon_comprometido, cantidadesArticulo.OnHand.format(2).toString())),
                            hashMapOf("Solicitado" to Pair(R.drawable.icon_solicitado, cantidadesArticulo.OnOrder.toString())),
                            hashMapOf("Disponible" to Pair(R.drawable.icon_disponible, "${cantidadesArticulo.OnHand - cantidadesArticulo.IsCommited + cantidadesArticulo.OnOrder}")))
                    }
                    mostrarBottomDialog(lista)
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

    }


    fun mostrarBottomDialog(listaElementos: List<HashMap<String, Pair<Int, String>>>){
        BaseBottomSheetCustomDialog(
            R.drawable.icon_inventario,
            this,
            "Código: ",
            articuloSeleccionado!!.ItemCode
        ).showBottomSheetDialog(
            listaElementos
        ){ texto ->
            Toast.makeText(this, "Selección", Toast.LENGTH_SHORT).show()
        }
    }










    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (articuloSeleccionado != null){
            setResult(RESULT_OK, Intent().putExtra("itemCodeArticulo", articuloSeleccionado?.ItemCode))
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Debe seleccionar un articulo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_expand)?.isVisible = true

        searchViewHelper = SearchViewHelper(menu, "Buscar Articulo",{ newText->
            articuloViewModel.dataGetAllArticulosSm.observe(this) { listaArticulos ->

                val articulosFiltrados = listaArticulos.filter { articulo->
                    articulo.ItemName.contains(newText, ignoreCase = true) ||
                            articulo.ItemCode.contains(newText, ignoreCase = true)
                }

                articuloSeleccionableAdapter.updateDataForSearching(articulosFiltrados)}
        },{textSubmit-> })
        searchViewHelper.setOnDismiss{}

        //Se cambia el icono de add por el de check
        menu?.findItem(R.id.app_bar_add)?.setIcon(R.drawable.icon_check)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_add -> {
                onBackPressed()
            }

            R.id.app_bar_search -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.app_bar_expand -> {
                if (articuloSeleccionado != null){
                    articuloViewModel.getArticuloCantidadesPorItemCodeYWhsCode(articuloSeleccionado!!.ItemCode, codigoAlmacen)
                } else {
                    Toast.makeText(this, "Debe seleccionar un articulo", Toast.LENGTH_SHORT).show()
                }

            }

        }
        return super.onOptionsItemSelected(item)
    }
}
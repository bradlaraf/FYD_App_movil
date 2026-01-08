package com.mobile.massiveapp.ui.view.pedidocliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivitySeleccionarArticuloBinding
import com.mobile.massiveapp.domain.model.DoArticuloInventario
import com.mobile.massiveapp.ui.adapters.fragment.FMSeleccionarArticuloAdapter
import com.mobile.massiveapp.ui.base.BaseBottomSheetCustomDialog
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.mobile.massiveapp.MassiveApp.Companion.prefsPedido
import com.mobile.massiveapp.databinding.FragmentArticulosConStockBinding
import com.mobile.massiveapp.domain.model.DoArticuloInv
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.ArticulosConStockFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeleccionarArticuloActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeleccionarArticuloBinding
    private lateinit var searchViewHelper: SearchViewHelper

    private val providerViewModel: ProviderViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val articuloViewModel: ArticuloViewModel by viewModels()
    private var articuloSeleccionado: DoArticuloInv? = null
    private var codigoAlmacen = ""

    private val tabTitle = arrayOf("Con Stock", "Sin Stock")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ArticulosConStockFragment())
                .commit()
        }

        setValoresIniciales()
        setDefaultUi()


        //LiveData de InfoArticulo
        articuloViewModel.dataGetArticuloInfoBaseView.observe(this) { infoArticuloView ->
            try {
                var lista: List<HashMap<String, Pair<Int, String>>> = emptyList()

                lista = listOf(
                    hashMapOf("Descripción" to Pair(R.drawable.icon_description, infoArticuloView.Descripcion)),
                    hashMapOf("Stock" to Pair(R.drawable.icon_inventario, infoArticuloView.Stock.toString())),
                    hashMapOf("Comprometido" to Pair(R.drawable.icon_comprometido, infoArticuloView.Comprometido.format(6).toString())),
                    hashMapOf("Solicitado" to Pair(R.drawable.icon_solicitado, infoArticuloView.Solicitado.toString())),
                    hashMapOf("Disponible" to Pair(R.drawable.icon_disponible, infoArticuloView.Disponible.toString())),
                    hashMapOf("Precio unitario" to Pair(R.drawable.icon_number_one, infoArticuloView.Precio.toString())),
                )
                mostrarBottomDialog(lista)
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
    }



    private fun setValoresIniciales() {
        //Almacen por Default
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            codigoAlmacen = usuario.DefaultWarehouse
        }

        //Articulos

        articuloViewModel.getArticulosConStock(prefsPedido.getCardCode())
        articuloViewModel.getArticulosSinStock()


        //Articulo seleccionado
        articuloViewModel.articuloSeleccionado.observe(this){ articuloSeleccionado->
            this.articuloSeleccionado = articuloSeleccionado

        }


    }

    private fun setDefaultUi() {

        //TabLayout
        /*val pager = binding.viewPagerSeleccionarArticulo
        val tl = binding.tabLayoutSeleccionarArticulo
        pager.adapter = FMSeleccionarArticuloAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()*/

    }


    fun mostrarBottomDialog(listaElementos: List<HashMap<String, Pair<Int, String>>>){
        BaseBottomSheetCustomDialog(
            R.drawable.icon_inventario,
            this,
            "Código: ",
            articuloSeleccionado!!.ItemCode
        ).showBottomSheetDialog(
            listaElementos
        ){}
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_expand)?.isVisible = true

        searchViewHelper = SearchViewHelper(menu, "Buscar Articulo",{ newText->
            providerViewModel.saveData(newText)
        },{textSubmit-> })
        searchViewHelper.setOnDismiss{}

        //Se cambia el icono de add por el de check
        menu?.findItem(R.id.app_bar_add)?.setIcon(R.drawable.icon_check)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_add -> {
                if (articuloSeleccionado != null){
                    setResult(
                        RESULT_OK, Intent()
                            .putExtra("itemCodeArticulo", articuloSeleccionado!!.ItemCode))
                    onBackPressedDispatcher.onBackPressed()
                } else {
                    Toast.makeText(this, "Debe seleccionar un articulo", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.app_bar_search -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.app_bar_expand -> {
                if (articuloSeleccionado != null){
                    articuloViewModel.getArticuloInfoBaseView(itemCode = articuloSeleccionado?.ItemCode?:"")
                } else {
                    Toast.makeText(this, "Debe seleccionar un articulo", Toast.LENGTH_SHORT).show()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
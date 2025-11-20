package com.mobile.massiveapp.ui.view.pedidocliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityEditarPedidoDetalleBinding
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.PedidoActualizarDetalleModificarFragment
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.PedidoActualizarDetalleNuevoFragment
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditarPedidoDetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPedidoDetalleBinding
    private val articuloViewModel: ArticuloViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by viewModels()
    private var accDocEntry: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPedidoDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        accDocEntry = intent.getStringExtra("accDocEntry").toString()

        usuarioViewModel.getUsuarioFromDatabase()
        generalViewModel.getImpuestoDefault()
        articuloViewModel.getAllArticuloAlmacenes()
        articuloViewModel.getAllArticuloListaPrecios()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            if (intent.getBooleanExtra("editMode", false)){
                title = "Modificar Artículo"
                providerViewModel.saveData(intent.getStringExtra("itemCode").toString())
                replace<PedidoActualizarDetalleModificarFragment>(R.id.fcActualizarPedidoDetalle)
            } else {
                title = "Agregar Artículo"
                providerViewModel.saveData(intent.getStringExtra("itemCode").toString())
                replace<PedidoActualizarDetalleNuevoFragment>(R.id.fcActualizarPedidoDetalle)
            }
        }

        pedidoViewModel.getUnPedidoDetallePorAccDocEntryYLineNum(
            intent.getStringExtra("accDocEntry").toString(),
            intent.getIntExtra("lineNum", -1)
        )

        providerViewModel.itemCode.observe(this){ itemCodePedido->
            try {
                articuloViewModel.getArticuloPedidoInfo(
                    itemCodePedido
                )
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

    }








    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("articuloAgregado", true)
        intent.putExtra("accDocEntry",accDocEntry)
        intent.putExtra("editMode", true)
        setResult(RESULT_OK, intent)

        super.onBackPressed()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo_sn, menu)

        //Se oculta el icono que indica si hay conexión o no
        val item = menu?.findItem(R.id.app_bar_connectivity_status)
        item?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_check -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.fcActualizarPedidoDetalle)
                try {
                    if (fragment is PedidoActualizarDetalleModificarFragment){
                        pedidoViewModel.savePedidoDetalle(
                            fragment.savePedidoDetalle(
                                accDocEntry = intent.getStringExtra("accDocEntry").toString(),
                                lineNum = intent.getIntExtra("lineNum", -1),
                            )
                        )
                        pedidoViewModel.dataSavePedidoDetalle.observe(this){
                            if (it){
                                onBackPressed()
                            }
                        }
                    } else if (fragment is PedidoActualizarDetalleNuevoFragment){
                        pedidoViewModel.savePedidoDetalle(
                            fragment.savePedidoDetalle(
                                accDocEntry = intent.getStringExtra("accDocEntry").toString(),
                                lineNum = intent.getIntExtra("lineNum", -1) + 1000
                            )
                        )
                        pedidoViewModel.dataSavePedidoDetalle.observe(this){ if (it){ onBackPressed() } }
                    }
                } catch (e: Exception){
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
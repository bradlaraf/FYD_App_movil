package com.mobile.massiveapp.ui.view.cobranzas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityEditarCobranzaContenidoBinding
import com.mobile.massiveapp.ui.adapters.PagoDetalleAdapter
import com.mobile.massiveapp.ui.adapters.extension.SwipeToDeletePedidos
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditarCobranzaContenidoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarCobranzaContenidoBinding
    private lateinit var pagoDetalleAdapter: PagoDetalleAdapter
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private var hashInfo = HashMap<String, Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarCobranzaContenidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //Inicializacion del adapter
        pagoDetalleAdapter = PagoDetalleAdapter(emptyList()){ pedidoDetalleSeleccionado->
            /*Intent(this, EditarCobranzaDetalleActivity::class.java)
                .putExtra("editMode", true)
                .putExtra("docLine", pedidoDetalleSeleccionado.DocLine)
                .also { startForNuevoPagoDetalleResult.launch(it) }*/
            Intent(this, NuevoPagoDetalleActivity::class.java)
                .putExtra("editMode", true)
                .putExtra("edicion", true)
                .putExtra("docLine", pedidoDetalleSeleccionado.DocLine)
                .putExtra("docEntry", pedidoDetalleSeleccionado.DocEntry)
                .putExtra("cardCode", intent.getStringExtra("cardCode"))
                .also { startForNuevoPagoDetalleResult.launch(it) }
        }
        binding.rvEditarCobranzaContenidoDetalle.adapter = pagoDetalleAdapter


        //Se obtiene el DOCLINE actual
        cobranzaViewModel.getCurrentDocLine(SendData.instance.accDocEntryDoc)
        cobranzaViewModel.dataGetCurrentDocLine.observe(this){ docLine->
            try {
                SendData.instance.docLine = docLine
            } catch (e: Exception){
                e.printStackTrace()
            }
        }


        /** FLOW de Pedidos Detalle **/
        lifecycleScope.launch {
            cobranzaViewModel.dataAllPagoDetalleFLow.collect{ listaPagosDetalle->
                try {
                    val listaPagosAEditar = listaPagosDetalle.filter { it.DocLine >= 1000 }
                    pagoDetalleAdapter.updateData(listaPagosAEditar)
                    val total = listaPagosAEditar.sumOf { it.SumApplied }.format(2)
                    binding.txvContenidoPagoDetalleMontoPagoValue.text = "${SendData.instance.simboloMoneda} $total"
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        //Se obtienen todos los PAGOS DETALLE
        /*cobranzaViewModel.getAllPagosDetalleParaEditar(SendData.instance.accDocEntryDoc)
        cobranzaViewModel.dataGetAllPagosDetalleParaEditar.observe(this){ listaPagosDetalle->
            try {
                hashInfo["listaPagosDetalle"] = listaPagosDetalle
                hashInfo["docLine"] = listaPagosDetalle.size

                pagoDetalleAdapter.updateData(listaPagosDetalle)

                binding.txvContenidoPagoDetalleMontoPagoValue.text = listaPagosDetalle.sumOf { it.SumApplied }.toString()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }*/

        /**SWIPE TO REFRESH**/
        val swipeToRefreshCallback = object :SwipeToDeletePedidos(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition

                cobranzaViewModel.deleteUnPagoDetallePorAccDocEntryYDocLine(SendData.instance.accDocEntryDoc, position + 1000)
                cobranzaViewModel.dataDeleteUnPagoDetallePorAccDocEntryYDocLine.observe(this@EditarCobranzaContenidoActivity){ success->
                    val rv = binding.rvEditarCobranzaContenidoDetalle
                    rv.adapter?.notifyItemRemoved(position)

                    if (success){
                        cobranzaViewModel.getAllPagosDetalleParaEditar(SendData.instance.accDocEntryDoc)
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToRefreshCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvEditarCobranzaContenidoDetalle)



        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }











    val startForNuevoPagoDetalleResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == RESULT_OK){
            cobranzaViewModel.getAllPagosDetalleParaEditar(SendData.instance.accDocEntryDoc)
        }

    }









    /*---------------------------NAVBAR---------------------------*/

    override fun onSupportNavigateUp(): Boolean {
        setResult(RESULT_CANCELED)
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    /*override fun onBackPressed() {
        setResult(RESULT_OK, Intent()
            .putExtra("montoPago", binding.txvContenidoPagoDetalleMontoPagoValue.text.toString())
            .putExtra("cantidadPagos", pagoDetalleAdapter.itemCount)
        )
        super.onBackPressed()
    }*/



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contenido_articulos, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                setResult(RESULT_CANCELED)
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.app_bar_add -> {
                /*Intent(this, EditarCobranzaDetalleActivity::class.java)
                    .putExtra("cardCode", intent.getStringExtra("cardCode"))
                    .putExtra("docLine", hashInfo["docLine"] as Int)
                    .also { startForNuevoPagoDetalleResult.launch(it) }*/
                Intent(this, BuscarFacturaActivity::class.java)
                    .putExtra("cardCode", intent.getStringExtra("cardCode"))
                    .putExtra("edicion", true)
                    .putExtra("editMode", true)
                    .putExtra("docLine", pagoDetalleAdapter.itemCount + 1000)
                    .also { startForNuevoPagoDetalleResult.launch(it) }
            }



            R.id.app_bar_check -> {
                if(pagoDetalleAdapter.itemCount > 0){
                    setResult(RESULT_OK)
                    onBackPressedDispatcher.onBackPressed()
                } else {
                    Toast.makeText(this, "Debe agregar un pago", Toast.LENGTH_SHORT).show()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }
}







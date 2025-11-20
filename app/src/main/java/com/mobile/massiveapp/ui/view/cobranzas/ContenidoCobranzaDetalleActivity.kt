package com.mobile.massiveapp.ui.view.cobranzas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityContenidoCobranzaDetalleBinding
import com.mobile.massiveapp.ui.adapters.PagoDetalleAdapter
import com.mobile.massiveapp.ui.adapters.extension.SwipeToDeletePedidos
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContenidoCobranzaDetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContenidoCobranzaDetalleBinding
    private lateinit var pagoDetalleAdapter: PagoDetalleAdapter
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private var hashInfo = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContenidoCobranzaDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


            //Inicializacion del adapter
        pagoDetalleAdapter = PagoDetalleAdapter(emptyList()){ pedidoDetalleSeleccionado->
            Intent(this, NuevoPagoDetalleActivity::class.java)
                .putExtra("editMode", true)
                .putExtra("docLine", pedidoDetalleSeleccionado.DocLine)
                .putExtra("docEntry", pedidoDetalleSeleccionado.DocEntry)
                .putExtra("cardCode", intent.getStringExtra("cardCode"))
                .also { startForNuevoPagoDetalleResult.launch(it) }
        }
        binding.rvPagoDetalle.adapter = pagoDetalleAdapter

            //Se obtiene el DOCLINE actual
        lifecycleScope.launch {
            cobranzaViewModel.dataDoclineActual.collect { docline->
                try {
                    SendData.instance.docLine = docline
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }


        /******Cambio de model******/

        lifecycleScope.launch {
            cobranzaViewModel.dataAllPagoDetalleFLow.collect{ listaPagosDetalle->
                try {
                    pagoDetalleAdapter.updateData(listaPagosDetalle)
                    val total = listaPagosDetalle.sumOf { it.SumApplied }.format(2)
                    binding.txvContenidoPagoDetalleMontoPagoValue.text = "${SendData.instance.simboloMoneda} $total"
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }


        /**SWIPE TO DELETE**/
        val swipeToDeleteCallback = object :SwipeToDeletePedidos(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition

                cobranzaViewModel.deleteUnPagoDetallePorAccDocEntryYDocLine(SendData.instance.accDocEntryDoc, position)
                cobranzaViewModel.dataDeleteUnPagoDetallePorAccDocEntryYDocLine.observeOnce(this@ContenidoCobranzaDetalleActivity){ deleteSuccess->
                    val rv = binding.rvPagoDetalle
                    rv.adapter?.notifyItemRemoved(position)

                    if(deleteSuccess){
                        cobranzaViewModel.getAllPagoDetalleXAccDocEntry(SendData.instance.accDocEntryDoc)
                    }
                }
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvPagoDetalle)
        /*******************/

    }

    val startForNuevoPagoDetalleResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == RESULT_OK){
            val data = result.data
            val docEntry = data?.getStringExtra("docEntry")
            if (docEntry != null){
                cobranzaViewModel.getAllPagoDetalleXAccDocEntry(SendData.instance.accDocEntryDoc)
                hashInfo["docEntry"] = docEntry
            }
        }
    }






    /*---------------------------NAVBAR---------------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        setResult(RESULT_OK, Intent()
            .putExtra("montoPago", binding.txvContenidoPagoDetalleMontoPagoValue.text.toString())
            .putExtra("cantidadPagos", pagoDetalleAdapter.itemCount)
        )
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contenido_articulos, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.app_bar_add -> {
                Intent(this, BuscarFacturaActivity::class.java)
                    .putExtra("cardCode", intent.getStringExtra("cardCode"))
                    .also { startForNuevoPagoDetalleResult.launch(it) }
            }


            R.id.app_bar_delete -> {
                cobranzaViewModel.dataGetAllPagosDetallePorAccDocEntry.observeOnce(this){ listaPagosDetalle->
                    try {
                        BaseDialogChecklistWithId(
                            listaPagosDetalle.map { it.SumApplied.toString() }
                        ){ cobranzaSeleccionada, id ->
                            cobranzaViewModel.deleteUnPagoDetallePorAccDocEntryYDocLine(SendData.instance.accDocEntryDoc, id)
                        }.show(supportFragmentManager, "show")

                        //LiveData de la eliminacion del detalle pago
                        cobranzaViewModel.dataDeleteUnPagoDetallePorAccDocEntryYDocLine.observeOnce(this){ deleteSuccess->
                            if(deleteSuccess){
                                cobranzaViewModel.getAllPagosDetallePorAccDocEntry(SendData.instance.accDocEntryDoc)
                            }
                        }
                    } catch (e: Exception){
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }


            R.id.app_bar_check -> {
                if(pagoDetalleAdapter.itemCount > 0){
                    onBackPressed()
                } else {
                    Toast.makeText(this, "Debe agregar un pago", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        cobranzaViewModel.getCurrentDocLine(SendData.instance.accDocEntryDoc)
        cobranzaViewModel.dataGetCurrentDocLine.observe(this){ docLine->
            try {
                SendData.instance.docLine = docLine
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}

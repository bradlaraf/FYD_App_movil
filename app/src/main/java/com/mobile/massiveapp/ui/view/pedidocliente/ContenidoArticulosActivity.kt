package com.mobile.massiveapp.ui.view.pedidocliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.MassiveApp.Companion.prefsPedido
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.databinding.ActivityContenidoArticulosBinding
import com.mobile.massiveapp.ui.adapters.PedidoDetalleAdapter
import com.mobile.massiveapp.ui.adapters.extension.SwipeToDeletePedidos
import com.mobile.massiveapp.ui.base.BaseDialogAceptDialog
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.formatString
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContenidoArticulosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContenidoArticulosBinding
    private lateinit var pedidoDetalleItemsAdapter: PedidoDetalleAdapter
    private val pedidoViewModel: PedidoViewModel by viewModels()
    private var accDocEntry = ""
    private var gTotal = 0.0
    private var listaPedidosAEliminar: List<ClientePedidoDetalle> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContenidoArticulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        accDocEntry = intent.getStringExtra("accDocEntry").toString()

            //Inicializar adapter
        pedidoDetalleItemsAdapter = PedidoDetalleAdapter(emptyList()){ articuloAgregado->
            try {
                if (intent.getBooleanExtra("editMode", false)){
                    Intent(this, NuevoPedidoArticuloInfoActivity::class.java)
                        .putExtra("accDocEntry", accDocEntry)
                        .putExtra("lineNum", articuloAgregado.LineNum)
                        .putExtra("edicionDetalle", true)
                        .also { startForUpdateEditItemResult.launch(it) }

                } else {
                    Intent(this, NuevoPedidoArticuloInfoActivity::class.java)
                        .putExtra("accDocEntry", accDocEntry)
                        .putExtra("lineNum", articuloAgregado.LineNum)
                        .putExtra("edicionDetalle", true)
                        .also { startForEditItemResult.launch(it) }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.rvNpArticulosAgregados.adapter = pedidoDetalleItemsAdapter

        /**SWIPE TO DELETE**/
        val swipeToDeleteCallback = object : SwipeToDeletePedidos(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition

                if (intent.getBooleanExtra("editMode", false)){
                    pedidoViewModel.deleteUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry, position + 1000)
                } else {
                    pedidoViewModel.deleteUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry, position)
                }

                    //LiveData del detalle eliminado
                pedidoViewModel.dataDeleteUnPedidoDetallePorAccDocEntryYLineNum.observe(this@ContenidoArticulosActivity){ succsess->
                    val rv = binding.rvNpArticulosAgregados
                    rv.adapter?.notifyDataSetChanged()

                    /*if (succsess){
                        if (intent.getBooleanExtra("editMode", false)) {
                            pedidoViewModel.getAllPedidoDetallesParaEditar(accDocEntry)
                        } else {
                            pedidoViewModel.getAllPedidoDetallePorAccDocEntry(accDocEntry)
                        }
                    }*/
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvNpArticulosAgregados)







            //Obtener el detalle del pedido por accDocEntry
        if (accDocEntry.isNotEmpty() && !intent.getBooleanExtra("editMode", false)){
            pedidoViewModel.getAllPedidoDetallePorAccDocEntry(accDocEntry)
        } else if(accDocEntry.isNotEmpty() && intent.getBooleanExtra("editMode", false)){
            pedidoViewModel.getAllPedidoDetallesParaEditar(accDocEntry)
        }


        if (intent.getBooleanExtra("editMode", false)){
            lifecycleScope.launch {
                pedidoViewModel.dataAllPedidoDetalleParaEditar.collect{ listaDetalles->
                    setPedidosInfo(listaDetalles, true)
                }
            }
        } else {
            lifecycleScope.launch {
                pedidoViewModel.dataAllPedidosDetalle.collect{ listaDetalles->
                    setPedidosInfo(listaDetalles, false)
                }
            }
        }



            //LiveData del detalle pedido por el accDocEntry
        /*pedidoViewModel.dataGetAllPedidoDetallePorAccDocEntry.observe(this){ listaPedidoDetalle->
            try {
                if (listaPedidoDetalle.isNotEmpty()){
                    binding.txvCantidadPedidos.text = "${listaPedidoDetalle.size} articulos"
                    binding.txvCantidadPedidos.isVisible = true
                }

                if (listaPedidoDetalle.size > 3){
                    binding.rvNpArticulosAgregados.layoutParams.height = 500
                }

                //Actualizar Contenido Articulos
                val total = listaPedidoDetalle.sumOf { pedido -> pedido.LineTotal }
                setTotal(total)
                listaPedidosAEliminar = listaPedidoDetalle
                pedidoDetalleItemsAdapter.updateData(listaPedidoDetalle)

            } catch (e: Exception){
                e.printStackTrace()
            }
        }*/

    }

    private fun setPedidosInfo(listaDetalles: List<ClientePedidoDetalle>, editMode: Boolean) {
        if (listaDetalles.isNotEmpty()){
            binding.txvCantidadPedidos.text = "${listaDetalles.size} articulos"
            binding.txvCantidadPedidos.isVisible = true
        } else {
            binding.txvCantidadPedidos.isVisible = false
        }
        val total = listaDetalles.sumOf { pedido -> pedido.LineTotal }
        gTotal = listaDetalles.sumOf { pedido -> pedido.GTotal }

        setTotal(listaDetalles)
        if (editMode){
            pedidoDetalleItemsAdapter.updateDataEditMode(listaDetalles)
        } else {
            pedidoDetalleItemsAdapter.updateData(listaDetalles)
        }
    }

    private fun setTotal(pedidoDetalles: List<ClientePedidoDetalle>) {
        val totalDespuesImpuestos = pedidoDetalles.sumOf { it.GTotal.format(2) }.format(2)
        val totolImpuestos = pedidoDetalles.sumOf { it.VatSum.format(2) }.format(2)
        val totalAntesImpuestos = pedidoDetalles.sumOf { it.LineTotal.format(2) }.format(2)

        binding.txvNPArtInfoTotalAntesDescuentoValue.text = "${SendData.instance.simboloMoneda} $totalAntesImpuestos"
        binding.txvNpArtInfoTotalValue.text = "${SendData.instance.simboloMoneda} $totalDespuesImpuestos"
        binding.txvNpArtInfoImpuestoValue.text = "${SendData.instance.simboloMoneda} $totolImpuestos"
    }


    //Resultado de articulos agregados
    val startForArticulosAgregadosResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val articulo = result.data?.getBooleanExtra("articuloAgregado", false)
            val accDocEntryRtrn = result.data?.getStringExtra("accDocEntry")

            if (articulo == true) {
                if (accDocEntryRtrn != null) {
                    accDocEntry = accDocEntryRtrn
                    pedidoViewModel.getAllPedidoDetallePorAccDocEntry(accDocEntry)
                }
            }
        }
    }

        //Resultado del articulo editado
    val startForEditItemResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val articulo = result.data?.getBooleanExtra("articuloAgregado", false)
                val accDocEntryRtrn = result.data?.getStringExtra("accDocEntry")

                if (articulo == true) {
                    if (accDocEntryRtrn != null) {
                        accDocEntry = accDocEntryRtrn
                        pedidoViewModel.getAllPedidoDetallePorAccDocEntry(accDocEntry)
                    }
                }
            }
        }

    //Resultado de articulos agregados
    val startForUpdateArticulosAgregadosResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val articulo = result.data?.getBooleanExtra("articuloAgregado", false)
            val accDocEntryRtrn = result.data?.getStringExtra("accDocEntry")

            if (articulo == true) {
                if (accDocEntryRtrn != null) {
                    accDocEntry = accDocEntryRtrn
                    pedidoViewModel.getAllPedidoDetallesParaEditar(accDocEntry)
                }
            }
        }
    }

    //Resultado del articulo editado
    val startForUpdateEditItemResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val articulo = result.data?.getBooleanExtra("articuloAgregado", false)
            val accDocEntryRtrn = result.data?.getStringExtra("accDocEntry")

            if (articulo == true) {
                if (accDocEntryRtrn != null) {
                    accDocEntry = accDocEntryRtrn
                    pedidoViewModel.getAllPedidoDetallesParaEditar(accDocEntry)
                }
            }
        }
    }





    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (pedidoDetalleItemsAdapter.itemCount > 0){
            val intentArticulosAgregador = Intent()
            intentArticulosAgregador.putExtra("cantidadArticulos", pedidoDetalleItemsAdapter.itemCount)
            intentArticulosAgregador.putExtra("total", binding.txvNpArtInfoTotalValue.text.toString())
            intentArticulosAgregador.putExtra("gTotal", gTotal)
            setResult(RESULT_OK, intentArticulosAgregador)
            super.onBackPressed()

        } else {
            Toast.makeText(this, "Debe agregar al menos un artículo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contenido_articulos, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        menu?.findItem(R.id.app_bar_venta_sugerida)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.app_bar_venta_sugerida -> {
                BaseDialogAceptDialog(this).showConfirmationDialog("¿Desea agregar un pedido sugerido?",
                    onConfirmacion = {
                        pedidoViewModel.getPedidoSugerido(prefsPedido.getCardCode())
                    },
                    onCancel = {

                    }
                )
            }

            R.id.app_bar_add -> {
                try {

                    if (intent.getBooleanExtra("editMode", false)){
                        Intent(this, NuevoPedidoArticuloInfoActivity::class.java)
                            .putExtra("accDocEntry", accDocEntry)
                            .putExtra("lineNum", pedidoDetalleItemsAdapter.itemCount + 1000)
                            .also { startForUpdateArticulosAgregadosResult.launch(it) }
                    } else {
                        Intent(this, NuevoPedidoArticuloInfoActivity::class.java)
                            .putExtra("accDocEntry", accDocEntry)
                            .putExtra("lineNum", pedidoDetalleItemsAdapter.itemCount)
                            .also { startForArticulosAgregadosResult.launch(it) }
                    }
                } catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }

            }


            R.id.app_bar_check -> {
                onBackPressed()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
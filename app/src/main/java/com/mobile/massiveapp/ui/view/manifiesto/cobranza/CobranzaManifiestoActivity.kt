package com.mobile.massiveapp.ui.view.manifiesto.cobranza

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R


import com.mobile.massiveapp.databinding.ActivityCobranzaManifiestoBinding
import com.mobile.massiveapp.domain.model.DoLiquidacionPagoView
import com.mobile.massiveapp.ui.adapters.LiquidacionPagoAdapter
import com.mobile.massiveapp.ui.adapters.extension.SwipeToDeletePedidos
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import com.mobile.massiveapp.ui.view.util.showMessage
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.droidsonroids.gif.GifDrawable

@AndroidEntryPoint
class CobranzaManifiestoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCobranzaManifiestoBinding
    private val manifiestoViewModel: ManifiestoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private lateinit var liquidacionPagoAdapter: LiquidacionPagoAdapter
    private var docEntryManifiesto = -1
    private var listaLiquidacionPagos : List<DoLiquidacionPagoView> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCobranzaManifiestoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        liquidacionPagoAdapter = LiquidacionPagoAdapter(emptyList()){ pago->
            if (pago.Canceled != "Y"){
                Intent(this, EditarPagoManifiestoActivity::class.java)
                    .putExtra("accDocEntry", pago.AccDocEntry)
                    .putExtra("montoPendienteCobrar", binding.txvPendienteCobrar.text.toString().toDouble())
                    .also { startActivity(it)  }
            }
        }
        binding.rvPagos.adapter = liquidacionPagoAdapter


        /**SWIPE TO DELETE**/
        val swipeToDeleteCallback = object : SwipeToDeletePedidos(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val accDocEntry = listaLiquidacionPagos[position].AccDocEntry

                manifiestoViewModel.deleteLiquidacionPago(accDocEntry)
                binding.rvPagos.adapter?.notifyItemChanged(position)
                //showMessage(this@CobranzaManifiestoActivity, position.toString())
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvPagos)
        /**SWIPE TO DELETE**/

        setDefaultUI()
        setDefaultInfo()

    }

    private fun setDefaultInfo() {
        val docEntry = intent.getIntExtra("docEntryFactura", 0)
        manifiestoViewModel.getInfoCobranzaManifiesto(docEntry)
        manifiestoViewModel.dataGetInfoCobranzaManifiesto.observe(this){ infoManifiestoDocumento ->


            binding.txvNroComprobante.text = infoManifiestoDocumento.Comprobante
            binding.txvFechaEmision.text = infoManifiestoDocumento.FechaEmision
            binding.txvCliente.text = infoManifiestoDocumento.NombreCliente
            binding.txvNombreCliente.text = infoManifiestoDocumento.CodigoCliente
            binding.txvMoneda.text = infoManifiestoDocumento.Moneda
            binding.txvTotalPendiente.text = infoManifiestoDocumento.TotalPendiente.toString()
            docEntryManifiesto = infoManifiestoDocumento.DocEntry

        }


        lifecycleScope.launch {
            manifiestoViewModel.dataGetAllLiquidacionPagoFlow.collect{ listaLiquidaciones->
                liquidacionPagoAdapter.updateData(listaLiquidaciones)
                listaLiquidacionPagos = listaLiquidaciones
            }
        }

        lifecycleScope.launch {
            manifiestoViewModel.dateGetAllTotalesPagosFlow.collect { totalesPagos->
                binding.txvTotalCobrado.text = totalesPagos.TotalCobrado.toString()
                binding.txvPendienteCobrar.text = totalesPagos.TotalPorCobrar.toString()
                //Boton Agregar PAGO
                binding.btnAddPago.isVisible = !(totalesPagos.TotalPorCobrar <= 0.0)
            }
        }
    }

    private fun setDefaultUI() {

        //Loading Save crobranza
        val gif = GifDrawable(this.resources, R.drawable.gif_loading)
        val loadingDialog = BaseDialogLoadingCustom(this, "Enviando Pagos...", gif)
        manifiestoViewModel.isLoadingSendPagos.observe(this){
            if (it){
                loadingDialog.startLoading()
            } else {
                loadingDialog.onDismiss()
            }
        }

        binding.btnAddPago.setOnClickListener {
            Intent(this, NuevoPagoManifiestoActivity::class.java)
                .putExtra("montoPendienteCobrar", binding.txvPendienteCobrar.text.toString().toDouble())
                .putExtra("docEntryManifiestoDocumento", docEntryManifiesto)
                .putExtra("docEntryFactura", intent.getIntExtra("docEntryFactura", -1))
                .also { startActivity(it)  }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showDialogConfirmBack()
        setResult(
            Activity.RESULT_OK, Intent()
                .putExtra("pedidoRegistradoExitosamente", true))
    }


    private fun showDialogConfirmBack(){
        /*val builder = AlertDialog.Builder(this)
        builder
            .setTitle("¿Seguro que desea eliminar los pagos de esta Cobranza?")
            .setPositiveButton("Aceptar"){ _, _ ->
                manifiestoViewModel.deleteAllPagosConfirmationDialog(accDocEntry = SendData.instance.accDocEntryDoc)
                onBackPressedDispatcher.onBackPressed()
            }
            .setNegativeButton("Cancelar"){ _, _ ->

            }

        val dialog = builder.create()
        dialog.show()*/
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        showDialogConfirmBack()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo_sn, menu)

        val iconCheck = menu?.findItem(R.id.app_bar_check)
        iconCheck?.setIcon(R.drawable.icon_double_check)

        val item = menu?.findItem(R.id.app_bar_connectivity_status)
        item?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                showDialogConfirmBack()
            }

            R.id.app_bar_check -> {
                manifiestoViewModel.sendPagos()
                manifiestoViewModel.dataSendPagos.observe(this){ response->
                    when(response.ErrorCodigo){
                        500 ->{
                            BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                                //Aceptar
                                usuarioViewModel.logOut()
                            }
                        }
                        0 -> {
                            showMessage(this, response.ErrorMensaje)
                            setResult(RESULT_OK)
                            onBackPressedDispatcher.onBackPressed()
                        }
                        else -> { showMessage(this, response.ErrorMensaje) }
                    }
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

}
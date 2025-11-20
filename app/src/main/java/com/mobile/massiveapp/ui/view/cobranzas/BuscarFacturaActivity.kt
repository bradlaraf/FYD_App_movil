package com.mobile.massiveapp.ui.view.cobranzas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityBuscarFacturaBinding
import com.mobile.massiveapp.ui.adapters.BuscarFacturaAdapter
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.view.facturas.InfoFacturaActivity
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.agregarPagoDetalle
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuscarFacturaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuscarFacturaBinding
    private lateinit var buscarFacturasAdapter: BuscarFacturaAdapter
    private val facturasViewModel: SocioFacturasViewModel by viewModels()
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var infoHash = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarFacturaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Selección factura"

        //Datos del Usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            try {
                infoHash["usuarioCode"] = usuario.Code
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

            //Inicializacion del adapter
        buscarFacturasAdapter = BuscarFacturaAdapter(emptyList()){ facturaSeleccionada->
            try {
                infoHash["docEntry"] = facturaSeleccionada.DocEntry
                infoHash["docTotal"] = facturaSeleccionada.DocTotal
                infoHash["paidToDate"] = facturaSeleccionada.PaidToDate
                infoHash["folio"] = "${facturaSeleccionada.FolioPref} - ${facturaSeleccionada.FolioNum}"
                infoHash["instId"] = facturaSeleccionada.InstlmntID
                infoHash["edit_Ptd"] = facturaSeleccionada.Edit_Ptd
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.rvBuscarFacturas.adapter = buscarFacturasAdapter



            //Se traen todos los pedidos por CardCode
        facturasViewModel.getAllFacturasPorCardCode(intent.getStringExtra("cardCode").toString())
            //LiveData de los pedidos
        facturasViewModel.dataGetAllFacturasPorCardCode.observe(this) { listaFacturas->
            buscarFacturasAdapter.updateData(listaFacturas)
        }
    }

    fun savePagoDetalle(monto: Double){
        val montoFinal = if (monto <= infoHash["paidToDate"] as Double) monto else infoHash["paidToDate"] as Double


            /** Solo puede haber una detalle de pago por cada factura **/
        val pagoDetalle = agregarPagoDetalle(
                monto =             montoFinal,
                accDocEntry =       SendData.instance.accDocEntryDoc,
                docLine =           if(intent.getBooleanExtra("editMode", false)) intent.getIntExtra("docLine", -1) else SendData.instance.docLine,
                docEntryFactura =   infoHash["docEntry"] as Int,
                instId =            infoHash["instId"] as Int,
                usuario =           infoHash["usuarioCode"] as String
            )

        if (intent.getBooleanExtra("edicion", false)){
            cobranzaViewModel.saveNuevoPagoDetalleParaEditar(pagoDetalle)
        } else {
            cobranzaViewModel.saveCobranzaDetalle(pagoDetalle)
        }

        /** Metodo obsoleto para la logica del app de Seidor **/
        /*facturasViewModel.savePaidToDatePorPago(
            docEntry = infoHash["docEntry"].toString(),
            monto
        )*/

        cobranzaViewModel.dataSaveCobranzaDetalleUseCase.observe(this){ success->
            if (success){
                setResult(RESULT_OK,
                    Intent()
                        .putExtra("docEntry", infoHash["docEntry"].toString())
                )
                onBackPressedDispatcher.onBackPressed()
            } else {
                Toast.makeText(this, "Error al guardar el pago", Toast.LENGTH_SHORT).show()
            }
        }

        cobranzaViewModel.dataSavePagoDetalleParaEditar.observe(this) { success->
            if (success){
                setResult(RESULT_OK,
                    Intent()
                        .putExtra("docEntry", infoHash["docEntry"].toString())
                )
                onBackPressedDispatcher.onBackPressed()
            } else {
                Toast.makeText(this, "Error al guardar el pago", Toast.LENGTH_SHORT).show()
            }
        }


        //

        facturasViewModel.dataSetEditPtdLikePaidToDate.observeOnce(this){

        }
    }

    fun showInfoFactura(){
        try {
            if (infoHash["docEntry"] == null){
                throw Exception("Seleccione una factura")
            }
            Intent(this, InfoFacturaActivity::class.java).also {
                it.putExtra("docEntry", infoHash["docEntry"] as Int)
                startActivity(it)
            }

        } catch (e:Exception){
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }



    fun showDialogToConfirm(){
        try {
            val paidToDate = infoHash["paidToDate"] as Double

            if (paidToDate <= 0.0){
                throw Exception("Factura ya cancelada")
            }

            if (infoHash["docEntry"] == null){
                throw Exception("Seleccione una factura")
            }

            val montoAMostrar = infoHash["paidToDate"] as Double

            BaseDialogEdtWithTypeEdt(
                "decimal",
                "$montoAMostrar"
            ){ monto->
                val montoDouble = monto.toDoubleOrNull()?:0.0
                if (montoDouble == 0.0){
                    /*Toast.makeText(this, "Formato incorrecto", Toast.LENGTH_SHORT).show()*/
                } else {
                    savePagoDetalle(monto.toDouble())
                }
            }.show(supportFragmentManager, "BaseDialogEdt")

        } catch (e:Exception){
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    /*-----------------------NAVBAR---------------------*/
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        // Crear un nuevo ítem programáticamente
        menu?.add(
            Menu.NONE,  // Group ID
            R.id.app_bar_expand,  // Item ID
            1,  // Order
            "Info"  // Title
        )?.setIcon(R.drawable.icon_info_circle)  // Set icon if needed
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_check -> {
                showDialogToConfirm()
            }

            R.id.app_bar_expand->{
                showInfoFactura()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
package com.mobile.massiveapp.ui.view.cobranzas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityNuevoPagoDetalleBinding
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.agregarPagoDetalle
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NuevoPagoDetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevoPagoDetalleBinding
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private val facturasViewModel: SocioFacturasViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var hashInfo = HashMap<String, Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoPagoDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setValoresIniciales()
        setDefaultUi()

    }

    private fun setValoresIniciales() {

        //Datos del Usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            try {
                hashInfo["usuarioCode"] = usuario.Code
            } catch (e: Exception){
                e.printStackTrace()
            }
        }


        //Pago detalle
        cobranzaViewModel.getLiquidacionPorCodeYLineNum(
            accDocEntry = SendData.instance.accDocEntryDoc,
            docLine = intent.getIntExtra("docLine", -1)
        )
        cobranzaViewModel.dataGetLiquidacionPorCodeYLineNum.observe(this){ pagoDetalle->
            try {
                binding.txvFacturaInfoFechaContableValue.text = pagoDetalle.U_MSV_MA_FECHA
                binding.txvFacturaInfoMonedaValue.text = pagoDetalle.U_MSV_MA_MON

                binding.txvNuevoPagoDetalleMontoValue.text = "S/${pagoDetalle.U_MSV_MA_IMP}"
                hashInfo["montoActual"] = pagoDetalle.U_MSV_MA_IMP


            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        //Factura
        facturasViewModel.getFacturaPorDocEntry(intent.getIntExtra("docEntry", -1).toString())
        facturasViewModel.dataGetFacturaPorDocEntry.observe(this){ factura->
            try {
                val numeroSunat = "${factura.FolioPref} - ${factura.FolioNum}"
                binding.txvFacturaInfoNumeroSunatValue.text = numeroSunat

            } catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

    private fun setDefaultUi() {
        /*title = "Factura"*/

        //Click Monto
        binding.clNuevoPagoDetalleMonto.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "decimal",
                binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim()
            ){ monto->

                val montoDouble = monto.toDoubleOrNull()?:0.0

                //El monto no puede exceder el total de la Factura
                val montoFinal =
                    if (montoDouble <= hashInfo["paidToDate"] as Double)
                        montoDouble
                    else
                        hashInfo["paidToDate"] as Double

                binding.txvNuevoPagoDetalleMontoValue.text = "S/${montoFinal}"
            }.show(supportFragmentManager, "Dialog")
        }
    }


    /*--------------------NAVBAR-----------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_check -> {
                try {

                    if (binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim().toDouble().format(2) <= 0.00){
                        throw Exception("El monto debe ser mayor a 0")
                    }


                    val pagoDetalle = agregarPagoDetalle(
                        monto =             binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim().toDouble().format(2),
                        accDocEntry =       SendData.instance.accDocEntryDoc,
                        docLine =           intent.getIntExtra("docLine", -1),
                        docEntryFactura =   hashInfo["docEntry"] as Int,
                        instId =            hashInfo["instId"] as Int,
                        usuario =           hashInfo["usuarioCode"] as String
                    )

                    if (intent.getBooleanExtra("edicion", false)){
                        cobranzaViewModel.saveNuevoPagoDetalleParaEditar(pagoDetalle)
                    } else {
                        cobranzaViewModel.saveCobranzaDetalle(pagoDetalle)
                    }


                    //Hace el cambio en el EditPtd
                    facturasViewModel.savePaidToDatePorPago(
                        docEntry = hashInfo["docEntry"].toString(),
                        getMontoAActualizar()
                    )


                    cobranzaViewModel.dataSaveCobranzaDetalleUseCase.observe(this){ success->
                        if (success){
                            setResult(RESULT_OK,
                                Intent()
                                    .putExtra("docEntry", hashInfo["docEntry"].toString())
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
                                    .putExtra("docEntry", hashInfo["docEntry"].toString())
                            )
                            onBackPressedDispatcher.onBackPressed()
                        } else {
                            Toast.makeText(this, "Error al guardar el pago", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun getMontoAActualizar():Double{
        val result = binding.txvNuevoPagoDetalleMontoValue.text.toString().replace("S/", "").trim().toDouble()- hashInfo["montoActual"] as Double
        return result
    }

}
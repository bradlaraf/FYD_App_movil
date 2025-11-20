package com.mobile.massiveapp.ui.view.cobranzas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityEditarCobranzaDetalleBinding
import com.mobile.massiveapp.ui.view.cobranzas.fragments.EditarCobranzaEditarDetalleFragment
import com.mobile.massiveapp.ui.view.cobranzas.fragments.EditarCobranzaNuevoDetalleFragment
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioFacturasViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditarCobranzaDetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarCobranzaDetalleBinding
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private val facturasViewModel: SocioFacturasViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by viewModels()
    private var hashInfo = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarCobranzaDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        this.startActivity(Intent(this, NuevaCobranzaActivity::class.java).apply {
            
        })
                //Datos del Usuario
        usuarioViewModel.getUsuarioFromDatabase()

            //Manager de los Fragments según sea Edicion o Creacion de PagoDetalle
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            if (intent.getBooleanExtra("editMode", false)){
                title = "Modificar Pago"

                cobranzaViewModel.getCobranzaDetallePorAccDocEntryYLineNum(
                    accDocEntry = SendData.instance.accDocEntryDoc,
                    lineNum = intent.getIntExtra("docLine", -2).toString()
                )

                replace<EditarCobranzaEditarDetalleFragment>(R.id.fcEditarCobranzaDetalle)
            } else {
                title = "Agregar Pago"

                providerViewModel.saveDocLine(intent.getIntExtra("docLine", -2))
                providerViewModel.saveCardCode(intent.getStringExtra("cardCode").toString())

                replace<EditarCobranzaNuevoDetalleFragment>(R.id.fcEditarCobranzaDetalle)
            }
        }

        cobranzaViewModel.dataGetCobranzaDetallePorAccDocEntryYLineNum.observe(this){ pagoDetalle->
            try {
                facturasViewModel.getFacturaPorDocEntry(pagoDetalle.DocEntry.toString())
            } catch (e: Exception){
                e.printStackTrace()
            }
        }






            //EDICION DETALLE
        /*if (intent.getBooleanExtra("editMode", false)){


            facturasViewModel.dataGetFacturaPorDocEntry.observe(this){ factura->
                binding.txvNuevoPagoDetalleFacturaValue.text = "${factura.FolioPref} - ${factura.FolioNum}"
                hashInfo["docEntry"] = factura.DocEntry
                hashInfo["montoAPagar"] = factura.PaidToDate
                hashInfo["instId"] = factura.InstlmntID
            }



            cobranzaViewModel.getCobranzaDetallePorAccDocEntryYLineNum(
                accDocEntry = SendData.instance.accDocEntryDoc,
                lineNum = SendData.instance.docLine.toString()
            )
            cobranzaViewModel.dataGetCobranzaDetallePorAccDocEntryYLineNum.observe(this){ pagoDetalle->
                binding.txvNuevoPagoDetalleMontoValue.text = "S/${pagoDetalle.SumApplied}"
                Toast.makeText(this, "DocEntry: ${pagoDetalle.DocEntry}", Toast.LENGTH_SHORT).show()
            }
        }*/
            //LiveData Factura Guardada
        cobranzaViewModel.dataSaveCobranzaDetalleUseCase.observe(this){
            if (it){
                Intent().also { intent->
                    setResult(RESULT_OK, intent)
                    onBackPressedDispatcher.onBackPressed()
                }
        }

        cobranzaViewModel.dataSavePagoDetalleParaEditar.observe(this){
            if (it){
                Intent().also { intent->
                    setResult(RESULT_OK, intent)
                    onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
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
                val fragment = supportFragmentManager.findFragmentById(R.id.fcEditarCobranzaDetalle)

                if (fragment is EditarCobranzaEditarDetalleFragment){
                    fragment.savePagoDetalle()
                } else if (fragment is EditarCobranzaNuevoDetalleFragment){
                    fragment.savePagoDetalle()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
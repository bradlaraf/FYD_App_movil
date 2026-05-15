package com.mobile.massiveapp.ui.view.manifiesto.cobranza

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityEditarPagoManifiestoBinding
import com.mobile.massiveapp.databinding.ActivityNuevoPagoManifiestoBinding
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogReferenciaTransferencia
import com.mobile.massiveapp.ui.view.util.agregarPagoLiquidacion
import com.mobile.massiveapp.ui.view.util.editarPagoLiquidacion
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getCodigoDeDocumentoActual
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditarPagoManifiestoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPagoManifiestoBinding
    private val generalViewModel: GeneralViewModel by viewModels()
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private val manifiestoViewModel: ManifiestoViewModel by viewModels()
    private var infoPago = HashMap<String, Any>()
    private var montoPendiente = 0.0
    private var montoAEditar = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPagoManifiestoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContent()
        setDefaultUi()
    }

    private fun setDefaultUi() {
        //Tipo de pago
        generalViewModel.getAllFormasPago()
        binding.clCobranzaTipoPago.setOnClickListener {
            generalViewModel.dataGetAllFormasPago.observe(this){ formasPago->
                BaseDialogChecklistWithId(
                    binding.txvCobranzaTipoPagoValue.text.toString(),
                    formasPago.map { it.Name }
                ) { tipoDePagoSeleccionado, id ->
                    binding.txvCobranzaTipoPagoValue.text = tipoDePagoSeleccionado
                    val codePagoSeleccionado = formasPago[id].Code
                    binding.clCobranzaManifiestoNroOperacion.isVisible = !codePagoSeleccionado.startsWith("E")

                    infoPago["medioPago"] = codePagoSeleccionado
                }.show(supportFragmentManager, "BaseDialogChecklist")
            }
        }

        //Numero de operacion
        binding.clCobranzaManifiestoNroOperacion.setOnClickListener {
            BaseDialogReferenciaTransferencia(
                tipo = "phone",
                textEditable = binding.txvCobranzaManifiestoNroOperacionValue.text.toString(),
                titulo = "Ingrese el número de operación",
                maxLenght = 8
            ){referencia->
                binding.txvCobranzaManifiestoNroOperacionValue.text = referencia
            }.show(supportFragmentManager, "ReferenciaDialog")
        }

        //Importe
        binding.clCobranzaManifiestoImporte.setOnClickListener {
            BaseDialogReferenciaTransferencia(
                tipo = "decimal",
                textEditable = binding.txvCobranzaManifiestoImporteValue.text.toString(),
                titulo = "Ingrese el importe",
                maxLenght = -1
            ){ importe->
                val montoPendiente = montoPendiente + montoAEditar
                val importeFormateado = importe.toDoubleOrNull()?:0.0.format(2)

                if (importeFormateado > montoPendiente) {
                    binding.txvCobranzaManifiestoImporteValue.text = montoPendiente.toString()
                } else  {
                    binding.txvCobranzaManifiestoImporteValue.text = importeFormateado.toString()
                }

            }.show(supportFragmentManager, "ReferenciaDialog")
        }
    }

    private fun setContent() {

        manifiestoViewModel.getLiquidacionPagoEdicion(intent.getStringExtra("accDocEntry").toString())
        manifiestoViewModel.dataGetLiquidacionPagoEdicion.observe(this){ liquidacionPago->
            montoAEditar = liquidacionPago.U_MSV_MA_IMP

            binding.txvCobranzaManifiestoImporteValue.text = liquidacionPago.U_MSV_MA_IMP.toString()
            infoPago["medioPago"] = liquidacionPago.U_MSV_MA_MEDIO
            generalViewModel.dataGetAllFormasPago.observe(this){ formasPago->
                binding.txvCobranzaTipoPagoValue.text = formasPago.filter { it.Code == liquidacionPago.U_MSV_MA_MEDIO }.firstOrNull()?.Name?:""
            }

            //Transferencia
            binding.clCobranzaManifiestoNroOperacion.isVisible = liquidacionPago.U_MSV_MA_NROOPE.isNotEmpty()
            binding.txvCobranzaManifiestoNroOperacionValue.text = liquidacionPago.U_MSV_MA_NROOPE
        }
        manifiestoViewModel.dataGetMontoPendientePagoEdicion.observe(this){ montoPendiente->
            this.montoPendiente = montoPendiente
            binding.txvMontoPendienteFactura.text = "Monto pendiente: $montoPendiente"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_aceptar_check, menu)
        val aceptarMenuItem = menu?.findItem(R.id.app_bar_aceptar)
        val s = SpannableString(aceptarMenuItem?.title)
        s.setSpan(ForegroundColorSpan(Color.WHITE), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        aceptarMenuItem?.title = s

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.app_bar_aceptar-> {
                try {
                    val formaPagoSelect = binding.txvCobranzaTipoPagoValue.text.toString()
                    if (formaPagoSelect.isEmpty()) {
                        throw Exception("Seleccione un método de pago")
                    }
                    if (binding.clCobranzaManifiestoNroOperacion.isVisible &&
                        binding.txvCobranzaManifiestoNroOperacionValue.text.toString().isEmpty()){
                        throw Exception ("Completar un número de operación")
                    }

                    val nroOperacion = binding.txvCobranzaManifiestoNroOperacionValue.text.toString()
                    val nroCuenta = binding.txvCobranzaTipoPagoTransferenciaCuentaTitle.text.toString()
                    manifiestoViewModel.dataGetLiquidacionPagoEdicion.observe(this) { liquidacionPago ->

                        manifiestoViewModel.editarLiquidacionPago(
                            editarPagoLiquidacion(
                                docLine = liquidacionPago.DocLine,
                                monto = binding.txvCobranzaManifiestoImporteValue.text.toString()
                                    .toDouble().format(2),
                                accDocEntry = liquidacionPago.AccDocEntry,
                                numeroOperacion = if (nroOperacion.isNotEmpty()) nroOperacion else "",
                                numeroCuenta = if (nroCuenta.isNotEmpty()) nroOperacion else "",
                                moneda = liquidacionPago.U_MSV_MA_MON,
                                medio = infoPago["medioPago"] as String,
                                instId = liquidacionPago.U_MSV_MA_PAGO,
                                liquidacion = liquidacionPago.U_MSV_MA_LIQ,
                                manifiesto = liquidacionPago.U_MSV_MA_MANIF,
                                docEntryFactura = liquidacionPago.U_MSV_MA_CLAVE,
                                docEntryManifiesto = liquidacionPago.DocEntry
                            )
                        )
                    }
                    setResult(RESULT_OK)
                    onBackPressedDispatcher.onBackPressed()
                } catch (e: Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            android.R.id.home->{
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
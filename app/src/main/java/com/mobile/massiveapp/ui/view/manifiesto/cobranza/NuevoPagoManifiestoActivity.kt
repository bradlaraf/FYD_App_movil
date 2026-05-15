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
import androidx.core.view.isVisible
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityNuevoPagoManifiestoBinding
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogReferenciaTransferencia
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.agregarPagoLiquidacion
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getCodigoDeDocumentoActual
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NuevoPagoManifiestoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNuevoPagoManifiestoBinding
    private val generalViewModel: GeneralViewModel by viewModels()
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private val manifiestoViewModel: ManifiestoViewModel by viewModels()
    private var infoPago = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoPagoManifiestoBinding.inflate(layoutInflater)
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
                val montoPendiente = intent.getDoubleExtra("montoPendienteCobrar", 0.0)
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
        //Monto pendiente
        binding.txvMontoPendienteFactura.text = "Monto pendiente: ${intent.getDoubleExtra("montoPendienteCobrar", 0.0)}"
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

                    manifiestoViewModel.saveLiquidacion(
                        agregarPagoLiquidacion(
                            docLine = -1,
                            monto = binding.txvCobranzaManifiestoImporteValue.text.toString().toDouble().format(2),
                            accDocEntry = getCodigoDeDocumentoActual(this),
                            numeroOperacion = if (nroOperacion.isNotEmpty()) nroOperacion else "",
                            numeroCuenta = if (nroCuenta.isNotEmpty()) nroOperacion else "",
                            moneda = "SOL",
                            medio = infoPago["medioPago"] as String,
                            instId = -1,
                            liquidacion = -1,
                            manifiesto = -1,
                            docEntryFactura = intent.getIntExtra("docEntryFactura", -1),
                            docEntryManifiesto = intent.getIntExtra("docEntryManifiestoDocumento", -1)
                        )
                    )

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
package com.mobile.massiveapp.ui.view.cobranzas

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.mobile.massiveapp.MassiveApp.Companion.prefs
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.model.FormaPago
import com.mobile.massiveapp.databinding.ActivityTipoDePagoBinding
import com.mobile.massiveapp.domain.model.DoFormaPago
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.base.BaseDialogReferenciaTransferencia
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.mostrarCalendarioMaterial
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TipoDePagoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTipoDePagoBinding
    private val generalViewModel: GeneralViewModel by viewModels()
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private var formaPagoSelect: FormaPago? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipoDePagoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setCuentaDefault()
       // setContainersVisibles(prefs.getTipoPago())
        generalViewModel.getAllFormasPago()


            //Tipo de pago
        binding.clCobranzaTipoPago.setOnClickListener {
            generalViewModel.dataGetAllFormasPago.observe(this){ formasPago->
                BaseDialogChecklistWithId(
                    binding.clCobranzaTipoPagoValue.text.toString(),
                    formasPago.map { it.Name }
                ) { tipoDePagoSeleccionado, id ->
                    binding.clCobranzaTipoPagoValue.text = tipoDePagoSeleccionado
                    prefs.wipe()
                    val codePagoSeleccionado = formasPago[id].Code
                    setInfoFormaPago(codePagoSeleccionado)
                    formaPagoSelect = formasPago[id]
                }.show(supportFragmentManager, "BaseDialogChecklist")
            }
        }

        //Monto total
        lifecycleScope.launch {
            cobranzaViewModel.dataMontoTotalPagosDetalle.collect {
                binding.clCobranzaTipoPagoEfectivoImporteValue.text = "$it"
                binding.txvCobranzaTipoPagoChequeImporteValue.text = "$it"
                binding.txvCobranzaTipoPagoTransferenciaImporteValue.text = "$it"
                binding.clCobranzaTipoPagoDepositoImporteValue.text = "$it"
            }
        }


        //Transferencia Referencia
        binding.clCobranzaTipoPagoTransferenciaReferencia.setOnClickListener {
            BaseDialogReferenciaTransferencia(
                tipo = "phone",
                textEditable = binding.txvCobranzaTipoPagoTransferenciaReferenciaValue.text.toString(),
                titulo = "Ingrese el número de referencia",
                maxLenght = 8
            ){referencia->
                binding.txvCobranzaTipoPagoTransferenciaReferenciaValue.text = referencia
            }.show(supportFragmentManager, "ReferenciaDialog")
        }

    }



    fun setCuentaDefault(){
        val cuenta = ""
        binding.clCobranzaTipoPagoChequeCuentaValue.text = cuenta
        binding.clCobranzaTipoPagoEfectivoCuentaValue.text = cuenta
        binding.txvCobranzaTipoPagoTransferenciaCuentaValue.text = cuenta
        binding.clCobranzaTipoPagoDepositoCuentaValue.text = cuenta

        binding.clCobranzaTipoPagoChequeCuenta.setOnClickListener {
            BaseDialogChecklistWithId(
                cuenta,
                listOf(cuenta)
            ){ cuenta, id -> }.show(supportFragmentManager, "cuentaDefaultDialog") }

        binding.clCobranzaTipoPagoEfectivoCuenta.setOnClickListener {
            BaseDialogChecklistWithId(
                cuenta,
                listOf(cuenta)
            ){ cuenta, id -> }.show(supportFragmentManager, "cuentaDefaultDialog") }


        binding.clCobranzaTipoPagoDepositoCuenta.setOnClickListener {
            BaseDialogChecklistWithId(
                cuenta,
                listOf(cuenta)
            ){ cuenta, id -> }.show(supportFragmentManager, "cuentaDefaultDialog") }
    }


    @SuppressLint("SetTextI18n")
    fun setInfoFormaPago(formaPagoCode: String){
        generalViewModel.dataGetAllFormasPago.observe(this) { listaFormasPago->
            val formaPago = listaFormasPago.filter { it.Code == formaPagoCode }.firstOrNull()?: FormaPago()

            binding.clCobranzaTipoPagoTransferenciaReferencia.isVisible = formaPagoCode != "E"


            //TITULOS
            binding.txvCobranzaTipoPagoTransferenciaCuentaTitle.text = "${formaPago.Name} cuenta"
            binding.txvCobranzaTipoPagoTransferenciaReferenciaTitle.text = "${formaPago.Name} referencia"

            //INFO
            binding.txvCobranzaTipoPagoTransferenciaCuentaValue.text = formaPago.U_MSV_MA_CUENTA


        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
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
                    if (formaPagoSelect == null) {
                        throw Exception("Seleccione un método de pago")
                    }
                    if (formaPagoSelect?.Code != "E" &&
                        binding.txvCobranzaTipoPagoTransferenciaReferenciaValue.text.toString().isEmpty()){
                        throw Exception ("Completar un número de operación")
                    }

                    prefs.saveTipoPago(formaPagoSelect?.Code?:"")
                    prefs.saveCuentaPago(formaPagoSelect?.U_MSV_MA_CUENTA?:"")
                    prefs.saveReferenciaTransfer(binding.txvCobranzaTipoPagoTransferenciaReferenciaValue.text.toString())
                    //U_MSV_MA_MEDIO
                    //U_MSV_MA_NROOPE
                    //U_MSV_MA_CTA
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
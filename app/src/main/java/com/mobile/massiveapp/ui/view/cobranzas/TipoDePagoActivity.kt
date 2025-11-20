package com.mobile.massiveapp.ui.view.cobranzas

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
import com.mobile.massiveapp.databinding.ActivityTipoDePagoBinding
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.mostrarCalendarioMaterial
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

    private var transferencia = false
    private var deposito = false
    private var efectivo = false
    private var cheque = false

    private var MONTO_PAGO: Double = 0.0
    private var TIPO_PAGO = ""
    private var CUENTA_PAGO = ""
    private var FECHA_VENCIMIENTO_CHEQUE = ""
    private var REFERENCIA_TRANSFERENCIA = ""
    private var NUMERO_CHEQUE = 0
    private var CODIGO_BANCO = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipoDePagoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setCuentaDefault()
        setContainersVisibles(prefs.getTipoPago())


            //Tipo de pago
        binding.clCobranzaTipoPago.setOnClickListener {
            BaseDialogChecklistWithId(
                binding.clCobranzaTipoPagoValue.text.toString(),
                listOf("Efectivo", "Transferencia", "Depósito","Cheque")
            ) { tipoDePagoSeleccionado, id ->
                binding.clCobranzaTipoPagoValue.text = tipoDePagoSeleccionado
                prefs.wipe()
                setContainersVisibles(tipoDePagoSeleccionado)
                TIPO_PAGO = tipoDePagoSeleccionado

            }.show(supportFragmentManager, "BaseDialogChecklist")
        }

        //Monto total
        lifecycleScope.launch {
            cobranzaViewModel.dataMontoTotalPagosDetalle.collect {
                binding.clCobranzaTipoPagoEfectivoImporteValue.text = "$it"
                binding.clCobranzaTipoPagoChequeImporteValue.text = "$it"
                binding.clCobranzaTipoPagoTransferenciaImporteValue.text = "$it"
                binding.clCobranzaTipoPagoDepositoImporteValue.text = "$it"
            }
        }


        //Transferencia Referencia
        binding.clCobranzaTipoPagoTransferenciaReferencia.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "phone",
                binding.clCobranzaTipoPagoTransferenciaReferenciaValue.text.toString(),
                8
            ){referencia->
                binding.clCobranzaTipoPagoTransferenciaReferenciaValue.text = referencia
            }.show(supportFragmentManager, "ReferenciaDialog")
        }

        //Deposito Referencia
        binding.clCobranzaTipoPagoDepositoReferencia.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "phone",
                binding.clCobranzaTipoPagoDepositoReferenciaValue.text.toString(),
                8
            ){referencia->
                binding.clCobranzaTipoPagoDepositoReferenciaValue.text = referencia
            }.show(supportFragmentManager, "DepositoDialog")
        }


        //Bancos
        generalViewModel.getAllBancos()
        generalViewModel.dataGetAllBancos.observe(this){ bancos->
            try {
                if (prefs.getBancoCheque().isNotEmpty() && prefs.getTipoPago() == "Cheque"){
                    val bancoGuardado = bancos.filter { it.BankCode == prefs.getBancoCheque() }[0].BankName
                    binding.clCobranzaTipoPagoChequeBancoValue.text = bancoGuardado
                } else {
                    binding.clCobranzaTipoPagoChequeBancoValue.text = bancos[0].BankName
                    CODIGO_BANCO = bancos[0].BankCode
                }
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
        binding.clCobranzaTipoPagoChequeBanco.setOnClickListener {
            generalViewModel.dataGetAllBancos.observe(this){ bancos->
                BaseDialogChecklistWithId(
                    binding.clCobranzaTipoPagoChequeBancoValue.text.toString(),
                    bancos.map { it.BankName }
                ) { bancoSeleccionado, id ->
                    binding.clCobranzaTipoPagoChequeBancoValue.text = bancoSeleccionado
                    CODIGO_BANCO = bancos[id].BankCode
                }.show(supportFragmentManager, "BancoDialog")
            }
        }

        /*if (!intent.getStringExtra("CODIGO_BANCO").isNullOrEmpty()){
            val bankCode = intent.getStringExtra("CODIGO_BANCO").toString()
            generalViewModel.getAllBancos()
            generalViewModel.dataGetAllBancos.observeOnce(this){ bancos->
                try {
                    binding.clCobranzaTipoPagoChequeBancoValue.text = bancos.filter { it.BankCode ==  bankCode}[0].BankName
                } catch (e:Exception){
                    e.printStackTrace()
                }

            }
        }*/



        //Vencimiento
        binding.clCobranzaTipoPagoChequeVencimiento.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mostrarCalendarioMaterial(this, getFechaActual()){ dia, mes, year ->
                    binding.clCobranzaTipoPagoChequeVencimientoValue.text = "$year-$mes-$dia"
                    FECHA_VENCIMIENTO_CHEQUE = "$year-$mes-$dia"
                }
            }
        }

        //Numero de Cheque
        binding.clCobranzaTipoPagoChequeNumero.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "phone",
                binding.clCobranzaTipoPagoChequeNumeroValue.text.toString()
            ){numeroCheque->
                binding.clCobranzaTipoPagoChequeNumeroValue.text = numeroCheque
                NUMERO_CHEQUE = numeroCheque.toInt()
            }.show(supportFragmentManager, "ChequeNumeroDialog")
        }



    }






    fun setCuentaDefault(){
        val cuenta = "EFECTIVO EN TRÁNSITO"
        binding.clCobranzaTipoPagoChequeCuentaValue.text = cuenta
        binding.clCobranzaTipoPagoEfectivoCuentaValue.text = cuenta
        binding.clCobranzaTipoPagoTransferenciaCuentaValue.text = cuenta
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

        binding.clCobranzaTipoPagoTransferenciaCuenta.setOnClickListener {
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

    fun setContainersVisibles(tipoPago: String){
        val tipoPagoDefault = "Efectivo"
        binding.clCobranzaTipoPagoValue.text = tipoPago

        when (tipoPago){
            "Efectivo"-> {
                lifecycleScope.launch {
                    generalViewModel.dataCuentaCashDefault.take(1).collect{
                        CUENTA_PAGO = it
                    }
                }
                deposito = false
                transferencia = false
                efectivo = true
                cheque = false
            }
            "Transferencia"-> {
                lifecycleScope.launch {
                    generalViewModel.dataCuentaTransferenciaDefault.take(1).collect{
                        CUENTA_PAGO = it
                    }
                }
                binding.clCobranzaTipoPagoTransferenciaReferenciaValue.text = prefs.getReferenciaTransfer()

                transferencia = true
                efectivo = false
                cheque = false
                deposito = false
            }

            "Depósito"-> {
                lifecycleScope.launch {
                    generalViewModel.dataCuentaDepositoDefault.take(1).collect{
                        CUENTA_PAGO = it
                    }
                }
                binding.clCobranzaTipoPagoDepositoReferenciaValue.text = prefs.getReferenciaTransfer()

                transferencia = false
                efectivo = false
                cheque = false
                deposito = true
            }

            "Cheque"-> {
                lifecycleScope.launch {
                    generalViewModel.dataCuentaChequeDefault.take(1).collect{
                        CUENTA_PAGO = it
                    }
                }
                binding.clCobranzaTipoPagoChequeNumeroValue.text = if(prefs.getNumeroCheque() != -1) "${prefs.getNumeroCheque()}" else ""
                binding.clCobranzaTipoPagoChequeVencimientoValue.text = prefs.getFechaCheque().ifEmpty { getFechaActual() }

                transferencia = false
                efectivo = false
                cheque = true
                deposito = false
            } else -> {
                lifecycleScope.launch {
                    generalViewModel.dataCuentaCashDefault.take(1).collect{
                        CUENTA_PAGO = it
                    }
                }
                binding.clCobranzaTipoPagoValue.text = tipoPagoDefault
                transferencia = false
                efectivo = true
                cheque = false
                deposito = false
            }
        }

            binding.clCobranzaTipoPagoEfectivoCuenta.isVisible = efectivo
            binding.clCobranzaTipoPagoEfectivoImporte.isVisible = efectivo

            binding.clCobranzaTipoPagoTransferenciaCuenta.isVisible = transferencia
            binding.clCobranzaTipoPagoTransferenciaReferencia.isVisible = transferencia
            binding.clCobranzaTipoPagoTransferenciaImporte.isVisible = transferencia

            binding.clCobranzaTipoPagoDepositoCuenta.isVisible = deposito
            binding.clCobranzaTipoPagoDepositoReferencia.isVisible = deposito
            binding.clCobranzaTipoPagoDepositoImporte.isVisible = deposito

            binding.clCobranzaTipoPagoChequeCuenta.isVisible = cheque
            binding.clCobranzaTipoPagoChequeBanco.isVisible = cheque
            binding.clCobranzaTipoPagoChequeVencimiento.isVisible = cheque
            binding.clCobranzaTipoPagoChequeNumero.isVisible = cheque
            binding.clCobranzaTipoPagoChequeImporte.isVisible = cheque
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
                    if (cheque){
                        if (binding.clCobranzaTipoPagoChequeNumeroValue.text.isEmpty()){
                            throw Exception("Ingrese número de cheque")
                        }
                    }

                    if (transferencia){
                        if (binding.clCobranzaTipoPagoTransferenciaReferenciaValue.text.isEmpty()){
                            throw Exception("Ingrese número de referencia")
                        }
                    }

                    if (deposito){
                        if (binding.clCobranzaTipoPagoDepositoReferenciaValue.text.isEmpty()){
                            throw Exception("Ingrese número de referencia")
                        }
                    }

                    when (binding.clCobranzaTipoPagoValue.text.toString()){
                        "Efectivo"->{
                            prefs.saveCuentaPago(CUENTA_PAGO)
                        }
                        "Transferencia"-> {
                            prefs.saveCuentaPago(CUENTA_PAGO)
                            prefs.saveReferenciaTransfer(binding.clCobranzaTipoPagoTransferenciaReferenciaValue.text.toString())
                        }
                        "Depósito"-> {
                            prefs.saveCuentaPago(CUENTA_PAGO)
                            prefs.saveReferenciaTransfer(binding.clCobranzaTipoPagoDepositoReferenciaValue.text.toString())
                        }
                        "Cheque"-> {
                            prefs.saveCuentaPago(CUENTA_PAGO)
                            prefs.saveFechaCheque(binding.clCobranzaTipoPagoChequeVencimientoValue.text.toString())
                            prefs.saveNumeroCheque(binding.clCobranzaTipoPagoChequeNumeroValue.text.toString().toInt())
                            prefs.saveBancoCheque(CODIGO_BANCO)
                        }
                    }
                    prefs.saveTipoPago(binding.clCobranzaTipoPagoValue.text.toString())

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
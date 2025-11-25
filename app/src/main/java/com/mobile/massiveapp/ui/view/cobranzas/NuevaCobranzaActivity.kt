package com.mobile.massiveapp.ui.view.cobranzas

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.mobile.massiveapp.MassiveApp.Companion.prefs
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.databinding.ActivityNuevaCobranzaBinding
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdt
import com.mobile.massiveapp.ui.base.BaseDialogEdtCharacterLimit
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.pedidocliente.BuscarClienteActivity
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.agregarPagoCabecera
import com.mobile.massiveapp.ui.view.util.formatoSinModena
import com.mobile.massiveapp.ui.view.util.getCodigoDeDocumentoActual
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import pl.droidsonroids.gif.GifDrawable

@AndroidEntryPoint
class NuevaCobranzaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevaCobranzaBinding
    private val generalViewModel: GeneralViewModel by viewModels()
    private val socioNegocioViewModel: SocioViewModel by viewModels()
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var infoCobranza = HashMap<String, Any>()
    private var NUMERO_PAGOS = 0

    private var TIPO_PAGO = "C"

    private var MONTO_PAGO_CASH:Double = 0.0
    private var MONTO_PAGO_TRANSFERENCIA: Double = 0.0
    private var MONTO_PAGO_CHEQUE:Double = 0.0

    private var CUENTA_PAGO_CASH = ""
    private var CUENTA_PAGO_TRANSFERENCIA = ""
    private var CUENTA_PAGO_CHEQUE = ""

    private var FECHA_VENCIMIENTO_CHEQUE = ""
    private var FECHA_TRANSFERENCIA = ""

    private var NUMERO_REFERENCIA_TRANSFERENCIA = ""
    private var NUMERO_CHEQUE = 0

    private var CODIGO_BANCO = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaCobranzaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDefaultUi()
        setValoresIniciales()

        lifecycleScope.launch {
            asignarTipoPagoDefault()
        }

            /*** En caso de pago directo ***/
        if (intent.getStringExtra("cardCode") != null) {
            obtenerInformacionParaPagoDirecto(intent.getStringExtra("cardCode").toString())
        }
            /******************/

        //LiveData socio de negocio elegido
        socioNegocioViewModel.dataSocioNegocioPorCardCode.observe(this) { socioNegocioElegido ->
            try {
                binding.txvNuevaCobranzaNombreSocioNegocioValue.text = socioNegocioElegido.CardName
                binding.txvNuevaCobranzaCodigoSocioNegocioValue.text = socioNegocioElegido.CardCode

                //Se muestra el boton de pagos
                binding.clNuevaCobranzaPagos.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        //LiveData de todos los pagos detalle
        cobranzaViewModel.dataGetAllPagosDetallePorAccDocEntry.observe(this) { listaPagosDetalle ->
            try {
                NUMERO_PAGOS = listaPagosDetalle.size
                val total = listaPagosDetalle.sumOf { it.SumApplied }.toString().format(2)
                binding.txvNuevaCobranzaPagosValue.text = "${listaPagosDetalle.size} pagos"
                binding.txvNuevaCobranzaMontoTotalValue.text =
                    "${SendData.instance.simboloMoneda}${total.format(2)}"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //LiveData Cierre Sesion
        cierreSesion()


    }

    private fun cierreSesion() {
        //LiveData Cierre de Sesion
        usuarioViewModel.isLoadingLogOut.observe(this){
            val loadingSimpleDialog = BaseDialogSImpleLoading(this)
            loadingSimpleDialog.startLoading("Cerrando Sesion...")

            if (!it){
                loadingSimpleDialog.onDismiss()
            }
        }
        usuarioViewModel.dataLogOut.observe(this) { error->
            if (error.ErrorCodigo == 0){
                Intent(this, LoginActivity::class.java).also { startActivity(it) }
                finish()
            } else {
                Toast.makeText(this, error.ErrorMensaje, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDefaultUi() {
        binding.clNuevaCobranzaNumeroDocumento.isVisible = false

        //Loading Save crobranza
        val gif = GifDrawable(this.resources, R.drawable.gif_loading)
        val loadingDialog = BaseDialogLoadingCustom(this, "Enviando Cobranza...", gif)
        cobranzaViewModel.isLoadingSaveCobranza.observe(this){
            if (it){
                loadingDialog.startLoading()
            } else {
                loadingDialog.onDismiss()
            }
        }

        //Click Codigo socio de negocio
        binding.clNuevaCobranzaCodigoSocioNegocio.setOnClickListener {
            Intent(this, BuscarClienteActivity::class.java)
                .putExtra("clienteParaPago", true)
                .also {
                    startForSocioNegocioResult.launch(it)
                }
        }

        //Click Pagos
        binding.clNuevaCobranzaPagos.setOnClickListener {
            Intent(this, ContenidoCobranzaDetalleActivity::class.java)
                .putExtra(
                    "cardCode",
                    binding.txvNuevaCobranzaCodigoSocioNegocioValue.text.toString()
                )
                .also { startForCobranzaDetalleResult.launch(it) }
        }

        //Click Glosa
        binding.clNuevaCobranzaGlosa.setOnClickListener {
            BaseDialogEdt(
                binding.txvNuevaCobranzaGlosaValue.text.toString()
            ) { textoGlosa ->
                binding.txvNuevaCobranzaGlosaValue.text = textoGlosa
            }.show(supportFragmentManager, "BaseDialogEdt")
        }


        //Click Comentario
        binding.clNuevaCobranzaComentarios.setOnClickListener {
            BaseDialogEdtCharacterLimit(
                binding.txvNuevaCobranzaComentariosValue.text.toString(),
                "Ingrese el comentario"
            ) { textoComentarios ->
                binding.txvNuevaCobranzaComentariosValue.text = textoComentarios
            }.show(supportFragmentManager, "BaseDialogEdt")
        }


        //CLick en numero referencia
        binding.clNuevaCobranzaReferenciaOperacion.setOnClickListener {
            BaseDialogEdt(
                binding.txvNuevaCobranzaReferenciaOperacionValue.text.toString(),
                8
            ) { numeroReferencia ->
                binding.txvNuevaCobranzaReferenciaOperacionValue.text = numeroReferencia
                infoCobranza["numeroReferencia"] = numeroReferencia
            }.show(supportFragmentManager, "BaseDialogEdtWithTypeEdt")
        }


        //Click Tipo de Pago
        binding.clNuevaCobranzaTipoPago.setOnClickListener {
            Intent(this, TipoDePagoActivity::class.java).also {
                it.putExtra("TIPO_PAGO", binding.txvNuevaCobranzaTipoPagoValue.text)
                it.putExtra("CODIGO_BANCO", CODIGO_BANCO)
                startForTipoCobranzaResult.launch(it)
            }
        }
    }

    private fun setValoresIniciales() {
        /*//Cuentas bancarias
        generalViewModel.getAllGeneralCuentasB()*/

        //numero de documento actual
        binding.txvNuevoPedidoNumeroDocumentoValue.text = getCodigoDeDocumentoActual(this)

        //accDocEntry
        SendData.instance.accDocEntryDoc = binding.txvNuevoPedidoNumeroDocumentoValue.text.toString()

        //fecha actual
        binding.txvNuevaCobranzaFechaContableValue.text = getFechaActual()

        //tipo de pago por defecto
        prefs.saveTipoPago("")


        //set datos de Usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this) { usuario ->
            try {
                infoCobranza["codigoUsuario"] = usuario.Code
                infoCobranza["docCur"] = usuario.DefaultCurrency
                infoCobranza["numeroSeriePago"] = usuario.DefaultPagoRSeries
                infoCobranza["cashAccount"] = usuario.DefaultAcctCodeEf
                infoCobranza["slpCode"] = usuario.DefaultSlpCode
                infoCobranza["numeroReferencia"] = ""

                //Se setean las cuentas por defecto
                binding.txvNuevaCobranzaCuentasBValue.text = usuario.DefaultAcctCodeTr
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        //Set Empleado de venta
        generalViewModel.getEmpleadoDeVentas()
        generalViewModel.dataGetEmpleadoDeVemtas.observe(this) { empleadoDeVentas ->
            try {
                binding.txvNuevaCobranzaEmpleadoVentasValue.text = empleadoDeVentas.SlpName
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        //set Monedas
        generalViewModel.getAllGeneralMonedas()
        generalViewModel.dataGetAllGeneralMonedas.observe(this) { listaMonedas ->
            try {
                SendData.instance.simboloMoneda = listaMonedas.firstOrNull()?.CurrName?:"S/"
                binding.txvNuevaCobranzaMonedaValue.text = listaMonedas.firstOrNull()?.CurrCode?:"SOL"
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.clNuevaCobranzaMoneda.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvNuevaCobranzaMonedaValue.text.toString(),
                    listaMonedas.map { it.CurrCode }
                ) { monedaElegida, id ->
                    binding.txvNuevaCobranzaMonedaValue.text = monedaElegida.uppercase()
                }.show(supportFragmentManager, "BaseDialogChecklistWithId")
            }
        }


    }

    private fun obtenerInformacionParaPagoDirecto(cardCode: String) {
        socioNegocioViewModel.getSocioNegocioPorCardCode(cardCode)
        cobranzaViewModel.savePedidosDetalleParaCobranzaDirecta(
            docEntry = intent.getIntExtra("docEntry", -1).toString(),
            accDocEntry = binding.txvNuevoPedidoNumeroDocumentoValue.text.toString()
        )

        //Pago detalle PAGO DIRECTO
        cobranzaViewModel.dataSavePedidosDetalleParaCobranzaDirecta.observeOnce(this) { success ->
            if (success) {
                cobranzaViewModel.getAllPagosDetallePorAccDocEntry(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())
            }
        }
    }

    val startForTipoCobranzaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            binding.txvNuevaCobranzaTipoPagoValue.text = prefs.getTipoPago()
        }
    }

    val startForSocioNegocioResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val cardCode = data?.getStringExtra("cardCode")
            if (cardCode != null){
                socioNegocioViewModel.getSocioNegocioPorCardCode(cardCode)
            }
        }
    }

    val startForCobranzaDetalleResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val montoPago = data?.getStringExtra("montoPago")
            val cantidadPagos = data?.getIntExtra("cantidadPagos", 0)

            if(montoPago != null ){
                binding.clNuevaCobranzaMontoTotal.visibility = View.VISIBLE

                    //Se traen la lista de detalles
                cobranzaViewModel.getAllPagosDetallePorAccDocEntry(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())
            }
        }

    if (result.resultCode == Activity.RESULT_CANCELED){
        val data = result.data
        val cantidadPagos = data?.getIntExtra("cantidadPagos", 0)
        if (cantidadPagos != null){
            binding.txvNuevaCobranzaPagosValue.text = "$cantidadPagos pagos"
        }
    }

}
    suspend fun asignarTipoPagoDefault(){
        generalViewModel.dataCuentaCashDefault.take(1).collect{
            CUENTA_PAGO_CASH = it
            prefs.saveCuentaPago(it)
        }
        TIPO_PAGO = "E"
        binding.txvNuevaCobranzaTipoPagoValue.text = "Efectivo"
        prefs.saveTipoPago("Efectivo")
    }

    fun asignarVariables() {
        val monto_pago = binding.txvNuevaCobranzaMontoTotalValue.text.toString().formatoSinModena(SendData.instance.simboloMoneda)

        when (prefs.getTipoPago()){
            "Efectivo"-> {
                MONTO_PAGO_CASH = monto_pago
                CUENTA_PAGO_CASH = prefs.getCuentaPago()
                TIPO_PAGO = "E"
            }
            "Cheque"-> {
                MONTO_PAGO_CHEQUE = monto_pago
                FECHA_VENCIMIENTO_CHEQUE = prefs.getFechaCheque()
                CUENTA_PAGO_CHEQUE = prefs.getCuentaPago()
                CODIGO_BANCO = prefs.getBancoCheque()
                NUMERO_CHEQUE = prefs.getNumeroCheque()
                TIPO_PAGO = "C"
            }
            "Transferencia"-> {
                MONTO_PAGO_TRANSFERENCIA = monto_pago
                NUMERO_REFERENCIA_TRANSFERENCIA = prefs.getReferenciaTransfer()
                CUENTA_PAGO_TRANSFERENCIA = prefs.getCuentaPago()
                FECHA_TRANSFERENCIA = getFechaActual()
                TIPO_PAGO = "T"
            }

            "Depósito"-> {
                MONTO_PAGO_TRANSFERENCIA = monto_pago
                NUMERO_REFERENCIA_TRANSFERENCIA = prefs.getReferenciaTransfer()
                CUENTA_PAGO_TRANSFERENCIA = prefs.getCuentaPago()
                FECHA_TRANSFERENCIA = getFechaActual()
                TIPO_PAGO = "D"
            }

        }
    }

    private fun showDialogConfirmBack(){
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("¿Se eliminaran todos los cambios realizados, seguro que desea continuar?")
            .setPositiveButton("Aceptar"){ _, _ ->
                cobranzaViewModel.deleteAllPagosDetallesPorAccDocEntry(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())
                cobranzaViewModel.dataDeleteAllPagosDetallesPorAccDocEntry.observe(this){ success->
                    if(success){
                        prefs.wipe()
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
            .setNegativeButton("Cancelar"){ dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showDialogConfirmBack()
    }


    /*------------------NAVBAR-----------------------*/


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_check)?.setIcon(R.drawable.icon_double_check)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_check->{
                try {
                    if (binding.txvNuevaCobranzaCodigoSocioNegocioValue.text.isEmpty()){
                        throw Exception("Elija un socio de negocio")
                    }

                    if (NUMERO_PAGOS <= 0){
                        throw Exception("Registre al menos un pago")
                    }

                    if (binding.txvNuevaCobranzaTipoPagoValue.text.isEmpty()){
                        throw Exception("Elija un tipo de pago")
                    }
                    asignarVariables()
                    /*Toast.makeText(this, "$MONTO_PAGO_CASH-${prefs.getTipoPago()}", Toast.LENGTH_SHORT).show()*/

                    cobranzaViewModel.dataGetAllPagosDetallePorAccDocEntry.observe(this){ listaPagosDetalle->
                        cobranzaViewModel.saveCobranzaCabecera(
                            agregarPagoCabecera(
                                accDocEntry=                        binding.txvNuevoPedidoNumeroDocumentoValue.text.toString(),
                                cuentaCash=                         CUENTA_PAGO_CASH,
                                cardCode =                          binding.txvNuevaCobranzaCodigoSocioNegocioValue.text.toString(),
                                cardName =                          binding.txvNuevaCobranzaNombreSocioNegocioValue.text.toString(),
                                montoTotal =                        MONTO_PAGO_CASH,
                                comentarios=                        binding.txvNuevaCobranzaComentariosValue.text.toString(),
                                moneda =                            infoCobranza["docCur"] as String,
                                usuario =                           infoCobranza["codigoUsuario"] as String,
                                numeroReferenciaTransferencia =     NUMERO_REFERENCIA_TRANSFERENCIA,
                                numeroDeSeriePago =                 infoCobranza["numeroSeriePago"] as Int,
                                montoTotalTransferencia =           MONTO_PAGO_TRANSFERENCIA,
                                transferenciaCuenta =               CUENTA_PAGO_TRANSFERENCIA,
                                fechaTranferencia =                 FECHA_TRANSFERENCIA,
                                typePayment =                       TIPO_PAGO,
                                glosa =                             binding.txvNuevaCobranzaGlosaValue.text.toString(),
                                slpCode =                           infoCobranza["slpCode"] as Int,
                                CheckAct =                          CUENTA_PAGO_CHEQUE,
                                BankCode =                          CODIGO_BANCO,
                                DueDate =                           FECHA_VENCIMIENTO_CHEQUE,
                                CheckSum =                          MONTO_PAGO_CHEQUE,
                                CheckNum =                          NUMERO_CHEQUE,
                                listaDetalles =                     listaPagosDetalle.map { it.toModel() }
                            ), this
                        )
                    }

                    cobranzaViewModel.dataSaveCobranzaCabeceraUseCase.observe(this){ response->
                        when(response.ErrorCodigo){
                            500 ->{
                                BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                                    //Aceptar
                                    usuarioViewModel.logOut()
                                }
                            }
                            0 -> {
                                showMessage(response.ErrorMensaje)
                                setResult(RESULT_OK)
                                onBackPressedDispatcher.onBackPressed()
                            }
                            else -> { showMessage(response.ErrorMensaje) }
                        }
                    }

                } catch (e: Exception){
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }

            }

            android.R.id.home->{
                showDialogConfirmBack()
            }
        }
        return super.onOptionsItemSelected(item)
    }



}
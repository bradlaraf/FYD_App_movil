package com.mobile.massiveapp.ui.view.cobranzas

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.mobile.massiveapp.MassiveApp
import com.mobile.massiveapp.MassiveApp.Companion.prefs
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.model.ClientePagosDetalle
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.databinding.ActivityEditarCobranzaBinding
import com.mobile.massiveapp.domain.model.DoClientePago
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdt
import com.mobile.massiveapp.ui.base.BaseDialogEdtCharacterLimit
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.actualizarPagoCabecera
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.formatoSinModena
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import com.mobile.massiveapp.ui.view.util.setCopyToClipboardOnLongClick
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable

@AndroidEntryPoint
class EditarCobranzaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarCobranzaBinding
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
        binding = ActivityEditarCobranzaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDefaultUi()
        setValoresIniciales()


            //Duplicar todos los pagos detalles por accDocEntry
        cobranzaViewModel.duplicateAllPagoDetallesParaActualizacion(intent.getStringExtra("accDocEntry").toString())


            //Get Pedido Cabecera
        cobranzaViewModel.getPagoCabeceraPorAccDocEntry(intent.getStringExtra("accDocEntry").toString())
        cobranzaViewModel.dataGetPagoCabeceraPorAccDocEntry.observe(this){ pagoCabecera->
            try {
                asignarTipoDePago(pagoCabecera)

                binding.txvNuevoPedidoNumeroDocumentoValue.text = pagoCabecera.AccDocEntry
                binding.txvNuevaCobranzaEmpleadoVentasValue.text = pagoCabecera.AccCreateUser
                binding.txvNuevaCobranzaFechaContableValue.text = pagoCabecera.TaxDate
                binding.txvNuevaCobranzaCodigoSocioNegocioValue.text = pagoCabecera.CardCode
                binding.txvNuevaCobranzaNombreSocioNegocioValue.text = pagoCabecera.CardName
                binding.txvNuevaCobranzaMonedaValue.text = pagoCabecera.DocCurr
                binding.txvNuevaCobranzaGlosaValue.text = pagoCabecera.JrnlMemo

                binding.txvNuevaCobranzaComentariosValue.text = pagoCabecera.Comments

                infoCobranza["empID"] = pagoCabecera.AccCreateUser
                infoCobranza["cuentaB"] = pagoCabecera.TrsfrAcct
                infoCobranza["fechaCreacion"] = pagoCabecera.AccCreateDate
                infoCobranza["horaCreacion"] = pagoCabecera.AccCreateHour
                infoCobranza["taxDate"] = pagoCabecera.TaxDate
                infoCobranza["docNum"] = pagoCabecera.DocNum
                infoCobranza["docEntry"] = pagoCabecera.DocEntry

                infoCobranza["accNotificado"] = pagoCabecera.AccNotificado
                infoCobranza["transId"] = pagoCabecera.TransId

                SendData.instance.accDocEntryDoc = pagoCabecera.AccDocEntry

            } catch (e: Exception){
                e.printStackTrace()
            }

        }

        //Cantidad de Pagos
        cobranzaViewModel.dataDuplicateAllPagoDetallesParaActualizacion.observe(this){
            if (it){
                cobranzaViewModel.getAllPagosDetalleParaEditar(intent.getStringExtra("accDocEntry").toString())
            }
        }


            //Click Pagos
        binding.clNuevaCobranzaPagos.setOnClickListener {
            Intent(this, EditarCobranzaContenidoActivity::class.java)
                .putExtra("accDocEntry", intent.getStringExtra("accDocEntry").toString())
                .putExtra("cardCode", binding.txvNuevaCobranzaCodigoSocioNegocioValue.text.toString())
                .also { startForCobranzaDetalleResult.launch(it) }
        }



        //LiveData Pagos Detalle para editar
        cobranzaViewModel.dataGetAllPagosDetalleParaEditar.observe(this){ listaPagosDetalle->
            try {
                binding.txvNuevaCobranzaPagosValue.text = "${listaPagosDetalle.size} pagos"
                val total = listaPagosDetalle.sumOf { it.SumApplied }.format(2)
                binding.txvNuevaCobranzaMontoTotalValue.text = "${SendData.instance.simboloMoneda} $total"
            } catch (e: Exception){
                e.printStackTrace()
            }
        }


        //LiveData Cierre de sesion
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

    private fun setValoresIniciales() {
        //Datos default del usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            try {
                infoCobranza["codigoUsuario"] = usuario.Code
                infoCobranza["docCur"] = usuario.DefaultCurrency
                infoCobranza["numeroSeriePago"] = usuario.DefaultPagoRSeries
                infoCobranza["cashAccount"] = usuario.DefaultAcctCodeEf
                infoCobranza["numeroReferencia"] = ""
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        //set Monedas
        generalViewModel.getAllGeneralMonedas()
        generalViewModel.dataGetAllGeneralMonedas.observe(this) { listaMonedas ->
            try {
                SendData.instance.simboloMoneda = listaMonedas.firstOrNull()?.CurrName?:"S/"
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

    private fun asignarTipoDePago(pagoCabecera: DoClientePago) {
        val tipoPago = when(pagoCabecera.TypePayment){
            "E" ->{"Efectivo"}
            "C" ->{"Cheque"}
            "T" ->{"Transferencia"}
            "D" ->{"Depósito"}
            else -> ""
        }
        prefs.saveTipoPago(tipoPago)

        when (pagoCabecera.TypePayment){
            "E"-> {
                binding.txvNuevaCobranzaMontoTotalValue.text = pagoCabecera.CashSum.toString()
                binding.txvNuevaCobranzaTipoPagoValue.text = "Efectivo"
                prefs.saveCuentaPago(pagoCabecera.CashAcct)
            }
            "C"-> {
                binding.txvNuevaCobranzaMontoTotalValue.text = pagoCabecera.CheckSum.toString()
                binding.txvNuevaCobranzaTipoPagoValue.text = "Cheque"
                prefs.saveCuentaPago(pagoCabecera.CheckAct)
                prefs.saveBancoCheque(pagoCabecera.BankCode)
                prefs.saveNumeroCheque(pagoCabecera.CheckNum)
            }
            "T"-> {
                binding.txvNuevaCobranzaMontoTotalValue.text = pagoCabecera.TrsfrSum.toString()
                binding.txvNuevaCobranzaTipoPagoValue.text = "Transferencia"
                prefs.saveCuentaPago(pagoCabecera.TrsfrAcct)
                prefs.saveReferenciaTransfer(pagoCabecera.TrsfrRef)
            }

            "D"-> {
                binding.txvNuevaCobranzaMontoTotalValue.text = pagoCabecera.TrsfrSum.toString()
                binding.txvNuevaCobranzaTipoPagoValue.text = "Depósito"
                prefs.saveCuentaPago(pagoCabecera.TrsfrAcct)
                prefs.saveReferenciaTransfer(pagoCabecera.TrsfrRef)
            }
        }

    }

    private fun setDefaultUi() {
        setCopyToClipboardOnLongClick(
            binding.clNuevaCobranzaComentarios,
            binding.txvNuevaCobranzaComentariosValue,
            this
        )


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

        //Click Pagos
        binding.clNuevaCobranzaPagos.setOnClickListener {
            Intent(this, ContenidoCobranzaDetalleActivity::class.java)
                .putExtra("cardCode", binding.txvNuevaCobranzaCodigoSocioNegocioValue.text.toString())
                .also { startForCobranzaDetalleResult.launch(it) }
        }


        //EDIT CL del Codigo del Socio de Negocio
        binding.clNuevaCobranzaCodigoSocioNegocio.isClickable = false
        binding.txvNuevaCobranzaCodigoSocioNegocioValue.alpha = 0.8F
        binding.txvNuevaCobranzaCodigoSocioNegocioTitle.alpha = 0.8F


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


    val startForCobranzaDetalleResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            cobranzaViewModel.getAllPagosDetalleParaEditar(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())
        }
        if (result.resultCode == Activity.RESULT_CANCELED){
            cobranzaViewModel.getAllPagosDetalleParaEditar(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())
        }
    }

    val startForTipoCobranzaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            binding.txvNuevaCobranzaTipoPagoValue.text = MassiveApp.prefs.getTipoPago()
        }
    }




    fun getAllPagosDetallePorAccDocEntry(accDocEntry: String): List<ClientePagosDetalle> =
        try {
            var result = emptyList<DoClientePagoDetalle>()
            cobranzaViewModel.getAllPagosDetallePorAccDocEntry(accDocEntry)
            cobranzaViewModel.dataGetAllPagosDetallePorAccDocEntry.observe(this){ result = it}
            result.map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
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



    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }








    /*------------------NAVBAR-----------------------*/


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("¿Se eliminaran todos los cambios realizados, seguro que desea continuar?")
            .setPositiveButton("Aceptar"){ _, _ ->
                cobranzaViewModel.deleteAllPagoDetalleParaEditar(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())
                cobranzaViewModel.dataDeleteAllPagoDetalleParaEditar.observe(this){ success->
                    if(success){
                        setResult(RESULT_CANCELED)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_check)?.setIcon(R.drawable.icon_double_check)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_check->{
                cobranzaViewModel.updateAllPagosDetalle(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())

                asignarVariables()

                cobranzaViewModel.dataUpdateAllPagosDetalle.observe(this) { updatePagosDetalle ->
                    if (updatePagosDetalle) {
                        cobranzaViewModel.getAllPagosDetallePorAccDocEntry(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())

                        cobranzaViewModel.dataGetAllPagosDetallePorAccDocEntry.observe(this){ pagosDetalle->

                            cobranzaViewModel.saveCobranzaCabecera(
                                actualizarPagoCabecera(
                                    accDocEntry = binding.txvNuevoPedidoNumeroDocumentoValue.text.toString(),
                                    cuentaCash = CUENTA_PAGO_CASH,
                                    accNotificado =     infoCobranza["accNotificado"] as String,
                                    transId =           infoCobranza["transId"] as Int,
                                    cardCode = binding.txvNuevaCobranzaCodigoSocioNegocioValue.text.toString(),
                                    cardName = binding.txvNuevaCobranzaNombreSocioNegocioValue.text.toString(),
                                    montoEfectivo = MONTO_PAGO_CASH,
                                    comentarios = binding.txvNuevaCobranzaComentariosValue.text.toString(),
                                    glosa = binding.txvNuevaCobranzaGlosaValue.text.toString(),
                                    moneda = infoCobranza["docCur"] as String,
                                    usuario = infoCobranza["codigoUsuario"] as String,
                                    numeroReferenciaTransferencia = NUMERO_REFERENCIA_TRANSFERENCIA,
                                    numeroDeSeriePago = infoCobranza["numeroSeriePago"] as Int,
                                    montoTotalTransferencia = MONTO_PAGO_TRANSFERENCIA,
                                    transferenciaCuenta = CUENTA_PAGO_TRANSFERENCIA,
                                    fechaTranferencia = FECHA_TRANSFERENCIA,
                                    typePayment = TIPO_PAGO,
                                    CheckAct = CUENTA_PAGO_CHEQUE,
                                    BankCode = CODIGO_BANCO,
                                    DueDate = FECHA_VENCIMIENTO_CHEQUE,
                                    CheckSum = MONTO_PAGO_CHEQUE,
                                    CheckNum = NUMERO_CHEQUE,
                                    listaDetalles = pagosDetalle.map { it.toModel() },
                                    accAction = "U",
                                    fechaCreacion = infoCobranza["fechaCreacion"] as String,
                                    horaCreacion = infoCobranza["horaCreacion"] as String,
                                    fechaEdicion = getFechaActual(),
                                    horaEdicion = getHoraActual(),
                                    taxDate = infoCobranza["taxDate"] as String,
                                    docNum = infoCobranza["docNum"] as Int,
                                    docEntry = infoCobranza["docEntry"] as Int
                                ), this
                            )

                        }

                    }

                    cobranzaViewModel.dataSaveCobranzaCabeceraUseCase.observe(this) { response ->
                        when(response.ErrorCodigo){
                            500 ->{
                                BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                                    //Aceptar
                                    usuarioViewModel.logOut()
                                }
                            }
                            0 -> {
                                showMessage(response.ErrorMensaje)
                                setResult(RESULT_OK, Intent()
                                    .putExtra("accDocEntry", binding.txvNuevoPedidoNumeroDocumentoValue.text.toString()))
                                onBackPressedDispatcher.onBackPressed()
                            }
                            else -> { showMessage(response.ErrorMensaje) }
                        }
                    }
                }
            }

            android.R.id.home->{
                val builder = AlertDialog.Builder(this)
                builder
                    .setTitle("¿Se eliminaran todos los cambios realizados, seguro que desea continuar?")
                    .setPositiveButton("Aceptar"){ _, _ ->
                        cobranzaViewModel.deleteAllPagoDetalleParaEditar(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())
                        cobranzaViewModel.dataDeleteAllPagoDetalleParaEditar.observe(this){ success->
                            if(success){
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

        }
        return super.onOptionsItemSelected(item)
    }
}








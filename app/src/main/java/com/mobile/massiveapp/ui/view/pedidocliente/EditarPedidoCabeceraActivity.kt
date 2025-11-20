package com.mobile.massiveapp.ui.view.pedidocliente

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
import com.mobile.massiveapp.MassiveApp.Companion.prefsPedido
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.databinding.ActivityEditarPedidoCabeceraBinding
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdt
import com.mobile.massiveapp.ui.base.BaseDialogEdtCharacterLimit
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.actualizarCabeceraDePedido
import com.mobile.massiveapp.ui.view.util.formatoSinModena
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.view.util.obtenerTipoDocumentoIndicadorPorCodigo
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class EditarPedidoCabeceraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPedidoCabeceraBinding
    private lateinit var loadingDialog: BaseDialogLoadingCustom
    private val socioViewModel: SocioViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var datosPedido: HashMap<String, Any> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPedidoCabeceraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setDefaultUi()


        //Get Pedido Cabecera Actual
        pedidoViewModel.getPedidoPorAccDocEntry(intent.getStringExtra("accDocEntry").toString())
        pedidoViewModel.dataGetPedidoPorAccDocEntry.observe(this){ pedidoCabecera->
            try {
                datosPedido["accDocEntrySn"] = pedidoCabecera.AccDocEntrySN
                datosPedido["fechaCreacion"] = pedidoCabecera.DocDate
                datosPedido["horaCreacion"] = pedidoCabecera.AccCreateHour
                datosPedido["docEntry"] = pedidoCabecera.DocEntry
                datosPedido["docNum"] = pedidoCabecera.DocNum
                datosPedido["groupNum"] = pedidoCabecera.GroupNum
                datosPedido["licTradNum"] = pedidoCabecera.LicTradNum
                datosPedido["accNotificado"] = pedidoCabecera.AccNotificado

                binding.txvEditarPedidoCodigoSnValue.text = pedidoCabecera.CardCode
                binding.txvEditarPedidoNombreSnValue.text = pedidoCabecera.CardName
                binding.txvEditarPedidoIndicadorValue.text = obtenerTipoDocumentoIndicadorPorCodigo(pedidoCabecera.Indicator)

                binding.txvEditarPedidoMonedaValue.text = pedidoCabecera.DocCur
                binding.txvEditarPedidoDireccionEntregaValue.text = pedidoCabecera.Address2
                binding.txvEditarPedidoComentariosValue.text = pedidoCabecera.Comments

                generalViewModel.getCondicionPorGroupNum(pedidoCabecera.GroupNum)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        setValoresIniciales()
        duplicarDetallesParaActualizar()

        //detalles del pedido a editar
        pedidoViewModel.dataGetAllPedidoDetallePorAccDocEntry.observe(this){ listaPedidoDetalles->
            try {
                val total = listaPedidoDetalles.sumOf { it.LineTotal }.toString().format(2)
                binding.txvEditarPedidoArticulosValue.text = "${listaPedidoDetalles.size} artículos"
                binding.txvEditarPedidoTotalAntesDescuentoValue.text = "${SendData.instance.simboloMoneda} ${total.format(2)}"
                binding.txvEditarPedidoTotalValue.text = "${SendData.instance.simboloMoneda} ${total.format(2)}"
                binding.txvEditarPedidoPorcentajeDescuentoValue.text = "0.00"
                binding.txvEditarPedidoImpuestoValue.text = "0.0"
                binding.txvEditarPedidoDescuentoValue.text = "0.00"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        //LiveData Condiciones Socio
        generalViewModel.dataGetCondicionesSocio.observe(this){ condicionesSocio->
            BaseDialogChecklistWithId(
                binding.txvEditarPedidoCondicionPagoValue.text.toString(),
                condicionesSocio.map { condicion -> condicion.PymntGroup }
            ){ condicionSeleccionada , id ->
                if (condicionSeleccionada.isNotEmpty()){
                    binding.txvEditarPedidoCondicionPagoValue.text = condicionSeleccionada
                    datosPedido["groupNum"] = condicionesSocio[id].GroupNum
                }
            }.show(supportFragmentManager, "CondicionesDialog")
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
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun duplicarDetallesParaActualizar() {
        //Se duplican los detalles para la actualizacion
        pedidoViewModel.duplicatePedidoDetallesParaActualizacion(intent.getStringExtra("accDocEntry").toString())
        pedidoViewModel.dataDuplicatePedidoDetallesParaActualizacion.observe(this){
            if (it){
                pedidoViewModel.getAllPedidoDetallesParaEditar(intent.getStringExtra("accDocEntry").toString())
            }
        }
    }

    private fun setValoresIniciales() {

        //Setear Fecha actual
        val calendar = Calendar.getInstance()
        val dateFormatDoc = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val formatteddateDoc = dateFormatDoc.format(calendar.time)
        binding.txvEditarPedidoFechaContableValue.text = getFechaActual()
        binding.txvEditarPedidoFechaVencimientoValue.text = getFechaActual()


        //Se obtienen los datos del usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            try {
                datosPedido["slpCode"] = usuario.DefaultSlpCode
                datosPedido["codigoUsuario"] = usuario.Code
                datosPedido["docCur"] = usuario.DefaultCurrency
                datosPedido["numeroSeriePedido"] = usuario.DefaultOrderSeries
                datosPedido["priceList"] = usuario.DefaultPriceList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //AccDocEntry a editar
        binding.txvEditarPedidoNumeroDocumentoValue.text = intent.getStringExtra("accDocEntry").toString()

        //Monedas
        generalViewModel.getAllGeneralMonedas()
        generalViewModel.dataGetAllGeneralMonedas.observe(this){ monedas->
            try {
                SendData.instance.simboloMoneda = monedas[0].CurrName
            } catch (e: Exception){
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        //Set Empleado de departamento de ventas
        generalViewModel.getEmpleadoDeVentas()
        generalViewModel.dataGetEmpleadoDeVemtas.observe(this) { empleadoDeVenta->
            try {
                binding.txvEditarPedidoEmpleadoVentasValue.text = empleadoDeVenta.SlpName
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



        //DIRECCIONES
        socioViewModel.getSocioDireccionesPorCardCodeYTipo(intent.getStringExtra("cardCode").toString(), "B")
        socioViewModel.dataSocioDireccionesPorCardCodeYTipo.observe(this) { direccionesFiscales->
            try {
                if (direccionesFiscales.isNotEmpty()){
                    binding.txvEditarPedidoDireccionFiscalValue.text = direccionesFiscales[0].Street
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //btn direcciones fiscales
            binding.clEditarPedidoDireccionFiscal.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvEditarPedidoDireccionFiscalValue.text.toString(),
                    direccionesFiscales.map { direccion -> direccion.Street }
                ){direccionSeleccionada, id->
                    if (direccionSeleccionada.isNotEmpty()){
                        binding.txvEditarPedidoDireccionFiscalValue.text = direccionSeleccionada
                    }
                }.show(supportFragmentManager, "BaseDialogChecklist")
            }
        }

        //CONTACTOS
        socioViewModel.getSocioContactoPorCardCode(intent.getStringExtra("cardCode").toString())
        socioViewModel.dataSocioContactoPorCardCode.observe(this) { socioContactos->
            try {
                if (socioContactos.isNotEmpty()){
                    binding.txvEditarPedidoContactoValue.text = socioContactos[0].Name
                    datosPedido["cntctCode"] = socioContactos[0].CntctCode
                }
            } catch (e: Exception) {
                e.printStackTrace()

                binding.clEditarPedidoContacto.setOnClickListener {
                    BaseDialogChecklistWithId(
                        binding.txvEditarPedidoContactoValue.text.toString(),
                        socioContactos.map { contacto -> contacto.Name }
                    ){contactoSeleccionado, id->
                        if (contactoSeleccionado.isNotEmpty()){
                            binding.txvEditarPedidoContactoValue.text = contactoSeleccionado
                            datosPedido["cntctCode"] = socioContactos[id].CntctCode
                        }
                    }.show(supportFragmentManager, "BaseDialogChecklist")
                }
            }
        }

        //Set Condicion de pago
        generalViewModel.dataGetCondicionPorGroupNum.observe(this){ condicionDePago->
            try {
                binding.txvEditarPedidoCondicionPagoValue.text = condicionDePago.PymntGroup
                datosPedido["groupNum"] = condicionDePago.GroupNum
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setDefaultUi() {
        //Loading Save crobranza
        val gif = GifDrawable(this.resources, R.drawable.gif_loading)
        loadingDialog = BaseDialogLoadingCustom(this, "Enviando Pedido...", gif)

        pedidoViewModel.isLoadingSavePedido.observe(this){
            if (it){
                loadingDialog.startLoading()
            } else {
                loadingDialog.onDismiss()
            }
        }

        // Click Codigo SN
        binding.clEditarPedidoCodigoSn.setOnClickListener {
            /*startForClienteResult.launch(Intent(this, BuscarClienteActivity::class.java))*/
        }

        // Click Articulos
        binding.clPedidoEditarArticulos.setOnClickListener {
            prefsPedido.saveCardName(binding.txvEditarPedidoCodigoSnValue.text.toString())
            prefsPedido.saveAccDocEntry(binding.txvEditarPedidoNumeroDocumentoValue.text.toString())
            Intent(this, ContenidoArticulosActivity::class.java)
                .putExtra("accDocEntry", binding.txvEditarPedidoNumeroDocumentoValue.text.toString())
                .putExtra("editMode", true)
                .also { startForArticulosResult.launch( it ) }

        }

        //Click Comentarios
        binding.clNuecoPedidoComentarios.setOnClickListener {
            BaseDialogEdtCharacterLimit(
                binding.txvEditarPedidoComentariosValue.text.toString(),
                "Ingrese el comentario"
            ){ comentario->
                binding.txvEditarPedidoComentariosValue.text = comentario
            }.show(supportFragmentManager, "BaseDialogEdt")
        }

        //Click en Referencia
        binding.clEditarPedidoReferencia.setOnClickListener {
            BaseDialogEdt(
                binding.txvEditarPedidoReferenciaValue.text.toString()
            ){ referencia->
                binding.txvEditarPedidoReferenciaValue.text = referencia
            }.show(supportFragmentManager, "BaseDialogEdt")
        }

        // Click DocumentoDireccion de entrega
        binding.clEditarPedidoDireccionEntrega.setOnClickListener {
            //Se configura el Dialog de edicion
            BaseDialogEdt(
                binding.txvEditarPedidoDireccionEntregaValue.text.toString()
            ){ direccionDespachoDialog->
                binding.txvEditarPedidoDireccionEntregaValue.text = direccionDespachoDialog
            }.show(supportFragmentManager, "BaseDialogEdt")
        }

        //Click Condicion de pago
        binding.clEditarPedidoCondicionPago.setOnClickListener {
            val cardCode = binding.txvEditarPedidoCodigoSnValue.text.toString()
            if (cardCode.isNotEmpty()){
                generalViewModel.getCondicionesSocio(cardCode)
            }
        }
    }


    /*------------START FOR RESULT - Variables de manejo de retorno de información-------------*/


    //------Retorno de los ARTICULOS agregados-----
    val startForArticulosResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val cantidadArticulos = data?.getIntExtra("cantidadArticulos", 0)
            val total = data?.getStringExtra("total")
            if (cantidadArticulos != null) {
                if (cantidadArticulos > 0){
                    binding.txvEditarPedidoTotalAntesDescuentoValue.text = total
                    binding.txvEditarPedidoTotalValue.text = total
                    binding.txvEditarPedidoPorcentajeDescuentoValue.text = "0.00"
                    binding.txvEditarPedidoImpuestoValue.text = "0.0"
                    binding.txvEditarPedidoDescuentoValue.text = "0.00"

                }    //Get todos los detalles del pedido
                pedidoViewModel.getAllPedidoDetallesParaEditar(binding.txvEditarPedidoNumeroDocumentoValue.text.toString())

            }
        }
    }




    fun getAllPedidoDetallePorAccDocEntry(accDocEntry: String): List<ClientePedidoDetalle> =
        try {
            var result = emptyList<ClientePedidoDetalle>()
            pedidoViewModel.getAllPedidoDetallePorAccDocEntry(accDocEntry)
            pedidoViewModel.dataGetAllPedidoDetallePorAccDocEntry.observe(this){ result = it}
            result
        } catch (e: Exception) {
            emptyList()
        }


















    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onBackPressed() {
        setResult(
            Activity.RESULT_OK, Intent()
            .putExtra("success", true))
        super.onBackPressed()
    }



    //Menu de la barra de titulo
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo_sn, menu)

        //Se setea el icono de check a doble check
        val iconCheck = menu?.findItem(R.id.app_bar_check)
        iconCheck?.setIcon(R.drawable.icon_double_check)

        //Se oculta el icono que indica si hay conexión o no
        val item = menu?.findItem(R.id.app_bar_connectivity_status)
        item?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    //Se decide que hace cada boton del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                val builder = AlertDialog.Builder(this)
                builder
                    .setTitle("¿Seguro que desea eliminar los detalles de este pedido?")
                    .setPositiveButton("Aceptar"){ _, _ ->
                        pedidoViewModel.deleteAllPedidoDetallesParaEditar(binding.txvEditarPedidoNumeroDocumentoValue.text.toString())
                        pedidoViewModel.dataDeleteAllPedidoDetallesParaEditar.observe(this){
                            if(it){
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

            R.id.app_bar_check -> {
                pedidoViewModel.updateAllPedidosDetalle(binding.txvEditarPedidoNumeroDocumentoValue.text.toString())
                pedidoViewModel.dataUpdateAllPedidosDetalle.observeOnce(this){ updatePedidoDetalles->
                    if (updatePedidoDetalles){
                        try {
                            pedidoViewModel.getAllPedidoDetallePorAccDocEntry(binding.txvEditarPedidoNumeroDocumentoValue.text.toString())
                            pedidoViewModel.dataGetAllPedidoDetallePorAccDocEntry.observeOnce(this){ pedidosDetalles->
                                pedidoViewModel.savePedidoCabecera(
                                    actualizarCabeceraDePedido(
                                        accDocEntry =       binding.txvEditarPedidoNumeroDocumentoValue.text.toString(),
                                        accNotificado =     datosPedido["accNotificado"] as String   ,
                                        cardCode =          binding.txvEditarPedidoCodigoSnValue.text.toString(),
                                        cardName =          binding.txvEditarPedidoNombreSnValue.text.toString().replace("\n", " "),
                                        docDate =           binding.txvEditarPedidoFechaContableValue.text.toString(),
                                        docDueDate =        binding.txvEditarPedidoFechaVencimientoValue.text.toString(),
                                        direccionDespacho = binding.txvEditarPedidoDireccionEntregaValue.text.toString().replace("\n", " "),
                                        valorVenta =        binding.txvEditarPedidoTotalValue.text.toString().formatoSinModena(SendData.instance.simboloMoneda),
                                        impuestoTotal =     binding.txvEditarPedidoImpuestoValue.text.toString().formatoSinModena(SendData.instance.simboloMoneda),
                                        total =             binding.txvEditarPedidoTotalValue.text.toString().formatoSinModena(SendData.instance.simboloMoneda),
                                        comentario =        binding.txvEditarPedidoComentariosValue.text.toString(),
                                        codigoUsuario =     datosPedido["codigoUsuario"] as String,     //Se necesita traer del valor default del Usuario
                                        fechaCreacion =     datosPedido["fechaCreacion"] as String,
                                        horaCreacion =      datosPedido["horaCreacion"] as String,
                                        numeroSeriePedido = datosPedido["numeroSeriePedido"] as Int,
                                        slpCode =           datosPedido["slpCode"] as Int,
                                        docCur =            datosPedido["docCur"] as String,
                                        payToCode =         "FISCAL",
                                        shipToCode =        "DESPACHO",
                                        indicator =         binding.txvEditarPedidoIndicadorValue.text.toString().take(2),
                                        groupNum =          datosPedido["groupNum"] as Int,
                                        priceList =         datosPedido["priceList"] as Int,              //Se necesita traer del valor default del Usuario
                                        licTradNum =        datosPedido["licTradNum"] as String,
                                        cntctCode =         datosPedido["cntctCode"] as? Int?: 0,            //Se necesita traer desde documentoContactos
                                        taxDate =           binding.txvEditarPedidoFechaContableValue.text.toString(),
                                        direccionFiscal =   binding.txvEditarPedidoDireccionFiscalValue.text.toString().replace("\n", " "),
                                        horaActualizacion = getHoraActual(),
                                        fechaActualizacion = getFechaActual(),
                                        docEntry =          datosPedido["docEntry"] as Int,
                                        docNum =            datosPedido["docNum"] as Int,
                                        accDocEntrySN =     datosPedido["accDocEntrySn"] as String,
                                        detallePedido =     pedidosDetalles
                                    )
                                )
                            }

                        } catch (e:Exception){
                            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }

                        pedidoViewModel.dataSavePedidoCabecera.observe(this){ response->
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
                                        .putExtra("success", true))
                                    onBackPressedDispatcher.onBackPressed()
                                }
                                else -> { showMessage(response.ErrorMensaje) }
                            }
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
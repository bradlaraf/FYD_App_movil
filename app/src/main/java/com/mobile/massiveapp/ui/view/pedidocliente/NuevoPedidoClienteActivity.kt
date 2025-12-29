package com.mobile.massiveapp.ui.view.pedidocliente

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.mobile.massiveapp.MassiveApp.Companion.prefsPedido
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityNuevoPedidoClienteBinding
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdt
import com.mobile.massiveapp.ui.base.BaseDialogEdtCharacterLimit
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.agregarCabeceraDePedido
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.formatoSinModena
import com.mobile.massiveapp.ui.view.util.getCodigoDeDocumentoActual
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import com.mobile.massiveapp.ui.view.util.mostrarCalendarioMaterial
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.view.util.obtenerTipoDocumentoIndicadorPorCodigo
import com.mobile.massiveapp.ui.view.util.setCopyToClipboardOnLongClick
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioDireccionesViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable

@AndroidEntryPoint
class NuevoPedidoClienteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevoPedidoClienteBinding
    private val socioViewModel: SocioViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val socioDireccionesViewModel: SocioDireccionesViewModel by viewModels()
    private var datosPedido: HashMap<String, Any> = HashMap()
    private val CANTIDAD_ARTICULOS_DEFAULT = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoPedidoClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDefaultUi()
        setValoresIniciales()



            //LiveData del Cliente Seleccionado
        socioViewModel.dataSocioNegocioPorCardCode.observe(this) { socioSeleccionado->
            try {
                prefsPedido.saveCardCode(socioSeleccionado.CardCode)

                setCondicionPagoDefault(socioSeleccionado.CardCode)
                datosPedido["licTradNum"] = socioSeleccionado.LicTradNum
                datosPedido["groupNum"] = socioSeleccionado.GroupNum
                datosPedido["accDocEntrySN"] = socioSeleccionado.AccDocEntry

                binding.txvNuevoPedidoCorreoValue.text = socioSeleccionado.E_Mail
                binding.txvNuevoPedidoCodigoSnValue.text = socioSeleccionado.CardCode
                binding.txvNuevoPedidoNombreSnValue.text = socioSeleccionado.CardName
                binding.txvNuevoPedidoIndicadorValue.text = obtenerTipoDocumentoIndicadorPorCodigo(socioSeleccionado.Indicator)
            } catch (e: Exception){
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        //Direccion Despacho Predeterminada
        socioDireccionesViewModel.dataGetDireccionesDespachoXZona.observe(this) { direccionDespacho->
            try {
                if (direccionDespacho.isNotEmpty()){
                    binding.txvNuevoPedidoDireccionEntregaValue.text = direccionDespacho.toString()
                    datosPedido["shipToCode"] = direccionDespacho
                }
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }

        //Livedata todas las direcciones
        socioViewModel.dataSocioDireccionesPorCardCode.observe(this){ direcciones->
            if (direcciones.isNotEmpty()){
                try {
                    val direccionesFiscales = direcciones.filter { it.AdresType == "B" }
                    val direccionesDespacho = direcciones.filter { it.AdresType == "S" }

                    if (direccionesFiscales.isNotEmpty()){
                        binding.txvNuevoPedidoDireccionFiscalValue.text = direccionesFiscales[0].Street
                        datosPedido["payToCode"] = direccionesFiscales[0].Address
                    }

                    if (direccionesDespacho.isNotEmpty()){
                        /*binding.txvNuevoPedidoDireccionEntregaValue.text = direccionesDespacho[0].Street
                        datosPedido["shipToCode"] = direccionesDespacho[0].Address*/
                    }

                    //Direcciones fiscales
                    binding.clNuevoPedidoDireccionFiscal.setOnClickListener {
                        BaseDialogChecklistWithId(
                            binding.txvNuevoPedidoDireccionFiscalValue.text.toString(),
                            direccionesFiscales.map { direccion -> direccion.Street }
                        ){ direccionSeleccionada , id ->
                            if (direccionSeleccionada.isNotEmpty()){
                                binding.txvNuevoPedidoDireccionFiscalValue.text = direccionSeleccionada
                                datosPedido["payToCode"] = direccionesFiscales[id].Address
                            }
                        }.show(supportFragmentManager, "DireccionFiscalDialog")
                    }

                    //Direcciones despacho
                    binding.clNuevoPedidoDireccionEntrega.setOnClickListener {
                        BaseDialogChecklistWithId(
                            binding.txvNuevoPedidoDireccionEntregaValue.text.toString(),
                            direccionesDespacho.map { direccion -> direccion.Street }
                        ){ direccionSeleccionada , id ->
                            if (direccionSeleccionada.isNotEmpty()){
                                binding.txvNuevoPedidoDireccionEntregaValue.text = direccionSeleccionada
                                datosPedido["shipToCode"] = direccionesDespacho[id].Address
                            }
                        }.show(supportFragmentManager, "DireccionFiscalDialog")
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }



            //LiveData de los CONTACTOS del cliente seleccionado
        socioViewModel.dataSocioContactoPorCardCode.observe(this) { socioContactos->
            try {
                if (socioContactos.isNotEmpty()){
                    binding.txvNuevoPedidoContactoValue.text = socioContactos[0].Name
                    datosPedido["cntctCode"] = socioContactos[0].CntctCode
                }
                binding.clNuevoPedidoContacto.setOnClickListener {
                    BaseDialogChecklistWithId(
                        binding.txvNuevoPedidoContactoValue.text.toString(),
                        socioContactos.map { contacto -> contacto.Name }
                    ){contactoSeleccionado, id->
                        if (contactoSeleccionado.isNotEmpty()){
                            binding.txvNuevoPedidoContactoValue.text = contactoSeleccionado
                            datosPedido["cntctCode"] = socioContactos[id].CntctCode
                        }
                    }.show(supportFragmentManager, "BaseDialogChecklist")
                }
            } catch (e: Exception){
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }


            //LiveData pedidosDetalle
        pedidoViewModel.dataGetAllPedidoDetallePorAccDocEntry.observe(this){ listaPedidoDetalles->
            try {
                val total = listaPedidoDetalles.sumOf { it.LineTotal }.toString().format(2)
                val gTotal = listaPedidoDetalles.sumOf { it.GTotal }.toString().format(2)
                val vatSum = listaPedidoDetalles.sumOf { it.VatSum.format(2) }.toString().format(2)

                binding.txvNuevoPedidoArticulosValue.text = "${listaPedidoDetalles.size} artículos"

                binding.txvNuevoPedidoTotalAntesDescuentoValue.text = "${SendData.instance.simboloMoneda} ${total.format(2)}"
                binding.txvNuevoPedidoTotalValue.text = "${SendData.instance.simboloMoneda} ${gTotal.format(2)}"

                binding.txvNuevoPedidoPorcentajeDescuentoValue.text = "0.00"
                binding.txvNuevoPedidoImpuestoValue.text = vatSum
                binding.txvNuevoPedidoDescuentoValue.text = "0.00"

            } catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

            //LiveData Condiciones Socio
        generalViewModel.dataGetCondicionesSocio.observe(this){ condicionesSocio->
            BaseDialogChecklistWithId(
                binding.txvNuevoPedidoCondicionPagoValue.text.toString(),
                condicionesSocio.map { condicion -> condicion.PymntGroup }
            ){ condicionSeleccionada , id ->
                if (condicionSeleccionada.isNotEmpty()){
                    binding.txvNuevoPedidoCondicionPagoValue.text = condicionSeleccionada
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

    private fun setCondicionPagoDefault(cardCode: String) {
        //Set Condicion de pago
        generalViewModel.getCondicionDePagoDefault(cardCode)
        generalViewModel.dataGetCondicionDePagoDefault.observeOnce(this){ condicionDePagoDefault->
            try {
                binding.txvNuevoPedidoCondicionPagoValue.text = condicionDePagoDefault.PymntGroup
                datosPedido["groupNum"] = condicionDePagoDefault.GroupNum
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun setDefaultUi() {
        binding.clNuevoPedidoNumeroDocumento.isVisible = false
        binding.clNuevoPedidoMoneda.isVisible = false


        setCopyToClipboardOnLongClick(
            binding.clNuecoPedidoComentarios,
            binding.txvNuevoPedidoComentariosValue,
            this
        )

        //Loading Save crobranza
        val gif = GifDrawable(this.resources, R.drawable.gif_loading)
        val loadingDialog = BaseDialogLoadingCustom(this, "Enviando Pedido...", gif)
        pedidoViewModel.isLoadingSavePedido.observe(this){
            if (it){
                loadingDialog.startLoading()
            } else {
                loadingDialog.onDismiss()
            }
        }


        // Click Codigo SN
        binding.clNuevoPedidoCodigoSn.setOnClickListener {
            startForClienteResult.launch(Intent(this, BuscarClienteActivity::class.java))
        }


        // Click Articulos
        binding.clPedidoNuevoArticulos.setOnClickListener {
            prefsPedido.saveCardName(binding.txvNuevoPedidoCodigoSnValue.text.toString())
            prefsPedido.saveAccDocEntry(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())

            val intentPedidoArticulo = Intent(this, ContenidoArticulosActivity::class.java)
            intentPedidoArticulo.putExtra("accDocEntry", binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())

            startForArticulosResult.launch( intentPedidoArticulo )
        }


        //Click Comentarios
        binding.clNuecoPedidoComentarios.setOnClickListener {
            BaseDialogEdtCharacterLimit(
                binding.txvNuevoPedidoComentariosValue.text.toString(),
                "Ingrese el comentario"
            ){ comentario->
                binding.txvNuevoPedidoComentariosValue.text = comentario
            }.show(supportFragmentManager, "BaseDialogEdt")
        }


        //Click en Referencia
        binding.clNuevoPedidoReferencia.setOnClickListener {
            BaseDialogEdt(
                binding.txvNuevoPedidoReferenciaValue.text.toString()
            ){ referencia->
                binding.txvNuevoPedidoReferenciaValue.text = referencia
            }.show(supportFragmentManager, "BaseDialogEdt")
        }

        // Click DocumentoDireccion de entrega
        /*binding.clNuevoPedidoDireccionEntrega.setOnClickListener {
            //Se configura el Dialog de edicion
            BaseDialogEdt(
                binding.txvNuevoPedidoDireccionEntregaValue.text.toString()
            ){ direccionDespachoDialog->
                binding.txvNuevoPedidoDireccionEntregaValue.text = direccionDespachoDialog
            }.show(supportFragmentManager, "BaseDialogEdt")
        }*/

        //Fecha Entrega
        binding.clNuevoPedidoFechaEntrega.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mostrarCalendarioMaterial(
                    this,
                    binding.txvNuevoPedidoFechaEntregaValue.text.toString().ifEmpty { getFechaActual() }
                ){dia, mes, anio->
                    binding.txvNuevoPedidoFechaEntregaValue.text = "$anio-$mes-$dia"
                }
            }
        }

        //Click Condicion de pago
        /*binding.clNuevoPedidoCondicionPago.setOnClickListener {
            val cardCode = binding.txvNuevoPedidoCodigoSnValue.text.toString()
            if (cardCode.isNotEmpty()){
                generalViewModel.getCondicionesSocio(cardCode)
            }
        }*/
    }

    private fun setValoresIniciales() {

        binding.txvNuevoPedidoArticulosValue.text = "$CANTIDAD_ARTICULOS_DEFAULT artículos"

        //Setear Fecha actual
        binding.txvNuevoPedidoFechaContableValue.text = getFechaActual()
        binding.txvNuevoPedidoFechaVencimientoValue.text = getFechaActual()

        //Setea el numero de documento actual
        binding.txvNuevoPedidoNumeroDocumentoValue.text = getCodigoDeDocumentoActual(this)


        //Simbolo de moneda
        generalViewModel.getAllGeneralMonedas()
        generalViewModel.dataGetAllGeneralMonedas.observe(this){ monedas->
            try {
                SendData.instance.simboloMoneda = monedas[0].CurrName
            } catch (e: Exception){
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        //Se obtienen los datos del usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            try {
                datosPedido["slpCode"] = usuario.DefaultSlpCode
                datosPedido["codigoUsuario"] = usuario.Code
                datosPedido["docCur"] = usuario.DefaultCurrency
                datosPedido["numeroSeriePedido"] = usuario.DefaultOrderSeries
                datosPedido["priceList"] = usuario.DefaultPriceList

                binding.txvNuevoPedidoMonedaValue.text = usuario.DefaultCurrency
            } catch (e: Exception){
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        //Set Empleado de departamento de ventas
        generalViewModel.getEmpleadoDeVentas()
        generalViewModel.dataGetEmpleadoDeVemtas.observe(this) { empleadoDeVenta->
            try {
                binding.txvNuevoPedidoEmpleadoVentasValue.text = empleadoDeVenta.SlpName
            } catch (e: Exception){
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }


    /*------------START FOR RESULT - Variables de manejo de retorno de información-------------*/

        //------Retorno del CLIENTE seleccionado-----
    val startForClienteResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val cardCodeClienteSeleccionado = data?.getStringExtra("cardCode")
            if (!cardCodeClienteSeleccionado.isNullOrEmpty()){
                binding.txvNuevoPedidoContactoValue.text = ""
                    //Se trae el cliente seleccionado , las direcciones fiscales y los contactos
                socioViewModel.getSocioNegocioPorCardCode(cardCodeClienteSeleccionado)
                socioViewModel.getSocioDireccionesPorCardCode(cardCodeClienteSeleccionado)
                socioDireccionesViewModel.getDireccionesDespachoXZona(cardCodeClienteSeleccionado)
                socioViewModel.getSocioContactoPorCardCode(cardCodeClienteSeleccionado)
                    //visible el boton de articulo
                binding.clPedidoNuevoArticulos.visibility = android.view.View.VISIBLE

            }
        }
    }

        //------Retorno de los ARTICULOS agregados-----
    val startForArticulosResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val cantidadArticulos = data?.getIntExtra("cantidadArticulos", 0)
            val total = data?.getStringExtra("total")?.format(2) ?: "0.0"
            val gTotal = data?.getStringExtra("gtotal")?.format(2)?: "0.0"
            if (cantidadArticulos != null) {
                if (cantidadArticulos > 0){
                    totalesPedidoVisibles(true)
                    binding.txvNuevoPedidoTotalAntesDescuentoValue.text = total
                    binding.txvNuevoPedidoTotalValue.text = gTotal
                    binding.txvNuevoPedidoPorcentajeDescuentoValue.text = "0.00"
                    //binding.txvNuevoPedidoImpuestoValue.text = "0.0"
                    binding.txvNuevoPedidoDescuentoValue.text = "0.00"

                }    //Get todos los detalles del pedido
                pedidoViewModel.getAllPedidoDetallePorAccDocEntry(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())

            }
        }
    }


    fun totalesPedidoVisibles(visible: Boolean){
        binding.clNuevoPedidoPorcentajeDescuento.isVisible = visible
        binding.clNuevopedidoTotalAntesDescuento.isVisible = visible
        binding.clNuevoPedidoTotal.isVisible = visible
        binding.clNuevoPedidoImpuesto.isVisible = visible
        binding.clNuevoPedidoDescuento.isVisible = visible
    }




    private fun showDialogConfirmBack(){
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("¿Seguro que desea eliminar los detalles de este pedido?")
            .setPositiveButton("Aceptar"){ _, _ ->
                pedidoViewModel.deleteAllPedidoDetallePorAccDocEntry(binding.txvNuevoPedidoNumeroDocumentoValue.text.toString())
                onBackPressedDispatcher.onBackPressed()
            }
            .setNegativeButton("Cancelar"){ _, _ ->

            }

        val dialog = builder.create()
        dialog.show()
    }


    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun validateFields():String{
        var validFor = ""
        if (binding.txvNuevoPedidoFechaEntregaValue.text.isEmpty()) validFor = "Fecha de entrega"
        if (binding.txvNuevoPedidoDireccionEntregaValue.text.isEmpty()) validFor = "Dirección de entrega"
       return validFor
    }



    /*----------------BARRA DE TITULO - NAV -------------------*/

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showDialogConfirmBack()
        setResult(Activity.RESULT_OK, Intent()
            .putExtra("pedidoRegistradoExitosamente", true))
    }



    //Menu de la barra de titulo
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo_sn, menu)

        val iconCheck = menu?.findItem(R.id.app_bar_check)
        iconCheck?.setIcon(R.drawable.icon_double_check)

        val item = menu?.findItem(R.id.app_bar_connectivity_status)
        item?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    //Se decide que hace cada boton del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                showDialogConfirmBack()
            }


            R.id.app_bar_check -> {
                try {
                    val campoAValidar = validateFields()
                    if (campoAValidar.isNotEmpty()){
                        throw Exception("Debe llenar el campo ${campoAValidar}")
                    }
                    pedidoViewModel.dataGetAllPedidoDetallePorAccDocEntry.observe(this){ listaDetalle->
                        try {

                            if (listaDetalle.isNotEmpty()){
                                //Se traen los datos del cliente
                                socioViewModel.getSocioNegocioPorCardCode(binding.txvNuevoPedidoCodigoSnValue.text.toString())

                                pedidoViewModel.savePedidoCabecera(
                                    agregarCabeceraDePedido(
                                        accDocEntry =       binding.txvNuevoPedidoNumeroDocumentoValue.text.toString(),
                                        cardCode =          binding.txvNuevoPedidoCodigoSnValue.text.toString(),
                                        cardName =          binding.txvNuevoPedidoNombreSnValue.text.toString().replace("\n", " "),
                                        docDate =           binding.txvNuevoPedidoFechaContableValue.text.toString(),
                                        docDueDate =        binding.txvNuevoPedidoFechaEntregaValue.text.toString(),
                                        direccionDespacho = binding.txvNuevoPedidoDireccionEntregaValue.text.toString().replace("\n", " "),
                                        valorVenta =        binding.txvNuevoPedidoTotalAntesDescuentoValue.text.toString().formatoSinModena(SendData.instance.simboloMoneda),
                                        impuestoTotal =     binding.txvNuevoPedidoImpuestoValue.text.toString().formatoSinModena(SendData.instance.simboloMoneda),
                                        total =             binding.txvNuevoPedidoTotalValue.text.toString().formatoSinModena(SendData.instance.simboloMoneda),
                                        comentario =        binding.txvNuevoPedidoComentariosValue.text.toString(),
                                        codigoUsuario =     datosPedido["codigoUsuario"] as String,     //Se necesita traer del valor default del Usuario
                                        fechaCreacion =     binding.txvNuevoPedidoFechaContableValue.text.toString(),
                                        horaCreacion =      getHoraActual(),
                                        numeroSeriePedido = datosPedido["numeroSeriePedido"] as Int,
                                        slpCode =           datosPedido["slpCode"] as Int,
                                        docCur =            datosPedido["docCur"] as String,
                                        payToCode =         datosPedido["payToCode"] as String,
                                        shipToCode =        datosPedido["shipToCode"] as String,
                                        indicator =         binding.txvNuevoPedidoIndicadorValue.text.toString().take(2),
                                        groupNum =          datosPedido["groupNum"] as Int,
                                        priceList =         datosPedido["priceList"] as Int,   //Se necesita traer del valor default del Usuario
                                        licTradNum =        datosPedido["licTradNum"] as String,
                                        cntctCode =         datosPedido["cntctCode"] as? Int?: 0,    //Se necesita traer desde documentoContactos
                                        taxDate =           binding.txvNuevoPedidoFechaContableValue.text.toString(),
                                        direccionFiscal =   binding.txvNuevoPedidoDireccionFiscalValue.text.toString().replace("\n", " "),
                                        accDocEntrySN =     datosPedido["accDocEntrySN"] as String,
                                        detallePedido =     listaDetalle
                                    )
                                )
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
                                        setResult(RESULT_OK)
                                        onBackPressedDispatcher.onBackPressed()
                                    }
                                    else -> { showMessage(response.ErrorMensaje) }
                                }
                            }
                        } catch (e: Exception){
                            e.printStackTrace()
                            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
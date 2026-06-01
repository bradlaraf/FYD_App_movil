package com.mobile.massiveapp.ui.view.pedidocliente

import android.content.Intent
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
import com.mobile.massiveapp.databinding.ActivityNuevoPedidoArticuloInfoBinding
import com.mobile.massiveapp.domain.model.DoArticuloListaPrecios
import com.mobile.massiveapp.domain.model.DoArticuloPedidoInfo
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.ui.base.BaseBottomSheetCustomDialog
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.view.util.agregarDetalleDePedido
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.view.util.showMessage
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NuevoPedidoArticuloInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevoPedidoArticuloInfoBinding
    private val articuloViewModel: ArticuloViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val socioViewModel: SocioViewModel by viewModels()
    private var accDocEntry: String = ""
    private var itemCode: String = ""
    private var hashInfo = HashMap<String, Any>()
    private var usuario = DoUsuario()
    private val CANTIDAD_DEFAULT = 1
    private val PORCENTAJE_DESCUENTO = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoPedidoArticuloInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        setValoresIniciales()
        setDefaultUi()

        getInfoArticuloLiveData()


            //EDICION ARTICULO - Get Pedido Info
        if (intent.getBooleanExtra("edicionDetalle", false)){
            pedidoViewModel.getPedidoDetalleInfo(
                accDocEntry = intent.getStringExtra("accDocEntry").toString(),
                lineNum = intent.getIntExtra("lineNum", -1)
            )
        } else{
            setValoresInicialesNuevoDetalle()
        }

        //LiveData del pedido Detalle
        pedidoViewModel.dataGetPedidoDetalleInfo.observe(this){ detalleInfo->
            try {
                itemCode = detalleInfo.ItemCode

                binding.txvNPArtInfoArticuloValue.text = detalleInfo.ItemCode
                binding.txvNpArtInfoDescripcionValue.text = detalleInfo.ItemName
                binding.txvNpArtInfoGrupoUnidadMedidaValue.text = detalleInfo.UgpName   //Grupo unidad medida
                binding.txvNpArtInfoUnidadMedidaValue.text = detalleInfo.UomName        //Unidad medida
                binding.txvNpArtInfoCantidadValue.text = "${detalleInfo.Quantity.toInt()}"
                binding.txvNpArtInfoAlmacenValue.text = detalleInfo.Almacen
                binding.txvNpArtInfoListaPreciosValue.text = detalleInfo.ListaPrecio

                hashInfo["codigoImpuesto"] = detalleInfo.TaxCode
                hashInfo["listaPrecioCodigo"] = detalleInfo.PriceList
                hashInfo["codigoAlmacen"] = detalleInfo.WhsCode
                hashInfo["uomEntry"] = detalleInfo.UomEntry
                hashInfo["uomCode"] = detalleInfo.UomCode

                pedidoViewModel.getPrecioArticuloFYD(
                    itemCode = detalleInfo.ItemCode,
                    cardCode = prefsPedido.getCardCode()
                )

            } catch (e:Exception){
                //showMessage(this, e.message.toString())
                e.printStackTrace()
            }
        }


        //LiveData del ARTICULO INFO
        articuloViewModel.dataGetArticuloPedidoInfo.observe(this){ articuloSeleccionado ->
            try {
                llenarDatosDelArticulo(articuloSeleccionado)
                hashInfo["uomEntry"] = articuloSeleccionado.UomEntry


                pedidoViewModel.getPrecioArticuloFYD(
                    itemCode = articuloSeleccionado.ItemCode,
                    cardCode = prefsPedido.getCardCode()
                )

            } catch (e: Exception) {
                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
            }
        }

        //Cantidad X Tipo Unidad
        pedidoViewModel.dataGetCantidadXTipoUnidad.observe(this){ cantidadXTipo->
            if (binding.txvNpArtInfoTipoUnidadValue.text.toString() == "NIU"){
                binding.txvNpArtInfoCantidadCajaValue.text = cantidadXTipo.toString()
            } else if (binding.txvNpArtInfoTipoUnidadValue.text.toString() == "CAJA"){
                binding.txvNpArtInfoCantidadValue.text = cantidadXTipo.toString()
            }
        }


        //LiveData PrecioFinal por UM y ListaPrecio
        pedidoViewModel.dataGetUnidadMedidaYEquivalencia.observe(this){
            try {
                hashInfo["uomEntry"] = it.UomEntry
                setTotalValue(it.PrecioFinal, 0.0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        pedidoViewModel.dataGetPrecioArticuloFYD.observe(this) { infoPrecioFinal->
            try {
                val cantidad = binding.txvNpArtInfoCantidadValue.text.toString().toInt()
                //binding.txvNpArtInfoPrecioUnitarioValue.text = infoPrecioFinal.precioDescontado.toString()
                binding.txvNpArtInfoPorcentajeDescuentoValue.text = infoPrecioFinal.porcentajeDescuento.toString()
                binding.txvNpArtInfoPrecioDescontadoValue.text = infoPrecioFinal.precioFinal.toString()
                binding.txvNpArtInfoPrecioBrutoValue.text = infoPrecioFinal.precioBruto.toString()
                binding.txvNpArtInfoPrecioUnitarioValue.text = infoPrecioFinal.precioUnitario.toString()
                binding.txvNpArtInfoImpuestoValue.text = (infoPrecioFinal.precioBruto -infoPrecioFinal.precioFinal).format(2).toString()

                binding.txvNpArtInfoTotalValue.text = (infoPrecioFinal.precioFinal * cantidad).format(2).toString()
            } catch (e:Exception){
                e.printStackTrace()
            }

        }
    }

    private fun getInfoArticuloLiveData() {
        articuloViewModel.dataGetArticuloInfoBaseView.observe(this) { infoArticuloView ->

            try {
                var lista: List<HashMap<String, Pair<Int, String>>> = emptyList()

                lista = listOf(
                    hashMapOf("Ver Imagen" to Pair(R.drawable.icon_image, "Ver imagen")),
                    hashMapOf("Descripción" to Pair(R.drawable.icon_description, infoArticuloView.Descripcion)),
                    hashMapOf("Stock" to Pair(R.drawable.icon_inventario, infoArticuloView.Stock.toString())),
                    hashMapOf("Comprometido" to Pair(R.drawable.icon_comprometido, infoArticuloView.Comprometido.format(6).toString())),
                    hashMapOf("Solicitado" to Pair(R.drawable.icon_solicitado, infoArticuloView.Solicitado.toString())),
                    hashMapOf("Disponible" to Pair(R.drawable.icon_disponible, infoArticuloView.Disponible.toString())),
                    hashMapOf("Precio unitario" to Pair(R.drawable.icon_number_one, infoArticuloView.Precio.toString())),
                )

                mostrarBottomDialog(lista)
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
    }


    fun mostrarBottomDialog(listaElementos: List<HashMap<String, Pair<Int, String>>>){
        BaseBottomSheetCustomDialog(
            R.drawable.icon_inventario,
            this,
            "Código: ",
            binding.txvNPArtInfoArticuloValue.text.toString()
        ).showBottomSheetDialog(
            listaElementos
        ){}
    }


    private fun setValoresInicialesNuevoDetalle() {

        //Set CANTIDAD
        binding.txvNpArtInfoCantidadValue.text = "$CANTIDAD_DEFAULT"
        binding.txvNpArtInfoCantidadCajaValue.text = "$CANTIDAD_DEFAULT"


        //Get DATOS DEL USUARIO
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){
            try {
                this.usuario = it
                hashInfo["codigoUsuario"] = it.Code
                hashInfo["codigoAlmacen"] = it.DefaultWarehouse
                hashInfo["codigoImpuesto"] = it.DefaultTaxCode
                /*hashInfo["listaPrecioCodigo"] = it.DefaultPriceList*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //Lista Precio Default Cliente
        socioViewModel.getSocioNegocioPorCardCode(prefsPedido.getCardName())
        socioViewModel.dataSocioNegocioPorCardCode.observe(this){ socio->
            hashInfo["listaPrecioCodigo"] = socio.ListNum
        }

        //Set ALMACENES
        articuloViewModel.getAllArticuloAlmacenes()
        articuloViewModel.dataGetAllArticuloAlmacenes.observe(this) { almacenes ->
            try {
                binding.txvNpArtInfoAlmacenValue.text = almacenes.filter { it.WhsCode == usuario.DefaultWarehouse }.firstOrNull()?.WhsName?:"--"
                hashInfo["codigoAlmacen"] = usuario.DefaultWarehouse
            } catch (e: Exception) {
                Toast.makeText(this, "No se pudo obtener los almacenes", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        }


        //Set LISTA DE PRECIOS
        articuloViewModel.getAllArticuloListaPrecios()
        articuloViewModel.dataGetAllArticuloListaPrecios.observe(this){ listasPrecio->
            try {
                socioViewModel.getSocioNegocioPorCardCode(prefsPedido.getCardName())
                socioViewModel.dataSocioNegocioPorCardCode.observeOnce(this){ socio->
                    binding.txvNpArtInfoListaPreciosValue.text = listasPrecio.filter {
                        it.ListNum == 1
                    }.firstOrNull()?.ListName?:"--"
                }

            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        //Set IMPUESTO
        generalViewModel.getImpuestoDefault()
        generalViewModel.dataGetImpuestoDefault.observe(this){ impuestoDefault->
            try {
                binding.txvNpArtInfoImpuestoValue.text = impuestoDefault.Name
            } catch (e: Exception) {
                Toast.makeText(this, "No se pudo obtener el impuesto", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun setValoresIniciales() {
        accDocEntry = intent.getStringExtra("accDocEntry").toString()

        //Get DATOS DEL USUARIO
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){
            try {
                this.usuario = it
                hashInfo["codigoUsuario"] = it.Code
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    private fun setDefaultUi() {
        binding.clNpArtInfoGrupoUnidadMedida.isVisible = false

        //Click SELECCIONAR ARTICULO
        binding.clNPArtInfoArticulo.setOnClickListener {
            startForArticuloSeleccionadoResult.launch(
                Intent(this, SeleccionarArticuloActivity::class.java)
            )
        }


        //Set UNIDAD DE MEDIDA
        binding.clNpArtInfoUnidadMedida.setOnClickListener {
        }

        pedidoViewModel.dataGetAllUnidadesDeMedidaPorItemCode.observe(this) { unidadesDeMedida->
            BaseDialogChecklistWithId(
                binding.txvNpArtInfoUnidadMedidaValue.text.toString(),
                unidadesDeMedida.map { it.UomName }
            ) { unidadMedidaSeleccionada, id ->

                if (unidadMedidaSeleccionada.isNotEmpty()) {
                    binding.txvNpArtInfoUnidadMedidaValue.text = unidadMedidaSeleccionada

                    pedidoViewModel.getUnidadMedidaYEquivalencia(
                        itemCode =      binding.txvNPArtInfoArticuloValue.text.toString(),
                        unidadMedida =  unidadMedidaSeleccionada,
                        listNum =       hashInfo["listaPrecioCodigo"] as Int
                    )
                }
            }.show(supportFragmentManager, "unidadMedida")
        }


        //TIPO de UNIDAD

        binding.clNpArtInfoTipoUnidad.setOnClickListener {
            pedidoViewModel.dataGetAllTiposDeUnidad.observe(this){ tiposUnidad->
                BaseDialogChecklistWithId(
                    binding.txvNpArtInfoTipoUnidadValue.text.toString(),
                    tiposUnidad.map { it.Name }
                ) { tipoUnidadSelect, id ->
                    binding.txvNpArtInfoTipoUnidadValue.text = tipoUnidadSelect

                    binding.clNpArtInfoCantidadCaja.isEnabled = tipoUnidadSelect != "NIU"
                    binding.clNpArtInfoCantidad.isEnabled = tipoUnidadSelect == "NIU"

                    val cantidadXTipo = if (tipoUnidadSelect == "CAJA") binding.txvNpArtInfoCantidadCajaValue.text.toString().toDouble()
                    else binding.txvNpArtInfoCantidadValue.text.toString().toDouble()
                    pedidoViewModel.getCantidadXTipoUnidad(
                        itemCode = binding.txvNPArtInfoArticuloValue.text.toString(),
                        tipoUnidad = binding.txvNpArtInfoTipoUnidadValue.text.toString(),
                        cantidad = cantidadXTipo
                    )

                }.show(supportFragmentManager, "tipoPago")
            }
        }

        //CANTIDAD CAJA
        binding.clNpArtInfoCantidadCaja.setOnClickListener {
            try {
                if (binding.txvNpArtInfoTipoUnidadValue.text.toString().isEmpty()){
                    throw Exception("Debe seleccionar un artículo")
                }

                BaseDialogEdtWithTypeEdt(
                    tipo = "phone",
                    textEditable = binding.txvNpArtInfoCantidadCajaValue.text.toString()
                ){ cantidad->
                    if (cantidad.isNotEmpty()){
                        binding.txvNpArtInfoCantidadCajaValue.text = cantidad
                    }
                    pedidoViewModel.getCantidadXTipoUnidad(
                        itemCode = binding.txvNPArtInfoArticuloValue.text.toString(),
                        tipoUnidad = binding.txvNpArtInfoTipoUnidadValue.text.toString(),
                        cantidad = cantidad.toDouble()
                    )
                }.show(supportFragmentManager, "cantidadCaja")

            } catch (e:Exception){
                showMessage(this, e.message.toString())
            }

        }

        //Set Cantidad
        binding.clNpArtInfoCantidad.setOnClickListener {
            try {
                if (binding.txvNpArtInfoGrupoUnidadMedidaValue.text.isEmpty()){
                    throw Exception("Debe seleccionar un articulo")
                }

                BaseDialogEdtWithTypeEdt(
                    tipo = "phone",
                    textEditable = binding.txvNpArtInfoCantidadValue.text.toString()
                ){ cantidad->
                    if (cantidad.isNotEmpty()) {
                        binding.txvNpArtInfoCantidadValue.text = cantidad
                        val precio = binding.txvNpArtInfoPrecioUnitarioValue.text.toString().toDouble()

                        pedidoViewModel.getPrecioArticuloFYD(
                            itemCode = binding.txvNPArtInfoArticuloValue.text.toString(),
                            cardCode = prefsPedido.getCardCode()
                        )

                        pedidoViewModel.getCantidadXTipoUnidad(
                            itemCode = binding.txvNPArtInfoArticuloValue.text.toString(),
                            tipoUnidad = binding.txvNpArtInfoTipoUnidadValue.text.toString(),
                            cantidad = cantidad.toDouble()
                        )
                    }
                }.show(supportFragmentManager, "cantidad")

            } catch (e:Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showListaPrecioDialog(listasPrecio: List<DoArticuloListaPrecios>) {
        BaseDialogChecklistWithId(
            binding.txvNpArtInfoListaPreciosValue.text.toString(),
            listasPrecio.map { it.ListName }
        ){ listaSeleccionada, id->
            if (listaSeleccionada.isNotEmpty()){
                binding.txvNpArtInfoListaPreciosValue.text = listaSeleccionada
                hashInfo["listaPrecioCodigo"] = listasPrecio[id].ListNum
                val cantidad = binding.txvNpArtInfoCantidadValue.text.toString().toInt()

                pedidoViewModel.getPrecioArticulo(
                    cantidad,
                    itemCode,
                    hashInfo["listaPrecioCodigo"] as Int
                )
            }
        }.show(supportFragmentManager, "listaPrecios")
    }


    fun llenarDatosDelArticulo(articuloSeleccionado: DoArticuloPedidoInfo) {
        binding.txvNPArtInfoArticuloValue.text = articuloSeleccionado.ItemCode                  //codigo
        binding.txvNpArtInfoDescripcionValue.text = articuloSeleccionado.ItemName               //descripcion
        binding.txvNpArtInfoGrupoUnidadMedidaValue.text = articuloSeleccionado.UgpName          //grupo de unidad de medida
        binding.txvNpArtInfoPorcentajeDescuentoValue.text = "0.00"                              //descuento
        binding.txvNpArtInfoCantidadValue.text = "$CANTIDAD_DEFAULT"                            //cantidad
        binding.txvNpArtInfoUnidadMedidaValue.text = articuloSeleccionado.UomName               //unidad de medida
        binding.txvNpArtInfoTipoUnidadValue.text = articuloSeleccionado.TipoUnidad              //Tipo de unidad

        val isManual = articuloSeleccionado.UgpName == "MANUAL"
        binding.clNpArtInfoUnidadMedida.isClickable = !isManual
        //binding.imvUnidadMedida.isVisible = !isManual

        hashInfo["uomCode"] = articuloSeleccionado.UomCode
    }



    fun setTotalValue(precioUnitario: Double, precioDescontado: Double){
        val newPrecio = precioUnitario.format(2)
        val cantidad = binding.txvNpArtInfoCantidadValue.text.toString().toDouble().format(2)
        val total = (cantidad * newPrecio).format(2)
        val newTotal = total.format(2)
        binding.txvNpArtInfoTotalValue.text = newTotal.toString()
        binding.txvNpArtInfoPrecioUnitarioValue.text = newPrecio.toString()    //precio unitario
        binding.txvNpArtInfoPrecioBrutoValue.text = precioDescontado.format(2).toString()       //precio bruto sin descuento
    }

    fun setTotalValues(precioUnitario: Double, precioDescontado: Double, precioIGV: Double){
        val cantidad = binding.txvNpArtInfoCantidadValue.text.toString().toDouble().format(5)

        val precioUnitarioFormat = precioUnitario.format(2)
        val precioDescontadoFormat = precioDescontado.format(2)
        val precioIGVFormat = precioIGV.format(2)

        val totalUnitario = (cantidad * precioUnitarioFormat).format(5)
        val totalDescontado = (cantidad * precioDescontadoFormat).format(5)
        val totalIGV = (cantidad * precioIGVFormat).format(5)

        val totalFormatUnitario = totalUnitario.format(2)
        val totalFormatDescuento = totalDescontado.format(2)
        val totalFormatIGV = totalIGV.format(2)

        binding.txvNpArtInfoPrecioDescontadoValue.text = precioDescontado.toString()
        binding.txvNpArtInfoPrecioUnitarioValue.text = precioUnitario.toString()    //precio unitario
        binding.txvNpArtInfoPrecioBrutoValue.text = precioIGV.format(2).toString()       //precio bruto sin descuento

        //TOTAL
        binding.txvNpArtInfoTotalValue.text = (cantidad*precioDescontado).format(2).toString()



    }









        //Resultado del Articulo seleccionado
    val startForArticuloSeleccionadoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode.equals(RESULT_OK)){
            val data = result.data
            val itemCodeArticuloSeleccionado = data?.getStringExtra("itemCodeArticulo")
            if (!itemCodeArticuloSeleccionado.isNullOrEmpty()){

                articuloViewModel.getArticuloPedidoInfo(itemCodeArticuloSeleccionado)
                pedidoViewModel.getAllTiposDeUnidad(itemCodeArticuloSeleccionado)
                itemCode = itemCodeArticuloSeleccionado
            }
        }
    }













    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("articuloAgregado", true)
        intent.putExtra("accDocEntry",accDocEntry)
        intent.putExtra("editMode", true)
        setResult(RESULT_OK, intent)

        super.onBackPressed()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo_sn, menu)

            //Se oculta el icono que indica si hay conexión o no
        val item = menu?.findItem(R.id.app_bar_connectivity_status)
        item?.setIcon(R.drawable.icon_info_circle)

        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_connectivity_status -> {
                if (binding.txvNPArtInfoArticuloValue.text.toString().isNotEmpty()){
                    articuloViewModel.getArticuloInfoBaseView(itemCode = binding.txvNPArtInfoArticuloValue.text.toString())
                } else {
                    Toast.makeText(this, "Debe seleccionar un articulo", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.app_bar_check -> {
                try {
                    val cantidadPedidos =
                        if (intent.getIntExtra("lineNum", -1) >= 1000){
                            intent.getIntExtra("lineNum", -1) - 1000
                        } else {
                            intent.getIntExtra("lineNum", -1)
                        }

                    if (cantidadPedidos >= prefsPedido.getMaximoLineas()){
                        throw Exception("Cantidad máxima de lineas para pedido")
                    }

                    if (binding.txvNPArtInfoArticuloValue.text.toString().isEmpty()) {
                        throw Exception("Debe seleccionar un articulo")
                    }

                    if ((binding.txvNpArtInfoCantidadValue.text.trim().toString().toIntOrNull()?: 0) <= 0) {
                        throw Exception("La cantidad debe ser mayor a 0")
                    }

                    pedidoViewModel.savePedidoDetalle(
                        agregarDetalleDePedido(
                            usuario =               usuario.Code,
                            accDocEntry =           accDocEntry,
                            codigo =                binding.txvNPArtInfoArticuloValue.text.toString(),
                            nombre =                binding.txvNpArtInfoDescripcionValue.text.toString().replace("\n", " "),
                            unidadMedida =          binding.txvNpArtInfoUnidadMedidaValue.text.toString(),
                            cantidad =              binding.txvNpArtInfoCantidadValue.text.toString().trim().toDouble(),
                            grupoUM =               hashInfo["uomCode"] as String,
                            precio =                binding.txvNpArtInfoPrecioDescontadoValue.text.toString().toDouble(),
                            precioBruto =           binding.txvNpArtInfoPrecioBrutoValue.text.toString().toDouble(),
                            precioAftVat =          binding.txvNpArtInfoPrecioBrutoValue.text.toString().toDouble(),
                            precioLP =              binding.txvNpArtInfoPrecioUnitarioValue.text.toString().toDouble(),
                            porcentajeDescuento =   binding.txvNpArtInfoPorcentajeDescuentoValue.text.toString().toDouble(),
                            total =                 binding.txvNpArtInfoTotalValue.text.toString().toDouble(),
                            lineNum =               intent.getIntExtra("lineNum", -1),
                            fechaActual =           getFechaActual(),
                            horaActual =            getHoraActual(),
                            impuesto =              0.0, //falta poner el rate
                            codigoImpuesto =        hashInfo["codigoImpuesto"] as String,
                            listaPrecios =          hashInfo["listaPrecioCodigo"] as Int,
                            codigoAlmacen =         hashInfo["codigoAlmacen"] as String,
                            uomEntry =              hashInfo["uomEntry"] as Int,
                        )
                    )

                    pedidoViewModel.dataSavePedidoDetalle.observe(this){ result->
                        if (result){
                            onBackPressed()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
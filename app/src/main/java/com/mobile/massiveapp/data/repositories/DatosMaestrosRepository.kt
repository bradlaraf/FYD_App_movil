package com.mobile.massiveapp.data.repositories

import android.util.Log
import com.mobile.massiveapp.data.database.dao.ClientePagosDao
import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.database.dao.SocioContactosDao
import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.entities.ErrorLogEntity
import com.mobile.massiveapp.data.database.entities.toModel
import com.mobile.massiveapp.data.model.Articulo
import com.mobile.massiveapp.data.model.ArticuloAlmacenes
import com.mobile.massiveapp.data.model.ArticuloCantidad
import com.mobile.massiveapp.data.model.ArticuloFabricantes
import com.mobile.massiveapp.data.model.ArticuloGrupos
import com.mobile.massiveapp.data.model.ArticuloGruposUM
import com.mobile.massiveapp.data.model.ArticuloGruposUMDetalle
import com.mobile.massiveapp.data.model.ArticuloListaPrecios
import com.mobile.massiveapp.data.model.ArticuloLocalidades
import com.mobile.massiveapp.data.model.ArticuloPrecio
import com.mobile.massiveapp.data.model.ArticuloUnidades
import com.mobile.massiveapp.data.model.Banco
import com.mobile.massiveapp.data.model.Bases
import com.mobile.massiveapp.data.model.Camioneta
import com.mobile.massiveapp.data.model.Cargos
import com.mobile.massiveapp.data.model.ClienteFacturaDetalle
import com.mobile.massiveapp.data.model.ClienteFacturas
import com.mobile.massiveapp.data.model.ClientePagos
import com.mobile.massiveapp.data.model.ClientePagosDetalle
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.model.ClientePedidos
import com.mobile.massiveapp.data.model.GeneralEmpleados
import com.mobile.massiveapp.data.model.GeneralMonedas
import com.mobile.massiveapp.data.model.ClienteSocios
import com.mobile.massiveapp.data.model.Conductor
import com.mobile.massiveapp.data.model.ConfigurarUsuarios
import com.mobile.massiveapp.data.model.CuentasC
import com.mobile.massiveapp.data.model.FormaPago
import com.mobile.massiveapp.data.model.GeneralActividadesE
import com.mobile.massiveapp.data.model.GeneralCentrosC
import com.mobile.massiveapp.data.model.GeneralCondiciones
import com.mobile.massiveapp.data.model.GeneralCuentasB
import com.mobile.massiveapp.data.model.GeneralDepartamentos
import com.mobile.massiveapp.data.model.GeneralDimensiones
import com.mobile.massiveapp.data.model.GeneralDistritos
import com.mobile.massiveapp.data.model.GeneralImpuestos
import com.mobile.massiveapp.data.model.GeneralIndicadores
import com.mobile.massiveapp.data.model.GeneralPaises
import com.mobile.massiveapp.data.model.GeneralProvincias
import com.mobile.massiveapp.data.model.GeneralProyectos
import com.mobile.massiveapp.data.model.GeneralSocioGrupos
import com.mobile.massiveapp.data.model.GeneralVendedores
import com.mobile.massiveapp.data.model.GeneralZonas
import com.mobile.massiveapp.data.model.GrupoDescuento
import com.mobile.massiveapp.data.model.GrupoDescuentoDetalle
import com.mobile.massiveapp.data.model.Manifiesto
import com.mobile.massiveapp.data.model.ManifiestoDocumento
import com.mobile.massiveapp.data.model.PrecioEspecial
import com.mobile.massiveapp.data.model.PrecioEspecial1
import com.mobile.massiveapp.data.model.PrecioEspecial2
import com.mobile.massiveapp.data.model.SeriesN
import com.mobile.massiveapp.data.model.Sociedad
import com.mobile.massiveapp.data.model.SocioContactos
import com.mobile.massiveapp.data.model.SocioDirecciones
import com.mobile.massiveapp.data.model.Sucursal
import com.mobile.massiveapp.data.model.TipoCambio
import com.mobile.massiveapp.data.model.UsuarioAlmacenes
import com.mobile.massiveapp.data.model.UsuarioGrupoArticulos
import com.mobile.massiveapp.data.model.UsuarioGrupoSocios
import com.mobile.massiveapp.data.model.UsuarioListaPrecios
import com.mobile.massiveapp.data.model.UsuarioToSend
import com.mobile.massiveapp.data.model.UsuarioZonas
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.data.util.getKeys
import com.mobile.massiveapp.data.util.getMap
import com.mobile.massiveapp.data.util.getNombreTabla
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoConfirmacionGuardado
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import timber.log.Timber
import javax.inject.Inject

class DatosMaestrosRepository @Inject constructor(
    private val api: DatosMaestrosService,

    private val socioContactosDao: SocioContactosDao,
    private val socioDireccionesDao: SocioDireccionesDao,
    private val clienteSociosDao: ClienteSociosDao,
    private val clientePedidosDao: ClientePedidosDao,
    private val clientePedidosDetalleDao: ClientePedidosDetalleDao,
    private val clientePagosDao: ClientePagosDao,
    private val clientePagosDetalleDao: ClientePagosDetalleDao,
    private val errorLogDao: ErrorLogDao,
    private val managerImputData: ManagerInputData

    ) {

    suspend fun getDatosMaestrosFromEndpointAndSave(
        endpoints: List<String>,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        top: Int = 0,
        timeout: Long = 60L,
        progressCallBack: (Int, String, Int) -> Unit
    ): Boolean {
        endpoints.forEach { endpoint ->
            try {
                progressCallBack(0, "Obteniendo $endpoint", 1)
                val dataList  = api.getDatoMaestroWithTop(
                    endpoint,
                    configuracion,
                    usuario,
                    url
                )

                    /********* Guardado de listas embebidas *********/
                when(endpoint){
                    /*"FacturasCL"-> {
                        val listaFacturas = dataList as List<ClienteFacturas>
                        val listaEmbebida = listaFacturas.map { it.facturasDetalle }
                        listaEmbebida.forEach {
                            val dataMaped = getMap(ClienteFacturaDetalle(), it)
                            managerImputData.registrarMaestro("ClienteFacturaDetalle", dataMaped as List<Any>)
                        }
                    }*/

                    "GruposUM"-> {
                        val listaGrupos = dataList as List<ArticuloGruposUM>
                        val listaEmbebida = listaGrupos.map { it.Lineas }
                        listaEmbebida.forEach {
                            val dataMaped = getMap(ArticuloGruposUMDetalle(), it) as List<Any>
                            managerImputData.registrarMaestro("ArticuloGruposUMDetalle", dataMaped)
                        }
                    }

                    "ClientePagos"-> {
                        val listaPagos = dataList as List<ClientePagos>
                        val listaEmbebida = listaPagos.map { it.clientePagosDetalles }
                        listaEmbebida.forEach {
                            val dataMaped = getMap(ClientePagosDetalle(), it) as List<Any>
                            managerImputData.registrarMaestro("ClientePagosDetalle", dataMaped)
                        }
                    }

                    "ClientePedidos"-> {
                        val listaPedidos = dataList as List<ClientePedidos>
                        val listaEmbebida = listaPedidos.map { it.clientePedidoDetalles }
                        listaEmbebida.forEach {
                            val dataMaped = getMap(ClientePedidoDetalle(), it) as List<Any>
                            managerImputData.registrarMaestro("ClientePedidosDetalle", dataMaped)
                        }
                    }

                    "GruposDE" -> {
                        val listaGrupoDescuento = dataList as List<GrupoDescuento>
                        val listaEmbebida = listaGrupoDescuento.map { it.Lineas }
                        listaEmbebida.forEach {
                            val dataMaped = getMap(GrupoDescuentoDetalle(), it) as List<Any>
                            managerImputData.registrarMaestro("GrupoDescuentoDetalle", dataMaped)
                        }
                    }

                    "Manifiestos" -> {
                        val listaManifiesto = dataList as List<Manifiesto>
                        val listaEmbebida = listaManifiesto.map { it.Documento }
                        listaEmbebida.forEach {
                            val dataMaped = getMap(ManifiestoDocumento(), it) as List<Any>
                            managerImputData.registrarMaestro("ManifiestoDocumento", dataMaped)
                        }
                    }

                    "PreciosEspecial" -> {
                        val listaPrecioEspecial = dataList as List<PrecioEspecial>
                        val listaEmbebida = listaPrecioEspecial.map { it.Linea1 }
                        val listaEmbebida2 = listaPrecioEspecial.map { it.Linea2 }
                        listaEmbebida.forEach {
                            val dataMaped = getMap(PrecioEspecial1(), it) as List<Any>
                            managerImputData.registrarMaestro("PrecioEspecial1", dataMaped)
                        }

                        listaEmbebida2.forEach {
                            val dataMaped = getMap(PrecioEspecial2(), it) as List<Any>
                            managerImputData.registrarMaestro("PrecioEspecial2", dataMaped)
                        }
                    }
                }

                /*********************************************/

                val dataListMaped =
                    when (endpoint){
                    "ClienteSociosContactos" -> { getMap(SocioContactos(), dataList) }
                    "ClienteSociosDirecciones" -> { getMap(SocioDirecciones(), dataList) }
                    "ArticuloCantidades" -> { getMap(ArticuloCantidad(), dataList) }
                    "ArticuloPrecios" -> { getMap(ArticuloPrecio(), dataList) }
                    "Articulo" -> { getMap(Articulo(), dataList) }
                    "ActividadesE" -> { getMap(GeneralActividadesE(), dataList) }
                    "Almacenes" -> { getMap(ArticuloAlmacenes(), dataList) }
                    "Bases" -> { getMap(Bases(), dataList) }
                    "Bancos" -> { getMap(Banco(), dataList) }
                    "CentrosC" -> { getMap(GeneralCentrosC(), dataList) }
                    "ClientePagos" -> { getMap(ClientePagos(), dataList) } //Lista embebida
                    "ClientePedidos" -> { getMap(ClientePedidos(), dataList) } //Lista embebida
                    "ClienteSocios" -> { getMap(ClienteSocios(), dataList) }
                    "Condiciones" -> { getMap(GeneralCondiciones(), dataList) }
                    "CuentasB" -> { getMap(GeneralCuentasB(), dataList) }
                    "Departamentos" -> { getMap(GeneralDepartamentos(), dataList) }
                    "Dimensiones" -> { getMap(GeneralDimensiones(), dataList) }
                    "Distritos" -> { getMap(GeneralDistritos(), dataList) }
                    "Empleados" -> { getMap(GeneralEmpleados(), dataList) }
                    "Fabricantes" -> { getMap(ArticuloFabricantes(), dataList) }
                    "FacturasCL" -> { getMap(ClienteFacturas(), dataList) }
                    "FacturasCLDetalles" ->{ getMap(ClienteFacturaDetalle(), dataList)}
                    "GruposAR" -> { getMap(ArticuloGrupos(), dataList) }
                    "GruposSN" -> { getMap(GeneralSocioGrupos(), dataList) }
                    "GruposUM" -> { getMap(ArticuloGruposUM(), dataList) } //Lista embebida
                    "Impuestos" -> { getMap(GeneralImpuestos(), dataList) }
                    "Indicadores" -> { getMap(GeneralIndicadores(), dataList) }
                    "ListaPrecios" -> { getMap(ArticuloListaPrecios(), dataList) }
                    "Monedas" -> { getMap(GeneralMonedas(), dataList) }
                    "Paises" -> { getMap(GeneralPaises(), dataList) }
                    "Provincias" -> { getMap(GeneralProvincias(), dataList) }
                    "Proyectos" -> { getMap(GeneralProyectos(), dataList) }
                    "Unidades" -> { getMap(ArticuloUnidades(), dataList) }

                    "UsuariosAlmacenes" -> { getMap(UsuarioAlmacenes(), dataList) }
                    "UsuariosZonas" -> { getMap(UsuarioZonas(), dataList) }
                    "UsuariosListaPrecios" -> { getMap(UsuarioListaPrecios(), dataList) }
                    "UsuariosGruposSocios" -> { getMap(UsuarioGrupoSocios(), dataList) }
                    "UsuariosGrupoArticulos" -> { getMap(UsuarioGrupoArticulos(), dataList) }
                    "Usuarios" -> { getMap(ConfigurarUsuarios(), dataList) }
                    "Series" -> { getMap(SeriesN(), dataList) }
                    "CuentasC" -> { getMap(CuentasC(), dataList) }

                    "Vendedores" -> { getMap(GeneralVendedores(), dataList) }
                    "ArticuloPrecio" -> { getMap(ArticuloPrecio(), dataList) }
                    "ArticuloCantidad" -> { getMap(ArticuloCantidad(), dataList) }
                    "ArticuloGruposUMDetalle" -> { getMap(ArticuloGruposUMDetalle(), dataList) }
                    "Sociedad" -> { getMap(Sociedad(), dataList) }
                    "Localidades" -> { getMap(ArticuloLocalidades(), dataList) }
                    "Zonas" -> { getMap(GeneralZonas(), dataList) }


                    "Cargos" -> { getMap(Cargos(), dataList) }
                    "FormaPagos" -> { getMap(FormaPago(), dataList) }

                    "GruposDE" -> { getMap(GrupoDescuento(), dataList) }
                    "Manifiestos" -> { getMap(Manifiesto(), dataList) }
                    "Camionetas" -> { getMap(Camioneta(), dataList) }
                    "TiposCambio" -> { getMap(TipoCambio(), dataList) }
                    "Conductores" -> { getMap(Conductor(), dataList) }
                    "Sucursales" -> { getMap(Sucursal(), dataList) }


                    else -> { emptyList<Any>() }
                } as List<Any>

                managerImputData.registrarMaestro(endpoint, dataListMaped)

                if (configuracion.UsarConfirmacion){
                    sendConfirmacion(endpoint, dataList, configuracion, usuario, url)
                }


            } catch (e: Exception){
                errorLogDao.insert(
                    ErrorLogEntity(
                        ErrorCode = "Maestro-$endpoint",
                        ErrorMessage = "${e.message}",
                        ErrorDate = getFechaActual(),
                        ErrorHour = getHoraActual()
                    )
                )
                return@forEach
            }
        }
        return true
    }

    suspend fun sendConfirmacion(endpoint: String, dataList: List<Any>,configuracion: DoConfiguracion, usuario: DoUsuario, url:String){
        if (dataList.isNotEmpty()){

            val listKeys =
                when (endpoint){
                    "ClienteSociosContactos" -> { getKeys(SocioContactos(), dataList as List<String>) }
                    "ClienteSociosDirecciones" -> { getKeys(SocioDirecciones(), dataList as List<String>) }
                    "ArticuloCantidades" -> { getKeys(ArticuloCantidad(), dataList as List<String>) }
                    "ArticuloPrecios" -> { getKeys(ArticuloPrecio(), dataList as List<String>) }
                    "Articulo" -> { getKeys(Articulo(), dataList) }
                    "ActividadesE" -> { getKeys(GeneralActividadesE(), dataList) }
                    "Almacenes" -> { getKeys(ArticuloAlmacenes(), dataList) }
                    "Bases" -> { getKeys(Bases(), dataList) }
                    "Bancos" -> { getKeys(Banco(), dataList) }
                    "CentrosC" -> { getKeys(GeneralCentrosC(), dataList) }
                    "ClientePagos" -> { getKeys(ClientePagos(), dataList) } //Lista embebida
                    "ClientePedidos" -> { getKeys(ClientePedidos(), dataList) } //Lista embebida
                    "ClienteSocios" -> { getKeys(ClienteSocios(), dataList) }
                    "Condiciones" -> { getKeys(GeneralCondiciones(), dataList) }
                    "CuentasB" -> { getKeys(GeneralCuentasB(), dataList) }
                    "Departamentos" -> { getKeys(GeneralDepartamentos(), dataList) }
                    "Dimensiones" -> { getKeys(GeneralDimensiones(), dataList) }
                    "Distritos" -> { getKeys(GeneralDistritos(), dataList) }
                    "Empleados" -> { getKeys(GeneralEmpleados(), dataList) }
                    "Fabricantes" -> { getKeys(ArticuloFabricantes(), dataList) }
                    "FacturasCL" -> { getKeys(ClienteFacturas(), dataList) } //Lista embebida
                    "GruposAR" -> { getKeys(ArticuloGrupos(), dataList) }
                    "GruposSN" -> { getKeys(GeneralSocioGrupos(), dataList) }
                    "GruposUM" -> { getKeys(ArticuloGruposUM(), dataList) } //Lista embebida
                    "Impuestos" -> { getKeys(GeneralImpuestos(), dataList) }
                    "Indicadores" -> { getKeys(GeneralIndicadores(), dataList) }
                    "ListaPrecios" -> { getKeys(ArticuloListaPrecios(), dataList) }
                    "Monedas" -> { getKeys(GeneralMonedas(), dataList) }
                    "Paises" -> { getKeys(GeneralPaises(), dataList) }
                    "Provincias" -> { getKeys(GeneralProvincias(), dataList) }
                    "Proyectos" -> { getKeys(GeneralProyectos(), dataList) }
                    "Unidades" -> { getKeys(ArticuloUnidades(), dataList) }

                    "UsuariosAlmacenes" -> { getKeys(UsuarioAlmacenes(), dataList) }
                    "UsuariosZonas" -> { getKeys(UsuarioZonas(), dataList) }
                    "UsuariosListaPrecios" -> { getKeys(UsuarioListaPrecios(), dataList) }
                    "UsuariosGruposSocios" -> { getKeys(UsuarioGrupoSocios(), dataList) }
                    "UsuariosGrupoArticulos" -> { getKeys(UsuarioGrupoArticulos(), dataList) }
                    "Usuarios" -> { getKeys(ConfigurarUsuarios(), dataList) }
                    "Series" -> { getKeys(SeriesN(), dataList) }
                    "CuentasC" -> { getKeys(CuentasC(), dataList) }

                    "Cargos" -> { getKeys(Cargos(), dataList) }
                    "FormaPagos" -> { getKeys(FormaPago(), dataList) }

                    "Vendedores" -> { getKeys(GeneralVendedores(), dataList) }
                    "ArticuloPrecio" -> { getKeys(ArticuloPrecio(), dataList) }
                    "ArticuloCantidad" -> { getKeys(ArticuloCantidad(), dataList) }
                    "ArticuloGruposUMDetalle" -> { getKeys(ArticuloGruposUMDetalle(), dataList) }
                    "Sociedad" -> { getKeys(Sociedad(), dataList) }
                    "Localidades" -> { getKeys(ArticuloLocalidades(), dataList) }
                    "Zonas" -> { getKeys(GeneralZonas(), dataList) }
                    else -> { emptyList<Any>() }
                } as List<Any>

            api.sendConfirmacionDeInsercion(
                DoConfirmacionGuardado(
                    NombreTabla = getNombreTabla(endpoint),
                    ClavePrimaria = listKeys
                ),
                configuracion,
                usuario,
                url
            )
        }
    }

    suspend fun getDatosEmbebidos(listaKeys: List<Any>, keyName:String, endpoint: String, configuracion: DoConfiguracion, usuario: DoUsuario, url: String, timeout: Long){

        val dataList = when(endpoint){
            "ClienteSociosContactos" -> { api.getSocioContactos(endpoint, configuracion, usuario, url, listaKeys, keyName, timeout) }
            "ClienteSociosDirecciones" -> { api.getSociosDirecciones(endpoint, configuracion, usuario, url, top = 0) }
            else -> { api.getDatoMaestroWithKeyParameter(endpoint, configuracion, usuario, url, listaKeys, keyName, timeout) }
        }

        val dataListMaped =
            when (endpoint){
                "ClienteSociosContactos" -> { getMap(SocioContactos(), dataList) }
                "ClienteSociosDirecciones" -> { getMap(SocioDirecciones(), dataList) }
                "ArticuloCantidades" -> { getMap(ArticuloCantidad(), dataList) }
                "ArticuloPrecios" -> { getMap(ArticuloPrecio(), dataList) }
                else -> { emptyList<Any>() }
            } as List<Any>

        managerImputData.registrarMaestro(endpoint, dataListMaped)

        sendConfirmacion(endpoint, dataList, configuracion, usuario, url)
    }

        //Enviar la reinicializacion
    suspend fun sendReinicializacion(
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String
    ): Boolean =
        try {
            api.sendReinicializacionDeMaestros(configuracion, usuario, url)
            true
        } catch (e: Exception) {
            Log.e("DatosMaestrosRepository", "Error al enviar reinicializacion", e)
            false
        }


        //Enviar la renicializacion de Pagos Y Pedidos
    suspend fun sendReinicializacionDePagosYPedidos(
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String) =
        try {
            api.sendReinicializacionDePedidosYPagos(configuracion, usuario, url)
            true
        } catch (e: Exception) {
            Log.e("DatosMaestrosRepository", "Error al enviar reinicializacion", e)
            false
        }




    //Se recoge la data de la BD local y se la envia al Service
    suspend fun sendDatosMaestros(
        progressCallBack: (Int, String, Int) -> Unit,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String
    ): Boolean =
        try {
            val fechaActual = getFechaActual()
            //Se eliminan los pedidos y pagos detalles SIN CABECERA
            clientePedidosDetalleDao.deleteAllPedidoDetallesSinCabecera()
            clientePagosDetalleDao.deleteAllPagosDetallesSinCabecera()

            val listaNuevosSociosBd = clienteSociosDao.getAllSociosNoMigrados()
            val listaPedidos = clientePedidosDao.getAllPedidosSinMigrar()
            val listaPagos = clientePagosDao.getAllPagosSinMigrar()
            val datosMaestrosHash = HashMap<String, List<Any>>()


                //SOCIOS
            if (listaNuevosSociosBd.isNotEmpty()) {
                var nuevosSociosModel = emptyList<ClienteSocios>()

                listaNuevosSociosBd.forEachIndexed {index, socioNuevo ->
                    val direcciones = socioDireccionesDao.getDireccionPorCardCode(socioNuevo.CardCode)
                    val contactos = socioContactosDao.getContactosPorCardCode(socioNuevo.CardCode)

                    nuevosSociosModel += if (direcciones.isNullOrEmpty() && contactos.isNullOrEmpty()) {
                        socioNuevo.toModel(emptyList(), emptyList())
                    } else {
                        socioNuevo.toModel(contactos, direcciones)
                    }
                    progressCallBack(index, "${socioNuevo.CardCode}", listaNuevosSociosBd.size)
                }
                datosMaestrosHash["ClienteSocios"] = nuevosSociosModel
            }


                //PEDIDOS
            if (listaPedidos.isNotEmpty()) {
                var pedidosList = emptyList<ClientePedidos>()

                listaPedidos.forEachIndexed {index, pedidoEach ->
                    val detallePedido =
                        clientePedidosDetalleDao.getAllPedidoDetallePorAccDocEntry(pedidoEach.AccDocEntry)

                    pedidosList += if (detallePedido.isNotEmpty()) {
                        pedidoEach.toModel(detallePedido)
                    } else {
                        pedidoEach.toModel(detallePedido)
                    }

                    progressCallBack(index, "${pedidoEach.AccDocEntry}", listaPedidos.size)

                }
                datosMaestrosHash["ClientePedidos"] = pedidosList
            }


                //PAGOS
            if (listaPagos.isNotEmpty()) {
                var pagosList = emptyList<ClientePagos>()

                listaPagos.forEachIndexed {index, pagoEach ->
                    val detallePago =
                        clientePagosDetalleDao.getAllPagosDetallePorAccDocEntry(pagoEach.AccDocEntry)

                    pagosList += if (detallePago.isNotEmpty()) {
                        pagoEach.toModel(detallePago)
                    } else {
                        pagoEach.toModel(detallePago)
                    }
                    progressCallBack(index, "${pagoEach.AccDocEntry}", listaPagos.size)
                }
                datosMaestrosHash["ClientePagos"] = pagosList
            }


            //Envio de los datos a sincronizar
            api.sendDatosMaestrosToEndpoint(datosMaestrosHash, configuracion, usuario, url)
            true

        } catch (e: Exception) {
            Log.e("DatosMaestrosRepository", "Error al insertar datos maestros", e)
            false
        }





    suspend fun sendUnDocumento(
        documento: HashMap<String, List<Any>> ,
        configuracion: DoConfiguracion,
        usuario: DoUsuario ,
        url: String,
        timeout:Long = 60L
        ) =
        try {
            api.sendDatosMaestrosToEndpoint(documento, configuracion, usuario ,url, timeout)
            true
        } catch (e: Exception) {
            Log.e("DatosMaestrosRepository", "Error al insertar datos maestros", e)
            false
        }

    suspend fun sendUsuario(
        listaUsuarios: List<UsuarioToSend>,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout:Long = 60L
    ) =
        try {
            api.sendUsuario(listaUsuarios, configuracion, usuario ,url, timeout)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    //Obtener todos los articulos del Service

    suspend fun getAllClientesAndSave(
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ){
        /*val listaSocios = api.getSocios(
            "ClienteSocios",
            configuracion,
            usuario,
            url,
            60L,
            400)

        clienteSociosDao.insertAllSocios(listaSocios.map { it.toDatabase() })*/

        /*listaSocios.forEach { socio->
            socioDireccionesDao.insertAllSocioDirecciones(socio.Direcciones.map { it.toDatabase() })
            socioContactosDao.insertAllSociosContactos(socio.Contactos.map { it.toDatabase() })
        }*/
    }


}
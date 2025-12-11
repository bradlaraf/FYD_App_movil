package com.mobile.massiveapp.data.network

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.entities.ErrorLogEntity
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
import com.mobile.massiveapp.data.model.ClienteFacturaDetalle
import com.mobile.massiveapp.data.model.ClienteFacturas
import com.mobile.massiveapp.data.model.ClientePagos
import com.mobile.massiveapp.data.model.ClientePedidos
import com.mobile.massiveapp.data.model.GeneralEmpleados
import com.mobile.massiveapp.data.model.GeneralMonedas
import com.mobile.massiveapp.data.model.ClienteSocios
import com.mobile.massiveapp.data.model.ConfigurarUsuarios
import com.mobile.massiveapp.data.model.CuentasC
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
import com.mobile.massiveapp.data.model.Pendiente
import com.mobile.massiveapp.data.model.SeriesN
import com.mobile.massiveapp.data.model.Sociedad
import com.mobile.massiveapp.data.model.SocioContactos
import com.mobile.massiveapp.data.model.SocioDirecciones
import com.mobile.massiveapp.data.model.UsuarioAlmacenes
import com.mobile.massiveapp.data.model.UsuarioGrupoArticulos
import com.mobile.massiveapp.data.model.UsuarioGrupoSocios
import com.mobile.massiveapp.data.model.UsuarioListaPrecios
import com.mobile.massiveapp.data.model.UsuarioToSend
import com.mobile.massiveapp.data.model.UsuarioZonas
import com.mobile.massiveapp.data.network.interceptor.ClientXmlInterceptor
import com.mobile.massiveapp.data.util.ToXmlSendRequestBody
import com.mobile.massiveapp.data.util.getXmlEstadoSesionRequestBody
import com.mobile.massiveapp.data.util.getXmlRequestBodyForPendiente
import com.mobile.massiveapp.data.util.getXmlRequestBodyWithKeyParameter
import com.mobile.massiveapp.data.util.getXmlRequestBodyWithTop
import com.mobile.massiveapp.data.util.parseXmlAndGetJsonValue
import com.mobile.massiveapp.data.util.toXmlConfirmacionRequestBody
import com.mobile.massiveapp.data.util.toXmlReinicializacionPagosYPedidosRequestBody
import com.mobile.massiveapp.data.util.toXmlReinicializacionRequestBody
import com.mobile.massiveapp.data.util.toXmlRequestBody
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoConfirmacionGuardado
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobile.massiveapp.data.model.Camioneta
import com.mobile.massiveapp.data.model.Conductor
import com.mobile.massiveapp.data.model.GrupoDescuento
import com.mobile.massiveapp.data.model.PrecioEspecial
import com.mobile.massiveapp.data.model.Sucursal
import com.mobile.massiveapp.data.model.TipoCambio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DatosMaestrosService @Inject constructor(
    private val api: ApiStandardClient,
    private val client: OkHttpClient,
    private val errorLogDao: ErrorLogDao
) {
    companion object{
        private val endpointDataMap = mapOf(
            "ClienteSociosContactos"    to object:TypeToken<List<SocioContactos>>(){}.type,
            "ClienteSociosDirecciones"  to object :TypeToken<List<SocioDirecciones>>(){}.type,
            "ArticuloCantidades"        to object :TypeToken<List<ArticuloCantidad>>(){}.type,
            "ArticuloPrecios"           to object: TypeToken<List<ArticuloPrecio>>(){}.type,
            "Articulo"                  to object : TypeToken<List<Articulo>>() {}.type,
            "ActividadesE"                  to object : TypeToken<List<GeneralActividadesE>>() {}.type,
            "Almacenes"         to object : TypeToken<List<ArticuloAlmacenes>>() {}.type,
            "Bases"                     to object : TypeToken<List<Bases>>() {}.type,
            "Bancos"                     to object : TypeToken<List<Banco>>() {}.type,
            "CentrosC"           to object : TypeToken<List<GeneralCentrosC>>() {}.type,
            "ClientePagos"              to object : TypeToken<List<ClientePagos>>() {}.type,
            "ClientePedidos"            to object : TypeToken<List<ClientePedidos>>() {}.type,
            "ClienteSocios"             to object : TypeToken<List<ClienteSocios>>() {}.type,
            "Condiciones"        to object : TypeToken<List<GeneralCondiciones>>() {}.type,
            "CuentasB"           to object : TypeToken<List<GeneralCuentasB>>() {}.type,
            "Departamentos"             to object : TypeToken<List<GeneralDepartamentos>>() {}.type,
            "Dimensiones"               to object : TypeToken<List<GeneralDimensiones>>() {}.type,
            "Distritos"                 to object : TypeToken<List<GeneralDistritos>>() {}.type,
            "Empleados"                 to object : TypeToken<List<GeneralEmpleados>>() {}.type,
            "Fabricantes"       to object : TypeToken<List<ArticuloFabricantes>>() {}.type,
            "FacturasCL"           to object : TypeToken<List<ClienteFacturas>>() {}.type,
            "GruposAR"            to object : TypeToken<List<ArticuloGrupos>>() {}.type,
            "GruposSN"        to object : TypeToken<List<GeneralSocioGrupos>>() {}.type,
            "GruposUM"          to object : TypeToken<List<ArticuloGruposUM>>() {}.type,
            "Impuestos"          to object : TypeToken<List<GeneralImpuestos>>() {}.type,
            "Indicadores"        to object : TypeToken<List<GeneralIndicadores>>() {}.type,
            "ListaPrecios"      to object : TypeToken<List<ArticuloListaPrecios>>() {}.type,
            "Monedas"            to object : TypeToken<List<GeneralMonedas>>() {}.type,
            "Paises"             to object : TypeToken<List<GeneralPaises>>() {}.type,
            "Provincias"         to object : TypeToken<List<GeneralProvincias>>() {}.type,
            "Proyectos"          to object : TypeToken<List<GeneralProyectos>>() {}.type,
            "Unidades"          to object : TypeToken<List<ArticuloUnidades>>() {}.type,
            "Vendedores"         to object : TypeToken<List<GeneralVendedores>>() {}.type,
            "ArticuloPrecio"           to object : TypeToken<List<ArticuloPrecio>>() {}.type,
            "ArticuloCantidad"        to object : TypeToken<List<ArticuloCantidad>>() {}.type,
            "ArticuloGruposUMDetalle"   to object : TypeToken<List<ArticuloGruposUMDetalle>>() {}.type,
            "Sociedad"                  to object : TypeToken<List<Sociedad>>() {}.type,
            "Localidades"       to object : TypeToken<List<ArticuloLocalidades>>() {}.type,
            "Zonas"                     to object : TypeToken<List<GeneralZonas>>() {}.type,

            //FYD Maestros extra
            "TiposCambio"                   to object : TypeToken<List<TipoCambio>>() {}.type,
            "PreciosEspecial"                     to object : TypeToken<List<PrecioEspecial>>() {}.type,
            "GruposDE"                     to object : TypeToken<List<GrupoDescuento>>() {}.type,
            "Camionetas"                     to object : TypeToken<List<Camioneta>>() {}.type,
            "Conductores"                     to object : TypeToken<List<Conductor>>() {}.type,
            "Sucursales"                     to object : TypeToken<List<Sucursal>>() {}.type,

            "UsuariosAlmacenes" to object : TypeToken<List<UsuarioAlmacenes>>() {}.type,
            "UsuariosZonas" to object : TypeToken<List<UsuarioZonas>>() {}.type,
            "UsuariosListaPrecios" to object : TypeToken<List<UsuarioListaPrecios>>() {}.type,
            "UsuariosGruposSocios" to object : TypeToken<List<UsuarioGrupoSocios>>() {}.type,
            "UsuariosGrupoArticulos" to object : TypeToken<List<UsuarioGrupoArticulos>>() {}.type,
            "Usuarios" to object : TypeToken<List<ConfigurarUsuarios>>() {}.type,
            "Series" to object : TypeToken<List<SeriesN>>() {}.type,
            "CuentasC" to object : TypeToken<List<CuentasC>>() {}.type,
        )

        val dataUsuarios = mapOf(
            "UsuariosAlmacenes" to "ConfigurarUsuarioAlmacenes",
            "UsuariosZonas" to "ConfigurarUsuarioZonas",
            "UsuariosListaPrecios" to "ConfigurarUsuarioListaPrecios",
            "UsuariosGruposSocios" to "ConfigurarUsuarioGrupoSocios",
            "UsuariosGrupoArticulos" to "ConfigurarUsuarioGrupoArticulos",
            "Usuarios" to "ConfigurarUsuarios",
            "Series" to "SeriesN",
            "CuentasC" to "CuentasC"
        )

    }





            //Se obtienen TODOS LOS DATOS MESTROS
    suspend fun getDatosMaestrosFromEndpoints(
                endpoints: List<String>,
                configuracion: DoConfiguracion,
                usuario: DoUsuario,
                url: String,
                timeout: Long
            ): Map<String, List<Any>>  {
        return withContext(Dispatchers.IO) {
            try {
                val deferredDatosMaestrosMap = endpoints.map { endpoint ->
                    async<List<Any>> {
                        try {
                            // Configurar la solicitud con el tiempo de espera
                            val request = Request.Builder()
                                .url(url)
                                .post("".toXmlRequestBody(endpoint, configuracion, usuario))
                                .build()

                            val clientWithTimeout = client.newBuilder()
                                .connectTimeout(timeout, TimeUnit.SECONDS)
                                .readTimeout(timeout, TimeUnit.SECONDS)
                                .writeTimeout(timeout, TimeUnit.SECONDS)
                                .build()

                            val response =  clientWithTimeout.newCall(request).execute()

                            val responseBody = response.body()?.string()
                            val json = responseBody?.parseXmlAndGetJsonValue(true)?: throw NullPointerException("Response is null")
                            val gson = Gson()
                            val dataType = endpointDataMap[endpoint]?: throw IllegalArgumentException("Unknown endpoint: $endpoint")
                            gson.fromJson(json, dataType)
                        } catch (e: Exception) {
                            Timber.d("ErrorMaestro $endpoint: $e")
                            emptyList<Any>()
                        }
                    }
                }
                val datosMaestrosList = deferredDatosMaestrosMap.awaitAll()
                val datosMaestrosMap = endpoints.zip(datosMaestrosList).toMap()
                datosMaestrosMap
            } catch (e: Exception) {
                e.printStackTrace()
                emptyMap()
            }
        }
    }


    /***************/
    //Se obtiene solo UN DATO MAESTRO
    suspend fun getArticulos(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
        top: Int
    ):List<Articulo>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)


            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithTop(endpoint, configuracion, usuario, top)

            val response = apiService.getDatoMaestro(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<Articulo>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    suspend fun getArticulosPrecios(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
        top: Int
    ):List<ArticuloPrecio>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithTop(endpoint, configuracion, usuario, top)

            val response = apiService.getArticulosPrecios(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ArticuloPrecio>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    suspend fun getArticuloCantidades(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
        top: Int
    ):List<ArticuloCantidad>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithTop(endpoint, configuracion, usuario, top)

            val response = apiService.getArticulosCantidades(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ArticuloCantidad>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    suspend fun getSocios(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
        top: Int
    ):List<ClienteSocios>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithTop(endpoint, configuracion, usuario, top)

            val response = apiService.getClientes(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ClienteSocios>>() {}.type
            gson.fromJson(json, listType)
        }
    }


    suspend fun getFacturas(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
        top: Int
    ):List<ClienteFacturas>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithTop(endpoint, configuracion, usuario, top)

            val response = apiService.getFacturas(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ClienteFacturas>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    suspend fun getFacturasDetalle(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
        top: Int
    ):List<ClienteFacturaDetalle>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithTop(endpoint, configuracion, usuario, top)

            val response = apiService.getFacturasDetalle(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ClienteFacturaDetalle>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    suspend fun getSociosContactos(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
        top: Int
    ):List<SocioContactos>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithTop(endpoint, configuracion, usuario, top)

            val response = apiService.getClienteContactos(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<SocioContactos>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    suspend fun getSociosDirecciones(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
        top: Int
    ):List<SocioDirecciones>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithTop(endpoint, configuracion, usuario, top)

            val response = apiService.getClienteDirecciones(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<SocioDirecciones>>() {}.type
            gson.fromJson(json, listType)
        }
    }


   /* suspend fun getSocioDirecciones(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        listaKeys: List<Any>,
        keyName: String,
        timeout: Long = 60L
    ):List<SocioDirecciones>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithKeyParameter(listaKeys, keyName,endpoint, configuracion, usuario)

            val response = apiService.getClienteDirecciones(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<SocioDirecciones>>() {}.type
            gson.fromJson(json, listType)
        }

    }*/


    suspend fun getSocioContactos(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        listaKeys: List<Any>,
        keyName: String,
        timeout: Long = 60L
    ):List<SocioContactos>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlRequestBodyWithKeyParameter(listaKeys, keyName,endpoint, configuracion, usuario)

            val response = apiService.getClienteContactos(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<SocioContactos>>() {}.type
            gson.fromJson(json, listType)
        }
    }



    fun createRetrofitClient(timeout: Long): Retrofit {
        val strategy = AnnotationStrategy()
        val serializer = Persister(strategy)

        val client = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(ClientXmlInterceptor().apply {})
            .build()

        return Retrofit.Builder()
            .baseUrl("http://your.api.base.url/")
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
            .client(client)
            .build()
    }
    /***************/





            //Se obtiene solo UN DATO MAESTRO
    suspend fun getDatoMaestro(
                endpoint: String,
                configuracion: DoConfiguracion,
                usuario: DoUsuario,
                url: String,
                timeout: Long = 60L
            ): List<Any> {
        return withContext(Dispatchers.IO) {
            // Configurar la solicitud con el tiempo de espera

            val request = Request.Builder()
                    .url(url)
                    .post("".toXmlRequestBody(endpoint, configuracion, usuario))
                    .build()

            val clientWithTimeout = client.newBuilder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build()

            // Realizar la solicitud utilizando el cliente con el tiempo de espera configurado
            val response = clientWithTimeout.newCall(request).execute()

            /*val response = api.getData("".toXmlRequestBody(endpoint, configuracion, usuario), url)*/
            val responseBody = response.body()?.string()
            val json = responseBody?.parseXmlAndGetJsonValue()?: throw NullPointerException("Response is null")
            val gson = Gson()
            val dataType = endpointDataMap[endpoint]?: throw IllegalArgumentException("Unknown endpoint: $endpoint")
            gson.fromJson(json, dataType)
        }
    }

    suspend fun getDatoMaestroWithTop(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeoutt: Long = 60L
    ): List<Any> {
        return withContext(Dispatchers.IO) {
            val timeout = 60L

            val top = when(endpoint){
                "ClienteSocios", "ClienteSociosContactos", "ClienteSociosDirecciones"-> { configuracion.TopCliente }
                "FacturasCL", "FacturasCLDetalles"-> { configuracion.TopFactura }
                "Articulo", "ArticuloCantidades", "ArticuloPrecios"-> { configuracion.TopArticulo }
                else -> { 0 }
            }
            when(endpoint){
                "ClientesSocios"->{

                }
            }

            if (endpoint == "ClienteSocios"){
                return@withContext getSocios(endpoint, configuracion, usuario, url, timeout, top)
            }

            if (endpoint == "ClienteSociosContactos"){
                return@withContext getSociosContactos(endpoint, configuracion, usuario, url, timeout, top)
            }

            if (endpoint == "ClienteSociosDirecciones"){
                return@withContext getSociosDirecciones(endpoint, configuracion, usuario, url, timeout, top)
            }
            if (endpoint == "ArticuloCantidades"){
                return@withContext getArticuloCantidades(endpoint, configuracion, usuario, url, timeout, top)
            }
            if (endpoint == "ArticuloPrecios"){
                return@withContext getArticulosPrecios(endpoint, configuracion, usuario, url, timeout, top)
            }

            if (endpoint == "FacturasCL") {
                return@withContext getFacturas(endpoint, configuracion, usuario, url, timeout, top)
            }

            if (endpoint == "FacturasCLDetalles") {
                return@withContext getFacturasDetalle("FacturasCLDetalle", configuracion, usuario, url, timeout, top)
            }

            val requestBody = getRequestBody(endpoint, configuracion, usuario, top)
            val response = getResponse(url, requestBody, timeout, endpoint)

            /*val response = api.getData("".toXmlRequestBody(endpoint, configuracion, usuario), url)*/

            /*insertErrorLog(response, endpoint)*/

            val responseBody = response?.body()?.string()?:""

            val json = responseBody.parseXmlAndGetJsonValue() ?: throw NullPointerException("Response is null")
            val gson = Gson()
            val dataType = endpointDataMap[endpoint]?: throw IllegalArgumentException("Unknown endpoint: $endpoint")
            gson.fromJson(json, dataType)
        }
    }


    suspend fun getResponse(url: String, requestBody: RequestBody, timeout: Long, endpoint: String): Response? {
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val clientWithTimeout = client.newBuilder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()

        return try {
            clientWithTimeout.newCall(request).execute()
        } catch (e: SocketTimeoutException) {
            insertError(endpoint, e.message.toString())
            null
        } catch (e: IOException) {
            insertError(endpoint, e.message.toString())
            null
        } catch (e: Exception) {
            insertError(endpoint, e.message.toString())
            null
        }
    }

    fun getRequestBody(endpoint: String, configuracion: DoConfiguracion, usuario: DoUsuario, top: Int): RequestBody {
        return if (endpoint == "ClienteSocios" || endpoint == "Articulo" || endpoint == "FacturasCL"|| endpoint == "ClienteSociosContactos"|| endpoint == "ClienteSociosDirecciones"|| endpoint == "ArticuloCantidades"|| endpoint == "ArticuloPrecios"){
            getXmlRequestBodyWithTop(endpoint, configuracion, usuario, top)
        } else {
            if (dataUsuarios.keys.contains(endpoint)){
                val rightEndpoint = dataUsuarios[endpoint]?:""
                "".toXmlRequestBody(rightEndpoint, configuracion, usuario)
            } else {
                "".toXmlRequestBody(endpoint, configuracion, usuario)
            }
        }
    }


    suspend fun insertError(endpoint: String, error: String){
        errorLogDao.insert(
            ErrorLogEntity(
                ErrorCode = "Maestro-$endpoint",
                ErrorMessage = error,
                ErrorDate = getFechaActual(),
                ErrorHour = getHoraActual()
            )
        )
    }

    suspend fun insertErrorLog(code: String, message: String){
        errorLogDao.insert(
            ErrorLogEntity(
                ErrorCode = code,
                ErrorMessage = message,
                ErrorDate = getFechaActual(),
                ErrorHour = getHoraActual()
            )
        )
    }


        //Get dato Maestro con
    suspend fun getDatoMaestroWithKeyParameter(
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        listaKeys: List<Any>,
        keyName: String,
        timeout: Long = 60L
    ): List<Any> {
        return withContext(Dispatchers.IO) {

            val request = Request.Builder()
                .url(url)
                .post(getXmlRequestBodyWithKeyParameter(listaKeys, keyName,endpoint, configuracion, usuario))
                .build()
            val clientWithTimeout = client.newBuilder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build()

            val response = clientWithTimeout.newCall(request).execute()

            /*val response = api.getData("".toXmlRequestBody(endpoint, configuracion, usuario), url)*/
            val responseBody = response.body()?.string()
            val json = responseBody?.parseXmlAndGetJsonValue()?: throw NullPointerException("Response is null")
            val gson = Gson()
            val dataType = endpointDataMap[endpoint]?: throw IllegalArgumentException("Unknown endpoint: $endpoint")
            gson.fromJson(json, dataType)
        }
    }



            //Enviar todos los DOCUMENTOS NO MIGRADOS
    suspend fun sendDatosMaestrosToEndpoint(
                listaDatosMaestrosAEnviar: HashMap<String, List<Any>>,
                configuracion: DoConfiguracion,
                usuario: DoUsuario,
                url: String,
                timeout: Long = 60L
            ) {
                withContext(Dispatchers.IO) {
                    try {
                        for(listaDatosEnvio in listaDatosMaestrosAEnviar){
                            if(listaDatosEnvio == null) continue
                            val endpoint = listaDatosEnvio.key
                            val listaDatos = listaDatosEnvio.value
                            val gson = Gson()
                            val listaDatosJsonArray = gson.toJson(listaDatos).replace("\n", " ")

                            // Configura el tiempo de espera
                            val request = Request.Builder()
                                .url(url)
                                .post("".ToXmlSendRequestBody(endpoint, listaDatosJsonArray, configuracion, usuario))
                                .build()


                            // Aplica el tiempo de espera configurado al cliente OkHttpClient inyectado
                            val clientWithTimeout = client.newBuilder()
                                .connectTimeout(timeout, TimeUnit.SECONDS)
                                .readTimeout(timeout, TimeUnit.SECONDS)
                                .writeTimeout(timeout, TimeUnit.SECONDS)
                                .build()
                            clientWithTimeout.newCall(request).execute()
                        }
                    } catch (e: Exception) {
                        insertErrorLog(
                            code = endpointDataMap.keys.firstOrNull()?:"Insercion",
                            message = "${e.message}"
                        )
                    }
            }
    }


    suspend fun sendUsuario(
        usuarioToSend: List<UsuarioToSend>,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L
    ): Any{
        return withContext(Dispatchers.IO){
            val gson = Gson()
            val listaDatosJsonArray = gson.toJson(usuarioToSend).replace("\n", " ")

            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            val requestBody = "".ToXmlSendRequestBody("Usuarios", listaDatosJsonArray, configuracion, usuario)
            val response = apiService.sendUsuarios(requestBody, url)
            val soapBody = response.body()?.body

            val codigoError = soapBody?.response?.result?.errorCodigo
            val codigoMensaje = soapBody?.response?.result?.errorMensaje

            val json = soapBody?.response?.result?.json

            val objType = object : TypeToken<List<UsuarioToSend>>() {}.type
            val listaUsuarios = gson.fromJson<List<UsuarioToSend>>(json, objType)?: emptyList()

            if ((codigoError?: -1) == 0){
                return@withContext gson.fromJson(json, objType)
            } else if((codigoError?: -1) == -2){
                val messageError = if (listaUsuarios.isNotEmpty()){
                    listaUsuarios.first().AccError
                } else {
                    "No se pudo conectar a la BD"
                }
                DoError(
                    ErrorCodigo = codigoError?:-1,
                    ErrorMensaje = messageError
                )
            } else {
                DoError(
                    ErrorCodigo = codigoError?:-1,
                    ErrorMensaje = codigoMensaje?:"No se pudo conectar a la BD"
                )
            }
        }
    }


    suspend fun sendSocios(
        listaSociosAEnviar: List<ClienteSocios>,
        endpoint: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L
    ): List<ClienteSocios> {
        val gson = Gson()

        val listaDatosJsonArray = gson.toJson(listaSociosAEnviar)
        val retrofit = createRetrofitClient(timeout)

        val apiService = retrofit.create(ApiStandardClient::class.java)

        val requestBody = "".ToXmlSendRequestBody(endpoint, listaDatosJsonArray, configuracion, usuario)

        val response = apiService.getArticulosPrecios(url, requestBody)

        val soapBody = response.body()?.body

        val json = soapBody?.response?.result?.json

        val listType = object : TypeToken<List<ClienteSocios>>() {}.type
        return gson.fromJson(json, listType)
    }

    suspend fun sendDatosMaestrosToEndpointWithRBody(
        listaDatosMaestrosAEnviar: HashMap<String, List<Any>>,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L
    ):Any {
        return withContext(Dispatchers.IO) {
            try {
                var insertResponse = false as Any
                for(listaDatosEnvio in listaDatosMaestrosAEnviar){

                    if(listaDatosEnvio == null) continue
                    val endpoint = listaDatosEnvio.key
                    val listaDatos = listaDatosEnvio.value
                    val gson = Gson()
                    val listaDatosJsonArray = gson.toJson(listaDatos).replace("\n", " ")

                    val request = Request.Builder()
                        .url(url)
                        .post("".ToXmlSendRequestBody(endpoint, listaDatosJsonArray, configuracion, usuario))
                        .build()

                    // Aplica el tiempo de espera configurado al cliente OkHttpClient inyectado
                    val clientWithTimeout = client.newBuilder()
                        .connectTimeout(timeout, TimeUnit.SECONDS)
                        .readTimeout(timeout, TimeUnit.SECONDS)
                        .writeTimeout(timeout, TimeUnit.SECONDS)
                        .build()

                    val response = clientWithTimeout.newCall(request).execute()
                    /*val response = api.sendDatosMaestros("".ToXmlSendRequestBody(endpoint, listaDatosJsonArray, configuracion, usuario), url)*/
                    val responseBody = response.body()?.string()
                    val json = responseBody?.parseXmlAndGetJsonValue()?: throw NullPointerException("Response is null")
                    val dataType = endpointDataMap[endpoint]?: throw IllegalArgumentException("Unknown endpoint: $endpoint")
                    insertResponse = gson.fromJson(json, dataType)
                }
                return@withContext insertResponse
            } catch (e: Exception) {
                insertErrorLog(
                    code = endpointDataMap.keys.firstOrNull()?:"Insercion",
                    message = "${e.message}"
                )
            }
        }
    }


    //Estado de sesion
    suspend fun getEstadoSesion(
        usuario: DoUsuario,
        configuracion: DoConfiguracion,
        url: String,
        timeout: Long = 60L
    ): Any{
        return withContext(Dispatchers.IO) {

            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            val requestBody = getXmlEstadoSesionRequestBody(configuracion, usuario)
            val response = apiService.getEstadoSesion(url, requestBody)
            val soapBody = response.body()?.body

            val codigoError = soapBody?.response?.result?.errorCodigo
            val codigoMensaje = soapBody?.response?.result?.errorMensaje
            val estadoSesion = soapBody?.response?.result?.json?:""

            if (estadoSesion.isNotEmpty()){
                estadoSesion
            } else {
                DoError(
                    ErrorCodigo = codigoError?:-1,
                    ErrorMensaje = codigoMensaje?:"Conexión fallida"
                )
            }

        }
    }

    //Estado de sesion
    suspend fun getEstadoSesionWithConectivityCheck(
        usuario: DoUsuario,
        configuracion: DoConfiguracion,
        url: String,
        timeout: Long = 60L
    ): Any{
        return withContext(Dispatchers.IO) {
            try {
                val retrofit = createRetrofitClient(timeout)
                val apiService = retrofit.create(ApiStandardClient::class.java)

                val requestBody = getXmlEstadoSesionRequestBody(configuracion, usuario)
                val response = apiService.getEstadoSesion(url, requestBody)
                val soapBody = response.body()?.body

                val codigoError = soapBody?.response?.result?.errorCodigo
                val codigoMensaje = soapBody?.response?.result?.errorMensaje
                val estadoSesion = soapBody?.response?.result?.json?:""

                if (estadoSesion.isNotEmpty()){
                    estadoSesion
                } else {
                    throw Exception()
                }
            } catch (e:Exception){
                DoError(
                    ErrorCodigo = -1,
                    ErrorMensaje = "Conexión fallida"
                )
            }
        }
    }



            //Se envia la CONFIRMACION de la INSERCION en la BD Local
    suspend fun sendConfirmacionDeInsercion(confirmacion: DoConfirmacionGuardado, configuracion: DoConfiguracion, usuario: DoUsuario, url: String) {
        withContext(Dispatchers.IO) {
            try {
                if (confirmacion.ClavePrimaria.isNotEmpty() && confirmacion.NombreTabla.isNotEmpty()){
                    if (confirmacion.ClavePrimaria[0] is String){
                        confirmacion.ClavePrimaria = confirmacion.ClavePrimaria.map {"'$it'"}
                    }
                }

                api.sendDatosMaestros("".toXmlConfirmacionRequestBody(confirmacion, configuracion, usuario), url)
            } catch (e: Exception) {
                println(e)
                FirebaseCrashlytics.getInstance().log("${e.message}")
                false
            }
        }
    }

            //Se envia la REINICIALIZACION para obtener todos los datos maestros
    suspend fun sendReinicializacionDeMaestros(configuracion: DoConfiguracion, usuario: DoUsuario, url: String) {
        withContext(Dispatchers.IO) {
            try {
                api.sendDatosMaestros("".toXmlReinicializacionRequestBody(configuracion, usuario), url)
            } catch (e: Exception) {
                println(e)
                false
            }
        }
    }

    //Se obtiene los Datos Maestros Pendientes
    suspend fun getPendientes(
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L
    ): List<Pendiente> {
        return withContext(Dispatchers.IO) {
            // Configurar la solicitud con el tiempo de espera
            val request = Request.Builder()
                .url(url)
                .post(getXmlRequestBodyForPendiente(configuracion, usuario))
                .build()

            val clientWithTimeout = client.newBuilder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build()

            // Realizar la solicitud utilizando el cliente con el tiempo de espera configurado
            val response = clientWithTimeout.newCall(request).execute()

            /*val response = api.getData("".toXmlRequestBody(endpoint, configuracion, usuario), url)*/
            val responseBody = response.body()?.string()
            val json = responseBody?.parseXmlAndGetJsonValue()?: throw NullPointerException("Response is null")
            val gson = Gson()
            val dataType = object : TypeToken<List<Pendiente>>() {}.type
            gson.fromJson(json, dataType)
        }
    }

    suspend fun sendReinicializacionDePedidosYPagos(configuracion: DoConfiguracion, usuario: DoUsuario, url: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.sendDatosMaestros("".toXmlReinicializacionPagosYPedidosRequestBody(configuracion, usuario), url)
                val responseBody = response.body()?.string()
                Timber.tag("ConfirmacionDeInsercion").d("Response: $responseBody")

            } catch (e: Exception) {
                println(e)
                false
            }
        }
    }


}
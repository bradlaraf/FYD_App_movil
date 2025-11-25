package com.mobile.massiveapp.data.util

import android.content.Context
import android.provider.Settings
import android.util.Xml
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoConfirmacionGuardado
import com.mobile.massiveapp.domain.model.DoUsuario
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

fun String.parseXmlAndGetJsonValue(): String? {
    var jsonValue: String? = null

    try {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(this))

        var eventType: Int = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "Json") {
                eventType = parser.next()
                jsonValue = parser.text.trim()
                if (jsonValue.startsWith("{")) { // Verifica si comienza con '{'
                    jsonValue = "[$jsonValue]" // Agrega corchetes si comienza con '{'
                }
                break
            }
            eventType = parser.next()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return jsonValue
}


fun toNotNotationScientific(numeroConNotacion: Double):Double {
    val numeroFormateado = String.format("%.10f", numeroConNotacion)
    return  numeroFormateado.toDoubleOrNull()?:0.0
}

fun String.parseXmlAndGetErrorCode(): String {
    var errorCode: String = ""

    try {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(this))

        var eventType: Int = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "ErrorCodigo") {
                eventType = parser.next()
                errorCode = parser.text?.trim() ?: ""
                break
            }
            eventType = parser.next()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return errorCode
}

fun String.parseXmlAndGetErrorMessage(): String {
    var errorMessage: String = ""

    try {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(this))

        var eventType: Int = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "ErrorMensaje") {
                eventType = parser.next()
                errorMessage = parser.text?.trim() ?: ""
                break
            }
            eventType = parser.next()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return errorMessage
}

/*<MSV_SocioNegocio_Sunat_ReceiveResult>string</MSV_SocioNegocio_Sunat_ReceiveResult>*/

fun String.parseXmlAndGetJsonValueForConsultaRuc(tipoDocumento: String): String? {
    val endpointName = if (tipoDocumento == "1") "MSV_SocioNegocio_Reniec_ReceiveResult" else if (tipoDocumento == "6") "MSV_SocioNegocio_Sunat_ReceiveResult" else ""
    var jsonValue: String? = null

    try {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(this))

        var eventType: Int = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == endpointName) {
                eventType = parser.next()
                jsonValue = parser.text.trim()
                break
            }
            eventType = parser.next()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return jsonValue
}




fun String.parseXmlAndGetJsonValue(withoutArrayControl: Boolean): String? {
    var jsonValue: String? = null

    try {
        val parser: XmlPullParser = Xml.newPullParser()                             // Se crea el parser
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)          // Se desactiva el procesamiento de namespaces
        parser.setInput(StringReader(this))                                      // Se da la cadena de texto a parsear

        var eventType: Int = parser.eventType                                       // Se obtiene el tipo de evento
        while (eventType != XmlPullParser.END_DOCUMENT) {                           // Mientras no sea el fin del documento
            if (eventType == XmlPullParser.START_TAG && parser.name == "Json") {    // Si es un tag de inicio y el nombre es Json
                eventType = parser.next()                                           // Se obtiene el siguiente evento
                jsonValue = parser.text.trim()                                      // Se obtiene el texto del evento y se le quitan los espacios en blanco
                break
            }
            eventType = parser.next()                                               // Se obtiene el siguiente evento
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return jsonValue                                                                // Se retorna el valor del Json
}


fun obtenerArraysAnidados(jsonString: String): Map<String, List<JsonObject>> {
    val gson = Gson()
    val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)

    val arraysAnidados = mutableMapOf<String, List<JsonObject>>()

    for ((key, value) in jsonObject.entrySet()) {
        if (value is JsonArray) {
            val jsonArray = value.asJsonArray
            if (jsonArray.size() > 0 && jsonArray[0].isJsonObject) {
                arraysAnidados[key] = jsonArray.map { it.asJsonObject }
            }
        }
    }

    return arraysAnidados
}

fun getNombreTabla(endpoint: String): String{
    return when (endpoint){
        "ClienteSociosContactos" -> { "SocioNegocioContacto" }
        "ClienteSociosDirecciones" -> { "SocioNegocioDireccion" }
        "ArticuloCantidades" -> { "ArticuloCantidad" }
        "ArticuloPrecios" -> { "ArticuloPrecio" }
        "Articulo" -> { "Articulo" }
        "ActividadesE" -> { "ActividadE" }
        "Almacenes" -> { "Almacen" }
        "Bases" -> { "" }
        "Bancos" -> { "Banco" }
        "CentrosC" -> { "CentroCosto" }
        "ClientePagos" -> { "" }
        "ClientePedidos" -> { "" }
        "ClienteSocios" -> { "SocioNegocio" }
        "Condiciones" -> { "CondicionPago" }
        "CuentasB" -> { "CuentaBancaria" }
        "Departamentos" -> { "Departamento" }
        "Dimensiones" -> { "Dimension" }
        "Distritos" -> { "Distrito" }
        "Empleados" -> { "Empleado" }
        "Fabricantes" -> { "Fabricante" }
        "FacturasCL" -> { "Factura" }
        "GruposAR" -> { "GrupoArticulo" }
        "GruposSN" -> { "GrupoSocio" }
        "GruposUM" -> { "GrupoUnidadMedida" }
        "Impuestos" -> { "Impuesto" }
        "Indicadores" -> { "Indicador" }
        "ListaPrecios" -> { "ListaPrecio" }
        "Monedas" -> { "Moneda" }
        "Paises" -> { "Pais" }
        "Provincias" -> { "Provincia" }
        "Proyectos" -> { "Proyecto" }
        "Unidades" -> { "UnidadMedida" }
        "Vendedores" -> { "Vendedor" }
        "ArticuloPrecio" -> { "" }
        "ArticuloCantidades" -> { "" }
        "ArticuloGruposUMDetalle" -> { "GrupoArticulo" }
        "Sociedad" -> { "Sociedad" }
        "Localidades" -> { "Localidad" }
        "Zonas" -> { "Zona" }
        else-> ""
    }
}




fun String.toXmlRequestBody(request: String, configuracion: DoConfiguracion, usuario: DoUsuario): RequestBody {
    val methodName = "MSV_APP_Obtener_$request"
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <$methodName xmlns="http://massive.org/">
                    <DataBase>${configuracion.BaseDeDatos}</DataBase>
                    <UserCode>${usuario.Code}</UserCode>
                    <UserIMEI>${configuracion.IMEI}</UserIMEI>              
                </$methodName>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}

fun getXmlRequestBodyWithKeyParameter(listaKeys: List<Any>,keyParameter: String , request: String, configuracion: DoConfiguracion, usuario: DoUsuario): RequestBody {
    val methodName = "MSV_APP_Obtener_$request"
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <$methodName xmlns="http://massive.org/">
                    <DataBase>${configuracion.BaseDeDatos}</DataBase>
                    <UserCode>${usuario.Code}</UserCode>
                    <UserIMEI>${configuracion.IMEI}</UserIMEI>
                    <$keyParameter>${listaKeys.joinToString(separator = ",")}</$keyParameter>
                </$methodName>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}

fun getXmlRequestBodyWithTop(endpoint: String, configuracion: DoConfiguracion, usuario: DoUsuario, top: Int): RequestBody {
    val methodName = "MSV_APP_Obtener_$endpoint"
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <$methodName xmlns="http://massive.org/">
                    <DataBase>${configuracion.BaseDeDatos}</DataBase>
                    <UserCode>${usuario.Code}</UserCode>
                    <UserIMEI>${configuracion.IMEI}</UserIMEI> 
                    <Top>${top}</Top>
                </$methodName>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}


fun getXmlParaReporteConFecha(request: String, configuracion: DoConfiguracion, usuario: DoUsuario, fechaIncial: String, fechaFinal: String): RequestBody {
    val methodName = "MSV_APP_Obtener_$request"
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <$methodName xmlns="http://massive.org/">
                    <DataBase>${configuracion.BaseDeDatos}</DataBase>
                    <UserCode>${usuario.Code}</UserCode>
                    <UserIMEI>${configuracion.IMEI}</UserIMEI> 
                    <FechaInicial>$fechaIncial</FechaInicial>
                    <FechaFinal>$fechaFinal</FechaFinal>                   
                </$methodName>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}


fun getXmlParaReporteEstadoCuenta(request: String, configuracion: DoConfiguracion, usuario: DoUsuario, codigoCliente: String): RequestBody {
    val methodName = "MSV_APP_Obtener_$request"
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <$methodName xmlns="http://massive.org/">
                    <DataBase>${configuracion.BaseDeDatos}</DataBase>
                    <UserCode>${usuario.Code}</UserCode>
                    <UserIMEI>${configuracion.IMEI}</UserIMEI> 
                    <Cliente>$codigoCliente</Cliente>                   
                </$methodName>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}

fun getXmlPedidoSugerido(request: String, configuracion: DoConfiguracion, usuario: DoUsuario, codigoCliente: String): RequestBody {
    val methodName = "MSV_APP_Obtener_$request"
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <$methodName xmlns="http://massive.org/">
                    <DataBase>${configuracion.BaseDeDatos}</DataBase>
                    <UserCode>${usuario.Code}</UserCode>
                    <UserIMEI>${configuracion.IMEI}</UserIMEI> 
                    <Cliente>$codigoCliente</Cliente>                   
                </$methodName>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}


fun getXmlRequestBodyForPendiente(configuracion: DoConfiguracion, usuario: DoUsuario): RequestBody {
    val methodName = "MSV_APP_Configurar_Pendiente"
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <$methodName xmlns="http://massive.org/">
                    <DataBase>${configuracion.BaseDeDatos}</DataBase>
                    <UserCode>${usuario.Code}</UserCode>
                    <UserIMEI>${configuracion.IMEI}</UserIMEI>              
                </$methodName>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}


fun String.toXmlRequestBody(request: String): RequestBody {
    val methodName = "MSV_APP_Obtener_$request"
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <$methodName xmlns="http://massive.org/">
                    <DataBase>MSV_MOVIL_TEST</DataBase>
                    <UserCode>SMC</UserCode>                  
                </$methodName>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}



fun String.toXmlConfirmacionRequestBody(confirmacion: DoConfirmacionGuardado, configuracion: DoConfiguracion, usuario: DoUsuario): RequestBody {
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <MSV_APP_Configurar_Confirmar xmlns="http://massive.org/">
              <DataBase>${configuracion.BaseDeDatos}</DataBase>
              <UserCode>${usuario.Code}</UserCode>
              <UserIMEI>${configuracion.IMEI}</UserIMEI>
              <Tabla>${confirmacion.NombreTabla}</Tabla>
              <Valor>${confirmacion.ClavePrimaria.joinToString(separator = ",")}</Valor>
            </MSV_APP_Configurar_Confirmar>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}



fun String.toXmlReinicializacionRequestBody(configuracion: DoConfiguracion, usuario: DoUsuario): RequestBody {
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <MSV_APP_Configurar_Reinicializar xmlns="http://massive.org/">
              <DataBase>${configuracion.BaseDeDatos}</DataBase>
              <UserCode>${usuario.Code}</UserCode>
              <UserIMEI>${configuracion.IMEI}</UserIMEI>
            </MSV_APP_Configurar_Reinicializar>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}

fun getXmlEstadoSesionRequestBody(configuracion: DoConfiguracion, usuario: DoUsuario): RequestBody {
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <MSV_APP_Configurar_EstadoSesion xmlns="http://massive.org/">
              <DataBase>${configuracion.BaseDeDatos}</DataBase>
              <UserCode>${usuario.Code}</UserCode>
              <UserIMEI>${configuracion.IMEI}</UserIMEI>
            </MSV_APP_Configurar_EstadoSesion>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}



fun String.toXmlReinicializacionPagosYPedidosRequestBody(configuracion: DoConfiguracion, usuario: DoUsuario): RequestBody {
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <MSV_APP_Configurar_Reinicializar xmlns="http://massive.org/">
              <DataBase>${configuracion.BaseDeDatos}</DataBase>
              <UserCode>${usuario.Code}</UserCode>
              <UserIMEI>${configuracion.IMEI}</UserIMEI>
            </MSV_APP_Configurar_Reinicializar>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}


fun String.ToXmlSendRequestBody(request: String, json: String, configuracion: DoConfiguracion, usuario: DoUsuario): RequestBody{
    val methodName = "MSV_APP_Insertar_$request"
    val xmlBody = """
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                    <$methodName xmlns="http://massive.org/">
                        <DataBase>${configuracion.BaseDeDatos}</DataBase>
                        <UserCode>${usuario.Code}</UserCode>
                        <UserIMEI>${configuracion.IMEI}</UserIMEI>
                        <Json>$json</Json>
                    </$methodName>
                </soap:Body>
            </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}

fun getXmlLoginRequestBody(version: String, usuario: String, password: String, configuracion: DoConfiguracion):RequestBody{
    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <MSV_APP_Obtener_Usuario xmlns="http://massive.org/">
                    <DataBase>${configuracion.BaseDeDatos}</DataBase>
                    <UserCode>$usuario</UserCode>
                    <UserPassword>$password</UserPassword>
                    <UserIMEI>${configuracion.IMEI}</UserIMEI>
                    <Version>$version</Version>
                </MSV_APP_Obtener_Usuario>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}


fun getXmlCerrarSesionRequestBody(usuario: DoUsuario, configuracion: DoConfiguracion):RequestBody{
    val methodName = "MSV_APP_Configurar_CerrarSesion"

    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <$methodName xmlns="http://massive.org/">
                    <DataBase>${configuracion.BaseDeDatos}</DataBase>
                    <UserCode>${usuario.Code}</UserCode>                  
                    <UserIMEI>${configuracion.IMEI}</UserIMEI>
                </$methodName>
            </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}


fun getXmlForConsultaRucRequestBody(configuracion: DoConfiguracion, tipoDocumento: String, numeroDocumento: String): RequestBody {
    val endpointName = if (tipoDocumento == "6") "Sunat" else "Reniec"

    val xmlBody = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <MSV_SocioNegocio_${endpointName}_Receive xmlns="http://massive.org/">
                      <NumeroSistema>MSV_MOVIL_DIST</NumeroSistema>
                      <Sociedad>MSV_MOVIL_DIST</Sociedad>
                      <DocumentoTipo>$tipoDocumento</DocumentoTipo>
                      <DocumentoNumero>$numeroDocumento</DocumentoNumero>
                </MSV_SocioNegocio_${endpointName}_Receive>
              </soap:Body>
        </soap:Envelope>
    """.trimIndent()

    val xmlMediaType = "text/xml".toMediaType()
    return RequestBody.create(xmlMediaType, xmlBody)
}







fun getSerialID(context: Context):String =
    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)





private fun String.toMediaType(): MediaType {
    return MediaType.parse("text/xml") ?: throw IllegalArgumentException("Invalid media type")
}




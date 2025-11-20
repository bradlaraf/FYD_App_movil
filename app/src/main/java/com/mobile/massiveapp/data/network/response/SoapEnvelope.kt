package com.mobile.massiveapp.data.network.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root


@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelope @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBody? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBody @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ArticuloResponse", required = false)
    var response: MSVAppObtenerArticuloResponse? = null
)

@Root(name = "MSV_APP_Obtener_ArticuloResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerArticuloResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ArticuloResult", required = false)
    var result: MSVAppObtenerArticuloResult? = null
)

//Articulos----------------

@Root(name = "MSV_APP_Obtener_ArticuloResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerArticuloResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)





//Socios----------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeClientes @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyClientes? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyClientes @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ClienteSociosResponse", required = false)
    var response: MSVAppObtenerClienteSociosResponse? = null
)

@Root(name = "MSV_APP_Obtener_ClienteSociosResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerClienteSociosResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ClienteSociosResult", required = false)
    var result: MSVAppObtenerClienteSociosResult? = null
)

@Root(name = "MSV_APP_Obtener_ClienteSociosResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerClienteSociosResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)

//Insertar Socios----------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeInsertarClientes @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyInsertarClientes? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyInsertarClientes @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Insertar_ClienteSociosResponse", required = false)
    var response: MSVAppInsertarClienteSociosResponse? = null
)

@Root(name = "MSV_APP_Insertar_ClienteSociosResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppInsertarClienteSociosResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Insertar_ClienteSociosResult", required = false)
    var result: MSVAppObtenerInsertarClienteSociosResult? = null
)

@Root(name = "MSV_APP_Insertar_ClienteSociosResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerInsertarClienteSociosResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)



//Articulos-------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeArticulos @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyArticulos? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyArticulos @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ArticuloResponse", required = false)
    var response: MSVAppObtenerArticulosResponse? = null
)

@Root(name = "MSV_APP_Obtener_ArticuloResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerArticulosResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ArticuloResult", required = false)
    var result: MSVAppObtenerArticulosResult? = null
)

@Root(name = "MSV_APP_Obtener_ArticuloResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerArticulosResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)

//Articulos Cantidades-------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeArticuloCantidades @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyArticuloCantidades? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyArticuloCantidades @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ArticuloCantidadesResponse", required = false)
    var response: MSVAppObtenerArticuloCantidadesResponse? = null
)

@Root(name = "MSV_APP_Obtener_ArticuloCantidadesResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerArticuloCantidadesResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ArticuloCantidadesResult", required = false)
    var result: MSVAppObtenerArticuloCantidadesResult? = null
)

@Root(name = "MSV_APP_Obtener_ArticuloCantidadesResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerArticuloCantidadesResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)


//Articulos Cantidades-------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeArticuloPrecios @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyArticuloPrecios? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyArticuloPrecios @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ArticuloPreciosResponse", required = false)
    var response: MSVAppObtenerArticuloPreciosResponse? = null
)

@Root(name = "MSV_APP_Obtener_ArticuloPreciosResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerArticuloPreciosResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ArticuloPreciosResult", required = false)
    var result: MSVAppObtenerArticuloPreciosResult? = null
)

@Root(name = "MSV_APP_Obtener_ArticuloPreciosResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerArticuloPreciosResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)



//Facturas------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeFacturas @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyFacturas? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyFacturas @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_FacturasCLResponse", required = false)
    var response: MSVAppObtenerFacturasResponse? = null
)

@Root(name = "MSV_APP_Obtener_FacturasCLResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerFacturasResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_FacturasCLResult", required = false)
    var result: MSVAppObtenerFacturasResult? = null
)

@Root(name = "MSV_APP_Obtener_FacturasCLResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerFacturasResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)

//Facturas Detalle------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeFacturasDetalle @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyFacturasDetalle? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyFacturasDetalle @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_FacturasCLDetalleResponse", required = false)
    var response: MSVAppObtenerFacturasDetalleResponse? = null
)

@Root(name = "MSV_APP_Obtener_FacturasCLDetalleResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerFacturasDetalleResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_FacturasCLDetalleResult", required = false)
    var result: MSVAppObtenerFacturasDetalleResult? = null
)

@Root(name = "MSV_APP_Obtener_FacturasCLDetalleResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerFacturasDetalleResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)

//Cliente Contactos------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeClienteContactos @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyClienteContactos? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyClienteContactos @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ClienteSociosContactosResponse", required = false)
    var response: MSVAppObtenerClienteContactosResponse? = null
)

@Root(name = "MSV_APP_Obtener_ClienteSociosContactosResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerClienteContactosResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ClienteSociosContactosResult", required = false)
    var result: MSVAppObtenerClienteContactosResult? = null
)

@Root(name = "MSV_APP_Obtener_ClienteSociosContactosResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerClienteContactosResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)


//Cliente Direcciones------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeClienteDirecciones @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyClienteDirecciones? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyClienteDirecciones @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ClienteSociosDireccionesResponse", required = false)
    var response: MSVAppObtenerClienteDireccionesResponse? = null
)

@Root(name = "MSV_APP_Obtener_ClienteSociosDireccionesResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerClienteDireccionesResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ClienteSociosDireccionesResult", required = false)
    var result: MSVAppObtenerClienteDireccionesResult? = null
)

@Root(name = "MSV_APP_Obtener_ClienteSociosDireccionesResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerClienteDireccionesResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)


//Cerrar Sesion------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeCerrarSesion @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyCerrarSesion? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyCerrarSesion @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Configurar_CerrarSesionResponse", required = false)
    var response: MSVAppObtenerCerrarSesionResponse? = null
)

@Root(name = "MSV_APP_Configurar_CerrarSesionResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerCerrarSesionResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Configurar_CerrarSesionResult", required = false)
    var result: MSVAppObtenerCerrarSesionResult? = null
)

@Root(name = "MSV_APP_Configurar_CerrarSesionResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerCerrarSesionResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)

//Usuario------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeUsuario @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyUsuario? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyUsuario @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_UsuarioResponse", required = false)
    var response: MSVAppObtenerUsuarioResponse? = null
)

@Root(name = "MSV_APP_Obtener_UsuarioResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerUsuarioResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_UsuarioResult", required = false)
    var result: MSVAppObtenerUsuarioResult? = null
)

@Root(name = "MSV_APP_Obtener_UsuarioResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerUsuarioResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)

//Insertar Usuario------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeInsertarUsuario @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyInsertarUsuario? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyInsertarUsuario @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Insertar_UsuariosResponse", required = false)
    var response: MSVAppObtenerInsertarUsuarioResponse? = null
)

@Root(name = "MSV_APP_Insertar_UsuariosResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerInsertarUsuarioResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Insertar_UsuariosResult", required = false)
    var result: MSVAppObtenerInsertarUsuarioResult? = null
)

@Root(name = "MSV_APP_Insertar_UsuariosResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerInsertarUsuarioResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)

//Estado Sesion------------------

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeEstadoSesion @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyEstadoSesion? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyEstadoSesion @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Configurar_EstadoSesionResponse", required = false)
    var response: MSVAppObtenerEstadoSesionResponse? = null
)

@Root(name = "MSV_APP_Configurar_EstadoSesionResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerEstadoSesionResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Configurar_EstadoSesionResult", required = false)
    var result: MSVAppObtenerEstadoSesionResult? = null
)

@Root(name = "MSV_APP_Configurar_EstadoSesionResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerEstadoSesionResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = -1,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)
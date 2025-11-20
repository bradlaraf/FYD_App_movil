package com.mobile.massiveapp.data.network.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

//Reporte Venta Diaria

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeReporteVentaDiaria @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyReporteVentaDiaria? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyReporteVentaDiaria @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteVentaDiariaResponse", required = false)
    var response: MSVAppObtenerReporteVentaDiariaResponse? = null
)

@Root(name = "MSV_APP_Obtener_ReporteVentaDiariaResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteVentaDiariaResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteVentaDiariaResult", required = false)
    var result: MSVAppObtenerReporteVentaDiariaResult? = null
)

@Root(name = "MSV_APP_Obtener_ReporteVentaDiariaResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteVentaDiariaResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)

//Reporte Avance de ventas

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeReporteAvanceVenta @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyReporteAvanceVenta? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyReporteAvanceVenta @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteAvanceVentaResponse", required = false)
    var response: MSVAppObtenerReporteAvanceVentaResponse? = null
)

@Root(name = "MSV_APP_Obtener_ReporteAvanceVentaResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteAvanceVentaResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteAvanceVentaResult", required = false)
    var result: MSVAppObtenerReporteAvanceVentaResult? = null
)

@Root(name = "MSV_APP_Obtener_ReporteAvanceVentaResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteAvanceVentaResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)

//Reporte Detalle Venta

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeReporteDetalleVenta @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyReporteDetalleVenta? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyReporteDetalleVenta @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteDetalleVentaResponse", required = false)
    var response: MSVAppObtenerReporteDetalleVentaResponse? = null
)

@Root(name = "MSV_APP_Obtener_ReporteDetalleVentaResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteDetalleVentaResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteDetalleVentaResult", required = false)
    var result: MSVAppObtenerReporteDetalleVentaResult? = null
)

@Root(name = "MSV_APP_Obtener_ReporteDetalleVentaResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteDetalleVentaResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)


//Reporte Estado Cuenta

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeReporteEstadoCuenta @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyReporteEstadoCuenta? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyReporteEstadoCuenta @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteEstadoCuentaResponse", required = false)
    var response: MSVAppObtenerReporteEstadoCuentaResponse? = null
)

@Root(name = "MSV_APP_Obtener_ReporteEstadoCuentaResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteEstadoCuentaResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteEstadoCuentaResult", required = false)
    var result: MSVAppObtenerReporteEstadoCuentaResult? = null
)

@Root(name = "MSV_APP_Obtener_ReporteEstadoCuentaResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteEstadoCuentaResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)


//Reporte Pre Cobranza

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeReportePreCobranza @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyReportePreCobranza? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyReportePreCobranza @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReportePreCobranzaResponse", required = false)
    var response: MSVAppObtenerReportePreCobranzaResponse? = null
)

@Root(name = "MSV_APP_Obtener_ReportePreCobranzaResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReportePreCobranzaResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReportePreCobranzaResult", required = false)
    var result: MSVAppObtenerReportePreCobranzaResult? = null
)

@Root(name = "MSV_APP_Obtener_ReportePreCobranzaResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReportePreCobranzaResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)


//Reporte Saldos por Cobrar

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapEnvelopeReporteCuentaCobrar @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: SoapBodyReporteCuentaCobrar? = null
)

@Root(name = "Body", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class SoapBodyReporteCuentaCobrar @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteCuentaCobrarResponse", required = false)
    var response: MSVAppObtenerReporteCuentaCobrarResponse? = null
)

@Root(name = "MSV_APP_Obtener_ReporteCuentaCobrarResponse", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteCuentaCobrarResponse @JvmOverloads constructor(
    @field:Element(name = "MSV_APP_Obtener_ReporteCuentaCobrarResult", required = false)
    var result: MSVAppObtenerReporteCuentaCobrarResult? = null
)

@Root(name = "MSV_APP_Obtener_ReporteCuentaCobrarResult", strict = false)
@Namespace(reference = "http://massive.org/")
data class MSVAppObtenerReporteCuentaCobrarResult @JvmOverloads constructor(
    @field:Element(name = "ErrorCodigo", required = false)
    var errorCodigo: Int = 0,

    @field:Element(name = "ErrorMensaje", required = false)
    var errorMensaje: String? = null,

    @field:Element(name = "Json", required = false)
    var json: String? = null
)
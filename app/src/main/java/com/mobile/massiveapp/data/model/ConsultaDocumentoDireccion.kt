package com.mobile.massiveapp.data.model

data class ConsultaDocumentoDireccion(
    val Calle: String?,
    val Codigo: String?,
    val CodigoPostal: String?,
    val DepartamentoCodigo: String?,
    val DepartamentoNombre: String?,
    val DistritoCodigo: String?,
    val DistritoNombre: String?,
    val DistritoNombreAlterno: String?,
    val Estacion: String?,
    val Fecha: String?,
    val Latitud: Double?,
    val Longitud: Double?,
    val Nombre: String?,
    val NumeroDocumento: String?,
    val PaisCodigo: String?,
    val PaisNombre: String?,
    val ProvinciaCodigo: String?,
    val ProvinciaNombre: String?,
    val Referencia: String?,
    val Tipo: String?
){
    constructor(): this(
        Calle = "",
        Codigo = "",
        CodigoPostal = "",
        DepartamentoCodigo = "",
        DepartamentoNombre = "",
        DistritoCodigo = "",
        DistritoNombre = "",
        DistritoNombreAlterno = "",
        Estacion = "",
        Fecha = "",
        Latitud = 0.0,
        Longitud = 0.0,
        Nombre = "",
        NumeroDocumento = "",
        PaisCodigo = "",
        PaisNombre = "",
        ProvinciaCodigo = "",
        ProvinciaNombre = "",
        Referencia = "",
        Tipo = ""
    )
}

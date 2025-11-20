package com.mobile.massiveapp.domain.model



data class DoSocioDniConsulta(
    val apellidoMaterno: String,
    val apellidoPaterno: String,
    val condicion: String,
    val departamento: String,
    val direccion: String,
    val distrito: String,
    val dpto: String,
    val estado: String,
    val interior: String,
    val kilometro: String,
    val lote: String,
    val manzana: String,
    val nombre: String,
    val nombres: String,
    val numero: String,
    val numeroDocumento: String,
    val provincia: String,
    val tipoDocumento: String,
    val ubigeo: String,
    val viaNombre: String,
    val viaTipo: String,
    val zonaCodigo: String,
    val zonaTipo: String
){
    constructor(): this(
        apellidoMaterno = "",
        apellidoPaterno = "",
        condicion = "",
        departamento = "",
        direccion = "",
        distrito = "",
        dpto = "",
        estado = "",
        interior = "",
        kilometro = "",
        lote = "",
        manzana = "",
        nombre = "",
        nombres = "",
        numero = "",
        numeroDocumento = "",
        provincia = "",
        tipoDocumento = "",
        ubigeo = "",
        viaNombre = "",
        viaTipo = "",
        zonaCodigo = "",
        zonaTipo = ""
    )
}


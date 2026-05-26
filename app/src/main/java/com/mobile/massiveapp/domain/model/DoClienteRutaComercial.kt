package com.mobile.massiveapp.domain.model

data class DoClienteRutaComercial(
    val AccDocEntry: String,
    val FechaRuta: String,
    val NombreVendedor: String,
    val CardCode: String
) {
    constructor() : this("", "", "", "")
}

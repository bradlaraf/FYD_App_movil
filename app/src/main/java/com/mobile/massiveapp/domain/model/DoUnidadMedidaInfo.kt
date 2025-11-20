package com.mobile.massiveapp.domain.model

data class DoUnidadMedidaInfo (
    val UomEntry: Int,
    var PrecioFinal: Double
    ){

    constructor(): this(0, 0.0)

}
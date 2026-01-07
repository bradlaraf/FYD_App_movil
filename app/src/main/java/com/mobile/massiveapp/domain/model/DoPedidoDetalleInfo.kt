package com.mobile.massiveapp.domain.model

data class DoPedidoDetalleInfo(
    val ItemCode: String,
    val ItemName: String,
    val LineTotal: Double,
    val Price: Double,
    val UnidadMedida: String,
    val Cantidad: Double,
    val Almacen: String
){
    constructor(): this(
        ItemCode = "",
        ItemName = "",
        LineTotal = 0.0,
        Price = 0.0,
        UnidadMedida = "",
        Cantidad = 0.0,
        Almacen = "",
)
}

data class DoTotalesContenidoView(
    val TotalAntesImpuesto: Double,
    val TotalImpuesto: Double,
    val TotalConImpuesto: Double
)

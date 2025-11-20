package com.mobile.massiveapp.domain.model

data class DoReporteDetalleVentas(
    val Numero: Int,
    val Codigo: String,
    val Articulo: String,
    val Unidad: String,
    var Cantidad: Double,
    val TipoPrecio: String,
    var Precio: Double,
    var Parcial: Double
) {
    constructor() : this(
        0,
        "",
        "",
        "",
        0.0,
        "",
        0.0,
        0.0)
}

data class DoReporteDetalleVentasCabecera(
    val Interno: String,
    val Sunat: String,
    val Fecha: String,
    val Cliente: String,
    var Importe: Double,
    val DocEntry: String
){
    constructor() : this(
        "",
        "",
        "",
        "",
        0.0,
        ""
    )
}

fun DoReporteDetalleVentasCabecera.toDoReporteDetalleVentas() = DoReporteDetalleVentas(
        Codigo = Interno,
        Articulo = Sunat,
        Unidad = Fecha,
        Cantidad = 0.0,
        TipoPrecio = Cliente,
        Precio = 0.0,
        Parcial = Importe,
        Numero = 0
    )

data class ReporteDetalleVenta(
    val CardName: String,
    val DocCur: String,
    val DocDate: String,
    val DocEntry: Int,
    val DocNum: Int,
    val DocTotal: Double,
    val Dscription: String,
    val Indicator: String,
    val ItemCode: String,
    val LineNum: Int,
    val LineTotal: Double,
    val NumAtCard: String,
    val Price: Double,
    val PriceList: String,
    val Quantity: Double,
    val UnitMsr: String
)


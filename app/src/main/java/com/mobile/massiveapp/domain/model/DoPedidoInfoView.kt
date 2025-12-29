package com.mobile.massiveapp.domain.model

data class DoPedidoInfoView(
    val SlpName:String,
    val AccDocEntry:String,
    val CardName:String,
    val DocCur:String,
    val DocDate:String,
    val DocDueDate: String,
    val DocTotal: Double,
    val VatSum:Double,
    val Comments:String,
    val DocStatus:String
){
    constructor(): this(
        SlpName = "",
        AccDocEntry = "",
        CardName = "",
        DocCur = "",
        DocDate = "",
        DocDueDate = "",
        DocTotal = 0.0,
        VatSum = 0.0,
        Comments = "",
        DocStatus = "",
    )
}

data class ArticuloPedido(
    val Precio: Double,
    val PorcentajeDescuento: Double,
    val PrecioUnitario: Double,
    var PrecioIGV: Double
) {
    constructor(): this (
        0.000,
        0.000,
        0.000,
        0.000
    )
}
package com.mobile.massiveapp.domain.model

data class DoReporteVentasDiarias(
    val Interno: String,
    val Sunat: String,
    var Importe: Double,
    val Cliente: String
)

data class DoReporteVentasDiariasCabecera(
    var Total: Double
)


data class ReporteVentasDiarias(
    val CardName: String,
    val DocCur: String,
    val DocDate: String,
    val DocEntry: Int,
    val DocNum: Int,
    val DocTotal: Double,
    val Indicator: String,
    val NumAtCard: String
)

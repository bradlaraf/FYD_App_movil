package com.mobile.massiveapp.domain.model

data class DoReportePreCobranza(
    val Cliente: String,
    val TipoPago: String,
    val Importe: Double,
    val NumeroDocumento: String
) {
    constructor() : this("", "", 0.0,"")
}

data class ReportePreCobranza(
    val CardName: String,
    val DocCur: String,
    val DocDate: String,
    val DocEntry: Int,
    val DocNum: Int,
    val DocTotal: Double,
    val Indicator: String,
    val NumAtCard: String,
    val PaidToDate: Double,
    val PayDate: String,
    val PayDocCur: String,
    val PayDocTotal: Double,
    val PayType: String
)
data class erere(
    val CardName: String,
    val DocCur: String,
    val DocDate: String,
    val DocEntry: Int,
    val DocNum: Int,
    val DocTotal: Double,
    val Indicator: String,
    val NumAtCard: String,
    val PaidToDate: Double,
    val PayDate: String,
    val PayDocCur: String,
    val PayDocTotal: Double,
    val PayType: String
)





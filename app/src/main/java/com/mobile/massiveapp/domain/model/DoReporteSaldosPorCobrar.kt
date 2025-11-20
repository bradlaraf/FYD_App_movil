package com.mobile.massiveapp.domain.model

data class DoReporteSaldosPorCobrar(
    val Clave: String,
    val Sunat: String,
    val Emision: String,
    val Dias: Int,
    val Nombre:String,
    val Direccion: String,
    var Total: Double,
    var Pagado: Double,
    var Saldo: Double
)

data class DoReporteSaldosPorCobrarCabecera(
    val CardCode: String,
    val CardName: String
)

data class DoReporteSaldosPorCobrarTotales(
    var Total: Double,
    var Pagado: Double,
    var Saldo: Double
)

data class ReporteSaldosPorCobrar(
    val Address: String = "--",
    val CardCode: String = "--",
    val CardName: String = "--",
    val Days: Int = 0,
    val DocCur: String = "--",
    val DocDate: String = "--",
    val DocEntry: Int = 0,
    val DocNum: Int = 0,
    val DocTotal: Double = 0.0,
    val Indicator: String = "--",
    val NumAtCard: String = "--",
    val PaidToDate: Double = 0.0
)
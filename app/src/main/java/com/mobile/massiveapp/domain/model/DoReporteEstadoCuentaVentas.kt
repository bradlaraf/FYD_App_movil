package com.mobile.massiveapp.domain.model

data class DoReporteEstadoCuentaVentas (
    val Clave: String,
    val Sunat: String,
    val Condicion: String,
    val Vendedor : String,
    val Emision: String,
    val Moneda: String,
    var Total: Double,
    var Saldo: Double,
)

data class DoReporteEstadoCuentaCobranzas(
    val Clave: String,
    val Sunat: String,
    val Condicion: String,
    val Vendedor : String,
    val Emision: String,
    val FechaPago: String,
    val NumeroDias: Int,
    val MonedaPago: String,
    var Pagado: Double,
)

data class DoReporteEstadoCuentaCabecera(
    val Cliente: String,
    val ListaPrecio: String,
    var LimiteCredito: Double,
    val CondicionPago: String
) {
    constructor() : this("", "", 0.0, "")
}

data class ReporteEstadoCuenta(
    val CardName: String,
    val CreditLine: Double,
    val DocCur: String,
    val DocDate: String,
    val DocEntry: Int,
    val DocTotal: Double,
    val Indicator: String,
    val LicTradNum: String,
    val ListName: String,
    val NumAtCard: String,
    val PaidDays: Int,
    val PaidDocCur: String,
    val PaidDocDate: String,
    val PaidDocTotal: Double,
    val PaidToDate: Double,
    val PymntGroup: String,
    val SlpName: String,
    val Type: Int
)



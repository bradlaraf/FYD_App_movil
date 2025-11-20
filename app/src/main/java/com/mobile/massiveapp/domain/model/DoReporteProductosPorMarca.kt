package com.mobile.massiveapp.domain.model

data class DoReporteProductosPorMarca(
    val Producto: String,
    var Stock: Double,
    var PrecioCobertura: Double,
    var PrecioMayorista: Double,

) {
    constructor() : this("", 0.0, 0.0, 0.0)
}

data class DoReporteProductosPorMarcaCabecera(
    val FirmCode: Int,
    val FirmName: String
)

fun DoReporteProductosPorMarcaCabecera.toDoReporteProductosPorMarca(): DoReporteProductosPorMarca =
    DoReporteProductosPorMarca(
        Producto = FirmName,
        Stock = 0.0,
        PrecioCobertura = 0.0,
        PrecioMayorista = 0.0
    )


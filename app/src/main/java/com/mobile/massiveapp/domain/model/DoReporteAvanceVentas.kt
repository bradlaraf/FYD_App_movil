package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.ui.view.util.format

data class DoReporteAvanceVentas (
    val Codigo: String,
    val Articulo: String,
    var Cantidad: Double,
    var Importe: Double
)

data class DoReporteAvanceVentasCabecera (
    val FirmName: String,
    val FirmCode: Int,
    var TotalCantidad: Double,
    var TotalImporte: Double
)


fun DoReporteAvanceVentasCabecera.toDoReporteAvanceVentas(): DoReporteAvanceVentas =
    DoReporteAvanceVentas(
        Codigo = "",
        Articulo = FirmName,
        Cantidad = TotalCantidad,
        Importe = TotalImporte.format(2)
    )

data class ReporteAvanceVentas(
    val Indicator: String ="--",
    val ItemName: String ="--",
    val FirmName: String = "--",
    val NumAtCard: String = "--",
    val LineTotal: String = "--",
    val Quantity: Double = 0.0
)
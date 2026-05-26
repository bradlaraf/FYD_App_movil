package com.mobile.massiveapp.domain.model

data class DoReporteProductosFrecuentes(
    val ItemCode: String,
    val ItemName: String,
    val Quantity: Double
) {
    constructor() : this("", "", 0.0)
}

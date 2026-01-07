package com.mobile.massiveapp.ui.view.cobranzas.uistate

import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import com.mobile.massiveapp.domain.model.DoPagoDetalle

data class PagosDetalleUiState(
    val pagos: List<DoLiquidacionPago> = emptyList(),
    val total: Double = 0.0,
    val cantidad: Int = 0,
    val loading: Boolean = false,
    val error: String? = null
)

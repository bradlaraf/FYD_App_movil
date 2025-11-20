package com.mobile.massiveapp.domain.model

import kotlinx.coroutines.flow.Flow

data class DoDashboardInfo (
    val totalAcumuladoPedidos: Flow<Double>,
    val totalPedidos: Flow<Int>,
    val totalAcumuladoPagos: Flow<Double>,
    val totalPagos: Flow<Int>,
    val totalFacturas: Flow<Int>,
    val totalAcumuladoFacturas: Flow<Double>
)
package com.mobile.massiveapp.domain.dashboard

import com.mobile.massiveapp.data.repositories.DashboardRepository
import com.mobile.massiveapp.ui.view.util.getFechaActual
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDashboardInfoUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {
    val fechaActual = getFechaActual()

    fun getTotalAcumuladoPedidos(): Flow<Double> = dashboardRepository.getTotalAcumuladoPedidos(fechaActual = fechaActual)
    fun getTotalPedidos(): Flow<Int> =  dashboardRepository.getTotalPedidos(fechaActual)
    fun getTotalAcumuladoPagos(): Flow<Double> = dashboardRepository.getTotalAcumuladoPagos(fechaActual)
    fun getTotalPagos(): Flow<Int> = dashboardRepository.getTotalPagos(fechaActual)
    fun getTotalFacturas(): Flow<Int> = dashboardRepository.getTotalFacturas()
    fun getTotalAcumuladoFacturas(): Flow<Double> = dashboardRepository.getTotalAcumuladoFacturas()

}

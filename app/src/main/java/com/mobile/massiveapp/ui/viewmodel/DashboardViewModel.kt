package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mobile.massiveapp.domain.dashboard.GetDashboardInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardInfoUseCase: GetDashboardInfoUseCase
) : ViewModel(){

    //Get Dashboard Info
    val dataTotalAcumuladoPedidos: Flow<Double> = getDashboardInfoUseCase.getTotalAcumuladoPedidos()
    val dataTotalPedidos: Flow<Int> = getDashboardInfoUseCase.getTotalPedidos()
    val dataTotalAcumuladoPagos: Flow<Double> = getDashboardInfoUseCase.getTotalAcumuladoPagos()
    val dataTotalPagos: Flow<Int> = getDashboardInfoUseCase.getTotalPagos()
    val dataTotalFacturas: Flow<Int> = getDashboardInfoUseCase.getTotalFacturas()
    val dataTotalAcumuladoFacturas: Flow<Double> = getDashboardInfoUseCase.getTotalAcumuladoFacturas()

}
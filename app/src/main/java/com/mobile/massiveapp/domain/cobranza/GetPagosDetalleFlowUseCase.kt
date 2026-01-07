package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import com.mobile.massiveapp.domain.model.DoLiquidacionPagoView
import com.mobile.massiveapp.domain.model.DoPagoDetalle
import com.mobile.massiveapp.ui.view.util.SendData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagosDetalleFlowUseCase @Inject constructor(
    val pagosDetalleDao: ClientePagosDetalleDao,
    val liquidacionPagoDao: LiquidacionPagoDao
) {
    fun getAllPagosLiquidacion(): Flow<List<DoLiquidacionPagoView>> = liquidacionPagoDao.getAllViewFlow(SendData.instance.accDocEntryDoc)

    fun getAllPagosDetalle(): Flow<List<DoPagoDetalle>> =  pagosDetalleDao.getAllPagoDetalleXAccDocEntryFlow(
        SendData.instance.accDocEntryDoc)

    fun getTotalPagosDetalle(): Flow<Double> = pagosDetalleDao.getMontoTotalPagoDetalles(SendData.instance.accDocEntryDoc)

    fun getTotalCantidadPagosDetalle(): Flow<Int> = pagosDetalleDao.getCantidadTotalPagoDetalles(SendData.instance.accDocEntryDoc)
}
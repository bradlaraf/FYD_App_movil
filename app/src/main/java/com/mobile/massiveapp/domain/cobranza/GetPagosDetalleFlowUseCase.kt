package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.data.database.dao.ManifiestoDocumentoDao
import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import com.mobile.massiveapp.domain.model.DoLiquidacionPagoView
import com.mobile.massiveapp.domain.model.DoLiquidacionPagosTotales
import com.mobile.massiveapp.domain.model.DoManifiestoDocumentoView
import com.mobile.massiveapp.domain.model.DoPagoDetalle
import com.mobile.massiveapp.ui.view.util.SendData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPagosDetalleFlowUseCase @Inject constructor(
    val pagosDetalleDao: ClientePagosDetalleDao,
    val liquidacionPagoDao: LiquidacionPagoDao,
    val manifiestoDocumentoDao: ManifiestoDocumentoDao
) {
    fun getAllPagosLiquidacion(): Flow<List<DoLiquidacionPagoView>> = liquidacionPagoDao.getAllViewFlow(SendData.instance.docEntryFactura).map { listaPagos->
        listaPagos.mapIndexed {index, pago->
            pago.copy(DocLine = index)
        }
    }

    fun getAllPagosDetalle(): Flow<List<DoPagoDetalle>> =  pagosDetalleDao.getAllPagoDetalleXAccDocEntryFlow(
        SendData.instance.accDocEntryDoc)

    fun getTotalPagosDetalle(): Flow<Double> = pagosDetalleDao.getMontoTotalPagoDetalles(SendData.instance.accDocEntryDoc)

    fun getTotalesPagos(): Flow<DoLiquidacionPagosTotales> = liquidacionPagoDao.getTotalesLiquidacion(SendData.instance.lineIdManifiestoDocumento, SendData.instance.docEntryFactura, SendData.instance.docEntry)

    fun getTotalCantidadPagosDetalle(): Flow<Int> = pagosDetalleDao.getCantidadTotalPagoDetalles(SendData.instance.accDocEntryDoc)

    fun getManifiestosDocumentos():Flow<List<DoManifiestoDocumentoView>> = manifiestoDocumentoDao.getAllManDocViewFlow(SendData.instance.docEntry)

    fun getPagosManifiesto(): Flow<List<DoLiquidacionPagoView>> = liquidacionPagoDao.getAllPagosManifiestoFlow(SendData.instance.docEntry)
}
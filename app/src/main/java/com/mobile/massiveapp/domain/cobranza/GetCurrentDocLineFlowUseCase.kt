package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.ui.view.util.SendData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentDocLineFlowUseCase @Inject constructor(
    private val pagosDetalleDao: ClientePagosDetalleDao
) {
    fun getCurrentDocline(): Flow<Int> = pagosDetalleDao.getCurrentDocLineFlow(SendData.instance.accDocEntryDoc)
}
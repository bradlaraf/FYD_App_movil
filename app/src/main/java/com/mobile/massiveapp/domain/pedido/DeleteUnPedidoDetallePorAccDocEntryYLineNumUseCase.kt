package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class  DeleteUnPedidoDetallePorAccDocEntryYLineNumUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository,
    private val pedidosDetalleDao: ClientePedidosDetalleDao
) {
    suspend operator fun invoke(accDocEntry: String, lineNum: Int): Boolean =
        try {
            /** Primero se repone el isCommited al articulo **/
            /*val pedidoAEliminar = pedidoRepository.getUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry, lineNum)
            pedidoRepository.updateIsCommitedArticulo(pedidoAEliminar.Quantity * -1, pedidoAEliminar.ItemCode)*/


            val deleteSuccess = pedidoRepository.deleteUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry, lineNum)
            if (deleteSuccess){
                /** Se actualiza el LineNum **/
                /** EJEM: Si se eliminó el de indice 0, todos las siguiente a este, osea 1,2,3 etc disminuyen en 1 su LineNum **/

                val pedidosDetalleToUpdateLineNumList = pedidoRepository.getAllPedidoDetallePorAccDocEntry(accDocEntry).filter { it.LineNum > lineNum }

                if (pedidosDetalleToUpdateLineNumList.isNotEmpty()){
                    pedidosDetalleToUpdateLineNumList.forEach {

                        pedidosDetalleDao.updateLineNum(
                            updateLineNum = it.LineNum -1,
                            lineNum = it.LineNum,
                            accDocEntry = accDocEntry
                        )

                        /*pedidoRepository.deleteUnPedidoDetallePorAccDocEntryYLineNum(it.AccDocEntry, it.LineNum)
                        it.LineNum -= 1
                        pedidoRepository.savePedidoDetalle(it)*/
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}

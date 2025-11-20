package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.ClientePagosDao
import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.database.entities.toModel
import com.mobile.massiveapp.data.model.ClientePagos
import com.mobile.massiveapp.data.model.ClientePagosDetalle
import com.mobile.massiveapp.domain.model.DoClientePago
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle
import com.mobile.massiveapp.domain.model.toDomain
import com.mobile.massiveapp.ui.view.util.getFechaActual
import javax.inject.Inject

class CobranzaRepository @Inject constructor(
    private val clientePagosDetalleDao: ClientePagosDetalleDao,
    private val clientePagosDao: ClientePagosDao
){


        //Guardar un detella de cobranza
    suspend fun saveCobranzaDetalle(cobranzaDetalle: DoClientePagoDetalle):Boolean{
        return try {
            clientePagosDetalleDao.saveUnaCobranzaDetalle(cobranzaDetalle.toDatabase())
            true
        } catch (e: Exception) {
            false
        }
    }

        //Guardar una cabecera de cobranza
    suspend fun saveCobranzaCabecera(cobranzaCabecera: ClientePagos):Boolean{
        return try {
            clientePagosDao.saveUnaCobranza(cobranzaCabecera.toDatabase())
            true
        } catch (e: Exception) {
            false
        }
    }



        //Traer todos los pagos cabecera
    suspend fun getAllPagosCabecera():List<DoClientePago>{
        return try {
            clientePagosDao.getAll().map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }


        //Traer todos los pagos detalle
    suspend fun getAllPagosDetalle():List<DoClientePagoDetalle> {
        return try {
            clientePagosDetalleDao.getAll().map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }



        //Traer todos los pagos detalle por DocEntry
    suspend fun getAllPagosDetallePorDocEntry(docEntry: String):List<ClientePagosDetalle>{
        return try {
            clientePagosDetalleDao.getAllPagosDetallePorDocEntry(docEntry).map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }
    }



        //Traer todos los pagos de un cliente
    suspend fun getAllPedidosCabeceraAprobados():List<DoClientePago>{
        return try {
            clientePagosDao.getAllPedidosCabeceraMigrados(getFechaActual()).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

        //Traer un pago cabecera por el AccDocEntry
    suspend fun getPagoCabeceraPorAccDocEntry(accDocEntry: String):DoClientePago =
        try {
            clientePagosDao.getPagoCabeceraPorAccDocEntry(accDocEntry).toDomain()
        } catch (e: Exception) {
            DoClientePago()
        }


        //Traer todos los pagos cabecera No Migrados
    suspend fun getAllPedidosCabeceraNoMigrados():List<DoClientePago>{
        return try {
            clientePagosDao.getAllPagosSinMigrar().map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

        //Traer un pago detalle por accDocEntry y lineNum
    suspend fun getCobranzaDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: String):DoClientePagoDetalle =
        try {
            clientePagosDetalleDao.getCobranzaDetallePorAccDocEntryYLineNum(accDocEntry, lineNum).toDomain()
        } catch (e: Exception) {
            DoClientePagoDetalle()
        }


        //Traer todos los pagos detalle por el AccDocEntry
    suspend fun getAllPagosDetallePorAccDocEntry(accDocEntry: String):List<DoClientePagoDetalle>{
        return try {
            clientePagosDetalleDao.getAllPagosDetallePorAccDocEntry(accDocEntry).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAllPagosDetallePorAccDocEntryParaEliminar(accDocEntry: String):List<DoClientePagoDetalle>{
        return try {
            clientePagosDetalleDao.getAllPagosDetallePorAccDocEntry(accDocEntry).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }


        //Traer el DocLine actual por accDocEntry
    suspend fun getCurrentDocLine(accDocEntry: String):Int{
        return try {
            clientePagosDetalleDao.getCurrentDocLine(accDocEntry)
        } catch (e: Exception) {
            -1
        }
    }

        //Eliminar todos los pedido detalle por accDocEntry
    suspend fun deleteAllPagosDetallesPorAccDocEntry(accDocEntry: String):Boolean =
        try {
            clientePagosDetalleDao.deleteAllPagosDetallesPorAccDocEntry(accDocEntry)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

        //Eliminar Un pago detalle por accDocEntry y DocLine
    suspend fun deleteUnPagoDetallePorAccDocEntryYDocLine(accDocEntry: String, docLine: Int):Boolean =
        try {
            clientePagosDetalleDao.deleteCobranzaDetallePorAccDocEntryYLineNum(accDocEntry, docLine)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }




}
package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.ClienteFacturaDetalleDao
import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.domain.model.DoClienteFacturas
import com.mobile.massiveapp.domain.model.DoFacturaView
import com.mobile.massiveapp.domain.model.toDatabase
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class FacturasRepository @Inject constructor(
    private val facturasDao: ClienteFacturasDao,
    private val facturaDetalleDao: ClienteFacturaDetalleDao
){
        //Traer todaos los detalle de una factura por DocEntry
    suspend fun getAllFacturaDetallePorDocEntry(docEntry: Int) =
        try {
            facturaDetalleDao.getAllDetallesPorDocEntry(docEntry)
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }


        //Traer todas las facturas
    suspend fun getAllFacturasDelVendedor():List<DoFacturaView> =
        try {
            //facturasDao.getAllDelVendedor()
            facturasDao.getAllDelVendedorCZona()
        } catch (e: Exception){
            emptyList()
        }


        //Obtener todas las facturas de un cliente por CardCode
    suspend fun getAllFacturasPorCardCode(cardCode: String):List<DoClienteFacturas> =
        try {
            facturasDao.getAllFacturasPorCardCode(cardCode).map { it.toDomain()}
        } catch (e: Exception){
            emptyList()
        }



        //Guardar el paidToDate por pago
    suspend fun savePaidToDatePorPago(docEntry: String, paidToDate: Double):Boolean =
        try {
            facturasDao.savePaidToDatePorPago(docEntry, paidToDate)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }

    suspend fun updateEditPtd(docEntry: String, editPtd: Double):Boolean =
        try {
            facturasDao.savePaidToDateToEdit(docEntry, editPtd)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }

    suspend fun setEditPtdEqualPaidToCode(docEntry: Int):Boolean =
        try {
            facturasDao.setEditPtdEqualPaidToCode(docEntry)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }

        //Modifica la suma registrada por los pagos detalles eliminados a la factura
    suspend fun sumarPaidToDatePorPagoEliminado(montoRestore: Double, docEntry: Int):Boolean =
        try {
            facturasDao.sumarPaidToDatePorPagoEliminado(docEntry, montoRestore)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    //Modifica la suma registrada por los pagos detalles eliminados a la factura
    suspend fun sumarEditPtdPorPagoEliminado(montoRestore: Double, docEntry: Int):Boolean =
        try {
            facturasDao.sumarEditPtdPorPagoEliminado(docEntry, montoRestore)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }


        //Obtener una factura por DocEntry
    suspend fun getFacturaPorDocEntry(docEntry: String): DoClienteFacturas =
        try {
            facturasDao.getFacturaPorDocEntry(docEntry).toDomain()
        } catch (e: Exception){
            DoClienteFacturas()
        }

        //Guardar una factura
    suspend fun saveFactura(factura: DoClienteFacturas):Boolean =
        try {
            facturasDao.saveFactura(factura.toDatabase())
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }

    suspend fun updateFactura(factura: DoClienteFacturas):Boolean =
        try {
            facturasDao.update(factura.toDatabase())
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }


        //Eliminar una factura por DocEntry
    suspend fun deleteFacturaPorDocEntry(docEntry: String):Boolean =
        try {
            facturasDao.deleteFacturaPorDocEntry(docEntry)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }






}
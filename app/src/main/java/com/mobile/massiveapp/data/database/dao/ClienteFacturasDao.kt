package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ClienteFacturasEntity
import com.mobile.massiveapp.domain.model.DoFacturaView
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteFacturasDao:BaseDao<ClienteFacturasEntity>{

    @Query("""
        SELECT 
            T0.CardName, 
            T0.CardCode,
            T0.DocCur, 
            T0.DocEntry, 
            T0.DocDate, 
            T0.DocTotal, 
            T0.FolioNum, 
            T0.FolioPref, 
            T0.PaidToDate
        FROM Factura T0
        ORDER BY T0.CardName, T0.FolioPref, T0.FolioNum
    """)
    suspend fun getAllDelVendedor(): List<DoFacturaView>

    @Query("""
    SELECT 
        T0.CardName, 
        T0.CardCode,
        T0.DocCur, 
        T0.DocEntry, 
        T0.DocDate, 
        T0.DocTotal, 
        T0.FolioNum, 
        T0.FolioPref, 
        T0.PaidToDate
    FROM Factura T0
    WHERE T0.SlpCode IN (SELECT Z0.DefaultSlpCode FROM Usuario Z0) 
    ORDER BY T0.CardName, T0.FolioPref, T0.FolioNum
""")
    fun getAllFacturasFlow(): Flow<List<DoFacturaView>>

    @Query("DELETE FROM Factura")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<ClienteFacturasEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: ClienteFacturasEntity)

    @Update
    suspend fun updateList(data: List<ClienteFacturasEntity>)

    @Update
    suspend fun update(data: ClienteFacturasEntity)


        //Insertar una factura
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFactura(factura: ClienteFacturasEntity)


        //Eliminar una factura por DocEntry
    @Query("DELETE FROM Factura WHERE DocEntry = :docEntry")
    suspend fun deleteFacturaPorDocEntry(docEntry: String)

        //Obtener todos las facturas por cardCode
    @Query("""
            SELECT 
                * 
            FROM Factura T0 
            WHERE T0.CardCode = :cardCode AND T0.SlpCode IN (SELECT Z0.DefaultSlpCode FROM Usuario Z0) AND PaidToDate != 0.0
        """)
    suspend fun getAllFacturasPorCardCode(cardCode: String):List<ClienteFacturasEntity>

    @Query("""
            SELECT 
                * 
            FROM Factura T0 
            WHERE T0.CardCode = :cardCode AND PaidToDate != 0.0
        """)
    suspend fun getAllFacturasPorCardCodeSuperUser(cardCode: String):List<ClienteFacturasEntity>

        //Guardar el paidToDate por pago
    @Query("UPDATE Factura SET PaidToDate = :paidToDate WHERE DocEntry = :docEntry")
    suspend fun savePaidToDatePorPago(docEntry: String, paidToDate: Double)

    @Query("UPDATE Factura SET Edit_Ptd = :editPtd WHERE DocEntry = :docEntry")
    suspend fun savePaidToDateToEdit(docEntry: String, editPtd: Double)

        // Se iguala el valor del editPtd al de paidToDate en la factura
    @Query("UPDATE Factura SET Edit_Ptd = PaidToDate WHERE DocEntry = :docEntry AND Edit_Ptd == -11.0")
    suspend fun setEditPtdEqualPaidToCode(docEntry: Int)

    //Se setea el valor de EditPtd a su valor default
    @Query("UPDATE Factura SET Edit_Ptd = -11.0 WHERE Edit_Ptd != -11.0")
    suspend fun setEditPtdToDeafult()


        //Sumar al paidToDate por pago
    @Query("UPDATE Factura SET PaidToDate = PaidToDate + :paidToDate WHERE DocEntry = :docEntry")
    suspend fun sumarPaidToDatePorPagoEliminado(docEntry: Int, paidToDate: Double)

    @Query("UPDATE Factura SET PaidToDate = PaidToDate - :paidToDate WHERE DocEntry = :docEntry")
    suspend fun restarPaidToDate(docEntry: Int, paidToDate: Double)

    //Sumar al editPtd por pago
    @Query("UPDATE Factura SET Edit_Ptd = :editPtd WHERE DocEntry = :docEntry")
    suspend fun sumarEditPtdPorPagoEliminado(docEntry: Int, editPtd: Double)

        //Obtener una factura por DocEntry
    @Query("SELECT * FROM Factura WHERE DocEntry = :docEntry")
    suspend fun getFacturaPorDocEntry(docEntry: String): ClienteFacturasEntity

    //Udate una factura EditPtd por AccDocEntry
    @Query("SELECT * FROM Factura WHERE DocEntry IN (SELECT Z0.DocEntry FROM ClientePagosDetalle Z0 WHERE AccDocEntry = :accDocEntry)")
    suspend fun getAllFacturasPorAccDocEntry(accDocEntry: String): List<ClienteFacturasEntity>


}
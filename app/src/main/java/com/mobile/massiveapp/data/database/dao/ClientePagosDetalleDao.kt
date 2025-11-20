package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ClientePagosDetalleEntity
import com.mobile.massiveapp.domain.model.DoPagoDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientePagosDetalleDao:BaseDao<ClientePagosDetalleEntity> {

    @Query("SELECT * FROM ClientePagosDetalle")
    suspend fun getAll(): List<ClientePagosDetalleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ClientePagosDetalleEntity>)

    @Query("DELETE FROM ClientePagosDetalle")
    suspend fun clearAll()

    @Update
    suspend fun update(items: List<ClientePagosDetalleEntity>)

    @Update
    suspend fun update(items: ClientePagosDetalleEntity)


        //Guardar un detella de cobranza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUnaCobranzaDetalle(cobranzaDetalle: ClientePagosDetalleEntity)


        //Traer todos los pagos detalle por el AccDocEntry
    @Query("SELECT * FROM ClientePagosDetalle WHERE AccDocEntry = :accDocEntry")
    suspend fun getAllPagosDetallePorAccDocEntry(accDocEntry: String): List<ClientePagosDetalleEntity>

    @Query("""
        SELECT  
        T0.SumApplied,
        T0.AccCreateDate,
        T0.DocLine,
        T0.DocEntry,
        T1.FolioPref || '-' || T1.FolioNum AS 'NumeroFactura'
        FROM ClientePagosDetalle T0
        INNER JOIN Factura T1 ON T0.DocEntry = T1.DocEntry
        WHERE AccDocEntry = :accDocEntry
    """)
    suspend fun getAllPagoDetalleXAccDocEntry(accDocEntry: String): List<DoPagoDetalle>

    @Query("""
        SELECT  
        T0.SumApplied,
        T0.AccCreateDate,
        T0.DocLine,
        T0.DocEntry,
        T1.FolioPref || '-' || T1.FolioNum AS 'NumeroFactura'
        FROM ClientePagosDetalle T0
        INNER JOIN Factura T1 ON T0.DocEntry = T1.DocEntry
        WHERE AccDocEntry = :accDocEntry
    """)
    fun getAllPagoDetalleXAccDocEntryFlow(accDocEntry: String): Flow<List<DoPagoDetalle>>
    @Query("""
        SELECT  
        IFNULL(SUM(T0.SumApplied), 0.0) as 'Total'
        FROM ClientePagosDetalle T0 
        WHERE AccDocEntry = :accDocEntry
    """)
    fun getMontoTotalPagoDetalles(accDocEntry: String): Flow<Double>

    @Query("""
        SELECT  
        IFNULL(SUM(T0.SumApplied), 0.0) as 'Total'
        FROM ClientePagosDetalle T0 
        WHERE AccDocEntry = :accDocEntry
    """)
    fun getMontoTotalDetalle(accDocEntry: String): Double

    @Query("""
        SELECT  
        IFNULL(COUNT(*), 0)
        FROM ClientePagosDetalle T0 
        WHERE T0.AccDocEntry = :accDocEntry
    """)
    fun getCantidadTotalPagoDetalles(accDocEntry: String): Flow<Int>


        //Obtener el DocLine actual por accDocEntry
    @Query("SELECT COUNT(*) FROM ClientePagosDetalle WHERE AccDocEntry = :accDocEntry")
    suspend fun getCurrentDocLine(accDocEntry: String): Int

    @Query("SELECT COUNT(*) FROM ClientePagosDetalle WHERE AccDocEntry = :accDocEntry")
    fun getCurrentDocLineFlow(accDocEntry: String): Flow<Int>



        //Obtener todos los pagos detalle por DocEntry
    @Query("SELECT * FROM ClientePagosDetalle WHERE DocEntry = :docEntry")
    suspend fun getAllPagosDetallePorDocEntry(docEntry: String): List<ClientePagosDetalleEntity>



        //Obtener un pago detalle por accDocEntry y lineNum
    @Query("SELECT * FROM ClientePagosDetalle WHERE AccDocEntry = :accDocEntry AND DocLine = :lineNum")
    suspend fun getCobranzaDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: String): ClientePagosDetalleEntity

    @Query("""SELECT 
                * 
              FROM ClientePagosDetalle 
              WHERE AccDocEntry = :accDocEntry AND DocEntry = :docEntry""")
    suspend fun getPagoDetalle(accDocEntry: String, docEntry: Int): List<ClientePagosDetalleEntity>

        //Eliminar todos los pagos detalles por accDocEntry
    @Query("DELETE FROM ClientePagosDetalle WHERE AccDocEntry = :accDocEntry")
    suspend fun deleteAllPagosDetallesPorAccDocEntry(accDocEntry: String)


    @Query("DELETE FROM ClientePagosDetalle WHERE DocEntry = :docEntry")
    suspend fun deleteAllPagosDetallesPorDocEntry(docEntry: String)

        //Eliminar todos los pagos detalles sin Cabecera
    @Query("DELETE FROM ClientePagosDetalle WHERE AccDocEntry NOT IN (SELECT AccDocEntry FROM ClientePagos)")
    suspend fun deleteAllPagosDetallesSinCabecera()


        //Eliminar un pago detalle por accDocEntry y lineNum
    @Query("DELETE FROM ClientePagosDetalle WHERE AccDocEntry = :accDocEntry AND DocLine = :lineNum")
    suspend fun deleteCobranzaDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: Int)





}
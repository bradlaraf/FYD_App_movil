package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.LiquidacionPagoEntity
import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import com.mobile.massiveapp.domain.model.DoLiquidacionPagoView
import com.mobile.massiveapp.domain.model.DoLiquidacionPagosTotales
import kotlinx.coroutines.flow.Flow

@Dao
interface LiquidacionPagoDao:BaseDao<LiquidacionPagoEntity> {
    @Query("SELECT * FROM LiquidacionPago")
    fun getAllFlow(): Flow<List<LiquidacionPagoEntity>>

    @Query("""
        SELECT 
            IFNULL(T0.DocLine, -1) AS DocLine,
            IFNULL(T0.U_MSV_MA_CLAVE, -1) AS DocEntryFactura,
            IFNULL((SELECT Z0.NumAtCard FROM Factura Z0 WHERE Z0.DocEntry = T0.U_MSV_MA_CLAVE), '') AS SUNAT,
            IFNULL(T0.U_MSV_MA_FECHA, '') AS FechaCreacion,
            IFNULL(T0.U_MSV_MA_IMP, 0.0) AS Monto,
            IFNULL((
                SELECT 
                    ROUND(Z0.PaidToDate - T0.U_MSV_MA_IMP, 2) 
                FROM Factura Z0 
                WHERE Z0.DocEntry = T0.U_MSV_MA_CLAVE
            ), 0.0) AS Saldo,
            IFNULL((SELECT Z0.Name FROM FormaPago Z0 WHERE Z0.Code = T0.U_MSV_MA_MEDIO),'') AS TipoPago,
            IFNULL('SOL','') AS Moneda,
            IFNULL(T0.AccMigrated,'') AS AccMigrated,
            IFNULL(T0.U_MSV_MA_NROOPE, '') AS NroOperacion,
            IFNULL(T0.DocEntry, -1) AS DocEntry,
            IFNULL(T0.AccFinalized, '') AS AccFinalized,
            IFNULL(T0.AccDocEntry, '') AS AccDocEntry,
            IFNULL(T0.Canceled, '') AS Canceled
        FROM LiquidacionPago T0
        WHERE T0.U_MSV_MA_CLAVE = :code
        """)
    fun getAllViewFlow(code: Int): Flow<List<DoLiquidacionPagoView>>

    @Query("""
        SELECT
            T0.*
        FROM LiquidacionPago T0
        WHERE T0.AccDocEntry = :accDocEntry
    """)
    suspend fun getLiquidacionPago(accDocEntry: String):LiquidacionPagoEntity

    @Query("""
        SELECT 
            IFNULL(T0.DocLine, -1) AS DocLine,
            IFNULL(T0.U_MSV_MA_CLAVE, -1) AS DocEntryFactura,
            IFNULL((SELECT Z0.U_MSV_MA_SUNAT FROM ManifiestoDocumento Z0 WHERE Z0.U_MSV_MA_CLAVE = T0.U_MSV_MA_CLAVE), '') AS SUNAT,
            IFNULL(T0.U_MSV_MA_FECHA, '') AS FechaCreacion,
            IFNULL(T0.U_MSV_MA_IMP, 0.0) AS Monto,
            IFNULL((
                SELECT 
                    ROUND(Z0.PaidToDate - T0.U_MSV_MA_IMP, 2) 
                FROM Factura Z0 
                WHERE Z0.DocEntry = T0.U_MSV_MA_CLAVE
            ), 0.0) AS Saldo,
            IFNULL((SELECT Z0.Name FROM FormaPago Z0 WHERE Z0.Code = T0.U_MSV_MA_MEDIO),'') AS TipoPago,
            IFNULL('SOL','') AS Moneda,
            IFNULL(T0.AccMigrated,'') AS AccMigrated,
            IFNULL(T0.U_MSV_MA_NROOPE, '') AS NroOperacion,
            IFNULL(T0.DocEntry, -1) AS DocEntry,
            IFNULL(T0.AccFinalized, '') AS AccFinalized,
            IFNULL(T0.AccDocEntry, '') AS AccDocEntry,
            IFNULL(T0.Canceled, '') AS Canceled
        FROM LiquidacionPago T0
        INNER JOIN Manifiesto T1 ON T1.DocEntry = T0.U_MSV_MA_MANIF
        WHERE T1.DocEntry = :docEntry
        """)
    suspend fun getAllPagosManifiesto(docEntry: Int): List<DoLiquidacionPagoView>

    @Query("""
        SELECT 
            IFNULL(T0.DocLine, -1) AS DocLine,
            IFNULL(T0.U_MSV_MA_CLAVE, -1) AS DocEntryFactura,
            IFNULL((SELECT Z0.U_MSV_MA_SUNAT FROM ManifiestoDocumento Z0 WHERE Z0.U_MSV_MA_CLAVE = T0.U_MSV_MA_CLAVE), '') AS SUNAT,
            IFNULL(T0.U_MSV_MA_FECHA, '') AS FechaCreacion,
            IFNULL(T0.U_MSV_MA_IMP, 0.0) AS Monto,
            IFNULL((
                SELECT 
                    ROUND(Z0.PaidToDate - T0.U_MSV_MA_IMP, 2) 
                FROM Factura Z0 
                WHERE Z0.DocEntry = T0.U_MSV_MA_CLAVE
            ), 0.0) AS Saldo,
            IFNULL((SELECT Z0.Name FROM FormaPago Z0 WHERE Z0.Code = T0.U_MSV_MA_MEDIO),'') AS TipoPago,
            IFNULL('SOL','') AS Moneda,
            IFNULL(T0.AccMigrated,'') AS AccMigrated,
            IFNULL(T0.U_MSV_MA_NROOPE, '') AS NroOperacion,
            IFNULL(T0.DocEntry, -1) AS DocEntry,
            IFNULL(T0.AccFinalized, '') AS AccFinalized,
            IFNULL(T0.AccDocEntry, '') AS AccDocEntry,
            IFNULL(T0.Canceled, '') AS Canceled
        FROM LiquidacionPago T0
        INNER JOIN Manifiesto T1 ON T1.DocEntry = T0.U_MSV_MA_MANIF
        WHERE T1.DocEntry = :docEntry
        """)
    fun getAllPagosManifiestoFlow(docEntry: Int): Flow<List<DoLiquidacionPagoView>>

    @Query("""
        SELECT 
            ROUND(
                        (
                            ROUND(
                                (
                                    CASE 
                                        WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                                            THEN IFNULL(U_MSV_MA_TOTCRELOC, 0)
                                        ELSE U_MSV_MA_TOTCONLOC
                                    END
                                ) -
                                (
                                    CASE 
                                        WHEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0) = 0.0
                                            THEN IFNULL(U_MSV_MA_TOTCREEXT, 0.0)
                                        ELSE U_MSV_MA_TOTCONEXT
                                    END
                                )
                            , 2)
                         - SUM(
                            (
                                IFNULL(
                                    (
                                    SELECT
                                        SUM(Z0.U_MSV_MA_IMP) 
                                    FROM LiquidacionPago Z0 
                                    WHERE Z0.U_MSV_MA_CLAVE = (SELECT X0.U_MSV_MA_CLAVE FROM LiquidacionPago X0 WHERE X0.AccDocEntry = :accDocEntry)
                                    AND Z0.Canceled <> 'Y'
                                    AND Z0.AccMigrated = 'N'
                                    AND Z0.DocEntry = -1
                                    ), 0.0)
                            )
                        )) 
                    ,2 ) AS MontoPendiente 
        FROM ManifiestoDocumento T0
        WHERE T0.DocEntry = (SELECT X0.U_MSV_MA_MANIF FROM LiquidacionPago X0 WHERE X0.AccDocEntry = :accDocEntry)
        AND T0.U_MSV_MA_CLAVE = (SELECT X0.U_MSV_MA_CLAVE FROM LiquidacionPago X0 WHERE X0.AccDocEntry = :accDocEntry)
    """)
    suspend fun getMontoPendientePago(accDocEntry: String): Double

    @Query("""
        SELECT
            ROUND(
                (   IFNULL(
                    (
                        SELECT
                            SUM(Z0.U_MSV_MA_IMP) 
                        FROM LiquidacionPago Z0 
                        WHERE Z0.U_MSV_MA_CLAVE = :docEntryFactura
                        AND Z0.Canceled <> 'Y'
                    ) ,0.0)
                )
            , 2 ) AS TotalCobrado,
            ROUND(
                (
                    ROUND(
                        (
                            CASE 
                                WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                                    THEN IFNULL(U_MSV_MA_TOTCRELOC, 0)
                                ELSE U_MSV_MA_TOTCONLOC
                            END
                        ) -
                        (
                            CASE 
                                WHEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0) = 0.0
                                    THEN IFNULL(U_MSV_MA_TOTCREEXT, 0.0)
                                ELSE U_MSV_MA_TOTCONEXT
                            END
                        )
                    , 2)
                 - SUM(
                    (
                        IFNULL(
                            (
                            SELECT
                                SUM(Z0.U_MSV_MA_IMP) 
                            FROM LiquidacionPago Z0 
                            WHERE Z0.U_MSV_MA_CLAVE = :docEntryFactura
                            AND Z0.Canceled <> 'Y'
                            AND Z0.AccMigrated = 'N'
                            AND Z0.DocEntry = -1
                            ), 0.0)
                    )
                )) 
            ,2 ) AS TotalPorCobrar 
        FROM ManifiestoDocumento T0
        WHERE T0.DocEntry = :docEntry
        AND T0.LineId = :lineId

    """)
    fun getTotalesLiquidacion(lineId:Int, docEntryFactura: Int, docEntry:Int): Flow<DoLiquidacionPagosTotales>

    @Query("""
        SELECT (
        (
            SELECT 
                COUNT(*) 
            FROM LiquidacionPago T0 
            WHERE T0.U_MSV_MA_CLAVE = :docEntryFactura
        )) AS LineNum
    """)
    suspend fun getLineNumeLiquidacion(docEntryFactura: Int): Int

    @Query("""
        SELECT 
            * 
        FROM LiquidacionPago 
        WHERE AccDocEntry = :accDocEntry 
            AND DocLine = :docLine
        LIMIT 1
    """)
    suspend fun getLiquidacionPago(accDocEntry: String, docLine: Int): LiquidacionPagoEntity

    @Query("""
        SELECT 
            * 
        FROM LiquidacionPago 
        WHERE DocLine = :docLine
            AND U_MSV_MA_CLAVE = (  SELECT 
                                        Z0.U_MSV_MA_CLAVE 
                                    FROM ManifiestoDocumento Z0 
                                    WHERE Z0.LineId = :lineId 
                                        AND Z0.U_MSV_MA_CLAVE = :docEntryFactura
                                )
    """)
    suspend fun getLiquidacionPago(docEntryFactura: Int, docLine: Int ,lineId: Int): LiquidacionPagoEntity


    @Query("SELECT * FROM LiquidacionPago WHERE AccDocEntry = :code")
    suspend fun getAllByCode(code: String): List<LiquidacionPagoEntity>

    @Query("""
        SELECT 
            *
        FROM LiquidacionPago T0
        WHERE T0.U_MSV_MA_CLAVE = :docEntryFactura
            AND T0.AccMigrated = "N" OR T0.Canceled = "Y"
        """)
    suspend fun getAllPagos(docEntryFactura: Int): List<LiquidacionPagoEntity>

    @Query("SELECT * FROM LiquidacionPago")
    suspend fun getAll(): List<LiquidacionPagoEntity>

    @Query("DELETE FROM LiquidacionPago")
    suspend fun clearAll()

    @Query("""
        UPDATE LiquidacionPago
        SET 
            Canceled = 'Y',
            AccAction = 'U',
            AccFinalized = 'N'
        WHERE AccDocEntry = :accDocEntry
        """)
    suspend fun deletePago(accDocEntry: String)

    @Query("""
        DELETE FROM LiquidacionPago
        WHERE EditableMovil = 'Y'
        """)
    suspend fun deletePagos()

    @Query("""
        UPDATE LiquidacionPago 
        SET DocLine = :updateDocLine
        WHERE AccDocEntry = :accDocEntry
    """)
    suspend fun updateDocLine(updateDocLine: Int,  accDocEntry: String)

    @Query("""
        DELETE FROM LiquidacionPago
        WHERE AccDocEntry = :accDocEntry
    """)
    suspend fun deletePagoLiquidacion(accDocEntry: String)

    @Query("""
        SELECT 
            *
        FROM LiquidacionPago T0
        WHERE T0.U_MSV_MA_CLAVE = :docEntryFactura
        ORDER BY T0.DocLine ASC 
    """)
    suspend fun getAllLiquidacionesPorAccDocEntry(docEntryFactura: Int):List<LiquidacionPagoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LiquidacionPagoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: LiquidacionPagoEntity)

    @Update
    suspend fun update(items: List<LiquidacionPagoEntity>)
}
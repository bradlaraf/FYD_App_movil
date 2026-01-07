package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.LiquidacionPagoEntity
import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import com.mobile.massiveapp.domain.model.DoLiquidacionPagoView
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
            ), 0.0) AS Saldo
        FROM LiquidacionPago T0
        WHERE T0.AccDocEntry = :code
            AND T0.EditableMovil = "Y"
        """)
    fun getAllViewFlow(code: String): Flow<List<DoLiquidacionPagoView>>

    @Query("""
        SELECT (
        (
            SELECT 
                COUNT(*) 
            FROM LiquidacionPago 
            WHERE AccDocEntry = :accDocEntry 
                AND EditableMovil = 'Y')) AS LineNum
    """)
    suspend fun getLineNumeLiquidacion(accDocEntry: String): Int

    @Query("""
        SELECT 
            * 
        FROM LiquidacionPago 
        WHERE AccDocEntry = :accDocEntry 
            AND DocLine = :docLine
        LIMIT 1
    """)
    suspend fun getLiquidacionPago(accDocEntry: String, docLine: Int): LiquidacionPagoEntity


    @Query("SELECT * FROM LiquidacionPago WHERE AccDocEntry = :code")
    suspend fun getAllByCode(code: String): List<LiquidacionPagoEntity>

    @Query("SELECT * FROM LiquidacionPago")
    suspend fun getAll(): List<LiquidacionPagoEntity>

    @Query("DELETE FROM LiquidacionPago")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LiquidacionPagoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: LiquidacionPagoEntity)

    @Update
    suspend fun update(items: List<LiquidacionPagoEntity>)
}
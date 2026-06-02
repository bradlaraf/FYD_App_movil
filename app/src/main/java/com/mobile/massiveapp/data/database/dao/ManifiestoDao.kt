package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ManifiestoEntity
import com.mobile.massiveapp.domain.model.DoManifiestoView
import kotlinx.coroutines.flow.Flow

@Dao
interface ManifiestoDao:BaseDao<ManifiestoEntity> {
    @Query("SELECT * FROM Manifiesto")
    suspend fun getAll(): List<ManifiestoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sociedad: ManifiestoEntity)

    @Query("""
        SELECT
            T0.DocEntry AS DocEntry,
            T0.U_MSV_MA_FECSALIDA AS FechaSalida,
            'SOL' AS Moneda,
            (   SELECT
                    ROUND(
                        CASE
                            WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                                THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCRELOC), 0.0)
                                ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONLOC), 0.0)
                            END,2 ) AS TotalPendiente
                FROM ManifiestoDocumento Z0
                WHERE Z0.DocEntry = T0.DocEntry) AS MontoPendiente,

           (    SELECT
                    ROUND(
                        CASE
                            WHEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0) = 0.0
                                THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCREEXT), 0.0)
                                ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONEXT), 0.0)
                        END,2 )AS TotalCobrado
                FROM ManifiestoDocumento Z0
                WHERE Z0.DocEntry = T0.DocEntry) AS MontoCobrado
        FROM Manifiesto T0
    """)
    suspend fun getAllManifiestosView(): List<DoManifiestoView>

    @Query("""
        SELECT
            T0.DocEntry AS DocEntry,
            T0.U_MSV_MA_FECSALIDA AS FechaSalida,
            'SOL' AS Moneda,
            (   SELECT
                    ROUND(
                        CASE
                            WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                                THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCRELOC), 0.0)
                                ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONLOC), 0.0)
                            END,2 ) AS TotalPendiente
                FROM ManifiestoDocumento Z0
                WHERE Z0.DocEntry = T0.DocEntry) AS MontoPendiente,

           (    SELECT
                    ROUND(
                        CASE
                            WHEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0) = 0.0
                                THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCREEXT), 0.0)
                                ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONEXT), 0.0)
                        END,2 )AS TotalCobrado
                FROM ManifiestoDocumento Z0
                WHERE Z0.DocEntry = T0.DocEntry) AS MontoCobrado
        FROM Manifiesto T0
        WHERE T0.DocEntry = :docEntry
    """)
    suspend fun getManifiestosView(docEntry: Int): DoManifiestoView

    @Query("""
        SELECT
            T0.DocEntry AS DocEntry,
            T0.U_MSV_MA_FECSALIDA AS FechaSalida,
            'SOL' AS Moneda,
            (   SELECT ROUND(CASE
                        WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                            THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCRELOC), 0.0)
                            ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONLOC), 0.0)
                        END, 2)
                FROM ManifiestoDocumento Z0
                WHERE Z0.DocEntry = T0.DocEntry) AS MontoPendiente,
            (   SELECT ROUND(CASE
                        WHEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0) = 0.0
                            THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCREEXT), 0.0)
                            ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONEXT), 0.0)
                        END, 2)
                FROM ManifiestoDocumento Z0
                WHERE Z0.DocEntry = T0.DocEntry) AS MontoCobrado
        FROM Manifiesto T0
        WHERE T0.U_MSV_MA_FECSALIDA BETWEEN :fechaInicio AND :fechaFin
          AND (SELECT ROUND(CASE
                    WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                        THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCRELOC), 0.0)
                        ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONLOC), 0.0)
                    END, 2)
               FROM ManifiestoDocumento Z0
               WHERE Z0.DocEntry = T0.DocEntry) > 0
    """)
    fun getManifiestosPendientes(fechaInicio: String, fechaFin: String): Flow<List<DoManifiestoView>>

    @Query("""
        SELECT
            T0.DocEntry AS DocEntry,
            T0.U_MSV_MA_FECSALIDA AS FechaSalida,
            'SOL' AS Moneda,
            (   SELECT ROUND(CASE
                        WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                            THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCRELOC), 0.0)
                            ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONLOC), 0.0)
                        END, 2)
                FROM ManifiestoDocumento Z0
                WHERE Z0.DocEntry = T0.DocEntry) AS MontoPendiente,
            (   SELECT ROUND(CASE
                        WHEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0) = 0.0
                            THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCREEXT), 0.0)
                            ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONEXT), 0.0)
                        END, 2)
                FROM ManifiestoDocumento Z0
                WHERE Z0.DocEntry = T0.DocEntry) AS MontoCobrado
        FROM Manifiesto T0
        WHERE T0.U_MSV_MA_FECSALIDA BETWEEN :fechaInicio AND :fechaFin
          AND (SELECT ROUND(CASE
                    WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                        THEN IFNULL(SUM(Z0.U_MSV_MA_TOTCRELOC), 0.0)
                        ELSE IFNULL(SUM(Z0.U_MSV_MA_TOTCONLOC), 0.0)
                    END, 2)
               FROM ManifiestoDocumento Z0
               WHERE Z0.DocEntry = T0.DocEntry) = 0
    """)
    fun getManifiestosCancelados(fechaInicio: String, fechaFin: String): Flow<List<DoManifiestoView>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<ManifiestoEntity>)

    @Query("DELETE FROM Manifiesto")
    suspend fun clearAll()
}

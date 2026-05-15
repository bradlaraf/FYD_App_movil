package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ManifiestoDocumentoEntity
import com.mobile.massiveapp.domain.model.DoInfoCobranzaManifiesto
import com.mobile.massiveapp.domain.model.DoManifiesto
import com.mobile.massiveapp.domain.model.DoManifiestoDocumentoView
import kotlinx.coroutines.flow.Flow

@Dao
interface ManifiestoDocumentoDao: BaseDao<ManifiestoDocumentoEntity> {
    @Query("SELECT * FROM ManifiestoDocumento")
    suspend fun getAll(): List<ManifiestoDocumentoEntity>

    @Query("DELETE FROM ManifiestoDocumento")
    suspend fun clearAll()

    @Query("""
        SELECT 
            IFNULL(T0.LineId, -1) AS LineId,
            IFNULL(T0.DocEntry, -1) AS DocEntry,
            IFNULL((SELECT SUBSTR(Z0.Name, 4) FROM Indicador Z0 WHERE Z0.Code = T0.U_MSV_MA_TIPODOC), '') AS TipoDocumento,
            T0.U_MSV_MA_SUNAT AS SUNAT,
            IFNULL(T0.U_MSV_MA_SOCNOM, '') AS NombreCliente,
            IFNULL((SELECT Z0.SlpName FROM Vendedor Z0 WHERE Z0.SlpCode = T0.U_MSV_MA_VENDEDOR LIMIT 1), '') AS Vendedor,
            IFNULL(T0.U_MSV_MA_MON, '') AS Moneda,  
            IFNULL((SELECT Z0.CurrName FROM Monedas Z0 WHERE Z0.CurrCode = T0.U_MSV_MA_MON),'') AS MonedaSimbolo,
            IFNULL(T0.U_MSV_MA_CLAVE, -11) AS DocEntryFactura,
            IFNULL(T0.U_MSV_MA_SOCCOD, '') AS CodigoSocio,
            -- TOTAL
            ROUND(
                T0.U_MSV_MA_TOT
            , 2) AS Total,
        
            -- PAGADO
            ROUND(
                CASE 
                    WHEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0) = 0.0
                        THEN IFNULL(U_MSV_MA_TOTCREEXT, 0.0)
                    ELSE U_MSV_MA_TOTCONEXT
                END
            , 2) AS Pagado,
        
            -- SALDO
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
            , 2) AS Saldo
        FROM ManifiestoDocumento T0
        WHERE T0.DocEntry = :docEntry
        ORDER BY T0.U_MSV_MA_SOCNOM
    """)
    suspend fun getAllManDocView(docEntry: Int): List<DoManifiestoDocumentoView>

    @Query("""
        SELECT 
            IFNULL(T0.LineId, -1) AS LineId,
            IFNULL(T0.DocEntry, -1) AS DocEntry,
            IFNULL((SELECT SUBSTR(Z0.Name, 4) FROM Indicador Z0 WHERE Z0.Code = T0.U_MSV_MA_TIPODOC), '') AS TipoDocumento,
            T0.U_MSV_MA_SUNAT AS SUNAT,
            IFNULL(T0.U_MSV_MA_SOCNOM, '') AS NombreCliente,
            IFNULL((SELECT Z0.SlpName FROM Vendedor Z0 WHERE Z0.SlpCode = T0.U_MSV_MA_VENDEDOR LIMIT 1), '') AS Vendedor,
            IFNULL(T0.U_MSV_MA_MON, '') AS Moneda,  
            IFNULL((SELECT Z0.CurrName FROM Monedas Z0 WHERE Z0.CurrCode = T0.U_MSV_MA_MON),'') AS MonedaSimbolo,
            IFNULL(T0.U_MSV_MA_CLAVE, -11) AS DocEntryFactura,
            IFNULL(T0.U_MSV_MA_SOCCOD, '') AS CodigoSocio,
            -- TOTAL
            ROUND(
                T0.U_MSV_MA_TOT
            , 2) AS Total,
        
            -- PAGADO
            ROUND(
                CASE 
                    WHEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0) = 0.0
                        THEN IFNULL(U_MSV_MA_TOTCREEXT, 0.0)
                    ELSE U_MSV_MA_TOTCONEXT
                END
            , 2) AS Pagado,
        
            -- SALDO
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
            , 2) AS Saldo
        FROM ManifiestoDocumento T0
        WHERE T0.DocEntry = :docEntry
        ORDER BY T0.U_MSV_MA_SOCNOM
    """)
    fun getAllManDocViewFlow(docEntry: Int): Flow<List<DoManifiestoDocumentoView>>

    @Query("""
        SELECT 
            IFNULL(T0.DocEntry, -1) AS DocEntry,
            IFNULL(T0.U_MSV_MA_SUNAT, '') AS Comprobante,
            IFNULL(T0.U_MSV_MA_FECEMI, '') AS FechaEmision,
            IFNULL(T0.U_MSV_MA_SOCNOM, '') AS NombreCliente,
            IFNULL(T0.U_MSV_MA_SOCCOD, '') AS CodigoCliente,
            IFNULL(T0.U_MSV_MA_MON, '') AS Moneda,
            -- TOTAL
            ROUND(
                CASE 
                    WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                        THEN IFNULL(U_MSV_MA_TOTCRELOC, 0)
                    ELSE U_MSV_MA_TOTCONLOC
                END
            , 2) AS Total,
        
            -- PAGADO
            ROUND(
                CASE 
                    WHEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0) = 0.0
                        THEN IFNULL(U_MSV_MA_TOTCREEXT, 0.0)
                    ELSE U_MSV_MA_TOTCONEXT
                END
            , 2) AS TotalCobrado,
        
            -- SALDO
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
            , 2) AS TotalPendiente
        FROM ManifiestoDocumento T0
        WHERE T0.U_MSV_MA_CLAVE = :docEntry
    """)
    suspend fun GetInfoCobranzaManifiesto(docEntry: Int): DoInfoCobranzaManifiesto

  /*  @Query("""
        SELECT 
            T0.U_MSV_MA_SUNAT AS Comprobante,
            T0.U_MSV_MA_FECEMI AS FechaEmision,
            T0.U_MSV_MA_SOCCOD AS Cliente,
            T0.U_MSV_MA_SOCNOM AS Nombre,
            T0.U_MSV_MA_MON AS Moneda
        FROM ManifiestoDocumento T0
    """)
    suspend fun getDocumentoManifiestoCobranzaInfo(): DoInfoCobranzaManifiesto*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ManifiestoDocumentoEntity>)

    @Update
    suspend fun update(items: List<ManifiestoDocumentoEntity>)
}
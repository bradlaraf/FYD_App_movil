package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ManifiestoDocumentoEntity
import com.mobile.massiveapp.domain.model.DoManifiestoDocumentoView

@Dao
interface ManifiestoDocumentoDao: BaseDao<ManifiestoDocumentoEntity> {
    @Query("SELECT * FROM ManifiestoDocumento")
    suspend fun getAll(): List<ManifiestoDocumentoEntity>

    @Query("DELETE FROM ManifiestoDocumento")
    suspend fun clearAll()

    @Query("""
        SELECT 
            IFNULL((SELECT SUBSTR(Z0.Name, 4) FROM Indicador Z0 WHERE Z0.Code = T0.U_MSV_MA_TIPODOC), '') AS TipoDocumento,
            T0.U_MSV_MA_SUNAT AS SUNAT,
            T0.U_MSV_MA_SOCNOM AS NombreCliente,
            IFNULL((SELECT Z0.SlpName FROM Vendedor Z0 WHERE Z0.SlpCode = T0.U_MSV_MA_VENDEDOR LIMIT 1), '') AS Vendedor,
            IFNULL(T0.U_MSV_MA_MON, '') AS Moneda,  
            IFNULL((SELECT Z0.CurrName FROM Monedas Z0 WHERE Z0.CurrCode = T0.U_MSV_MA_MON),'') AS MonedaSimbolo,
            IFNULL(T0.DocEntry, -11) AS DocEntry,
            IFNULL(T0.U_MSV_MA_SOCCOD, '') AS CodigoSocio,
            -- TOTAL
            ROUND(
                CASE 
                    WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0) = 0.0
                        THEN IFNULL(U_MSV_MA_TOTCONEXT, 0)
                    ELSE U_MSV_MA_TOTCONLOC
                END
            , 2) AS Total,
        
            -- PAGADO
            ROUND(
                CASE 
                    WHEN IFNULL(U_MSV_MA_TOTCRELOC, 0.0) = 0.0
                        THEN IFNULL(U_MSV_MA_TOTCREEXT, 0.0)
                    ELSE U_MSV_MA_TOTCRELOC
                END
            , 2) AS Pagado,
        
            -- SALDO
            ROUND(
                (
                    CASE 
                        WHEN IFNULL(U_MSV_MA_TOTCONLOC, 0.0) = 0.0
                            THEN IFNULL(U_MSV_MA_TOTCONEXT, 0.0)
                        ELSE U_MSV_MA_TOTCONLOC
                    END
                ) -
                (
                    CASE 
                        WHEN IFNULL(U_MSV_MA_TOTCRELOC, 0) = 0.0
                            THEN IFNULL(U_MSV_MA_TOTCREEXT, 0.0)
                        ELSE U_MSV_MA_TOTCRELOC
                    END
                )
            , 2) AS Saldo
        FROM ManifiestoDocumento T0
        ORDER BY T0.U_MSV_MA_SOCNOM
    """)
    suspend fun getAllManDocView(): List<DoManifiestoDocumentoView>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ManifiestoDocumentoEntity>)

    @Update
    suspend fun update(items: List<ManifiestoDocumentoEntity>)
}
package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.RutaComercialEntity
import com.mobile.massiveapp.domain.model.DoRutaComercialView
import kotlinx.coroutines.flow.Flow

@Dao
interface RutaComercialDao : BaseDao<RutaComercialEntity> {

    @Query("""
        SELECT 
            IFNULL(T0.AccDocEntry, '') AS AccDocEntry,
            IFNULL(T0.DocDate, '') AS FechaRuta,
            IFNULL((SELECT SlpName FROM Vendedor Z0 WHERE Z0.SlpCode = T0.SlpCode), '') AS NombreVendedor,
            IFNULL((
                SELECT 
                    COUNT(Z0.CardCode)
                FROM RutaComercialDetalle Z0
                WHERE Z0.AccDocEntry = T0.AccDocEntry
            ), 0) AS CantidadClientes,
            IFNULL(T0.AccMigrated, '') AS AccMigrated,
            IFNULL(T0.Canceled, '') AS Canceled,
            IFNULL((SELECT Z0.SuperUser FROM Usuario Z0 LIMIT 1), 'N') AS SuperUser
        FROM RutaComercial T0
        WHERE T0.DocDate BETWEEN :fechaInicio AND :fechaFin
        ORDER BY T0.DocDate DESC
    """)
    fun getAllFlow(fechaInicio: String, fechaFin: String): Flow<List<DoRutaComercialView>>

    @Query("""
        SELECT 
            * 
        FROM RutaComercial T0 
        WHERE (SELECT SlpName FROM Vendedor Z0 WHERE Z0.SlpCode = T0.SlpCode) LIKE '%' || :query || '%' 
        ORDER BY DocDate DESC
    """)
    fun getBySearchFlow(query: String): Flow<List<RutaComercialEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<RutaComercialEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data:RutaComercialEntity)

    @Query("SELECT * FROM RutaComercial WHERE AccDocEntry = :accDocEntry")
    suspend fun getByAccDocEntry(accDocEntry: String): RutaComercialEntity?

    @Query("UPDATE RutaComercial SET DocDate = :fechaRuta, AccAction = 'U' WHERE AccDocEntry = :accDocEntry")
    suspend fun updateFechaRuta(accDocEntry: String, fechaRuta: String)

    @Query("DELETE FROM RutaComercial")
    suspend fun clearAll()
}

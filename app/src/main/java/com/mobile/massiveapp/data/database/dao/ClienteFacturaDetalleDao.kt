package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ClienteFacturaDetalleEntity

@Dao
interface ClienteFacturaDetalleDao:BaseDao<ClienteFacturaDetalleEntity> {

    @Query("SELECT * FROM FacturaDetalle")
    suspend fun getAll(): List<ClienteFacturaDetalleEntity>

    @Query("SELECT * FROM FacturaDetalle WHERE DocEntry = :docEntry")
    suspend fun getAllDetallesPorDocEntry(docEntry: Int): List<ClienteFacturaDetalleEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listaFacturasDetalle: List<ClienteFacturaDetalleEntity>)

    @Query("DELETE FROM FacturaDetalle")
    suspend fun deleteAll()
}

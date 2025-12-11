package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GrupoDescuentoDetalleEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDescuentoDetalleDao : BaseDao<GrupoDescuentoDetalleEntity>  {

    @Query("SELECT * FROM GrupoDescuentoDetalle")
    fun getAllFlow(): Flow<List<GrupoDescuentoDetalleEntity>>

    @Query("SELECT * FROM GrupoDescuentoDetalle WHERE AbsEntry = :code")
    suspend fun getAllByCode(code: Int): List<GrupoDescuentoDetalleEntity>

    @Query("SELECT * FROM GrupoDescuentoDetalle")
    suspend fun getAll(): List<GrupoDescuentoDetalleEntity>

    @Query("DELETE FROM GrupoDescuentoDetalle")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GrupoDescuentoDetalleEntity>)

    @Update
    suspend fun update(items: List<GrupoDescuentoDetalleEntity>)
}
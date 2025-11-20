package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloGruposUMDetalleEntity

@Dao
interface ArticuloGruposUMDetalleDao:BaseDao<ArticuloGruposUMDetalleEntity> {

    @Query("SELECT * FROM ArticuloGruposUMDetalle")
    suspend fun getAllArticuloGruposUMDetalle(): List<ArticuloGruposUMDetalleEntity>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertArticuloGruposUMDetalle(articuloGruposUMDetalleEntity: List<ArticuloGruposUMDetalleEntity>)

    @Query("DELETE FROM ArticuloGruposUMDetalle")
    suspend fun deleteAllArticuloGruposUMDetalle()

    @Query("DELETE FROM ArticuloGruposUMDetalle WHERE UgpEntry = :ugpEntry")
    suspend fun deleteArticuloGruposUMDetallePorUgpEntry(ugpEntry: Int)

    @Update
    suspend fun updateArticuloGruposUMDetalle(articuloGruposUMDetalleEntity: List<ArticuloGruposUMDetalleEntity>)

        //Obtener GrupoUM Detalle
    @Query("SELECT * FROM ArticuloGruposUMDetalle WHERE UgpEntry = :ugpEntry AND LineNum = :lineNum")
    suspend fun getArticuloGruposUMDetallePorUomEntry(lineNum: Int, ugpEntry: Int): ArticuloGruposUMDetalleEntity


}
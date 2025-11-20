package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloGruposUMEntity

@Dao
interface ArticuloGruposUMDao:BaseDao<ArticuloGruposUMEntity> {

    @Query("SELECT * FROM GrupoUnidadMedida ORDER BY UgpEntry ASC")
    suspend fun getAllArticuloGruposUM(): List<ArticuloGruposUMEntity>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertArticuloGruposUM(articuloGruposUMEntity:List<ArticuloGruposUMEntity> )

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insert(articuloGruposUMEntity:ArticuloGruposUMEntity)

    @Query("DELETE FROM GrupoUnidadMedida")
    suspend fun deleteAllArticuloGruposUM()

    @Update
    suspend fun updateArticuloGruposUM(articuloGruposUMEntity: List<ArticuloGruposUMEntity>)
}
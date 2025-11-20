package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloLocalidadesEntity

@Dao
interface ArticuloLocalidadesDao:BaseDao<ArticuloLocalidadesEntity> {

    @Query("SELECT * FROM Localidad")
    suspend fun getAllArticuloLocalidades(): List<ArticuloLocalidadesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticuloLocalidades(articuloLocalidadesEntity: List<ArticuloLocalidadesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articuloLocalidadesEntity: ArticuloLocalidadesEntity)

    @Query("DELETE FROM Localidad")
    suspend fun deleteAllArticuloLocalidades()

    @Update
    suspend fun updateArticuloLocalidades(articuloLocalidadesEntity: List<ArticuloLocalidadesEntity>)
}
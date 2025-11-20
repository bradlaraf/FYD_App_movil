package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloFabricantesEntity

@Dao
interface ArticuloFabricantesDao:BaseDao<ArticuloFabricantesEntity> {

    @Query("SELECT * FROM Fabricante")
    suspend fun getAllArticuloFabricantes(): List<ArticuloFabricantesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticuloFabricantes(articuloFabricantes: List<ArticuloFabricantesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articuloFabricantes: ArticuloFabricantesEntity)

    @Query("DELETE FROM Fabricante")
    suspend fun deleteAllArticuloFabricantes()

    @Update
    suspend fun updateArticuloFabricantes(articuloFabricantes: List<ArticuloFabricantesEntity>)

}
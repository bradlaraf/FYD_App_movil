package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloAlmacenesEntity
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem

@Dao
interface ArticuloAlmacenesDao:BaseDao<ArticuloAlmacenesEntity> {

    @Query("SELECT * FROM Almacenes ORDER BY WhsName DESC")
    suspend fun getAllArticuloAlmacenes(): List<ArticuloAlmacenesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticuloAlmacenes(articuloAlmacenes:List<ArticuloAlmacenesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articuloAlmacenes:ArticuloAlmacenesEntity)

    @Query("""
            SELECT
                T0.WhsCode as Code,
                T0.WhsName as Name,
                0 as Checked
            FROM Almacenes T0
            """)
    suspend fun getAlmacenesCreacion(): List<DoNuevoUsuarioItem>

    @Query("DELETE FROM Almacenes")
    suspend fun clearAllArticuloAlmacenes()

    @Update
    suspend fun updateArticuloAlmacenes(articuloAlmacenes: List<ArticuloAlmacenesEntity>)
}
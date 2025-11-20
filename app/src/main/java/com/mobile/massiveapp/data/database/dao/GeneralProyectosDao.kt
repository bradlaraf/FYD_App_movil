package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralProyectosEntity

@Dao
interface GeneralProyectosDao:BaseDao<GeneralProyectosEntity> {
    @Query("SELECT * FROM Proyecto")
    suspend fun getAll(): List<GeneralProyectosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalProyectos: List<GeneralProyectosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalProyectos: GeneralProyectosEntity)

    @Query("DELETE FROM Proyecto")
    suspend fun clearAll()

    @Update
    suspend fun update(generalProyectos: List<GeneralProyectosEntity>)
}
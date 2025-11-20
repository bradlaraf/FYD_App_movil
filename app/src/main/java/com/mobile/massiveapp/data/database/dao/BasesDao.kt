package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.BasesEntity


@Dao
interface BasesDao:BaseDao<BasesEntity> {
    @Query("SELECT * FROM Bases")
    suspend fun getAll(): List<BasesEntity>

    @Query("""
        SELECT 
            * 
        FROM Bases T1
        WHERE T1.`DataBase` IN (SELECT BaseDeDatos FROM Configuracion)
        """)
    suspend fun getBaseActual(): BasesEntity

    @Query("SELECT * FROM Bases WHERE Code = :Code")
    suspend fun getByCode(Code: String): BasesEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bases: List<BasesEntity>)

    @Query("DELETE FROM Bases")
    suspend fun clearAll()
}
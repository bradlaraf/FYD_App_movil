package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.CargosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CargosDao:BaseDao<CargosEntity> {
    @Query("SELECT * FROM Cargos")
    fun getAllFlow(): Flow<List<CargosEntity>>

    @Query("SELECT * FROM Cargos WHERE posID = :code")
    suspend fun getAllByCode(code: String): List<CargosEntity>

    @Query("SELECT * FROM Cargos")
    suspend fun getAll(): List<CargosEntity>

    @Query("DELETE FROM Cargos")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CargosEntity>)

    @Update
    suspend fun update(items: List<CargosEntity>)
}
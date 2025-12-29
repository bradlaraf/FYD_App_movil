package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ConductorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConductorDao : BaseDao<ConductorEntity>  {

    @Query("SELECT * FROM Conductor")
    fun getAllFlow(): Flow<List<ConductorEntity>>

    @Query("SELECT * FROM Conductor WHERE Code = :code")
    suspend fun getAllByCode(code: String): List<ConductorEntity>

    @Query("SELECT * FROM Conductor")
    suspend fun getAll(): List<ConductorEntity>

    @Query("SELECT * FROM Conductor ORDER BY Name, Code")
    suspend fun getAllOrder(): List<ConductorEntity>

    @Query("DELETE FROM Conductor")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ConductorEntity>)

    @Update
    suspend fun update(items: List<ConductorEntity>)
}
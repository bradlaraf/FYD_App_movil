package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ConductorEntity
import com.mobile.massiveapp.data.database.entities.PrecioEspecialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrecioEspecialDao: BaseDao<PrecioEspecialEntity>  {

    @Query("SELECT * FROM PrecioEspecial")
    fun getAllFlow(): Flow<List<PrecioEspecialEntity>>

    @Query("SELECT * FROM PrecioEspecial WHERE Code = :code")
    suspend fun getAllByCode(code: String): List<PrecioEspecialEntity>

    @Query("SELECT * FROM PrecioEspecial")
    suspend fun getAll(): List<PrecioEspecialEntity>

    @Query("DELETE FROM PrecioEspecial")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PrecioEspecialEntity>)

    @Update
    suspend fun update(items: List<PrecioEspecialEntity>)
}
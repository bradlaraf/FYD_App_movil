package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.PrecioEspecial2Entity
import com.mobile.massiveapp.data.database.entities.PrecioEspecialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrecioEspecial2Dao : BaseDao<PrecioEspecial2Entity>  {

    @Query("SELECT * FROM PrecioEspecial2")
    fun getAllFlow(): Flow<List<PrecioEspecial2Entity>>

    @Query("SELECT * FROM PrecioEspecial2 WHERE Code = :code")
    suspend fun getAllByCode(code: String): List<PrecioEspecial2Entity>

    @Query("SELECT * FROM PrecioEspecial2")
    suspend fun getAll(): List<PrecioEspecial2Entity>

    @Query("DELETE FROM PrecioEspecial2")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PrecioEspecial2Entity>)

    @Update
    suspend fun update(items: List<PrecioEspecial2Entity>)
}
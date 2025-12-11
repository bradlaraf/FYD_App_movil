package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.PrecioEspecial1Entity
import com.mobile.massiveapp.data.database.entities.PrecioEspecialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrecioEspecial1Dao : BaseDao<PrecioEspecial1Entity>  {

    @Query("SELECT * FROM PrecioEspecial1")
    fun getAllFlow(): Flow<List<PrecioEspecial1Entity>>

    @Query("SELECT * FROM PrecioEspecial1 WHERE Code = :code")
    suspend fun getAllByCode(code: String): List<PrecioEspecial1Entity>

    @Query("SELECT * FROM PrecioEspecial1")
    suspend fun getAll(): List<PrecioEspecial1Entity>

    @Query("DELETE FROM PrecioEspecial1")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PrecioEspecial1Entity>)

    @Update
    suspend fun update(items: List<PrecioEspecial1Entity>)
}
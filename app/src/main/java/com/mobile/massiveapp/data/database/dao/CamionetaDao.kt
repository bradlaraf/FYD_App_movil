package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.CamionetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CamionetaDao : BaseDao<CamionetaEntity>  {

    @Query("SELECT * FROM Camioneta")
    fun getAllFlow(): Flow<List<CamionetaEntity>>

    @Query("SELECT * FROM Camioneta")
    suspend fun getAll(): List<CamionetaEntity>

    @Query("DELETE FROM Camioneta")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CamionetaEntity>)

    @Update
    suspend fun update(items: List<CamionetaEntity>)
}
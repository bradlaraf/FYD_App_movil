package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.SucursalEntity
import com.mobile.massiveapp.data.database.entities.TipoCambioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SucursalDao : BaseDao<SucursalEntity>  {

    @Query("SELECT * FROM Sucursal")
    fun getAllFlow(): Flow<List<SucursalEntity>>

    @Query("SELECT * FROM Sucursal")
    suspend fun getAll(): List<SucursalEntity>

    @Query("DELETE FROM Sucursal")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SucursalEntity>)

    @Update
    suspend fun update(items: List<SucursalEntity>)
}
package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.TipoCambioEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface TipoCambioDao : BaseDao<TipoCambioEntity>  {

    @Query("SELECT * FROM TipoCambio")
    fun getAllFlow(): Flow<List<TipoCambioEntity>>

    @Query("SELECT * FROM TipoCambio WHERE Code = :code")
    fun getAllByCode(code: String): Flow<List<TipoCambioEntity>>

    @Query("SELECT * FROM TipoCambio")
    suspend fun getAll(): List<TipoCambioEntity>

    @Query("DELETE FROM TipoCambio")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TipoCambioEntity>)

    @Update
    suspend fun update(items: List<TipoCambioEntity>)
}
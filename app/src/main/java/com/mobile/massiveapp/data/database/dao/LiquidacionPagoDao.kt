package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.LiquidacionPagoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LiquidacionPagoDao:BaseDao<LiquidacionPagoEntity> {
    @Query("SELECT * FROM LiquidacionPago")
    fun getAllFlow(): Flow<List<LiquidacionPagoEntity>>

    @Query("SELECT * FROM LiquidacionPago WHERE AccDocEntry = :code")
    suspend fun getAllByCode(code: String): List<LiquidacionPagoEntity>

    @Query("SELECT * FROM LiquidacionPago")
    suspend fun getAll(): List<LiquidacionPagoEntity>

    @Query("DELETE FROM LiquidacionPago")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LiquidacionPagoEntity>)

    @Update
    suspend fun update(items: List<LiquidacionPagoEntity>)
}
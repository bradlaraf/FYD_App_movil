package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.FormaPagoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FormaPagoDao:BaseDao<FormaPagoEntity> {
    @Query("SELECT * FROM FormaPago")
    fun getAllFlow(): Flow<List<FormaPagoEntity>>

    @Query("SELECT * FROM FormaPago WHERE Code = :code")
    suspend fun getAllByCode(code: String): List<FormaPagoEntity>

    @Query("SELECT * FROM FormaPago")
    suspend fun getAll(): List<FormaPagoEntity>

    @Query("DELETE FROM FormaPago")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FormaPagoEntity>)

    @Update
    suspend fun update(items: List<FormaPagoEntity>)
}
package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GrupoDescuentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDescuentoDao: BaseDao<GrupoDescuentoEntity>  {

    @Query("SELECT * FROM GrupoDescuento")
    fun getAllFlow(): Flow<List<GrupoDescuentoEntity>>

    @Query("SELECT * FROM GrupoDescuento WHERE AbsEntry = :code")
    suspend fun getAllByCode(code: Int): List<GrupoDescuentoEntity>

    @Query("SELECT * FROM GrupoDescuento")
    suspend fun getAll(): List<GrupoDescuentoEntity>

    @Query("DELETE FROM GrupoDescuento")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GrupoDescuentoEntity>)

    @Update
    suspend fun update(items: List<GrupoDescuentoEntity>)
}
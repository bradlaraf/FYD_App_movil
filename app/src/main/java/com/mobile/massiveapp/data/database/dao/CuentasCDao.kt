package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.CuentasCEntity

@Dao
interface CuentasCDao: BaseDao<CuentasCEntity> {

    @Query("SELECT * FROM CuentasC")
    suspend fun getAll(): List<CuentasCEntity>

    @Query("DELETE FROM CuentasC")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CuentasCEntity>)

    @Update
    suspend fun update(items: List<CuentasCEntity>)
}
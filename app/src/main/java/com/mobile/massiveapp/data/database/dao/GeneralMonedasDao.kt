package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralMonedasEntity

@Dao
interface GeneralMonedasDao:BaseDao<GeneralMonedasEntity> {

    @Query("SELECT * FROM Monedas")
    suspend fun getAllGeneralMonedas(): List<GeneralMonedasEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGeneralMonedas(generalMonedas: List<GeneralMonedasEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalMonedas: GeneralMonedasEntity)

    @Query("DELETE FROM Monedas")
    suspend fun deleteAllGeneralMonedas()

    @Update
    suspend fun updateGeneralMonedas(generalMonedas: List<GeneralMonedasEntity>)
}
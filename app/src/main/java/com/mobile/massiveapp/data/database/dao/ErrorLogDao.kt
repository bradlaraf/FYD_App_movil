package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ErrorLogEntity

@Dao
interface ErrorLogDao {

    @Query("""SELECT 
               * 
           FROM ErrorLog
           ORDER BY ErrorDate, ErrorHour""")
    suspend fun getAll():List<ErrorLogEntity>

    @Query("""SELECT 
               * 
           FROM ErrorLog
           WHERE ErrorDate = :date AND ErrorHour = :hour""")
    suspend fun getError(date: String, hour: String):ErrorLogEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(error: ErrorLogEntity)

    @Query("""DELETE FROM ErrorLog""")
    fun clearAll()

}
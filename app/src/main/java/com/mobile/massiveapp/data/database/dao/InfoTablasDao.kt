package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.mobile.massiveapp.data.database.entities.InfoTablasEntity

@Dao
interface InfoTablasDao {
    @Query("SELECT * FROM InfoTablas ORDER BY Tabla")
    suspend fun getAll(): List<InfoTablasEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(infoTabla: List<InfoTablasEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(infoTabla: InfoTablasEntity)

    @RawQuery
    suspend fun getCount(query: SupportSQLiteQuery): Int

    @RawQuery
    suspend fun deleteTable(query: SupportSQLiteQuery):Int

    @Transaction
    suspend fun deleteAllTables(tables: List<String>){
        for (tableName in tables){
            val query = SimpleSQLiteQuery("DELETE FROM $tableName")
            deleteTable(query)
        }
    }

    @RawQuery
    suspend fun getAllTableNames(query: SupportSQLiteQuery): List<String>


}

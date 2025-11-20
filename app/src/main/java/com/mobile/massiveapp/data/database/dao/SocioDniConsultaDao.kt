package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.SocioDniConsultaEntity


@Dao
interface SocioDniConsultaDao {

    @Query("SELECT * FROM SocioDniConsulta")
    suspend fun getAll(): SocioDniConsultaEntity

    @Query("DELETE FROM SocioDniConsulta")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(socioDniConsultaEntity: SocioDniConsultaEntity)

}
package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ConsultaDocumentoContactosEntity

@Dao
interface ConsultaDocumentoContactosDao {

    @Query("SELECT * FROM ConsultaDocumentoContactos")
    suspend fun getAll(): List<ConsultaDocumentoContactosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sociosRucConsultaContactos: List<ConsultaDocumentoContactosEntity>)

    @Query("DELETE FROM ConsultaDocumentoContactos")
    suspend fun deleteAll()


}
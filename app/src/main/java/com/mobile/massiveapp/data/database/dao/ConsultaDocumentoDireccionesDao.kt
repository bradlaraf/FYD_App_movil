package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ConsultaDocumentoDireccionesEntity

@Dao
interface ConsultaDocumentoDireccionesDao {

    @Query("SELECT * FROM ConsultaDocumentoDirecciones")
    suspend fun getAll(): ConsultaDocumentoDireccionesEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sociosRucConsultaDirecciones: List<ConsultaDocumentoDireccionesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnoDireccion(sociosRucConsultaDirecciones: ConsultaDocumentoDireccionesEntity)

    @Query("DELETE FROM ConsultaDocumentoDirecciones")
    suspend fun deleteAll()


}
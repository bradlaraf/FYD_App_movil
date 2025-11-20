package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ConsultaDocumentoEntity

@Dao
interface DocumentoConsultaDao {

    @Query("SELECT * FROM ConsultaDocumento")
    suspend fun getAllSocioRucConsulta(): ConsultaDocumentoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSocioRucConsulta(sociosRucConsulta: ConsultaDocumentoEntity)

    @Query("DELETE FROM ConsultaDocumento")
    suspend fun deleteAllSocioRucConsulta()

    //get documento por numero
    @Query("SELECT LicTradNum FROM SocioNegocio WHERE LicTradNum = :numeroDocumento")
    suspend fun validarExisteDocumento(numeroDocumento: String): String


}
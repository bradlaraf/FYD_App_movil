package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.SocioContactosEntity

@Dao
interface SocioContactosDao:BaseDao<SocioContactosEntity> {

    @Query("SELECT * FROM SocioContactos")
    suspend fun getAllSociosContactos(): List<SocioContactosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSociosContactos(sociosContactos:List<SocioContactosEntity>)

        //Obtener todos los socio contactos por CardCode
    @Query("SELECT * FROM SocioContactos WHERE CardCode = :cardCode")
    suspend fun getSocioContactoPorCardCode(cardCode: String): List<SocioContactosEntity>

    @Update
    suspend fun updateSociosContactos(sociosContactos: List<SocioContactosEntity>)


    @Query("DELETE FROM SocioContactos WHERE CardCode = :cardCode ")
    suspend fun deleteSocioContactosByCardCode(cardCode: String)


    @Query("DELETE FROM SocioContactos WHERE AccDocEntry = :accDocEntry")
    suspend fun deleteSocioContactosByAccDocEntry(accDocEntry: String)



        //Obtener un socio contacto por celular y cardCode
    @Query("SELECT * FROM SocioContactos WHERE Cellolar = :celular AND CardCode = :cardCode")
    suspend fun getSocioContactoPorCelularYCardCode(celular: String, cardCode: String): SocioContactosEntity

        //Eliminar un socio contacto por celular y cardCode
    @Query("DELETE FROM SocioContactos WHERE Cellolar = :celular AND CardCode = :cardCode")
    suspend fun deleteSocioContactoPorCelularYCardCode(celular: String, cardCode: String)

        //Eliminar todos los socio contactos
    @Query("DELETE FROM SocioContactos")
    suspend fun clearAllSociosContactos()

        //Buscar contactos por CardCode
    @Query("SELECT * FROM SocioContactos WHERE CardCode = :cardCode")
    suspend fun getContactosPorCardCode(cardCode:String): List<SocioContactosEntity>

        //Guardar contactos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSocioContacto(contactoNuevo: SocioContactosEntity)

}

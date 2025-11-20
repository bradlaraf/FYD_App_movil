package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.SocioContactosDao
import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.dao.ConsultaDocumentoContactosDao
import com.mobile.massiveapp.data.database.dao.ConsultaDocumentoDireccionesDao
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.dao.SocioDniConsultaDao
import com.mobile.massiveapp.data.database.dao.SocioNuevoAwaitDao
import com.mobile.massiveapp.data.database.dao.DocumentoConsultaDao
import com.mobile.massiveapp.data.database.entities.SocioContactosEntity
import com.mobile.massiveapp.data.database.entities.SocioDireccionesEntity
import com.mobile.massiveapp.data.database.entities.ClienteSociosEntity
import com.mobile.massiveapp.data.database.entities.SocioNuevoAwaitEntity
import com.mobile.massiveapp.data.database.entities.ConsultaDocumentoEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.database.entities.toModel
import com.mobile.massiveapp.data.model.SocioDirecciones
import com.mobile.massiveapp.data.model.ConsultaDocumentoDireccion
import com.mobile.massiveapp.data.network.SocioService
import com.mobile.massiveapp.domain.model.DoClienteSocios
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoSocioContactos
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import com.mobile.massiveapp.domain.model.DoSocioNuevoAwait
import com.mobile.massiveapp.domain.model.DoConsultaDocumento
import com.mobile.massiveapp.domain.model.toDomain
import timber.log.Timber
import javax.inject.Inject

class SocioRepository @Inject constructor(
    private val api: SocioService,
    private val clienteSociosDao: ClienteSociosDao,
    private val documentoConsultaDao: DocumentoConsultaDao,
    private val consultaDocumentoDireccionesDao: ConsultaDocumentoDireccionesDao,
    private val consultaDocumentoContactosDao: ConsultaDocumentoContactosDao,

    private val socioDniConsultaDao: SocioDniConsultaDao,
    private val socioDireccionesDao: SocioDireccionesDao,
    private val socioNuevoAwaitDao: SocioNuevoAwaitDao,
    private val socioContactoDao: SocioContactosDao
){


        //Eliminar un socio de negocio por cardCode
    suspend fun deleteSocioPorCardCode(cardCode: String) =
        try {
            clienteSociosDao.deleteSocioPorCardCode(cardCode)
            true
        } catch (e: Exception){
            Timber.e(e, "Error al eliminar socio negocio por cardCode")
            false
        }


        //Obtener todos los socios de negocio
    suspend fun getAllSocioNegocio(): List<DoClienteSocios> =
        try {
            val response: List<ClienteSociosEntity> = clienteSociosDao.getAllSocios()
            response.map { it.toDomain() }
        } catch (e: Exception){
            emptyList()
        }





        //Obtener un socio de negocio por cardCode
    suspend fun getSocioNegocioPorCardCode(cardCode: String) =
        try {
            val result = clienteSociosDao.getClienteSocioPorCardCode(cardCode)
            result.toDomain()
        } catch (e: Exception){
            Timber.e(e, "Error al obtener socio negocio por cardCode")
            DoClienteSocios()
        }





        //Se traen todos los socios migrados
    suspend fun getAllSociosFiltradoPorMigrado(migrado: String):List<DoClienteSocios> =
        try {
            val response = clienteSociosDao.getAllSocioFiltradoPorMigrado(migrado)
            response.map { it.toDomain() }
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }



    //Se traen todos los socios no migrados
    suspend fun getSocioNuevoAwaitFromDatabase(): List<DoSocioNuevoAwait>{
        return try {
            val response: List<SocioNuevoAwaitEntity> = socioNuevoAwaitDao.getAllSocioNuevoAwait()
            response.map { it.toDomain() }
        } catch (e: Exception) {
            Timber.e(e, "Error al obtener todos los socios nuevos")
            emptyList()
        }
    }


    suspend fun insertSocio(clienteSociosEntity: ClienteSociosEntity) =
        try {
            clienteSociosDao.insertSocio(clienteSociosEntity)
            true
        } catch (e: Exception){
            false
        }




    //Obtener un cliente socio por cardCode
    suspend fun getClienteSocioPorCardCode(cardCode: String): DoClienteSocios {
        return try {
            clienteSociosDao.getClienteSocioPorCardCode(cardCode).let {
                return it.toDomain()
            }

        } catch (e: Exception){
            Timber.e(e, "Error al obtener cliente socio por cardCode")
            DoClienteSocios()
        }
    }

    //ClienteSocios Direcciones
    suspend fun getAllSocioDirecciones(): List<DoSocioDirecciones>{
        val response: List<SocioDirecciones> = api.getSocioDirecciones()
        return response.map { it.toDomain() }
    }

    suspend fun getAllSocioDireccionesFromDatabase(): List<DoSocioDirecciones>{
        val response: List<SocioDireccionesEntity> = socioDireccionesDao.getAllSociosDirecciones()
        return response.map { it.toDomain() }
    }

    /*---------------------SOCIO CONTACTO-------------------------------*/




        //Obtener socio contacto por CardCode
    suspend fun getSocioContactoPorCardCode(cardCode: String): List<DoSocioContactos>{
        return try {
            val response: List<SocioContactosEntity> = socioContactoDao.getSocioContactoPorCardCode(cardCode)
            response.map { it.toDomain() }
        } catch (e: Exception){
            Timber.e(e, "Error al obtener socio contacto por cardCode")
            emptyList()
        }
    }

        //Guardar socio contacto
    suspend fun saveSocioContacto(contactoNuevo: DoSocioContactos): Boolean{
            return try {
                socioContactoDao.saveSocioContacto(contactoNuevo.toDatabase())
                true
            } catch (e: Exception){
                Timber.e(e, "Error al guardar socio contacto por cardCode")
                false
            }
    }

        //Obtener socio contacto por celular y cardCode
    suspend fun getSocioContactoPorCelularYCardCode(celular: String, cardCode: String): DoSocioContactos{
        return try {
            val response: SocioContactosEntity = socioContactoDao.getSocioContactoPorCelularYCardCode(celular, cardCode)
            response.toDomain()
        } catch (e: Exception){
            Timber.e(e, "Error al obtener socio contacto por celular y cardCode")
            DoSocioContactos()
        }
    }

        //Eliminar socio contacto por celular y cardCode
    suspend fun deleteSocioContactoPorCelularYCardCode(celular: String, cardCode: String): Boolean{
        return try {
            socioContactoDao.deleteSocioContactoPorCelularYCardCode(celular, cardCode)
            true
        } catch (e: Exception){
            Timber.e(e, "Error al eliminar socio contacto por celular y cardCode")
            false
        }
    }

        //Eliminar todos los socio contactos por accDocEntry
    suspend fun deleteAllSocioContactosPorAccDocEntry(accDocEntry: String) =
        try {
            socioContactoDao.deleteSocioContactosByAccDocEntry(accDocEntry)
            true
        } catch (e: Exception){
            Timber.e(e, "Error al eliminar todos los socio contactos por accDocEntry")
            false
        }





    /*---------------------SOCIO DIRECCIONES-------------------------------*/


        //DocumentoDireccion DESPACHO por cardCode Y Tipo
    suspend fun getDireccionesPorTipoYCardCode(cardCode:String, tipo:String): List<DoSocioDirecciones>{
        val response: List<SocioDireccionesEntity> = socioDireccionesDao.getDireccionesPorTipoYCardCode(cardCode, tipo)
        return response.map { it.toDomain() }
    }

    suspend fun getDireccinesPorCardCode(cardCode: String): List<DoSocioDirecciones>{
        val response: List<SocioDireccionesEntity> = socioDireccionesDao.getDireccionesPorCardCode(cardCode)
        return response.map { it.toDomain() }
    }


        //Elimnar todas las direcciones por CardCode
    suspend fun deleteSocioDireccionesByCardCode(cardCode: String) =
        try {
            socioDireccionesDao.deleteSocioDireccionesByCardCode(cardCode)
            true
        } catch (e: Exception){
            Timber.e(e, "Error al eliminar socio direcciones por cardCode")
            false
        }


        //Eliminar todos los socio direcciones por accDocEntry
    suspend fun deleteAllSocioDireccionesPorAccDocEntry(accDocEntry: String) =
        try {
            socioDireccionesDao.deleteSocioDireccionesByAccDocEntry(accDocEntry)
            true
        } catch (e: Exception){
            Timber.e(e, "Error al eliminar todos los socio direcciones por accDocEntry")
            false
        }

    suspend fun insertAllSocioDirecciones(socioDireccionesEntity: List<SocioDireccionesEntity>){
        socioDireccionesDao.insertAllSocioDirecciones(socioDireccionesEntity)
    }

    suspend fun insertSocioDirecciones(socioDireccionesEntity: SocioDireccionesEntity): Boolean{
        return try {
            socioDireccionesDao.insertSocioDirecciones(socioDireccionesEntity)
            true
        } catch (e: Exception){
            false
        }

    }
    suspend fun clearSocioDirecciones(){
        socioDireccionesDao.clearAllSociosDirecciones()
    }

    //Informacion de ClienteSocios de Negocio
    suspend fun getInfoSocio(cardCode:String): DoClienteSocios =
        try {
            val response: ClienteSociosEntity = clienteSociosDao.getInfoSocio(cardCode)
            response.toDomain()
        } catch (e: Exception){
            Timber.e(e, "Error al obtener informacion de socio")
            DoClienteSocios()
        }



    suspend fun getInfoSocioAwait(cardCode:String): DoSocioNuevoAwait{
        val response: SocioNuevoAwaitEntity = socioNuevoAwaitDao.getInfoSocioAwait(cardCode)
        return response.toDomain()
    }


    //Consulta Ruc--------------------------------------

            //Get RUC
    suspend fun getConsultaDocumentoFromApi(
                configuracion: DoConfiguracion,
                tipoDocumento: String,
                numeroDocumento: String
    ): DoConsultaDocumento =
        try {
            val response = api.getConsultaRuc(configuracion = configuracion,tipoDocumento =  tipoDocumento, numeroDocumento =  numeroDocumento)
            val direccion = try { response.documentoDireccions } catch (e: Exception){ emptyList() }
            val contactos = try { response.documentoContactos } catch (e: Exception){ emptyList() }

            documentoConsultaDao.deleteAllSocioRucConsulta()
            documentoConsultaDao.insertAllSocioRucConsulta(response.toDatabase())

            if(contactos.isNotEmpty()){
                consultaDocumentoContactosDao.deleteAll()
                consultaDocumentoContactosDao.insertAll(contactos.map { it.toDatabase() })
            }

            if (direccion.isNotEmpty()){
                consultaDocumentoDireccionesDao.deleteAll()
                consultaDocumentoDireccionesDao.insertAll(direccion.map { it.toDatabase() })
            }

            val consultaDocumento =
                if (direccion.isEmpty()) {
                    response.toDomain(ConsultaDocumentoDireccion())
                } else {
                    response.toDomain(direccion.first())
                }

            consultaDocumento
        } catch (e: Exception){
            Timber.tag("ConsultaRuc").e(e, "Error al obtener consulta de ruc")
            DoConsultaDocumento()
        }






    suspend fun getConsultaRucFromDatabase(): DoConsultaDocumento =
        try {
            val response: ConsultaDocumentoEntity = documentoConsultaDao.getAllSocioRucConsulta()
            val direccion = consultaDocumentoDireccionesDao.getAll()
            response.toDomain(direccion.toModel())
        } catch (e: Exception){
            DoConsultaDocumento()
        }


}
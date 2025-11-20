package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.ConsultaDocumentoContactosDao
import com.mobile.massiveapp.data.database.dao.ConsultaDocumentoDireccionesDao
import com.mobile.massiveapp.data.database.dao.DocumentoConsultaDao
import com.mobile.massiveapp.data.database.entities.ConsultaDocumentoDireccionesEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.model.ConsultaDocumentoDireccion
import javax.inject.Inject

class ConsultaDocumentoRepository @Inject constructor(
    private val consultaDocumentoContactosDao: ConsultaDocumentoContactosDao,
    private val consultaDocumentoDireccionesDao: ConsultaDocumentoDireccionesDao,
    private val documentoConsultaDao: DocumentoConsultaDao
) {






        //Guardar una direccion de Consulta documento
    suspend fun saveDireccionConsultaDocumento(direccion: ConsultaDocumentoDireccionesEntity) =
        try {
            consultaDocumentoDireccionesDao.insertUnoDireccion(direccion)
            true
        } catch (e: Exception) {
            println(e)
            false
        }


        //Obtener todas las direcciones de Consulta documento
    suspend fun getAllDireccionesConsultaDocumento() =
        try {
            consultaDocumentoDireccionesDao.getAll()
        } catch (e: Exception) {
            println(e)
            ConsultaDocumentoDireccion().toDatabase()
        }



}
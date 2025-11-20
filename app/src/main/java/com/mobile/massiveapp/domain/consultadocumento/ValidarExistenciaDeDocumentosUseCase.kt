package com.mobile.massiveapp.domain.consultadocumento

import com.mobile.massiveapp.data.database.dao.DocumentoConsultaDao
import javax.inject.Inject

class ValidarExistenciaDeDocumentosUseCase @Inject constructor(
    val consultaDocumentoDao: DocumentoConsultaDao
) {
    suspend operator fun invoke(numeroDocumento: String) =
        try {
            val result = consultaDocumentoDao.validarExisteDocumento(numeroDocumento).isNotEmpty()
            result
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}
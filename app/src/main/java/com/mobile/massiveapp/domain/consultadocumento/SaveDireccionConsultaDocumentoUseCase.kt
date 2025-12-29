package com.mobile.massiveapp.domain.consultadocumento

import com.mobile.massiveapp.data.repositories.ConsultaDocumentoRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.SocioDireccionesRepository
import com.mobile.massiveapp.ui.view.util.crearSocioDireccion
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import javax.inject.Inject

class SaveDireccionConsultaDocumentoUseCase @Inject constructor(
    private val consultaDocumentoRepository: ConsultaDocumentoRepository,
    private val socioDireccionesRepository: SocioDireccionesRepository,
    private val usuarioRepository: LoginRepository

){

    suspend operator fun invoke(accDocEntry: String) =
        try {

            val usuarioCode = usuarioRepository.getUsuarioFromDatabase().Code
            val direccion = consultaDocumentoRepository.getAllDireccionesConsultaDocumento()

            val cardCode =
                if (direccion.NumeroDocumento.length > 8){
                    "C${direccion.NumeroDocumento}"
                } else {
                    "C000${direccion.NumeroDocumento}"
                }

            consultaDocumentoRepository.saveDireccionConsultaDocumento(direccion)
            socioDireccionesRepository.saveDireccion(
                crearSocioDireccion(
                    AccAction =                 "I",
                    AccCreateDate =             getFechaActual(),
                    AccCreateHour =             getHoraActual(),
                    AccCreateUser =             usuarioCode,
                    AccDocEntry =               accDocEntry,
                    AccLocked =                 "N",
                    AccMigrated =               "N",
                    AccUpdateDate =             "",
                    AccUpdateHour =             "",
                    AccUpdateUser =             "",
                    Address =                   direccion.Nombre,
                    AdresType =                 direccion.Tipo,
                    Block =                     "",
                    CardCode =                  cardCode,
                    City =                      direccion.DistritoNombre,
                    Country =                   direccion.PaisCodigo,
                    County =                    direccion.ProvinciaNombre,
                    LineNum =                   0,
                    State =                     direccion.DepartamentoCodigo,
                    Street =                    direccion.Calle,
                    U_MSV_CP_LATITUD =          direccion.Latitud.toString(),
                    U_MSV_CP_LONGITUD =         direccion.Longitud.toString(),
                    U_MSV_FE_UBI =              direccion.DistritoCodigo,
                    zona = "",
                    ZipCode =                   direccion.DistritoNombre,
                    AccControl =                "N"
                )
            )
            true
        } catch (e: Exception) {
            println(e)
            false
        }

}
package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import timber.log.Timber
import javax.inject.Inject

class DeleteSocioNegocioUseCase @Inject constructor(
    private val socioNegocioRepository: SocioRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,
    private val datosMaestrosRepository: DatosMaestrosRepository
) {
    suspend operator fun invoke(cardCode: String) =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)


            val socioAEliminar = socioNegocioRepository.getSocioNegocioPorCardCode(cardCode)
            socioAEliminar.CANCELED = "Y"

            val socioAEliminarModel = socioAEliminar.toModel(emptyList(), emptyList())
            socioAEliminarModel.CANCELED = "Y"


            val hashAEnviar = hashMapOf("ClienteSocios" to listOf(socioAEliminarModel as Any))

            val sendSocioAEliminar =
                try {
                    datosMaestrosRepository.sendUnDocumento(
                        hashAEnviar,
                        configuracion,
                        usuario,
                        url)
                } catch (e: Exception){
                    Timber.tag("ErrorSendSNEliminado").e(e)
                    false
                }

            socioNegocioRepository.deleteSocioDireccionesByCardCode(cardCode)
            socioNegocioRepository.deleteSocioDireccionesByCardCode(cardCode)
            socioNegocioRepository.deleteSocioPorCardCode(cardCode)

            sendSocioAEliminar

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}
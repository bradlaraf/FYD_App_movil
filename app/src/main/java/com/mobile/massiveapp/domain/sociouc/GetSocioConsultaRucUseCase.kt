package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoConsultaDocumento
import javax.inject.Inject

class GetSocioConsultaRucUseCase @Inject constructor(
    private val socioRepository: SocioRepository,
    private val configuracionRepository: ConfiguracionRepository
) {
    suspend operator fun invoke(tipoDocumento: String,  numeroDocumento: String): DoConsultaDocumento =
        try {
            val configuracion = configuracionRepository.getConfiguracion()

            val response = socioRepository.getConsultaDocumentoFromApi(
                configuracion = configuracion,
                tipoDocumento = tipoDocumento,
                numeroDocumento = numeroDocumento
            )

            response
        } catch (e: Exception) {
            DoConsultaDocumento()
        }

}

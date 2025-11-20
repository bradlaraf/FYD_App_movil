package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.model.Bases
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.GeneralRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.getUrlFromPuertoEIpPublica
import javax.inject.Inject


class GetAllBasesDisponiblesUseCase @Inject constructor(
    private val generalRepository: GeneralRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(ipPublica: String, puerto: String):List<Bases> =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromPuertoEIpPublica(ipPublica = ipPublica, puerto = puerto)

            val listaBasesFromApi =
                generalRepository.getAllBasesDisponiblesFromApi(
                    usuario,
                    configuracion,
                    url,
                    5L
                ) as List<Bases>


            val listaBasesResult =
                if (listaBasesFromApi.isNotEmpty()){
                    generalRepository.clearAllBases()
                    generalRepository.saveAllBasesDisponibles(listaBasesFromApi.map { it.toDatabase() })
                    listaBasesFromApi
                }
                else{
                    generalRepository.getAllBasesDisponibles()
                }

            listaBasesResult
        } catch (e: Exception){
            emptyList()
        }
}
package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import javax.inject.Inject

class GetArticulosFromEndpointUseCase @Inject constructor(
    private val datosMaestrosService: DatosMaestrosService,
    private val articuloDao: ArticuloDao,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val clienteSociosDao: ClienteSociosDao,
    private val loginRepository: LoginRepository,
    private val configuracionRepository: ConfiguracionRepository
) {
    suspend operator fun invoke()=
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)


            /*val listaClientes = datosMaestrosService.getSocios(
                "ClienteSocios",
                configuracion,
                usuario,
                url,
                60L,
                0)

            Timber.d(listaClientes.size.toString())


            val listaClientesEntity = listaClientes.map { it.toDatabase() }
            val listaCardCode = listaClientesEntity.map { it.CardCode }

            clienteSociosDao.insertAllSocios(listaClientesEntity)
            datosMaestrosService.sendConfirmacionDeInsercion(
                DoConfirmacionGuardado(
                    NombreTabla = "SocioNegocio",
                    ClavePrimaria = listaCardCode
                ),
                configuracion,
                usuario,
                url
            )*/

            /*val listaArticulos = datosMaestrosService.getArticulos(
                "Articulo",
                configuracion,
                usuario,
                url,
                60L,
                configuracion.TopArticulo)

            val listaEntity = listaArticulos.map { it.toDatabase() }
            val articuloItemCodeList =
                listaEntity.map { it.ItemCode }
            articuloDao.insertAll(listaEntity)

            datosMaestrosService.sendConfirmacionDeInsercion(
                DoConfirmacionGuardado(
                    NombreTabla = "Articulo",
                    ClavePrimaria = articuloItemCodeList
                ),
                configuracion,
                usuario,
                url
            )*/

            /*datosMaestrosRepository.getArticulosFromEndpointsAndSaveToDatabase(
                listOf("Articulo"),
                configuracion,
                usuario,
                url,
                configuracion.TopCliente) { progress, message, maxLenght->

            }*/

            true
        } catch (e:Exception){
            e.printStackTrace()
            false
        }
}
package com.mobile.massiveapp.domain.configuracion

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.PedidoRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.ui.view.util.getFechaActual
import timber.log.Timber
import javax.inject.Inject

class ReinicializarPedidosYPagosUseCase @Inject constructor(
    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val cobranzaRepository: CobranzaRepository,
    private val pedidoRepository: PedidoRepository
) {
    suspend operator fun invoke() =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)
            val fechaActual = getFechaActual()

            configuracionRepository.clearPagosFueraDeFechaActual(fechaActual)
            configuracionRepository.clearPedidosFueraDeFechaActual(fechaActual)


            val sendReinicializacionDePagosYPedidos =
                try {
                    datosMaestrosRepository.sendReinicializacionDePagosYPedidos(
                        url = url,
                        usuario = usuario,
                        configuracion = configuracion
                    )
                } catch (e: Exception) {
                    Timber.tag("ReinicializarPedidosYPagos").e(e)
                    false
                }

            sendReinicializacionDePagosYPedidos

        } catch (e: Exception){
            Timber.tag("ReinicializarPedidosYPagos").e(e)
            false
        }
}
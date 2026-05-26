package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.network.RutaComercialService
import com.mobile.massiveapp.domain.model.DoClienteRutaComercial
import javax.inject.Inject

class ObtenerClienteRutasComercialesUseCase @Inject constructor(
    private val rutaComercialService: RutaComercialService
) {
    suspend operator fun invoke(): List<DoClienteRutaComercial> {
        return try {
            rutaComercialService.obtenerClienteRutasComerciales()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

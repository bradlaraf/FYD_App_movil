package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.network.RutaComercialService
import javax.inject.Inject

class InsertarClienteRutasComercialesUseCase @Inject constructor(
    private val rutaComercialService: RutaComercialService
) {
    suspend operator fun invoke(json: String): Boolean {
        return try {
            rutaComercialService.insertarClienteRutasComerciales(json)
        } catch (e: Exception) {
            false
        }
    }
}

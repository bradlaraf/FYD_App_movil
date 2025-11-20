package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.domain.model.DoClientePago
import javax.inject.Inject

class GetAllPagosCabeceraNoMigradosUseCase @Inject constructor(
    private val repository: CobranzaRepository
){
    suspend operator fun invoke():List<DoClientePago> =
        try {
            repository.getAllPedidosCabeceraNoMigrados()
        } catch (e: Exception) {
            emptyList()
        }
}
package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import javax.inject.Inject

class GetDireccionesUseCase @Inject constructor(
    private val socioDireccionesDao: SocioDireccionesDao
) {
}
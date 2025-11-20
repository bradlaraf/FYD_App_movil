package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloFabricantesEntity
import com.mobile.massiveapp.data.model.ArticuloFabricantes

data class DoArticuloFabricantes(
    val FirmCode: Int,
    val FirmName: String
)

fun ArticuloFabricantes.toDomain() = DoArticuloFabricantes(
    FirmCode = FirmCode,
    FirmName = FirmName
)

fun ArticuloFabricantesEntity.toDomain() = DoArticuloFabricantes(
    FirmCode = FirmCode,
    FirmName = FirmName
)
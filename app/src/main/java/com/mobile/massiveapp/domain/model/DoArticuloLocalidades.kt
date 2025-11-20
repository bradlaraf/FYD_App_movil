package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloLocalidadesEntity
import com.mobile.massiveapp.data.model.ArticuloLocalidades

data class DoArticuloLocalidades (
    val Code: Int,
    val Location: String
    )

fun ArticuloLocalidades.toDomain() = DoArticuloLocalidades(
    Code = Code,
    Location = Location
)

fun ArticuloLocalidadesEntity.toDomain() = DoArticuloLocalidades(
    Code = Code,
    Location = Location
)
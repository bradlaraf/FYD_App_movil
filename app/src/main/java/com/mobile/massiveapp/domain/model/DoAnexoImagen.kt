package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.AnexoImagenEntity

data class DoAnexoImagen(
    val Nombre: String,
    val Uri: String,
    val Code: String
)

fun AnexoImagenEntity.toDomain() = DoAnexoImagen(
    Nombre = Nombre,
    Uri = Uri,
    Code = Code
)

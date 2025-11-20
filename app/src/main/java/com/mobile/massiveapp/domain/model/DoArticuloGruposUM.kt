package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloGruposUMEntity
import com.mobile.massiveapp.data.model.ArticuloGruposUM

data class DoArticuloGruposUM (
    val UgpCode: String,
    val UgpEntry: Int,
    val UgpName: String
    )

fun ArticuloGruposUM.toDomain() = DoArticuloGruposUM(
    UgpCode = UgpCode,
    UgpEntry = UgpEntry,
    UgpName = UgpName
)

fun ArticuloGruposUMEntity.toDomain() = DoArticuloGruposUM(
    UgpCode = UgpCode,
    UgpEntry = UgpEntry,
    UgpName = UgpName
)

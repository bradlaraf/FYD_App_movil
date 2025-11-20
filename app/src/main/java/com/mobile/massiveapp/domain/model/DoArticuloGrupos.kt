package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloGruposEntity
import com.mobile.massiveapp.data.model.ArticuloGrupos

data class DoArticuloGrupos(
    val ItmsGrpCod: Int,
    val ItmsGrpNam: String
)

fun ArticuloGrupos.toDomain() = DoArticuloGrupos(
    ItmsGrpCod = ItmsGrpCod,
    ItmsGrpNam = ItmsGrpNam
)

fun ArticuloGruposEntity.toDomain() = DoArticuloGrupos(
    ItmsGrpCod = ItmsGrpCod,
    ItmsGrpNam = ItmsGrpNam
)
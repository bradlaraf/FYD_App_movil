package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloUnidadesEntity
import com.mobile.massiveapp.data.model.ArticuloUnidades

data class DoArticuloUnidades(
    val UomCode: String,
    val UomEntry: Int,
    val UomName: String
){
    constructor():this(
        "",
        0,
        ""
    )
}

fun ArticuloUnidades.toDomain() = DoArticuloUnidades(
    UomCode = UomCode,
    UomEntry = UomEntry,
    UomName = UomName
)

fun ArticuloUnidadesEntity.toDomain() = DoArticuloUnidades(
    UomCode = UomCode,
    UomEntry = UomEntry,
    UomName = UomName
)
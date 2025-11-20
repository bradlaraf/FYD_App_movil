package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloGruposUMDetalleEntity
import com.mobile.massiveapp.data.model.ArticuloGruposUMDetalle

data class DoArticuloGruposUMDetalle(
    val AltQty: Double,
    val BaseQty: Double,
    val LineNum: Int,
    val UgpEntry: Int,
    val UomEntry: Int
){
    constructor():this(
        0.0,
        0.0,
        0,
        0,
        0)
}
fun ArticuloGruposUMDetalle.toDomain() = DoArticuloGruposUMDetalle(
    AltQty = AltQty,
    BaseQty = BaseQty,
    LineNum = LineNum,
    UgpEntry = UgpEntry,
    UomEntry = UomEntry
)

fun ArticuloGruposUMDetalleEntity.toDomain() = DoArticuloGruposUMDetalle(
    AltQty = AltQty,
    BaseQty = BaseQty,
    LineNum = LineNum,
    UgpEntry = UgpEntry,
    UomEntry = UomEntry
)


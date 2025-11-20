package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.ArticuloGruposUMDetalle
import com.mobile.massiveapp.domain.model.DoArticuloGruposUMDetalle

@Entity(primaryKeys = ["UgpEntry", "UomEntry"], tableName = "ArticuloGruposUMDetalle")
data class ArticuloGruposUMDetalleEntity(

    @ColumnInfo(name = "AltQty") val AltQty: Double,
    @ColumnInfo(name = "BaseQty") val BaseQty: Double,
    @ColumnInfo(name = "LineNum") val LineNum: Int,
    @ColumnInfo(name = "UgpEntry") val UgpEntry: Int,
    @ColumnInfo(name = "UomEntry") val UomEntry: Int
)

fun DoArticuloGruposUMDetalle.toDatabase() = ArticuloGruposUMDetalleEntity(
    AltQty = AltQty,
    BaseQty = BaseQty,
    LineNum = LineNum,
    UgpEntry = UgpEntry,
    UomEntry = UomEntry
)

fun ArticuloGruposUMDetalle.toDatabase() = ArticuloGruposUMDetalleEntity(
    AltQty = AltQty,
    BaseQty = BaseQty,
    LineNum = LineNum,
    UgpEntry = UgpEntry,
    UomEntry = UomEntry
)
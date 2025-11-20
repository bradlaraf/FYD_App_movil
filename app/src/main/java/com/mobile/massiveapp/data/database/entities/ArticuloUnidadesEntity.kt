package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ArticuloUnidades
import com.mobile.massiveapp.domain.model.DoArticuloUnidades

@Entity(tableName = "UnidadMedida")
data class ArticuloUnidadesEntity(
    @PrimaryKey
    @ColumnInfo(name = "UomCode") val UomCode: String,
    @ColumnInfo(name = "UomEntry") val UomEntry: Int,
    @ColumnInfo(name = "UomName") val UomName: String
)

fun DoArticuloUnidades.toDatabase() = ArticuloUnidadesEntity(
    UomCode = UomCode,
    UomEntry = UomEntry,
    UomName = UomName
)

fun ArticuloUnidades.toDatabase() = ArticuloUnidadesEntity(
    UomCode = UomCode,
    UomEntry = UomEntry,
    UomName = UomName
)
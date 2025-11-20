package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ArticuloGruposUM
import com.mobile.massiveapp.domain.model.DoArticuloGruposUM

@Entity(tableName = "GrupoUnidadMedida")
data class ArticuloGruposUMEntity(
    @PrimaryKey
    @ColumnInfo(name = "UgpCode") val UgpCode: String,
    @ColumnInfo(name = "UgpEntry") val UgpEntry: Int,
    @ColumnInfo(name = "UgpName") val UgpName: String
)

fun DoArticuloGruposUM.toDatabase() = ArticuloGruposUMEntity(
    UgpCode = UgpCode,
    UgpEntry = UgpEntry,
    UgpName = UgpName
)

fun ArticuloGruposUM.toDatabase() = ArticuloGruposUMEntity(
    UgpCode = UgpCode,
    UgpEntry = UgpEntry,
    UgpName = UgpName
)


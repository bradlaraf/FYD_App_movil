package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ArticuloGrupos
import com.mobile.massiveapp.domain.model.DoArticuloGrupos

@Entity(tableName = "GrupoArticulo")
data class ArticuloGruposEntity(
    @PrimaryKey
    @ColumnInfo(name = "ItmsGrpCod") val ItmsGrpCod: Int,
    @ColumnInfo(name = "ItmsGrpNam") val ItmsGrpNam: String
)

fun DoArticuloGrupos.toDatabase() = ArticuloGruposEntity(
    ItmsGrpCod = ItmsGrpCod,
    ItmsGrpNam = ItmsGrpNam
)

fun ArticuloGrupos.toDatabase() = ArticuloGruposEntity(
    ItmsGrpCod = ItmsGrpCod,
    ItmsGrpNam = ItmsGrpNam
)
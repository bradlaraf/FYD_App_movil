package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ArticuloLocalidades
import com.mobile.massiveapp.domain.model.DoArticuloLocalidades

@Entity(tableName = "Localidad")
data class ArticuloLocalidadesEntity (
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: Int,
    @ColumnInfo(name = "Location") val Location: String
    )

fun DoArticuloLocalidades.toDatabase() = ArticuloLocalidadesEntity(
    Code = Code,
    Location = Location
)

fun ArticuloLocalidades.toDatabase() = ArticuloLocalidadesEntity(
    Code = Code,
    Location = Location
)
package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.domain.model.DoAnexoImagen

@Entity(tableName = "AnexoImagen")
data class AnexoImagenEntity (
    @PrimaryKey
    @ColumnInfo(name = "Nombre") val Nombre: String,
    @ColumnInfo(name = "Uri") val Uri: String,
    @ColumnInfo(name = "Code") val Code: String
)

fun DoAnexoImagen.toDatabase()= AnexoImagenEntity(
    Nombre = Nombre,
    Uri = Uri,
    Code = Code
)

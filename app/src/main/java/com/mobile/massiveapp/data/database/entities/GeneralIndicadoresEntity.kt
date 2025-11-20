package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralIndicadores

@Entity(tableName = "Indicador")
data class GeneralIndicadoresEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String
)

fun GeneralIndicadores.toDatabase() = GeneralIndicadoresEntity(
    Code = Code,
    Name = Name
)

fun GeneralIndicadoresEntity.toModel() = GeneralIndicadores(
    Code = Code,
    Name = Name
)
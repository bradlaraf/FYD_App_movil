package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Cargos

@Entity(tableName = "Cargos")
data class CargosEntity(
    @PrimaryKey
    @ColumnInfo(name = "posID") val posID: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "descriptio") val descriptio: String,
)

fun Cargos.toDatabase() = CargosEntity(
    posID = posID,
    name = name,
    descriptio = descriptio,
)

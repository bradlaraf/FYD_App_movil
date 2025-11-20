package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.InfoTable

@Entity(tableName = "InfoTablas")
data class InfoTablasEntity(
    @PrimaryKey
    @ColumnInfo(name = "Tabla") val Tabla: String,
    @ColumnInfo(name = "Cantidad") val Cantidad: Int
)

fun InfoTablasEntity.toModel() = InfoTable(
    Tabla = Tabla,
    Cantidad = Cantidad
)

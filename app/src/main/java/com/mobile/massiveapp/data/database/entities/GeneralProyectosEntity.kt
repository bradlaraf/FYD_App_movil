package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralProyectos

@Entity(tableName = "Proyecto")
data class GeneralProyectosEntity(
    @PrimaryKey
    @ColumnInfo(name = "PrjCode") val PrjCode: String,
    @ColumnInfo(name = "PrjName") val PrjName: String
)

fun GeneralProyectos.toDatabase() = GeneralProyectosEntity(
    PrjCode = PrjCode,
    PrjName = PrjName
)
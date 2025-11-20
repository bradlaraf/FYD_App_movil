package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralZonas

@Entity(tableName = "Zona")
data class GeneralZonasEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String
)

fun GeneralZonas.toDatabase() = GeneralZonasEntity(
    Code = Code,
    Name = Name
)



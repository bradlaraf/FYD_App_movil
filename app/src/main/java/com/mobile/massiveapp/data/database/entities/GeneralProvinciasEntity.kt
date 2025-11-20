package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralProvincias

@Entity(tableName = "Provincia")
data class GeneralProvinciasEntity (
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String
    )

fun GeneralProvincias.toDatabase() = GeneralProvinciasEntity(
    Code = Code,
    Name = Name
)

fun GeneralProvinciasEntity.toModel() = GeneralProvincias(
    Code = Code,
    Name = Name
)
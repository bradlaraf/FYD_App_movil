package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralPaises

@Entity(tableName = "Pais")
data class GeneralPaisesEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String
)

fun GeneralPaises.toDatabase() = GeneralPaisesEntity(
    Code = Code,
    Name = Name
)

fun GeneralPaisesEntity.toModel() = GeneralPaises(
    Code = Code,
    Name = Name
)
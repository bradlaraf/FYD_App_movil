package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralDistritos

@Entity(tableName = "Distrito")
data class GeneralDistritosEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String
)

fun GeneralDistritos.toDatabase() = GeneralDistritosEntity(
    Code = Code,
    Name = Name
)

fun GeneralDistritosEntity.toModel() = GeneralDistritos(
    Code = Code,
    Name = Name
)
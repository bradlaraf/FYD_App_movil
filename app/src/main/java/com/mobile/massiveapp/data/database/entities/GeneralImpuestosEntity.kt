package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralImpuestos

@Entity (tableName = "Impuesto")
data class GeneralImpuestosEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String,
    @ColumnInfo(name = "Rate") val Rate: Double
)

fun GeneralImpuestos.toDatabase() = GeneralImpuestosEntity(
    Code = Code,
    Name = Name,
    Rate = Rate
)

fun GeneralImpuestosEntity.toModel() = GeneralImpuestos(
    Code = Code,
    Name = Name,
    Rate = Rate
)
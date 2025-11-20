package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ArticuloFabricantes
import com.mobile.massiveapp.domain.model.DoArticuloFabricantes

@Entity(tableName = "Fabricante")
data class ArticuloFabricantesEntity(
    @PrimaryKey
    @ColumnInfo(name = "FirmCode") val FirmCode: Int,
    @ColumnInfo(name = "FirmName") val FirmName: String
)

fun DoArticuloFabricantes.toDatabase() = ArticuloFabricantesEntity(
    FirmCode = FirmCode,
    FirmName = FirmName
)

fun ArticuloFabricantes.toDatabase() = ArticuloFabricantesEntity(
    FirmCode = FirmCode,
    FirmName = FirmName
)
package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralVendedores

@Entity(tableName = "Vendedor")
data class GeneralVendedoresEntity(
    @PrimaryKey
    @ColumnInfo(name = "SlpCode") val SlpCode: Int,
    @ColumnInfo(name = "SlpName") val SlpName: String
)

fun GeneralVendedores.toDatabase() = GeneralVendedoresEntity(
    SlpCode = SlpCode,
    SlpName = SlpName
)

fun GeneralVendedoresEntity.toModel() = GeneralVendedores(
    SlpCode = SlpCode,
    SlpName = SlpName
)
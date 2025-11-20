package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralCentrosC

@Entity(tableName = "CentrosCosto")
data class GeneralCentrosCEntity(
    @PrimaryKey
    @ColumnInfo(name = "DimCode") val DimCode: Int,
    @ColumnInfo(name = "PrcCode") val PrcCode: String,
    @ColumnInfo(name = "PrcName") val PrcName: String
)

fun GeneralCentrosC.toDatabase() = GeneralCentrosCEntity(
    DimCode = DimCode,
    PrcCode = PrcCode,
    PrcName = PrcName
)

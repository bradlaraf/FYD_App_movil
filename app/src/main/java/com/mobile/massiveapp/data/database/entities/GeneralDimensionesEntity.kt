package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralDimensiones


@Entity(tableName = "Dimension")
data class GeneralDimensionesEntity(
    @PrimaryKey
    @ColumnInfo(name = "DimCode") val DimCode: Int,
    @ColumnInfo(name = "DimDesc") val DimDesc: String,
    @ColumnInfo(name = "DimName") val DimName: String
)

fun GeneralDimensiones.toDatabase() = GeneralDimensionesEntity(
    DimCode = DimCode,
    DimDesc = DimDesc,
    DimName = DimName
)

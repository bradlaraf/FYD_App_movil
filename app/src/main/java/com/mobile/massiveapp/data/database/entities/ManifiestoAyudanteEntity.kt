package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.ManifiestoAyudante


@Entity(tableName = "ManifiestoAyudante", primaryKeys = ["DocEntry", "LineId"])
data class ManifiestoAyudanteEntity (
    @ColumnInfo(name = "DocEntry") val DocEntry: Int,
    @ColumnInfo(name = "LineId") val LineId: Int,

    @ColumnInfo(name = "U_MSV_MA_CODIGO") val U_MSV_MA_CODIGO: Int,
    @ColumnInfo(name = "U_MSV_MA_NOMBRE") val U_MSV_MA_NOMBRE: String
)

fun ManifiestoAyudante.toDatabase() = ManifiestoAyudanteEntity(
    DocEntry = DocEntry,
    LineId = LineId,
    U_MSV_MA_CODIGO = U_MSV_MA_CODIGO,
    U_MSV_MA_NOMBRE = U_MSV_MA_NOMBRE,
)


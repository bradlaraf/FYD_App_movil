package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.ManifiestoDocumento

@Entity(tableName = "ManifiestoDocumento", primaryKeys = ["DocEntry", "LineId"])
data class ManifiestoDocumentoEntity(
    @ColumnInfo(name = "DocEntry") val DocEntry: Int,
    @ColumnInfo(name = "LineId") val LineId: Int,

    @ColumnInfo(name = "U_MSV_MA_OBJETO") val U_MSV_MA_OBJETO: Int,
    @ColumnInfo(name = "U_MSV_MA_TIPODOC") val U_MSV_MA_TIPODOC: String, // char(2)
    @ColumnInfo(name = "U_MSV_MA_CLAVE") val U_MSV_MA_CLAVE: Int,
    @ColumnInfo(name = "U_MSV_MA_NUMERO") val U_MSV_MA_NUMERO: Int,
    @ColumnInfo(name = "U_MSV_MA_SUNAT") val U_MSV_MA_SUNAT: String,
    @ColumnInfo(name = "U_MSV_MA_FECEMI") val U_MSV_MA_FECEMI: String, // char(10)
    @ColumnInfo(name = "U_MSV_MA_SOCCOD") val U_MSV_MA_SOCCOD: String,
    @ColumnInfo(name = "U_MSV_MA_SOCNOM") val U_MSV_MA_SOCNOM: String,
    @ColumnInfo(name = "U_MSV_MA_CONDP") val U_MSV_MA_CONDP: Int,
    @ColumnInfo(name = "U_MSV_MA_VENDEDOR") val U_MSV_MA_VENDEDOR: Int,
    @ColumnInfo(name = "U_MSV_MA_MON") val U_MSV_MA_MON: String,

    @ColumnInfo(name = "U_MSV_MA_TOTCONLOC") val U_MSV_MA_TOTCONLOC: Double,
    @ColumnInfo(name = "U_MSV_MA_TOTCONEXT") val U_MSV_MA_TOTCONEXT: Double,
    @ColumnInfo(name = "U_MSV_MA_TOTCRELOC") val U_MSV_MA_TOTCRELOC: Double,
    @ColumnInfo(name = "U_MSV_MA_TOTCREEXT") val U_MSV_MA_TOTCREEXT: Double,
    @ColumnInfo(name = "U_MSV_MA_UBIGEO") val U_MSV_MA_UBIGEO: String,
    @ColumnInfo(name = "U_MSV_MA_PESOT") val U_MSV_MA_PESOT: Double,
    @ColumnInfo(name = "U_MSV_MA_VOLUMENM") val U_MSV_MA_VOLUMENM: Double,

    @ColumnInfo(name = "U_MSV_MA_ESTADOE") val U_MSV_MA_ESTADOE: String, // char(1)
    @ColumnInfo(name = "U_MSV_MA_FECLLE") val U_MSV_MA_FECLLE: String,   // char(10)
    @ColumnInfo(name = "U_MSV_MA_HORLLE") val U_MSV_MA_HORLLE: String,   // char(8)
    @ColumnInfo(name = "U_MSV_MA_DISTRITO") val U_MSV_MA_DISTRITO: String
)

fun ManifiestoDocumento.toDatabase() = ManifiestoDocumentoEntity (
    DocEntry = DocEntry,
    LineId = LineId,
    U_MSV_MA_OBJETO = U_MSV_MA_OBJETO,
    U_MSV_MA_TIPODOC = U_MSV_MA_TIPODOC,
    U_MSV_MA_CLAVE = U_MSV_MA_CLAVE,
    U_MSV_MA_NUMERO = U_MSV_MA_NUMERO,
    U_MSV_MA_SUNAT = U_MSV_MA_SUNAT,
    U_MSV_MA_FECEMI = U_MSV_MA_FECEMI,
    U_MSV_MA_SOCCOD = U_MSV_MA_SOCCOD,
    U_MSV_MA_SOCNOM = U_MSV_MA_SOCNOM,
    U_MSV_MA_CONDP = U_MSV_MA_CONDP,
    U_MSV_MA_VENDEDOR = U_MSV_MA_VENDEDOR,
    U_MSV_MA_MON = U_MSV_MA_MON,
    U_MSV_MA_TOTCONLOC = U_MSV_MA_TOTCONLOC,
    U_MSV_MA_TOTCONEXT = U_MSV_MA_TOTCONEXT,
    U_MSV_MA_TOTCRELOC = U_MSV_MA_TOTCRELOC,
    U_MSV_MA_TOTCREEXT = U_MSV_MA_TOTCREEXT,
    U_MSV_MA_UBIGEO = U_MSV_MA_UBIGEO,
    U_MSV_MA_PESOT = U_MSV_MA_PESOT,
    U_MSV_MA_VOLUMENM = U_MSV_MA_VOLUMENM,
    U_MSV_MA_ESTADOE = U_MSV_MA_ESTADOE,
    U_MSV_MA_FECLLE = U_MSV_MA_FECLLE,
    U_MSV_MA_HORLLE = U_MSV_MA_HORLLE,
    U_MSV_MA_DISTRITO = U_MSV_MA_DISTRITO,
)

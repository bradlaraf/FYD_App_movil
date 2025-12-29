package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Manifiesto
import com.mobile.massiveapp.domain.model.DoManifiesto

@Entity(tableName = "Manifiesto")
data class ManifiestoEntity (
    @PrimaryKey
    @ColumnInfo(name = "DocEntry") val DocEntry: Int,

    @ColumnInfo(name = "DocNum") val DocNum: Int,
    @ColumnInfo(name = "U_MSV_MA_SERIE") val U_MSV_MA_SERIE: String,
    @ColumnInfo(name = "U_MSV_MA_FOLIO") val U_MSV_MA_FOLIO: Int,
    @ColumnInfo(name = "U_MSV_MA_ESTADO") val U_MSV_MA_ESTADO: String,      // char(1)
    @ColumnInfo(name = "U_MSV_MA_TIPODES") val U_MSV_MA_TIPODES: String,    // char(1)
    @ColumnInfo(name = "U_MSV_MA_FECSALIDA") val U_MSV_MA_FECSALIDA: String,// char(10)
    @ColumnInfo(name = "U_MSV_MA_DESCRIPCION") val U_MSV_MA_DESCRIPCION: String,
    @ColumnInfo(name = "U_MSV_MA_ALMACEN") val U_MSV_MA_ALMACEN: String,
    @ColumnInfo(name = "U_MSV_MA_ENC") val U_MSV_MA_ENC: String,
    @ColumnInfo(name = "U_MSV_MA_TRANSP") val U_MSV_MA_TRANSP: String,
    @ColumnInfo(name = "U_MSV_MA_TRANSPNO") val U_MSV_MA_TRANSPNO: String,
    @ColumnInfo(name = "U_MSV_MA_CON") val U_MSV_MA_CON: String,
    @ColumnInfo(name = "U_MSV_MA_CAM") val U_MSV_MA_CAM: String,

    @ColumnInfo(name = "U_MSV_MA_VPESOTO") val U_MSV_MA_VPESOTO: Double,
    @ColumnInfo(name = "U_MSV_MA_VVOLM") val U_MSV_MA_VVOLM: Double,
    @ColumnInfo(name = "U_MSV_MA_PESOTO") val U_MSV_MA_PESOTO: Double,
    @ColumnInfo(name = "U_MSV_MA_VOLM") val U_MSV_MA_VOLM: Double,
    @ColumnInfo(name = "U_MSV_MA_TOCONL") val U_MSV_MA_TOCONL: Double,
    @ColumnInfo(name = "U_MSV_MA_TOCONE") val U_MSV_MA_TOCONE: Double,
    @ColumnInfo(name = "U_MSV_MA_TOCREL") val U_MSV_MA_TOCREL: Double,
    @ColumnInfo(name = "U_MSV_MA_TOCREE") val U_MSV_MA_TOCREE: Double
)

fun Manifiesto.toDatabase() = ManifiestoEntity(
    DocEntry = DocEntry,
    DocNum = DocNum,
    U_MSV_MA_SERIE = U_MSV_MA_SERIE,
    U_MSV_MA_FOLIO = U_MSV_MA_FOLIO,
    U_MSV_MA_ESTADO = U_MSV_MA_ESTADO,
    U_MSV_MA_TIPODES = U_MSV_MA_TIPODES,
    U_MSV_MA_FECSALIDA = U_MSV_MA_FECSALIDA,
    U_MSV_MA_DESCRIPCION = U_MSV_MA_DESCRIPCION,
    U_MSV_MA_ALMACEN = U_MSV_MA_ALMACEN,
    U_MSV_MA_ENC = U_MSV_MA_ENC,
    U_MSV_MA_TRANSP = U_MSV_MA_TRANSP,
    U_MSV_MA_TRANSPNO = U_MSV_MA_TRANSPNO,
    U_MSV_MA_CON = U_MSV_MA_CON,
    U_MSV_MA_CAM = U_MSV_MA_CAM,
    U_MSV_MA_VPESOTO = U_MSV_MA_VPESOTO,
    U_MSV_MA_VVOLM = U_MSV_MA_VVOLM,
    U_MSV_MA_PESOTO = U_MSV_MA_PESOTO,
    U_MSV_MA_VOLM = U_MSV_MA_VOLM,
    U_MSV_MA_TOCONL = U_MSV_MA_TOCONL,
    U_MSV_MA_TOCONE = U_MSV_MA_TOCONE,
    U_MSV_MA_TOCREL = U_MSV_MA_TOCREL,
    U_MSV_MA_TOCREE = U_MSV_MA_TOCREE,
)

fun DoManifiesto.toDatabase() = ManifiestoEntity(
    DocEntry = DocEntry,
    DocNum = DocNum,
    U_MSV_MA_SERIE = U_MSV_MA_SERIE,
    U_MSV_MA_FOLIO = U_MSV_MA_FOLIO,
    U_MSV_MA_ESTADO = U_MSV_MA_ESTADO,
    U_MSV_MA_TIPODES = U_MSV_MA_TIPODES,
    U_MSV_MA_FECSALIDA = U_MSV_MA_FECSALIDA,
    U_MSV_MA_DESCRIPCION = U_MSV_MA_DESCRIPCION,
    U_MSV_MA_ALMACEN = U_MSV_MA_ALMACEN,
    U_MSV_MA_ENC = U_MSV_MA_ENC,
    U_MSV_MA_TRANSP = U_MSV_MA_TRANSP,
    U_MSV_MA_TRANSPNO = U_MSV_MA_TRANSPNO,
    U_MSV_MA_CON = U_MSV_MA_CON,
    U_MSV_MA_CAM = U_MSV_MA_CAM,
    U_MSV_MA_VPESOTO = U_MSV_MA_VPESOTO,
    U_MSV_MA_VVOLM = U_MSV_MA_VVOLM,
    U_MSV_MA_PESOTO = U_MSV_MA_PESOTO,
    U_MSV_MA_VOLM = U_MSV_MA_VOLM,
    U_MSV_MA_TOCONL = U_MSV_MA_TOCONL,
    U_MSV_MA_TOCONE = U_MSV_MA_TOCONE,
    U_MSV_MA_TOCREL = U_MSV_MA_TOCREL,
    U_MSV_MA_TOCREE = U_MSV_MA_TOCREE,
)
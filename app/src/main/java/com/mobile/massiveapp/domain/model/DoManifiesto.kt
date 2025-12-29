package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ManifiestoEntity

data class DoManifiesto(
    val DocEntry: Int,
    val DocNum: Int,
    val U_MSV_MA_SERIE: String,
    val U_MSV_MA_FOLIO: Int,
    val U_MSV_MA_ESTADO: String,
    val U_MSV_MA_TIPODES: String,
    val U_MSV_MA_FECSALIDA: String,
    val U_MSV_MA_DESCRIPCION: String,
    val U_MSV_MA_ALMACEN: String,
    val U_MSV_MA_ENC: String,
    val U_MSV_MA_TRANSP: String,
    val U_MSV_MA_TRANSPNO: String,
    val U_MSV_MA_CON: String,
    val U_MSV_MA_CAM: String,
    val U_MSV_MA_VPESOTO: Double,
    val U_MSV_MA_VVOLM: Double,
    val U_MSV_MA_PESOTO: Double,
    val U_MSV_MA_VOLM: Double,
    val U_MSV_MA_TOCONL: Double,
    val U_MSV_MA_TOCONE: Double,
    val U_MSV_MA_TOCREL: Double,
    val U_MSV_MA_TOCREE: Double
)

fun ManifiestoEntity.toDomain() = DoManifiesto(
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

data class DoManifiestoView(
    val NumeroManifiesto: String,
    val Conductor: String,
    val Vehiculo: String,
    val FechaSalida: String,
    val Estado: String,
    val Total: Double,
    val Saldo: Double,
)

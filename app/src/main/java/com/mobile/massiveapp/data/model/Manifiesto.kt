package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Manifiesto (
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
    val U_MSV_MA_TOCREE: Double,
    val Documento: List<ManifiestoDocumento>,

    /*"DocEntry":6,
      "DocNum":6,
      "U_MSV_MA_SERIE":"",
      "U_MSV_MA_FOLIO":0,
      "U_MSV_MA_ESTADO":"A",
      "U_MSV_MA_TIPODES":"T",
      "U_MSV_MA_FECSALIDA":"2025-12-16",
      "U_MSV_MA_DESCRIPCION":"CONSOLIDACIÓN DOCUMENTOS.",
      "U_MSV_MA_ALMACEN":"A8000",
      "U_MSV_MA_ENC":"0001",
      "U_MSV_MA_TRANSP":"P20603272596",
      "U_MSV_MA_TRANSPNO":"LOGISTICA SACSAYHUAMAN CARGO S.A.C",
      "U_MSV_MA_CON":"CON001",
      "U_MSV_MA_CAM":"VEH0001",
      "U_MSV_MA_VPESOTO":80000.00,
      "U_MSV_MA_VVOLM":0.00,
      "U_MSV_MA_PESOTO":0.00,
      "U_MSV_MA_VOLM":0.00,
      "U_MSV_MA_TOCONL":0.00,
      "U_MSV_MA_TOCONE":0.00,
      "U_MSV_MA_TOCREL":0.00,
      "U_MSV_MA_TOCREE":0.00,
      "Documento":*/
): MappingInteface<Manifiesto>(){
    constructor():this(
        DocEntry = -1,
        DocNum = -1,
        U_MSV_MA_SERIE = "",
        U_MSV_MA_FOLIO = -1,
        U_MSV_MA_ESTADO = "",
        U_MSV_MA_TIPODES = "",
        U_MSV_MA_FECSALIDA = "",
        U_MSV_MA_DESCRIPCION = "",
        U_MSV_MA_ALMACEN = "",
        U_MSV_MA_ENC = "",
        U_MSV_MA_TRANSP = "",
        U_MSV_MA_TRANSPNO = "",
        U_MSV_MA_CON = "",
        U_MSV_MA_CAM = "",
        U_MSV_MA_VPESOTO = 0.0,
        U_MSV_MA_VVOLM = 0.0,
        U_MSV_MA_PESOTO = 0.0,
        U_MSV_MA_VOLM = 0.0,
        U_MSV_MA_TOCONL = 0.0,
        U_MSV_MA_TOCONE = 0.0,
        U_MSV_MA_TOCREL = 0.0,
        U_MSV_MA_TOCREE = 0.0,
        Documento = emptyList(),
    )

    override fun map(data: List<Manifiesto>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Manifiesto>): List<Any> {
        return data.map {it.DocEntry}
    }
}
package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ManifiestoDocumento(
    val DocEntry: Int,
    val LineId: Int,
    val U_MSV_MA_OBJETO: Int,
    val U_MSV_MA_TIPODOC: String,
    val U_MSV_MA_CLAVE: Int,
    val U_MSV_MA_NUMERO: Int,
    val U_MSV_MA_SUNAT: String,
    val U_MSV_MA_FECEMI: String,
    val U_MSV_MA_SOCCOD: String,
    val U_MSV_MA_SOCNOM: String,
    val U_MSV_MA_CONDP: Int,
    val U_MSV_MA_VENDEDOR: Int,
    val U_MSV_MA_MON: String,
    val U_MSV_MA_TOTCONLOC: Double,
    val U_MSV_MA_TOTCONEXT: Double,
    val U_MSV_MA_TOTCRELOC: Double,
    val U_MSV_MA_TOTCREEXT: Double,
    val U_MSV_MA_UBIGEO: String,
    val U_MSV_MA_PESOT: Double,
    val U_MSV_MA_VOLUMENM: Double,
    val U_MSV_MA_ESTADOE: String,
    val U_MSV_MA_FECLLE: String,
    val U_MSV_MA_HORLLE: String,
    val U_MSV_MA_DISTRITO: String


):MappingInteface<ManifiestoDocumento>(){
    constructor():this(
        DocEntry = -1,
        LineId = -1,
        U_MSV_MA_OBJETO = -1,
        U_MSV_MA_TIPODOC = "",
        U_MSV_MA_CLAVE = -1,
        U_MSV_MA_NUMERO = -1,
        U_MSV_MA_SUNAT = "",
        U_MSV_MA_FECEMI = "",
        U_MSV_MA_SOCCOD = "",
        U_MSV_MA_SOCNOM = "",
        U_MSV_MA_CONDP = -1,
        U_MSV_MA_VENDEDOR = -1,
        U_MSV_MA_MON = "",
        U_MSV_MA_TOTCONLOC = 0.0,
        U_MSV_MA_TOTCONEXT = 0.0,
        U_MSV_MA_TOTCRELOC = 0.0,
        U_MSV_MA_TOTCREEXT = 0.0,
        U_MSV_MA_UBIGEO = "",
        U_MSV_MA_PESOT = 0.0,
        U_MSV_MA_VOLUMENM = 0.0,
        U_MSV_MA_ESTADOE = "",
        U_MSV_MA_FECLLE = "",
        U_MSV_MA_HORLLE = "",
        U_MSV_MA_DISTRITO = "",
    )
    override fun map(data: List<ManifiestoDocumento>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ManifiestoDocumento>): List<Any> {
        return data.map { "${it.DocEntry}_${it.LineId}" }
    }

}

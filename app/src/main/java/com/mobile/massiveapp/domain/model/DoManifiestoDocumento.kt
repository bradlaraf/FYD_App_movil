package com.mobile.massiveapp.domain.model

data class DoManifiestoDocumento(
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
)

data class DoManifiestoDocumentoView(
    val TipoDocumento: String,
    val SUNAT: String,
    val Vendedor: String,
    val NombreCliente: String,
    val Total: Double,
    val Saldo: Double,
    val Pagado: Double,
    val Moneda: String,
    val MonedaSimbolo: String,
    val DocEntryFactura: Int,
    val CodigoSocio: String,
)

package com.mobile.massiveapp.domain.model

data class DoRutaComercialDetalleView(
    val AccDocEntry: String,
    val LineNum: Int,
    val CardCode: String,
    val Address: String,
    val CardName: String,
    val Street: String,
    val AccMigrated: String,
    val Status: String,
    val AddressType: String,
    val Comments: String,
    val ObjType: Int,
    val DocEntry: Int,
    val Latitud: String,
    val Longitud: String
)

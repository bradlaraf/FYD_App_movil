package com.mobile.massiveapp.domain.model

data class DoRutaComercialDetalleAdminView(
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

fun DoRutaComercialDetalleView.toAdminView(latitud: String = "", longitud: String = "") =
    DoRutaComercialDetalleAdminView(
        AccDocEntry = AccDocEntry,
        LineNum     = LineNum,
        CardCode    = CardCode,
        Address     = Address,
        CardName    = CardName,
        Street      = Street,
        AccMigrated = AccMigrated,
        Status      = Status,
        AddressType = AddressType,
        Comments    = Comments,
        ObjType     = ObjType,
        DocEntry    = DocEntry,
        Latitud     = latitud,
        Longitud    = longitud
    )

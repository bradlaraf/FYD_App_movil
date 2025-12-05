package com.mobile.massiveapp.data.model

data class GrupoDescuento(
    val AbsEntry: Int,
    val Type: String,
    val Obj: String,
    val ObjCode: String,
    val DiscRef: String,
    val ValidFor: String,
    val ValidForm: String,
    val ValidTo: String,
    val Lineas: List<GrupoDescuentoDetalle>
)

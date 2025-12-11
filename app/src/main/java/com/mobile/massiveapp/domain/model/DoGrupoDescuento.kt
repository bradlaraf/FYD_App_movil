package com.mobile.massiveapp.domain.model

data class DoGrupoDescuento(
    val AbsEntry: Int,
    val Type: String,
    val Obj: String,
    val ObjCode: String,
    val DiscRef: String,
    val ValidFor: String,
    val ValidForm: String,
    val ValidTo: String,
    val Lineas: List<DoGrupoDescuentoDetalle>
)

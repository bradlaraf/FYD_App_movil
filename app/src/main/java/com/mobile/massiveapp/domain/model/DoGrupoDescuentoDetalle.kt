package com.mobile.massiveapp.domain.model

data class DoGrupoDescuentoDetalle(
    val AbsEntry: Int,
    val Obj: String,
    val ObjKey: String,
    val DiscRef: String,
    val Discount: Double,
    val PayFor: Double,
    val ForFree: Double,
    val UpTo: Double
)

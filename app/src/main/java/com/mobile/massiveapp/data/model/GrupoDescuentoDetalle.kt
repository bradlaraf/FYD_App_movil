package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GrupoDescuentoDetalle(
    val AbsEntry: Int,
    val Obj: String,
    val ObjKey: String,
    val DiscRef: String,
    val Discount: Double,
    val PayFor: Double,
    val ForFree: Double,
    val UpTo: Double
):MappingInteface<GrupoDescuentoDetalle>() {
    constructor():this(
        AbsEntry = 0,
        Obj = "",
        ObjKey = "",
        DiscRef = "",
        Discount = 0.0,
        PayFor = 0.0,
        ForFree = 0.0,
        UpTo = 0.0,
    )
    override fun map(data: List<GrupoDescuentoDetalle>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GrupoDescuentoDetalle>): List<Any> {
        return data.map { it.AbsEntry }
    }
}

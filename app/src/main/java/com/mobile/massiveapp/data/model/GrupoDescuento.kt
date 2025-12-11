package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

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
) : MappingInteface<GrupoDescuento>(){
    constructor():this(
        AbsEntry = 0,
        Type = "",
        Obj = "",
        ObjCode = "",
        DiscRef = "",
        ValidFor = "",
        ValidForm = "",
        ValidTo = "",
        Lineas = emptyList()
    )
    override fun map(data: List<GrupoDescuento>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GrupoDescuento>): List<Any> {
        return data.map { it.AbsEntry }
    }

}

package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class RutaComercial(
    val AccDocEntry: String,
    val AccAction: String,
    val AccCreateDate: String,
    val AccCreateHour: String,
    val AccCreateUser: String,
    val AccError: String,
    val Canceled: String,
    val AccNotificado: String,
    val AccFinalized: String,
    val AccMigrated: String,
    val AccMovil: String,
    val AccUpdateDate: String,
    val AccUpdateHour: String,
    val AccUpdateUser: String,
    val AccControl: String,
    val ObjType: Int,
    val DocEntry: Int,
    val DocNum: Int,
    val NombreVendedor: String,
    val FechaRuta: String,
    val Comments: String,
    val Detalle: List<RutaComercialDetalle>
) : MappingInteface<RutaComercial>() {
    constructor() : this(
        AccDocEntry = "",
        AccAction = "",
        AccCreateDate = "",
        AccCreateHour = "",
        AccCreateUser = "",
        AccError = "",
        Canceled = "",
        AccNotificado = "",
        AccFinalized = "",
        AccMigrated = "",
        AccMovil = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        AccControl = "",
        ObjType = -1,
        DocEntry = -1,
        DocNum = -1,
        NombreVendedor = "",
        FechaRuta = "",
        Comments = "",
        Detalle = emptyList()
    )

    override fun map(data: List<RutaComercial>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<RutaComercial>): List<Any> {
        return data.map { it.AccDocEntry }
    }
}

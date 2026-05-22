package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class RutaComercialDetalle(
    val AccDocEntry: String,
    val DocLine: Int,
    val AccAction: String,
    val AccCreateDate: String,
    val AccCreateHour: String,
    val AccCreateUser: String,
    val AccUpdateDate: String,
    val AccUpdateHour: String,
    val AccUpdateUser: String,
    val AccMigrated: String,
    val AccControl: String,
    val Status: String,
    val CardCode: String,
    val Address: String,
    val AddressType: String,
    val Comments: String,
    val ObjType: Int,
    val DocEntry: Int
) : MappingInteface<RutaComercialDetalle>() {
    constructor() : this(
        AccDocEntry = "",
        DocLine = -1,
        AccAction = "",
        AccCreateDate = "",
        AccCreateHour = "",
        AccCreateUser = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        AccMigrated = "",
        AccControl = "",
        Status = "P",
        CardCode = "",
        Address = "",
        AddressType = "",
        Comments = "",
        ObjType = -1,
        DocEntry = -1
    )

    override fun map(data: List<RutaComercialDetalle>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<RutaComercialDetalle>): List<Any> {
        return data.map { "${it.AccDocEntry}_${it.DocLine}" }
    }
}

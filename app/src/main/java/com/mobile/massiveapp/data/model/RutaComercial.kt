package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.RutaComercialEntity
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
    val SlpCode: Int,
    val DocDate: String,
    val Comments: String,
    var Lineas: List<RutaComercialDetalle>
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
        SlpCode = -1,
        DocDate = "",
        Comments = "",
        Lineas = emptyList()
    )

    override fun map(data: List<RutaComercial>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<RutaComercial>): List<Any> {
        return data.map { it.AccDocEntry }
    }
}

fun RutaComercialEntity.toModel(lista: List<RutaComercialDetalle>) = RutaComercial(
        AccDocEntry = AccDocEntry,
        AccAction = AccAction,
        AccCreateDate = AccCreateDate,
        AccCreateHour = AccCreateHour,
        AccCreateUser = AccCreateUser,
        AccError = AccError,
        Canceled = Canceled,
        AccNotificado = AccNotificado,
        AccFinalized = AccFinalized,
        AccMigrated = AccMigrated,
        AccMovil = AccMovil,
        AccUpdateDate = AccUpdateDate,
        AccUpdateHour = AccUpdateHour,
        AccUpdateUser = AccUpdateUser,
        AccControl = AccControl,
        ObjType = ObjType,
        DocEntry = DocEntry,
        DocNum = DocNum,
        SlpCode = SlpCode,
        DocDate = DocDate,
        Comments = Comments,
        Lineas =  lista
    )



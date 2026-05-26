package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.RutaComercialEntity

data class DoRutaComercial(
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
    val Comments: String
)

fun RutaComercialEntity.toDomain() = DoRutaComercial(
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
    Comments = Comments
)

data class DoRutaComercialView(
    val AccDocEntry: String,
    val FechaRuta: String,
    val NombreVendedor: String,
    val CantidadClientes: Int,
    val AccMigrated: String,
    val Canceled: String
)


data class DireccionesClienteEvent(
    val cardCode: String,
    val calles: List<String>,
    val codigos: List<String>
)
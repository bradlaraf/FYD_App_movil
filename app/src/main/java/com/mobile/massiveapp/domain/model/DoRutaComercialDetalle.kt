package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.RutaComercialDetalleEntity

data class DoRutaComercialDetalle(
    val AccDocEntry: String,
    val LineNum: Int,
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
)

fun RutaComercialDetalleEntity.toDomain() = DoRutaComercialDetalle(
    AccDocEntry = AccDocEntry,
    LineNum = LineNum,
    AccAction = AccAction,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser,
    AccMigrated = AccMigrated,
    AccControl = AccControl,
    Status = Status,
    CardCode = CardCode,
    Address = Address,
    AddressType = AddressType,
    Comments = Comments,
    ObjType = ObjType,
    DocEntry = DocEntry
)

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
    val DocEntry: Int,
    val U_MSV_CP_LATITUD: String,
    val U_MSV_CP_LONGITUD: String,

    val Country: String,
    val State: String,
    val County: String,//Provincia
    val City: String, //Ciudad
    val ZipCode: String, //Vacio
    val Street: String, //Calle
    val Block: String, //Referencia-va vacio
    val Ubigeo: String,
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
    DocEntry = DocEntry,
    U_MSV_CP_LATITUD = U_MSV_CP_LATITUD,
    U_MSV_CP_LONGITUD = U_MSV_CP_LONGITUD,
    Country = Country,
    State = State,
    County = County,
    City = City,
    ZipCode = ZipCode,
    Street = Street,
    Block = Block,
    Ubigeo = U_MSV_FE_UBI,
)

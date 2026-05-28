package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.RutaComercialDetalleEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class RutaComercialDetalle(
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

    val Country: String,
    val State: String,
    val County: String,//Provincia
    val City: String, //Ciudad
    val ZipCode: String, //Vacio
    val Street: String, //Calle
    val Block: String, //Referencia-va vacio
    val U_MSV_FE_UBI: String,

    val AccControl: String,
    val Status: String,
    val CardCode: String,
    val Address: String,
    val AdresType: String,
    val Comments: String,
    val ObjType: Int,
    val DocEntry: Int,
    val U_MSV_CP_LATITUD: String,
    val U_MSV_CP_LONGITUD: String
) : MappingInteface<RutaComercialDetalle>() {
    constructor() : this(
        AccDocEntry = "",
        LineNum = -1,
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
        AdresType = "",
        Comments = "",
        ObjType = -1,
        DocEntry = -1,
        U_MSV_CP_LATITUD = "",
        U_MSV_CP_LONGITUD = "",

        Country = "",
        State = "",
        County = "",
        City = "",
        ZipCode = "",
        Street = "",
        Block = "",
        U_MSV_FE_UBI = "",
    )

    override fun map(data: List<RutaComercialDetalle>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<RutaComercialDetalle>): List<Any> {
        return data.map { "${it.AccDocEntry}_${it.LineNum}" }
    }
}

fun RutaComercialDetalleEntity.toModel() = RutaComercialDetalle(
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
    AdresType = AddressType,
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
    U_MSV_FE_UBI = U_MSV_FE_UBI,
)

package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.SocioDireccionesEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface
import com.mobile.massiveapp.domain.model.DoSocioDirecciones

data class SocioDirecciones(
    val AccAction: String,
    val AccControl: String,
    val AccCreateDate: String,
    val AccCreateHour: String,
    val AccCreateUser: String,
    val AccDocEntry: String,
    val AccLocked: String,
    val AccMigrated: String,
    val AccUpdateDate: String,
    val AccUpdateHour: String,
    val AccUpdateUser: String,
    val Address: String,
    val AdresType: String,
    val Block: String,
    val CardCode: String,
    val City: String,
    val Country: String,
    val County: String,
    val LineNum: Int,
    val State: String,
    val Street: String,
    val U_MSV_CP_LATITUD: String,
    val U_MSV_CP_LONGITUD: String,
    val U_MSV_FE_UBI: String,
    val ZipCode: String
): MappingInteface<SocioDirecciones>(){
    constructor() : this(
        AccAction = "",
        AccControl = "",
        AccCreateDate = "",
        AccCreateHour = "",
        AccCreateUser = "",
        AccDocEntry = "",
        AccLocked = "",
        AccMigrated = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        Address = "",
        AdresType = "",
        Block = "",
        CardCode = "",
        City = "",
        Country = "",
        County = "",
        LineNum = 0,
        State = "",
        Street = "",
        U_MSV_CP_LATITUD = "",
        U_MSV_CP_LONGITUD = "",
        U_MSV_FE_UBI = "",
        ZipCode = ""
    )

    override fun map(data: List<SocioDirecciones>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<SocioDirecciones>): List<Any> {
        return data.map { "${it.AccDocEntry}_${it.LineNum}" }
    }
}

fun SocioDireccionesEntity.toModel() = SocioDirecciones(
    CardCode = CardCode,
    AccAction = AccAction,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccDocEntry = AccDocEntry,
    AccLocked = AccLocked,
    AccMigrated = AccMigrated,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser,
    Address = Address,
    AdresType = AdresType,
    Block = Block,
    City = City,
    Country = Country,
    County = County,
    LineNum = LineNum,
    State = State,
    Street = Street,
    U_MSV_CP_LATITUD = U_MSV_CP_LATITUD,
    U_MSV_CP_LONGITUD = U_MSV_CP_LONGITUD,
    U_MSV_FE_UBI = U_MSV_FE_UBI,
    ZipCode = ZipCode,
    AccControl = AccControl
)

fun DoSocioDirecciones.toModel() = SocioDirecciones(
    CardCode = CardCode,
    AccAction = AccAction,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccDocEntry = AccDocEntry,
    AccLocked = AccLocked,
    AccMigrated = AccMigrated,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser,
    Address = Address,
    AdresType = AdresType,
    Block = Block,
    City = City,
    Country = Country,
    County = County,
    LineNum = LineNum,
    State = State,
    Street = Street,
    U_MSV_CP_LATITUD = U_MSV_CP_LATITUD,
    U_MSV_CP_LONGITUD = U_MSV_CP_LONGITUD,
    U_MSV_FE_UBI = U_MSV_FE_UBI,
    ZipCode = ZipCode,
    AccControl = AccControl
)

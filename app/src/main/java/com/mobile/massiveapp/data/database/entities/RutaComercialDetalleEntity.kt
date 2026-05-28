package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.RutaComercialDetalle
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalle

@Entity(tableName = "RutaComercialDetalle", primaryKeys = ["AccDocEntry", "LineNum"])
data class RutaComercialDetalleEntity(
    @ColumnInfo(name = "AccAction") val AccAction: String,
    @ColumnInfo(name = "AccCreateDate") val AccCreateDate: String,
    @ColumnInfo(name = "AccCreateHour") val AccCreateHour: String,
    @ColumnInfo(name = "AccCreateUser") val AccCreateUser: String,
    @ColumnInfo(name = "AccUpdateDate") val AccUpdateDate: String,
    @ColumnInfo(name = "AccUpdateHour") val AccUpdateHour: String,
    @ColumnInfo(name = "AccUpdateUser") val AccUpdateUser: String,

    @ColumnInfo(name = "AccMigrated") val AccMigrated: String,
    @ColumnInfo(name = "AccControl") val AccControl: String,

    @ColumnInfo(name = "Status") val Status: String, //P(pendiente) R(rechazado) A(aceptado)
    @ColumnInfo(name = "AccDocEntry") val AccDocEntry: String,
    @ColumnInfo(name = "LineNum") val LineNum: Int,
    @ColumnInfo(name = "CardCode") val CardCode: String,
    @ColumnInfo(name = "Address") val Address: String,
    @ColumnInfo(name = "AddressType") val AddressType: String,
    @ColumnInfo(name = "Comments") val Comments: String,
    @ColumnInfo(name = "ObjType") val ObjType: Int,
    @ColumnInfo(name = "DocEntry") val DocEntry: Int,
    @ColumnInfo(name = "U_MSV_CP_LATITUD") val U_MSV_CP_LATITUD: String,
    @ColumnInfo(name = "U_MSV_CP_LONGITUD") val U_MSV_CP_LONGITUD: String,

    @ColumnInfo(name = "Country")val Country: String,
    @ColumnInfo(name = "State")val State: String,
    @ColumnInfo(name = "County")val County: String,//Provincia
    @ColumnInfo(name = "City")val City: String, //Ciudad
    @ColumnInfo(name = "ZipCode")val ZipCode: String, //Vacio
    @ColumnInfo(name = "Street")val Street: String, //Calle
    @ColumnInfo(name = "Block")val Block: String, //Referencia-va vacio
    @ColumnInfo(name = "U_MSV_FE_UBI")val U_MSV_FE_UBI: String,
)

fun RutaComercialDetalle.toDatabase() = RutaComercialDetalleEntity(
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
    AddressType = AdresType,
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

fun DoRutaComercialDetalle.toDatabase() = RutaComercialDetalleEntity(
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
    U_MSV_FE_UBI = Ubigeo,
)

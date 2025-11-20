package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.SocioDirecciones
import com.mobile.massiveapp.domain.model.DoSocioDirecciones

@Entity(tableName = "SocioDirecciones", primaryKeys = ["CardCode", "LineNum", "AdresType"])
data class SocioDireccionesEntity (
    @ColumnInfo(name = "CardCode") val CardCode: String,
    @ColumnInfo(name = "AccAction") val AccAction: String,
    @ColumnInfo(name = "AccCreateDate") val AccCreateDate: String,
    @ColumnInfo(name = "AccCreateHour") val AccCreateHour: String,
    @ColumnInfo(name = "AccCreateUser") val AccCreateUser: String,
    @ColumnInfo(name = "AccDocEntry") val AccDocEntry: String,
    @ColumnInfo(name = "AccLocked") val AccLocked: String,
    @ColumnInfo(name = "AccControl") val AccControl: String,
    @ColumnInfo(name = "AccMigrated") val AccMigrated: String,
    @ColumnInfo(name = "AccUpdateDate") val AccUpdateDate: String,
    @ColumnInfo(name = "AccUpdateHour") val AccUpdateHour: String,
    @ColumnInfo(name = "AccUpdateUser") val AccUpdateUser: String,
    @ColumnInfo(name = "Address") val Address: String,
    @ColumnInfo(name = "AdresType") val AdresType: String,
    @ColumnInfo(name = "Block") val Block: String,
    @ColumnInfo(name = "City") val City: String,
    @ColumnInfo(name = "Country") val Country: String,
    @ColumnInfo(name = "County") val County: String,
    @ColumnInfo(name = "LineNum") val LineNum: Int,
    @ColumnInfo(name = "State") val State: String,
    @ColumnInfo(name = "Street") val Street: String,
    @ColumnInfo(name = "U_MSV_CP_LATITUD") val U_MSV_CP_LATITUD: String,
    @ColumnInfo(name = "U_MSV_CP_LONGITUD") val U_MSV_CP_LONGITUD: String,
    @ColumnInfo(name = "U_MSV_FE_UBI") val U_MSV_FE_UBI: String,
    @ColumnInfo(name = "ZipCode") val ZipCode: String
)

fun DoSocioDirecciones.toDatabase() = SocioDireccionesEntity(
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

fun SocioDirecciones.toDatabase() = SocioDireccionesEntity(
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
    LineNum = LineNum.toInt(),
    State = State,
    Street = Street,
    U_MSV_CP_LATITUD = U_MSV_CP_LATITUD,
    U_MSV_CP_LONGITUD = U_MSV_CP_LONGITUD,
    U_MSV_FE_UBI = U_MSV_FE_UBI,
    ZipCode = ZipCode,
    AccControl = AccControl
)


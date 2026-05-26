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
    @ColumnInfo(name = "DocEntry") val DocEntry: Int
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
    AddressType = AddressType,
    Comments = Comments,
    ObjType = ObjType,
    DocEntry = DocEntry
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
    DocEntry = DocEntry
)

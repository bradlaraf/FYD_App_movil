package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.RutaComercial
import com.mobile.massiveapp.domain.model.DoRutaComercial

@Entity(tableName = "RutaComercial")
data class RutaComercialEntity(
    @PrimaryKey
    @ColumnInfo(name = "AccDocEntry") val AccDocEntry: String,
    @ColumnInfo(name = "AccAction") val AccAction: String,
    @ColumnInfo(name = "AccCreateDate") val AccCreateDate: String,
    @ColumnInfo(name = "AccCreateHour") val AccCreateHour: String,
    @ColumnInfo(name = "AccCreateUser") val AccCreateUser: String,
    @ColumnInfo(name = "AccError") val AccError: String,
    @ColumnInfo(name = "Canceled") val Canceled: String,
    @ColumnInfo(name = "AccNotificado") val AccNotificado: String,
    @ColumnInfo(name = "AccFinalized") val AccFinalized: String,
    @ColumnInfo(name = "AccMigrated") val AccMigrated: String,
    @ColumnInfo(name = "AccMovil") val AccMovil: String,
    @ColumnInfo(name = "AccUpdateDate") val AccUpdateDate: String,
    @ColumnInfo(name = "AccUpdateHour") val AccUpdateHour: String,
    @ColumnInfo(name = "AccUpdateUser") val AccUpdateUser: String,
    @ColumnInfo(name = "AccControl") val AccControl: String,

    @ColumnInfo(name = "ObjType") val ObjType: Int,
    @ColumnInfo(name = "DocEntry") val DocEntry: Int,
    @ColumnInfo(name = "DocNum") val DocNum: Int,
    @ColumnInfo(name = "SlpCode") val SlpCode: Int,
    @ColumnInfo(name = "DocDate") val DocDate: String,
    @ColumnInfo(name = "Comments") val Comments: String
)

fun RutaComercial.toDatabase() = RutaComercialEntity(
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

fun DoRutaComercial.toDatabase() = RutaComercialEntity(
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

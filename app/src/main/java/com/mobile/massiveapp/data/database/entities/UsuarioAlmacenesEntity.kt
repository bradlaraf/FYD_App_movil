package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.UsuarioAlmacenes

@Entity(tableName = "UsuarioAlmacenes", primaryKeys = ["Code", "WhsCode"])
data class UsuarioAlmacenesEntity(
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "LineNum") val LineNum: Int,
    @ColumnInfo(name = "TypeW") val TypeW: String,
    @ColumnInfo(name = "WhsCode") val WhsCode: String,
    @ColumnInfo(name = "AccAction") val AccAction: String,
    @ColumnInfo(name = "AccControl") val AccControl: String,
    @ColumnInfo(name = "AccCreateDate") val AccCreateDate: String,
    @ColumnInfo(name = "AccCreateHour") val AccCreateHour: String,
    @ColumnInfo(name = "AccCreateUser") val AccCreateUser: String,
    @ColumnInfo(name = "AccLocked") val AccLocked: String,
    @ColumnInfo(name = "AccMigrated") val AccMigrated: String,
    @ColumnInfo(name = "AccUpdateDate") val AccUpdateDate: String,
    @ColumnInfo(name = "AccUpdateHour") val AccUpdateHour: String,
    @ColumnInfo(name = "AccUpdateUser") val AccUpdateUser: String
)


fun UsuarioAlmacenes.toDatabase() = UsuarioAlmacenesEntity(
    Code = Code,
    LineNum = LineNum,
    TypeW = TypeW,
    WhsCode = WhsCode,
    AccAction = AccAction,
    AccControl = AccControl,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccLocked = AccLocked,
    AccMigrated = AccMigrated,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser
)

fun UsuarioAlmacenesEntity.toModel() = UsuarioAlmacenes(
    Code = Code,
    LineNum = LineNum,
    TypeW = TypeW,
    WhsCode = WhsCode,
    AccAction = AccAction,
    AccControl = AccControl,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccLocked = AccLocked,
    AccMigrated = AccMigrated,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser
)

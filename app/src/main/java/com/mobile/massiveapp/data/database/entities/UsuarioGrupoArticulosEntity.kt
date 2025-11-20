package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.UsuarioGrupoArticulos

@Entity(tableName = "UsuarioGrupoArticulos", primaryKeys = ["Code", "ItmsGrpCod"])
data class UsuarioGrupoArticulosEntity(
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "ItmsGrpCod") val ItmsGrpCod: Int,
    @ColumnInfo(name = "LineNum") val LineNum: Int,
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

fun UsuarioGrupoArticulos.toDatabase() = UsuarioGrupoArticulosEntity(
    Code = Code,
    ItmsGrpCod = ItmsGrpCod,
    LineNum = LineNum,
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

fun UsuarioGrupoArticulosEntity.toModel() = UsuarioGrupoArticulos(
    Code = Code,
    ItmsGrpCod = ItmsGrpCod,
    LineNum = LineNum,
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


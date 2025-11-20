package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.SocioContactos
import com.mobile.massiveapp.domain.model.DoSocioContactos

@Entity(primaryKeys = ["CardCode", "CntctCode"] ,tableName = "SocioContactos")
data class SocioContactosEntity (
    @ColumnInfo (name = "CardCode") var CardCode: String,
    @ColumnInfo(name = "Name") var Name: String,
    @ColumnInfo(name = "AccAction") var AccAction: String,
    @ColumnInfo(name = "AccCreateDate") var AccCreateDate: String,
    @ColumnInfo(name = "AccCreateHour") var AccCreateHour: String,
    @ColumnInfo(name = "AccCreateUser") var AccCreateUser: String,
    @ColumnInfo(name = "AccDocEntry") var AccDocEntry: String,
    @ColumnInfo(name = "AccLocked") var AccLocked: String,
    @ColumnInfo(name = "AccControl") var AccControl: String,
    @ColumnInfo(name = "AccMigrated") var AccMigrated: String,
    @ColumnInfo(name = "AccUpdateDate") var AccUpdateDate: String,
    @ColumnInfo(name = "AccUpdateHour") var AccUpdateHour: String,
    @ColumnInfo(name = "AccUpdateUser") var AccUpdateUser: String,
    @ColumnInfo(name = "Cellolar") var Cellolar: String,
    @ColumnInfo(name = "CntctCode") var CntctCode: Int,
    @ColumnInfo(name = "E_Mail") var E_Mail: String,
    @ColumnInfo(name = "LicTradNum") var LicTradNum: String,
    @ColumnInfo(name = "Position") var Position: String,
    @ColumnInfo(name = "Tel1") var Tel1: String,
    @ColumnInfo(name = "Tel2") var Tel2: String,
)

fun DoSocioContactos.toDatabase() = SocioContactosEntity(
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
    Cellolar = Cellolar,
    CntctCode = CntctCode,
    E_Mail = E_Mail,
    LicTradNum = LicTradNum,
    Name = Name,
    Position = Position,
    Tel1 = Tel1,
    Tel2 = Tel2,
    AccControl = AccControl
)

fun SocioContactos.toDatabase() = SocioContactosEntity(
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
    Cellolar = Cellolar,
    CntctCode = CntctCode,
    E_Mail = E_Mail,
    LicTradNum = LicTradNum,
    Name = Name,
    Position = Position,
    Tel1 = Tel1,
    Tel2 = Tel2,
    AccControl = AccControl
)
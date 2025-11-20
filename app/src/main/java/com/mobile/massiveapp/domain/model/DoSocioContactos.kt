package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.SocioContactosEntity
import com.mobile.massiveapp.data.model.SocioContactos

data class DoSocioContactos (
    val AccAction: String,
    val AccCreateDate: String,
    val AccCreateHour: String,
    val AccCreateUser: String,
    val AccDocEntry: String,
    val AccLocked: String,
    val AccControl: String,
    val AccMigrated: String,
    val AccUpdateDate: String,
    val AccUpdateHour: String,
    val AccUpdateUser: String,
    val CardCode: String,
    val Cellolar: String,
    val CntctCode: Int,
    val E_Mail: String,
    val LicTradNum: String,
    val Name: String,
    val Position: String,
    val Tel1: String,
    val Tel2: String
    ) {
    constructor() : this(
        AccAction = "",
        AccCreateDate = "",
        AccCreateHour = "",
        AccCreateUser = "",
        AccDocEntry = "",
        AccLocked = "",
        AccMigrated = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        CardCode = "",
        Cellolar = "",
        CntctCode = -10,
        E_Mail = "",
        LicTradNum = "",
        Name = "",
        Position = "",
        Tel1 = "",
        Tel2 = "",
        AccControl = ""
    )
}

fun SocioContactos.toDomain() = DoSocioContactos(
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

fun SocioContactosEntity.toDomain() = DoSocioContactos(
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


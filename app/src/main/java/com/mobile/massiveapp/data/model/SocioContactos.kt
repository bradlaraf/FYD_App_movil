package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.SocioContactosEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface
import com.mobile.massiveapp.domain.model.DoSocioContactos

data class SocioContactos(
    val AccAction: String,
    val AccCreateDate: String,
    val AccCreateHour: String,
    val AccCreateUser: String,
    val AccDocEntry: String,
    val AccControl: String,
    val AccLocked: String,
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
): MappingInteface<SocioContactos>(){
    constructor() : this(
        AccAction = "",
        AccCreateDate = "",
        AccCreateHour = "",
        AccCreateUser = "",
        AccDocEntry = "",
        AccControl = "",
        AccLocked = "",
        AccMigrated = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        CardCode = "",
        Cellolar = "",
        CntctCode = 0,
        E_Mail = "",
        LicTradNum = "",
        Name = "",
        Position = "",
        Tel1 = "",
        Tel2 = ""
    )

    override fun map(data: List<SocioContactos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<SocioContactos>): List<Any> {
        return data.map { "${it.AccDocEntry}_${it.CntctCode}" }
    }
}
fun SocioContactosEntity.toModel() = SocioContactos(
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

fun DoSocioContactos.toModel() = SocioContactos(
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
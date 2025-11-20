package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class UsuarioZonas(
    val AccAction: String,
    val AccControl: String,
    val AccCreateDate: String,
    val AccCreateHour: String,
    val AccCreateUser: String,
    val AccLocked: String,
    val AccMigrated: String,
    val AccUpdateDate: String,
    val AccUpdateHour: String,
    val AccUpdateUser: String,
    val Code: String,
    val CodeZona: String,
    val LineNum: Int
): MappingInteface<UsuarioZonas>() {

    constructor() : this(
        AccAction = "",
        AccControl = "",
        AccCreateDate = "",
        AccCreateHour = "",
        AccCreateUser = "",
        AccLocked = "",
        AccMigrated = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        Code = "",
        CodeZona = "",
        LineNum = 0
    )

    override fun map(data: List<UsuarioZonas>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<UsuarioZonas>): List<Any> {
        return emptyList()
    }

}
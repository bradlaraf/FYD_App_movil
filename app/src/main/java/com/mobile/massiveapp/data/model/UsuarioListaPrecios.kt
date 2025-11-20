package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class UsuarioListaPrecios(
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
    val LineNum: Int,
    val ListNum: Int
): MappingInteface<UsuarioListaPrecios>() {

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
        LineNum = 0,
        ListNum = 0
    )

    override fun map(data: List<UsuarioListaPrecios>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<UsuarioListaPrecios>): List<Any> {
        return emptyList()
    }

}
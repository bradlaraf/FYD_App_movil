package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class UsuarioGrupoSocios(
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
    val GroupCode: Int,
    val LineNum: Int
): MappingInteface<UsuarioGrupoSocios>() {

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
        GroupCode = 0,
        LineNum = 0
    )

    override fun map(data: List<UsuarioGrupoSocios>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<UsuarioGrupoSocios>): List<Any> {
        return emptyList()
    }

}
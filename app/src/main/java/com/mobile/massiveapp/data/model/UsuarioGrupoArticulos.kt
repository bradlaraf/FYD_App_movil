package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class UsuarioGrupoArticulos(
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
    val ItmsGrpCod: Int,
    val LineNum: Int
): MappingInteface<UsuarioGrupoArticulos>() {

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
        ItmsGrpCod = 0,
        LineNum = 0
    )

    override fun map(data: List<UsuarioGrupoArticulos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<UsuarioGrupoArticulos>): List<Any> {
        return emptyList()
    }

}
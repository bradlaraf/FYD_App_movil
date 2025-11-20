package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class UsuarioAlmacenes(
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
    val TypeW: String,
    val WhsCode: String
): MappingInteface<UsuarioAlmacenes>() {

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
        TypeW = "",
        WhsCode = ""
    )

    override fun map(data: List<UsuarioAlmacenes>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<UsuarioAlmacenes>): List<Any> {
        return emptyList()
    }

}
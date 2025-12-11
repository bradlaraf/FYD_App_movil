package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Conductor(
    val Code: String,
    val Name: String,
    val U_MSV_MA_CONLIC: String,
    val U_MSV_MA_COTDOC: String,
    val U_MSV_MA_CONNDOC: String,
    val U_MSV_MA_CONOMB: String,
    val U_MSV_MA_EMTDOC: String,
    val U_MSV_MA_EMNNDOC: String,
    val U_MSV_MA_EMNOMB: String,
    val U_MSV_MA_MTC: String
) : MappingInteface<Conductor> () {
    constructor():this(
        Code = "",
        Name = "",
        U_MSV_MA_CONLIC = "",
        U_MSV_MA_COTDOC = "",
        U_MSV_MA_CONNDOC = "",
        U_MSV_MA_CONOMB = "",
        U_MSV_MA_EMTDOC = "",
        U_MSV_MA_EMNNDOC = "",
        U_MSV_MA_EMNOMB = "",
        U_MSV_MA_MTC = ""
    )
    override fun map(data: List<Conductor>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Conductor>): List<Any> {
        return data.map { it.Code }
    }

}

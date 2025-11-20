package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Articulo(
    val ItemCode: String,
    val AccLocked: String,
    val FirmCode: Int,
    val InvntItem: String,
    val InvntryUom: String,
    val ItemName: String,
    val ItmsGrpCod: Int,
    val IuoMEntry: Int,
    val SalUnitMsr: String,
    val SuoMEntry: Int,
    val UgpEntry: Int
) : MappingInteface<Articulo>() {

    constructor() : this(
        AccLocked = "",
        FirmCode = 0,
        InvntItem = "",
        InvntryUom = "",
        ItemCode = "",
        ItemName = "",
        ItmsGrpCod = 0,
        IuoMEntry = 0,
        SalUnitMsr = "",
        SuoMEntry = 0,
        UgpEntry = 0
    )

    override fun map(data: List<Articulo>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Articulo>): List<Any> {
        return data.map { it.ItemCode }
    }
}



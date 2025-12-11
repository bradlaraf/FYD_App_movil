package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.PrecioEspecial1Entity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class PrecioEspecial1(
    val Code: String,
    val ItemCode: String,
    val CardCode: String,
    val LINENUM: Int,
    val Price: Double,
    val Currency: String,
    val Discount: Double,
    val ListNum: Int,
    val FromDate: String,
    val ToDate: String
): MappingInteface<PrecioEspecial1>(){
    constructor(): this(
        Code = "",
        ItemCode = "",
        CardCode = "",
        LINENUM = 0,
        Price = 0.0,
        Currency = "",
        Discount = 0.0,
        ListNum = 0,
        FromDate = "",
        ToDate = "",
    )
    override fun map(data: List<PrecioEspecial1>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<PrecioEspecial1>): List<Any> {
        return data.map { it.Code }
    }

}

fun PrecioEspecial1Entity.toModel() = PrecioEspecial1(
    Code = Code,
    ItemCode = ItemCode,
    CardCode = CardCode,
    LINENUM = LINENUM,
    Price = Price,
    Currency = Currency,
    Discount = Discount,
    ListNum = ListNum,
    FromDate = FromDate,
    ToDate = ToDate
)


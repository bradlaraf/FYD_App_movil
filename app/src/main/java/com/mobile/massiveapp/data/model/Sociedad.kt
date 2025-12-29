package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Sociedad(
    val AliasName: String,
    val CompnyName: String,
    val E_Mail: String,
    val TaxIdNum: String,
    val WebSite: String,
    val PriceDec: Int
): MappingInteface<Sociedad>(){
    constructor(): this(
        AliasName = "",
        CompnyName = "",
        E_Mail = "",
        TaxIdNum = "",
        WebSite = "",
        PriceDec = 2
    )
    override fun map(data: List<Sociedad>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Sociedad>): List<Any> {
        return data.map { it.TaxIdNum }
    }

}

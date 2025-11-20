package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralCuentasB(
    val AbsEntry: Int,
    val Account: String,
    val BankCode: String,
    val BankKey: Int,
    val Branch: String,
    val GLAccount: String
):MappingInteface<GeneralCuentasB>(){
    constructor(): this(
        AbsEntry = 0,
        Account = "",
        BankCode = "",
        BankKey = 0,
        Branch = "",
        GLAccount = ""
    )

    override fun map(data: List<GeneralCuentasB>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralCuentasB>): List<Any> {
        return data.map { it.AbsEntry }
    }
}
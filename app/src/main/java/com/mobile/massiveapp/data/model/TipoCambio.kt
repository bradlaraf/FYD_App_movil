package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class TipoCambio(
    val Code: String,
    val RateDate: String,
    val Currency: String,
    val Rate: Double
):MappingInteface<TipoCambio>() {
    constructor():this(
        Code = "",
        RateDate = "",
        Currency = "",
        Rate = 0.000
    )
    override fun map(data: List<TipoCambio>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<TipoCambio>): List<Any> {
        return data.map { it.Code }
    }
}

package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.FormaPagoEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class FormaPago(
    val Code: String,
    val Name: String
): MappingInteface<FormaPago>(){
    constructor(): this(
        Code = "",
        Name = ""
    )
    override fun map(data: List<FormaPago>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<FormaPago>): List<Any> {
        return data.map { it.Code }
    }
}


fun FormaPagoEntity.toModel() = FormaPago(
    Code = Code,
    Name = Name
)

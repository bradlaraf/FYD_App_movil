package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralEmpleados(
    val empID: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val position: Int
):MappingInteface<GeneralEmpleados>(){
    constructor(): this(
        empID = 0,
        firstName = "",
        lastName = "",
        middleName = "",
        position = 0
    )
    override fun map(data: List<GeneralEmpleados>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralEmpleados>): List<Any> {
        return data.map { it.empID }
    }

}
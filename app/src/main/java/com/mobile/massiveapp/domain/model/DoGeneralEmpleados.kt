package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.GeneralEmpleadosEntity
import com.mobile.massiveapp.data.model.GeneralEmpleados

data class DoGeneralEmpleados(
    val empID: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val position: Int
) {
    constructor() : this(
        -1,
        "",
        "",
        "",
        0)
}

fun GeneralEmpleados.toDomain() = DoGeneralEmpleados(
    empID = empID,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    position = position
)

fun GeneralEmpleadosEntity.toDomain() = DoGeneralEmpleados(
    empID = empID,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    position = position
)



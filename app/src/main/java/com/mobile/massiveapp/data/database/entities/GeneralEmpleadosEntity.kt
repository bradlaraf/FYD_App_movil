package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralEmpleados
import com.mobile.massiveapp.domain.model.DoGeneralEmpleados

@Entity(tableName = "Empleado")
data class GeneralEmpleadosEntity(
    @PrimaryKey
    @ColumnInfo(name = "empID") val empID: Int,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "middleName") val middleName: String,
    @ColumnInfo(name = "position") val position: Int
)

fun GeneralEmpleadosEntity.toDatabase() = DoGeneralEmpleados(
    empID = empID,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    position = position
)

fun GeneralEmpleados.toDatabase() = GeneralEmpleadosEntity(
    empID = empID,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    position = position
)
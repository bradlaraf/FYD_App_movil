package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralDepartamentos

@Entity(tableName = "Departamento")
data class GeneralDepartamentosEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code:String,
    @ColumnInfo(name = "Name") val Name: String
)

fun GeneralDepartamentos.toDatabase() = GeneralDepartamentosEntity(
    Code = Code,
    Name = Name
)

fun GeneralDepartamentosEntity.toModel() = GeneralDepartamentos(
    Code = Code,
    Name = Name
)

package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Sucursal
import com.mobile.massiveapp.domain.model.DoSucursal

@Entity(tableName = "Sucursal")
data class SucursalEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String,
)

fun Sucursal.toDatabase() = SucursalEntity(
    Code = Code,
    Name = Name
)

fun DoSucursal.toDatabase() = SucursalEntity(
    Code = Code,
    Name = Name
)
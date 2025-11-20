package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ArticuloAlmacenes
import com.mobile.massiveapp.domain.model.DoArticuloAlmacenes

@Entity(tableName = "Almacenes")
data class ArticuloAlmacenesEntity(

    @PrimaryKey
    @ColumnInfo(name = "WhsCode") val WhsCode: String,
    @ColumnInfo(name = "WhsName") val WhsName: String,
    @ColumnInfo(name = "TypeW") val TypeW: String,
    @ColumnInfo(name = "Street") val Street: String,
    @ColumnInfo(name = "Block") val Block: String,
    @ColumnInfo(name = "City") val City: String,
    @ColumnInfo(name = "County") val County: String,
    @ColumnInfo(name = "State") val State: String,
    @ColumnInfo(name = "Country") val Country: String,
    @ColumnInfo(name = "Ubigeo") val Ubigeo: String,
    @ColumnInfo(name = "Principal") val Principal: String

)

fun DoArticuloAlmacenes.toDatabase() = ArticuloAlmacenesEntity(
    Block = Block,
    City = City,
    Country = Country,
    County = County,
    Principal = Principal,
    State = State,
    Street = Street,
    TypeW = TypeW,
    Ubigeo = Ubigeo,
    WhsCode = WhsCode,
    WhsName = WhsName
)

fun ArticuloAlmacenes.toDatabase() = ArticuloAlmacenesEntity(
    Block = Block,
    City = City,
    Country = Country,
    County = County,
    Principal = Principal,
    State = State,
    Street = Street,
    TypeW = TypeW,
    Ubigeo = Ubigeo,
    WhsCode = WhsCode,
    WhsName = WhsName
)

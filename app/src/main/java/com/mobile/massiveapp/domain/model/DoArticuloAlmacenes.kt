package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloAlmacenesEntity
import com.mobile.massiveapp.data.model.ArticuloAlmacenes

data class DoArticuloAlmacenes(
    val Block: String,
    val City: String,
    val Country: String,
    val County: String,
    val Principal: String,
    val State: String,
    val Street: String,
    val TypeW: String,
    val Ubigeo: String,
    val WhsCode: String,
    val WhsName: String
)

fun ArticuloAlmacenes.toDomain() = DoArticuloAlmacenes(
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

fun ArticuloAlmacenesEntity.toDomain() = DoArticuloAlmacenes(
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

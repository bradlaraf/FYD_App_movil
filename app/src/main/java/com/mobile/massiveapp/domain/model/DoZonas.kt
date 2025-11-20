package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.GeneralZonasEntity

data class DoZonas (
    val Code: String,
    val Name: String
    )
fun GeneralZonasEntity.toDomain() = DoZonas(
    Code = Code,
    Name = Name
)
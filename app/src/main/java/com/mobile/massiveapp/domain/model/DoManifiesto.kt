package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ManifiestoEntity

data class DoManifiesto(
    val Numero: String,
    val Conductor: String,
    val Vehiculo: String,
    val FechaSalida: String,
    val Estado: String
)

fun ManifiestoEntity.toDomain() = DoManifiesto(
    Numero = Numero,
    Conductor = Conductor,
    Vehiculo = Vehiculo,
    FechaSalida = FechaSalida,
    Estado = Estado
)

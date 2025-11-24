package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Manifiesto
import com.mobile.massiveapp.domain.model.DoManifiesto

@Entity(tableName = "Manifiesto")
data class ManifiestoEntity (
    @PrimaryKey
    @ColumnInfo(name = "Numero") val Numero: String,
    @ColumnInfo(name = "Conductor") val Conductor: String,
    @ColumnInfo(name = "Vehiculo") val Vehiculo: String,
    @ColumnInfo(name = "FechaSalida") val FechaSalida: String,
    @ColumnInfo(name = "Estado") val Estado: String
)
fun Manifiesto.toDatabase() = ManifiestoEntity(
    Numero = Numero,
    Conductor = Conductor,
    Vehiculo = Vehiculo,
    FechaSalida = FechaSalida,
    Estado = Estado
)

fun DoManifiesto.toDatabase() = ManifiestoEntity(
    Numero = Numero,
    Conductor = Conductor,
    Vehiculo = Vehiculo,
    FechaSalida = FechaSalida,
    Estado = Estado
)
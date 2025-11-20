package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.domain.model.DoSocioDniConsulta

@Entity (tableName = "SocioDniConsulta")
data class SocioDniConsultaEntity (
    @PrimaryKey
    @ColumnInfo(name = "numeroDocumento") val numeroDocumento: String,
    @ColumnInfo(name = "apellidoPaterno") val apellidoPaterno: String,
    @ColumnInfo(name = "apellidoMaterno") val apellidoMaterno: String,
    @ColumnInfo(name = "condicion") val condicion: String,
    @ColumnInfo(name = "departamento") val departamento: String,
    @ColumnInfo(name = "direccion") val direccion: String,
    @ColumnInfo(name = "distrito") val distrito: String,
    @ColumnInfo(name = "dpto") val dpto: String,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "interior") val interior: String,
    @ColumnInfo(name = "kilometro") val kilometro: String,
    @ColumnInfo(name = "lote") val lote: String,
    @ColumnInfo(name = "manzana") val manzana: String,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "nombres") val nombres: String,
    @ColumnInfo(name = "numero") val numero: String,
    @ColumnInfo(name = "provincia") val provincia: String,
    @ColumnInfo(name = "tipoDocumento") val tipoDocumento: String,
    @ColumnInfo(name = "ubigeo") val ubigeo: String,
    @ColumnInfo(name = "viaNombre") val viaNombre: String,
    @ColumnInfo(name = "viaTipo") val viaTipo: String,
    @ColumnInfo(name = "zonaCodigo") val zonaCodigo: String,
    @ColumnInfo(name = "zonaTipo") val zonaTipo: String


)

fun DoSocioDniConsulta.toDatabase() = SocioDniConsultaEntity(
    numeroDocumento = numeroDocumento,
    apellidoPaterno = apellidoPaterno,
    apellidoMaterno = apellidoMaterno,
    condicion = condicion,
    departamento = departamento,
    direccion = direccion,
    distrito = distrito,
    dpto = dpto,
    estado = estado,
    interior = interior,
    kilometro = kilometro,
    lote = lote,
    manzana = manzana,
    nombre = nombre,
    nombres = nombres,
    numero = numero,
    provincia = provincia,
    tipoDocumento = tipoDocumento,
    ubigeo = ubigeo,
    viaNombre = viaNombre,
    viaTipo = viaTipo,
    zonaCodigo = zonaCodigo,
    zonaTipo = zonaTipo
)

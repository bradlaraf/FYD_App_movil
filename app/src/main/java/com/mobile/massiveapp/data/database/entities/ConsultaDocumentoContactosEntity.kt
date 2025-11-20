package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ConsultaDocumentoContacto

@Entity(tableName = "ConsultaDocumentoContactos")
data class ConsultaDocumentoContactosEntity (
    @PrimaryKey
    @ColumnInfo(name ="NumeroDocumento") val NumeroDocumento: String,
    @ColumnInfo(name = "Id") val Id: String,
    @ColumnInfo(name = "Documento") val Documento: String,
    @ColumnInfo(name = "Nombre") val Nombre: String,
    @ColumnInfo(name = "Cargo") val Cargo: String,
    @ColumnInfo(name = "Posicion") val Posicion: String,
    @ColumnInfo(name = "Fecha") val Fecha: String
)

fun ConsultaDocumentoContactosEntity.toModel() = ConsultaDocumentoContacto(
    NumeroDocumento = NumeroDocumento,
    Id = Id,
    Documento = Documento,
    Nombre = Nombre,
    Cargo = Cargo,
    Posicion = Posicion,
    Fecha = Fecha
)

fun ConsultaDocumentoContacto.toDatabase() = ConsultaDocumentoContactosEntity(
    NumeroDocumento = NumeroDocumento?:"",
    Id = Id?:"",
    Documento = Documento?:"",
    Nombre = Nombre?:"",
    Cargo = Cargo?:"",
    Posicion = Posicion?:"",
    Fecha = Fecha?:""
)
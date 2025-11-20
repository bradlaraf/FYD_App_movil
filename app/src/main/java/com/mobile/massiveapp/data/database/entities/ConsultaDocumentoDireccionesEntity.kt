package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ConsultaDocumentoDireccion

@Entity("ConsultaDocumentoDirecciones")
data class ConsultaDocumentoDireccionesEntity (
    @PrimaryKey
    @ColumnInfo(name ="NumeroDocumento") val NumeroDocumento: String,
    @ColumnInfo(name = "Tipo") val Tipo: String,
    @ColumnInfo(name = "Estacion") val Estacion: String,
    @ColumnInfo(name = "Codigo") val Codigo: String,
    @ColumnInfo(name = "Calle") val Calle: String,
    @ColumnInfo(name = "PaisCodigo") val PaisCodigo: String,
    @ColumnInfo(name = "PaisNombre") val PaisNombre: String,
    @ColumnInfo(name = "DepartamentoCodigo") val DepartamentoCodigo: String,
    @ColumnInfo(name = "DepartamentoNombre") val DepartamentoNombre: String,
    @ColumnInfo(name = "ProvinciaCodigo") val ProvinciaCodigo: String,
    @ColumnInfo(name = "ProvinciaNombre") val ProvinciaNombre: String,
    @ColumnInfo(name = "DistritoCodigo") val DistritoCodigo: String,
    @ColumnInfo(name = "DistritoNombre") val DistritoNombre: String,
    @ColumnInfo(name = "DistritoNombreAlterno") val DistritoNombreAlterno: String,
    @ColumnInfo(name = "CodigoPostal") val CodigoPostal: String,
    @ColumnInfo(name = "Referencia") val Referencia: String,
    @ColumnInfo(name = "Latitud") val Latitud: Double,
    @ColumnInfo(name = "Longitud") val Longitud: Double,
    @ColumnInfo(name = "Nombre") val Nombre: String,
    @ColumnInfo(name = "Fecha") val Fecha: String
    )

fun ConsultaDocumentoDireccionesEntity.toModel() = ConsultaDocumentoDireccion(
    NumeroDocumento = NumeroDocumento,
    Tipo = Tipo,
    Estacion = Estacion,
    Codigo = Codigo,
    Calle = Calle,
    PaisCodigo = PaisCodigo,
    PaisNombre = PaisNombre,
    DepartamentoCodigo = DepartamentoCodigo,
    DepartamentoNombre = DepartamentoNombre,
    ProvinciaCodigo = ProvinciaCodigo,
    ProvinciaNombre = ProvinciaNombre,
    DistritoCodigo = DistritoCodigo,
    DistritoNombre = DistritoNombre,
    DistritoNombreAlterno = DistritoNombreAlterno,
    CodigoPostal = CodigoPostal,
    Referencia = Referencia,
    Latitud = Latitud,
    Longitud = Longitud,
    Nombre = Nombre,
    Fecha = Fecha
)

fun ConsultaDocumentoDireccion.toDatabase() = ConsultaDocumentoDireccionesEntity(
    NumeroDocumento = NumeroDocumento?:"",
    Tipo = Tipo?:"",
    Estacion = Estacion?:"",
    Codigo = Codigo?:"",
    Calle = Calle?:"",
    PaisCodigo = PaisCodigo?:"",
    PaisNombre = PaisNombre?:"",
    DepartamentoCodigo = DepartamentoCodigo?:"",
    DepartamentoNombre = DepartamentoNombre?:"",
    ProvinciaCodigo = ProvinciaCodigo?:"",
    ProvinciaNombre = ProvinciaNombre?:"",
    DistritoCodigo = DistritoCodigo?:"",
    DistritoNombre = DistritoNombre?:"",
    DistritoNombreAlterno = DistritoNombreAlterno?:"",
    CodigoPostal = CodigoPostal?:"",
    Referencia = Referencia?:"",
    Latitud = Latitud?:0.0,
    Longitud = Longitud?:0.0,
    Nombre = Nombre?:"",
    Fecha = Fecha?:""
)
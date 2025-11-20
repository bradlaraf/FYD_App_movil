package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ConsultaDocumento
import com.mobile.massiveapp.domain.model.DoConsultaDocumento

@Entity(tableName = "ConsultaDocumento")
data class ConsultaDocumentoEntity(

    @PrimaryKey
    @ColumnInfo(name = "NumeroDocumento") val NumeroDocumento: String,
    @ColumnInfo(name = "ActividadEconomica1") val ActividadEconomica1: String,
    @ColumnInfo(name = "ActividadEconomica2") val ActividadEconomica2: String,
    @ColumnInfo(name = "ActividadEconomica3") val ActividadEconomica3: String,
    @ColumnInfo(name = "Activo") val Activo: String,
    @ColumnInfo(name = "AgentePercepcion") val AgentePercepcion: String,
    @ColumnInfo(name = "AgenteRetenedor") val AgenteRetenedor: String,
    @ColumnInfo(name = "ApellidoMaterno") val ApellidoMaterno: String,
    @ColumnInfo(name = "ApellidoPaterno") val ApellidoPaterno: String,
    @ColumnInfo(name = "BuenContribuyente") val BuenContribuyente: String,
    @ColumnInfo(name = "Consulta") val Consulta: String,
    @ColumnInfo(name = "Error") val Error: String,
    @ColumnInfo(name = "Fecha") val Fecha: String,
    @ColumnInfo(name = "Habido") val Habido: String,
    @ColumnInfo(name = "Habilitar") val Habilitar: String,
    @ColumnInfo(name = "Indicador") val Indicador: String,
    @ColumnInfo(name = "NombreComercial") val NombreComercial: String,
    @ColumnInfo(name = "PersonaNaturalConNegocio") val PersonaNaturalConNegocio: String,
    @ColumnInfo(name = "PrimerNombre") val PrimerNombre: String,
    @ColumnInfo(name = "RazonSocial") val RazonSocial: String,
    @ColumnInfo(name = "SegundoNombre") val SegundoNombre: String,
    @ColumnInfo(name = "Telefono1") val Telefono1: String,
    @ColumnInfo(name = "Telefono2") val Telefono2: String,
    @ColumnInfo(name = "Telefono3") val Telefono3: String,
    @ColumnInfo(name = "TipoDocumento") val TipoDocumento: String,
    @ColumnInfo(name = "TipoPersona") val TipoPersona: String,
)

fun DoConsultaDocumento.toDatabase() = ConsultaDocumentoEntity(
    ActividadEconomica1 = ActividadEconomica1,
    ActividadEconomica2 = ActividadEconomica2,
    ActividadEconomica3 = ActividadEconomica3,
    Activo = Activo,
    AgentePercepcion = AgentePercepcion,
    AgenteRetenedor = AgenteRetenedor,
    ApellidoMaterno = ApellidoMaterno,
    ApellidoPaterno = ApellidoPaterno,
    BuenContribuyente = BuenContribuyente,
    Consulta = Consulta,
    Error = Error,
    Fecha = Fecha,
    Habido = Habido,
    Habilitar = Habilitar,
    Indicador = Indicador,
    NombreComercial = NombreComercial,
    NumeroDocumento = NumeroDocumento,
    PersonaNaturalConNegocio = PersonaNaturalConNegocio,
    PrimerNombre = PrimerNombre,
    RazonSocial = RazonSocial,
    SegundoNombre = SegundoNombre,
    Telefono1 = Telefono1,
    Telefono2 = Telefono2,
    Telefono3 = Telefono3,
    TipoDocumento = TipoDocumento,
    TipoPersona = TipoPersona,
)

fun ConsultaDocumento.toDatabase() = ConsultaDocumentoEntity(
    ActividadEconomica1 = ActividadEconomica1?:"",
    ActividadEconomica2 = ActividadEconomica2?:"",
    ActividadEconomica3 = ActividadEconomica3?:"",
    Activo = Activo?:"",
    AgentePercepcion = AgentePercepcion?:"",
    AgenteRetenedor = AgenteRetenedor?:"",
    ApellidoMaterno = ApellidoMaterno?:"",
    ApellidoPaterno = ApellidoPaterno?:"",
    BuenContribuyente = BuenContribuyente?:"",
    Consulta = Consulta?:"",
    Error = Error?:"",
    Fecha = Fecha?:"",
    Habido = Habido?:"",
    Habilitar = Habilitar?:"",
    Indicador = Indicador?:"",
    NombreComercial = NombreComercial?:"",
    NumeroDocumento = NumeroDocumento?:"",
    PersonaNaturalConNegocio = PersonaNaturalConNegocio?:"",
    PrimerNombre = PrimerNombre?:"",
    RazonSocial = RazonSocial?:"",
    SegundoNombre = SegundoNombre?:"",
    Telefono1 = Telefono1?:"",
    Telefono2 = Telefono2?:"",
    Telefono3 = Telefono3?:"",
    TipoDocumento = TipoDocumento?:"",
    TipoPersona = TipoPersona?:""
)


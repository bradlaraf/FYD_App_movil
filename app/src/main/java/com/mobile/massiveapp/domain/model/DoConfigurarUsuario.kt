package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ConfigurarUsuariosEntity

data class DoConfigurarUsuario (
    val AccStatusSession: String,

    val Code: String,
    val Password: String,
    val IdPhone1: String,

    val Name: String,
    val Email: String,
    val Phone1: String,

    val DefaultSlpCode: String,

    val DefaultOrderSeries: String,
    val DefaultPagoRSeries: String,
    val DefaultSNSerieCli: String,
    val DefaultPriceList: String,
    val DefaultWarehouse: String,
    val DefaultCurrency: String,
    val DefaultTaxCode: String,
    val DefaultProyecto: String,

    val DefaultAcctCodeCh: String,
    val DefaultAcctCodeDe: String,
    val DefaultAcctCodeEf: String,
    val DefaultAcctCodeTr: String,
    val DefaultConductor: String,
    val DefaultZona: String,

    val SuperUser: String,
    val AccLocked: String,
    val CanApprove: String,
    val CanCreate: String,
    val CanDecline: String,
    val CanEditPrice: String,
    val CanUpdate: String,

    val ListaPrecios: Int,
    val GrupoArticulos: Int,
    val GrupoSocios: Int,
    val Almacenes: Int,
    val Zonas: Int
) {
    constructor(): this(
        AccStatusSession = "",
        Code ="",
        Name="",
        Email="",
        SuperUser="",
        DefaultSlpCode="",
        Password="",
        AccLocked="",
        IdPhone1 = "",
        CanApprove = "",
        CanCreate = "",
        CanDecline = "",
        CanEditPrice = "",
        CanUpdate = "",
        Phone1 = "",
        DefaultOrderSeries = "",
        DefaultPagoRSeries = "",
        DefaultSNSerieCli = "",
        DefaultPriceList = "",
        DefaultWarehouse = "",
        DefaultCurrency = "",
        DefaultTaxCode = "",
        DefaultProyecto = "",
        DefaultAcctCodeCh = "",
        DefaultAcctCodeDe = "",
        DefaultAcctCodeEf = "",
        DefaultAcctCodeTr = "",
        DefaultConductor = "",
        DefaultZona = "",
        ListaPrecios = 0,
        GrupoArticulos = 0,
        GrupoSocios = 0,
        Almacenes = 0,
        Zonas = 0,
    )
}

fun ConfigurarUsuariosEntity.toDomain() = DoConfigurarUsuario(
    AccStatusSession = AccStatusSession,
    Code = Code,
    Name = Name,
    Email = Email,
    SuperUser = SuperUser,
    DefaultSlpCode = DefaultSlpCode.toString(),
    Password = Password,
    AccLocked = AccLocked,
    IdPhone1 = IdPhone1,
    CanApprove = CanApprove,
    CanCreate = CanCreate,
    CanDecline = CanDecline,
    CanEditPrice = CanEditPrice,
    CanUpdate = CanUpdate,
    Phone1 = Phone1,
    DefaultOrderSeries = DefaultOrderSeries.toString(),
    DefaultPagoRSeries = DefaultPagoRSeries.toString(),
    DefaultSNSerieCli = DefaultSNSerieCli.toString(),
    DefaultPriceList = DefaultPriceList.toString(),
    DefaultWarehouse = DefaultWarehouse,
    DefaultCurrency = DefaultCurrency,
    DefaultTaxCode = DefaultTaxCode,
    DefaultProyecto = DefaultProyecto,
    DefaultAcctCodeCh = DefaultAcctCodeCh,
    DefaultAcctCodeDe = DefaultAcctCodeDe,
    DefaultAcctCodeEf = DefaultAcctCodeEf,
    DefaultAcctCodeTr = DefaultAcctCodeTr,
    DefaultConductor = DefaultConductor,
    DefaultZona = DefaultZona,
    ListaPrecios = 0,
    GrupoArticulos = 0,
    GrupoSocios = 0,
    Almacenes = 0,
    Zonas = 0,
)

data class DoValidarUsuario(
    val Codigo:Boolean,
    val Email:Boolean,
    val IdTelefono:Boolean,
    val Vendedor:Boolean,
    val Telefono:Boolean,
    val Nombre:Boolean
){
    constructor():this(
        Codigo = false,
        Email = false,
        IdTelefono = false,
        Vendedor = false,
        Telefono = false,
        Nombre = false
    )
}

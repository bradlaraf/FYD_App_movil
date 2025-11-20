package com.mobile.massiveapp.data.model

data class Client(
    val Address: String,
    val CardCode: String,
    val CardName: String,
    val CondicionPago: String,
    val Correo: String,
    val Grupo: String,
    val Indicador: String,
    val ListaPrecio: String,
    val Moneda: String,
    val NumDoc: String,
    val Telefono1: String,
    val Telefono2: String?,
    val TelefonoMovil: String,
    val TipoDoc: String,
    val TipoPersona: String,
    val Zona: String
)
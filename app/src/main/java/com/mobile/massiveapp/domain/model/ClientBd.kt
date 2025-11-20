package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ClientEntity
import com.mobile.massiveapp.data.model.Client

data class ClientBd (
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

fun Client.toDomain()= ClientBd(Address, CardCode, CardName, CondicionPago, Correo, Grupo, Indicador,
    ListaPrecio, Moneda, NumDoc, Telefono1, Telefono2, TelefonoMovil, TipoDoc, TipoPersona, Zona)

fun ClientEntity.toDomain()= ClientBd(Address, CardCode, CardName, CondicionPago, Correo, Grupo, Indicador,
    ListaPrecio, Moneda, NumDoc, Telefono1, Telefono2, TelefonoMovil, TipoDoc, TipoPersona, Zona)

package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.domain.model.ClientBd

@Entity(tableName = "Cliente_pedido")
data class ClientEntity (
    @PrimaryKey
    @ColumnInfo(name = "CardCode") val CardCode: String,
    @ColumnInfo(name = "Address") val Address: String,
    @ColumnInfo(name = "CardName") val CardName: String,
    @ColumnInfo(name = "CondicionPago") val CondicionPago: String,
    @ColumnInfo(name = "Correo") val Correo: String,
    @ColumnInfo(name = "Grupo") val Grupo: String,
    @ColumnInfo(name = "Indicador") val Indicador: String,
    @ColumnInfo(name = "ListaPrecio") val ListaPrecio: String,
    @ColumnInfo(name = "Moneda") val Moneda: String,
    @ColumnInfo(name = "NumDoc") val NumDoc: String,
    @ColumnInfo(name = "Telefono1") val Telefono1: String,
    @ColumnInfo(name = "Telefono2") val Telefono2: String?,
    @ColumnInfo(name = "TelefonoMovil") val TelefonoMovil: String,
    @ColumnInfo(name = "TipoDoc") val TipoDoc: String,
    @ColumnInfo(name = "TipoPersona") val TipoPersona: String,
    @ColumnInfo(name = "Zona") val Zona: String
    )

fun ClientBd.toDataBase() = ClientEntity(
    CardCode = CardCode,
    CardName = CardName,
    Address = Address,
    CondicionPago = CondicionPago,
    Correo = Correo,
    Grupo = Grupo,
    Indicador = Indicador,
    ListaPrecio = ListaPrecio,
    Moneda = Moneda,
    NumDoc = NumDoc,
    Telefono1 = Telefono1,
    Telefono2 = Telefono2,
    TelefonoMovil = TelefonoMovil,
    TipoDoc = TipoDoc,
    TipoPersona = TipoPersona,
    Zona = Zona
)
package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ClientePedidosEntity
import java.util.Objects

data class DoClientePedido(
    val AccNotificado: String,
    val AccDocEntry: String,
    val AccMigrated: String,
    val AccFinalized: String,
    var CANCELED: String,
    val CardName: String,
    val CardCode: String,
    val DocDate: String,
    val DocStatus: String,
    val DocTotal: Double,
    val NumAtCard: String,
    val Indicator: String,
    val ObjType: Int
)

fun ClientePedidosEntity.toDomain() = DoClientePedido(
    AccNotificado = AccNotificado,
    AccDocEntry = AccDocEntry,
    AccMigrated = AccMigrated,
    AccFinalized = AccFinalized,
    CANCELED = CANCELED,
    CardName = CardName,
    DocStatus = DocStatus,
    CardCode = CardCode,
    DocDate = DocDate,
    DocTotal = DocTotal,
    NumAtCard = NumAtCard,
    Indicator = Indicator,
    ObjType = ObjType
)



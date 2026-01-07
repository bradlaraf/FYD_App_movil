package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloCantidadesEntity
import com.mobile.massiveapp.data.model.ArticuloCantidad

data class DoArticuloCantidades(
    val AvgPrice: Double,
    val IsCommited: Double,
    val ItemCode: String,
    val OnHand: Double,
    val OnOrder: Double,
    val WhsCode: String,
    val available: Double = OnHand + OnOrder - IsCommited
) {
    constructor() : this(0.0, 0.0, "", 0.0, 0.0, "")
}

fun ArticuloCantidad.toDomain() = DoArticuloCantidades(
    ItemCode = ItemCode,
    WhsCode = WhsCode,
    OnHand = OnHand,
    OnOrder = OnOrder,
    IsCommited = IsCommited,
    AvgPrice = AvgPrice
)

fun ArticuloCantidadesEntity.toDomain() = DoArticuloCantidades(
    AvgPrice = AvgPrice,
    IsCommited = IsCommited,
    ItemCode = ItemCode,
    OnHand = OnHand,
    OnOrder = OnOrder,
    WhsCode = WhsCode
)

data class DoArticuloInfoBaseView(
    val Descripcion: String,
    val Stock: Double,
    val Comprometido: Double,
    val Solicitado: Double,
    val Disponible: Double,
    val Precio: Double,
)

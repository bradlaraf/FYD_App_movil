package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.ArticuloCantidad
import com.mobile.massiveapp.data.util.toNotNotationScientific
import com.mobile.massiveapp.domain.model.DoArticuloCantidades

@Entity(tableName = "ArticuloCantidad", primaryKeys = ["ItemCode", "WhsCode"])
data class ArticuloCantidadesEntity(
    @ColumnInfo(name = "ItemCode") val ItemCode: String,
    @ColumnInfo(name = "WhsCode") val WhsCode: String,
    @ColumnInfo(name = "OnHand") val OnHand: Double,
    @ColumnInfo(name = "OnOrder") val OnOrder: Double,
    @ColumnInfo(name = "IsCommited") val IsCommited: Double,
    @ColumnInfo(name = "AvgPrice") val AvgPrice: Double
)

fun DoArticuloCantidades.toDatabase() = ArticuloCantidadesEntity(
    ItemCode = ItemCode,
    WhsCode = WhsCode,
    OnHand = toNotNotationScientific(OnHand),
    OnOrder = OnOrder,
    IsCommited = IsCommited,
    AvgPrice = AvgPrice
)

fun ArticuloCantidad.toDatabase() = ArticuloCantidadesEntity(
    ItemCode = ItemCode,
    WhsCode = WhsCode,
    OnHand = OnHand,
    OnOrder = OnOrder,
    IsCommited = IsCommited,
    AvgPrice = AvgPrice
)



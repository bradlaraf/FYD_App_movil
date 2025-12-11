package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.TipoCambio
import com.mobile.massiveapp.domain.model.DoTipoCambio

@Entity(tableName = "TipoCambio")
data class TipoCambioEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "RateDate") val RateDate: String,
    @ColumnInfo(name = "Currency") val Currency: String,
    @ColumnInfo(name = "Rate") val Rate: Double
)

fun TipoCambio.toDatabase() = TipoCambioEntity(
    Code = Code,
    RateDate = RateDate,
    Currency = Currency,
    Rate = Rate
)

fun DoTipoCambio.toDatabase() = TipoCambioEntity(
    Code = Code,
    RateDate = RateDate,
    Currency = Currency,
    Rate = Rate
)

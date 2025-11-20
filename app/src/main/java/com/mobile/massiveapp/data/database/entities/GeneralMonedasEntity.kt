package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralMonedas
import com.mobile.massiveapp.domain.model.DoGeneralMonedas

@Entity(tableName = "Monedas")
data class GeneralMonedasEntity(
    @PrimaryKey
    @ColumnInfo(name = "CurrCode") val CurrCode: String,
    @ColumnInfo(name = "CurrName") val CurrName: String,
    @ColumnInfo(name = "ISOCurrCod") val ISOCurrCod: String
)

fun GeneralMonedas.toDatabase() = GeneralMonedasEntity(
    CurrCode = CurrCode,
    CurrName = CurrName,
    ISOCurrCod = ISOCurrCod
)

fun DoGeneralMonedas.toDatabase() = GeneralMonedasEntity(
    CurrCode = CurrCode,
    CurrName = CurrName,
    ISOCurrCod = ISOCurrCod
)
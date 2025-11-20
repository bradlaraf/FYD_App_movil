package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.GeneralMonedasEntity
import com.mobile.massiveapp.data.model.GeneralMonedas

data class DoGeneralMonedas(
    val CurrCode: String,
    val CurrName: String,
    val ISOCurrCod: String
)

fun GeneralMonedas.toDomain() = DoGeneralMonedas(
    CurrCode = CurrCode,
    CurrName = CurrName,
    ISOCurrCod = ISOCurrCod
)

fun GeneralMonedasEntity.toDomain() = DoGeneralMonedas(
    CurrCode = CurrCode,
    CurrName = CurrName,
    ISOCurrCod = ISOCurrCod
)

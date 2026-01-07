package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.LiquidacionPagoEntity

data class DoLiquidacionPago(
    val AccDocEntry: String,
    val Code: String,
    val Name: String,
    val DocEntry: Int,
    val DocLine: Int,
    val U_MSV_MA_LIQ: Int,
    val U_MSV_MA_MANIF: Int,
    val U_MSV_MA_OBJETO: Int,
    val U_MSV_MA_CLAVE: Int,
    val U_MSV_MA_FECHA: String,
    val U_MSV_MA_MEDIO: String,
    val U_MSV_MA_MON: String,
    val U_MSV_MA_IMP: Double,
    val U_MSV_MA_NROOPE: String,
    val U_MSV_MA_CTA: String,
    val U_MSV_MA_PAGO: Int,
    val EditableMovil: String,
)

fun LiquidacionPagoEntity.toDomain() = DoLiquidacionPago(
    AccDocEntry = AccDocEntry,
    Code = Code,
    Name = Name,
    DocEntry = DocEntry,
    U_MSV_MA_LIQ = U_MSV_MA_LIQ,
    U_MSV_MA_MANIF = U_MSV_MA_MANIF,
    U_MSV_MA_OBJETO = U_MSV_MA_OBJETO,
    U_MSV_MA_CLAVE = U_MSV_MA_CLAVE,
    U_MSV_MA_FECHA = U_MSV_MA_FECHA,
    U_MSV_MA_MEDIO = U_MSV_MA_MEDIO,
    U_MSV_MA_MON = U_MSV_MA_MON,
    U_MSV_MA_IMP = U_MSV_MA_IMP,
    U_MSV_MA_NROOPE = U_MSV_MA_NROOPE,
    U_MSV_MA_CTA = U_MSV_MA_CTA,
    U_MSV_MA_PAGO = U_MSV_MA_PAGO,
    EditableMovil = EditableMovil,
    DocLine = DocLine
)


data class DoLiquidacionPagoView(
    val DocLine: Int,
    val DocEntryFactura: Int,
    val SUNAT: String,
    val FechaCreacion: String,
    val Monto: Double,
    val Saldo: Double
)



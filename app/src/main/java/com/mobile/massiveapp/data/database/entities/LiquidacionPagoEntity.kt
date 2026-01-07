package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.LiquidacionPago
import com.mobile.massiveapp.domain.model.DoLiquidacionPago

@Entity(tableName = "LiquidacionPago")
data class LiquidacionPagoEntity (
    @PrimaryKey
    @ColumnInfo(name = "AccDocEntry") val AccDocEntry: String,
    @ColumnInfo(name = "Code")val Code: String, //AccDocEntry
    @ColumnInfo(name = "Name")val Name: String,  //AccDocEntry
    @ColumnInfo(name = "DocEntry")val DocEntry: Int, //-1
    @ColumnInfo(name = "DocLine")val DocLine: Int,
    @ColumnInfo(name = "U_MSV_MA_LIQ")val U_MSV_MA_LIQ: Int,
    @ColumnInfo(name = "U_MSV_MA_MANIF")val U_MSV_MA_MANIF: Int,
    @ColumnInfo(name = "U_MSV_MA_OBJETO")val U_MSV_MA_OBJETO: Int, //13
    @ColumnInfo(name = "U_MSV_MA_CLAVE")val U_MSV_MA_CLAVE: Int, //DocEntry Factura
    @ColumnInfo(name = "U_MSV_MA_FECHA")val U_MSV_MA_FECHA: String,
    @ColumnInfo(name = "U_MSV_MA_MEDIO")val U_MSV_MA_MEDIO: String, //Codigo de forma de pago
    @ColumnInfo(name = "U_MSV_MA_MON")val U_MSV_MA_MON: String, //Codigo moneda
    @ColumnInfo(name = "U_MSV_MA_IMP")val U_MSV_MA_IMP: Double,
    @ColumnInfo(name = "U_MSV_MA_NROOPE")val U_MSV_MA_NROOPE: String,
    @ColumnInfo(name = "U_MSV_MA_CTA")val U_MSV_MA_CTA: String, //Cuenta de Formas de pago
    @ColumnInfo(name = "U_MSV_MA_PAGO")val U_MSV_MA_PAGO: Int, //-1

    @ColumnInfo(name = "EditableMovil") val EditableMovil:String,
)

fun LiquidacionPago.toDatabase() = LiquidacionPagoEntity(
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
    EditableMovil = "N",
    DocLine = 0,
)

fun DoLiquidacionPago.toDatabase() = LiquidacionPagoEntity(
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
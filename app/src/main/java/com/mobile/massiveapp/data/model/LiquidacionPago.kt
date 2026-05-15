package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.LiquidacionPagoEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface


data class LiquidacionPago(
    val AccAction: String,
    val AccCreateDate: String,
    val AccCreateHour: String,
    val AccCreateUser: String,
    val AccError: String,
    val AccNotificado: String,
    var AccFinalized: String,
    val AccMigrated: String,
    val AccMovil: String,
    val Canceled: String,
    val AccControl: String,
    val AccUpdateDate: String,
    val AccUpdateHour: String,
    val AccUpdateUser: String,
    val AccDocEntry: String,
    val Code: String,
    val Name: String,
    val DocEntry: Int,
    val ObjType: Int,
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
):MappingInteface<LiquidacionPago>(){
    constructor():this(
        AccDocEntry = "",
        Code = "",
        Name = "",
        DocEntry = -1,
        Canceled = "",
        U_MSV_MA_LIQ = -1,
        U_MSV_MA_MANIF = -1,
        U_MSV_MA_OBJETO = -1,
        U_MSV_MA_CLAVE = -1,
        U_MSV_MA_FECHA = "",
        U_MSV_MA_MEDIO = "",
        U_MSV_MA_MON = "",
        U_MSV_MA_IMP = 0.0,
        ObjType = -1,
        U_MSV_MA_NROOPE = "",
        U_MSV_MA_CTA = "",
        U_MSV_MA_PAGO = -1,
        AccError = "",
        AccControl = "",
        AccAction = "",
        AccCreateDate = "",
        AccCreateHour = "",
        AccCreateUser = "",
        AccNotificado = "",
        AccFinalized = "",
        AccMigrated = "",
        AccMovil = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
    )
    override fun map(data: List<LiquidacionPago>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<LiquidacionPago>): List<Any> {
        return data.map { it.AccDocEntry }
    }

}

fun LiquidacionPagoEntity.toModel() = LiquidacionPago(
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
    ObjType = ObjType,
    AccAction = AccAction,
    Canceled = Canceled,
    AccControl = AccControl,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccError = AccError,
    AccNotificado = AccNotificado,
    AccFinalized = AccFinalized,
    AccMigrated = AccMigrated,
    AccMovil = AccMovil,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser,
)
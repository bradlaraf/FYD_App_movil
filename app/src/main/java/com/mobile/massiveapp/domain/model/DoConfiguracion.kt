package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ConfiguracionEntity

data class DoConfiguracion (
    val IpPublica: String,
    val IpLocal: String,
    val NumeroPuerto: String,
    val BaseDeDatos: String,
    val SincAutomatica: Boolean,
    var IMEI: String,
    val TimerServicio: Int,
    val TopArticulo: Int,
    val TopFactura: Int,
    val TopCliente: Int,
    val SetIpPublica: Boolean,
    val UsarLimites: Boolean,
    val UsarConfirmacion: Boolean,
    val LimiteLineasPedido: Int,
    val AppVersion: String
    )

{
    constructor() : this(
        IpPublica = "45.232.150.245",
        IpLocal= "192.168.1.3",
        NumeroPuerto = "82",
        BaseDeDatos = "Sociedad",
        SincAutomatica = false,
        IMEI = "",
        TimerServicio = 15,
        TopArticulo = 0,
        TopFactura = 0,
        TopCliente = 0,
        true,
        false,
        false,
        25,
        "V1.0.1"
    )

}

fun ConfiguracionEntity.toDomain() = DoConfiguracion(
    IpPublica = IpPublica,
    IpLocal = IpLocal,
    NumeroPuerto = NumeroPuerto,
    BaseDeDatos = BaseDeDatos,
    SincAutomatica = SincAutomatica,
    IMEI = IMEI,
    TimerServicio = TimerServicio,
    TopArticulo = TopArticulo,
    TopFactura = TopFactura,
    TopCliente = TopCliente,
    SetIpPublica = SetIpPublica,
    UsarLimites = UsarLimites,
    UsarConfirmacion = UsarConfirmacion,
    LimiteLineasPedido = LimiteLineasPedido,
    AppVersion = AppVersion
)
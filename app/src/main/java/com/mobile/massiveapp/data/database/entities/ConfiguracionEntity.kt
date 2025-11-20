package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.domain.model.DoConfiguracion


@Entity("Configuracion")
data class ConfiguracionEntity(
    @PrimaryKey
    @ColumnInfo("IpPublica") val IpPublica: String,
    @ColumnInfo("IpLocal") val IpLocal: String,
    @ColumnInfo("NumeroPuerto") val NumeroPuerto: String,
    @ColumnInfo("BaseDeDatos") val BaseDeDatos: String,
    @ColumnInfo("SincAutomatica") val SincAutomatica: Boolean,
    @ColumnInfo("IMEI") val IMEI: String,
    @ColumnInfo("TimerServicio") val TimerServicio: Int,
    @ColumnInfo("UsarLimites") val UsarLimites: Boolean,
    @ColumnInfo("TopArticulo") val TopArticulo: Int,
    @ColumnInfo("TopFactura") val TopFactura: Int,
    @ColumnInfo("TopCliente") val TopCliente: Int,
    @ColumnInfo("SetIpPublica") val SetIpPublica: Boolean,
    @ColumnInfo("UsarConfirmacion") val UsarConfirmacion: Boolean,
    @ColumnInfo("LimiteLineasPedido") val LimiteLineasPedido: Int,
    @ColumnInfo("AppVersion") val AppVersion: String
)

fun DoConfiguracion.toDatabase() = ConfiguracionEntity(
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



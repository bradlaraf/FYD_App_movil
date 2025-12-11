package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Camioneta
import com.mobile.massiveapp.domain.model.DoCamioneta

@Entity(tableName = "Camioneta")
data class CamionetaEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String,
    @ColumnInfo(name = "U_MSV_MA_PLACA") val U_MSV_MA_PLACA: String,
    @ColumnInfo(name = "U_MSV_MA_MARCA") val U_MSV_MA_MARCA: String,
    @ColumnInfo(name = "U_MSV_MA_MODELO") val U_MSV_MA_MODELO: String,
    @ColumnInfo(name = "U_MSV_MA_TOLVA") val U_MSV_MA_TOLVA: String,
    @ColumnInfo(name = "U_MSV_MA_ANIOFAB") val U_MSV_MA_ANIOFAB: Int
)

fun Camioneta.toDatabase() = CamionetaEntity (
    Code = Code,
    Name = Name,
    U_MSV_MA_PLACA = U_MSV_MA_PLACA,
    U_MSV_MA_MARCA = U_MSV_MA_MARCA,
    U_MSV_MA_MODELO = U_MSV_MA_MODELO,
    U_MSV_MA_TOLVA = U_MSV_MA_TOLVA,
    U_MSV_MA_ANIOFAB = U_MSV_MA_ANIOFAB
)

fun DoCamioneta.toDatabase() = CamionetaEntity (
    Code = Code,
    Name = Name,
    U_MSV_MA_PLACA = U_MSV_MA_PLACA,
    U_MSV_MA_MARCA = U_MSV_MA_MARCA,
    U_MSV_MA_MODELO = U_MSV_MA_MODELO,
    U_MSV_MA_TOLVA = U_MSV_MA_TOLVA,
    U_MSV_MA_ANIOFAB = U_MSV_MA_ANIOFAB
)

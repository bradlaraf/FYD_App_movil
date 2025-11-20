package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralCondiciones

@Entity(tableName = "CondicionPago")
data class GeneralCondicionesEntity(
    @PrimaryKey
    @ColumnInfo(name = "GroupNum") val GroupNum: Int,
    @ColumnInfo(name = "PymntGroup") val PymntGroup: String,
    @ColumnInfo(name = "ExtraDays") val ExtraDays: Int,
    @ColumnInfo(name = "ExtraMonth") val ExtraMonth: Int
)

fun GeneralCondiciones.toDatabase() = GeneralCondicionesEntity(
    GroupNum = GroupNum,
    PymntGroup = PymntGroup,
    ExtraDays = ExtraDays,
    ExtraMonth = ExtraMonth
)

fun GeneralCondicionesEntity.toModel() = GeneralCondiciones(
    GroupNum = GroupNum,
    PymntGroup = PymntGroup,
    ExtraDays = ExtraDays,
    ExtraMonth = ExtraMonth
)
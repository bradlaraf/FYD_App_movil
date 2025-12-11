package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GrupoDescuentoDetalle
import com.mobile.massiveapp.domain.model.DoGrupoDescuentoDetalle

@Entity(tableName = "GrupoDescuentoDetalle")
data class GrupoDescuentoDetalleEntity(
    @PrimaryKey
    @ColumnInfo(name = "AbsEntry") val AbsEntry: Int,
    @ColumnInfo(name = "Obj") val Obj: String,
    @ColumnInfo(name = "ObjKey") val ObjKey: String,
    @ColumnInfo(name = "DiscType") val DiscRef: String,
    @ColumnInfo(name = "Discount") val Discount: Double,
    @ColumnInfo(name = "PayFor") val PayFor: Double,
    @ColumnInfo(name = "ForFree") val ForFree: Double,
    @ColumnInfo(name = "UpTo") val UpTo: Double
)

fun GrupoDescuentoDetalle.toDatabase() = GrupoDescuentoDetalleEntity(
    AbsEntry = AbsEntry,
    Obj = Obj,
    ObjKey = ObjKey,
    DiscRef = DiscRef,
    Discount = Discount,
    PayFor = PayFor,
    ForFree = ForFree,
    UpTo = UpTo
)

fun DoGrupoDescuentoDetalle.toDatabase() = GrupoDescuentoDetalleEntity(
    AbsEntry = AbsEntry,
    Obj = Obj,
    ObjKey = ObjKey,
    DiscRef = DiscRef,
    Discount = Discount,
    PayFor = PayFor,
    ForFree = ForFree,
    UpTo = UpTo
)
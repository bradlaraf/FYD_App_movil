package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GrupoDescuento
import com.mobile.massiveapp.domain.model.DoGrupoDescuento

@Entity(tableName = "GrupoDescuento")
data class GrupoDescuentoEntity(
    @PrimaryKey
    @ColumnInfo(name = "AbsEntry") val AbsEntry: Int,
    @ColumnInfo(name = "Type") val Type: String,
    @ColumnInfo(name = "Obj") val Obj: String,
    @ColumnInfo(name = "ObjCode") val ObjCode: String,
    @ColumnInfo(name = "DiscRef") val DiscRef: String,
    @ColumnInfo(name = "ValidFor") val ValidFor: String,
    @ColumnInfo(name = "ValidForm") val ValidForm: String,
    @ColumnInfo(name = "ValidTo") val ValidTo: String
)

fun GrupoDescuento.toDatabase() = GrupoDescuentoEntity(
    AbsEntry = AbsEntry,
    Type = Type,
    Obj = Obj,
    ObjCode = ObjCode,
    DiscRef = DiscRef,
    ValidFor = ValidFor,
    ValidForm = ValidForm,
    ValidTo = ValidTo
)

fun DoGrupoDescuento.toDatabase() = GrupoDescuentoEntity(
    AbsEntry = AbsEntry,
    Type = Type,
    Obj = Obj,
    ObjCode = ObjCode,
    DiscRef = DiscRef,
    ValidFor = ValidFor,
    ValidForm = ValidForm,
    ValidTo = ValidTo
)
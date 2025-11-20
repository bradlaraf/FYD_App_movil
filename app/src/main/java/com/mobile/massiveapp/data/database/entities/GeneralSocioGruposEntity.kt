package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralSocioGrupos

@Entity(tableName = "GrupoSocio")
data class GeneralSocioGruposEntity(
    @PrimaryKey
    @ColumnInfo(name = "GroupCode") val GroupCode: String,
    @ColumnInfo(name = "GroupName") val GroupName: String,
    @ColumnInfo(name = "GroupType") val GroupType: String
 )

fun GeneralSocioGrupos.toDatabase() = GeneralSocioGruposEntity(
    GroupCode = GroupCode,
    GroupName = GroupName,
    GroupType = GroupType
)

fun GeneralSocioGruposEntity.toModel() = GeneralSocioGrupos(
    GroupCode = GroupCode,
    GroupName = GroupName,
    GroupType = GroupType
)
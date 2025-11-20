package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.SocioGrupos
import com.mobile.massiveapp.domain.model.DoSocioGrupos

@Entity(tableName = "SocioGrupos")
data class SocioGruposEntity (
    @PrimaryKey
    @ColumnInfo(name = "GroupCode") val GroupCode: Int,
    @ColumnInfo(name = "GroupName") val GroupName: String,
    @ColumnInfo(name = "GroupType") val GroupType: String
    )

fun DoSocioGrupos.toDatabase() = SocioGruposEntity(
    GroupCode = GroupCode,
    GroupName = GroupName,
    GroupType = GroupType
)

fun SocioGrupos.toDatabase() = SocioGruposEntity(
    GroupCode = GroupCode,
    GroupName = GroupName,
    GroupType = GroupType
)

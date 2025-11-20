package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.SocioGruposEntity
import com.mobile.massiveapp.data.model.SocioGrupos

data class DoSocioGrupos(
    val GroupCode: Int,
    val GroupName: String,
    val GroupType: String
)

fun SocioGrupos.toDomain() = DoSocioGrupos(
    GroupCode = GroupCode,
    GroupName = GroupName,
    GroupType = GroupType
)
fun SocioGruposEntity.toDomain() = DoSocioGrupos (
    GroupCode = GroupCode,
    GroupName = GroupName,
    GroupType = GroupType
)
package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.UsuarioAlmacenesEntity
import com.mobile.massiveapp.data.database.entities.UsuarioGrupoArticulosEntity
import com.mobile.massiveapp.data.database.entities.UsuarioGrupoSociosEntity
import com.mobile.massiveapp.data.database.entities.UsuarioListaPreciosEntity
import com.mobile.massiveapp.data.database.entities.UsuarioZonasEntity
import com.mobile.massiveapp.data.model.UsuarioAlmacenes
import com.mobile.massiveapp.data.model.UsuarioGrupoArticulos
import com.mobile.massiveapp.data.model.UsuarioGrupoSocios
import com.mobile.massiveapp.data.model.UsuarioListaPrecios
import com.mobile.massiveapp.data.model.UsuarioZonas
import com.mobile.massiveapp.domain.getRadmonNumber
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import com.mobile.massiveapp.ui.view.util.getStringForAccLocked

data class DoUsuarioZonas (
    val Code: String,
    val Name: String
)

data class DoNuevoUsuarioItem (
    val Code: String,
    val Name: String,
    var Checked: Boolean
)

fun UsuarioAlmacenesEntity.updateDataToSend(
    userCode: String,
    userInfo: DoNuevoUsuarioItem
) = UsuarioAlmacenesEntity(
    Code = userCode,
    LineNum = LineNum,
    TypeW = TypeW,
    WhsCode = userInfo.Code,
    AccAction = "I",
    AccControl = AccControl,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = AccCreateUser,
    AccLocked = userInfo.Checked.getStringForAccLocked(),
    AccMigrated = "N",
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser
)

fun UsuarioListaPreciosEntity.updateDataToSend(
    userCode: String,
    userInfo: DoNuevoUsuarioItem
) = UsuarioListaPreciosEntity(
    Code = userCode,
    LineNum = LineNum,
    ListNum = userInfo.Code.toIntOrNull()?:-1,
    AccAction = "I",
    AccControl = AccControl,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = AccCreateUser,
    AccLocked = userInfo.Checked.getStringForAccLocked(),
    AccMigrated = "N",
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser
)

fun UsuarioGrupoArticulosEntity.updateDataToSend(
    userCode: String,
    userInfo: DoNuevoUsuarioItem
) = UsuarioGrupoArticulosEntity(
    Code = userCode,
    ItmsGrpCod = userInfo.Code.toIntOrNull()?:-1,
    LineNum = LineNum,
    AccAction = "I",
    AccControl = AccControl,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = AccCreateUser,
    AccLocked = userInfo.Checked.getStringForAccLocked(),
    AccMigrated = "N",
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser
)

fun UsuarioGrupoSociosEntity.updateDataToSend(
    userCode: String,
    userInfo: DoNuevoUsuarioItem
) = UsuarioGrupoSociosEntity(
    Code = userCode,
    GroupCode = userInfo.Code.toIntOrNull()?:-1,
    LineNum = LineNum,
    AccAction = "I",
    AccControl = AccControl,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = AccCreateUser,
    AccLocked = userInfo.Checked.getStringForAccLocked(),
    AccMigrated = "N",
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser
)

fun UsuarioZonasEntity.updateDataToSend(
    userCode: String,
    userInfo: DoNuevoUsuarioItem
) = UsuarioZonasEntity(
    Code = userCode,
    CodeZona = userInfo.Code,
    LineNum = LineNum,
    AccAction = "I",
    AccControl = AccControl,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = AccCreateUser,
    AccLocked = userInfo.Checked.getStringForAccLocked(),
    AccMigrated = "N",
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser
)

/*******/

val ACC_CRONTROL_DEFAULT = "N"
val ACC_MIGRATED_DEFAULT = "N"
fun DoNuevoUsuarioItem.toModelAlmacenes(userCode: String) = UsuarioAlmacenes(
    Code = userCode,
    LineNum = getRadmonNumber(),
    TypeW = "N",
    WhsCode = Code,
    AccAction = "I",
    AccControl = ACC_CRONTROL_DEFAULT,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = "",
    AccLocked = Checked.getStringForAccLocked(),
    AccMigrated = ACC_MIGRATED_DEFAULT,
    AccUpdateDate = "",
    AccUpdateHour = "",
    AccUpdateUser = ""
)

fun DoNuevoUsuarioItem.toModelListaPrecios(userCode: String) = UsuarioListaPrecios(
    Code = userCode,
    LineNum = getRadmonNumber(),
    ListNum = Code.toIntOrNull()?:-1,
    AccAction = "I",
    AccControl = ACC_CRONTROL_DEFAULT,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = "",
    AccLocked = Checked.getStringForAccLocked(),
    AccMigrated = ACC_MIGRATED_DEFAULT,
    AccUpdateDate = "",
    AccUpdateHour = "",
    AccUpdateUser = ""
)

fun DoNuevoUsuarioItem.toModelGrupoArticulos(userCode: String) = UsuarioGrupoArticulos(
    Code = userCode,
    ItmsGrpCod = Code.toIntOrNull()?:-1,
    LineNum = getRadmonNumber(),
    AccAction = "I",
    AccControl = ACC_CRONTROL_DEFAULT,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = "",
    AccLocked = Checked.getStringForAccLocked(),
    AccMigrated = ACC_MIGRATED_DEFAULT,
    AccUpdateDate = "",
    AccUpdateHour = "",
    AccUpdateUser = ""
)

fun DoNuevoUsuarioItem.toModelGrupoSocios(userCode: String) = UsuarioGrupoSocios(
    Code = userCode,
    GroupCode = Code.toIntOrNull()?:-1,
    LineNum = getRadmonNumber(),
    AccAction = "I",
    AccControl = ACC_CRONTROL_DEFAULT,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = "",
    AccLocked = Checked.getStringForAccLocked(),
    AccMigrated = ACC_MIGRATED_DEFAULT,
    AccUpdateDate = "",
    AccUpdateHour = "",
    AccUpdateUser = ""
)

fun DoNuevoUsuarioItem.toModelZonas(userCode: String) = UsuarioZonas(
    Code = userCode,
    CodeZona = Code,
    LineNum = getRadmonNumber(),
    AccAction = "I",
    AccControl = ACC_CRONTROL_DEFAULT,
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = "",
    AccLocked = Checked.getStringForAccLocked(),
    AccMigrated = ACC_MIGRATED_DEFAULT,
    AccUpdateDate = "",
    AccUpdateHour = "",
    AccUpdateUser = ""
)
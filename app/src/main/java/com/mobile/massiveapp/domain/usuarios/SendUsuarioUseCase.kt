package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ConfigurarUsuarioDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.dao.UsuarioAlmacenesDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoArticuloDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoSociosDao
import com.mobile.massiveapp.data.database.dao.UsuarioListaPreciosDao
import com.mobile.massiveapp.data.database.dao.UsuarioZonasDao
import com.mobile.massiveapp.data.database.entities.ConfigurarUsuariosEntity
import com.mobile.massiveapp.data.database.entities.toModel
import com.mobile.massiveapp.data.model.UsuarioAlmacenes
import com.mobile.massiveapp.data.model.UsuarioGrupoArticulos
import com.mobile.massiveapp.data.model.UsuarioGrupoSocios
import com.mobile.massiveapp.data.model.UsuarioListaPrecios
import com.mobile.massiveapp.data.model.UsuarioToSend
import com.mobile.massiveapp.data.model.UsuarioZonas
import com.mobile.massiveapp.data.model.toSendModel
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.UsuarioRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.domain.model.toModelAlmacenes
import com.mobile.massiveapp.domain.model.toModelGrupoArticulos
import com.mobile.massiveapp.domain.model.toModelGrupoSocios
import com.mobile.massiveapp.domain.model.toModelListaPrecios
import com.mobile.massiveapp.domain.model.toModelZonas
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import javax.inject.Inject

class SendUsuarioUseCase @Inject constructor(
    private val configurarUsuarioDao: ConfigurarUsuarioDao,
    private val usuarioRepository: UsuarioRepository,

    private val usuarioAlmacenesDao: UsuarioAlmacenesDao,
    private val usuarioListaPreciosDao: UsuarioListaPreciosDao,
    private val usuarioZonasDao: UsuarioZonasDao,
    private val usuarioGrupoSociosDao: UsuarioGrupoSociosDao,
    private val usuarioGrupoArticulosDao: UsuarioGrupoArticuloDao,

    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,
    private val errorLogDao: ErrorLogDao,
    private val datosMaestrosService: DatosMaestrosService,
    private val datosMaestrosRepository: DatosMaestrosRepository
) {
    var mensaje = "Socios Sincronizados"
    var errorCodigo = 0
    suspend operator fun invoke(listaInfoUsuario: HashMap<String, List<DoNuevoUsuarioItem>>, usuarioCabecera: DoConfigurarUsuario, accion: String): DoError{
        return try {
            val configuracion = configuracionRepository.getConfiguracion()
            val usuario = loginRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)
            val codeUser = usuarioCabecera.Code
            val timeOut = 60L

            val estadoSesion = datosMaestrosService.getEstadoSesion(
                usuario,
                configuracion,
                url,
                10L
            )

            //Verificar Estado de la sesion
            when(estadoSesion) {
                is String ->{
                    if (estadoSesion == "N"){
                        mensaje = "Su sesión esta cerrada"
                        errorCodigo = ManagerInputData.SESION_CERRADA
                        throw Exception(mensaje)
                    }
                }
                is DoError ->{
                    mensaje = estadoSesion.ErrorMensaje
                    errorCodigo = estadoSesion.ErrorCodigo
                    errorLogDao.insert(getError(errorCodigo.toString(), mensaje))
                    throw Exception(mensaje)
                }
            }


            val usuarioCabeceraModel = getUsuarioCabecera(usuarioCabecera, accion).toModel()

            var almacenesSend: List<UsuarioAlmacenes> = emptyList()
            var listasPrecioSend: List<UsuarioListaPrecios> = emptyList()
            var zonasSend: List<UsuarioZonas> = emptyList()
            var grupoSociosSend: List<UsuarioGrupoSocios> = emptyList()
            var grupoArticulosSend: List<UsuarioGrupoArticulos> = emptyList()

            if (accion == "U"){
                usuarioCabeceraModel.AccStatusSession = "N"
                usuarioRepository.updateInfoUsuario(listaInfoUsuario, codeUser)

                almacenesSend = usuarioAlmacenesDao.getAlmacenes(codeUser).map { it.toModel() }
                listasPrecioSend = usuarioListaPreciosDao.getListasPrecio(codeUser).map { it.toModel() }
                zonasSend = usuarioZonasDao.getZonas(codeUser).map { it.toModel() }
                grupoSociosSend = usuarioGrupoSociosDao.getGrupoSocios(codeUser).map { it.toModel() }
                grupoArticulosSend = usuarioGrupoArticulosDao.getGrupoArticulos(codeUser).map { it.toModel() }

            } else {
                //Insert
                almacenesSend = listaInfoUsuario["Almacenes"]!!.map { it.toModelAlmacenes(codeUser) }
                listasPrecioSend = listaInfoUsuario["ListaPrecios"]!!.map { it.toModelListaPrecios(codeUser) }
                zonasSend = listaInfoUsuario["Zonas"]!!.map { it.toModelZonas(codeUser) }
                grupoSociosSend = listaInfoUsuario["GrupoSocios"]!!.map { it.toModelGrupoSocios(codeUser) }
                grupoArticulosSend = listaInfoUsuario["GrupoArticulos"]!!.map { it.toModelGrupoArticulos(codeUser) }
            }



            val usuarioToSend = usuarioCabeceraModel.toSendModel(
                Almacenes = almacenesSend,
                GrupoArticulos = grupoArticulosSend,
                GrupoSocios = grupoSociosSend,
                ListaPrecios = listasPrecioSend,
                Zonas = zonasSend
            )



            val sendUsuario = datosMaestrosRepository.sendUsuario(
                listOf(usuarioToSend),
                configuracion,
                usuario,
                url,
                timeOut
            )

            mensaje = when (sendUsuario){
                is List<*> -> {
                    var mensajeIfAccError = "Registro de usuario exitoso"

                    val accError =
                        try {
                            (sendUsuario.first() as UsuarioToSend).AccError
                        } catch (e:Exception){
                            ""
                        }
                    if (accError.isNotEmpty()){
                        errorLogDao.insert(getError(
                            code = "Insertar Usuario",
                            message = accError
                        ))
                        mensajeIfAccError = "Envío fallido"
                    }
                    mensajeIfAccError
                }

                is DoError -> {
                    errorCodigo = sendUsuario.ErrorCodigo
                    sendUsuario.ErrorMensaje
                }
                else -> { "Sin conexión" }
            }

            val listaUsuario = sendUsuario as? List<UsuarioToSend>?: emptyList()

            if (listaUsuario.isNotEmpty()){
                usuarioRepository.insertUser(listaUsuario.first())
                val accError = listaUsuario.firstOrNull()?.AccError?:""
                if (accError.isNotEmpty()){
                    mensaje = accError
                }
            }

            DoError(
                mensaje,
                errorCodigo
            )
        } catch (e:Exception){
            e.printStackTrace()
            DoError(e.message.toString(), errorCodigo)
        }




    }

    private suspend fun getUsuarioCabecera(cabecera: DoConfigurarUsuario, accion: String):ConfigurarUsuariosEntity {
        val usuario = loginRepository.getUsuarioFromDatabase()
        return if (accion == "I"){
             ConfigurarUsuariosEntity(
                Code = cabecera.Code,
                AccAction = accion,
                AccControl = "N",
                AccStatusSession = "N",
                AccCreateDate = getFechaActual(),
                AccCreateHour = getHoraActual(),
                AccCreateUser = usuario.Code,
                AccLocked = cabecera.AccLocked,
                AccMigrated = "N",
                AccUpdateDate = "",
                AccUpdateHour = "",
                AccUpdateUser = "",
                CanApprove = cabecera.CanApprove,
                CanCreate = cabecera.CanCreate,
                CanDecline = cabecera.CanDecline,
                CanEditPrice = cabecera.CanEditPrice,
                CanUpdate = cabecera.CanUpdate,
                Comment = "",
                DefaultAcctCodeCh = cabecera.DefaultAcctCodeCh,
                DefaultAcctCodeDe = cabecera.DefaultAcctCodeDe,
                DefaultAcctCodeEf = cabecera.DefaultAcctCodeEf,
                DefaultAcctCodeTr = cabecera.DefaultAcctCodeTr,
                DefaultCC1 = "",
                DefaultCC2 = "",
                DefaultCC3 = "",
                DefaultCC4 = "",
                DefaultCC5 = "",
                DefaultCurrency = cabecera.DefaultCurrency,
                DefaultEmpId = -1,
                DefaultOrderSeries = cabecera.DefaultOrderSeries.toIntOrNull()?:-1,
                DefaultPagoRSeries = cabecera.DefaultPagoRSeries.toIntOrNull()?:-1,
                DefaultPriceList = cabecera.DefaultPriceList.toIntOrNull()?:-1,
                DefaultProyecto = cabecera.DefaultProyecto,
                DefaultSNSerieCli = cabecera.DefaultSNSerieCli.toIntOrNull()?:-1,
                DefaultSlpCode = cabecera.DefaultSlpCode.toIntOrNull()?:-1,
                DefaultTaxCode = cabecera.DefaultTaxCode,
                DefaultWarehouse = cabecera.DefaultWarehouse,
                DefaultConductor = cabecera.DefaultConductor,
                 DefaultZona = cabecera.DefaultZona,
                Email = cabecera.Email,
                IdPhone1 = cabecera.IdPhone1,
                IdPhone1Val = "Y",
                Image = "",
                Name = cabecera.Name,
                ObjType = -64001,
                Password = cabecera.Password,
                Phone1 = cabecera.Phone1,
                ResetIdPhone = "Y",
                ShowImage = "N",
                SuperUser = cabecera.SuperUser,
                 AccError = "",
                 AccMovil = "Y",
                 AccFinalized = "Y",
            )
        } else {
            val usuarioBD = configurarUsuarioDao.getUser(cabecera.Code)
            val user =
                ConfigurarUsuariosEntity(
                    Code = cabecera.Code.ifEmpty { usuarioBD.Code },
                    AccAction = accion,
                    AccControl = "N",
                    AccCreateDate = usuarioBD.AccCreateDate,
                    AccCreateHour = usuarioBD.AccCreateHour,
                    AccCreateUser = usuarioBD.AccCreateUser,
                    AccLocked = cabecera.AccLocked,
                    AccMigrated = "N",
                    AccStatusSession = cabecera.AccStatusSession,
                    AccUpdateDate = getFechaActual(),
                    AccUpdateHour = getHoraActual(),
                    AccUpdateUser = usuario.Code,
                    CanApprove = cabecera.CanApprove.ifEmpty { usuarioBD.CanApprove },
                    CanCreate = cabecera.CanCreate.ifEmpty { usuarioBD.CanCreate },
                    CanDecline = cabecera.CanDecline.ifEmpty { usuarioBD.CanDecline },
                    CanEditPrice = cabecera.CanEditPrice.ifEmpty { usuarioBD.CanEditPrice },
                    CanUpdate = cabecera.CanUpdate.ifEmpty { usuarioBD.CanUpdate },
                    Comment = "",
                    DefaultAcctCodeCh = cabecera.DefaultAcctCodeCh.ifEmpty { usuarioBD.DefaultAcctCodeCh },
                    DefaultAcctCodeDe = cabecera.DefaultAcctCodeDe.ifEmpty { usuarioBD.DefaultAcctCodeDe },
                    DefaultAcctCodeEf = cabecera.DefaultAcctCodeEf.ifEmpty { usuarioBD.DefaultAcctCodeEf },
                    DefaultAcctCodeTr = cabecera.DefaultAcctCodeTr.ifEmpty { usuarioBD.DefaultAcctCodeTr },
                    DefaultCC1 = "",
                    DefaultCC2 = "",
                    DefaultCC3 = "",
                    DefaultCC4 = "",
                    DefaultCC5 = "",
                    DefaultCurrency = cabecera.DefaultCurrency.ifEmpty { usuarioBD.DefaultCurrency },
                    DefaultEmpId = -1,
                    DefaultOrderSeries = cabecera.DefaultOrderSeries.ifEmpty { usuarioBD.DefaultOrderSeries.toString() }.toIntOrNull()?:-1,
                    DefaultPagoRSeries = cabecera.DefaultPagoRSeries.ifEmpty { usuarioBD.DefaultPagoRSeries.toString() }.toIntOrNull()?:-1,
                    DefaultPriceList = cabecera.DefaultPriceList.ifEmpty { usuarioBD.DefaultPriceList.toString() }.toIntOrNull()?:-1,
                    DefaultProyecto = cabecera.DefaultProyecto,
                    DefaultSNSerieCli = cabecera.DefaultSNSerieCli.ifEmpty { usuarioBD.DefaultSNSerieCli.toString() }.toIntOrNull()?:-1,
                    DefaultSlpCode = cabecera.DefaultSlpCode.ifEmpty { usuarioBD.DefaultSlpCode.toString() }.toIntOrNull()?:-1,
                    DefaultTaxCode = cabecera.DefaultTaxCode.ifEmpty { usuarioBD.DefaultTaxCode },
                    DefaultWarehouse = cabecera.DefaultWarehouse.ifEmpty { usuarioBD.DefaultWarehouse },
                    DefaultConductor = cabecera.DefaultConductor.ifEmpty { usuarioBD.DefaultConductor },
                    DefaultZona = cabecera.DefaultZona.ifEmpty { usuarioBD.DefaultZona },
                    Email = cabecera.Email.ifEmpty { usuarioBD.Email },
                    IdPhone1 = cabecera.IdPhone1.ifEmpty { usuarioBD.IdPhone1 },
                    IdPhone1Val = "Y",
                    Image = "",
                    Name = cabecera.Name.ifEmpty { usuarioBD.Name },
                    ObjType = -64001,
                    Password = cabecera.Password.ifEmpty { usuarioBD.Password },
                    Phone1 = cabecera.Phone1.ifEmpty { usuarioBD.Phone1 },
                    ResetIdPhone = "Y",
                    ShowImage = "N",
                    SuperUser = cabecera.SuperUser.ifEmpty { usuarioBD.SuperUser },
                    AccError = usuarioBD.AccError,
                    AccMovil = usuarioBD.AccMovil,
                    AccFinalized = usuarioBD.AccFinalized,
                )

            return user
        }

    }
}
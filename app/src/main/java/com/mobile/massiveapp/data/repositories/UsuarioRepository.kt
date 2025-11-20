package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.ConfigurarUsuarioDao
import com.mobile.massiveapp.data.database.dao.UsuarioAlmacenesDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoArticuloDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoSociosDao
import com.mobile.massiveapp.data.database.dao.UsuarioListaPreciosDao
import com.mobile.massiveapp.data.database.dao.UsuarioZonasDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.database.entities.toModel
import com.mobile.massiveapp.data.database.entities.toSendModel
import com.mobile.massiveapp.data.model.UsuarioToSend
import com.mobile.massiveapp.data.model.toDatabase
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.ui.view.util.getStringForAccLocked
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val configurarUsuarioDao: ConfigurarUsuarioDao,

    private val datosMaestrosService: DatosMaestrosService,

    private val usuarioAlmacenesDao: UsuarioAlmacenesDao,
    private val usuarioListaPreciosDao: UsuarioListaPreciosDao,
    private val usuarioZonasDao: UsuarioZonasDao,
    private val usuarioGrupoSociosDao: UsuarioGrupoSociosDao,
    private val usuarioGrupoArticulosDao: UsuarioGrupoArticuloDao
) {
    suspend fun getUsuarioToSend(
        userCode: String
    ): UsuarioToSend =
        try {
            val usuario = configurarUsuarioDao.getUser(userCode).toSendModel(
                zonas = usuarioZonasDao.getZonas(userCode).map { it.toModel() },
                almacenes = usuarioAlmacenesDao.getAlmacenes(userCode).map { it.toModel() },
                grupoArticulos = usuarioGrupoArticulosDao.getGrupoArticulos(userCode).map { it.toModel() },
                grupoSocios = usuarioGrupoSociosDao.getGrupoSocios(userCode).map { it.toModel() },
                listaPrecios = usuarioListaPreciosDao.getListasPrecio(userCode).map { it.toModel() }
            )
            usuario
        } catch (e:Exception){
            e.printStackTrace()
            UsuarioToSend()
        }

    suspend fun getUsuarioToSendSinDatos(
        userCode: String
    ): UsuarioToSend =
        try {
            val usuario = configurarUsuarioDao.getUser(userCode).toSendModel(
                zonas = emptyList(),
                almacenes = emptyList(),
                grupoArticulos = emptyList(),
                grupoSocios = emptyList(),
                listaPrecios = emptyList()
            )
            usuario
        } catch (e:Exception){
            e.printStackTrace()
            UsuarioToSend()
        }
    suspend fun getAllUsuariosToSend(): List<UsuarioToSend> =
        try {
            val listaCabeceras = configurarUsuarioDao.getAll()
            val usuariosToSend = listaCabeceras.map { usuarioCabecera->
                usuarioCabecera.toSendModel(
                    zonas = usuarioZonasDao.getZonas(usuarioCabecera.Code).map { it.toModel() },
                    almacenes = usuarioAlmacenesDao.getAlmacenes(usuarioCabecera.Code).map { it.toModel() },
                    grupoArticulos = usuarioGrupoArticulosDao.getGrupoArticulos(usuarioCabecera.Code).map { it.toModel() },
                    grupoSocios = usuarioGrupoSociosDao.getGrupoSocios(usuarioCabecera.Code).map { it.toModel() },
                    listaPrecios = usuarioListaPreciosDao.getListasPrecio(usuarioCabecera.Code).map { it.toModel() }
                )
            }
            usuariosToSend
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }


    suspend fun insertUser(
        usuario: UsuarioToSend
    ): Boolean =
        try {
            configurarUsuarioDao.insert(usuario.toDatabase())
            usuarioAlmacenesDao.insertAll(usuario.Almacenes.map { it.toDatabase() })
            usuarioZonasDao.insertAll(usuario.Zonas.map { it.toDatabase() })
            usuarioGrupoSociosDao.insertAll(usuario.GrupoSocios.map { it.toDatabase() })
            usuarioGrupoArticulosDao.insertAll(usuario.GrupoArticulos.map { it.toDatabase() })
            usuarioListaPreciosDao.insertAll(usuario.ListaPrecios.map { it.toDatabase() })
            true
        } catch (e:Exception){
            false
        }

    suspend fun inserAlltUsers(
        usuarios: List<UsuarioToSend>
    ): Boolean =
        try {
            usuarios.forEach { usuario->
                configurarUsuarioDao.insert(usuario.toDatabase())
                usuarioAlmacenesDao.insertAll(usuario.Almacenes.map { it.toDatabase() })
                usuarioZonasDao.insertAll(usuario.Zonas.map { it.toDatabase() })
                usuarioGrupoSociosDao.insertAll(usuario.GrupoSocios.map { it.toDatabase() })
                usuarioGrupoArticulosDao.insertAll(usuario.GrupoArticulos.map { it.toDatabase() })
                usuarioListaPreciosDao.insertAll(usuario.ListaPrecios.map { it.toDatabase() })
            }
            true
        } catch (e:Exception){
            false
        }

    suspend fun updateInfoUsuario(
        listaInfoUsuario: HashMap<String, List<DoNuevoUsuarioItem>>,
        userCode: String
    ){
        listaInfoUsuario["GrupoArticulos"].let { data->
            data?.forEach { grupoarticulo->
                usuarioGrupoArticulosDao.updateAccLocked(grupoarticulo.Checked.getStringForAccLocked(), grupoarticulo.Code, userCode)
            }
        }
        listaInfoUsuario["GrupoSocios"].let {data->
            data?.forEach { gruposocio->
                usuarioGrupoSociosDao.updateAccLocked(gruposocio.Checked.getStringForAccLocked(), gruposocio.Code, userCode)
            }
        }
        listaInfoUsuario["ListaPrecios"].let {data->
            data?.forEach { listaprecio->
                usuarioListaPreciosDao.updateAccLocked(listaprecio.Checked.getStringForAccLocked(), listaprecio.Code, userCode)
            }
        }
        listaInfoUsuario["Almacenes"].let {data->
            data?.forEach { almacen->
                usuarioAlmacenesDao.updateAccLocked(almacen.Checked.getStringForAccLocked(), almacen.Code, userCode)
            }
        }
        listaInfoUsuario["Zonas"].let {data->
            data?.forEach { zona->
                usuarioZonasDao.updateAccLocked(zona.Checked.getStringForAccLocked(), zona.Code, userCode)
            }
        }

    }
}
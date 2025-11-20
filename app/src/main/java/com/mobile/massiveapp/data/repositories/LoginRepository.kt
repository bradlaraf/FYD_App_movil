package com.mobile.massiveapp.data.repositories
import android.content.Context
import com.mobile.massiveapp.data.database.dao.UsuarioDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.model.Usuario
import com.mobile.massiveapp.data.network.LoginService
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject
class LoginRepository @Inject constructor(
    private val loginService: LoginService,
    private val usuarioDao: UsuarioDao
) {

    //Hacer el Login
    suspend fun login(
        version: String,
        usuario:String,
        password:String,
        context: Context,
        configuracion: DoConfiguracion,
        url: String) =
        try {
            loginService.login(version, usuario, password, context, configuracion, url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    //Guarda el usuario en al BD
    suspend fun saveUsuarioInDatabase(usuario: Usuario) =
        try {
            usuarioDao.insert(usuario.toDatabase())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    //Se obtienen los datos del usuario
    suspend fun getUsuarioFromDatabase():DoUsuario =
        try {
            usuarioDao.getAll().toDomain()
        } catch (e: Exception) {
            DoUsuario()
        }


    //Se eliminar el usuario de la BD
    suspend fun cerrarSesion(
        usuario: DoUsuario,
        configuracion: DoConfiguracion,
        url: String,
        timeout: Long = 60L
    ):DoError {
        val error = loginService.cerrarSesion(usuario, configuracion, url, timeout)
        usuarioDao.deleteAll()
        return error
    }





    suspend fun insertUsuarioPorDefault() =
        try {
            usuarioDao.insertUsuarioDefault()
        } catch (e: Exception) {
            e.printStackTrace()
        }
}




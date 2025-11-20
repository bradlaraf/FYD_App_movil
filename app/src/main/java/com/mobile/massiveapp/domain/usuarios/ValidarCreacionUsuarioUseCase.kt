package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ConfigurarUsuarioDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.domain.model.DoValidarUsuario
import javax.inject.Inject

class ValidarCreacionUsuarioUseCase @Inject constructor(
    private val configurarUsuarioDao: ConfigurarUsuarioDao,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke(usuarioCabecera: DoConfigurarUsuario, action: String) =
        try {
            var Codigo = "null"
            var Email = "null"
            var IdTelefono = "null"
            var Vendedor = "null"
            var Telefono = "null"
            var Nombre = "null"

            if (action == "U"){
                val userEditBd = configurarUsuarioDao.getUser(usuarioCabecera.Code)
                if (userEditBd.Code != usuarioCabecera.Code){
                    Codigo = usuarioCabecera.Code
                }
                if (userEditBd.Name != usuarioCabecera.Name){
                    Nombre = usuarioCabecera.Name
                }
                if (userEditBd.Email != usuarioCabecera.Email){
                    Email = usuarioCabecera.Email
                }
                if (userEditBd.IdPhone1 != usuarioCabecera.IdPhone1){
                    IdTelefono = usuarioCabecera.IdPhone1
                }
                if (userEditBd.DefaultSlpCode.toString() != usuarioCabecera.DefaultSlpCode){
                    Vendedor = usuarioCabecera.DefaultSlpCode
                }
                if (userEditBd.Phone1 != usuarioCabecera.Phone1){
                    Telefono = usuarioCabecera.Phone1
                }
            } else {
                Codigo = usuarioCabecera.Code
                Email = usuarioCabecera.Email
                IdTelefono = usuarioCabecera.IdPhone1
                Vendedor = usuarioCabecera.DefaultSlpCode
                Telefono = usuarioCabecera.Phone1
                Nombre = usuarioCabecera.Name

            }

            val validarUsuario = configurarUsuarioDao.validarUsuario(
                Codigo = Codigo,
                Email = Email,
                IdTelefono = IdTelefono,
                Vendedor = Vendedor,
                Telefono = Telefono,
                Nombre = Nombre
            )
            validarUsuario
        } catch (e:Exception){
            errorLogDao.insert(getError(code = "Validacion usuario", message = "${e.message}"))
            e.message.toString()
            DoValidarUsuario()
        }
}
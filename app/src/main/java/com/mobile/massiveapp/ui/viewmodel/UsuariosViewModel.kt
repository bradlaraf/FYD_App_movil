package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.domain.model.DoConfigurarUsuarioInfo
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.domain.model.DoUsuarioAlmacenes
import com.mobile.massiveapp.domain.model.DoUsuarioGrupoArticulos
import com.mobile.massiveapp.domain.model.DoUsuarioGrupoSocios
import com.mobile.massiveapp.domain.model.DoUsuarioListaPrecios
import com.mobile.massiveapp.domain.model.DoUsuarioView
import com.mobile.massiveapp.domain.model.DoUsuarioZonas
import com.mobile.massiveapp.domain.model.DoValidarUsuario
import com.mobile.massiveapp.domain.usuarios.CerrarSesionUsuarioUseCase
import com.mobile.massiveapp.domain.usuarios.CerrarTodasLasSesionesUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioAlmacenesCreacionUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioAlmacenesUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioGeneralInfoUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioGrupoArticulosCreacionUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioGrupoArticulosUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioGrupoSociosCreacionUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioGrupoSociosUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioListaPreciosCreacionUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioListaPreciosUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioZonasCreacionUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuarioZonasUseCase
import com.mobile.massiveapp.domain.usuarios.GetAllUsuariosUseCase
import com.mobile.massiveapp.domain.usuarios.GetUsuarioInfoUseCase
import com.mobile.massiveapp.domain.usuarios.ResetIdMovilUseCase
import com.mobile.massiveapp.domain.usuarios.ResetearIDUnUsuarioUseCase
import com.mobile.massiveapp.domain.usuarios.SendUsuarioUseCase
import com.mobile.massiveapp.domain.usuarios.ValidarCreacionUsuarioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuariosViewModel @Inject constructor(
    private val getAllUsuariosUseCase: GetAllUsuariosUseCase,
    private val getAllUsuarioGeneralInfoUseCase: GetAllUsuarioGeneralInfoUseCase,
    private val getAllUsuarioZonasUseCase: GetAllUsuarioZonasUseCase,
    private val getAllUsuarioListaPreciosUseCase: GetAllUsuarioListaPreciosUseCase,
    private val getAllUsuarioAlmacenesUseCase: GetAllUsuarioAlmacenesUseCase,
    private val getAllUsuarioGrupoArticulosUseCase: GetAllUsuarioGrupoArticulosUseCase,
    private val getAllUsuarioGrupoSociosUseCase: GetAllUsuarioGrupoSociosUseCase,
    private val validarCreacionUsuarioUseCase: ValidarCreacionUsuarioUseCase,

    private val getAllUsuarioZonasCreacionUseCase: GetAllUsuarioZonasCreacionUseCase,
    private val getAllUsuarioListaPreciosCreacionUseCase: GetAllUsuarioListaPreciosCreacionUseCase,
    private val getAllUsuarioAlmacenesCreacionUseCase: GetAllUsuarioAlmacenesCreacionUseCase,
    private val getAllUsuarioGrupoArticulosCreacionUseCase: GetAllUsuarioGrupoArticulosCreacionUseCase,
    private val getAllUsuarioGrupoSociosCreacionUseCase: GetAllUsuarioGrupoSociosCreacionUseCase,

    private val getUsuarioInfoUseCase: GetUsuarioInfoUseCase,

    private val sendUsuarioUseCase: SendUsuarioUseCase,

    private val cerrarSesionUsuarioUseCase: CerrarSesionUsuarioUseCase,
    private val cerrarTodasLasSesionesUseCase: CerrarTodasLasSesionesUseCase,
    private val resetearIDUnUsuarioUseCase: ResetearIDUnUsuarioUseCase,

    private val resetIdMovilUseCase: ResetIdMovilUseCase

    ): ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val isLoadingCierreMasivo = MutableLiveData<Boolean>()
    val isLoadingResetId = MutableLiveData<Boolean>()

    //Reset id de todos los usuarios
    val dataResetIdMovil = MutableLiveData<DoError>()
    fun resetIdMovil(){
        viewModelScope.launch {
            isLoadingResetId.postValue(true)
            val result = resetIdMovilUseCase()
            result.let {
                dataResetIdMovil.postValue(it)
                isLoadingResetId.postValue(false)
            }
        }
    }

    //Obtener info del usuario
    val dataGetusuarioInfo = MutableLiveData<DoUsuarioView>()
    fun getUsuarioInfo(code: String){
        viewModelScope.launch {
            val result = getUsuarioInfoUseCase(code)
            result.let {
                dataGetusuarioInfo.postValue(it)
            }
        }
    }

    //Validar Creacion de Usuario
    val dataValidarCreacionUsuario = MutableLiveData<DoValidarUsuario>()
    fun validarCreacionUsuario(usuarioCabecera: DoConfigurarUsuario, action:String){
        viewModelScope.launch {
            val result = validarCreacionUsuarioUseCase(usuarioCabecera, action)
            result.let {
                dataValidarCreacionUsuario.postValue(it)
            }
        }
    }

    //Cerrar Todas las sesiones
    val dataCerrarTodasLasSesiones = MutableLiveData<DoError>()
    fun cerrarTodasLasSesiones(){
        viewModelScope.launch {
            isLoadingCierreMasivo.postValue(true)
            val result = cerrarTodasLasSesionesUseCase()
            result.let {
                dataCerrarTodasLasSesiones.postValue(it)
                isLoadingCierreMasivo.postValue(false)
            }
        }
    }

    //Cerrar Sesion Usuario
    val dataCerrarSesionUsuario = MutableLiveData<DoError>()
    fun cerrarSesionUsuario(userCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = cerrarSesionUsuarioUseCase(userCode)
            result.let {
                dataCerrarSesionUsuario.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

    //Resetear ID de un usuario
    val dataResetearIDUnUsuario = MutableLiveData<DoError>()
    fun resetearIDUnUsuario(userCode: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = resetearIDUnUsuarioUseCase(userCode)
            result.let {
                dataResetearIDUnUsuario.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

    //All usuarios FLow
    val dataAllUsuariosFlow: Flow<List<DoConfigurarUsuario>> = getAllUsuariosUseCase.getAllUsuariosFlow()
    val dataAllUsuariosLiveData: LiveData<List<DoConfigurarUsuario>> = dataAllUsuariosFlow.asLiveData()

    //Enviar Usuario
    val sendUsuarioIsLoading = MutableLiveData<Boolean>()
    val dataSendUsuario = MutableLiveData<DoError>()
    fun sendUsuario(datosUsuario: HashMap<String, List<DoNuevoUsuarioItem>>, usuarioCabecera: DoConfigurarUsuario, accion: String){
        viewModelScope.launch {
            sendUsuarioIsLoading.postValue(true)
            val result = sendUsuarioUseCase(datosUsuario, usuarioCabecera, accion)
            result.let {
                dataSendUsuario.postValue(it)
                sendUsuarioIsLoading.postValue(false)
            }
        }
    }

    val dataGetAllUsuarios = MutableLiveData<List<DoConfigurarUsuario>>()
    fun getAllUsuarios(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllUsuariosUseCase()
            result.let {
                dataGetAllUsuarios.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

    val dataGetAllUsuarioGeneralInfo = MutableLiveData<DoConfigurarUsuarioInfo>()
    fun getAllUsuarioGeneralInfo(userCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioGeneralInfoUseCase(userCode)
            result.let {
                dataGetAllUsuarioGeneralInfo.postValue(it)
            }
        }
    }


    val dataGetAllUsuarioZonas = MutableLiveData<List<DoUsuarioZonas>>()
    fun getAllUsuarioZonas(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioZonasUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioZonas.postValue(it)
            }
        }
    }


    val dataGetAllUsuarioListaPrecios = MutableLiveData<List<DoUsuarioListaPrecios>>()
    fun getAllUsuarioListaPrecios(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioListaPreciosUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioListaPrecios.postValue(it)
            }
        }
    }


    val dataGetAllUsuarioAlmacenes = MutableLiveData<List<DoUsuarioAlmacenes>>()
    fun getAllUsuarioAlmacenes(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioAlmacenesUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioAlmacenes.postValue(it)
            }
        }
    }


    val dataGetAllUsuarioGrupoArticulos = MutableLiveData<List<DoUsuarioGrupoArticulos>>()
    fun getAllUsuarioGrupoArticulos(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioGrupoArticulosUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioGrupoArticulos.postValue(it)
            }
        }
    }


    val dataGetAllUsuarioGrupoSocios = MutableLiveData<List<DoUsuarioGrupoSocios>>()
    fun getAllUsuarioGrupoSocios(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioGrupoSociosUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioGrupoSocios.postValue(it)
            }
        }
    }



    val dataGetAllUsuarioZonasCreacion = MutableLiveData<List<DoNuevoUsuarioItem>>()
    fun getAllUsuarioZonasCreacion(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioZonasCreacionUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioZonasCreacion.postValue(it)
            }
        }
    }


    val dataGetAllUsuarioListaPreciosCreacion = MutableLiveData<List<DoNuevoUsuarioItem>>()
    fun getAllUsuarioListaPreciosCreacion(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioListaPreciosCreacionUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioListaPreciosCreacion.postValue(it)
            }
        }
    }


    val dataGetAllUsuarioAlmacenesCreacion = MutableLiveData<List<DoNuevoUsuarioItem>>()
    fun getAllUsuarioAlmacenesCreacion(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioAlmacenesCreacionUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioAlmacenesCreacion.postValue(it)
            }
        }
    }


    val dataGetAllUsuarioGrupoArticulosCreacion = MutableLiveData<List<DoNuevoUsuarioItem>>()
    fun getAllUsuarioGrupoArticulosCreacion(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioGrupoArticulosCreacionUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioGrupoArticulosCreacion.postValue(it)
            }
        }
    }


    val dataGetAllUsuarioGrupoSociosCreacion = MutableLiveData<List<DoNuevoUsuarioItem>>()
    fun getAllUsuarioGrupoSociosCreacion(usuarioCode: String){
        viewModelScope.launch {
            val result = getAllUsuarioGrupoSociosCreacionUseCase(usuarioCode)
            result.let {
                dataGetAllUsuarioGrupoSociosCreacion.postValue(it)
            }
        }
    }

}
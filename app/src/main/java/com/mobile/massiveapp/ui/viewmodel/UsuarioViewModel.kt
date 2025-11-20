package com.mobile.massiveapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.MassiveApp.Companion.prefs
import com.mobile.massiveapp.MassiveApp.Companion.prefsApp
import com.mobile.massiveapp.domain.login.GetEstadoSesionUseCase
import com.mobile.massiveapp.domain.login.GetUsuarioFromDatabaseUseCase
import com.mobile.massiveapp.domain.login.LogOutUseCase
import com.mobile.massiveapp.domain.login.LoginUseCase
import com.mobile.massiveapp.domain.login.SetCanCreateUseCase
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.domain.usuarios.DeleteAllInfoUsuarioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getUsuarioFromDatabaseUseCase: GetUsuarioFromDatabaseUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val setCanCreateUseCase: SetCanCreateUseCase,
    private val getEstadoSesionUseCase: GetEstadoSesionUseCase,
    private val deleteAllInfoUsuarioUseCase: DeleteAllInfoUsuarioUseCase
):ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

        //Estado de sesion
    val dataGetEstadoSesion = MutableLiveData<Any>()
    fun getEstadoSesion(){
        viewModelScope.launch {
            val result = getEstadoSesionUseCase()
            result.let {
                dataGetEstadoSesion.postValue(it)
            }
        }
    }

        //Set CanCreate
    val dataSetCanCreate = MutableLiveData<Boolean>()
    fun setCanCreate(){
        viewModelScope.launch {
            val result = setCanCreateUseCase()
            result.let {
                dataSetCanCreate.postValue(it)
            }
        }
    }

        //Login
    val loginSuccess = MutableLiveData<DoError>()

    fun login(version: String, usuario:String, password:String, context: Context){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = loginUseCase(version, usuario, password, context)

            result.let {
                loginSuccess.postValue(it)
                isLoading.postValue(false)
            }
        }
    }


        //Obtener usuario
    val dataGetUsuarioFromDatabase = MutableLiveData<DoUsuario>()

    fun getUsuarioFromDatabase(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getUsuarioFromDatabaseUseCase()

            result.let {
                dataGetUsuarioFromDatabase.postValue(it)
                isLoading.postValue(false)
            }
        }
    }


        //Logout
    val dataLogOut = MutableLiveData<DoError>()
    val isLoadingLogOut = MutableLiveData<Boolean>()
    fun logOut(){
        viewModelScope.launch {
            prefsApp.saveChangeHappened("Y")

            isLoadingLogOut.postValue(true)
            deleteAllInfoUsuarioUseCase()

            val result = logOutUseCase()

            result.let {
                dataLogOut.postValue(it)
                isLoadingLogOut.postValue(false)
            }
        }
    }

    //Logout Drawer
    val dataLogOutDrawer = MutableLiveData<DoError>()
    val isLoadingLogOutDrawer = MutableLiveData<Boolean>()
    fun logOutDrawer(){
        viewModelScope.launch {
            isLoadingLogOutDrawer.postValue(true)
            val result = logOutUseCase()

            result.let {
                dataLogOutDrawer.postValue(it)
                isLoadingLogOutDrawer.postValue(false)
            }
        }
    }
}
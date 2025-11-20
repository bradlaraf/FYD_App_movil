package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.GetDatosMaestrosUseCase
import com.mobile.massiveapp.domain.GetInfoSociosUseCase
import com.mobile.massiveapp.domain.GetInfoUsuariosUseCase
import com.mobile.massiveapp.domain.GetInventarioUseCase
import com.mobile.massiveapp.domain.SendDatosMaestrosUseCase
import com.mobile.massiveapp.domain.SendReinicializacionDeMaestrosUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticulosFromEndpointUseCase
import com.mobile.massiveapp.domain.model.DoError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatosMaestrosViewModel @Inject constructor(
    private val getDatosMaestrosUseCase: GetDatosMaestrosUseCase,
    private val sendDatosMaestrosUseCase: SendDatosMaestrosUseCase,
    private val sendReinicializacionDeMaestrosUseCase: SendReinicializacionDeMaestrosUseCase,
    private val getArticulosFromEndpointUseCase: GetArticulosFromEndpointUseCase,
    private val getInventarioUseCase: GetInventarioUseCase,
    private val getInfoUsuariosUseCase: GetInfoUsuariosUseCase,
    private val getInfoSociosUseCase: GetInfoSociosUseCase
) : ViewModel(){
    val isLoading = MutableLiveData<Boolean>()

    val dataGetArticulosFromEndpoint = MutableLiveData<Boolean>()

    //Sincronizar Info Usuario
    val dataGetInfoUsuario = MutableLiveData<DoError>()
    fun getInfoUsuario(progressCallBack: (Int, String, Int) -> Unit){
        viewModelScope.launch {
            val result = getInfoUsuariosUseCase(progressCallBack)
            result.let {
                dataGetInfoUsuario.postValue(it)
            }
        }
    }

    //Sincronizar Socios
    val dataGetInfoSocios = MutableLiveData<DoError>()
    fun getInfoSocios(progressCallBack: (Int, String, Int) -> Unit){
        viewModelScope.launch {
            val result = getInfoSociosUseCase(progressCallBack)
            result.let {
                dataGetInfoSocios.postValue(it)
            }
        }
    }

    //Sincronizar Inventario
    val dataGetInventario = MutableLiveData<DoError>()
    fun getInventario(progressCallBack: (Int, String, Int) -> Unit){
        viewModelScope.launch {
            val result = getInventarioUseCase(progressCallBack)
            result.let {
                dataGetInventario.postValue(it)
            }

        }
    }

    fun getArticulosFromEndpoint(){
        viewModelScope.launch {
            val result = getArticulosFromEndpointUseCase()
            result.let {
                dataGetArticulosFromEndpoint.postValue(it)
            }
        }
    }
        //Enviar la reinicializacion de los datos maestros
    val dataSendReinicializacionDeMaestros = MutableLiveData<Boolean>()

    fun sendReinicializacionDeMaestros(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = sendReinicializacionDeMaestrosUseCase()
            result.let {
                isLoading.postValue(false)
                dataSendReinicializacionDeMaestros.postValue(it)
            }
        }
    }

    val dataSetSincronizarMaestrosRemote = MutableLiveData<Boolean>()
    fun setSincronizarMaestrosRemote(){
        dataSetSincronizarMaestrosRemote.postValue(false)
        viewModelScope.launch {
            dataSetSincronizarMaestrosRemote.postValue(true)
        }
    }

    val dataSetSincronizarDocumentosRemote = MutableLiveData<Boolean>()
    fun setSincronizarDocumentosRemote(){
        /*dataSetSincronizarMaestrosRemote.postValue(false)*/
        viewModelScope.launch {
            dataSetSincronizarDocumentosRemote.postValue(true)
        }
    }



        //Obtener todos los datos Maestros
    val dataGetAllDatosMaestros = MutableLiveData<DoError>()
    fun getAllDatosMaestros(progressCallBack: (Int, String, Int) -> Unit){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getDatosMaestrosUseCase(progressCallBack)
            result.let {
                dataGetAllDatosMaestros.postValue(it)
                isLoading.postValue(false)
            }
        }
    }


    //Envio de datos Maestros al servidor
    val dataSendDatosMaestros = MutableLiveData<DoError>()
    fun sendDatosMaestros(progressCallBack: (Int, String, Int) -> Unit) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = sendDatosMaestrosUseCase(progressCallBack)
            result.let {
                dataSendDatosMaestros.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

}
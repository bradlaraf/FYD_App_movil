package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.model.DoSocioContactos
import com.mobile.massiveapp.domain.sociouc.sociocontacto.DeleteSocioContactoPorCelularYCardCodeUseCase
import com.mobile.massiveapp.domain.sociouc.sociocontacto.GetSocioContactoPorCelularYCardCodeUseCase
import com.mobile.massiveapp.domain.sociouc.sociocontacto.GetSocioContactosPorCardCodeUseCase
import com.mobile.massiveapp.domain.sociouc.sociocontacto.SaveSocioContactoUseCase


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocioContactoViewModel @Inject constructor(
    private val saveSocioContactoUseCase: SaveSocioContactoUseCase,
    private val getSocioContactosPorCardCodeUseCase: GetSocioContactosPorCardCodeUseCase,
    private val getSocioContactoPorCelularYCardCodeUseCase: GetSocioContactoPorCelularYCardCodeUseCase,
    private val deleteSocioContactoPorCelularYCardCodeUseCase: DeleteSocioContactoPorCelularYCardCodeUseCase
):ViewModel() {
    val isLoading = MutableLiveData<Boolean>()


        //Guardar un socio contacto
    val dataSaveSocioContacto = MutableLiveData<Boolean>()

    fun saveSocioContacto(contactoNuevo: DoSocioContactos){
        viewModelScope.launch {
            isLoading.value = true
            val result = saveSocioContactoUseCase(contactoNuevo)
            result.let {
                dataSaveSocioContacto.postValue(it)
                isLoading.value = false
            }
        }
    }

        //Obtener todos los socios contactos por CardCode
    val dataGetSocioContactosPorCardCode = MutableLiveData<List<DoSocioContactos>>()

    fun getSocioContactosPorCardCode(cardCode: String){
        viewModelScope.launch {
            isLoading.value = true
            val result = getSocioContactosPorCardCodeUseCase(cardCode)
            result.let {
                dataGetSocioContactosPorCardCode.postValue(it)
                isLoading.value = false
            }
        }
    }

        //Obtener un socio contacto por celular y cardCode
    val dataGetSocioContactoPorCelularYCardCode = MutableLiveData<DoSocioContactos>()

    fun getSocioContactoPorCelularYCardCode(celular: String, cardCode: String){
        viewModelScope.launch {
            isLoading.value = true
            val result = getSocioContactoPorCelularYCardCodeUseCase(celular, cardCode)

            result.let {
                dataGetSocioContactoPorCelularYCardCode.postValue(it)
                isLoading.value = false
            }
        }
    }

        //Eliminar un socio contacto por celular y cardCode
    val dataDeleteSocioContactoPorCelularYCardCode = MutableLiveData<Boolean>()

    fun deleteSocioContactoPorCelularYCardCode(celular: String, cardCode: String){
        viewModelScope.launch {
            isLoading.value = true
            val result = deleteSocioContactoPorCelularYCardCodeUseCase(celular, cardCode)

            result.let {
                dataDeleteSocioContactoPorCelularYCardCode.postValue(it)
                isLoading.value = false
            }
        }
    }
}
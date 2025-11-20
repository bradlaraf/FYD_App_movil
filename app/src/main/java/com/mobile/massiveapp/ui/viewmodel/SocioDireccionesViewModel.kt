package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import com.mobile.massiveapp.domain.sociouc.GetAllDireccionPorTipoYCardCodeUseCase
import com.mobile.massiveapp.domain.sociouc.direcciones.DeleteUnaDireccionPorCardCodeYTipoUseCase
import com.mobile.massiveapp.domain.sociouc.direcciones.GetAllDireccionesPorCardCodeUseCase
import com.mobile.massiveapp.domain.sociouc.direcciones.GetDireccionPorCardCodeTipoYLineNumUseCase
import com.mobile.massiveapp.domain.sociouc.direcciones.GetLineNumPorCardCodeYTipoUseCase
import com.mobile.massiveapp.domain.sociouc.direcciones.SaveDireccionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocioDireccionesViewModel @Inject constructor(
    private val getLineNumPorCardCodeYTipoUseCase: GetLineNumPorCardCodeYTipoUseCase,
    private val saveDireccionUseCase: SaveDireccionUseCase,
    private val getAllDireccionesPorCardCodeYTipoUseCase: GetAllDireccionPorTipoYCardCodeUseCase,
    private val getDireccionPorCardCodeTipoYLineNumUseCase: GetDireccionPorCardCodeTipoYLineNumUseCase,
    private val deleteUnaDireccionPorCardCodeYTipoUseCase: DeleteUnaDireccionPorCardCodeYTipoUseCase,
    private val getAllDireccionesPorCardCodeUseCase: GetAllDireccionesPorCardCodeUseCase
): ViewModel(){
    val isLoading = MutableLiveData<Boolean>()


        //Guardar una direccion de despacho
    val dataSaveDireccion = MutableLiveData<Boolean>()

    fun saveDireccion(direccion: DoSocioDirecciones){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = saveDireccionUseCase(direccion)

            result.let {
                dataSaveDireccion.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


        //Obtener todas las direcciones por CardCode
    val dataGetAllDireccionesPorCardCode = MutableLiveData<List<DoSocioDirecciones>>()
    fun getAllDireccionesPorCardCode(cardCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllDireccionesPorCardCodeUseCase(cardCode)

            result.let {
                dataGetAllDireccionesPorCardCode.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


        //Obtener el LineNum por el CardCode y Tipo
    val dataGetLineNumPorCardCodeYTipo = MutableLiveData<Int>()

    fun getLineNumPorCardCodeYTipo(cardCode: String, tipo: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getLineNumPorCardCodeYTipoUseCase(cardCode, tipo)

            if (result != -1){
                dataGetLineNumPorCardCodeYTipo.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Obtener Todas las direcciones por CardCode y tipo
    val dataGetAllDireccionesPorCardCodeYTipo = MutableLiveData<List<DoSocioDirecciones>>()

    fun getAllDireccionesPorCardCodeYTipo(cardCode: String, tipo: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllDireccionesPorCardCodeYTipoUseCase(cardCode, tipo)

            result.let {
                dataGetAllDireccionesPorCardCodeYTipo.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Obtener una direccion por cardCode, tipo y LineNum
    val dataGetDireccionPorCardCodeTipoYLineNum = MutableLiveData<DoSocioDirecciones>()

    fun getDireccionPorCardCodeTipoYLineNum(cardCode: String, tipo: String, lineNum: Int){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getDireccionPorCardCodeTipoYLineNumUseCase(cardCode, tipo, lineNum)

            result.let {
                dataGetDireccionPorCardCodeTipoYLineNum.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Eliminar una direccion por CardCode y tipo
    val dataDeleteDireccionPorCardCodeYTipo = MutableLiveData<Boolean>()

    fun deleteUnaDireccionPorCardCodeYTipo(cardCode: String, tipo: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = deleteUnaDireccionPorCardCodeYTipoUseCase(cardCode, tipo)

            result.let {
                dataDeleteDireccionPorCardCodeYTipo.postValue(result)
                isLoading.postValue(false)
            }
        }
    }
}
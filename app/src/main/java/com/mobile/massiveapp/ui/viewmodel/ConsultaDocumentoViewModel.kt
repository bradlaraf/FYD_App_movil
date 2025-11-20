package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.consultadocumento.SaveDireccionConsultaDocumentoUseCase
import com.mobile.massiveapp.domain.consultadocumento.ValidarExistenciaDeDocumentosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsultaDocumentoViewModel @Inject constructor(
    private val saveDireccionConsultaDocumentoUseCase: SaveDireccionConsultaDocumentoUseCase,
    private val validarExistenciaDeDocumentosUseCase: ValidarExistenciaDeDocumentosUseCase
): ViewModel() {
    val isLoading = MutableLiveData<Boolean>()


        //Guardar una direccion de Consulta documento
    val dataSaveDireccionConsultaDocumento = MutableLiveData<Boolean>()

    fun saveDireccionConsultaDocumento(accDocEntry: String){
        viewModelScope.launch {
            val result = saveDireccionConsultaDocumentoUseCase(accDocEntry)
            result.let {
                dataSaveDireccionConsultaDocumento.postValue(result)
            }
        }
    }

        //Validar si existe documento en la bd
    val dataValidarExistenciaDeDocumento = MutableLiveData<Boolean>()

    fun validarExistenciaDeDocumento(numeroDocumento: String){
        viewModelScope.launch {
            val result = validarExistenciaDeDocumentosUseCase(numeroDocumento)
            result.let {
                dataValidarExistenciaDeDocumento.postValue(it)
            }
        }
    }



}
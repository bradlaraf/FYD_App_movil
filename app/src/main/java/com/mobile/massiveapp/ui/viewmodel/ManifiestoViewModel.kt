package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.manifiesto.GetAllManiefiestosUseCase
import com.mobile.massiveapp.domain.manifiesto.GetAllManifiestoDocumentosUseCase
import com.mobile.massiveapp.domain.model.DoManifiesto
import com.mobile.massiveapp.domain.model.DoManifiestoDocumentoView
import com.mobile.massiveapp.ui.view.util.diffutil.ManifiestoDocuementoDiffUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManifiestoViewModel @Inject constructor(
    private val getAllManiefiestosUseCase: GetAllManiefiestosUseCase,
    private val getAllManifiestoDocumentosUseCase: GetAllManifiestoDocumentosUseCase
):ViewModel(){
    val isLoading = MutableLiveData<Boolean>()

    //Todos los manifiestos
    val dataGetAllManifiestos = MutableLiveData<List<DoManifiesto>>()
    fun getAllManifiestos(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllManiefiestosUseCase()

            result.let {
                isLoading.postValue(false)
                dataGetAllManifiestos.postValue(result)
            }
        }
    }

    //Todos los documentos del manifiesto
    val dataGetAllManifiestoDocumento = MutableLiveData<List<DoManifiestoDocumentoView>>()
    fun getAllManifiestoDocumento(){
        viewModelScope.launch {
            val result = getAllManifiestoDocumentosUseCase()
            result.let {
                dataGetAllManifiestoDocumento.postValue(it)
            }
        }
    }
}
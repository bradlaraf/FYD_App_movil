package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.manifiesto.GetAllManiefiestosUseCase
import com.mobile.massiveapp.domain.model.DoManifiesto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManifiestoViewModel @Inject constructor(
    private val getAllManiefiestosUseCase: GetAllManiefiestosUseCase
):ViewModel(){
    val isLoading = MutableLiveData<Boolean>()

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
}
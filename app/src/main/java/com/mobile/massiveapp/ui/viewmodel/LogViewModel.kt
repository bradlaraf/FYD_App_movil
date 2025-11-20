package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.data.database.entities.ErrorLogEntity
import com.mobile.massiveapp.domain.log.GetAllErroresUseCase
import com.mobile.massiveapp.domain.log.GetErrorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val getAllErroresUseCase: GetAllErroresUseCase,
    private val getErrorUseCase: GetErrorUseCase
): ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

    val dataGetAllErrores = MutableLiveData<List<ErrorLogEntity>>()

    fun getAllErrores(){
        viewModelScope.launch {
            val result = getAllErroresUseCase()
            result.let {
                dataGetAllErrores.postValue(it)
            }
        }
    }

    val dataGetError = MutableLiveData<ErrorLogEntity>()

    fun getError(date: String, hour: String){
        viewModelScope.launch {
            val result = getErrorUseCase(date, hour)
            result.let {
                dataGetError.postValue(it)
            }
        }
    }
}
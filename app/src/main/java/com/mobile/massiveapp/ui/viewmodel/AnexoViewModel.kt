package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.massiveapp.domain.anexo.GetAllImagesByCodeUseCase
import com.mobile.massiveapp.domain.model.DoAnexoImagen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AnexoViewModel @Inject constructor(
    private val getAllImagesByCodeUseCase: GetAllImagesByCodeUseCase
):  ViewModel()
{
    val isLoading = MutableLiveData<Boolean>()

    fun getAnexoImagesByCode(code: String): Flow<List<DoAnexoImagen>> {
        return getAllImagesByCodeUseCase.getAllImagesByCode(code)
    }

}
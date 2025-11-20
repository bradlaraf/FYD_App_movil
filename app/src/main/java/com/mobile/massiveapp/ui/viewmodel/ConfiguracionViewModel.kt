package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.data.model.InfoTable
import com.mobile.massiveapp.domain.configuracion.GetConfiguracionActualUseCase
import com.mobile.massiveapp.domain.configuracion.ReinicializarPedidosYPagosUseCase
import com.mobile.massiveapp.domain.configuracion.SaveConfiguracionUseCase
import com.mobile.massiveapp.domain.configuracion.SaveLimitesConfigurationUseCase
import com.mobile.massiveapp.domain.configuracion.GetAllTablasNoSincronizadasUseCase
import com.mobile.massiveapp.domain.model.DoConfiguracion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfiguracionViewModel @Inject constructor(
    private val saveConfiguracionUseCase: SaveConfiguracionUseCase,
    private val getConfiguracionActualUseCase: GetConfiguracionActualUseCase,
    private val reinicializarPedidosYPagosUseCase: ReinicializarPedidosYPagosUseCase,
    private val saveLimitesConfigurationUseCase: SaveLimitesConfigurationUseCase,
    private val getAllTablasNoSincronizadasUseCase: GetAllTablasNoSincronizadasUseCase
):ViewModel() {

    val isloading = MutableLiveData<Boolean>()

    //Obtener Tablas sin informacion
    val dataGetAllTablasNoSincronizadas = MutableLiveData<List<InfoTable>>()
    fun getAllTablasNoSincronizadas(){
        viewModelScope.launch {
            val result = getAllTablasNoSincronizadasUseCase()
            result.let {
                dataGetAllTablasNoSincronizadas.postValue(it)
            }
        }
    }

        //Update limites
    val dataSaveLimitesConfiguration = MutableLiveData<Boolean>()
    fun saveLimitesConfiguration(usarLimite: Boolean,articulo: Int, factura: Int, cliente: Int){
        viewModelScope.launch {
            val result = saveLimitesConfigurationUseCase(usarLimite, articulo, factura, cliente)
            result.let {
                dataSaveLimitesConfiguration.postValue(it)
            }
        }
    }

        //Reinicializar pedidos y pagos
    val dataReinicializarPedidosYPagos = MutableLiveData<Boolean>()
    fun reinicializarPedidosYPagos() {
        viewModelScope.launch {
            val result = reinicializarPedidosYPagosUseCase()
            result.let {
                dataReinicializarPedidosYPagos.postValue(it)
            }
        }
    }


        //Guardar una configuracion
    val dataSaveConfiguracion = MutableLiveData<Boolean>()

    fun saveConfiguracion(configuracion: DoConfiguracion) {
        viewModelScope.launch {
            val result = saveConfiguracionUseCase(configuracion)
            result.let {
                dataSaveConfiguracion.postValue(it)
            }
        }
    }


        //Obtener la configuracion actual
    val dataGetConfiguracionActual = MutableLiveData<DoConfiguracion>()

    fun getConfiguracionActual() {
        viewModelScope.launch {
            val result = getConfiguracionActualUseCase()
            result.let {
                dataGetConfiguracionActual.postValue(it)
            }
        }
    }
}
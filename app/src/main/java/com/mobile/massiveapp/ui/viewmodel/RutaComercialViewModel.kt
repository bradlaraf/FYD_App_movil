package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.MassiveApp.Companion.prefsRutaComercial
import com.mobile.massiveapp.data.model.RutaComercial
import com.mobile.massiveapp.domain.model.DoClienteRutaComercial
import com.mobile.massiveapp.domain.model.DoDireccion
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoRutaComercial
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.domain.model.DoRutaComercialView
import com.mobile.massiveapp.domain.rutacomercial.DeleteDetalleRutaComercialUseCase
import com.mobile.massiveapp.domain.rutacomercial.DeleteRutaComercialDetalleUseCase
import com.mobile.massiveapp.domain.rutacomercial.GetAllRutasComercialUseCase
import com.mobile.massiveapp.domain.rutacomercial.GetAllDireccionesClienteUseCase
import com.mobile.massiveapp.domain.rutacomercial.GetRutaComercialByAccDocEntryUseCase
import com.mobile.massiveapp.domain.rutacomercial.GetRutaComercialDetalleViewUseCase
import com.mobile.massiveapp.domain.rutacomercial.InsertarClienteRutasComercialesUseCase
import com.mobile.massiveapp.domain.rutacomercial.ObtenerClienteRutasComercialesUseCase
import com.mobile.massiveapp.domain.rutacomercial.SaveConfirmacionRutaUseCase
import com.mobile.massiveapp.domain.rutacomercial.SaveRutaComercialDetalleUseCase
import com.mobile.massiveapp.domain.rutacomercial.SaveRutaComercialUseCase
import com.mobile.massiveapp.domain.rutacomercial.UpdateAddressRutaComercialDetalleUseCase
import com.mobile.massiveapp.domain.rutacomercial.UpdateDocLinesRutaComercialUseCase
import com.mobile.massiveapp.domain.rutacomercial.UpdateRutaComercialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RutaComercialViewModel @Inject constructor(
    private val getAllRutasComercialUseCase: GetAllRutasComercialUseCase,
    private val getRutaComercialDetalleViewUseCase: GetRutaComercialDetalleViewUseCase,
    private val saveRutaComercialDetalleUseCase: SaveRutaComercialDetalleUseCase,
    private val updateDocLinesRutaComercialUseCase: UpdateDocLinesRutaComercialUseCase,
    private val saveRutaComercialUseCase: SaveRutaComercialUseCase,
    private val getAllDireccionesClienteUseCase: GetAllDireccionesClienteUseCase,
    private val updateAddressRutaComercialDetalleUseCase: UpdateAddressRutaComercialDetalleUseCase,
    private val getRutaComercialByAccDocEntryUseCase: GetRutaComercialByAccDocEntryUseCase,
    private val updateRutaComercialUseCase: UpdateRutaComercialUseCase,
    private val deleteDetalleRutaComercialUseCase: DeleteDetalleRutaComercialUseCase,
    private val deleteRutaComercialDetalleUseCase: DeleteRutaComercialDetalleUseCase,
    private val obtenerClienteRutasComercialesUseCase: ObtenerClienteRutasComercialesUseCase,
    private val insertarClienteRutasComercialesUseCase: InsertarClienteRutasComercialesUseCase,
    private val saveConfirmacionRutaUseCase: SaveConfirmacionRutaUseCase
) : ViewModel() {

    //Confirmar Ruta
    val dataSaveConfirmacionRuta = MutableLiveData<DoError>()
    fun saveConfirmacionRuta(detalle: DoRutaComercialDetalleView, comentario: String){
        viewModelScope.launch {
            val result = saveConfirmacionRutaUseCase(detalle, comentario)
            result.let {
                dataSaveConfirmacionRuta.postValue(it)
            }
        }
    }

    //Eliminar un detalle de ruta
    val dataDeleteRutaComercialDetalle = MutableLiveData<Boolean>()
    fun deleteRutaComercialDetalle(docLine: Int, accDocEntry: String){
        viewModelScope.launch {
            val result = deleteRutaComercialDetalleUseCase(docLine, accDocEntry)
            result.let {
                dataDeleteRutaComercialDetalle.postValue(it)
            }
        }
    }

    // --- Lista de rutas (pantalla principal) ---

    private val _fechasFiltro = MutableStateFlow(
        prefsRutaComercial.getFechaInicio() to prefsRutaComercial.getFechaFin()
    )

    val dataGetAllRutas: Flow<List<DoRutaComercialView>> = _fechasFiltro
        .flatMapLatest { (inicio, fin) -> getAllRutasComercialUseCase.getAllRutas(inicio, fin) }

    fun actualizarFechasFiltro() {
        _fechasFiltro.value = prefsRutaComercial.getFechaInicio() to prefsRutaComercial.getFechaFin()
    }

    // --- Detalle de una ruta (nueva / editar) ---

    private val _accDocEntry = MutableStateFlow("")

    val detalleFlow: Flow<List<DoRutaComercialDetalleView>> = getRutaComercialDetalleViewUseCase.getAllDetallesViewFlow()

    fun initAccDocEntry(accDocEntry: String) { _accDocEntry.value = accDocEntry }

    fun saveDetalle(accDocEntry: String, cardCode: String) {
        viewModelScope.launch {
            saveRutaComercialDetalleUseCase(accDocEntry, cardCode)
        }
    }

    fun updateDocLines(detalle: List<DoRutaComercialDetalleView>) {
        viewModelScope.launch { updateDocLinesRutaComercialUseCase(detalle)
        }
    }

    fun deleteDetalle(accDocEntry: String, cardCode: String) {
        viewModelScope.launch { deleteDetalleRutaComercialUseCase(accDocEntry, cardCode) }
    }

    // --- Cabecera ---

    val dataGetRutaComercial = MutableLiveData<DoRutaComercial?>()

    fun cargarRuta(accDocEntry: String) {
        viewModelScope.launch {
            dataGetRutaComercial.postValue(getRutaComercialByAccDocEntryUseCase(accDocEntry))
        }
    }

    val dataSaveRutaCabecera = MutableLiveData<DoError>()
    val isLoadingSaveRutaCabecera = MutableLiveData<Boolean>()
    fun saveRutaCabecera(rutaComercialCabecera: RutaComercial) {
        viewModelScope.launch {
            isLoadingSaveRutaCabecera.postValue(true)
            val result = saveRutaComercialUseCase(rutaComercialCabecera)
            result.let {
                dataSaveRutaCabecera.postValue(it)
                isLoadingSaveRutaCabecera.postValue(false)
            }
        }
    }

    fun updateRutaFecha(accDocEntry: String, fechaRuta: String) {
        viewModelScope.launch { updateRutaComercialUseCase(accDocEntry, fechaRuta) }
    }

    //Direcciones Cliente
    val dataGetAllDireccionesCliente = MutableLiveData<List<DoDireccion>?>()
    fun getAllDireccionesCliente(cardCode: String) {
        dataGetAllDireccionesCliente.value = null
        viewModelScope.launch {
            val result = getAllDireccionesClienteUseCase(cardCode)
            result.let {
                dataGetAllDireccionesCliente.postValue(it)
            }
        }
    }

    fun updateAddress(accDocEntry: String, direccion: DoDireccion, lineNum: Int) {
        viewModelScope.launch {
            updateAddressRutaComercialDetalleUseCase(accDocEntry, direccion, lineNum)
        }
    }

    val dataObtenerClienteRutasComerciales = MutableLiveData<List<DoClienteRutaComercial>>()
    fun obtenerClienteRutasComerciales() {
        viewModelScope.launch {
            val result = obtenerClienteRutasComercialesUseCase()
            result.let {
                dataObtenerClienteRutasComerciales.postValue(it)
            }
        }
    }

    val dataInsertarClienteRutasComerciales = MutableLiveData<Boolean>()
    fun insertarClienteRutasComerciales(json: String) {
        viewModelScope.launch {
            val result = insertarClienteRutasComercialesUseCase(json)
            result.let {
                dataInsertarClienteRutasComerciales.postValue(it)
            }
        }
    }
}

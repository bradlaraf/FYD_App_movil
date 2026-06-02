package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.data.model.LiquidacionPago
import com.mobile.massiveapp.domain.cobranza.GetPagosDetalleFlowUseCase
import com.mobile.massiveapp.domain.manifiesto.DeleteAllPagosConfirmationDialogUseCase
import com.mobile.massiveapp.domain.manifiesto.DeleteLiquidacionPagoUseCase
import com.mobile.massiveapp.domain.manifiesto.EditarLiquidacionPagoUseCase
import com.mobile.massiveapp.domain.manifiesto.GetAllManiefiestosUseCase
import com.mobile.massiveapp.domain.manifiesto.GetManifiestosCanceladosUseCase
import com.mobile.massiveapp.domain.manifiesto.GetManifiestosPendientesUseCase
import com.mobile.massiveapp.domain.manifiesto.GetAllManifiestoDocumentosUseCase
import com.mobile.massiveapp.domain.manifiesto.GetAllPagosXManifiestoUseCase
import com.mobile.massiveapp.domain.manifiesto.GetInfoCobranzaManifiestoUseCase
import com.mobile.massiveapp.domain.manifiesto.GetInfoManifiestoUseCase
import com.mobile.massiveapp.domain.manifiesto.GetLiquidacionPagoEdicionUseCase
import com.mobile.massiveapp.domain.manifiesto.GetMontoPendientePagoEdicionUseCase
import com.mobile.massiveapp.domain.manifiesto.SaveLiquidacionUseCase
import com.mobile.massiveapp.domain.manifiesto.SendPagosUseCase
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoInfoCobranzaManifiesto
import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import com.mobile.massiveapp.domain.model.DoLiquidacionPagoView
import com.mobile.massiveapp.domain.model.DoLiquidacionPagosTotales
import com.mobile.massiveapp.domain.model.DoManifiesto
import com.mobile.massiveapp.domain.model.DoManifiestoDocumentoView
import com.mobile.massiveapp.domain.model.DoManifiestoView
import com.mobile.massiveapp.ui.view.util.diffutil.ManifiestoDocuementoDiffUtil
import com.mobile.massiveapp.MassiveApp.Companion.prefsManifiesto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManifiestoViewModel @Inject constructor(
    private val getAllManiefiestosUseCase: GetAllManiefiestosUseCase,
    private val getAllManifiestoDocumentosUseCase: GetAllManifiestoDocumentosUseCase,
    private val getInfoCobranzaManifiestoUseCase: GetInfoCobranzaManifiestoUseCase,
    private val saveLiquidacionUseCase: SaveLiquidacionUseCase,
    private val getPagosDetalleFlowUseCase: GetPagosDetalleFlowUseCase,
    private val deleteLiquidacionPagoUseCase: DeleteLiquidacionPagoUseCase,
    private val deleteAllPagosConfirmationDialogUseCase: DeleteAllPagosConfirmationDialogUseCase,
    private val sendPagosUseCase: SendPagosUseCase,
    private val getAllPagosXManifiestoUseCase: GetAllPagosXManifiestoUseCase,
    private val getInfoManifiestoUseCase: GetInfoManifiestoUseCase,
    private val getLiquidacionPagoEdicionUseCase: GetLiquidacionPagoEdicionUseCase,
    private val editarLiquidacionPagoUseCase: EditarLiquidacionPagoUseCase,
    private val getMontoPendientePagoEdicionUseCase: GetMontoPendientePagoEdicionUseCase,
    private val getManifiestosPendientesUseCase: GetManifiestosPendientesUseCase,
    private val getManifiestosCanceladosUseCase: GetManifiestosCanceladosUseCase
):ViewModel(){
    val isLoading = MutableLiveData<Boolean>()

    //Editar Liquidacion Pago
    val dataEditarLiquidacionPago = MutableLiveData<Boolean>()
    fun editarLiquidacionPago(liquidacion: DoLiquidacionPago){
        viewModelScope.launch {
            val result = editarLiquidacionPagoUseCase(liquidacion)
            result.let {
                dataEditarLiquidacionPago.postValue(it)
            }
        }
    }

    //Obtener pago para edicion
    val dataGetLiquidacionPagoEdicion = MutableLiveData<DoLiquidacionPago>()
    val dataGetMontoPendientePagoEdicion = MutableLiveData<Double>()
    fun getLiquidacionPagoEdicion(accDocEntry: String){
        viewModelScope.launch {
            val result = getLiquidacionPagoEdicionUseCase(accDocEntry)
            val resultMonto = getMontoPendientePagoEdicionUseCase(accDocEntry)
            result.let {
                dataGetLiquidacionPagoEdicion.postValue(it)
            }
            resultMonto.let {
                dataGetMontoPendientePagoEdicion.postValue(it)
            }
        }
    }

    //Obtener la info de un manifiesto
    val dataGetInfoManifiesto = MutableLiveData<DoManifiestoView>()
    fun getInfoManifiesto(docEntry: Int){
        viewModelScope.launch {
            val result = getInfoManifiestoUseCase(docEntry)
            result.let {
                dataGetInfoManifiesto.postValue(it)
            }
        }
    }

    //Todos los pagos del manifiesto
    val dataGetAllPagosXManifiesto = MutableLiveData<List<DoLiquidacionPagoView>>()
    fun getAllPagosXManifiesto(docEntry: Int){
        viewModelScope.launch {
            val result = getAllPagosXManifiestoUseCase(docEntry)
            result.let {
                dataGetAllPagosXManifiesto.postValue(it)
            }
        }
    }

    //Enviar los pagos de la liquidación
    val isLoadingSendPagos = MutableLiveData<Boolean>()
    val dataSendPagos = MutableLiveData<DoError>()
    fun sendPagos(){
        viewModelScope.launch {
            isLoadingSendPagos.postValue(true)
            val result = sendPagosUseCase()
            result.let {
                dataSendPagos.postValue(it)
                isLoadingSendPagos.postValue(false)
            }
        }
    }

    //Eliminar todos los pagos agregados a la cobranza(Dialog de confirmacion)
    val dataDeleteAllPagosConfirmationDialog = MutableLiveData<Boolean>()
    fun deleteAllPagosConfirmationDialog(accDocEntry:String){
        viewModelScope.launch {
            val result = deleteAllPagosConfirmationDialogUseCase(accDocEntry)
            result.let {
                dataDeleteAllPagosConfirmationDialog.postValue(it)
            }
        }
    }

    //Eliminar Liquidacion pago
    val dataDeleteLiquidacionPago = MutableLiveData<Boolean>()
    fun deleteLiquidacionPago(accDocEntry: String){
        viewModelScope.launch {
            val result = deleteLiquidacionPagoUseCase(accDocEntry)
            result.let {
                dataDeleteLiquidacionPago.postValue(it)
            }
        }
    }

    //Registrar Liquidacion
    val dataSaveLiquidacion = MutableLiveData<Boolean>()
    fun saveLiquidacion(liquidacion: DoLiquidacionPago) {
        viewModelScope.launch {
            val result = saveLiquidacionUseCase(liquidacion)
            result.let {
                dataSaveLiquidacion.postValue(it)
            }
        }
    }

    val dataGetAllLiquidacionPagoFlow: Flow<List<DoLiquidacionPagoView>> = getPagosDetalleFlowUseCase.getAllPagosLiquidacion()
    val dateGetAllTotalesPagosFlow: Flow<DoLiquidacionPagosTotales> = getPagosDetalleFlowUseCase.getTotalesPagos()
    val dataGetManifiestosDocumentos: Flow<List<DoManifiestoDocumentoView>> = getPagosDetalleFlowUseCase.getManifiestosDocumentos()
    val dataGetPagosManifiesto: Flow<List<DoLiquidacionPagoView>> = getPagosDetalleFlowUseCase.getPagosManifiesto()

    //Todos los manifiestos
    val dataGetAllManifiestos = MutableLiveData<List<DoManifiestoView>>()
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

    private val _fechasFiltro = MutableStateFlow(
        prefsManifiesto.getFechaInicio() to prefsManifiesto.getFechaFin()
    )

    val dataGetManifiestosPendientes: Flow<List<DoManifiestoView>> = _fechasFiltro
        .flatMapLatest { (inicio, fin) ->
            getManifiestosPendientesUseCase.getManifiestosPendientes(inicio, fin)
        }

    val dataGetManifiestosCancelados: Flow<List<DoManifiestoView>> = _fechasFiltro
        .flatMapLatest { (inicio, fin) ->
            getManifiestosCanceladosUseCase.getManifiestosCancelados(inicio, fin)
        }

    fun actualizarFechas() {
        _fechasFiltro.value = prefsManifiesto.getFechaInicio() to prefsManifiesto.getFechaFin()
    }

    //Todos los documentos del manifiesto
    val dataGetAllManifiestoDocumento = MutableLiveData<List<DoManifiestoDocumentoView>>()
    fun getAllManifiestoDocumento(docEntry: Int){
        viewModelScope.launch {
            val result = getAllManifiestoDocumentosUseCase(docEntry)
            result.let {
                dataGetAllManifiestoDocumento.postValue(it)
            }
        }
    }

    //Informacion manifiesto Documento para cobranza
    val dataGetInfoCobranzaManifiesto = MutableLiveData<DoInfoCobranzaManifiesto>()
    fun getInfoCobranzaManifiesto(docEntry: Int) {
        viewModelScope.launch {
            val result = getInfoCobranzaManifiestoUseCase(docEntry)
            result.let {
                dataGetInfoCobranzaManifiesto.postValue(it)
            }
        }
    }
}
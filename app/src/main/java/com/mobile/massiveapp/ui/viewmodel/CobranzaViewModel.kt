package com.mobile.massiveapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.data.model.ClientePagos
import com.mobile.massiveapp.domain.cobranza.CancelCobranzaUseCase
import com.mobile.massiveapp.domain.cobranza.DeleteAllPagoDetalleParaEditarUseCase
import com.mobile.massiveapp.domain.cobranza.DeleteAllPagoDetalleSinCabeceraUseCase
import com.mobile.massiveapp.domain.cobranza.DeleteAllPagosDetallesPorAccDocEntryUseCase
import com.mobile.massiveapp.domain.cobranza.DeleteUnPagoDetallePorAccDocEntryYDocLineUseCase
import com.mobile.massiveapp.domain.cobranza.DuplicateAllPagoDetallesParaActualizacionUseCase
import com.mobile.massiveapp.domain.cobranza.GetAllPagoDetalleXAccDocEntryUseCase
import com.mobile.massiveapp.domain.cobranza.GetAllPagosCabeceraNoMigradosUseCase
import com.mobile.massiveapp.domain.cobranza.GetAllPagosCabeceraAprobadosUseCase
import com.mobile.massiveapp.domain.cobranza.GetAllPagosDetalleParaEditarUseCase
import com.mobile.massiveapp.domain.cobranza.GetAllPagosDetallePorAccDocEntryUseCase
import com.mobile.massiveapp.domain.cobranza.GetCobranzaDetallePorAccDocEntryYLineNumUseCase
import com.mobile.massiveapp.domain.cobranza.GetCobranzasCanceladasUseCase
import com.mobile.massiveapp.domain.cobranza.GetCobranzasDeAyerUseCase
import com.mobile.massiveapp.domain.cobranza.GetCurrentDocLineFlowUseCase
import com.mobile.massiveapp.domain.cobranza.GetCurrentDocLineUseCase
import com.mobile.massiveapp.domain.cobranza.GetCurrentPaidToCodeUseCase
import com.mobile.massiveapp.domain.cobranza.GetPagoCabeceraPorAccDocEntryUseCase
import com.mobile.massiveapp.domain.cobranza.GetPagosDetalleFlowUseCase
import com.mobile.massiveapp.domain.cobranza.SaveCobranzaCabeceraUseCase
import com.mobile.massiveapp.domain.cobranza.SaveCobranzaDetalleUseCase
import com.mobile.massiveapp.domain.cobranza.SavePagoDetalleParaEditarUseCase
import com.mobile.massiveapp.domain.cobranza.SavePedidosDetalleParaCobranzaDirectaUseCase
import com.mobile.massiveapp.domain.cobranza.UpdateAllPagosDetalleUseCase
import com.mobile.massiveapp.domain.model.DoClientePago
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoPagoDetalle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CobranzaViewModel @Inject constructor(
    private val saveCobranzaCabeceraUseCase: SaveCobranzaCabeceraUseCase,
    private val saveCobranzaDetalleUseCase: SaveCobranzaDetalleUseCase,
    private val getAllPagosCabeceraAprobadosUseCase: GetAllPagosCabeceraAprobadosUseCase,
    private val getAllPagosCabeceraNoMigradosUseCase: GetAllPagosCabeceraNoMigradosUseCase,
    private val getAllPagosDetallePorAccDocEntryUseCase: GetAllPagosDetallePorAccDocEntryUseCase,
    private val getCurrentDocLineUseCase: GetCurrentDocLineUseCase,
    private val deleteAllPagosDetallesPorAccDocEntryUseCase: DeleteAllPagosDetallesPorAccDocEntryUseCase,
    private val getPagoCabeceraPorAccDocEntryUseCase: GetPagoCabeceraPorAccDocEntryUseCase,
    private val getCobranzaDetallePorAccDocEntryYLineNumUseCase: GetCobranzaDetallePorAccDocEntryYLineNumUseCase,
    private val duplicateAllPagoDetallesParaActualizacionUseCase: DuplicateAllPagoDetallesParaActualizacionUseCase,
    private val deleteAllPagoDetalleParaEditarUseCase: DeleteAllPagoDetalleParaEditarUseCase,
    private val getAllPagosDetalleParaEditarUseCase: GetAllPagosDetalleParaEditarUseCase,
    private val savePagoDetalleParaEditarUseCase: SavePagoDetalleParaEditarUseCase,
    private val updateAllPagosDetalleUseCase: UpdateAllPagosDetalleUseCase,
    private val deleteUnPagoDetallePorAccDocEntryYDocLineUseCase: DeleteUnPagoDetallePorAccDocEntryYDocLineUseCase,
    private val getCurrentPaidToCodeUseCase: GetCurrentPaidToCodeUseCase,
    private val deleteAllPagoDetalleSinCabeceraUseCase: DeleteAllPagoDetalleSinCabeceraUseCase,
    private val cancelCobranzaUseCase: CancelCobranzaUseCase,
    private val savePedidosDetalleParaCobranzaDirectaUseCase: SavePedidosDetalleParaCobranzaDirectaUseCase,
    private val getAllPagoDetalleXAccDocEntryUseCase: GetAllPagoDetalleXAccDocEntryUseCase,
    private val getPagosDetalleFlowUseCase: GetPagosDetalleFlowUseCase,
    private val getCurrentDocLineFlowUseCase: GetCurrentDocLineFlowUseCase,
    private val getCobranzasCanceladasUseCase: GetCobranzasCanceladasUseCase,
    private val getCobranzasDeAyerUseCase: GetCobranzasDeAyerUseCase
) : ViewModel(){
    val isLoading = MutableLiveData<Boolean>()


    //Set Cobranzas fecha
    val dataSetConbranzasHoy = MutableLiveData<Boolean>()
    fun setCobranzasHoy(value: Boolean){
        dataSetConbranzasHoy.postValue(value)
    }

    //Pagos de AYER
    val dataGetCobranzasDeAyer = MutableLiveData<List<DoClientePago>>()
    fun getCobranzasDeAyer(){
        viewModelScope.launch {
            val result = getCobranzasDeAyerUseCase()
            result.let {
                dataGetCobranzasDeAyer.postValue(it)
            }
        }
    }


    //Docline Actual
    val dataDoclineActual: Flow<Int> = getCurrentDocLineFlowUseCase.getCurrentDocline()

    val dataAllPagoDetalleFLow: Flow<List<DoPagoDetalle>> = getPagosDetalleFlowUseCase.getAllPagosDetalle()

    //Monto Total pagos detalle
    val dataMontoTotalPagosDetalle: Flow<Double> = getPagosDetalleFlowUseCase.getTotalPagosDetalle()

    //Cantidad total de pagos detalle
    val dataCantidadTotalPagosDetalle: Flow<Int> = getPagosDetalleFlowUseCase.getTotalCantidadPagosDetalle()


    val dataSavePedidosDetalleParaCobranzaDirecta = MutableLiveData<Boolean>()
    fun savePedidosDetalleParaCobranzaDirecta(docEntry: String, accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = savePedidosDetalleParaCobranzaDirectaUseCase(docEntry, accDocEntry)
            result.let {
                isLoading.postValue(false)
                dataSavePedidosDetalleParaCobranzaDirecta.postValue(result)
            }
        }
    }

        //Cobranzas Canceladas
    val dataGetCobranzasCanceladas = MutableLiveData<List<DoClientePago>>()
    fun getCobranzasCanceladas(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getCobranzasCanceladasUseCase()
            result.let {
                dataGetCobranzasCanceladas.postValue(it)
                isLoading.postValue(false)
            }
        }
    }


        //Traer todos los pagos detalle por accdocentry DoPagoDetalle
    val dataGetAllPagoDetalleXAccDocEntry = MutableLiveData<List<DoPagoDetalle>>()
    fun getAllPagoDetalleXAccDocEntry(accDocEntry: String){
        viewModelScope.launch {
            val result = getAllPagoDetalleXAccDocEntryUseCase(accDocEntry)
            result.let {
                dataGetAllPagoDetalleXAccDocEntry.postValue(it)
            }
        }
    }


        //Cancelar una cobranza
    val dataCancelCobranza = MutableLiveData<DoError>()
    fun cancelCobranza(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = cancelCobranzaUseCase(accDocEntry)
            result.let {
                isLoading.postValue(false)
                dataCancelCobranza.postValue(result)
            }
        }
    }


        //Eliminar todos los pagos detalles sin cabecera
    val dataDeleteAllPagoDetalleSinCabecera = MutableLiveData<Boolean>()
    fun deleteAllPagoDetalleSinCabecera(){
        viewModelScope.launch {
            val result = deleteAllPagoDetalleSinCabeceraUseCase()
            result.let {
                dataDeleteAllPagoDetalleSinCabecera.postValue(result)
            }
        }
    }



        //Obtener el PaidToDate para edicion
    val dataGetCurrentPaidToCode = MutableLiveData<Double>()
    fun getCurrentPaidToCode(accDocEntry: String, docEntry: String){
        viewModelScope.launch {
            val result = getCurrentPaidToCodeUseCase(accDocEntry, docEntry)
            result.let {
                dataGetCurrentPaidToCode.postValue(result)
            }
        }
    }




        //Eliminar un pago detalle por accDocEntry y docLine
    val dataDeleteUnPagoDetallePorAccDocEntryYDocLine = MutableLiveData<Boolean>()
    fun deleteUnPagoDetallePorAccDocEntryYDocLine(accDocEntry: String, docLine: Int){
        viewModelScope.launch {
            val result = deleteUnPagoDetallePorAccDocEntryYDocLineUseCase(accDocEntry, docLine)
            result.let {
                dataDeleteUnPagoDetallePorAccDocEntryYDocLine.postValue(result)
            }
        }
    }



        //Actualizar todos los pagos detalles
    val dataUpdateAllPagosDetalle = MutableLiveData<Boolean>()
    fun updateAllPagosDetalle(accDocEntry: String){
        viewModelScope.launch {
            val result = updateAllPagosDetalleUseCase(accDocEntry)
            result.let {
                dataUpdateAllPagosDetalle.postValue(result)
            }
        }
    }




        //Guardar un Pago Detalle para editar
    val dataSavePagoDetalleParaEditar = MutableLiveData<Boolean>()
    fun saveNuevoPagoDetalleParaEditar(clientePagoDetalle: DoClientePagoDetalle){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = savePagoDetalleParaEditarUseCase(clientePagoDetalle)
            result.let {
                isLoading.postValue(false)
                dataSavePagoDetalleParaEditar.postValue(result)
            }
        }
    }



        //Traer todos los pagos detalles para editar
    val dataGetAllPagosDetalleParaEditar = MutableLiveData<List<DoPagoDetalle>>()
    fun getAllPagosDetalleParaEditar(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllPagosDetalleParaEditarUseCase(accDocEntry)

            result.let {
                isLoading.postValue(false)
                dataGetAllPagosDetalleParaEditar.postValue(result)
            }
        }
    }



        //Eliminar todos los pagos detalles por accDocEntry
    val dataDeleteAllPagoDetalleParaEditar = MutableLiveData<Boolean>()
    fun deleteAllPagoDetalleParaEditar(accDocEntry: String){
        viewModelScope.launch {
            val result = deleteAllPagoDetalleParaEditarUseCase(accDocEntry)
            result.let {
                dataDeleteAllPagoDetalleParaEditar.postValue(result)
            }
        }
    }



        //Duplicar todos los pagos detalles por accDocEntry
    val dataDuplicateAllPagoDetallesParaActualizacion = MutableLiveData<Boolean>()
    fun duplicateAllPagoDetallesParaActualizacion(accDocEntry: String){
        viewModelScope.launch {
            val result = duplicateAllPagoDetallesParaActualizacionUseCase(accDocEntry)
            result.let {
                dataDuplicateAllPagoDetallesParaActualizacion.postValue(result)
            }
        }
    }



        //Traer un pago cabecera por el AccDocEntry
    val dataGetPagoCabeceraPorAccDocEntry = MutableLiveData<DoClientePago>()
    fun getPagoCabeceraPorAccDocEntry(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getPagoCabeceraPorAccDocEntryUseCase(accDocEntry)

            result.let {
                isLoading.postValue(false)
                dataGetPagoCabeceraPorAccDocEntry.postValue(result)
            }
        }
    }



        //Traer un pago detalle por accDocEntry y lineNum
    val dataGetCobranzaDetallePorAccDocEntryYLineNum = MutableLiveData<DoClientePagoDetalle>()
    fun getCobranzaDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getCobranzaDetallePorAccDocEntryYLineNumUseCase(accDocEntry, lineNum)

            result.let {
                isLoading.postValue(false)
                dataGetCobranzaDetallePorAccDocEntryYLineNum.postValue(result)
            }
        }
    }



        //Guardar un detella de cobranza
    val dataSaveCobranzaDetalleUseCase = MutableLiveData<Boolean>()

    fun saveCobranzaDetalle(cobranzaDetalle: DoClientePagoDetalle){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = saveCobranzaDetalleUseCase(cobranzaDetalle)

            result.let {
                isLoading.postValue(false)
                dataSaveCobranzaDetalleUseCase.postValue(result)
            }
        }
    }

        //Guardar una cabecera de cobranza
    val dataSaveCobranzaCabeceraUseCase = MutableLiveData<DoError>()
    val isLoadingSaveCobranza = MutableLiveData<Boolean>()

    fun saveCobranzaCabecera(cobranzaCabecera: ClientePagos, context: Context){
        viewModelScope.launch {
            isLoadingSaveCobranza.postValue(true)
            val result = saveCobranzaCabeceraUseCase(cobranzaCabecera, context)

            result.let {
                isLoadingSaveCobranza.postValue(false)
                dataSaveCobranzaCabeceraUseCase.postValue(result)
            }
        }
    }
        //Traer todos los pagos cabecera de un cliente
    val dataGetAllPedidosCabeceraSinDetalle = MutableLiveData<List<DoClientePago>>()

    fun getAllPagosCabeceraAprobados(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllPagosCabeceraAprobadosUseCase()

            result.let {
                isLoading.postValue(false)
                dataGetAllPedidosCabeceraSinDetalle.postValue(result)
            }
        }
    }
        //Traer todos los pagos NO Migrados
    val dataGetAllPedidosCabeceraNoMigrados = MutableLiveData<List<DoClientePago>>()

    fun getAllPagosCabeceraNoMigrados(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllPagosCabeceraNoMigradosUseCase()

            result.let {
                isLoading.postValue(false)
                dataGetAllPedidosCabeceraNoMigrados.postValue(result)
            }
        }
    }

        //Traer todos los pagos Detalle por accDocEntry
    val dataGetAllPagosDetallePorAccDocEntry = MutableLiveData<List<DoClientePagoDetalle>>()

    fun getAllPagosDetallePorAccDocEntry(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllPagosDetallePorAccDocEntryUseCase(accDocEntry)

            result.let {
                isLoading.postValue(false)
                dataGetAllPagosDetallePorAccDocEntry.postValue(result)
            }
        }
    }

        //Consultar Docline actual por accDocEntry
    val dataGetCurrentDocLine = MutableLiveData<Int>()

    fun getCurrentDocLine(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getCurrentDocLineUseCase(accDocEntry)

            result.let {
                isLoading.postValue(false)
                dataGetCurrentDocLine.postValue(result)
            }
        }
    }

        //Eliminar pago detalle por accDocEntry
    val dataDeleteAllPagosDetallesPorAccDocEntry = MutableLiveData<Boolean>()

    fun deleteAllPagosDetallesPorAccDocEntry(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = deleteAllPagosDetallesPorAccDocEntryUseCase(accDocEntry)

            result.let {
                isLoading.postValue(false)
                dataDeleteAllPagosDetallesPorAccDocEntry.postValue(result)
            }
        }
    }
}
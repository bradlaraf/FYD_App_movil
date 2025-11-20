package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.cobranza.SavePaidToDateEditUseCase
import com.mobile.massiveapp.domain.facturas.GetAllFacturasDetalleUseCase
import com.mobile.massiveapp.domain.facturas.GetAllFacturasPorCardCodeUseCase
import com.mobile.massiveapp.domain.facturas.GetAllFacturasUseCase
import com.mobile.massiveapp.domain.facturas.GetFacturaPorDocEntryUseCase
import com.mobile.massiveapp.domain.facturas.SavePaidToDatePorPagoUseCase
import com.mobile.massiveapp.domain.facturas.SetEditPtdLikePaidToDateUseCase
import com.mobile.massiveapp.domain.facturas.SetEditPtdToDefaultUseCase
import com.mobile.massiveapp.domain.model.DoClienteFacturaDetalle
import com.mobile.massiveapp.domain.model.DoClienteFacturas
import com.mobile.massiveapp.domain.model.DoFacturaView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocioFacturasViewModel @Inject constructor(
    private val getAllFacturasPorCardCodeUseCase: GetAllFacturasPorCardCodeUseCase,
    private val savePaidToDatePorPagoUseCase: SavePaidToDatePorPagoUseCase,
    private val getFacturaPorDocEntryUseCase: GetFacturaPorDocEntryUseCase,
    private val getAllFacturasUseCase: GetAllFacturasUseCase,
    private val getAllFacturasDetalleUseCase: GetAllFacturasDetalleUseCase,
    private val savePaidToDateEditUseCase: SavePaidToDateEditUseCase,
    private val setEditPtdLikePaidToDateUseCase: SetEditPtdLikePaidToDateUseCase,
    private val setEditPtdToDefaultUseCase: SetEditPtdToDefaultUseCase
):ViewModel() {
    val isLoading = MutableLiveData<Boolean>()



        //Set editPtd to default
    val dataSetEditPtdToDefault = MutableLiveData<Boolean>()
    fun setEditPtdToDefault(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = setEditPtdToDefaultUseCase()
            result.let {
                dataSetEditPtdToDefault.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Setear el valor del EditPtd al del PaidToDate
    val dataSetEditPtdLikePaidToDate = MutableLiveData<DoClienteFacturas>()
    fun setEditPtdLikePaidToDate(docEntry: Int){
        viewModelScope.launch{
            isLoading.postValue(true)
            val result = setEditPtdLikePaidToDateUseCase(docEntry)
            result.let {
                dataSetEditPtdLikePaidToDate.postValue(it)
                isLoading.postValue(false)
            }
        }
    }


    val dataGetAllFacturasDetalle = MutableLiveData<List<DoClienteFacturaDetalle>>()
    fun getAllFacturaDetallePorDocEntry(docEntry:Int){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllFacturasDetalleUseCase(docEntry)
            result.let {
                dataGetAllFacturasDetalle.postValue(it)
                isLoading.postValue(false)
            }
        }
    }


        //Traer todas las facturas
    val dataGetAllFacturas = MutableLiveData<List<DoFacturaView>>()

    fun getallFacturas() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllFacturasUseCase()
            result.let{
                dataGetAllFacturas.postValue(result)
                isLoading.postValue(false)
            }
        }
    }



        //Obtener una factura por DocEntry
    val dataGetFacturaPorDocEntry = MutableLiveData<DoClienteFacturas>()
    fun getFacturaPorDocEntry(docEntry: String){
        viewModelScope.launch {
            val result = getFacturaPorDocEntryUseCase(docEntry)
            result.let {
                dataGetFacturaPorDocEntry.postValue(result)
            }
        }
    }


        //Traer todas las facturas por CardCode
    val dataGetAllFacturasPorCardCode = MutableLiveData<List<DoClienteFacturas>>()

    fun getAllFacturasPorCardCode(cardCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllFacturasPorCardCodeUseCase(cardCode)

            if (result.isNotEmpty()){
                dataGetAllFacturasPorCardCode.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Guardar el paidToDate por pago
    val dataSavePaidToDatePorPago = MutableLiveData<Boolean>()

    fun savePaidToDatePorPago(docEntry: String, paidToDate: Double) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = savePaidToDatePorPagoUseCase(docEntry, paidToDate)

            result.let {
                dataSavePaidToDatePorPago.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Guardar el ptd para edit
    val dataSavePaidToDateToEdit = MutableLiveData<Boolean>()

    fun savePaidToDateToEdit(docEntry: String, paidToDate: Double){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = savePaidToDateEditUseCase(docEntry,paidToDate)
            result.let {
                dataSavePaidToDateToEdit.postValue(it)
                isLoading.postValue(false)
            }
        }
    }
}
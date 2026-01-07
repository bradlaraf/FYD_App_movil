package com.mobile.massiveapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.model.ClientePedidos
import com.mobile.massiveapp.data.model.PrecioFinalView
import com.mobile.massiveapp.domain.model.ArticuloPedido
import com.mobile.massiveapp.domain.model.DoArticuloUnidades
import com.mobile.massiveapp.domain.model.DoClientePedido
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoPedidoDetalle
import com.mobile.massiveapp.domain.model.DoPedidoDetalleInfo
import com.mobile.massiveapp.domain.model.DoPedidoInfoView
import com.mobile.massiveapp.domain.model.DoUnidadMedidaInfo
import com.mobile.massiveapp.domain.pedido.CancelPedidoUseCase
import com.mobile.massiveapp.domain.pedido.ComprobarEstadoActualPedidoUseCase
import com.mobile.massiveapp.domain.pedido.DeleteAllPedidoDetallePorAccDocEntryUseCase
import com.mobile.massiveapp.domain.pedido.DeleteAllPedidoDetalleSinCabeceraUseCase
import com.mobile.massiveapp.domain.pedido.DeleteAllPedidoDetallesParaEditarUseCase
import com.mobile.massiveapp.domain.pedido.DeleteUnPedidoDetallePorAccDocEntryYLineNumUseCase
import com.mobile.massiveapp.domain.pedido.DuplicatePedidoDetallesParaActualizacionUseCase
import com.mobile.massiveapp.domain.pedido.GetAllPedidoDetallesParaEditarUseCase
import com.mobile.massiveapp.domain.pedido.GetAllPedidosClienteUseCase
import com.mobile.massiveapp.domain.pedido.GetAllPedidosNoMigradosUseCase
import com.mobile.massiveapp.domain.pedido.GetAllPedidosPorCardCodeUseCase
import com.mobile.massiveapp.domain.pedido.GetPedidoCabeceraInfoUseCase
import com.mobile.massiveapp.domain.pedido.GetPedidoDetalleInfoUseCase
import com.mobile.massiveapp.domain.pedido.GetUnidadesDeMedidaPorGrupoUnidadDeMedidaUseCase
import com.mobile.massiveapp.domain.pedido.GetPedidoDetallePorAccDocEntryUseCase
import com.mobile.massiveapp.domain.pedido.GetPedidoDetallesInfoUseCase
import com.mobile.massiveapp.domain.pedido.GetPedidoPorAccDocEntryUseCase
import com.mobile.massiveapp.domain.pedido.GetPedidoSugeridoUseCase
import com.mobile.massiveapp.domain.pedido.GetPedidosCanceladosUseCase
import com.mobile.massiveapp.domain.pedido.GetPedidosDeAyerUseCase
import com.mobile.massiveapp.domain.pedido.GetPrecioArticuloUseCase
import com.mobile.massiveapp.domain.pedido.GetUnPedidoDetallePorAccDocEntryYLineNumUseCase
import com.mobile.massiveapp.domain.pedido.GetUnidadMedidaYEquivalenciaUseCase
import com.mobile.massiveapp.domain.pedido.ObtenerPrecioArticuloFYDUseCase
import com.mobile.massiveapp.domain.pedido.SavePedidoCabeceraUseCase
import com.mobile.massiveapp.domain.pedido.SavePedidoDetalleParaEditarUseCase
import com.mobile.massiveapp.domain.pedido.SavePedidoDetalleUseCase
import com.mobile.massiveapp.domain.pedido.UpdateAllPedidosDetalleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedidoViewModel @Inject constructor(
    private val savePedidoDetalleUseCase: SavePedidoDetalleUseCase,
    private val getPedidoDetallePorAccDocEntryUseCase: GetPedidoDetallePorAccDocEntryUseCase,
    private val savePedidoCabeceraUseCase: SavePedidoCabeceraUseCase,
    private val deleteAllPedidoDetallePorAccDocEntryUseCase: DeleteAllPedidoDetallePorAccDocEntryUseCase,
    private val getAllPedidosClienteUseCase: GetAllPedidosClienteUseCase,
    private val getPedidoPorAccDocEntryUseCase: GetPedidoPorAccDocEntryUseCase,
    private val deleteUnPedidoDetallePorAccDocEntryYLineNumUseCase: DeleteUnPedidoDetallePorAccDocEntryYLineNumUseCase,
    private val getAllPedidosNoMigradosUseCase: GetAllPedidosNoMigradosUseCase,
    private val getAllPedidosPorCardCodeUseCase: GetAllPedidosPorCardCodeUseCase,
    private val deleteAllPedidoDetalleSinCabeceraUseCase: DeleteAllPedidoDetalleSinCabeceraUseCase,
    private val getUnPedidoDetallePorAccDocEntryYLineNumUseCase: GetUnPedidoDetallePorAccDocEntryYLineNumUseCase,
    private val getAllPedidoDetallesParaEditarUseCase: GetAllPedidoDetallesParaEditarUseCase,
    private val duplicatePedidoDetallesParaActualizacionUseCase: DuplicatePedidoDetallesParaActualizacionUseCase,
    private val updateAllPedidosDetalleUseCase: UpdateAllPedidosDetalleUseCase,
    private val savePedidoDetalleParaEditarUseCase: SavePedidoDetalleParaEditarUseCase,
    private val deleteAllPedidoDetallesParaEditarUseCase: DeleteAllPedidoDetallesParaEditarUseCase,
    private val getUnidadMedidaYEquivalenciaUseCase: GetUnidadMedidaYEquivalenciaUseCase,
    private val getUnidadesDeMedidaPorGrupoUnidadDeMedidaUseCase: GetUnidadesDeMedidaPorGrupoUnidadDeMedidaUseCase,
    private val cancelPedidoUseCase: CancelPedidoUseCase,
    private val getPedidosCanceladosUseCase: GetPedidosCanceladosUseCase,
    private val getPedidoDetalleInfoUseCase: GetPedidoDetalleInfoUseCase,
    private val getPedidosDeAyerUseCase: GetPedidosDeAyerUseCase,
    private val getPedidoCabeceraInfoUseCase: GetPedidoCabeceraInfoUseCase,
    private val getPedidoDetallesInfoUseCase: GetPedidoDetallesInfoUseCase,
    private val comprobarEstadoActualPedidoUseCase: ComprobarEstadoActualPedidoUseCase,
    private val getPedidoSugeridoUseCase: GetPedidoSugeridoUseCase,
    private val getPrecioArticuloUseCase: GetPrecioArticuloUseCase,
    private val obtenerPrecioArticuloFYDUseCase: ObtenerPrecioArticuloFYDUseCase
    )
    : ViewModel(){
    val isLoading = MutableLiveData<Boolean>()

        //Set Pedidos fecha
    val dataSetPedidosHoy = MutableLiveData<Boolean>()
    fun setPedidosHoy(value: Boolean){
        dataSetPedidosHoy.postValue(value)
    }

    //Obtener Precio Articulo (FYD)
    val dataGetPrecioArticuloFYD = MutableLiveData<PrecioFinalView>()
    fun getPrecioArticuloFYD(itemCode: String, cantidad: Double, cardCode: String){
        viewModelScope.launch {
            val result = obtenerPrecioArticuloFYDUseCase(itemCode = itemCode, cantidad = cantidad, cardCode = cardCode)
            result.let {
                dataGetPrecioArticuloFYD.postValue(it)
            }
        }
    }

    //Comprobar si puede editar
    val isLoadingComprobarPedido = MutableLiveData<Boolean>()
    val dataComprobarEstadoActualPedido = MutableLiveData<DoError>()

    fun comprobarEstadoActualPedido(accDocEntry: String, context: Context){
        viewModelScope.launch {
            isLoadingComprobarPedido.postValue(true)
            val result = comprobarEstadoActualPedidoUseCase(accDocEntry, context)
            result.let {
                dataComprobarEstadoActualPedido.postValue(it)
                isLoadingComprobarPedido.postValue(false)
            }
        }
    }

    //Obtener el precio
    val dataGetPrecioArticulo = MutableLiveData<ArticuloPedido>()
    fun getPrecioArticulo(cantidad: Int, itemCode: String, listaPrecio: Int){
        viewModelScope.launch {
            val result = getPrecioArticuloUseCase(cantidad, itemCode, listaPrecio)
            result.let {
                dataGetPrecioArticulo.postValue(it)
            }
        }
    }

    //Detalles Info
    val dataGetPedidoDetallesInfo = MutableLiveData<DoPedidoDetalleInfo>()
    fun getPedidoDetallesInfo(accDocEntry: String, lineNum: Int){
        viewModelScope.launch {
            val result = getPedidoDetallesInfoUseCase(accDocEntry, lineNum)
            result.let {
                dataGetPedidoDetallesInfo.postValue(it)
            }
        }
    }

    val dataGetPedidoCabeceraInfo = MutableLiveData<DoPedidoInfoView>()
    fun getPedidoCabeceraInfo(accDocEntry: String){
        viewModelScope.launch {
            val result = getPedidoCabeceraInfoUseCase(accDocEntry)
            result.let {
                dataGetPedidoCabeceraInfo.postValue(it)
            }
        }
    }

        //Pedidos de ayer
    val dataGetPedidosDeAyer = MutableLiveData<List<DoClientePedido>>()
    fun getPedidosDeAyer(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getPedidosDeAyerUseCase()
            result.let {
                dataGetPedidosDeAyer.postValue(it)
                isLoading.postValue(false)

            }
        }
    }

        //Info Detalle
        val dataGetPedidoDetalleInfo = MutableLiveData<DoPedidoDetalle>()
    fun getPedidoDetalleInfo(accDocEntry: String, lineNum: Int){
        viewModelScope.launch {
            val result = getPedidoDetalleInfoUseCase(accDocEntry, lineNum)
            result.let {
                dataGetPedidoDetalleInfo.postValue(it)
            }
        }
    }

//Pedido sugerido
val datagetPedidoSugerido = MutableLiveData<Boolean>()
fun getPedidoSugerido(cardCode: String){
    viewModelScope.launch {
        val result = getPedidoSugeridoUseCase(cardCode)
        result.let {
            datagetPedidoSugerido.postValue(it)
        }
    }
}

        //Pedidos Cancelados
    val dataGetPedidosCancelados = MutableLiveData<List<DoClientePedido>>()
    fun getPedidosCancelados(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getPedidosCanceladosUseCase()
            result.let {
                dataGetPedidosCancelados.postValue(it)
                isLoading.postValue(false)
            }
        }
    }



        //Cancelar un pedido
    val dataCancelPedido = MutableLiveData<DoError>()
    fun cancelPedido(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = cancelPedidoUseCase(accDocEntry)
            result.let {
                dataCancelPedido.postValue(result)
                isLoading.postValue(false)
            }
        }
    }




        //Obtener todas las unidades de medida por el uomEntry
    val dataGetAllUnidadesDeMedidaPorItemCode = MutableLiveData<List<DoArticuloUnidades>>()
    fun getAllUnidadesDeMedidaPorGrupoUnidadDeMedida(itemCode: String){
        viewModelScope.launch {
            val result = getUnidadesDeMedidaPorGrupoUnidadDeMedidaUseCase(itemCode)
            result.let {
                dataGetAllUnidadesDeMedidaPorItemCode.postValue(result)
            }
        }
    }


        //Obtener la unidad de medida y equivalencia
    val dataGetUnidadMedidaYEquivalencia = MutableLiveData<DoUnidadMedidaInfo>()
    fun getUnidadMedidaYEquivalencia(itemCode: String, unidadMedida: String, listNum: Int){
        viewModelScope.launch {
            val result = getUnidadMedidaYEquivalenciaUseCase(itemCode, unidadMedida, listNum)
            result.let {
                dataGetUnidadMedidaYEquivalencia.postValue(result)
            }
        }
    }


        //Eliminar todos los pedido detalle para editar
    val dataDeleteAllPedidoDetallesParaEditar = MutableLiveData<Boolean>()
    fun deleteAllPedidoDetallesParaEditar(accDocEntry: String){
        viewModelScope.launch {
            val result = deleteAllPedidoDetallesParaEditarUseCase(accDocEntry)
            result.let{
                dataDeleteAllPedidoDetallesParaEditar.postValue(true)
            }
        }
    }


        //Guardar una linea de pedido detalle para editar
    fun savePedidoDetalleParaEditar(clientePedidoDetalle: ClientePedidoDetalle){
        viewModelScope.launch {
            val result = savePedidoDetalleParaEditarUseCase(clientePedidoDetalle)
            result.let {
                dataSavePedidoDetalle.postValue(true)
            }
        }
    }


        //Obtener todos los pedido detalle para editar
    fun getAllPedidoDetallesParaEditar(accDocEntry: String){
        viewModelScope.launch {
            val result = getAllPedidoDetallesParaEditarUseCase(accDocEntry)
            result.let{
                dataGetAllPedidoDetallePorAccDocEntry.postValue(result)
            }
        }
    }


        //Duplicar los pedidos detalle para actualizar
    val dataDuplicatePedidoDetallesParaActualizacion = MutableLiveData<Boolean>()
    fun duplicatePedidoDetallesParaActualizacion(accDocEntry: String){
        viewModelScope.launch {
            val result = duplicatePedidoDetallesParaActualizacionUseCase(accDocEntry)
            result.let{
                dataDuplicatePedidoDetallesParaActualizacion.postValue(result)
            }
        }
    }


        //Actualizar todos los pedidos detalle
    val dataUpdateAllPedidosDetalle = MutableLiveData<Boolean>()
    fun updateAllPedidosDetalle(accDocEntry: String){
        viewModelScope.launch {
            val result = updateAllPedidosDetalleUseCase(accDocEntry)
            result.let{
                dataUpdateAllPedidosDetalle.postValue(result)
            }
        }
    }


    //Guardar una linea de pedido detalle
    val dataSavePedidoDetalle = MutableLiveData<Boolean>()
    fun savePedidoDetalle(clientePedidoDetalle: ClientePedidoDetalle){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = savePedidoDetalleUseCase(clientePedidoDetalle)

            if (result){
                dataSavePedidoDetalle.postValue(true)
                isLoading.postValue(false)
            }
        }

    }


    //Guardar un pedido en la BD
    val dataSavePedidoCabecera = MutableLiveData<DoError>()
    val isLoadingSavePedido = MutableLiveData<Boolean>()

    fun savePedidoCabecera(pedidoCliente: ClientePedidos){
        viewModelScope.launch {
            isLoadingSavePedido.postValue(true)
            val result = savePedidoCabeceraUseCase(pedidoCliente)

            result.let {
                dataSavePedidoCabecera.postValue(it)
                isLoadingSavePedido.postValue(false)
            }
        }
    }


    //Obtener un pedido por el accDocEntry
    val dataGetPedidoPorAccDocEntry = MutableLiveData<ClientePedidos>()

    fun getPedidoPorAccDocEntry(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getPedidoPorAccDocEntryUseCase(accDocEntry)

            result.let {
                dataGetPedidoPorAccDocEntry.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


    //Obtener todos los pedidos del cliente
    val dataGetAllPedidosCliente = MutableLiveData<List<DoClientePedido>>()

    fun getAllPedidosCliente(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllPedidosClienteUseCase()

            result.let{
                dataGetAllPedidosCliente.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


        //Obtener un pedido detalle por el accDocEntry y LineNum
    val dataGetUnPedidoDetallePorAccDocEntryYLineNum = MutableLiveData<ClientePedidoDetalle>()
    fun getUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: Int){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getUnPedidoDetallePorAccDocEntryYLineNumUseCase(accDocEntry, lineNum)

            result.let{
                dataGetUnPedidoDetallePorAccDocEntryYLineNum.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


    //Obtener todos los pedidos NO MIGRADOS
    val dataGetAllPedidosNoMigrados = MutableLiveData<List<DoClientePedido>>()
    fun getAllPedidosNoMigrados(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllPedidosNoMigradosUseCase()

            result.let {
                dataGetAllPedidosNoMigrados.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Detalles Pedidos FLOW
    val dataAllPedidosDetalle: Flow<List<ClientePedidoDetalle>> =
        getPedidoDetallePorAccDocEntryUseCase.getAllPedidoDetalle()
    val dataAllPedidoDetalleParaEditar: Flow<List<ClientePedidoDetalle>> =
        getPedidoDetallePorAccDocEntryUseCase.getAllPedidoDetalleParaEditar()

    //Obtener los detalles del pedido por el accDocEntry
    val dataGetAllPedidoDetallePorAccDocEntry = MutableLiveData<List<ClientePedidoDetalle>>()

    fun getAllPedidoDetallePorAccDocEntry(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getPedidoDetallePorAccDocEntryUseCase(accDocEntry)

            result.let{
                dataGetAllPedidoDetallePorAccDocEntry.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


    //Obtener todos los pedidos por CardCode
    val dataGetAllPedidosPorCardCode = MutableLiveData<List<ClientePedidos>>()

    fun getAllPedidosPorCardCode(cardCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllPedidosPorCardCodeUseCase(cardCode)

            result.let {
                dataGetAllPedidosPorCardCode.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


    //Eliminar todos los detalles del pedido por el accDocEntry
    val dataDeleteAllPedidoDetallePorAccDocEntry = MutableLiveData<Boolean>()

    fun deleteAllPedidoDetallePorAccDocEntry(accDocEntry: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = deleteAllPedidoDetallePorAccDocEntryUseCase(accDocEntry)

            if (result != null){
                dataDeleteAllPedidoDetallePorAccDocEntry.postValue(true)
                isLoading.postValue(false)
            }
        }
    }


        //Eliminar un pedido detalle por el accDocEntry y LineNum
    val dataDeleteUnPedidoDetallePorAccDocEntryYLineNum = MutableLiveData<Boolean>()

    fun deleteUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: Int){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = deleteUnPedidoDetallePorAccDocEntryYLineNumUseCase(accDocEntry, lineNum)

            result.let{
                dataDeleteUnPedidoDetallePorAccDocEntryYLineNum.postValue(true)
                isLoading.postValue(false)
            }
        }
    }


        //Eliminar todos los pedido detalle sin Cabecera
    val dataDeleteAllPedidoDetalleSinCabecera = MutableLiveData<Boolean>()

    fun deleteAllPedidoDetalleSinCabecera(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = deleteAllPedidoDetalleSinCabeceraUseCase()

            result.let{
                dataDeleteAllPedidoDetalleSinCabecera.postValue(true)
                isLoading.postValue(false)
            }
        }
    }

}
package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.articulouc.GetAllArticuloAlmacenesUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticuloInfoUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticuloPorItemCodeUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticuloListaPrecioUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticuloPedidoInfoUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticuloPrecioListaNombreUseCase
import com.mobile.massiveapp.domain.articulouc.GetAllArticuloPreciosPorItemCodeUseCase
import com.mobile.massiveapp.domain.articulouc.GetAllArticulosFromDatabaseUseCase
import com.mobile.massiveapp.domain.articulouc.GetAllArticulosUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticuloCantidadPedidoUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticuloCantidadesPorItemCodeYWhsCodeUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticulosConStockUseCase
import com.mobile.massiveapp.domain.articulouc.GetArticulosSinStockUseCase
import com.mobile.massiveapp.domain.articulouc.GetCountArticulosUseCase
import com.mobile.massiveapp.domain.articulouc.SearchArticuloUseCase
import com.mobile.massiveapp.domain.model.DoArticulo
import com.mobile.massiveapp.domain.model.DoArticuloAlmacenes
import com.mobile.massiveapp.domain.model.DoArticuloCantidades
import com.mobile.massiveapp.domain.model.DoArticuloInfo
import com.mobile.massiveapp.domain.model.DoArticuloInv
import com.mobile.massiveapp.domain.model.DoArticuloInventario
import com.mobile.massiveapp.domain.model.DoArticuloListaPrecios
import com.mobile.massiveapp.domain.model.DoArticuloPedidoInfo
import com.mobile.massiveapp.domain.model.DoArticuloPrecios
import com.mobile.massiveapp.domain.pedido.GetArticuloPrecioPorItemCodeYPriceListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticuloViewModel @Inject constructor(
    private val getAllArticulosFromDatabaseUseCase: GetAllArticulosFromDatabaseUseCase,
    private val getAllArticuloPreciosPorItemCodeUseCase: GetAllArticuloPreciosPorItemCodeUseCase,
    private val getArticuloListaPrecioUseCase: GetArticuloListaPrecioUseCase,
    private val getArticuloPrecioListaNombreUseCase: GetArticuloPrecioListaNombreUseCase,
    private val getArticuloAlmacenesUseCase: GetAllArticuloAlmacenesUseCase,
    private val getArticuloPorItemCodeUseCase: GetArticuloPorItemCodeUseCase,
    private val getArticuloPedidoInfoUseCase: GetArticuloPedidoInfoUseCase,
    private val getArticuloInfoUseCase: GetArticuloInfoUseCase,
    private val getArticuloPrecioPorItemCodeYPriceListUseCase: GetArticuloPrecioPorItemCodeYPriceListUseCase,
    private val getArticuloCantidadesPorItemCodeYWhsCodeUseCase: GetArticuloCantidadesPorItemCodeYWhsCodeUseCase,
    private val getAllArticulosUseCase: GetAllArticulosUseCase,
    private val getArticuloCantidadPedidoUseCase: GetArticuloCantidadPedidoUseCase,
    private val getCountArticulosUseCase: GetCountArticulosUseCase,
    private val searchArticuloUseCase: SearchArticuloUseCase,
    private val getArticulosConStockUseCase: GetArticulosConStockUseCase,
    private val getArticulosSinStockUseCase: GetArticulosSinStockUseCase
): ViewModel() {
    val isLoading = MutableLiveData<Boolean>()


        //Articulos CON Stock
    val dataGetArticulosConStock = MutableLiveData<List<DoArticuloInv>>()
    fun getArticulosConStock(){
        viewModelScope.launch {
            val result = getArticulosConStockUseCase()
            result.let {
                dataGetArticulosConStock.postValue(it)
            }
        }
    }

    //Articulos SIN Stock
    val dataGetArticulosSinStock = MutableLiveData<List<DoArticuloInventario>>()
    fun getArticulosSinStock(){
        viewModelScope.launch {
            val result = getArticulosSinStockUseCase()
            result.let {
                dataGetArticulosSinStock.postValue(it)
            }
        }
    }


        //Seleccion de info de articulo
    val articuloSeleccionado = MutableLiveData<DoArticuloInv>()
    fun saveArticuloSeleccionado(articulo: DoArticuloInv){
        viewModelScope.launch {
            articuloSeleccionado.postValue(articulo)
        }
    }




        /** Trae todos los Articulos - Se trae el onHand y el grupo Articulo con un map() - hace lento el traer articulos **/

    val dataGetAllArticulos = MutableLiveData<List<DoArticulo>>()
    fun getAllArticulos(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllArticulosFromDatabaseUseCase()
            result.let{
                dataGetAllArticulos.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Buscar Articulos
    val dataSearchArticulo = MutableLiveData<List<DoArticuloInventario>>()
    fun searchArticulo(text: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = searchArticuloUseCase(text)
            result.let {
                dataGetAllArticulosSm.postValue(it)
                dataSearchArticulo.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Todos los articulos
    val dataGetCountArticulos: Flow<Int> = getCountArticulosUseCase.getCountArticulosFlow()

    /*fun getCountArticulos() {
        viewModelScope.launch {
            val result = getCountArticulosUseCase()
            result.let {
                dataGetCountArticulos.postValue(it)
            }
        }
    }*/

        //Trae todos los artículos sin modificaciones
    val dataGetAllArticulosSm: MutableLiveData<List<DoArticuloInventario>> = MutableLiveData()
    fun getAllArticulosSm(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllArticulosUseCase()
            result.let{
                dataGetAllArticulosSm.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

    val dataGetArticuloCantidadPedido = MutableLiveData<Double>()
    fun getArticuloCantidadPedido(itemCode: String, unidadMedida: String, whsCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getArticuloCantidadPedidoUseCase(itemCode, unidadMedida, whsCode)
            result.let {
                dataGetArticuloCantidadPedido.postValue(it)
                isLoading.postValue(false)
            }
        }
    }


        //Trae todos los articulo precios con el nombre de la lista de precios por ItemCode
    val dataGetAllArticuloPreciosPorItemCode = MutableLiveData<List<DoArticuloPrecios>>()
    fun getAllArticuloPreciosPorItemCode(itemCode:String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllArticuloPreciosPorItemCodeUseCase(itemCode)

            result.let {
                dataGetAllArticuloPreciosPorItemCode.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


        //Trae un articulo precio por lista e itemCode
    val dataGetArticuloPrecioPorItemCodeYPriceList = MutableLiveData<DoArticuloPrecios>()

    fun getArticuloPrecioPorItemCodeYPriceList(itemCode: String, priceList: Int){
        viewModelScope.launch {
            val result = getArticuloPrecioPorItemCodeYPriceListUseCase(itemCode, priceList)

            result.let{
                dataGetArticuloPrecioPorItemCodeYPriceList.postValue(result)
            }
        }
    }


        //Trae un articulo cantidad por itemCode y whsCode
    val dataGetArticuloCantidadesPorItemCodeYWhsCode = MutableLiveData<DoArticuloCantidades>()
    val _dataGetArticuloCantidadesPorItemCodeYWhsCode: LiveData<DoArticuloCantidades> get() = dataGetArticuloCantidadesPorItemCodeYWhsCode
    fun getArticuloCantidadesPorItemCodeYWhsCode(itemCode: String, whsCode: String){
        viewModelScope.launch {
            val result = getArticuloCantidadesPorItemCodeYWhsCodeUseCase(itemCode, whsCode)
            val result2 = getAllArticuloPreciosPorItemCodeUseCase(itemCode)
            result.let{
                dataGetArticuloCantidadesPorItemCodeYWhsCode.postValue(result)
            }
            result2.let {
                dataGetAllArticuloPreciosPorItemCode.postValue(it)
            }

        }
    }

        //Trae todas las unidades de medida por ItemCode



        //todos los Articulo Lista Precios
    val dataGetAllArticuloListaPrecios = MutableLiveData<List<DoArticuloListaPrecios>>()

    fun getAllArticuloListaPrecios(){
        viewModelScope.launch {
            val result = getArticuloListaPrecioUseCase()

            if (!result.isNullOrEmpty()){
                dataGetAllArticuloListaPrecios.postValue(result)
                isLoading.postValue(false)
            }
        }
    }



        //Buscar todos los Articulo Almacen
    val dataGetAllArticuloAlmacenes = MutableLiveData<List<DoArticuloAlmacenes>>()

    fun getAllArticuloAlmacenes(){
        viewModelScope.launch {
            val result = getArticuloAlmacenesUseCase()

            if (!result.isNullOrEmpty()){
                dataGetAllArticuloAlmacenes.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


        //Articulo Pedido Info
    val dataGetArticuloPedidoInfo = MutableLiveData<DoArticuloPedidoInfo>()
    fun getArticuloPedidoInfo(ItemCode: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getArticuloPedidoInfoUseCase(ItemCode)

            result.let{
                dataGetArticuloPedidoInfo.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Articulo Info
    val dataGetArticuloInfo = MutableLiveData<DoArticuloInfo>()
    fun getArticuloInfo(ItemCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getArticuloInfoUseCase(ItemCode)

            result.let{
                dataGetArticuloInfo.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

}
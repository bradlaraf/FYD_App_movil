package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProviderViewModel @Inject constructor(

):ViewModel() {

    //LiveData para Strings en general
    private val mutableGetData = MutableLiveData<Boolean>()
    val dataRecolectarData: LiveData<Boolean> get() = mutableGetData
    fun recolectarData(data: Boolean){
        mutableGetData.value = data
    }

    //LiveData Contador
    private val MutableDataGetContador = MutableLiveData<Int>()
    val dataGetContador: LiveData<Int> get() = MutableDataGetContador
    fun saveContador(data: Int){
        MutableDataGetContador.value = data
    }

    //LiveData Info Usuario
    private val MutableDataGetInfoUsuario = MutableLiveData<DoConfigurarUsuario>()
    val dataGetInfoUsuario: LiveData<DoConfigurarUsuario> get() = MutableDataGetInfoUsuario
    fun saveInfoUsuario(data: DoConfigurarUsuario){
        MutableDataGetInfoUsuario.value = data
    }

    //LiveData Almacenes
    private val MutableDataGetAlmacenes = MutableLiveData<List<DoNuevoUsuarioItem>>()
    val dataGetAlmacenes: LiveData<List<DoNuevoUsuarioItem>> get() = MutableDataGetAlmacenes
    fun saveAlmacenes(data: List<DoNuevoUsuarioItem>){
        MutableDataGetAlmacenes.value = data
    }

    //LiveData Grupo Articulos
    private val MutableDataGetGrupoArticulos = MutableLiveData<List<DoNuevoUsuarioItem>>()
    val dataGetGrupoArticulos: LiveData<List<DoNuevoUsuarioItem>> get() = MutableDataGetGrupoArticulos
    fun saveGrupoArticulos(data: List<DoNuevoUsuarioItem>){
        MutableDataGetGrupoArticulos.value = data
    }

    //LiveData Grupo Socios
    private val MutableDataGetGrupoSocios = MutableLiveData<List<DoNuevoUsuarioItem>>()
    val dataGetGrupoSocios: LiveData<List<DoNuevoUsuarioItem>> get() = MutableDataGetGrupoSocios
    fun saveGrupoSocios(data: List<DoNuevoUsuarioItem>){
        MutableDataGetGrupoSocios.value = data
    }

    //LiveData Lista Precios
    private val MutableDataGetListaPrecios = MutableLiveData<List<DoNuevoUsuarioItem>>()
    val dataGetListaPrecios: LiveData<List<DoNuevoUsuarioItem>> get() = MutableDataGetListaPrecios
    fun saveListaPrecios(data: List<DoNuevoUsuarioItem>){
        MutableDataGetListaPrecios.value = data
    }

    //LiveData Zonas
    private val MutableDataGetZonas = MutableLiveData<List<DoNuevoUsuarioItem>>()
    val dataGetZonas: LiveData<List<DoNuevoUsuarioItem>> get() = MutableDataGetZonas
    fun saveZonas(data: List<DoNuevoUsuarioItem>){
        MutableDataGetZonas.value = data
    }


        //LiveData para el accDocEntry
    private val mutableAccDocEntry = MutableLiveData<String>()  //*mutable.accdocentry.observe*//
    val accDocEntry: LiveData<String> get() = mutableAccDocEntry

    fun saveAccDocEntry(accDocEntry: String){
        mutableAccDocEntry.value = accDocEntry
    }

        //LiveData para Strings en general
    private val mutableData = MutableLiveData<String>()
    val data: LiveData<String> get() = mutableData
    fun saveData(data: String){
        mutableData.value = data
    }

        //LiveData para HashMaps en general
    private val mutableDataHashMap = MutableLiveData<HashMap<String, Any>>()
    val dataHashMap: LiveData<HashMap<String, Any>> get() = mutableDataHashMap
    fun saveDataHashMap(hashMap: HashMap<String, Any>){
        mutableDataHashMap.value = hashMap
    }

        //LiveData para el cardCode
    private val mutableCardCode = MutableLiveData<String>()
    val cardCode: LiveData<String> get() = mutableCardCode
    fun saveCardCode(cardCode: String){
        mutableCardCode.value = cardCode
    }

        //LiveData para el docLine
    private val mutableDocLine = MutableLiveData<Int>()
    val docLine: LiveData<Int> get() = mutableDocLine
    fun saveDocLine(docLine: Int){
        mutableDocLine.value = docLine
    }



        //LiveData para el itemCode
    private val mutableItemCode = MutableLiveData<String>()
    val itemCode: LiveData<String> get() = mutableItemCode
    fun saveItemCode(itemCode: String){
        mutableItemCode.value = itemCode
    }

        //LiveData para el whsCode
    private val mutableWhsCode = MutableLiveData<String>()
    val whsCode: LiveData<String> get() = mutableWhsCode
    fun saveWhsCode(whsCode: String){
        mutableWhsCode.value = whsCode
    }


        //LiveData para nuevo Pago Detalle
    private val mutableNuevoPagoDetalle = MutableLiveData<Boolean>()
    val nuevoPagoDetalle: LiveData<Boolean> get() = mutableNuevoPagoDetalle
    fun saveNuevoPagoDetalle(nuevoPagoDetalle: Boolean){
        mutableNuevoPagoDetalle.value = nuevoPagoDetalle
    }


        //LiveData boolean para el estado de los permisos
    private val mutablePermiso = MutableLiveData<Boolean>()
    val permiso: LiveData<Boolean> get() = mutablePermiso
    fun savePermiso(permiso: Boolean){
        mutablePermiso.value = permiso
    }





}
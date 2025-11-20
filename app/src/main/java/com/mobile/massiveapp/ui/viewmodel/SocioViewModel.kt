package com.mobile.massiveapp.ui.viewmodel

import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.entities.ClienteSociosEntity
import com.mobile.massiveapp.data.repositories.pagingSource.SociosPagingSource
import com.mobile.massiveapp.domain.articulouc.GetAllSocioDireccionesUseCase
import com.mobile.massiveapp.domain.model.DoClienteInfo
import com.mobile.massiveapp.domain.model.DoClienteScreen
import com.mobile.massiveapp.domain.sociouc.GetAllSociosFiltradosPorMigradoUseCase
import com.mobile.massiveapp.domain.model.DoClienteSocios
import com.mobile.massiveapp.domain.model.DoSocioContactos
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import com.mobile.massiveapp.domain.model.DoSocioNuevoAwait
import com.mobile.massiveapp.domain.model.DoConsultaDocumento
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.sociouc.DeleteSocioNegocioUseCase
import com.mobile.massiveapp.domain.sociouc.EliminarDireccionesYContactosPorAccDocEntryUseCase
import com.mobile.massiveapp.domain.sociouc.GetAllDireccionPorTipoYCardCodeUseCase
import com.mobile.massiveapp.domain.sociouc.GetAllSocioNegocioConFacturasUseCase
import com.mobile.massiveapp.domain.sociouc.GetAllSocioNegocioNoBloqueadosUseCase
import com.mobile.massiveapp.domain.sociouc.GetDireccionesPorCardCodeUseCase
import com.mobile.massiveapp.domain.sociouc.DeleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNListUseCase
import com.mobile.massiveapp.domain.sociouc.GetAllSNUseCase
import com.mobile.massiveapp.domain.sociouc.GetAllSociosForMainScreenUseCse
import com.mobile.massiveapp.domain.sociouc.GetAllSociosPagingUseCase
import com.mobile.massiveapp.domain.sociouc.GetCountClientesUseCase
import com.mobile.massiveapp.domain.sociouc.GetInfoSocioNegocioAwaitUseCase
import com.mobile.massiveapp.domain.sociouc.GetInfoSocioNegocioUseCase
import com.mobile.massiveapp.domain.sociouc.GetSocioConsultaRucFromDatabaseUseCase
import com.mobile.massiveapp.domain.sociouc.GetSocioConsultaRucUseCase
import com.mobile.massiveapp.domain.sociouc.GetSocioContactoPorCardCodeUseCase
import com.mobile.massiveapp.domain.sociouc.GetSocioNegocioPorCardCodeYMigradoUseCase
import com.mobile.massiveapp.domain.sociouc.GetSocioNuevoAwaitUseCase
import com.mobile.massiveapp.domain.sociouc.InsertarSocioNegocioUseCase
import com.mobile.massiveapp.domain.sociouc.InsertarSocioNuevoAwaitUseCase
import com.mobile.massiveapp.domain.sociouc.InsertarUnaDireccionUseCase
import com.mobile.massiveapp.domain.sociouc.SearchSociosViewUseCase
import com.mobile.massiveapp.domain.sociouc.sociocontacto.GetInfoClientePorCardCodeUseCase
import com.mobile.massiveapp.domain.sociouc.sociocontacto.SendAllLeadsToWServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocioViewModel @Inject constructor(
    private val getAllSociosFiltradosPorMigradoUseCase: GetAllSociosFiltradosPorMigradoUseCase,
    private val getSocioConsultaRucUseCase: GetSocioConsultaRucUseCase,
    private val getSocioConsultaRucFromDatabaseUseCase: GetSocioConsultaRucFromDatabaseUseCase,
    private val insertarSocioNegocioUseCase: InsertarSocioNegocioUseCase,
    private val insertarSocioNuevoAwaitUseCase: InsertarSocioNuevoAwaitUseCase,
    private val getSocioNuevoAwaitUseCase: GetSocioNuevoAwaitUseCase,
    private val getInfoSocioNegocioUseCase: GetInfoSocioNegocioUseCase,
    private val getInfoSocioNegocioAwaitUseCase: GetInfoSocioNegocioAwaitUseCase,
    private val getAllSocioDireccionesUseCase: GetAllSocioDireccionesUseCase,
    private val insertarUnaDireccionUseCase: InsertarUnaDireccionUseCase,
    private val getAllDireccionPorTipoYCardCodeUseCase: GetAllDireccionPorTipoYCardCodeUseCase,
    private val eliminarDireccionesYContactosPorAccDocEntryUseCase: EliminarDireccionesYContactosPorAccDocEntryUseCase,
    private val getSocioContactoPorCardCodeUseCase: GetSocioContactoPorCardCodeUseCase,
    private val getSocioDireccionesPorCardCodeUseCase: GetDireccionesPorCardCodeUseCase,
    private val getSocioNegocioPorCardCodeYMigradoUseCase: GetSocioNegocioPorCardCodeYMigradoUseCase,
    private val getAllSocioNegocioConFacturasUseCase: GetAllSocioNegocioConFacturasUseCase,
    private val deleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNListUseCase: DeleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNListUseCase,
    private val getSocioInfoClientePorCardCodeUseCase: GetInfoClientePorCardCodeUseCase,
    private val deleteSocioNegocioUseCase: DeleteSocioNegocioUseCase,
    private val getAllSocioNegocioNoBloqueadosUseCase: GetAllSocioNegocioNoBloqueadosUseCase,
    private val getAllSociosPagingUseCase: GetAllSociosPagingUseCase,
    private val sendAllLeadsToWServiceUseCase: SendAllLeadsToWServiceUseCase,
    private val getCountClientesUseCase: GetCountClientesUseCase,
    private val sociosDao: ClienteSociosDao,
    private val getAllSociosForMainScreenUseCse: GetAllSociosForMainScreenUseCse,
    private val searchSociosViewUseCase: SearchSociosViewUseCase,
    private val getAllSNUseCase: GetAllSNUseCase
): ViewModel() {


    val infoSocioNegocioAwait = MutableLiveData<DoSocioNuevoAwait>()
    val insertarNuevaDireccionResponse: MutableLiveData<Boolean> = MutableLiveData()


    val isLoading = MutableLiveData<Boolean>()

    //Todos los socios con PAGING

    /*fun sociosPaging(): LiveData<PagingData<ClienteSociosEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20, maxSize = 60),
            pagingSourceFactory = { sociosDao.getAllSociosPaging("") }
        ).liveData.cachedIn(viewModelScope)
    }*/

/*    val sociosPagingFlow = Pager(
        config= PagingConfig(pageSize = 20, initialLoadSize = 20, maxSize = 100)
    ){
        sociosDao.getAllSociosPaging()
    }.flow.cachedIn(viewModelScope)*/


    private val _sociosPagingFlow = MutableStateFlow<PagingData<ClienteSociosEntity>>(PagingData.empty())
    val sociosPagingFlow: StateFlow<PagingData<ClienteSociosEntity>> = _sociosPagingFlow

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    init {
        fetchSocios()
    }

    private fun fetchSocios() {
        viewModelScope.launch {
            try {
                val pager = Pager(
                    config = PagingConfig(pageSize = 20, initialLoadSize = 20, maxSize = 100),
                    pagingSourceFactory = { SociosPagingSource(sociosDao) }
                )
                pager.flow.collectLatest { pagingData ->
                    _sociosPagingFlow.value = pagingData
                }
            } catch (e: Exception) {
                _errorLiveData.value = "Error inesperado: ${e.message}"
            }
        }
    }


    val queryFlow = MutableStateFlow("")
    fun observePagingSource(): Flow<PagingData<ClienteSociosEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 5, initialLoadSize = 5, maxSize = 20),
            pagingSourceFactory = { sociosDao.getAllSociosPagingSource() }
        ).flow.cachedIn(viewModelScope)
            .combine(queryFlow) { pagingData, query ->
                pagingData.filter { it.CardName.startsWith(query)
                }
            }
    }

   /* val sociosPaging = Pager(
        config = PagingConfig(pageSize = 10)
    ){
        getAllSociosPagingUseCase()
    }.flow.cachedIn(viewModelScope)*/


    //Bucar Socios
    val dataSearchSociosView = MutableLiveData<List<DoClienteScreen>>()
    fun searchSociosView(text: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = searchSociosViewUseCase(text)
            result.let {
                dataGetAllSociosForMainScreen.postValue(it)
                dataSearchSociosView.postValue(it)
                isLoading.postValue(false)
            }

        }
    }
    //All socios
    val dataGetAllSN = MutableLiveData<List<DoClienteScreen>>()
    fun getAllSN(){
        viewModelScope.launch {
            val result = getAllSNUseCase()
            result.let {
                dataGetAllSN.postValue(it)
            }
        }
    }

    //Todos los socios para la pantalla principal
    val dataGetAllSociosForMainScreen = MutableLiveData<List<DoClienteScreen>>()
    fun getAllSociosToMainScreen(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllSociosForMainScreenUseCse()
            result.let {
                isLoading.postValue(false)
                dataGetAllSociosForMainScreen.postValue(it)
            }
        }
    }



    //Enviar todos los leads a la web service
    val dataSendAllLeadsToWService = MutableLiveData<Boolean>()
    fun sendAllLeadsToWService(){
        viewModelScope.launch {
            val result = sendAllLeadsToWServiceUseCase()
            result.let {
                dataSendAllLeadsToWService.postValue(it)
            }
        }
    }


    //Obtener todos los socio de negocio NO BLOQUEADOS
    val dataGetAllSocioNegocioNoBloqueados = MutableLiveData<List<DoClienteSocios>>()
    fun getAllSocioNegocioNoBloqueados(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllSocioNegocioNoBloqueadosUseCase()

            result.let {
                dataGetAllSocioNegocioNoBloqueados.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

    //Obtener la cantidad total de clientes
    val dataGetCountClientes: Flow<Int> = getCountClientesUseCase.getCountClientesFlow()




    //Eliminar un socio de negocio

    val dataDeleteSocioNegocio = MutableLiveData<Boolean>()
    fun deleteSocioNegocio(cardCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = deleteSocioNegocioUseCase(cardCode)
            result.let {
                isLoading.postValue(false)
                dataDeleteSocioNegocio.postValue(result)
            }
        }
    }





    //Eliminar todas las direcciones y contactos que no tengan un socio de negocio asociado
    val dataDeleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNList = MutableLiveData<Boolean>()
    fun deleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNList(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = deleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNListUseCase()

            result.let{
                isLoading.postValue(false)
                dataDeleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNList.postValue(it)
            }
        }
    }




    //Obtener toda la informacion del CLiente por CardCode
    val dataGetInfoClientePorCardCode = MutableLiveData<DoClienteInfo>()
    fun getInfoClientePorCardCode(cardCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getSocioInfoClientePorCardCodeUseCase(cardCode)
            result.let {
                dataGetInfoClientePorCardCode.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


    //Obtener TODOS los socio de negocio MIGRADOS
    val dataGetAllSocioNegocioMigrados = MutableLiveData<List<DoClienteSocios>>()
    fun getAllSocioNegocioMigrados() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllSociosFiltradosPorMigradoUseCase("Y")

            result.let {
                dataGetAllSocioNegocioMigrados.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

    //Obtener TODOS los socio de negocio NO MIGRADOS
    val dataGetAllSocioNegocioNoMigrados = MutableLiveData<List<DoClienteSocios>>()
    fun getAllSocioNegocioNoMigrados() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllSociosFiltradosPorMigradoUseCase("N")

            result.let {
                dataGetAllSocioNegocioNoMigrados.postValue(result)
                isLoading.postValue(false)
            }
        }
    }



    val respuestaConsultaRuc = MutableLiveData<DoConsultaDocumento>()
    fun socioConsultaRuc(numeroDocumento: String, tipoDocumento: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getSocioConsultaRucUseCase(numeroDocumento = numeroDocumento, tipoDocumento = tipoDocumento)

            result.let {
                respuestaConsultaRuc.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

    // Obtener un Socio de negocio por CardCode y migrado
    val dataGetSocioNegocioPorCardCodeYMigrado = MutableLiveData<Any>()

    fun getSocioNegocioPorCardCodeYMigrado(cardCode:String, migrado:String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getSocioNegocioPorCardCodeYMigradoUseCase(cardCode, migrado)

            if (result != null){
                dataGetSocioNegocioPorCardCodeYMigrado.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


    val consultaRucFromDatabase = MutableLiveData<DoConsultaDocumento>()
    fun getConsultaRucFromDatabase(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getSocioConsultaRucFromDatabaseUseCase()

            result.let {
                consultaRucFromDatabase.postValue(result)
                isLoading.postValue(false)
            }
        }
    }



    //Direcciones ClienteSocios de Negocio
    val socioDirecciones = MutableLiveData<List<DoSocioDirecciones>>()
    fun getAllSocioDirecciones(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllSocioDireccionesUseCase()

            result.let{
                socioDirecciones.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


    fun insertSocioNegocio(doClienteSocios: DoClienteSocios ){
        viewModelScope.launch {
            insertarSocioNegocioUseCase(doClienteSocios)
        }
    }

    val insertSocioIsLoading = MutableLiveData<Boolean>()
    val dataInsertSocioNuevoAwait = MutableLiveData<DoError>()
    fun insertSocioNuevoAwait(socioNegocio: DoClienteSocios){
        viewModelScope.launch {
            insertSocioIsLoading.postValue(true)
            val result = insertarSocioNuevoAwaitUseCase(socioNegocio)
            result.let {
                insertSocioIsLoading.postValue(false)
                dataInsertSocioNuevoAwait.postValue(it)
            }
        }
    }

    val socioNuevoAwait = MutableLiveData<List<DoSocioNuevoAwait>>()
    fun getAllSocioNuevoAwait(){
        viewModelScope.launch {
            val result = getSocioNuevoAwaitUseCase()
            result.let {
                socioNuevoAwait.postValue(result)
            }
        }
    }
    //Buscar SN por CardCode
    val dataSocioNegocioPorCardCode = MutableLiveData<DoClienteSocios>()

    fun getSocioNegocioPorCardCode(cardCode: String){
        viewModelScope.launch {
            val result = getInfoSocioNegocioUseCase(cardCode)

            result.let{
                dataSocioNegocioPorCardCode.postValue(result)
            }
        }
    }

    //Buscar SocioContactos por CardCode
    val dataSocioContactoPorCardCode = MutableLiveData<List<DoSocioContactos>>()

    fun getSocioContactoPorCardCode(cardCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getSocioContactoPorCardCodeUseCase(cardCode)

            if (result != null) {
                dataSocioContactoPorCardCode.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

    //Buscar SocioDirecciones por CardCode y tipo (Fiscal o Despacho)
    val dataSocioDireccionesPorCardCodeYTipo = MutableLiveData<List<DoSocioDirecciones>>()
    fun getSocioDireccionesPorCardCodeYTipo(cardCode: String, tipo: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllDireccionPorTipoYCardCodeUseCase(cardCode, tipo)

            if(!result.isNullOrEmpty()){
                dataSocioDireccionesPorCardCodeYTipo.postValue(result)
                isLoading.postValue(false)
            }
        }
    }
    //Buscar SocioDirecciones por CardCode
    val dataSocioDireccionesPorCardCode = MutableLiveData<List<DoSocioDirecciones>>()
    fun getSocioDireccionesPorCardCode(cardCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getSocioDireccionesPorCardCodeUseCase(cardCode)
            if(!result.isNullOrEmpty()){
                dataSocioDireccionesPorCardCode.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

    fun getInfoSocioNegocioAwait(cardCode: String){
        viewModelScope.launch {
            val result = getInfoSocioNegocioAwaitUseCase(cardCode)
            if(result != null){
                infoSocioNegocioAwait.postValue(result)
            }
        }
    }

    //Insertar una nueva direccion de socio de negocio
    fun insertarUnaDireccion(doSocioDirecciones: DoSocioDirecciones){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = insertarUnaDireccionUseCase(doSocioDirecciones)
            if (result){
                isLoading.postValue(false)
                insertarNuevaDireccionResponse.postValue(result)
            }
        }
    }


    //Obtener todos los socio de negocio con pedidos
    val dataGetAllSocioNegocioConFacturas = MutableLiveData<List<DoClienteScreen>>()
    fun getAllSocioNegocioConFacturas(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllSocioNegocioConFacturasUseCase()

            result.let{
                dataGetAllSocioNegocioConFacturas.postValue(result)
                isLoading.postValue(false)
            }
        }
    }


    //Eliminar direccion de socio de negocio por CardCode
    val dataEliminarContactosYDireccionesPorAccDocEntry: MutableLiveData<Boolean> = MutableLiveData()
    fun eliminarContactosYDireccionesPorAccDocEntry(accDocEntry: String){
        viewModelScope.launch {
            val result = eliminarDireccionesYContactosPorAccDocEntryUseCase(accDocEntry)
            result.let {
                dataEliminarContactosYDireccionesPorAccDocEntry.postValue(it)
            }
        }
    }
}



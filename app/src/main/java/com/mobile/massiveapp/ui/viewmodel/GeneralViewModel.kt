package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.data.database.entities.GeneralCondicionesEntity
import com.mobile.massiveapp.data.database.entities.GeneralVendedoresEntity
import com.mobile.massiveapp.data.model.Banco
import com.mobile.massiveapp.data.model.Bases
import com.mobile.massiveapp.data.model.FormaPago
import com.mobile.massiveapp.data.model.GeneralCondiciones
import com.mobile.massiveapp.data.model.GeneralCuentasB
import com.mobile.massiveapp.data.model.GeneralDepartamentos
import com.mobile.massiveapp.data.model.GeneralDistritos
import com.mobile.massiveapp.data.model.GeneralImpuestos
import com.mobile.massiveapp.data.model.GeneralIndicadores
import com.mobile.massiveapp.data.model.GeneralPaises
import com.mobile.massiveapp.data.model.GeneralProvincias
import com.mobile.massiveapp.data.model.GeneralSocioGrupos
import com.mobile.massiveapp.data.model.GeneralVendedores
import com.mobile.massiveapp.data.model.InfoTable
import com.mobile.massiveapp.domain.general.GetAllBancosUseCase
import com.mobile.massiveapp.domain.general.GetAllBasesDisponiblesUseCase
import com.mobile.massiveapp.domain.general.GetAllConductoresUseCase
import com.mobile.massiveapp.domain.general.GetAllCuentasCUseCase
import com.mobile.massiveapp.domain.general.GetAllDepartamentosUseCase
import com.mobile.massiveapp.domain.general.GetAllDistritosUseCase
import com.mobile.massiveapp.domain.general.GetAllFormasPagoUseCase
import com.mobile.massiveapp.domain.general.GetAllGeneralCondicionesUseCase
import com.mobile.massiveapp.domain.general.GetAllGeneralCuentasBUseCase
import com.mobile.massiveapp.domain.general.GetEmpleadosDeVentaUseCase
import com.mobile.massiveapp.domain.general.GetAllGeneralGruposSociosUseCase
import com.mobile.massiveapp.domain.general.GetAllGeneralImpuestosUseCase
import com.mobile.massiveapp.domain.general.GetAllGeneralIndicadoresUseCase
import com.mobile.massiveapp.domain.general.GetAllGeneralMonedosUseCase
import com.mobile.massiveapp.domain.general.GetAllGeneralSeriesUseCase
import com.mobile.massiveapp.domain.general.GetAllGeneralVendedoresUseCase
import com.mobile.massiveapp.domain.general.GetAllPaisesUseCase
import com.mobile.massiveapp.domain.general.GetAllProvinciasUseCase
import com.mobile.massiveapp.domain.general.GetAllZonasUseCase
import com.mobile.massiveapp.domain.general.GetBaseActualUseCase
import com.mobile.massiveapp.domain.general.GetCondicionDePagoDefaultUseCase
import com.mobile.massiveapp.domain.general.GetCondicionPorGroupNumUseCase
import com.mobile.massiveapp.domain.general.GetCondicionesSocioUseCase
import com.mobile.massiveapp.domain.general.GetCuentasDefaultUseCase
import com.mobile.massiveapp.domain.general.GetDepartamentoCodePorNombreUseCase
import com.mobile.massiveapp.domain.general.GetImpuestoDefaultUseCase
import com.mobile.massiveapp.domain.general.GetInfoTablasUseCase
import com.mobile.massiveapp.domain.model.DoConductor
import com.mobile.massiveapp.domain.model.DoCuentasC
import com.mobile.massiveapp.domain.model.DoGeneralMonedas
import com.mobile.massiveapp.domain.model.DoSeriesN
import com.mobile.massiveapp.domain.model.DoZonas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val getAllPaisesUseCase: GetAllPaisesUseCase,
    private val getAllDepartamentosUseCase: GetAllDepartamentosUseCase,
    private val getAllProvinciasUseCase: GetAllProvinciasUseCase,
    private val getAllDistritosUseCase: GetAllDistritosUseCase,
    private val getEmpleadosDeVentaUseCase: GetEmpleadosDeVentaUseCase,
    private val getAllGeneralMonedosUseCase: GetAllGeneralMonedosUseCase,
    private val getAllGeneralGruposSociosUseCase: GetAllGeneralGruposSociosUseCase,
    private val getAllGeneralIndicadoresUseCase: GetAllGeneralIndicadoresUseCase,
    private val getAllGeneralCondicionesUseCase: GetAllGeneralCondicionesUseCase,
    private val getAllGeneralVendedoresUseCase: GetAllGeneralVendedoresUseCase,
    private val getDepartamentoCodePorNombreUseCase: GetDepartamentoCodePorNombreUseCase,
    private val getAllGeneralCuentasBUseCase: GetAllGeneralCuentasBUseCase,
    private val getAllGeneralImpuestosUseCase: GetAllGeneralImpuestosUseCase,
    private val getCondicionDePagoDefaultUseCase: GetCondicionDePagoDefaultUseCase,
    private val getImpuestoDefaultUseCase: GetImpuestoDefaultUseCase,
    private val getAllBasesDisponiblesUseCase: GetAllBasesDisponiblesUseCase,
    private val getInfoTablasUseCase: GetInfoTablasUseCase,
    private val getAllBancosUseCase: GetAllBancosUseCase,
    private val getCuentasDefaultUseCase: GetCuentasDefaultUseCase,
    private val getAllZonasUseCase: GetAllZonasUseCase,
    private val getCondicionesSocioUseCase: GetCondicionesSocioUseCase,
    private val getCondicionPorGroupNumUseCase: GetCondicionPorGroupNumUseCase,
    private val getBaseActualUseCase: GetBaseActualUseCase,
    private val getAllGeneralSeriesUseCase: GetAllGeneralSeriesUseCase,
    private val getAllCuentasCUseCase: GetAllCuentasCUseCase,
    private val getAllConductoresUseCase: GetAllConductoresUseCase,
    private val getAllFormasPagoUseCase: GetAllFormasPagoUseCase
): ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    //Cuentas bancarias por defecto (Usuario)
    val dataCuentaTransferenciaDefault: Flow<String> = getCuentasDefaultUseCase.getCuentaTransferenciaDetault()

    val dataCuentaCashDefault: Flow<String> = getCuentasDefaultUseCase.getCuentaCashDefault()

    val dataCuentaChequeDefault: Flow<String> = getCuentasDefaultUseCase.getCuentaChequeDefault()

    val dataCuentaDepositoDefault: Flow<String> = getCuentasDefaultUseCase.getCuentaDepositoDefault()


    val dataGetAllConductores = MutableLiveData<List<DoConductor>>()
    fun getAllConductores(){
        viewModelScope.launch {
            val result = getAllConductoresUseCase()
            result.let {
                dataGetAllConductores.postValue(it)
            }
        }
    }

    //Formas de Pago
    val dataGetAllFormasPago = MutableLiveData<List<FormaPago>>()
    fun getAllFormasPago(){
        viewModelScope.launch {

        }
    }


    //Obtener base actual
    val dataGetBaseActual = MutableLiveData<String>()
    fun getBaseActual(){
        viewModelScope.launch {
            val result = getBaseActualUseCase()
            result.let {
                dataGetBaseActual.postValue(it)
            }
        }
    }
    val dataGetAllCuentasC = MutableLiveData<List<DoCuentasC>>()
    fun getAllCuentasC(){
        viewModelScope.launch {
            val result = getAllCuentasCUseCase()
            result.let {
                dataGetAllCuentasC.postValue(it)
            }
        }
    }

    val dataGetAllSeriesN = MutableLiveData<List<DoSeriesN>>()
    fun getAllSeriesN(){
        viewModelScope.launch {
            val result = getAllGeneralSeriesUseCase()
            result.let {
                dataGetAllSeriesN.postValue(it)
            }
        }
    }


    val dataGetAllBancos = MutableLiveData<List<Banco>>()
    fun getAllBancos(){
        viewModelScope.launch {
            val result = getAllBancosUseCase()
            result.let {
                dataGetAllBancos.postValue(it)
            }
        }
    }

    // Trae todas las zonas
    val dataGetAllZonas = MutableLiveData<List<DoZonas>>()
    fun getAllZonas(){
        viewModelScope.launch {
            val result = getAllZonasUseCase()
            result.let {
                dataGetAllZonas.postValue(it)
            }
        }
    }

        //Condicion de pago por cliente
    val dataGetCondicionesSocio = MutableLiveData<List<GeneralCondicionesEntity>>()
    fun getCondicionesSocio(cardCode: String){
        viewModelScope.launch {
            val result = getCondicionesSocioUseCase(cardCode)
            result. let {
                dataGetCondicionesSocio.postValue(it)
            }
        }
    }

        //Condicion de pago por groupNum
    val dataGetCondicionPorGroupNum = MutableLiveData<GeneralCondicionesEntity>()
    fun getCondicionPorGroupNum(groupNum: Int){
        viewModelScope.launch {
            val result = getCondicionPorGroupNumUseCase(groupNum)
            result.let {
                dataGetCondicionPorGroupNum.postValue(it)
            }
        }
    }


        //Trae toda la info de las tablas
    val dataGetInfoTablas = MutableLiveData<List<InfoTable>>()
    fun getInfoTablas(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getInfoTablasUseCase()
            result.let {
                dataGetInfoTablas.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todas las bases disponibles
    val dataGetAllBasesDisponibles = MutableLiveData<List<Bases>>()
    val isLoadingBases = MutableLiveData<Boolean>()
    fun getAllBasesDisponibles(ipPublica: String, puerto: String){
        viewModelScope.launch {
            isLoadingBases.postValue(true)
            val result = getAllBasesDisponiblesUseCase(ipPublica, puerto)
            result.let{
                dataGetAllBasesDisponibles.postValue(it)
                isLoadingBases.postValue(false)
            }
        }
    }



        //Traer todos los paises
    val dataGetAllPaises = MutableLiveData<List<GeneralPaises>>()

    fun getAllPaises(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllPaisesUseCase()

            result.let{
                dataGetAllPaises.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todos los departamentos
    val dataGetAllDepartamentos = MutableLiveData<List<GeneralDepartamentos>>()

    fun getAllDepartamentos(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllDepartamentosUseCase()

            result.let{
                dataGetAllDepartamentos.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todas las provincias
    val dataGetAllProvincias = MutableLiveData<List<GeneralProvincias>>()

    fun getAllProvincias(code: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllProvinciasUseCase(code)

            result.let{
                dataGetAllProvincias.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todos los distritos
    val dataGetAllDistritos = MutableLiveData<List<GeneralDistritos>>()

    fun getAllDistritos(code:String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllDistritosUseCase(code)

            result.let{
                dataGetAllDistritos.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todos los empleados
    val dataGetEmpleadoDeVemtas = MutableLiveData<GeneralVendedoresEntity>()

    fun getEmpleadoDeVentas(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getEmpleadosDeVentaUseCase()

            result.let{
                dataGetEmpleadoDeVemtas.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todas las monedas
    val dataGetAllGeneralMonedas = MutableLiveData<List<DoGeneralMonedas>>()

    fun getAllGeneralMonedas(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllGeneralMonedosUseCase()

            result.let{
                dataGetAllGeneralMonedas.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todos los grupos de socios
    val dataGetAllGeneralGruposSocios = MutableLiveData<List<GeneralSocioGrupos>>()

    fun getAllGeneralGruposSocios(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllGeneralGruposSociosUseCase()

            result.let{
                dataGetAllGeneralGruposSocios.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todos los indicadores
    val dataGetAllGeneralIndicadores = MutableLiveData<List<GeneralIndicadores>>()
    fun getAllGeneralIndicadores(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllGeneralIndicadoresUseCase()

            result.let{
                dataGetAllGeneralIndicadores.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todas las condiciones de pago
    val dataGetAllGeneralCondiciones = MutableLiveData<List<GeneralCondiciones>>()

    fun getAllGeneralCondiciones(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllGeneralCondicionesUseCase()

            result.let{
                dataGetAllGeneralCondiciones.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer la condicion de pago por default
    val dataGetCondicionDePagoDefault = MutableLiveData<GeneralCondiciones>()
    fun getCondicionDePagoDefault(cardCode: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getCondicionDePagoDefaultUseCase(cardCode)

            result.let{
                dataGetCondicionDePagoDefault.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Traer todos los vendedores
    val dataGetAllGeneralVendedores = MutableLiveData<List<GeneralVendedores>>()
    fun getAllGeneralVendedores(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllGeneralVendedoresUseCase()

            result.let{
                dataGetAllGeneralVendedores.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Obtener codigo por nombre de departamento
    val dataGetDepartamentoCodePorNombre = MutableLiveData<String>()

    fun getDepartamentoCodePorNombre(nombre:String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getDepartamentoCodePorNombreUseCase(nombre)

            result.let{
                dataGetDepartamentoCodePorNombre.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Obtener todos las cuentas bancarias para depositar
    val dataGetAllGeneralCuentasB = MutableLiveData<List<GeneralCuentasB>>()

    fun getAllGeneralCuentasB(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllGeneralCuentasBUseCase()

            result.let{
                dataGetAllGeneralCuentasB.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Obtener todos los impuestos
    val dataGetAllGeneralImpuestos = MutableLiveData<List<GeneralImpuestos>>()
    fun getAllGeneralImpuestos(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getAllGeneralImpuestosUseCase()

            result.let{
                dataGetAllGeneralImpuestos.postValue(it)
                isLoading.postValue(false)
            }
        }
    }

        //Obtener el impuesto por default
    val dataGetImpuestoDefault = MutableLiveData<GeneralImpuestos>()

    fun getImpuestoDefault(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getImpuestoDefaultUseCase()

            result.let{
                dataGetImpuestoDefault.postValue(it)
                isLoading.postValue(false)
            }
        }
    }


        //Obteber todas las bases disponibles



}
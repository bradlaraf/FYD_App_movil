package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.BasesDao
import com.mobile.massiveapp.data.database.dao.GeneralCentrosCDao
import com.mobile.massiveapp.data.database.dao.GeneralCondicionesDao
import com.mobile.massiveapp.data.database.dao.GeneralCuentasBDao
import com.mobile.massiveapp.data.database.dao.GeneralDepartamentosDao
import com.mobile.massiveapp.data.database.dao.GeneralDistritosDao
import com.mobile.massiveapp.data.database.dao.GeneralEmpleadosDao
import com.mobile.massiveapp.data.database.dao.GeneralImpuestosDao
import com.mobile.massiveapp.data.database.dao.GeneralIndicadoresDao
import com.mobile.massiveapp.data.database.dao.GeneralMonedasDao
import com.mobile.massiveapp.data.database.dao.GeneralPaisesDao
import com.mobile.massiveapp.data.database.dao.GeneralProvinciasDao
import com.mobile.massiveapp.data.database.dao.GeneralProyectosDao
import com.mobile.massiveapp.data.database.dao.GeneralSocioGruposDao
import com.mobile.massiveapp.data.database.dao.GeneralVendedoresDao
import com.mobile.massiveapp.data.database.entities.BasesEntity
import com.mobile.massiveapp.data.database.entities.toModel
import com.mobile.massiveapp.data.model.GeneralCondiciones
import com.mobile.massiveapp.data.model.GeneralImpuestos
import com.mobile.massiveapp.data.model.GeneralVendedores
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoGeneralMonedas
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject
class GeneralRepository @Inject constructor(
    private val generalEmpleadosDao: GeneralEmpleadosDao,
    private val generalMonedasDao: GeneralMonedasDao,
    private val generalCentrosCDao: GeneralCentrosCDao,
    private val generalCondicionesDao: GeneralCondicionesDao,
    private val generalDepartamentosDao: GeneralDepartamentosDao,
    private val generalDistritosDao: GeneralDistritosDao,
    private val generalImpuestosDao: GeneralImpuestosDao,
    private val generalIndicadoresDao: GeneralIndicadoresDao,
    private val generalPaisesDao: GeneralPaisesDao,
    private val generalProvinciasDao: GeneralProvinciasDao,
    private val generalProyectosDao: GeneralProyectosDao,
    private val generalSocioGruposDao: GeneralSocioGruposDao,
    private val generalVendedoresDao: GeneralVendedoresDao,
    private val generalCuentasBDao: GeneralCuentasBDao,
    private val basesDao: BasesDao,
    private val maestrosService: DatosMaestrosService
) {

        //Obtener todos los paises
    suspend fun getAllPaises() =
        try {
            generalPaisesDao.getAll().map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener todos los departamentos
    suspend fun getAllDepartamentos() =
        try {
            generalDepartamentosDao.getAll().map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener todos los distritos
    suspend fun getAllDistritos(code:String) =
        try {
            generalDistritosDao.getAllByCode(code).map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener todos las provincias
    suspend fun getAllProvincias(code:String) =
        try {
            generalProvinciasDao.getAllByCode(code).map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener todos los empleados
    suspend fun getEmpleadoDeVentas() =
            generalEmpleadosDao.getEmpleadoDeVentas()

        //Obtener todas las monedas
    suspend fun getAllGeneralMonedas():List<DoGeneralMonedas> =
        try {
            generalMonedasDao.getAllGeneralMonedas().map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }


        //Obtener todos los grupos de socios
    suspend fun getAllGeneralGruposSocios() =
        try {
            generalSocioGruposDao.getAll().map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener todos los indicadores
    suspend fun getAllGeneralIndicadores() =
        try {
            generalIndicadoresDao.getAll().map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener todos las condiciones de pago
    suspend fun getAllGeneralCondiciones() =
        try {
            generalCondicionesDao.getAll().map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener condicion de pago por default
    suspend fun getCondicionDePagoDefault(cardCode: String) =
        try {
            generalCondicionesDao.getCondicionDePagoDefault(cardCode).toModel()
        } catch (e: Exception) {
            GeneralCondiciones()
        }

        //Obtener todos los vendedores
    suspend fun getAllGeneralVendedores() =
        try {
            generalVendedoresDao.getAll().map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener vendedor por default
    suspend fun getVendedorDefault() =
        try {
            generalVendedoresDao.getVendedorDefault().toModel()
        } catch (e: Exception) {
            GeneralVendedores()
        }

        //Obtener departamento code por nombre
    suspend fun getDepartamentoCodePorNombre(nombre:String):String =
        try{
            generalDepartamentosDao.getDepartamentoCodePorNombre(nombre)
        } catch (e: Exception){
            ""
        }

        //Obtener todas las cuentas B
    suspend fun getAllGeneralCuentasB() =
        try {
            generalCuentasBDao.getAll().map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener todos los impuestos
    suspend fun getAllGeneralImpuestos() =
        try {
            generalImpuestosDao.getAll().map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener impuesto por default
    suspend fun getImpuestoDefault() =
        try {
            generalImpuestosDao.getImpuestoDefault().toModel()
        } catch (e: Exception) {
            GeneralImpuestos()
        }


    /***********BASES**********/

        //Obtener todos las bases de datos disponibles
    suspend fun getAllBasesDisponibles() =
        try {
            basesDao.getAll().map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }

        //Obtener todoas las bases de datos de la api
    suspend fun getAllBasesDisponiblesFromApi(usuario: DoUsuario, configuracion: DoConfiguracion, url: String, timeOut: Long) =
        try {
            maestrosService.getDatoMaestro("Bases", configuracion, usuario, url, timeOut)
        } catch (e: Exception){
            emptyList()
        }

        //Guardar todas las bases de datos en la base de datos
    suspend fun saveAllBasesDisponibles(bases: List<BasesEntity>) =
        try {
            basesDao.insertAllData(bases)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

        //Eliminar todas las bases de la Bd
    suspend fun clearAllBases() =
        try {
            basesDao.clearAll()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

}


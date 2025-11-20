package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.ClientePagosDao
import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.database.dao.ConfiguracionDao
import com.mobile.massiveapp.data.database.entities.ConfiguracionEntity
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class ConfiguracionRepository @Inject constructor(
    private val configuracionDao: ConfiguracionDao,
    private val clientePedidosDao: ClientePedidosDao,
    private val clientePagosDetalleDao: ClientePagosDetalleDao,
    private val clientePagosDao: ClientePagosDao,
    private val clientePedidosDetalleDao: ClientePedidosDetalleDao
) {


        //Guardar una configuracion
    suspend fun saveConfiguracion(configuracion: ConfiguracionEntity) =
        try {
            configuracionDao.insertAll(configuracion)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }


        //Obtener la configuracion actual
    suspend fun getConfiguracion(): DoConfiguracion =
        try {
            configuracionDao.getAll().toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            DoConfiguracion()
        }


        //Eliminar la configuracion actual
    suspend fun clearConfiguracion() =
        try {
            configuracionDao.clearAll()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }


        //Eliminar todos los pagos que no sean del dia actual
    suspend fun clearPagosFueraDeFechaActual(fechaActual: String) =
        try {
            clientePagosDao.deleteAllPagosFueraDeFechaActual(fechaActual)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

        //Eliminar todos los pedidos que no sean del dia actual
    suspend fun clearPedidosFueraDeFechaActual(fechaActual: String) =
        try {
            clientePedidosDao.deleteAllPedidosFueraDeFechaActual(fechaActual)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }



}
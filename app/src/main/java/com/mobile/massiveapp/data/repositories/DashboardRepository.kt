package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.DashboardDao
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val dashboardDao: DashboardDao
){


        //PEDIDOS
    fun getTotalAcumuladoPedidos(fechaActual: String) =
        dashboardDao.getTotalAcumuladoPedidos(fechaActual)


    fun getTotalPedidos(fechaActual: String) =
            dashboardDao.getTotalPedidos(fechaActual)




        //PAGOS
     fun getTotalAcumuladoPagos(fechaActual: String) =
            dashboardDao.getTotalAcumuladoPagos(fechaActual)


    fun getTotalPagos(fechaActual: String) =
            dashboardDao.getTotalPagos(fechaActual)



        //FACTURAS
    fun getTotalFacturas() =
            dashboardDao.getTotalFacturas()

    fun getTotalAcumuladoFacturas() =
            dashboardDao.getTotalAcumuladoFacturas()


}

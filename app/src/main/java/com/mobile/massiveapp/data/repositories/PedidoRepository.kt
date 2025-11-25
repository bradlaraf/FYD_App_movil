package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.database.entities.ClientePedidosDetalleEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.model.ClientePedidos
import com.mobile.massiveapp.data.model.toEntity
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.network.PedidoService
import com.mobile.massiveapp.domain.model.DoClientePedido
import com.mobile.massiveapp.domain.model.toDomain
import com.mobile.massiveapp.ui.view.util.getFechaActual
import javax.inject.Inject

class PedidoRepository @Inject constructor(
    private val clientePedidoDetalleDao: ClientePedidosDetalleDao,
    private val clientePedidosDao: ClientePedidosDao,
    private val pedidoService: PedidoService
){

        //Obtener todos AccDocEntry pedidos detalle
    suspend fun getAllAccDocEntryPedidosDetalleSinCabecera(): List<ClientePedidoDetalle> =
        try {
            clientePedidoDetalleDao.getAllAccDocEntryPedidosDetalleSinCabecera().map { it.toModel() }
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }


        //Agregar el onCommited al articulo del Pedido Detalle
    suspend fun updateIsCommitedArticulo(cantidad: Double, itemCode: String): Boolean =
        try {
            clientePedidoDetalleDao.updateIsCommitedArticulo(cantidad, itemCode)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }




        //Guardar en la BD el detalle del pedido
    suspend fun savePedidoDetalle(clientePedidoDetalle: ClientePedidoDetalle): Boolean{
        return try{
            val request = clientePedidoDetalle.toEntity()
            clientePedidoDetalleDao.insertUnPedidoDetalle(request)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    //Guardar en la BD el detalle del pedido
    suspend fun savePedidoDetalle(clientePedidoDetalle: List<ClientePedidoDetalle>): Boolean{
        return try{
            clientePedidoDetalle.map {
                clientePedidoDetalleDao.insertUnPedidoDetalle(it.toDatabase())
            }
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

        //Guardar un pedido en la BD
    suspend fun savePedidoCabecera(clientePedido: ClientePedidos): Boolean{
        return try{
            val pedido = clientePedido.toEntity()
            clientePedidosDao.insertUnPedido(pedido)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    //Pedido  sugerido
    suspend fun getPedidoSugeridoFromApi(cardCode: String): List<ClientePedidoDetalle>{
        return try{
            pedidoService.getPedidoSugerido(cardCode)
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }

    //Obtener el detalle del pedido por el accDocEntry
    suspend fun getAllPedidoDetallePorAccDocEntry(accDocEntry: String):List<ClientePedidoDetalle> =
        try {
            val detallePedido:List<ClientePedidosDetalleEntity> = clientePedidoDetalleDao.getAllPedidoDetallePorAccDocEntry(accDocEntry)
            val result = detallePedido.map { it.toModel() }
            result
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

        //Obtener un pedido detalle por el accDocEntry y LineNum
    suspend fun getUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: Int): ClientePedidoDetalle =
        try {
            val pedidoDetalle = clientePedidoDetalleDao.getUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry, lineNum)
            pedidoDetalle.toModel()
        } catch (e: Exception){
            e.printStackTrace()
            ClientePedidoDetalle()
        }


        //Obtener todos los pedidos detalle
    suspend fun getAllPedidoDetalle(): List<ClientePedidoDetalle> =
        try {
            val pedidosDetalle = clientePedidoDetalleDao.getAll()
            pedidosDetalle.map { it.toModel() }
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }


    //Obtener todos los pedidos del cliente
    suspend fun getAllPedidosCliente(): List<DoClientePedido> =
        try {
            val pedidos = clientePedidosDao.getAll()
            pedidos.map { it.toDomain() }
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    //Todos los pedidos del dia
    suspend fun getAllPedidosDelDia(): List<DoClientePedido> =
        try {
            val pedidos = clientePedidosDao.getAll(getFechaActual())
            pedidos.map { it.toDomain() }
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    //Obtener todos los pedidos NO MIGRADOS
    suspend fun getAllPedidosNoMigrados() =
        try {
            val pedidos = clientePedidosDao.getAllPedidosSinMigrar()
            pedidos.map { it.toDomain() }
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    //Obtener un pedido por el accDocEntry
    suspend fun getPedidoPorAccDocEntry(accDocEntry: String): ClientePedidos =
        try {
            val pedido = clientePedidosDao.getPedidoPorAccDocEntry(accDocEntry)
            pedido.toModel(emptyList())
        } catch (e: Exception){
            e.printStackTrace()
            throw Exception("No se pudo obtener el pedido por el AccDocEntry")
        }

    //Obtener todos los pedidos por CardCode
    suspend fun getAllPedidosPorCardCode(cardCode: String): List<ClientePedidos> =
        try {
            val pedidos = clientePedidosDao.getAllPedidosPorCardCode(cardCode)
            pedidos.map { it.toModel(emptyList()) }
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

        //Eliminar todos los detalles del pedido por el accDocEntry
    suspend fun deleteAllPedidoDetallePorAccDocEntry(accDocEntry: String): Boolean{
        return try {
            clientePedidoDetalleDao.deleteAllPedidoDetallePorAccDocEntry(accDocEntry)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteAllPedidoDetalleDuplicados(accDocEntry: String) =
        try {
            clientePedidoDetalleDao.deleteAllPedidoDetalleDuplicados(accDocEntry)
            true
        } catch (e:Exception){
            false
        }

        //Eliminar un pedido detalle por el AccDocEntry y LineNum
    suspend fun deleteUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: Int): Boolean{
        return try {
            clientePedidoDetalleDao.deleteUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry, lineNum)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }
}
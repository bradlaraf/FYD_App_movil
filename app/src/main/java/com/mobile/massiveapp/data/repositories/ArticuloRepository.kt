package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.ArticuloAlmacenesDao
import com.mobile.massiveapp.data.database.dao.ArticuloCantidadesDao
import com.mobile.massiveapp.data.database.dao.ArticuloDao
import com.mobile.massiveapp.data.database.dao.ArticuloGruposDao
import com.mobile.massiveapp.data.database.dao.ArticuloGruposUMDetalleDao
import com.mobile.massiveapp.data.database.dao.ArticuloListaPreciosDao
import com.mobile.massiveapp.data.database.dao.ArticuloPreciosDao
import com.mobile.massiveapp.data.database.dao.ArticuloUnidadesDao
import com.mobile.massiveapp.data.database.entities.ArticuloAlmacenesEntity
import com.mobile.massiveapp.data.database.entities.ArticuloEntity
import com.mobile.massiveapp.data.database.entities.ArticuloListaPreciosEntity
import com.mobile.massiveapp.data.network.ArticuloService
import com.mobile.massiveapp.domain.model.DoArticulo
import com.mobile.massiveapp.domain.model.DoArticuloAlmacenes
import com.mobile.massiveapp.domain.model.DoArticuloCantidades
import com.mobile.massiveapp.domain.model.DoArticuloGruposUMDetalle
import com.mobile.massiveapp.domain.model.DoArticuloInfo
import com.mobile.massiveapp.domain.model.DoArticuloListaPrecios
import com.mobile.massiveapp.domain.model.DoArticuloPedidoInfo
import com.mobile.massiveapp.domain.model.DoArticuloPrecioYNombreLista
import com.mobile.massiveapp.domain.model.DoArticuloPrecios
import com.mobile.massiveapp.domain.model.DoArticuloUnidades
import com.mobile.massiveapp.domain.model.DoUnidadMedidaInfo
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class ArticuloRepository @Inject constructor(
       private val api:ArticuloService,
       private val articuloDao: ArticuloDao,
       private val articuloListaPreciosDao: ArticuloListaPreciosDao,
       private val articuloPreciosDao: ArticuloPreciosDao,
       private val articuloAlmacenesDao: ArticuloAlmacenesDao,
       private val articuloCantidadesDao: ArticuloCantidadesDao,
       private val articuloUnidadesDao: ArticuloUnidadesDao,
       private val articuloGruposUMDetalleDao: ArticuloGruposUMDetalleDao,
       private val articuloGruposDao: ArticuloGruposDao
) {

    suspend fun getOnHandPorCardCode(itemCode: String): Double =
        try {
            articuloDao.getOnHandPorCardCode(itemCode)
        } catch (e: Exception){
            e.printStackTrace()
            0.0
        }
     suspend fun getAllArticulosFromApi(): List<DoArticulo> {
        val response = api.getArticulos()
        return response.map { it.toDomain() }
    }

    suspend fun getAllArticulosFromDatabase(): List<DoArticulo> {
        val response: List<ArticuloEntity> = articuloDao.getAllArticulos()
        return response.map { it.toDomain() }
    }

    suspend fun getAllArticulosPedido() =
        try {
            articuloDao.getAllArticulosPedidos()
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }

        //Articulo Unidades de medida
    suspend fun getAllArticuloUnidadesPorUomEntry(uomEntry: Int) =
        try {
            val response = articuloUnidadesDao.getAllArticuloUnidadesPorUomEntry(uomEntry)
            response
        } catch (e: Exception) {
            emptyList()
        }


         //Obtener la Unidad de Medida por UomCode
    suspend fun getArticuloUnidadMedidaPorUomCode(uomCode: String): DoArticuloUnidades =
        try {
            val response = articuloUnidadesDao.getArticuloUnidadMedidaPorUomCode(uomCode)
            response
        } catch (e: Exception) {
            DoArticuloUnidades()
        }

        //Obtener la Grupo de Unidad de Medida Detalle
    suspend fun getArticuloUnidadMedidaDetallePorUomEntry(lineNum: Int, ugpEntry: Int):DoArticuloGruposUMDetalle =
        try {
            val response = articuloGruposUMDetalleDao.getArticuloGruposUMDetallePorUomEntry(lineNum, ugpEntry)
            response.toDomain()
        } catch (e: Exception) {
            DoArticuloGruposUMDetalle()
        }

    suspend fun getArticuloGrupoUMPorUgpEntry(ugpEntry: String): String =
        try {
            articuloUnidadesDao.getArticuloGrupoUMPorUgpEntry(ugpEntry)
        } catch (e: Exception) {
            ""
        }


        //Obtener la Grupo de Unidad de Medida
    suspend fun getArticuloUnidadMedidaPorGrupUnidadMedida(itemCode: String): List<DoArticuloUnidades> =
        try {
            val response = articuloUnidadesDao.getArticuloUnidadesPorItemCode(itemCode)
            response.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }

    //Articulo Lista de Precios--------------------


    suspend fun getAllArticulosListaPreciosFromDatabase(): List<DoArticuloListaPrecios> {
        val response: List<ArticuloListaPreciosEntity> =
            articuloListaPreciosDao.getAllArticuloListaPrecios()
        return response.map { it.toDomain() }
    }


    suspend fun getAllArticuloPreciosPorItemCode(itemCode: String): List<DoArticuloPrecios> =
        try {
            val response = articuloPreciosDao.getAllArticuloPreciosPorItemCode(itemCode)
            response
        } catch (e: Exception) {
            emptyList()
        }


    //Articulo Precios y Nombre de Lista-----------------
    suspend fun getArticuloPreciosYNombreLista():List<DoArticuloPrecioYNombreLista>{
        val response:List<DoArticuloPrecioYNombreLista> = articuloListaPreciosDao.getArticuloPrecioYNombreLista()
        return response
    }

        //Articulo por CardCode
    suspend fun getArticuloPorItemCode(itemCode: String):DoArticulo =
        try {
            val response:ArticuloEntity = articuloDao.getArticuloPorItemCode(itemCode)
            response.toDomain()
        } catch (e: Exception) {
            DoArticulo()
        }



        //Articulo Pedido Info Con Unidad de Medida
    suspend fun getArticuloInfoPedidoConUnidadMedida(itemCode: String):DoArticuloPedidoInfo =
        try {
            articuloDao.getArticuloInfoPedidoConUnidadMedida(itemCode)
        } catch (e: Exception) {
            e.printStackTrace()
            DoArticuloPedidoInfo()
        }

        //Articulo Pedido Info Sin Unidad de Medida
    suspend fun getArticuloInfoPedidoSinUnidadMedida(itemCode: String):DoArticuloPedidoInfo =
        try {
            articuloDao.getArticuloInfoPedidoSinUnidadMedida(itemCode)
        } catch (e: Exception) {
            e.printStackTrace()
            DoArticuloPedidoInfo()
        }

    suspend fun getArticuloGrupoPorItmsGrpCod(itmsGrpCod: Int): String =
        try {
            val response = articuloGruposDao.getArticuloGrupoPorItmsGrpCod(itmsGrpCod)
            response
        } catch (e: Exception) {
            ""
        }








        //Obtener el GRUPO DE UNIDAD DE MEDIDA por ItemCode
    suspend fun getArticuloUnidadMedida(itemCode: String):String =
        try {
            val response = articuloDao.getArticuloUnidadMedida(itemCode)
            response
        } catch (e: Exception) {
            ""
        }

        //Articulo Info
    suspend fun getArticuloInfoConUnidadDeMedida(itemCode: String): DoArticuloInfo =
        try {
            articuloDao.getArticuloInfoConUnidadDeMedida(itemCode)
        } catch (e: Exception) {
            e.printStackTrace()
            DoArticuloInfo()
        }


        //Trae todos los ArticulosAlmacenes de la BD local
    suspend fun getAllArticuloAlmacenesFromDatabase(): List<DoArticuloAlmacenes> {
        val response: List<ArticuloAlmacenesEntity> = articuloAlmacenesDao.getAllArticuloAlmacenes()
        return response.map {it.toDomain()}
    }



        //Trae un articulo precio por itemcode y pricelist
    suspend fun getArticuloPrecioPorItemCodeYPriceList(itemCode: String, priceList: Int): DoArticuloPrecios =
        try {
            val response = articuloPreciosDao.getArticuloPrecioPorItemCodeYPriceList(itemCode, priceList)
            response.toDomain()
        } catch (e: Exception) {
            DoArticuloPrecios()
        }

    suspend fun getArticuloPrecioParaPedido(itemCode: String, priceList: Int, unidadMedida: String) =
        try {
            articuloDao.getArticuloPrecioPedido(itemCode, unidadMedida, priceList)
        } catch (e: Exception){
            DoUnidadMedidaInfo()
        }

    suspend fun getArticuloCantidadParaPedido(itemCode: String, unidadMedida: String, whsCode: String) =
        try {
            articuloCantidadesDao.getArticuloCantidadPedido(itemCode, unidadMedida, whsCode)
        } catch (e:Exception){
            0.0
        }


        //Trae un articulo cantidad por itemcode y whsCode
    suspend fun getArticuloCantidadPorItemCodeYWhsCode(itemCode: String, whsCode: String): DoArticuloCantidades =
        try {
            val response = articuloCantidadesDao.getArticuloCantidadPorItemCodeYWhsCode(itemCode, whsCode)
            response.toDomain()
        } catch (e: Exception) {
            DoArticuloCantidades()
        }

        //Obtiene el precio de un articulo por lista de precio y unidad de medida
    suspend fun getPrecioPorListaDePrecioYUnidadDeMedida(itemCode: String, listaDePrecio: Int, unidadDeMedida: String): DoUnidadMedidaInfo =
        try {
            val response = articuloPreciosDao.getPrecioPorArticuloListaDePrecioYUnidadDeMedida(itemCode, listaDePrecio, unidadDeMedida)
            response
        } catch (e: Exception) {
            DoUnidadMedidaInfo()
        }





}
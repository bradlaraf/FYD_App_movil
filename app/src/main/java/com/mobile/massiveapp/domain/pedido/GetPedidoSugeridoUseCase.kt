package com.mobile.massiveapp.domain.pedido


import com.mobile.massiveapp.MassiveApp.Companion.prefsApp
import com.mobile.massiveapp.MassiveApp.Companion.prefsPedido
import com.mobile.massiveapp.data.database.dao.ArticuloDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.dao.UsuarioDao
import com.mobile.massiveapp.data.repositories.PedidoRepository
import com.mobile.massiveapp.ui.view.util.agregarDetalleDePedido
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import javax.inject.Inject

class GetPedidoSugeridoUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository,
    private val pedidosDao: ClientePedidosDao,
    private val articulosDao: ArticuloDao,
    private val usuarioDao: UsuarioDao,
    private val pedidosDetalleDao: ClientePedidosDetalleDao,
    private val clienteDao: ClienteSociosDao
) {
    suspend operator fun invoke(cardCode: String): Boolean =
        try {
            val usuario = usuarioDao.getAll()
            val pedidosSugeridos = pedidoRepository.getPedidoSugeridoFromApi(cardCode)

            pedidosSugeridos.forEach { pedidoSugerido ->
                val articulo = articulosDao.getArticuloInfoPedidoConUnidadMedida(itemCode = pedidoSugerido.ItemCode)
                val precioFYD = pedidosDao.obtenerPrecioFinal(articulo = pedidoSugerido.ItemCode, cardCode = cardCode)
                val listNum = clienteDao.getClienteSocioPorCardCode(cardCode).ListNum

                val detalle = agregarDetalleDePedido(
                    usuario =               prefsApp.getUserCode(),
                    accDocEntry =           prefsPedido.getAccDocEntry(),
                    codigo =                pedidoSugerido.ItemCode,
                    nombre =                pedidoSugerido.ItemName.replace("\n", " "),
                    unidadMedida =          articulo.UgpName,
                    cantidad =              pedidoSugerido.CantDoc.toDouble(),
                    grupoUM =               articulo.UomCode,
                    precio =                precioFYD?.precioFinal?:0.0,
                    precioBruto =           precioFYD?.precioBruto?:0.0,
                    precioAftVat =          precioFYD?.precioBruto?:0.0,
                    precioLP =              precioFYD?.precioUnitario?:0.0,
                    porcentajeDescuento =   precioFYD?.porcentajeDescuento?:0.0,
                    total =                 (precioFYD?.precioFinal?:0.0 * pedidoSugerido.CantDoc).format(2),
                    lineNum =               pedidosDetalleDao.getLineNum(prefsPedido.getAccDocEntry()),
                    fechaActual =           getFechaActual(),
                    horaActual =            getHoraActual(),
                    impuesto =              0.0,
                    codigoImpuesto =        usuario.DefaultTaxCode,
                    listaPrecios =          listNum,
                    codigoAlmacen =         usuario.DefaultWarehouse,
                    uomEntry =              articulo.UomEntry,
                )

                if (pedidoSugerido.Tipo == "FRECUENTE"){
                    detalle.OcrCode2 = "Y"
                }

                if (pedidoSugerido.Tipo == "ULTPED"){
                    detalle.OcrCode = "Y"
                }

                pedidoRepository.savePedidoDetalle(detalle)

            }
            true

        } catch (e:Exception){
                false
        }
}

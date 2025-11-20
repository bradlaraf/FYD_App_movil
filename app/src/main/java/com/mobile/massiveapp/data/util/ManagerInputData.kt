package com.mobile.massiveapp.data.util

import com.mobile.massiveapp.data.database.dao.ArticuloAlmacenesDao
import com.mobile.massiveapp.data.database.dao.ArticuloCantidadesDao
import com.mobile.massiveapp.data.database.dao.ArticuloDao
import com.mobile.massiveapp.data.database.dao.ArticuloFabricantesDao
import com.mobile.massiveapp.data.database.dao.ArticuloGruposDao
import com.mobile.massiveapp.data.database.dao.ArticuloGruposUMDao
import com.mobile.massiveapp.data.database.dao.ArticuloGruposUMDetalleDao
import com.mobile.massiveapp.data.database.dao.ArticuloListaPreciosDao
import com.mobile.massiveapp.data.database.dao.ArticuloLocalidadesDao
import com.mobile.massiveapp.data.database.dao.ArticuloPreciosDao
import com.mobile.massiveapp.data.database.dao.ArticuloUnidadesDao
import com.mobile.massiveapp.data.database.dao.BancoDao
import com.mobile.massiveapp.data.database.dao.BaseDao
import com.mobile.massiveapp.data.database.dao.BasesDao
import com.mobile.massiveapp.data.database.dao.ClienteFacturaDetalleDao
import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.database.dao.ClientePagosDao
import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.dao.ConfigurarUsuarioDao
import com.mobile.massiveapp.data.database.dao.CuentasCDao
import com.mobile.massiveapp.data.database.dao.GeneralActividadesEDao
import com.mobile.massiveapp.data.database.dao.GeneralCentrosCDao
import com.mobile.massiveapp.data.database.dao.GeneralCondicionesDao
import com.mobile.massiveapp.data.database.dao.GeneralCuentasBDao
import com.mobile.massiveapp.data.database.dao.GeneralDepartamentosDao
import com.mobile.massiveapp.data.database.dao.GeneralDimensionesDao
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
import com.mobile.massiveapp.data.database.dao.GeneralZonasDao
import com.mobile.massiveapp.data.database.dao.SeriesNDao
import com.mobile.massiveapp.data.database.dao.SociedadDao
import com.mobile.massiveapp.data.database.dao.SocioContactosDao
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.dao.UsuarioAlmacenesDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoArticuloDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoSociosDao
import com.mobile.massiveapp.data.database.dao.UsuarioListaPreciosDao
import com.mobile.massiveapp.data.database.dao.UsuarioZonasDao
import javax.inject.Inject

class ManagerInputData @Inject constructor(
    private val articuloDao: ArticuloDao,
    private val articuloListaPreciosDao: ArticuloListaPreciosDao,
    private val articuloPreciosDao: ArticuloPreciosDao,
    private val articuloCantidadesDao: ArticuloCantidadesDao,
    private val articuloGruposDao: ArticuloGruposDao,
    private val articuloGruposUMDao: ArticuloGruposUMDao,
    private val articuloGruposUMDetalleDao: ArticuloGruposUMDetalleDao,
    private val articuloLocalidadesDao: ArticuloLocalidadesDao,
    private val articuloFabricantesDao: ArticuloFabricantesDao,
    private val articuloAlmacenesDao: ArticuloAlmacenesDao,
    private val articuloUnidadesDao: ArticuloUnidadesDao,

    private val socioContactosDao: SocioContactosDao,
    private val socioDireccionesDao: SocioDireccionesDao,
    private val clienteSociosDao: ClienteSociosDao,
    private val clientePedidosDao: ClientePedidosDao,
    private val clientePedidosDetalleDao: ClientePedidosDetalleDao,
    private val clientePagosDao: ClientePagosDao,
    private val clientePagosDetalleDao: ClientePagosDetalleDao,
    private val clienteFacturasDao: ClienteFacturasDao,

    private val clienteFacturaDetalleDao: ClienteFacturaDetalleDao,
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
    private val generalDimensionesDao: GeneralDimensionesDao,
    private val generalActividadesEDao: GeneralActividadesEDao,
    private val generalZonasDao: GeneralZonasDao,
    private val sociedadDao: SociedadDao,
    private val basesDao: BasesDao,
    private val bancoDao: BancoDao,
    val configurarUsuariosDao: ConfigurarUsuarioDao,
    val usuarioListaPreciosDao: UsuarioListaPreciosDao,
    val usuarioGrupoArticulosDao: UsuarioGrupoArticuloDao,
    val usuarioGrupoSociosDao: UsuarioGrupoSociosDao,
    val usuarioZonasDao: UsuarioZonasDao,
    val usuarioAlmacenesDao: UsuarioAlmacenesDao,
    val cuentasCDao: CuentasCDao,
    val seriesNDao: SeriesNDao
) {
    companion object {
        const val ACTIVIDADES_E =       "ActividadesE"
        const val ALMACENES =           "Almacenes"
        const val ARTICULO =            "Articulo"
        const val ARTICULO_PRECIOS =    "ArticuloPrecios"
        const val ARTICULO_CANTIDADES = "ArticuloCantidades"
        const val BANCOS =              "Bancos"
        const val CENTROS_C =           "CentrosC"
        const val CONDICIONES =         "Condiciones"
        const val CUENTAS_B =           "CuentasB"
        const val DEPARTAMENTOS =       "Departamentos"
        const val DIMENSIONES =         "Dimensiones"
        const val DISTRITOS =           "Distritos"
        const val EMPLEADOS =           "Empleados"
        const val FABRICANTES =         "Fabricantes"
        const val FACTURAS_CL =         "FacturasCL"
        const val FACTURAS_CL_DETALLE = "FacturasCLDetalles"
        const val GRUPOS_AR =           "GruposAR"
        const val GRUPOS_SN =           "GruposSN"
        const val GRUPOS_UM =           "GruposUM"
        const val IMPUESTOS =           "Impuestos"
        const val INDICADORES =         "Indicadores"
        const val LISTA_PRECIOS =       "ListaPrecios"
        const val LOCALIDADES =         "Localidades"
        const val MONEDAS =             "Monedas"
        const val PAISES =              "Paises"
        const val PROVINCIAS =          "Provincias"
        const val PROYECTOS =           "Proyectos"
        const val SOCIEDAD =            "Sociedad"
        const val CLIENTE_SOCIOS_CONTACTOS =    "ClienteSociosContactos"
        const val CLIENTE_SOCIOS =              "ClienteSocios"
        const val CLIENTE_SOCIOS_DIRECCIONES =  "ClienteSociosDirecciones"
        const val UNIDADES =                    "Unidades"
        const val VENDEDORES =                  "Vendedores"
        const val ZONAS =                       "Zonas"

        const val USUARIO_ALMACENES =           "UsuariosAlmacenes"
        const val USUARIO_ZONAS =               "UsuariosZonas"
        const val USUARIO_LISTA_PRECIOS =       "UsuariosListaPrecios"
        const val USUARIO_GRUPOS_SOCIOS =       "UsuariosGruposSocios"
        const val USUARIO_GRUPOS_ARTICULOS =    "UsuariosGrupoArticulos"
        const val USUARIOS =                    "Usuarios"
        const val SERIES_N =                    "Series"
        const val CUENTAS_C =                   "CuentasC"

        const val SESION_CERRADA = 500

        val tablasNoMostrar = listOf(
            "InfoTablas",
            "android_metadata",
            "room_master_table",
            "ConsultaDocumentoContactos",
            "ConsultaDocumentoDirecciones",
            "SocioDniConsulta",
            "SocioNuevoAwait",
            "ConsultaDocumento",
            "Usuario",
            "Bases",
            "Configuracion",
            "SocioNegocio",
            "SocioDirecciones",
            "SocioContactos",
            "Articulo",
            "ArticuloCantidad",
            "ArticuloPrecio",
            "ClientePagos",
            "ClientePedidos",
            "ClientePagosDetalle",
            "ClientePedidosDetalle",
            "Factura",
            "FacturaDetalle",

            "UsuarioAlmacenes",
            "ConfigurarUsuarios",
            "UsuarioGrupoArticulos",
            "UsuarioGrupoSocios",
            "UsuarioListaPrecios",
            "UsuarioZonas",
            "Series",
            "CuentasC"
        )
    }
    private val daoMap = mapOf(
        ARTICULO                    to articuloDao,
        "Bases"                     to basesDao,
        BANCOS                      to bancoDao,
        LISTA_PRECIOS               to articuloListaPreciosDao,
        ARTICULO_PRECIOS            to articuloPreciosDao,
        ARTICULO_CANTIDADES         to articuloCantidadesDao,
        GRUPOS_AR                   to articuloGruposDao,
        GRUPOS_UM                   to articuloGruposUMDao,
        "ArticuloGruposUMDetalle"   to articuloGruposUMDetalleDao,
        LOCALIDADES                 to articuloLocalidadesDao,
        FABRICANTES                 to articuloFabricantesDao,
        ALMACENES                   to articuloAlmacenesDao,
        UNIDADES                    to articuloUnidadesDao,
        CLIENTE_SOCIOS_CONTACTOS    to socioContactosDao,
        CLIENTE_SOCIOS_DIRECCIONES  to socioDireccionesDao,
        CLIENTE_SOCIOS              to clienteSociosDao,
        "ClientePedidos"            to clientePedidosDao,
        "ClientePedidosDetalle"     to clientePedidosDetalleDao,
        "ClientePagos"              to clientePagosDao,
        "ClientePagosDetalle"       to clientePagosDetalleDao,
        FACTURAS_CL                 to clienteFacturasDao,
        FACTURAS_CL_DETALLE         to clienteFacturaDetalleDao,
        EMPLEADOS                   to generalEmpleadosDao,
        MONEDAS                     to generalMonedasDao,
        CENTROS_C                   to generalCentrosCDao,
        CONDICIONES                 to generalCondicionesDao,
        DEPARTAMENTOS               to generalDepartamentosDao,
        DISTRITOS                   to generalDistritosDao,
        IMPUESTOS                   to generalImpuestosDao,
        INDICADORES                 to generalIndicadoresDao,
        PAISES                      to generalPaisesDao,
        PROVINCIAS                  to generalProvinciasDao,
        PROYECTOS                   to generalProyectosDao,
        GRUPOS_SN                   to generalSocioGruposDao,
        VENDEDORES                  to generalVendedoresDao,
        CUENTAS_B                   to generalCuentasBDao,
        DIMENSIONES                 to generalDimensionesDao,
        ACTIVIDADES_E               to generalActividadesEDao,
        ZONAS                       to generalZonasDao,
        SOCIEDAD                    to sociedadDao,
        USUARIO_ALMACENES           to usuarioAlmacenesDao,
        USUARIO_ZONAS               to usuarioZonasDao,
        USUARIO_LISTA_PRECIOS       to usuarioListaPreciosDao,
        USUARIO_GRUPOS_SOCIOS       to usuarioGrupoSociosDao,
        USUARIO_GRUPOS_ARTICULOS    to usuarioGrupoArticulosDao,
        USUARIOS                    to configurarUsuariosDao,
        SERIES_N                    to seriesNDao,
        CUENTAS_C                   to cuentasCDao,
    )



    /*fun registrarMaestro(endpoint: String, listaDatos: List<Any>){
        val dao = daoMap[endpoint]
        val tipoDeDato = endpointDataMap[endpoint]

        val listaHash: Type? = when (tipoDeDato) {
            is ParameterizedType -> tipoDeDato.actualTypeArguments[0] // Si es un tipo parametrizado, obtenemos el primer argumento que debería ser el tipo de lista
            else -> null // Manejar otros casos si es necesario
        }
    }*/

    inline fun <reified T> castLista(lista: List<Any>): List<T>? {
        return try {
            lista.filterIsInstance<T>()
        } catch (e: ClassCastException) {
            null
        }
    }

    suspend fun registrarMaestro(endpoint: String, listaDatos: List<Any>) {
        val dao = daoMap[endpoint] ?: return
        (dao as BaseDao<*>).insertAllData(listaDatos as List<Nothing>)
    }
}

interface Mapping<T, Y> {
    fun map(data: T):Y
}





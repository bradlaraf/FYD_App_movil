package com.mobile.massiveapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.massiveapp.data.database.dao.AnexoImagenDao
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
import com.mobile.massiveapp.data.database.dao.BasesDao
import com.mobile.massiveapp.data.database.dao.CamionetaDao
import com.mobile.massiveapp.data.database.dao.CargosDao
import com.mobile.massiveapp.data.database.dao.ClienteFacturaDetalleDao
import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.database.dao.ClientePagosDao
import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.database.dao.DatosMaestrosDao
import com.mobile.massiveapp.data.database.dao.GeneralEmpleadosDao
import com.mobile.massiveapp.data.database.dao.GeneralMonedasDao
import com.mobile.massiveapp.data.database.dao.SocioContactosDao
import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.dao.ConductorDao
import com.mobile.massiveapp.data.database.dao.ConfiguracionDao
import com.mobile.massiveapp.data.database.dao.ConfigurarUsuarioDao
import com.mobile.massiveapp.data.database.dao.ConsultaDocumentoContactosDao
import com.mobile.massiveapp.data.database.dao.ConsultaDocumentoDireccionesDao
import com.mobile.massiveapp.data.database.dao.CuentasCDao
import com.mobile.massiveapp.data.database.dao.DashboardDao
import com.mobile.massiveapp.data.database.dao.GeneralActividadesEDao
import com.mobile.massiveapp.data.database.dao.GeneralCentrosCDao
import com.mobile.massiveapp.data.database.dao.GeneralCondicionesDao
import com.mobile.massiveapp.data.database.dao.GeneralCuentasBDao
import com.mobile.massiveapp.data.database.dao.GeneralDepartamentosDao
import com.mobile.massiveapp.data.database.dao.GeneralDimensionesDao
import com.mobile.massiveapp.data.database.dao.GeneralDistritosDao
import com.mobile.massiveapp.data.database.dao.GeneralImpuestosDao
import com.mobile.massiveapp.data.database.dao.GeneralIndicadoresDao
import com.mobile.massiveapp.data.database.dao.GeneralPaisesDao
import com.mobile.massiveapp.data.database.dao.GeneralProvinciasDao
import com.mobile.massiveapp.data.database.dao.GeneralProyectosDao
import com.mobile.massiveapp.data.database.dao.GeneralSocioGruposDao
import com.mobile.massiveapp.data.database.dao.GeneralVendedoresDao
import com.mobile.massiveapp.data.database.dao.GeneralZonasDao
import com.mobile.massiveapp.data.database.dao.ReportesDao
import com.mobile.massiveapp.data.database.dao.SociedadDao
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.dao.SocioDniConsultaDao
import com.mobile.massiveapp.data.database.entities.ArticuloEntity
import com.mobile.massiveapp.data.database.entities.GeneralEmpleadosEntity
import com.mobile.massiveapp.data.database.entities.SocioContactosEntity
import com.mobile.massiveapp.data.database.entities.SocioDireccionesEntity
import com.mobile.massiveapp.data.database.entities.ClienteSociosEntity
import com.mobile.massiveapp.data.database.entities.SocioGruposEntity
import com.mobile.massiveapp.data.database.dao.SocioGruposDao
import com.mobile.massiveapp.data.database.dao.SocioNuevoAwaitDao
import com.mobile.massiveapp.data.database.dao.DocumentoConsultaDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.dao.GrupoDescuentoDao
import com.mobile.massiveapp.data.database.dao.GrupoDescuentoDetalleDao
import com.mobile.massiveapp.data.database.dao.InfoTablasDao
import com.mobile.massiveapp.data.database.dao.ManifiestoAyudanteDao
import com.mobile.massiveapp.data.database.dao.ManifiestoDao
import com.mobile.massiveapp.data.database.dao.ManifiestoDocumentoDao
import com.mobile.massiveapp.data.database.dao.PrecioEspecial1Dao
import com.mobile.massiveapp.data.database.dao.PrecioEspecial2Dao
import com.mobile.massiveapp.data.database.dao.PrecioEspecialDao
import com.mobile.massiveapp.data.database.dao.SeriesNDao
import com.mobile.massiveapp.data.database.dao.SucursalDao
import com.mobile.massiveapp.data.database.dao.TipoCambioDao
import com.mobile.massiveapp.data.database.dao.FormaPagoDao
import com.mobile.massiveapp.data.database.dao.UsuarioAlmacenesDao
import com.mobile.massiveapp.data.database.dao.UsuarioDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoArticuloDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoSociosDao
import com.mobile.massiveapp.data.database.dao.UsuarioListaPreciosDao
import com.mobile.massiveapp.data.database.dao.UsuarioZonasDao
import com.mobile.massiveapp.data.database.entities.AnexoImagenEntity
import com.mobile.massiveapp.data.database.entities.ArticuloAlmacenesEntity
import com.mobile.massiveapp.data.database.entities.ArticuloCantidadesEntity
import com.mobile.massiveapp.data.database.entities.ArticuloFabricantesEntity
import com.mobile.massiveapp.data.database.entities.ArticuloGruposEntity
import com.mobile.massiveapp.data.database.entities.ArticuloGruposUMDetalleEntity
import com.mobile.massiveapp.data.database.entities.ArticuloGruposUMEntity
import com.mobile.massiveapp.data.database.entities.ArticuloListaPreciosEntity
import com.mobile.massiveapp.data.database.entities.ArticuloLocalidadesEntity
import com.mobile.massiveapp.data.database.entities.ArticuloPreciosEntity
import com.mobile.massiveapp.data.database.entities.ArticuloUnidadesEntity
import com.mobile.massiveapp.data.database.entities.BancoEntity
import com.mobile.massiveapp.data.database.entities.BasesEntity
import com.mobile.massiveapp.data.database.entities.CamionetaEntity
import com.mobile.massiveapp.data.database.entities.CargosEntity
import com.mobile.massiveapp.data.database.entities.ClienteFacturaDetalleEntity
import com.mobile.massiveapp.data.database.entities.ClienteFacturasEntity
import com.mobile.massiveapp.data.database.entities.ClientePagosDetalleEntity
import com.mobile.massiveapp.data.database.entities.ClientePagosEntity
import com.mobile.massiveapp.data.database.entities.ClientePedidosDetalleEntity
import com.mobile.massiveapp.data.database.entities.ClientePedidosEntity
import com.mobile.massiveapp.data.database.entities.ConductorEntity
import com.mobile.massiveapp.data.database.entities.ConfiguracionEntity
import com.mobile.massiveapp.data.database.entities.ConfigurarUsuariosEntity
import com.mobile.massiveapp.data.database.entities.ConsultaDocumentoContactosEntity
import com.mobile.massiveapp.data.database.entities.ConsultaDocumentoDireccionesEntity
import com.mobile.massiveapp.data.database.entities.GeneralActividadesEEntity
import com.mobile.massiveapp.data.database.entities.GeneralCentrosCEntity
import com.mobile.massiveapp.data.database.entities.GeneralCondicionesEntity
import com.mobile.massiveapp.data.database.entities.GeneralCuentasBEntity
import com.mobile.massiveapp.data.database.entities.GeneralDepartamentosEntity
import com.mobile.massiveapp.data.database.entities.GeneralDimensionesEntity
import com.mobile.massiveapp.data.database.entities.GeneralDistritosEntity
import com.mobile.massiveapp.data.database.entities.GeneralImpuestosEntity
import com.mobile.massiveapp.data.database.entities.GeneralIndicadoresEntity
import com.mobile.massiveapp.data.database.entities.GeneralMonedasEntity
import com.mobile.massiveapp.data.database.entities.GeneralPaisesEntity
import com.mobile.massiveapp.data.database.entities.GeneralProvinciasEntity
import com.mobile.massiveapp.data.database.entities.GeneralProyectosEntity
import com.mobile.massiveapp.data.database.entities.GeneralSocioGruposEntity
import com.mobile.massiveapp.data.database.entities.GeneralVendedoresEntity
import com.mobile.massiveapp.data.database.entities.GeneralZonasEntity
import com.mobile.massiveapp.data.database.entities.SociedadEntity
import com.mobile.massiveapp.data.database.entities.SocioDniConsultaEntity
import com.mobile.massiveapp.data.database.entities.SocioNuevoAwaitEntity
import com.mobile.massiveapp.data.database.entities.ConsultaDocumentoEntity
import com.mobile.massiveapp.data.database.entities.CuentasCEntity
import com.mobile.massiveapp.data.database.entities.ErrorLogEntity
import com.mobile.massiveapp.data.database.entities.GrupoDescuentoDetalleEntity
import com.mobile.massiveapp.data.database.entities.GrupoDescuentoEntity
import com.mobile.massiveapp.data.database.entities.InfoTablasEntity
import com.mobile.massiveapp.data.database.entities.ManifiestoAyudanteEntity
import com.mobile.massiveapp.data.database.entities.ManifiestoDocumentoEntity
import com.mobile.massiveapp.data.database.entities.ManifiestoEntity
import com.mobile.massiveapp.data.database.entities.PrecioEspecial1Entity
import com.mobile.massiveapp.data.database.entities.PrecioEspecial2Entity
import com.mobile.massiveapp.data.database.entities.PrecioEspecialEntity
import com.mobile.massiveapp.data.database.entities.SeriesNEntity
import com.mobile.massiveapp.data.database.entities.SucursalEntity
import com.mobile.massiveapp.data.database.entities.TipoCambioEntity
import com.mobile.massiveapp.data.database.entities.FormaPagoEntity
import com.mobile.massiveapp.data.database.entities.UsuarioAlmacenesEntity
import com.mobile.massiveapp.data.database.entities.UsuarioEntity
import com.mobile.massiveapp.data.database.entities.UsuarioGrupoArticulosEntity
import com.mobile.massiveapp.data.database.entities.UsuarioGrupoSociosEntity
import com.mobile.massiveapp.data.database.entities.UsuarioListaPreciosEntity
import com.mobile.massiveapp.data.database.entities.UsuarioZonasEntity

@Database(
    entities = [
        ArticuloEntity::class,
        ClienteSociosEntity::class,
        ClientePedidosEntity::class ,
        ClientePedidosDetalleEntity::class,
        ClientePagosEntity::class,
        ClientePagosDetalleEntity::class,
        ClienteFacturasEntity::class,
        SocioContactosEntity::class,
        SocioDireccionesEntity::class,
        SocioGruposEntity::class,
        ArticuloAlmacenesEntity::class,
        ArticuloFabricantesEntity::class,
        ArticuloCantidadesEntity::class,
        ArticuloGruposEntity::class,
        ArticuloGruposUMEntity::class,
        ArticuloGruposUMDetalleEntity::class,
        ArticuloListaPreciosEntity::class,
        ArticuloLocalidadesEntity::class,
        ArticuloPreciosEntity::class,
        ArticuloUnidadesEntity::class,
        ConsultaDocumentoEntity::class,
        SocioNuevoAwaitEntity::class,
        GeneralMonedasEntity::class,
        GeneralEmpleadosEntity::class,
        GeneralCentrosCEntity::class,
        GeneralCondicionesEntity::class,
        GeneralDepartamentosEntity::class,
        GeneralDistritosEntity::class,
        GeneralImpuestosEntity::class,
        GeneralIndicadoresEntity::class,
        GeneralPaisesEntity::class,
        GeneralProvinciasEntity::class,
        GeneralProyectosEntity::class,
        GeneralSocioGruposEntity::class,
        GeneralVendedoresEntity::class,
        GeneralCuentasBEntity::class,
        GeneralZonasEntity::class,
        UsuarioEntity::class,
        SocioDniConsultaEntity::class,
        BasesEntity::class,
        GeneralDimensionesEntity::class,
        GeneralActividadesEEntity::class,
        ConfiguracionEntity::class,
        SociedadEntity::class,
        ConsultaDocumentoContactosEntity::class,
        ConsultaDocumentoDireccionesEntity::class,
        ClienteFacturaDetalleEntity::class,
        InfoTablasEntity::class,
        BancoEntity::class,
        ErrorLogEntity::class,
        ConfigurarUsuariosEntity::class,
        UsuarioAlmacenesEntity::class,
        UsuarioGrupoArticulosEntity::class,
        UsuarioGrupoSociosEntity::class,
        UsuarioListaPreciosEntity::class,
        UsuarioZonasEntity::class,
        SeriesNEntity::class,
        CuentasCEntity::class,

        ManifiestoEntity::class,
        ManifiestoAyudanteEntity::class,
        ManifiestoDocumentoEntity::class,

        AnexoImagenEntity::class,

        TipoCambioEntity::class,
        SucursalEntity::class,
        CamionetaEntity::class,
        ConductorEntity::class,
        GrupoDescuentoEntity::class,
        GrupoDescuentoDetalleEntity::class,
        PrecioEspecialEntity::class,
        PrecioEspecial1Entity::class,
        PrecioEspecial2Entity::class,
        FormaPagoEntity::class,
        CargosEntity::class
       ],
    version = 2,

)
abstract class MassiveDatabase: RoomDatabase() {
    abstract fun getDatosMaestrosDao(): DatosMaestrosDao
    abstract fun getArticuloDao(): ArticuloDao
    abstract fun getArticuloAlmacenesDao(): ArticuloAlmacenesDao
    abstract fun getArticuloFabricantesDao(): ArticuloFabricantesDao
    abstract fun getArticuloCantidadesDao(): ArticuloCantidadesDao
    abstract fun getArticulosGruposDao(): ArticuloGruposDao
    abstract fun getArticuloGruposUMDao(): ArticuloGruposUMDao
    abstract fun getArticuloGruposUMDetalleDao(): ArticuloGruposUMDetalleDao
    abstract fun getArticuloListaPreciosDao(): ArticuloListaPreciosDao
    abstract fun getArticuloLocalidadesDao(): ArticuloLocalidadesDao
    abstract fun getArticuloPreciosDao(): ArticuloPreciosDao
    abstract fun getArticuloUnidadesDao(): ArticuloUnidadesDao
    abstract fun getSocioContactosDao(): SocioContactosDao
    abstract fun getSocioDireccionesDao(): SocioDireccionesDao
    abstract fun getSocioGruposDao(): SocioGruposDao
    abstract fun getSocioRucConsultaDao(): DocumentoConsultaDao
    abstract fun getSocioNuevoAwaitDao(): SocioNuevoAwaitDao
    abstract fun getSocioDao(): ClienteSociosDao
    abstract fun getClientePedidosDao(): ClientePedidosDao
    abstract fun getClientePagosDao(): ClientePagosDao
    abstract fun getClientePedidosDetalleDao(): ClientePedidosDetalleDao
    abstract fun getClientePagosDetalleDao(): ClientePagosDetalleDao
    abstract fun getClienteFacturasDao(): ClienteFacturasDao
    abstract fun getGeneralEmpleadosDao(): GeneralEmpleadosDao
    abstract fun getGeneralMonedasDao(): GeneralMonedasDao
    abstract fun getGeneralCentrosCDao(): GeneralCentrosCDao
    abstract fun getGeneralCondicionesDao(): GeneralCondicionesDao
    abstract fun getGeneralDepartamentosDao(): GeneralDepartamentosDao
    abstract fun getGeneralDistritosDao(): GeneralDistritosDao
    abstract fun getGeneralImpuestosDao(): GeneralImpuestosDao
    abstract fun getGeneralIndicadoresDao(): GeneralIndicadoresDao
    abstract fun getGeneralPaisesDao(): GeneralPaisesDao
    abstract fun getGeneralProvinciasDao(): GeneralProvinciasDao
    abstract fun getGeneralProyectosDao(): GeneralProyectosDao
    abstract fun getGeneralSocioGruposDao(): GeneralSocioGruposDao
    abstract fun getGeneralVendedoresDao(): GeneralVendedoresDao
    abstract fun getGeneralCuentasBDao(): GeneralCuentasBDao
    abstract fun getUsuarioDao(): UsuarioDao
    abstract fun getGeneralZonasDao(): GeneralZonasDao
    abstract fun getReportesDao(): ReportesDao
    abstract fun getSocioDniConsultaDao(): SocioDniConsultaDao
    abstract fun getBasesDao(): BasesDao
    abstract fun getGeneralDimensionesDao(): GeneralDimensionesDao
    abstract fun getGeneralActividadesEDao(): GeneralActividadesEDao
    abstract fun getConfiguracionDao(): ConfiguracionDao
    abstract fun getSociedadDao(): SociedadDao
    abstract fun getConsultaDocumentoContactosDao(): ConsultaDocumentoContactosDao
    abstract fun getConsultaDocumentoDireccionesDao(): ConsultaDocumentoDireccionesDao
    abstract fun getDashboardDao(): DashboardDao
    abstract fun getClienteFacturaDetalleDao(): ClienteFacturaDetalleDao
    abstract fun getInfoTablasDao(): InfoTablasDao
    abstract fun getBancoDao(): BancoDao
    abstract fun getErrorLogDao(): ErrorLogDao
    abstract fun getConfigurarUsuarioDao(): ConfigurarUsuarioDao
    abstract fun getUsuarioAlmacenesDao(): UsuarioAlmacenesDao
    abstract fun getUsuarioGrupoArticuloDao(): UsuarioGrupoArticuloDao
    abstract fun getUsuarioGrupoSociosDao(): UsuarioGrupoSociosDao
    abstract fun getUsuarioListaPreciosDao(): UsuarioListaPreciosDao
    abstract fun getUsuarioZonasDao(): UsuarioZonasDao
    abstract fun getSeriesNDao(): SeriesNDao
    abstract fun getCuentasC(): CuentasCDao

    abstract fun getManifiestoDao(): ManifiestoDao
    abstract fun getManifiestoAyudanteDao(): ManifiestoAyudanteDao
    abstract fun getManifiestoDocumentoDao(): ManifiestoDocumentoDao

    abstract fun getAnexoImagenDao(): AnexoImagenDao

    abstract fun getTipoCambioDao(): TipoCambioDao
    abstract fun getSucursalDao(): SucursalDao
    abstract fun getCamionetaDao(): CamionetaDao
    abstract fun getConductorDao(): ConductorDao
    abstract fun getGrupoDescuentoDao(): GrupoDescuentoDao
    abstract fun getGrupoDescuentoDetalleDao(): GrupoDescuentoDetalleDao
    abstract fun getPrecioEspecialDao(): PrecioEspecialDao
    abstract fun getPrecioEspecial1Dao(): PrecioEspecial1Dao
    abstract fun getPrecioEspecial2Dao(): PrecioEspecial2Dao
    abstract fun getFormaPagoDao(): FormaPagoDao
    abstract fun getCargosDao(): CargosDao
}
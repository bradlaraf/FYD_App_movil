package com.mobile.massiveapp.di

import android.content.Context
import androidx.room.Room
import com.mobile.massiveapp.data.database.MassiveDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val MASSIVE_DATABASE_NAME = "massive_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MassiveDatabase::class.java, MASSIVE_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideArticuloDao (db:MassiveDatabase) = db.getArticuloDao()
    @Singleton
    @Provides
    fun provideSocioDao (db:MassiveDatabase) = db.getSocioDao()
    @Singleton
    @Provides
    fun providesSocioContactosDao (db:MassiveDatabase) = db.getSocioContactosDao()
    @Singleton
    @Provides
    fun providesSocioDireccionesDao (db:MassiveDatabase) = db.getSocioDireccionesDao()
    @Singleton
    @Provides
    fun providesSocioGruposDao (db:MassiveDatabase) = db.getSocioGruposDao()
    @Singleton
    @Provides
    fun providesGeneralEmpleadosDao (db:MassiveDatabase) = db.getGeneralEmpleadosDao()

    @Singleton
    @Provides
    fun providesArticuloAlmacenes (db:MassiveDatabase) = db.getArticuloAlmacenesDao()

    @Singleton
    @Provides
    fun providesArticuloFabricantes (db:MassiveDatabase) = db.getArticuloFabricantesDao()

    @Singleton
    @Provides
    fun providesArticuloCantidades (db:MassiveDatabase) = db.getArticuloCantidadesDao()

    @Singleton
    @Provides
    fun providesArticuloGrupos (db:MassiveDatabase) = db.getArticulosGruposDao()

    @Singleton
    @Provides
    fun providesArticuloGruposUM (db:MassiveDatabase) = db.getArticuloGruposUMDao()

    @Singleton
    @Provides
    fun providesArticuloGruposUMDetalle (db:MassiveDatabase) = db.getArticuloGruposUMDetalleDao()

    @Singleton
    @Provides
    fun providesArticuloListaPrecios(db:MassiveDatabase) = db.getArticuloListaPreciosDao()

    @Singleton
    @Provides
    fun providesArticuloLocalidades(db:MassiveDatabase) = db.getArticuloLocalidadesDao()

    @Singleton
    @Provides
    fun providesArticuloPrecios(db:MassiveDatabase) = db.getArticuloPreciosDao()

    @Singleton
    @Provides
    fun providesArticuloUnidades(db:MassiveDatabase) = db.getArticuloUnidadesDao()

    @Singleton
    @Provides
    fun providesDatosMaestros(db:MassiveDatabase) = db.getDatosMaestrosDao()

    @Singleton
    @Provides
    fun providesSocioRucConsulta(db:MassiveDatabase) = db.getSocioRucConsultaDao()

    @Singleton
    @Provides
    fun providesSocioNuevoAwait(db:MassiveDatabase) = db.getSocioNuevoAwaitDao()

    @Singleton
    @Provides
    fun providesGeneralMonedas(db:MassiveDatabase) = db.getGeneralMonedasDao()
    @Singleton
    @Provides
    fun providesClientePedidosDao(db:MassiveDatabase) = db.getClientePedidosDao()
    @Singleton
    @Provides
    fun providesClientePagosDao(db:MassiveDatabase) = db.getClientePagosDao()

    @Singleton
    @Provides
    fun providesClienteFacturasDao(db:MassiveDatabase) = db.getClienteFacturasDao()
    @Singleton
    @Provides
    fun providesClientePedidosDetalleDao(db:MassiveDatabase) = db.getClientePedidosDetalleDao()
    @Singleton
    @Provides
    fun providesClientePagosDetalleDao(db:MassiveDatabase) = db.getClientePagosDetalleDao()
    @Singleton
    @Provides
    fun providesGeneralCentrosCDao(db:MassiveDatabase) = db.getGeneralCentrosCDao()
    @Singleton
    @Provides
    fun providesGeneralCondicionesDao(db:MassiveDatabase) = db.getGeneralCondicionesDao()

    @Singleton
    @Provides
    fun providesGeneralDepartamentosDao(db:MassiveDatabase) = db.getGeneralDepartamentosDao()

    @Singleton
    @Provides
    fun providesGeneralDistritosDao(db:MassiveDatabase) = db.getGeneralDistritosDao()

    @Singleton
    @Provides
    fun providesGeneralImpuestosDao(db:MassiveDatabase) = db.getGeneralImpuestosDao()

    @Singleton
    @Provides
    fun providesGeneralIndicadoresDao(db:MassiveDatabase) = db.getGeneralIndicadoresDao()

    @Singleton
    @Provides
    fun providesGeneralPaisesDao(db:MassiveDatabase) = db.getGeneralPaisesDao()

    @Singleton
    @Provides
    fun providesGeneralProvinciasDao(db:MassiveDatabase) = db.getGeneralProvinciasDao()

    @Singleton
    @Provides
    fun providesGeneralProyectosDao(db:MassiveDatabase) = db.getGeneralProyectosDao()

    @Singleton
    @Provides
    fun providesGeneralSocioGruposDao(db:MassiveDatabase) = db.getGeneralSocioGruposDao()
    @Singleton
    @Provides
    fun providesGeneralVendedoresDao(db:MassiveDatabase) = db.getGeneralVendedoresDao()
    @Singleton
    @Provides
    fun providesGeneralCuentasBDao(db:MassiveDatabase) = db.getGeneralCuentasBDao()
    @Singleton
    @Provides
    fun providesUsuarioDao(db:MassiveDatabase) = db.getUsuarioDao()
    @Singleton
    @Provides
    fun providesGeneralZonasDao(db:MassiveDatabase) = db.getGeneralZonasDao()
    @Singleton
    @Provides
    fun providesReportesDao(db:MassiveDatabase) = db.getReportesDao()
    @Singleton
    @Provides
    fun providesSocioDniConsultaDao(db:MassiveDatabase) = db.getSocioDniConsultaDao()
    @Singleton
    @Provides
    fun providesBasesDao(db:MassiveDatabase) = db.getBasesDao()
    @Singleton
    @Provides
    fun providesGeneralDimensionesDao(db:MassiveDatabase) = db.getGeneralDimensionesDao()
    @Singleton
    @Provides
    fun providesGeneralActividadesEDao(db:MassiveDatabase) = db.getGeneralActividadesEDao()
    @Singleton
    @Provides
    fun providesConfiguracionDao(db:MassiveDatabase) = db.getConfiguracionDao()
    @Singleton
    @Provides
    fun providesSociedadDao(db:MassiveDatabase) = db.getSociedadDao()
    @Singleton
    @Provides
    fun providesConsultaDocumentoDireccionesDao(db:MassiveDatabase) = db.getConsultaDocumentoDireccionesDao()
    @Singleton
    @Provides
    fun providesConsultaDocumentoContactosDao(db:MassiveDatabase) = db.getConsultaDocumentoContactosDao()
    @Singleton
    @Provides
    fun providesDashboardDao(db:MassiveDatabase) = db.getDashboardDao()
    @Singleton
    @Provides
    fun providesClienteFacturaDetalleDao(db:MassiveDatabase) = db.getClienteFacturaDetalleDao()
    @Singleton
    @Provides
    fun providesInfoTablasDao(db:MassiveDatabase) = db.getInfoTablasDao()
    @Singleton
    @Provides
    fun providesBancoDao(db:MassiveDatabase) = db.getBancoDao()
    @Singleton
    @Provides
    fun providesErrorLogDao(db:MassiveDatabase) = db.getErrorLogDao()
    @Singleton
    @Provides
    fun providesUsuarioAlmacenesDao(db:MassiveDatabase) = db.getUsuarioAlmacenesDao()

    @Singleton
    @Provides
    fun providesConfigurarUsuarioDao(db:MassiveDatabase) = db.getConfigurarUsuarioDao()

    @Singleton
    @Provides
    fun providesUsuarioGrupoArticuloDao(db:MassiveDatabase) = db.getUsuarioGrupoArticuloDao()

    @Singleton
    @Provides
    fun providesUsuarioGrupoSociosDao(db:MassiveDatabase) = db.getUsuarioGrupoSociosDao()

    @Singleton
    @Provides
    fun providesUsuarioListaPreciosDao(db:MassiveDatabase) = db.getUsuarioListaPreciosDao()

    @Singleton
    @Provides
    fun providesUsuarioZonasDao(db:MassiveDatabase) = db.getUsuarioZonasDao()

    @Singleton
    @Provides
    fun providesSeriesNDao(db:MassiveDatabase) = db.getSeriesNDao()
    @Singleton
    @Provides
    fun providesCuentasCDao(db:MassiveDatabase) = db.getCuentasC()
}
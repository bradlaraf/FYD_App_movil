package com.mobile.massiveapp.ui.view.menu.drawer

import android.content.Intent
import android.os.Build
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import com.mobile.massiveapp.MassiveApp.Companion.prefsApp
import com.mobile.massiveapp.R
import com.mobile.massiveapp.core.SincronizarDatosWorker
import com.mobile.massiveapp.databinding.ActivityDrawerBaseBinding
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogConfirmationBasicHelper
import com.mobile.massiveapp.ui.base.BaseDialogLoading
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.configuracion.ConfiguracionActivity
import com.mobile.massiveapp.ui.view.cobranzas.CobranzasActivity
import com.mobile.massiveapp.ui.view.facturas.FacturasActivity
import com.mobile.massiveapp.ui.view.infodata.InfoTablesActivity
import com.mobile.massiveapp.ui.view.inventario.InventarioActivity
import com.mobile.massiveapp.ui.view.log.LogActivity
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.menu.MenuActivity
import com.mobile.massiveapp.ui.view.pedidocliente.PedidoClienteActivity
import com.mobile.massiveapp.ui.view.reportes.ReportesActivity
import com.mobile.massiveapp.ui.view.sociodenegocio.SocioNegocioActivity
import com.mobile.massiveapp.ui.view.usuarios.UsuariosActivity
import com.mobile.massiveapp.ui.viewmodel.DatosMaestrosViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.google.android.material.navigation.NavigationView
import com.mobile.massiveapp.ui.view.manifiesto.ManifiestoActivity
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration

@AndroidEntryPoint
open class DrawerBaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityDrawerBaseBinding
    private lateinit var drawerLayout: DrawerLayout
    private val loadinDialog = BaseDialogLoading(this, "Sincronizando Datos Maestros")
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val usuariosViewModel: UsuariosViewModel by viewModels()
    private val datosMaestrosViewModel: DatosMaestrosViewModel by viewModels()

    override fun setContentView(view: View?) {

        drawerLayout = layoutInflater.inflate(R.layout.activity_drawer_base, null) as DrawerLayout

        val container: FrameLayout = drawerLayout.findViewById(R.id.activityContainer)
        container.addView(view)
        super.setContentView(drawerLayout)

        val toolbar: Toolbar = drawerLayout.findViewById(R.id.drawerToolBar)
        setSupportActionBar(toolbar)

        val navigationView: NavigationView = drawerLayout.findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)


        val toggle = ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

            //Set USUARIO NAME
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuarioDb->
            val userName = try {
                usuarioDb.Name
            } catch (e: Exception){
                DoUsuario().Name
            }
            setUserName()
            setVersionName()

            //Gestion Usuarios
            if (usuarioDb.SuperUser == "Y"){
                val menu = navigationView.menu
                menu.setGroupVisible(R.id.group_usuarios, true)
                menu.findItem(R.id.nav_drawer_sincronizar_usuarios).isVisible = true
                menu.findItem(R.id.nav_drawer_reset_id_movil).isVisible = true
            }
        }

        //LiveData info Usuarios
        datosMaestrosViewModel.dataGetInfoUsuario.observe(this){ response->
            loadinDialog.onDismiss()
            when(response.ErrorCodigo){
                500->{
                    BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                        //Aceptar
                        usuarioViewModel.logOutDrawer()
                    }
                }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }


        //LiveData de datos maestros
        datosMaestrosViewModel.dataGetAllDatosMaestros.observe(this){ response->
            loadinDialog.onDismiss()
            when(response.ErrorCodigo){
                500->{
                    BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                        //Aceptar
                        usuarioViewModel.logOutDrawer()
                    }
                }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        //LiveData de sincronizar documentos
        datosMaestrosViewModel.dataSendDatosMaestros.observe(this){ response->
            loadinDialog.onDismiss()
            when(response.ErrorCodigo){
                500->{
                    BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                        //Aceptar
                        usuarioViewModel.logOutDrawer()
                    }
                }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        //LiveData de la sincronizacion de inventario
        datosMaestrosViewModel.dataGetInventario.observe(this){ response->
            loadinDialog.onDismiss()
            when(response.ErrorCodigo){
                500->{
                    BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                        //Aceptar
                        usuarioViewModel.logOutDrawer()
                    }
                }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        //LiveData de la sincronizacion de Socios
        datosMaestrosViewModel.dataGetInfoSocios.observe(this){ response->
            loadinDialog.onDismiss()
            when(response.ErrorCodigo){
                500->{
                    BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                        //Aceptar
                        usuarioViewModel.logOutDrawer()
                    }
                }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        //LiveData del Reset ID
        usuariosViewModel.dataResetIdMovil.observe(this) { response->
            loadinDialog.onDismiss()
            when(response.ErrorCodigo){
                500->{
                    BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                        //Aceptar
                        usuarioViewModel.logOutDrawer()
                    }
                }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        //LiveData Cierre de Sesion
        usuarioViewModel.isLoadingLogOutDrawer.observe(this){
            val loadingSimpleDialog = BaseDialogSImpleLoading(this@DrawerBaseActivity)
            loadingSimpleDialog.startLoading("Cerrando Sesion...")

            if (!it){
                loadingSimpleDialog.onDismiss()
            }
        }

        usuarioViewModel.dataLogOutDrawer.observe(this) { error->
            if (error.ErrorCodigo == 0){
                Intent(this, LoginActivity::class.java).also { startActivity(it) }
                finish()
            } else {
                Toast.makeText(this, error.ErrorMensaje, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setVersionName() {
        try {
            val versionTextView = findViewById<TextView>(R.id.txvVersionNameApplication)
            versionTextView.text = prefsApp.getVersionApp()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val workRequest2 = OneTimeWorkRequestBuilder<SincronizarDatosWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofSeconds(15)
            )
            .build()
        when(item.itemId){
            R.id.nav_drawer_inicio -> {
                if (this::class.java != MenuActivity::class.java) {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }

            R.id.nav_drawer_inventario -> {
                if (this::class.java != InventarioActivity::class.java) {
                    val intent = Intent(this, InventarioActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }

            R.id.nav_drawer_manifiesto -> {
                if (this::class.java != ManifiestoActivity::class.java) {
                    val intent = Intent(this, ManifiestoActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }

            R.id.nav_drawer_facturas -> {
                if (this::class.java != FacturasActivity::class.java) {
                    val intent = Intent(this, FacturasActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }

            R.id.nav_drawer_socios -> {
                if (this::class.java != SocioNegocioActivity::class.java) {
                    val intent = Intent(this, SocioNegocioActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }

            R.id.nav_drawer_pedidos_cliente -> {
                if (this::class.java != PedidoClienteActivity::class.java) {
                    val intent = Intent(this, PedidoClienteActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }

            R.id.nav_drawer_cobranzas -> {
                if (this::class.java != CobranzasActivity::class.java) {
                    val intent = Intent(this, CobranzasActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }

            R.id.nav_drawer_info_tablas -> {
                if (this::class.java != InfoTablesActivity::class.java) {
                    val intent = Intent(this, InfoTablesActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }

            R.id.nav_drawer_log -> {
                if (this::class.java != LogActivity::class.java) {
                    val intent = Intent(this, LogActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }


            R.id.nav_drawer_usuarios -> {
                if (this::class.java != UsuariosActivity::class.java) {
                    val intent = Intent(this, UsuariosActivity::class.java)
                    startActivity(intent)
                } else {
                    drawerLayout.close()
                }
            }


            R.id.nav_drawer_sincronizar_usuarios ->{
                drawerLayout.close()
                loadinDialog.startLoading()

                datosMaestrosViewModel.getInfoUsuario {progress, message, maxLenght->
                    loadinDialog.updateProgress(progress, message, maxLenght)
                }

            }


            R.id.nav_drawer_sincronizar_maestros ->{
                drawerLayout.close()
                loadinDialog.startLoading()

                datosMaestrosViewModel.getAllDatosMaestros {progress, message, maxLenght->
                    loadinDialog.updateProgress(progress, message, maxLenght)
                }

            }


            R.id.nav_drawer_sincronizar_documentos ->{
                drawerLayout.close()
                loadinDialog.startLoading()

                datosMaestrosViewModel.sendDatosMaestros { progress, message, maxLenght->
                    loadinDialog.updateProgress(progress, message, maxLenght)
                }
            }

            R.id.nav_drawer_sincronizar_inventario ->{
                drawerLayout.close()
                loadinDialog.startLoading()

                datosMaestrosViewModel.getInventario {progress, message, maxLenght->
                    loadinDialog.updateProgress(progress, message, maxLenght)
                }

            }

            R.id.nav_drawer_sincronizar_socios ->{
                drawerLayout.close()
                loadinDialog.startLoading()

                datosMaestrosViewModel.getInfoSocios {progress, message, maxLenght->
                    loadinDialog.updateProgress(progress, message, maxLenght)
                }

            }

            //Reset Id
            R.id.nav_drawer_reset_id_movil -> {
                drawerLayout.close()
                loadinDialog.startLoading()

                usuariosViewModel.resetIdMovil()
                loadinDialog.updateProgress(0, "Reseteando Id de usuarios", 1)


            }

            R.id.nav_drawer_reportes -> {
                val intent = Intent(this, ReportesActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_drawer_configuracion -> {
                Intent(this, ConfiguracionActivity::class.java).also { startActivity(it) }
            }

            R.id.nav_drawer_cerrar_sesion -> {

                val dialog = BaseDialogConfirmationBasicHelper(this)
                dialog.showConfirmationDialog("¿Seguro que desea cerrar sesión?"){
                    //Aceptar
                    usuarioViewModel.logOutDrawer()
                }

            }
        }

        return false
    }

    protected fun allocateActivityTitle(titleString: String){
        if (supportActionBar != null) supportActionBar!!.title = titleString
    }

    protected fun setUserName(){
        try {
            val usuarioText = findViewById<TextView>(R.id.txvDrawerHeaderNombreUsuario)
            usuarioText.text = prefsApp.getUserName()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}
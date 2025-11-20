package com.mobile.massiveapp.ui.view.menu

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mobile.massiveapp.MassiveApp.Companion.prefsApp
import com.mobile.massiveapp.R
import com.mobile.massiveapp.core.ForegroundDatosMaestrosService
import com.mobile.massiveapp.core.SincronizarDatosWorker
import com.mobile.massiveapp.databinding.ActivityMenuBinding
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.view.cobranzas.CobranzasActivity
import com.mobile.massiveapp.ui.view.facturas.FacturasActivity
import com.mobile.massiveapp.ui.view.inventario.InventarioActivity
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.pedidocliente.PedidoClienteActivity
import com.mobile.massiveapp.ui.view.sociodenegocio.SocioNegocioActivity
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.ConfiguracionViewModel
import com.mobile.massiveapp.ui.viewmodel.DashboardViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MenuActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityMenuBinding
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val socioViewModel: SocioViewModel by viewModels()
    private val articuloViewModel: ArticuloViewModel by viewModels()
    private val configuracionViewModel: ConfiguracionViewModel by viewModels()
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private var foregroundDMService: Intent? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        allocateActivityTitle("Inicio")
        foregroundDMService = Intent(this, ForegroundDatosMaestrosService::class.java)


        /*verificarPermisosDeEscritura(binding.root)
        verificarPermisosDeLecturaYEscritura(binding.root)*/


            //Inicializa el servicio
        /*val serviceIntent = Intent(this, BackgroundService::class.java)
        serviceIntent.action = BackgroundService.Actions.START.toString()
        startService(serviceIntent)*/
        if (prefsApp.getChangeHappened() == "Y"){
            BaseDialogAlert(this).showConfirmationDialog("Debe sincronizar datos"){
                prefsApp.saveChangeHappened("N")
            }
        }

        generalViewModel.getAllGeneralMonedas()
        generalViewModel.dataGetAllGeneralMonedas.observe(this) { monedas ->
            try {
                SendData.instance.simboloMoneda = monedas[0].CurrName
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


            //Primera sincronización por Login
        if (intent.getBooleanExtra("login", false)){
            /*BaseDialogConfirmationBasicHelper(this).showConfirmationDialog(
                "¿Desea Sincronizar todos los datos ahora?"
            ){
                *//*val workRequest = OneTimeWorkRequestBuilder<SincronizarDatosWorker>()
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .setBackoffCriteria(
                        backoffPolicy = BackoffPolicy.LINEAR,
                        duration = Duration.ofSeconds(15)
                    )
                    .build()
                WorkManager.getInstance(applicationContext)
                    .enqueueUniqueWork(
                        "Sincronizacionn",
                        ExistingWorkPolicy.KEEP,
                        workRequest)*//*
                datosMaestrosViewModel.setSincronizarMaestrosRemote()
            }*/
        }


        binding.txvNumeroClientesActuales.paintFlags = binding.txvNumeroClientesActuales.paintFlags or 8
        binding.txvNumeroProductosActuales.paintFlags = binding.txvNumeroProductosActuales.paintFlags or 8


            //Get datos del DASHBOARD
        val moneda = SendData.instance.simboloMoneda
        lifecycleScope.launch {
            dashboardViewModel.dataTotalPedidos.collect{ binding.txvDashboardCantidadPedidosValue.text = "$it" }
        }
        lifecycleScope.launch {
            dashboardViewModel.dataTotalAcumuladoPedidos.collect{ binding.txvDashboardAcumuladoPedidosValue.text = "${SendData.instance.simboloMoneda} ${it.format(2)}"}
        }
        lifecycleScope.launch {
            dashboardViewModel.dataTotalPagos.collect{ binding.txvDashboardCantidadCobranzasValue.text = "$it" }
        }
        lifecycleScope.launch {
            dashboardViewModel.dataTotalAcumuladoPagos.collect{ binding.txvDashboardAcumuladoCobranzasValue.text = "${SendData.instance.simboloMoneda} ${it.format(2)}" }
        }
        lifecycleScope.launch {
            dashboardViewModel.dataTotalFacturas.collect{ binding.txvDashboardCantidadFacturasValue.text = "$it" }
        }
        lifecycleScope.launch {
            dashboardViewModel.dataTotalAcumuladoFacturas.collect{ binding.txvDashboardAcumuladoFacturasValue.text = "${SendData.instance.simboloMoneda} ${it.format(2)}" }
        }


        /****TEST GET ARTICULOS**/
        binding.cvMenuAcumuladoCobranzas.setOnClickListener {
            /*datosMaestrosViewModel.getArticulosFromEndpoint()*/
        }
        /****TEST GET ARTICULOS**/

        lifecycleScope.launch {
            socioViewModel.dataGetCountClientes.collect { cantidadClientes->
                try {
                    binding.txvNumeroClientesActuales.text = cantidadClientes.toString()
                    binding.txvNumeroClientesActuales.paintFlags = binding.txvNumeroClientesActuales.paintFlags or 8
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        lifecycleScope.launch {
            articuloViewModel.dataGetCountArticulos.collect { cantidadArticulos ->
                try {
                    binding.txvNumeroProductosActuales.text = cantidadArticulos.toString()
                    binding.txvNumeroProductosActuales.paintFlags = binding.txvNumeroProductosActuales.paintFlags or 8
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }





        //Click Dashboard---------------
        binding.cvDashboardClientes.setOnClickListener {
            /*configuracionViewModel.getAllTablasNoSincronizadas()
            configuracionViewModel.dataGetAllTablasNoSincronizadas.observeOnce(this){ tablasNoSincronizadas->
                if (tablasNoSincronizadas.isNotEmpty()){
                    val listaNombre = tablasNoSincronizadas.map { it.Tabla }
                    BaseSimpleInformativeDialog(
                        "Debe sincronizar estos datos:",
                        listaNombre.joinToString(separator = "\n ")
                    ).show(supportFragmentManager, "mensaje")
                } else{
                }
            }*/

            startActivity(Intent(this, SocioNegocioActivity::class.java))

        }

        binding.cvDashboardProductos.setOnClickListener { startActivity(Intent(this, InventarioActivity::class.java)) }


        binding.cvMenuCantidadPedidos.setOnClickListener { startActivity(Intent(this, PedidoClienteActivity::class.java)) }

        binding.cvMenuCantidadCobranzas.setOnClickListener { startActivity(Intent(this, CobranzasActivity::class.java)) }

        binding.cvMenuCantidadFacturas.setOnClickListener { startActivity(Intent(this, FacturasActivity::class.java)) }



    }





    @RequiresApi(Build.VERSION_CODES.O)
    fun initWorker(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        //Inicializa el Worker
        val workRequest = PeriodicWorkRequestBuilder<SincronizarDatosWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setInitialDelay(10, TimeUnit.MINUTES)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofSeconds(10)
            )
            .setConstraints(constraints)
            .build()

        configuracionViewModel.getConfiguracionActual()
        configuracionViewModel.dataGetConfiguracionActual.observe(this){ configuracion->
            try {
                if (configuracion.SincAutomatica){

                    /*if (isServiceRunning(SincronizarDatosWorker::class.java, this)){
                        throw Exception("Sincronización en curso")
                    }*/

                   /* val workRequest2 = OneTimeWorkRequestBuilder<SincronizarDatosWorker>()
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .setBackoffCriteria(
                            backoffPolicy = BackoffPolicy.LINEAR,
                            duration = Duration.ofSeconds(15)
                        )
                        .build()

                    WorkManager.getInstance(applicationContext)
                        .enqueueUniqueWork(
                            "SincronizacionDataWork",
                            ExistingWorkPolicy.KEEP,
                            workRequest2)*/



                    /*val workRequest2 = OneTimeWorkRequestBuilder<SincronizarDatosWorker>()
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .setBackoffCriteria(
                            backoffPolicy = BackoffPolicy.LINEAR,
                            duration = Duration.ofSeconds(15)
                        )
                        .build()
                    WorkManager.getInstance(applicationContext).enqueue(workRequest2)*/


                    val workManager = WorkManager.getInstance(applicationContext)

                    workManager.enqueueUniquePeriodicWork(
                        "SincronizacionDatosMaestross",
                        ExistingPeriodicWorkPolicy.KEEP,
                        workRequest)


                    //Observer del estado del Worker
                    /*workManager.getWorkInfosForUniqueWorkLiveData("SincronizacionDataWork").observe(this){
                        it.forEach { workInfo ->
                            if(workInfo.state == androidx.work.WorkInfo.State.ENQUEUED){
                                binding.txvUltimaHoraActualizacion.text = getHoraActual()
                            }
                            Toast.makeText(this, "Estado del Servicio: ${workInfo.state}", Toast.LENGTH_SHORT).show()
                        }
                    }*/

                    /**ALARM MANAGER**/
                    /*val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val alarmIntent = Intent(this, SincMaestrosBroadcastReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        this,
                        0,
                        alarmIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )


                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        intervaloEjecucion* 60 * 1000,
                        pendingIntent
                    )*//**ALARM MANAGER**/


                }
            } catch (e: Exception) {
                Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                Timber.e(e)
            }

        }
    }









    fun isServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val services = manager?.getRunningServices(Integer.MAX_VALUE)
        if (services != null) {
            for (service in services) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
        }
        return false
    }











    /*---------------------Barra de titulo----------------------*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_version_app, menu)
        generalViewModel.getBaseActual()
        generalViewModel.dataGetBaseActual.observe(this){ baseActual->
            menu?.findItem(R.id.nav_item_base_actual)?.setTitle(baseActual)
        }
        menu?.findItem(R.id.nav_item_version_app)?.setTitle(prefsApp.getVersionApp())
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when(item.itemId){
            R.id.nav_item_version_app->{
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
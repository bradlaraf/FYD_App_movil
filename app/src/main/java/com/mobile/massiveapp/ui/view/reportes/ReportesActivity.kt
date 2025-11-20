package com.mobile.massiveapp.ui.view.reportes

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityReportesBinding
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.pedidocliente.BuscarClienteActivity
import com.mobile.massiveapp.ui.view.util.clearExternalCache
import com.mobile.massiveapp.ui.view.util.clearInternalCache
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.mostrarCalendarioMaterial
import com.mobile.massiveapp.ui.viewmodel.ReportesViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable
import timber.log.Timber
import java.io.File


@AndroidEntryPoint
class ReportesActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityReportesBinding
    private lateinit var loadinDialog: BaseDialogLoadingCustom
    private val reportesViewModel: ReportesViewModel by viewModels()
    private val loginViewModel: UsuarioViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportesBinding.inflate(layoutInflater)
        setContentView(binding.root)



            //Control del PROGRESSBAR
        reportesViewModel.isLoading.observe(this){
            binding.progressBar.isVisible = it
        }


            //Set FECHA INICIO
        binding.clReportesFechaInicio.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mostrarCalendarioMaterial(
                    this,
                    binding.txvReportesFechaInicioValue.text.toString().ifEmpty { getFechaActual() }
                ){dia, mes, anio->
                    binding.txvReportesFechaInicioValue.text = "$anio-$mes-$dia"
                }
            }
        }

            //Set FECHA FIN
        binding.clReportesFechaFin.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mostrarCalendarioMaterial(
                    this,
                    binding.txvReportesFechaFinValue.text.toString().ifEmpty { getFechaActual() }
                ){dia, mes, anio->
                    binding.txvReportesFechaFinValue.text = "$anio-$mes-$dia"
                }
            }
        }

        showPdfOptions()

        binding.clReportesTipoReporte.setOnClickListener {
            showPdfOptions()
        }


            //LiveData del REPORTE DE ESTADO DE CUENTA
        reportesViewModel.dataGenerateReporteEstadoCuenta.observe(this){
            if (it){
                loadinDialog.onDismiss()
                abrirPDF("reporteEstadoCuenta.pdf")
            } else {
                Timber.tag("pdf").d("Error pdf")
                loadinDialog.onDismiss()
            }
        }

            //LiveData de SALDOS POR COBRAR
        reportesViewModel.dataGenerateReporteSaldosPorCobrar.observe(this){
            if (it){
                loadinDialog.onDismiss()
                abrirPDF("reporteSaldosPorCobrar.pdf")
            } else {
                Toast.makeText(this, "Error Pdf", Toast.LENGTH_LONG).show()
                loadinDialog.onDismiss()
            }
        }

            //LiveData de PRODUCTOS POR MARCA
        reportesViewModel.dataGenerateReporteProductosPorMarca.observe(this){
            if (it){
                loadinDialog.onDismiss()
                abrirPDF("reporteProductosPorMarca.pdf")
            } else {
                Toast.makeText(this, "Error Pdf", Toast.LENGTH_LONG).show()
                loadinDialog.onDismiss()
            }
        }

            //LiveData de PRE COBRANZA
        reportesViewModel.dataGenerateReportePreCobranza.observe(this){
            if (it){
                loadinDialog.onDismiss()
                abrirPDF("reportePreCobranza.pdf")
            } else {
                Toast.makeText(this, "Error Pdf", Toast.LENGTH_LONG).show()
                loadinDialog.onDismiss()
            }
        }

            //LiveData de AVANCES DE VENTAS
        reportesViewModel.dataGenerateReporteAvancesVentas.observe(this){
            if (it){
                loadinDialog.onDismiss()
                abrirPDF( "reporteAvanceDeVentas.pdf")
            } else {
                Toast.makeText(this, "Error Pdf", Toast.LENGTH_LONG).show()
                loadinDialog.onDismiss()
            }
        }

            //LiveData de VENTAS DIARIAS
        reportesViewModel.dataGenerateReporteVentasDiarias.observe(this){
            if (it){
                loadinDialog.onDismiss()
                abrirPDF("reporteVentasDiarias.pdf")
            } else {
                Toast.makeText(this, "Error Pdf", Toast.LENGTH_LONG).show()
                loadinDialog.onDismiss()
            }
        }

            //LiveData de DETALLE DE VENTAS
        reportesViewModel.dataGenerateReporteDetalleVentas.observe(this){
            if (it){
                loadinDialog.onDismiss()
                abrirPDF("reporteDetalleDeVentas.pdf")
            } else {
                Toast.makeText(this, "Error Pdf", Toast.LENGTH_LONG).show()
                loadinDialog.onDismiss()
            }
        }

            //LiveData Estado de sesion
        loginViewModel.dataGetEstadoSesion.observe(this){ estadoSesion->
            when(estadoSesion){
                is String->{
                    if (estadoSesion == "Y"){
                        makeReport()
                    } else if (estadoSesion == "N"){
                        BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                            //Aceptar
                            loginViewModel.logOut()
                        }
                    }
                }
                is DoError->{
                    showMessage(estadoSesion.ErrorMensaje)
                }
            }
        }

            //Livedata Cierre de Sesion
        cierreSesion()
    }

    private fun makeReport(){
        try {
            clearInternalCache(this)
            clearExternalCache(this)
        } catch (e:Exception){
            e.printStackTrace()
            showMessage(e.message.toString())
        }
        when(binding.txvReportesTipoReporteValue.text){
            "Saldos por Cobrar" -> {
                iniciarLoadingDialog("Saldos por Cobrar")
                reportesViewModel.generateReporteSaldosPorCobrar(
                    binding.txvReportesFechaInicioValue.text.toString(),
                    binding.txvReportesFechaFinValue.text.toString(),
                    this
                )
            }

            "Pre Cobranza"->{
                iniciarLoadingDialog("Pre Cobranza")
                reportesViewModel.generateReportePreCobranza(
                    binding.txvReportesFechaInicioValue.text.toString(),
                    binding.txvReportesFechaFinValue.text.toString(),
                    this
                )
            }
            "Avances de Ventas" -> {
                iniciarLoadingDialog("Avances de Ventas")
                reportesViewModel.generateReporteAvancesVentas(
                    binding.txvReportesFechaInicioValue.text.toString(),
                    binding.txvReportesFechaFinValue.text.toString(),
                    this
                )
            }
            "Ventas Diarias" -> {
                iniciarLoadingDialog("Ventas Diarias")
                reportesViewModel.generateReporteVentasDiarias(
                    binding.txvReportesFechaInicioValue.text.toString(),
                    binding.txvReportesFechaFinValue.text.toString(),
                    this
                )
            }
            "Detalle de Ventas" -> {
                iniciarLoadingDialog("Detalle de Ventas")
                reportesViewModel.generateReporteDetalleVentas(
                    binding.txvReportesFechaInicioValue.text.toString(),
                    binding.txvReportesFechaFinValue.text.toString(),
                    this
                )
            }

            else -> {
                Toast.makeText(this, "No se ha seleccionado un tipo de reporte", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun cierreSesion() {
        //LiveData Cierre de Sesion
        loginViewModel.isLoadingLogOut.observe(this){
            val loadingSimpleDialog = BaseDialogSImpleLoading(this)
            loadingSimpleDialog.startLoading("Cerrando Sesion...")

            if (!it){
                loadingSimpleDialog.onDismiss()
            }
        }
        loginViewModel.dataLogOut.observe(this) { error->
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




    fun showPdfOptions(){
        val listaOrdenada = listOf(
            "Saldos por Cobrar",
            "Estado de Cuenta",
            "Productos por Marca",
            "Pre Cobranza",
            "Avances de Ventas",
            "Ventas Diarias",
            "Detalle de Ventas"
        ).sorted()

        //Set TIPO DE REPORTE
        BaseDialogChecklistWithId(
            listaOrdenada
        ){ reporteElegido, id->

            when (reporteElegido) {

                "Estado de Cuenta" -> {
                    Intent(this, BuscarClienteActivity::class.java)
                        .apply { startForClienteSeleccionadoResult.launch(this)
                        }
                }

                "Productos por Marca" -> {
                    iniciarLoadingDialog("Productos por Marca")
                    reportesViewModel.generateReporteProductosPorMarca(this)
                }

                /*"Saldos por Cobrar"-> {
                    binding.txvReportesTipoReporteValue.text = reporteElegido
                    binding.clContainerReportes.isVisible = true
                    binding.clReportesFechaFin.isVisible = false
                    binding.clReportesFechaInicio.isVisible = false
                }*/

                /*"Ventas Diarias"-> {
                    binding.txvReportesFechaInicioTitle.text = "Fecha del reporte"
                    binding.txvReportesFechaInicioValue.text = getFechaActual()
                    binding.clReportesFechaInicio.isVisible = true
                    binding.clContainerReportes.isVisible = true
                    binding.txvReportesTipoReporteValue.text = reporteElegido

                    binding.clReportesFechaFin.isVisible = false
                }*/

                else -> {
                    binding.clContainerReportes.isVisible = true
                    binding.clReportesFechaInicio.isVisible = true
                    binding.clReportesFechaFin.isVisible = true

                    binding.txvReportesTipoReporteValue.text = reporteElegido
                    binding.txvReportesFechaInicioValue.text = getFechaActual()
                    binding.txvReportesFechaFinValue.text = getFechaActual()

                }
            }
        }.show(supportFragmentManager, "dialogo")
    }


    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            /*Toast.makeText(this, "OK", Toast.LENGTH_LONG).show()*/
        }
        if (result.resultCode == Activity.RESULT_CANCELED){
/*            try {
                showPdfOptions()
            } catch (e:Exception){
                e.printStackTrace()
            }*/
        }
    }

    private val startForClienteSeleccionadoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val cardCodeClienteSeleccionado = data?.getStringExtra("cardCode")
            if (!cardCodeClienteSeleccionado.isNullOrEmpty()){
                iniciarLoadingDialog("Estado de Cuenta")
                reportesViewModel.generateReporteEstadoCuenta(
                    cardCodeClienteSeleccionado,
                    this
                )
            }
        }
    }

    fun abrirPDF(nombreArchivo: String) {
        try {
            val file = File(this.filesDir, nombreArchivo)

            if (file.exists()) {
                // Obtén la URI del archivo usando FileProvider para garantizar el acceso seguro
                val uri = FileProvider.getUriForFile(this, "com.mobile.massiveapp.fileprovider", file)

                // Crea un intent para abrir el archivo PDF
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/pdf")
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startForResult.launch(intent)

                // Intenta resolver la actividad que maneja la visualización de PDF
                /*val pdfViewers = context.packageManager.queryIntentActivities(intent, 0)*/

                /*if (intent.resolveActivity(context.packageManager) != null) {*/
                /*} else {
                    // Si no hay visor de PDF instalado, puedes mostrar un mensaje al usuario o proporcionar otra acción adecuada
                    // Por ejemplo, abrir la tienda de aplicaciones para descargar un visor de PDF
                    val playStoreIntent = Intent(Intent.ACTION_VIEW)
                    playStoreIntent.data = Uri.parse("market://details?id=com.adobe.reader")
                    context.startActivity(playStoreIntent)
                }*/
            } else {
                Toast.makeText(this, "Error abriendo el PDF", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }




    private fun iniciarLoadingDialog(titulo: String){
        val gif = GifDrawable(this.resources, R.drawable.gif_document)
        loadinDialog = BaseDialogLoadingCustom(this, "Generando reporte de $titulo", gif)
        loadinDialog.startLoading()
    }



    /*----------------------------NAV BAR------------------------------*/




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_check -> {
                    loginViewModel.getEstadoSesion()
                }
            }
        return super.onOptionsItemSelected(item)
    }
}
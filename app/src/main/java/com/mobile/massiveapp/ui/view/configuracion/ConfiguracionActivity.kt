package com.mobile.massiveapp.ui.view.configuracion

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.util.getSerialID
import com.mobile.massiveapp.databinding.ActivityConfiguracionBinding
import com.mobile.massiveapp.domain.custom.ColorModeProviderImpl
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.ui.base.BaseDialogEdt
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.menu.MenuActivity
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.viewmodel.ConfiguracionViewModel
import com.mobile.massiveapp.ui.viewmodel.DatosMaestrosViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable
import timber.log.Timber


@AndroidEntryPoint
class ConfiguracionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracionBinding
    private val generalViewModel: GeneralViewModel by viewModels()
    private val configuracionViewModel: ConfiguracionViewModel by viewModels()
    private val datosMaestrosViewModel: DatosMaestrosViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val colorModeProvider: ColorModeProvider = ColorModeProviderImpl()
    private var foregroundDMService: Intent? = null
    private var mensajeDialog = ""
    private var ARTICULO = 0
    private var FACTURAS = 0
    private var CLIENTES = 0
    private var USAR_LIMITES = false
    private val ACTIVO_TEXT = "Habilitado"
    private val INACTIVO_TEXT = "Deshabilitado"

    private val IP_LOCAL = "192.168.1.3"
    private val IP_PUBLICA = "45.232.150.245"
    private val PUERTO = "82"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val loadingDialog = BaseDialogSImpleLoading(this)
        loadingDialog.startLoading()


        setValoresIniciales()
        setDefaulUi()



            //Se trae la CONFIGURACION POR DEFECTO
        configuracionViewModel.getConfiguracionActual()
        configuracionViewModel.dataGetConfiguracionActual.observe(this){ configuracionActual->
            try {
                binding.txvConfiguracionVerionAplicacionValue.text = configuracionActual.AppVersion
                binding.txvConfiguracionIpPublicaValue.text = configuracionActual.IpPublica
                binding.txvConfiguracionIpLocalValue.text = configuracionActual.IpLocal
                binding.cbConfiguracionSetTipoIp.isChecked = configuracionActual.SetIpPublica
                binding.txvConfiguracionSetTipoIpValue.text = if (configuracionActual.SetIpPublica) "Usando Ip Pública" else "Usando Ip Local"
                binding.txvConfiguracionPuertoValue.text = configuracionActual.NumeroPuerto
                binding.cbSincronizacionAutomatica.isChecked = configuracionActual.SincAutomatica
                binding.autoComplete.setText(configuracionActual.BaseDeDatos, false)
                binding.txvConfiguracionSincronizacionAutomaticaValue.text = if (configuracionActual.SincAutomatica) ACTIVO_TEXT else INACTIVO_TEXT
                binding.txvConfiguracionLimitesValue.text = if (configuracionActual.UsarLimites) ACTIVO_TEXT else INACTIVO_TEXT
                binding.txvConfiguracionUsarConfirmacionValue.text = if (configuracionActual.UsarConfirmacion) ACTIVO_TEXT else INACTIVO_TEXT
                binding.clConfiguracionTimerServicio.isVisible = configuracionActual.SincAutomatica
                binding.cbSincronizacionAutomatica.isChecked = configuracionActual.SincAutomatica
                binding.cbConfiguracionUsarConfirmacion.isChecked = configuracionActual.UsarConfirmacion
                binding.txvConfiguracionLimitesLineasPedidosValue.text = configuracionActual.LimiteLineasPedido.toString()
                loadingDialog.onDismiss()
            } catch (e: Exception){
                Timber.d("Error al traer la configuracion actual")
            }
        }






        //LiveData save configuracion
        configuracionViewModel.dataSaveConfiguracion.observe(this){ success->
            if (success){
                usuarioViewModel.getUsuarioFromDatabase()
                usuarioViewModel.dataGetUsuarioFromDatabase.observeOnce(this){ usuario->
                    if (usuario.Code.isNotEmpty()){
                        Intent(this, MenuActivity::class.java).also { startActivity(it) }
                        Toast.makeText(this, "Configuracion guardada", Toast.LENGTH_SHORT).show()
                    } else {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }


    }

    private fun setValoresIniciales() {
        //ID del dispositivo
        binding.txvConfiguracionIdDispositivoValue.text = getSerialID(this)
        /*binding.txvConfiguracionIdDispositivoValue.text = "cb50c78480b61d8a"   //JOEL*/
        /*binding.txvConfiguracionIdDispositivoValue.text = "2020f4518d636087"*/


        //Base de datos default
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            if (usuario.Code.isEmpty()){
                /*setBaseDefault()*/
                binding.clConfiguracionReinicializarMaestros.isVisible = false
            }
        }
    }

    private fun setDefaulUi() {

        // Loading dialog
        val gif = GifDrawable(this.resources, R.drawable.gif_loading)
        val loadinDialog = BaseDialogLoadingCustom(this, "Reinicializando Maestros", gif)

        datosMaestrosViewModel.isLoading.observe(this){
            if (it){
                loadinDialog.startLoading()
            } else {
                loadinDialog.onDismiss()
            }
        }

        setTipoDeIp()
        setUsarSincAutomatica()
        setUsarConfirmacion()

        //Limites
        binding.clConfiguracionLimites.setOnClickListener {
            startForLimitesResult.launch(Intent(
                this, LimitesActivity::class.java)
            )
        }

        //DocumentoDireccion de la IP Publica
        binding.clConfiguracionIpPublica.setOnClickListener {
            BaseDialogEdt(
                textEditable = binding.txvConfiguracionIpPublicaValue.text.toString()
            ) { ipPublica->
                binding.txvConfiguracionIpPublicaValue.text = ipPublica
            }.show(supportFragmentManager, "dialog")
        }

        //DocumentoDireccion de la IP Local
        binding.clConfiguracionIpLocal.setOnClickListener {
            BaseDialogEdt(
                textEditable = binding.txvConfiguracionIpLocalValue.text.toString()
            ) { ipLocal->
                binding.txvConfiguracionIpLocalValue.text = ipLocal
            }.show(supportFragmentManager, "dialog")
        }


        //Numero de Puerto
        binding.clConfiguracionPuerto.setOnClickListener {
            BaseDialogEdt(
                textEditable = binding.txvConfiguracionPuertoValue.text.toString()
            ) { numeroPuerto->
                binding.txvConfiguracionPuertoValue.text = numeroPuerto
            }.show(supportFragmentManager, "dialog")
        }

        //Limite lineas Pedidos
        binding.clConfiguracionLimiteLineasPedidos.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                tipo = "decimal",
                textEditable = binding.txvConfiguracionLimitesLineasPedidosValue.text.toString()
            ) { limiteLineas->
                binding.txvConfiguracionLimitesLineasPedidosValue.text = limiteLineas
            }.show(supportFragmentManager, "limiteLineasDialog")
        }

        //Reinicializar Datos Maestros
        binding.btnConfiguracionReinicializarMaestros.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder
                .setTitle("¿Seguro que desea reinicializar los datos maestros?")
                .setPositiveButton("Aceptar"){ _, _ ->
                    mensajeDialog = "Reinicializando Maestros"
                    datosMaestrosViewModel.sendReinicializacionDeMaestros()
                    datosMaestrosViewModel.dataSendReinicializacionDeMaestros.observe(this){ reinicioSuccess->
                        if (reinicioSuccess){
                            Toast.makeText(this, "Reinicialización exitosa", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancelar"){ _, _ ->

                }
            val dialog = builder.create()
            dialog.show()

        }

        //Bases de Datos Disponibles
        binding.autoComplete.setOnClickListener {
            val gif = GifDrawable(this.resources, R.drawable.gif_loading)
            val loadinDialog = BaseDialogLoadingCustom(this, "Obteniendo las bases disponibles", gif)
            loadinDialog.startLoading()
            val ipAUsar = if (binding.cbConfiguracionSetTipoIp.isChecked) binding.txvConfiguracionIpPublicaValue.text.toString()
            else binding.txvConfiguracionIpLocalValue.text.toString()

            generalViewModel.getAllBasesDisponibles(
                ipPublica = ipAUsar,
                puerto = binding.txvConfiguracionPuertoValue.text.toString()
            )
            generalViewModel.dataGetAllBasesDisponibles.observe(this){ listaBases->
                if (listaBases.isNotEmpty()){
                    loadinDialog.onDismiss()
                    binding.autoComplete.setAdapter(
                        ArrayAdapter(
                            this,
                            android.R.layout.simple_list_item_1,
                            listaBases.map { it.DataBase }
                        )
                    )
                    binding.autoComplete.showDropDown()
                } else {
                    loadinDialog.onDismiss()
                    Toast.makeText(this, "No hay bases de datos disponibles", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setBaseDefault() {
        generalViewModel.getAllBasesDisponibles(
            ipPublica = IP_PUBLICA,
            puerto = PUERTO
        )
        generalViewModel.dataGetAllBasesDisponibles.observeOnce(this){ listaBases->
            if (listaBases.isNotEmpty()){

                val baseDefault = listaBases.filter { it.Code == "0001" }.firstOrNull()?.DataBase?:"MSV_MOVIL_PRAGSA"
                binding.autoComplete.setText(baseDefault)


                binding.autoComplete.setAdapter(
                    ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        listaBases.map { it.DataBase }
                    )
                )
            }
        }
    }

    private fun setTipoDeIp(){
        binding.clConfiguracionSetTipoIp.setOnClickListener {
            binding.cbConfiguracionSetTipoIp.isChecked = !binding.cbConfiguracionSetTipoIp.isChecked

            if (binding.cbConfiguracionSetTipoIp.isChecked){
                binding.txvConfiguracionSetTipoIpValue.text = "Usando Ip Pública"
            } else {
                binding.txvConfiguracionSetTipoIpValue.text = "Usando Ip Local"
            }
        }

        //Checkbox de la Ip publica
        binding.cbConfiguracionSetTipoIp.setOnClickListener {
            if (binding.cbConfiguracionSetTipoIp.isChecked){
                binding.txvConfiguracionSetTipoIpValue.text = "Usando Ip Pública"
            } else {
                binding.txvConfiguracionSetTipoIpValue.text = "Usando Ip Local"
            }
        }
    }

    private fun setUsarSincAutomatica() {
        //Sincronizacion Automatica
        binding.clConfiguracionSincronizacionAutomatica.setOnClickListener {
            binding.cbSincronizacionAutomatica.isChecked = !binding.cbSincronizacionAutomatica.isChecked

            if (binding.cbSincronizacionAutomatica.isChecked){
                binding.txvConfiguracionSincronizacionAutomaticaValue.text = "Habilitado"
                binding.clConfiguracionTimerServicio.isVisible = true
            } else {
                binding.txvConfiguracionSincronizacionAutomaticaValue.text = "Desabilitado"
                binding.clConfiguracionTimerServicio.isVisible = false
            }
        }


        //Checkbox de la sincronizacion automatica
        binding.cbSincronizacionAutomatica.setOnClickListener {
            if (binding.cbSincronizacionAutomatica.isChecked){
                binding.txvConfiguracionSincronizacionAutomaticaValue.text = "Habilitado"
                binding.clConfiguracionTimerServicio.isVisible = true
            } else {
                binding.txvConfiguracionSincronizacionAutomaticaValue.text = "Desabilitado"
                binding.clConfiguracionTimerServicio.isVisible = false
            }
        }
    }

    private fun setUsarConfirmacion() {
        binding.clConfiguracionUsarConfirmacion.setOnClickListener {
            binding.cbConfiguracionUsarConfirmacion.isChecked = !binding.cbConfiguracionUsarConfirmacion.isChecked

            if (binding.cbConfiguracionUsarConfirmacion.isChecked){
                binding.txvConfiguracionUsarConfirmacionValue.text = "Habilitado"
            } else {
                binding.txvConfiguracionUsarConfirmacionValue.text = "Desabilitado"
            }
        }


        //Checkbox de la sincronizacion automatica
        binding.cbConfiguracionUsarConfirmacion.setOnClickListener {
            if (binding.cbConfiguracionUsarConfirmacion.isChecked){
                binding.txvConfiguracionUsarConfirmacionValue.text = "Habilitado"
            } else {
                binding.txvConfiguracionUsarConfirmacionValue.text = "Desabilitado"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun cambiarColorPrincipalTheme(){
        // Obtiene el color primario actual del tema
        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        val colorPrimary = typedValue.data

        // Obtiene el nuevo color primario
        val newColorPrimary = ContextCompat.getColor(this, R.color.color_black)

        // Crea un nuevo tema con el color primario actualizado
        val newTheme = this.resources.newTheme()
        newTheme.applyStyle(R.style.Base_Theme_MassiveApp, true)
        newTheme.applyStyle(R.style.Theme_MassiveApp, true)
        newTheme.applyStyle(R.style.Theme_AppCompat_Starting, true)
        newTheme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        typedValue.data = newColorPrimary

        // Establece el nuevo tema a la actividad
        setTheme(newTheme)

        // Actualiza la UI para reflejar el cambio de tema
        recreate()

    }

    val startForLimitesResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == RESULT_OK){
            val data: Intent? = result.data
            USAR_LIMITES = data?.getBooleanExtra("usarLimites", false)?: false
            ARTICULO = data?.getIntExtra("articulo", 0)?:0
            CLIENTES = data?.getIntExtra("clientes", 0)?:0
            FACTURAS = data?.getIntExtra("facturas", 0)?:0

            if (USAR_LIMITES){
                binding.txvConfiguracionLimitesValue.text = ACTIVO_TEXT
            } else {
                binding.txvConfiguracionLimitesValue.text = INACTIVO_TEXT
            }
        }
    }




    fun allFieldsAreEmpty(): Boolean {
        return binding.txvConfiguracionIpPublicaValue.text.toString().isEmpty() ||
                binding.txvConfiguracionPuertoValue.text.toString().isEmpty() ||
                binding.autoComplete.text.toString().isEmpty() ||
                binding.autoComplete.text.toString() == "Sociedad"
    }



    /**************************NAVBAR***************************/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_check -> {
                if (allFieldsAreEmpty()){
                    Toast.makeText(this, "Debe elegir un puerto e IP", Toast.LENGTH_LONG).show()
                } else {

                    configuracionViewModel.saveConfiguracion(
                        DoConfiguracion(
                            IpPublica =     binding.txvConfiguracionIpPublicaValue.text.toString(),
                            IpLocal = binding.txvConfiguracionIpLocalValue.text.toString(),
                            NumeroPuerto =  binding.txvConfiguracionPuertoValue.text.toString(),
                            BaseDeDatos =   binding.autoComplete.text.toString(),
                            SincAutomatica = binding.cbSincronizacionAutomatica.isChecked,
                            IMEI =           binding.txvConfiguracionIdDispositivoValue.text.toString(),
                            TimerServicio =  if (binding.txvConfiguracionSincronizacionAutomaticaValue.text.toString() == "Habilitado") binding.edtConfiguracionIntervaloSincronizacion.text.toString().toInt() else 5,
                            UsarLimites = USAR_LIMITES,
                            TopArticulo = ARTICULO,
                            TopFactura = FACTURAS,
                            TopCliente = CLIENTES,
                            SetIpPublica = binding.cbConfiguracionSetTipoIp.isChecked,
                            UsarConfirmacion = false,
                            LimiteLineasPedido = binding.txvConfiguracionLimitesLineasPedidosValue.text.toString().toIntOrNull()?:25,
                            AppVersion = packageManager.getPackageInfo(packageName, 0).versionName
                        )
                    )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}

interface ColorModeProvider {
    @StyleRes
    fun getColoredTheme(): Int
    fun switchColor()
}
package com.mobile.massiveapp.ui.view.sociodenegocio

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.mobile.massiveapp.R
import com.mobile.massiveapp.core.ConnectivityObserver
import com.mobile.massiveapp.databinding.ActivityNuevoSocioNegocioBinding
import com.mobile.massiveapp.domain.model.DoConsultaDocumento
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogEdt
import com.mobile.massiveapp.ui.view.util.setOnClickListeners
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.util.agregarSocioNegocio
import com.mobile.massiveapp.ui.view.util.formatearFecha
import com.mobile.massiveapp.ui.view.util.getCodigoDeDocumentoActual
import com.mobile.massiveapp.ui.view.util.hideKeyboard
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import pl.droidsonroids.gif.GifDrawable


@AndroidEntryPoint
class NuevoSocioNegocioActivity : AppCompatActivity() {
    @Inject
    lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var binding: ActivityNuevoSocioNegocioBinding
    private var newSocioRucConsulta = DoConsultaDocumento()   //Datos del RUC Consultado
    private var infomacionSocioHash = HashMap<String, Any>()
    private val socioViewModel: SocioViewModel by viewModels()
    private val articuloViewModel: ArticuloViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var ZONA = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoSocioNegocioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDefaultUI()

        //Loading Save Socio
        val gif = GifDrawable(this.resources, R.drawable.gif_loading)
        val loadingDialog = BaseDialogLoadingCustom(this, "Enviando Socio...", gif)
        socioViewModel.insertSocioIsLoading.observe(this){
            if (it){
                loadingDialog.startLoading()
            } else {
                loadingDialog.onDismiss()
            }
        }

            //Datos Usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            try {
                infomacionSocioHash["usuarioCode"] = usuario.Code
                infomacionSocioHash["serieSocio"] = usuario.DefaultSNSerieCli
                infomacionSocioHash["slpCode"] = usuario.DefaultSlpCode
                infomacionSocioHash["currency"] = usuario.DefaultCurrency
            } catch (e: Exception){
                e.printStackTrace()
            }
        }


            //General Empleados
        generalViewModel.getEmpleadoDeVentas()
        generalViewModel.dataGetEmpleadoDeVemtas.observe(this){ empleadoDeVentas->
            try {
                binding.edtNuevoSnEmpleadoDPVentas.text = empleadoDeVentas.SlpName
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

            //General Indicadores
        generalViewModel.getAllGeneralIndicadores()
        generalViewModel.dataGetAllGeneralIndicadores.observe(this){ listaIndicadores->
            try {
                binding.txvNuevoSnIndicadorValue.text = listaIndicadores[0].Name
            } catch (e: Exception){
                e.printStackTrace()
            }

            binding.clNuevoSnIndicador.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvNuevoSnIndicadorValue.text.toString(),
                    listaIndicadores.map { it.Name }
                ){ indicadorSeleccionado, id->
                    binding.txvNuevoSnIndicadorValue.text = indicadorSeleccionado
                }.show(supportFragmentManager, "Indicador Dialog")
            }
        }

            //General Socio Grupos
        generalViewModel.getAllGeneralGruposSocios()
        generalViewModel.dataGetAllGeneralGruposSocios.observe(this){ listaGrupos->
            try {
                binding.txvNuevoSnGrupoValue.text = listaGrupos[0].GroupName
                infomacionSocioHash["groupCode"] = listaGrupos[0].GroupCode.toInt()
            } catch (e: Exception){
                e.printStackTrace()
            }

            binding.clNuevoSnGrupo.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvNuevoSnGrupoValue.text.toString(),
                    listaGrupos.map { it.GroupName }
                ){ grupoSeleccionado, id ->
                    binding.txvNuevoSnGrupoValue.text = grupoSeleccionado
                    infomacionSocioHash["groupCode"] = listaGrupos[id].GroupCode.toInt()
                }.show(supportFragmentManager, "Grupo Dialog")
            }
        }

            //General Condiciones de pago
        generalViewModel.getAllGeneralCondiciones()
        generalViewModel.dataGetAllGeneralCondiciones.observe(this){ listaCondiciones->
            try {
                binding.txvNuevoSnCondicionPagoValue.text = listaCondiciones[0].PymntGroup
                infomacionSocioHash["condicionPago"] = listaCondiciones[0].GroupNum
            } catch (e: Exception){
                e.printStackTrace()
            }

            /*binding.clNuevoSnCondicioPago.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvNuevoSnCondicionPagoValue.text.toString(),
                    listaCondiciones.map { it.PymntGroup }
                ){ condicionSeleccionada, id->
                    binding.txvNuevoSnCondicionPagoValue.text = condicionSeleccionada
                    infomacionSocioHash["condicionPago"] = listaCondiciones[id].GroupNum
                }.show(supportFragmentManager, "Condicion Dialog")
            }*/
        }


            //Lista de Precios
        articuloViewModel.getAllArticuloListaPrecios()
        articuloViewModel.dataGetAllArticuloListaPrecios.observe(this){ listaPrecios->
            try {
                binding.edtNuevoSnListaPrecios.text = listaPrecios[0].ListName
                infomacionSocioHash["listaPrecios"] = listaPrecios[0].ListNum
            } catch (e: Exception){
                e.printStackTrace()
            }
            hideKeyboard(binding.root)
            binding.clNuevoSnListaPrecios.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.edtNuevoSnListaPrecios.text.toString(),
                    listaPrecios.map { lista-> lista.ListName }
                ){ listaSeleccionada, id->
                    binding.edtNuevoSnListaPrecios.text = listaSeleccionada
                    infomacionSocioHash["listaPrecios"] = listaPrecios[id].ListNum
                }.show(supportFragmentManager, "Lista de precios Dialog")
            }
        }

            //Telefono 1
        binding.clNuevoSnTelefono1.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                tipo = "phone",
                binding.txvNuevoSnTelefono1Value.text.toString()
            ){ textoTelefono1->
                binding.txvNuevoSnTelefono1Value.text = textoTelefono1
            }.show(supportFragmentManager, "BaseDialogChecklist")
        }

            //Telefono 2
        binding.clNuevoSnTelefono2.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                tipo = "phone",
                binding.txvNuevoSnTelefono2Value.text.toString()
            ){ textoTelefono2->
                binding.txvNuevoSnTelefono2Value.text = textoTelefono2
            }.show(supportFragmentManager, "BaseDialogChecklist")
        }

            //Telefono Móvil
        binding.clNuevoSnTelefonoMovil.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                tipo= "phone",
                binding.txvNuevoSnTelefonoMovilValue.text.toString()
            ){ textoTelefonoMovil->
                binding.txvNuevoSnTelefonoMovilValue.text = textoTelefonoMovil
            }.show(supportFragmentManager, "BaseDialogChecklist")
        }

        //Zonas
        generalViewModel.getAllZonas()
        generalViewModel.dataGetAllZonas.observe(this){ listaZonas->
            binding.clNuevoSnZonas.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvNuevoSnZonasValue.text.toString(),
                    listaZonas.map { it.Name }
                ){ zonaSeleccionada, id->
                    binding.txvNuevoSnZonasValue.text = zonaSeleccionada
                    infomacionSocioHash["zona"] = listaZonas[id].Code
                }.show(supportFragmentManager, "ZonasDialog")
            }
        }

            //Codigo de Documento
        binding.txvNuevoSnCodigoDocumentoValue.text = getCodigoDeDocumentoActual(this)



            //LiveData la consulta de RUC
        socioViewModel.consultaRucFromDatabase.observe(this){ rucConsulta->
            try {
                binding.edtNuevoSnCodigo.text = if(rucConsulta.TipoDocumento == "6") "C${rucConsulta.NumeroDocumento}" else "C000${rucConsulta.NumeroDocumento}"
                newSocioRucConsulta = rucConsulta
                binding.edtNuevoSnDocumento.text =              rucConsulta.NumeroDocumento
                binding.edtNuevoSnNombre.text =                 if (rucConsulta.TipoDocumento == "6") rucConsulta.RazonSocial else "${rucConsulta.PrimerNombre} ${rucConsulta.SegundoNombre} ${rucConsulta.ApellidoPaterno} ${rucConsulta.ApellidoMaterno}"
                binding.edtNuevoSnDestinatarioFactura.text =    rucConsulta.Calle
            } catch (e: Exception){
                e.printStackTrace()
            }
        }


        //Lista de elementos que desplegaran un Dialog con edt
        val edtDialogList  = listOf(
            binding.edtNuevoSnCodigo,
            binding.edtNuevoSnNombre,
        )
        edtDialogList.setOnClickListeners {view ->
            BaseDialogEdt(view.text.toString(), onRegisterClickListener = { value ->
                if (value.isEmpty()){
                    return@BaseDialogEdt
                }else {
                    view.setText(value)
                }
            }
            ).show(supportFragmentManager, "BaseDialogEdt")
        }




        // (Btn) Consultar el RUC
        binding.clNuevoSnDocumento.setOnClickListener {
            Intent(this, ConsultaDocumentoActivity::class.java)
                .putExtra("accDocEntry", binding.txvNuevoSnCodigoDocumentoValue.text.toString())
                .also {
                    startForResult.launch(it)
                }
        }

        // (Btn) Elegir una direccion FISCAL
        binding.clNuevoSnDestinatarioFactura.setOnClickListener {
            if (binding.edtNuevoSnCodigo.text.toString().isNotEmpty()){
                Intent(this, DireccionesActivity::class.java)
                    .putExtra("cardCode", binding.edtNuevoSnCodigo.text.toString())
                    .putExtra("accDocEntry", binding.txvNuevoSnCodigoDocumentoValue.text.toString())
                    .putExtra("tipo", "B")
                    .also {
                        startForResultDireccion.launch(it)
                    }
            } else {
                Toast.makeText(this, "Debe consultar el documento primero", Toast.LENGTH_SHORT).show()
            }
        }


        // (Btn) Elegir una direccion de DESPACHO
        binding.clNuevoSnDestinatario.setOnClickListener {
            if (binding.edtNuevoSnCodigo.text.toString().isNotEmpty()){
                Intent(this, DireccionesActivity::class.java)
                    .putExtra("cardCode", binding.edtNuevoSnCodigo.text.toString())
                    .putExtra("accDocEntry", binding.txvNuevoSnCodigoDocumentoValue.text.toString())
                    .putExtra("tipo", "S")
                    .also {
                        startForResultDireccion.launch(it)
                    }
            } else {
                Toast.makeText(this, "Debe consultar el documento primero", Toast.LENGTH_SHORT).show()
            }
        }


        // (Btn) Elegir un Contacto Principal
        binding.clNuevoSnContactoPrincipal.setOnClickListener {
            if (binding.edtNuevoSnCodigo.text.toString().isNotEmpty()){
                Intent(this, ContactosActivity::class.java)
                    .putExtra("cardCode", "${binding.edtNuevoSnCodigo.text}")
                    .putExtra("accDocEntry", binding.txvNuevoSnCodigoDocumentoValue.text.toString())
                    .also {
                        startForResultDocumentoContacto.launch(it)
                    }

            } else {
                Toast.makeText(this, "Debe consultar el documento primero", Toast.LENGTH_SHORT).show()
            }
        }





        // (Btn) Correo Electronico
        binding.clNuevoSnCorreoElectronico.setOnClickListener {
            BaseDialogEdtWithTypeEdt("email", binding.edtNuevoSnCorreoElectronico.text.toString()){ correo->
                if (correo.isNotEmpty()) {
                    binding.edtNuevoSnCorreoElectronico.text = correo
                }
            }.show(supportFragmentManager, "BaseDialogChecklist")
        }

        //LiveData Cierre de sesion
        cierreSesion()

    }

    private fun setDefaultUI() {
        binding.clNuevoSnCodigoDocumento.isVisible = false
    }


    // (Btn) Consultar el RUC
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val succsessResponse = data?.getBooleanExtra("insertSuccess", false)
            val tipo = data?.getStringExtra("tipo")

            if (succsessResponse == true){
                socioViewModel.getConsultaRucFromDatabase()
            }
        }
    }


    // (Btn) Elegir una direccion de FISCAL
    val startForResultDireccion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val tipo = data?.getStringExtra("tipo")
            val direccion = data?.getStringExtra("direccion")
            if (!direccion.isNullOrEmpty()){
                if ( tipo == "B"){
                    binding.edtNuevoSnDestinatarioFactura.text = direccion
                } else {
                    binding.edtNuevoSnDestinatario.text = direccion
                }
            }
        }
    }


    // (Btn) Elegir un DocumentoContacto Principal
    val startForResultDocumentoContacto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val contacto = data?.getStringExtra("contacto")
            if (!contacto.isNullOrEmpty()){
                binding.edtNuevoSnContactoPrincipal.text = contacto
            }
        }
    }
    private fun showDialogConfirmBack(){
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Estás seguro de continuar, se perderán las direcciones creadas?")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Aceptar") { _, _ ->

                socioViewModel.eliminarContactosYDireccionesPorAccDocEntry(binding.txvNuevoSnCodigoDocumentoValue.text.toString())

                socioViewModel.dataEliminarContactosYDireccionesPorAccDocEntry.observe(this){ response->
                    if (response){
                        Toast.makeText(this, "Se eliminaron las direcciones creadas", Toast.LENGTH_SHORT).show()
                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("No se pudieron eliminar las direcciones creadas")
                            .setPositiveButton("Aceptar"){dialog, _ ->
                                dialog.dismiss()
                            }.create()
                    }
                }
            }
            .create()

        alertDialog.show()
    }

    private fun cierreSesion() {

        //LiveData Cierre de Sesion
        usuarioViewModel.isLoadingLogOut.observe(this){
            val loadingSimpleDialog = BaseDialogSImpleLoading(this)
            if (it){
                loadingSimpleDialog.startLoading()
            } else {
                loadingSimpleDialog.onDismiss()
            }
        }
        usuarioViewModel.dataLogOut.observe(this) { error->
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




    /*----------------BARRA DE TITULO - NAV -------------------*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo_sn, menu)
        menu?.findItem(R.id.app_bar_connectivity_status)?.isVisible = false
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (binding.edtNuevoSnCodigo.text.isNotEmpty()){
                    showDialogConfirmBack()
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
                true
            }


            //Validacion antes de crear el nuevo SN
            R.id.app_bar_check -> {
                try {
                    val fieldEmpty = fieldsAreEmpty()
                    if (fieldEmpty.isNotEmpty()){
                        throw Exception("Debe llenar el campo de $fieldEmpty")
                    } else {
                        socioViewModel.insertSocioNuevoAwait(
                            agregarSocioNegocio(
                                cardCode =          binding.edtNuevoSnCodigo.text.toString(),
                                cardName =          binding.edtNuevoSnNombre.text.toString(),
                                numeroDocumento =   binding.edtNuevoSnDocumento.text.toString(),
                                slpCode =           infomacionSocioHash["slpCode"] as Int,
                                activo =            newSocioRucConsulta.Activo,
                                habido =            newSocioRucConsulta.Habido,
                                indicador =         newSocioRucConsulta.Indicador,
                                accDocEntry =       binding.txvNuevoSnCodigoDocumentoValue.text.toString(),
                                tipoDocumento =     newSocioRucConsulta.TipoDocumento,
                                groupCode =         infomacionSocioHash["groupCode"] as Int,
                                groupNum =          infomacionSocioHash["condicionPago"] as Int,
                                listNum =           infomacionSocioHash["listaPrecios"] as Int,
                                telefono1 =         binding.txvNuevoSnTelefono1Value.text.toString(),
                                telefono2 =         binding.txvNuevoSnTelefono2Value.text.toString(),
                                serieSocio =        infomacionSocioHash["serieSocio"] as Int,
                                currency =          infomacionSocioHash["currency"] as String,
                                telefonoMovil =     binding.txvNuevoSnTelefonoMovilValue.text.toString(),
                                usuario =           infomacionSocioHash["usuarioCode"] as String,
                                primerNombre =      newSocioRucConsulta.PrimerNombre,
                                apellidoPaterno =   newSocioRucConsulta.ApellidoPaterno,
                                apellidoMaterno =   newSocioRucConsulta.ApellidoMaterno,
                                segundoNombre =     newSocioRucConsulta.SegundoNombre,
                                agenteRetenedor =   newSocioRucConsulta.AgenteRetenedor,
                                agentePercepcion =  newSocioRucConsulta.AgentePercepcion,
                                buenContribuyente = newSocioRucConsulta.BuenContribuyente,
                                tipoPersona =       newSocioRucConsulta.TipoPersona,
                                personaNaturalConNegocio = newSocioRucConsulta.PersonaNaturalConNegocio,
                                consultado =        newSocioRucConsulta.Consulta,
                                correo =            binding.edtNuevoSnCorreoElectronico.text.toString(),
                                zone =              "",
                                fechaConsulta = formatearFecha(newSocioRucConsulta.Fecha),

                                )
                        )
                        socioViewModel.dataInsertSocioNuevoAwait.observe(this){ response->
                            when(response.ErrorCodigo){
                                500 ->{
                                    BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                                        //Aceptar
                                        usuarioViewModel.logOut()
                                    }
                                }
                                0 -> {
                                    showMessage(response.ErrorMensaje)
                                    setResult(RESULT_OK)
                                    onBackPressedDispatcher.onBackPressed()
                                }
                                else -> { showMessage(response.ErrorMensaje) }
                            }
                        }

                    }
                } catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fieldsAreEmpty(): String {
        val emptyFields = mutableListOf<String>()
        if (binding.edtNuevoSnDestinatarioFactura.text.isEmpty()) emptyFields.add("Dirección fiscal")
        if (binding.edtNuevoSnDestinatario.text.isEmpty()) emptyFields.add("Dirección de despacho")
        if (binding.txvNuevoSnCodigo.text.isEmpty()) emptyFields.add("Código")
        //if (binding.txvNuevoSnZonasValue.text.isEmpty()) emptyFields.add("Zona")
        if (binding.txvNuevoSnNombre.text.isEmpty()) emptyFields.add("Nombre")
        if (binding.edtNuevoSnDocumento.text.isEmpty()) emptyFields.add("Documento")
        return emptyFields.joinToString()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showDialogConfirmBack()
        setResult(RESULT_CANCELED)
    }
}
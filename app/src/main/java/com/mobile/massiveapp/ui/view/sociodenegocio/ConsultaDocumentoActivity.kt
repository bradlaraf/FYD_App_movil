package com.mobile.massiveapp.ui.view.sociodenegocio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.mobile.massiveapp.R
import com.mobile.massiveapp.core.ConnectivityObserver
import com.mobile.massiveapp.core.NetworkConnectivityObserver
import com.mobile.massiveapp.databinding.ActivityConsultaDocumentoBinding
import com.mobile.massiveapp.domain.model.DoConsultaDocumento
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.ui.adapters.ConsultaRucAdapter
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogLoading
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.view.util.hideKeyboard
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.view.util.showMessage
import com.mobile.massiveapp.ui.viewmodel.ConsultaDocumentoViewModel
import com.mobile.massiveapp.ui.viewmodel.DatosMaestrosViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.droidsonroids.gif.GifDrawable

@AndroidEntryPoint
class ConsultaDocumentoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsultaDocumentoBinding
    private val socioViewModel: SocioViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val consultaDocumentoViewModel: ConsultaDocumentoViewModel by viewModels()
    private val datosMaestrosViewModel: DatosMaestrosViewModel by viewModels()
    private lateinit var usuario: DoUsuario
    private var successfulResponse: Boolean = false
    private var datosDocumento = DoConsultaDocumento()
    private lateinit var consultaDocumentoAdapter: ConsultaRucAdapter
    private val loadinDialog = BaseDialogLoading(this, "Sincronizando Clientes")
    private var menuItemCheck: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultaDocumentoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


            //Se Inicializa el Dilog del loading
        val gif = GifDrawable(this.resources, R.drawable.gif_searching6)
        val loadingDialog = BaseDialogLoadingCustom(this, "Buscando Documento", gif)


            //Inicializa el adapter
        consultaDocumentoAdapter = ConsultaRucAdapter(emptyList()){

        }
        binding.rvConsultaDoc.adapter = consultaDocumentoAdapter


            //USUARIO
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            try {
                usuario?.let {
                    this.usuario = it
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }


        //Manejo del editText del documento5
        try {
            setHint()
            if (binding.edtRuc.toString().length < 11) binding.edtRuc.error = "El RUC debe tener 11 dígitos"
            binding.edtRuc.addTextChangedListener { editable->
                val idTipoDocumentoChecked = binding.rgOpcionesConsulta.checkedRadioButtonId
                val nombreTipoDocChecked = this.findViewById<RadioButton>(idTipoDocumentoChecked).text.toString()
                when(nombreTipoDocChecked){
                    "DNI" -> if (editable.toString().length < 8) binding.edtRuc.error = "El DNI debe tener 8 dígitos"
                    "RUC" -> if (editable.toString().length < 11) binding.edtRuc.error = "El RUC debe tener 11 dígitos"
                }
            }
        } catch (e: Exception){
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }


        binding.rbRuc.setOnClickListener{ setHint() }

        binding.rbDni.setOnClickListener { setHint() }



        //Boton Buscar RUC
        binding.btnBuscarRuc.setOnClickListener {
            val tpoDoc = binding.rgOpcionesConsulta.checkedRadioButtonId
            val nombreRadioButton = this.findViewById<RadioButton>(tpoDoc).text.toString()
            val tipoDocumento = if (nombreRadioButton == "DNI") "1" else if (nombreRadioButton == "RUC") "6" else ""
            val numeroDocumento = binding.edtRuc.text.toString()

            setConsultaRUC(tipoDocumento,  numeroDocumento)
        }


        socioViewModel.isLoading.observe(this) {
            if (it == true){
                loadingDialog.startLoading()
            } else {
                loadingDialog.onDismiss()

            }
        }


            //Manejo de la respuesta de Consulta RUC
        socioViewModel.respuestaConsultaRuc.observe(this) { datosDocumento ->
            try {
                successfulResponse = datosDocumento.NumeroDocumento.isNotEmpty()

                this.datosDocumento = datosDocumento
                when (datosDocumento.TipoDocumento) {

                    "6" -> { //RUC
                        val activo = if (datosDocumento.Activo == "Y") "Activo" else if (datosDocumento.Activo == "N") "Inactivo" else ""

                        val datosRuc = listOf(
                            hashMapOf("Razón Social" to datosDocumento.RazonSocial),
                            hashMapOf("Dirección:" to datosDocumento.Calle),
                            hashMapOf("Estado:" to activo),
                            hashMapOf("Distrito:" to datosDocumento.DistritoNombre),
                            hashMapOf("Provincia:" to datosDocumento.ProvinciaNombre),
                            hashMapOf("Departamento:" to datosDocumento.DepartamentoNombre)
                        )

                        consultaDocumentoAdapter.updateData(datosRuc)
                    }

                    "1" -> { //DNI

                        val datosRuc = listOf(

                            hashMapOf("Nombre" to datosDocumento.Nombre),
                            hashMapOf("Apellido Paterno" to datosDocumento.ApellidoPaterno),
                            hashMapOf("Apellido Materno" to datosDocumento.ApellidoMaterno),
                            hashMapOf("Nombres" to datosDocumento.PrimerNombre),
                            hashMapOf("Numero de Documento" to datosDocumento.NumeroDocumento),
                            hashMapOf("Tipo de Documento" to "DNI")
                        )
                        consultaDocumentoAdapter.updateData(datosRuc)
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }


    fun setHint(){
        binding.edtRuc.text.clear()
        val idTipoDocumentoChecked = binding.rgOpcionesConsulta.checkedRadioButtonId
        val nombreTipoDocChecked = this.findViewById<RadioButton>(idTipoDocumentoChecked).text.toString()
        if (nombreTipoDocChecked == "RUC"){
            binding.tilNumeroDocumento.hint = "Ingrese RUC"
            binding.edtRuc.filters = arrayOf(InputFilter.LengthFilter(11))
        } else if (nombreTipoDocChecked == "DNI"){
            binding.tilNumeroDocumento.hint = "Ingrese DNI"
            binding.edtRuc.filters = arrayOf(InputFilter.LengthFilter(8))
        }
    }



    fun setConsultaRUC(tipoDocumento: String, numeroDocumento: String){
        try {
            when (tipoDocumento) {
                "6" -> {
                    if (binding.edtRuc.text.toString().trim().length < 11) {
                        throw Exception("El RUC debe tener 11 dígitos")
                    }
                }
                "1" -> {
                    if (binding.edtRuc.text.toString().trim().length < 8){
                        throw Exception("El DNI debe tener 8 dígitos")
                    }
                }
            }

            socioViewModel.socioConsultaRuc(
                numeroDocumento = numeroDocumento,
                tipoDocumento = tipoDocumento
            )
            hideKeyboard(binding.root)

        } catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }



    fun setDireccionFiscal():Boolean =
        try {
            consultaDocumentoViewModel.saveDireccionConsultaDocumento(intent.getStringExtra("accDocEntry").toString())
            true
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            false
            }





















    /*******************NAVBAR*****************/


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo_sn, menu)
        menu?.findItem(R.id.app_bar_connectivity_status)?.isVisible = false
        menuItemCheck = menu?.findItem(R.id.app_bar_check)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.app_bar_check -> {
                if (successfulResponse) {
                    menuItemCheck?.isEnabled = false
                    loadinDialog.startLoading()

                    datosMaestrosViewModel.getInfoSocios {progress, message, maxLenght->
                        loadinDialog.updateProgress(progress, message, maxLenght)
                    }

                    /***************/

                    //LiveData de la sincronizacion de Socios
                    datosMaestrosViewModel.dataGetInfoSocios.observeOnce(this){ response->
                        loadinDialog.onDismiss()

                        when(response.ErrorCodigo){
                            500->{
                                menuItemCheck?.isEnabled = true
                                BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                                    //Aceptar
                                    usuarioViewModel.logOutDrawer()
                                }
                            }
                            else -> {
                                //showMessage(this, response.ErrorMensaje)
                            }
                        }

                        consultaDocumentoViewModel.validarExistenciaDeDocumento(binding.edtRuc.text.toString())
                        consultaDocumentoViewModel.dataValidarExistenciaDeDocumento.observeOnce(this){
                            if (it){
                                menuItemCheck?.isEnabled = true
                                Toast.makeText(this, "El cliente ya existe", Toast.LENGTH_LONG).show()
                            } else{
                                if(setDireccionFiscal()){
                                    setResult(RESULT_OK,
                                        Intent()
                                            .putExtra("insertSuccess", successfulResponse)
                                            .putExtra("tipo", binding.switchDocumento.text.toString())
                                    )
                                    onBackPressedDispatcher.onBackPressed()
                                } else {
                                    menuItemCheck?.isEnabled = true
                                }
                            }
                        }
                    }
                    /****************/


                } else {
                    Toast.makeText(this, "No se encontraron datos, vuelva a consultar", Toast.LENGTH_LONG).show()
                }
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}


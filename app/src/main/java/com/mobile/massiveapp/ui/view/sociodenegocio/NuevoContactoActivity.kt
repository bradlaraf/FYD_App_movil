package com.mobile.massiveapp.ui.view.sociodenegocio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityNuevoContactoBinding
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.ui.base.BaseDialogEdt
import com.mobile.massiveapp.ui.view.util.agregarSocioContacto
import com.mobile.massiveapp.ui.viewmodel.SocioContactoViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NuevoContactoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevoContactoBinding
    private val socioContactoViewModel: SocioContactoViewModel by viewModels()
    private val socioViewModel: SocioViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var usuario = DoUsuario()
    private var licTradNum = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

            //Se obtiene el numero del contacto a editar
        val celularContacto = intent.getStringExtra("cecularContacto")
        val cardCode = intent.getStringExtra("cardCode")

        if (celularContacto != null && cardCode != null){
            socioContactoViewModel.getSocioContactoPorCelularYCardCode(
                celular = celularContacto,
                cardCode = cardCode
            )
        }

            //LiveData del contacto a editar
        socioContactoViewModel.dataGetSocioContactoPorCelularYCardCode.observe(this){ contacto->
            try {
                binding.txvNuevoContactoNombre.text = contacto.Name
                binding.txvNuevoContactoEmail.text = contacto.E_Mail
                binding.txvNuevoContactoCelularValue.text = contacto.Cellolar
                binding.txvNuevoContactoTelefono1.text = contacto.Tel1
                licTradNum = contacto.LicTradNum
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        setDefaultUi()
        setValoresIniciales()


    }

    private fun setValoresIniciales() {
        //Se obtiene el usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){
            try {
                usuario = it
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        socioViewModel.getConsultaRucFromDatabase()
        socioViewModel.consultaRucFromDatabase.observe(this){ rucConsulta->
            licTradNum = rucConsulta.NumeroDocumento
        }
    }

    private fun setDefaultUi() {
        //Nombre
        binding.clNuevoContactoNombre.setOnClickListener {
            BaseDialogEdt(binding.txvNuevoContactoNombre.text.toString(), onRegisterClickListener = { value ->
                if (value.isEmpty()){
                    return@BaseDialogEdt
                }else {
                    binding.txvNuevoContactoNombre.text = value
                }
            }).show(supportFragmentManager, "BaseDialogEdt")
        }



        //Email
        binding.clNuevoContactoEmail.setOnClickListener {
            BaseDialogEdt(binding.txvNuevoContactoEmail.text.toString(), onRegisterClickListener = { value ->
                if (value.isEmpty()){
                    return@BaseDialogEdt
                }else {
                    binding.txvNuevoContactoEmail.text = value
                }
            }).show(supportFragmentManager, "BaseDialogEdt")
        }



        //Celular
        binding.clNuevoContactoTelefono1.setOnClickListener {
            BaseDialogEdt(
                binding.txvNuevoContactoCelularValue.text.toString()
            ){ value ->
                if (value.isNotEmpty()){
                    binding.txvNuevoContactoCelularValue.text = value
                }
            }.show(supportFragmentManager, "BaseDialogEdt")
        }



        //Telefono 1
        binding.clNuevoContactoTelefono2.setOnClickListener {
            BaseDialogEdt(binding.txvNuevoContactoTelefono1.text.toString(), onRegisterClickListener = { value ->
                if (value.isEmpty()){
                    return@BaseDialogEdt
                }else {
                    binding.txvNuevoContactoTelefono1.text = value
                }
            }).show(supportFragmentManager, "BaseDialogEdt")
        }
    }


    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        setResult(RESULT_OK,
            Intent()
                .putExtra("contactoAgregado", true)
                .putExtra("cardCode", intent.getStringExtra("cardCode").toString())
        )

        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
            //Se oculta el icono de delete
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_check -> {
                if (binding.txvNuevoContactoNombre.text.isNotEmpty()
                    && binding.txvNuevoContactoCelularValue.text.isNotBlank()){

                        //Si se esta editanto un contacto
                    if (intent.getStringExtra("cecularContacto") != null){
                        socioContactoViewModel.deleteSocioContactoPorCelularYCardCode(
                            celular = intent.getStringExtra("cecularContacto").toString(),
                            cardCode = intent.getStringExtra("cardCode").toString().replace("\n"," ")
                        )
                    }

                    socioContactoViewModel.saveSocioContacto(
                        agregarSocioContacto(
                            cardCode =          intent.getStringExtra("cardCode").toString(),
                            nombre =            binding.txvNuevoContactoNombre.text.toString(),
                            email =             binding.txvNuevoContactoEmail.text.toString(),
                            celular =           binding.txvNuevoContactoCelularValue.text.toString(),
                            telefono1 =         binding.txvNuevoContactoTelefono1.text.toString(),
                            accDocEntry =       intent.getStringExtra("accDocEntry").toString(),
                            usuario =           usuario.Code,
                            licTradNum =        licTradNum
                        )
                    )
                } else {
                    Toast.makeText(this, "Debe ingresar un nombre y un número de celular como mínimo", Toast.LENGTH_SHORT).show()
                }

                socioContactoViewModel.dataSaveSocioContacto.observe(this){
                    if(it){
                        onBackPressed()
                    } else {
                        Toast.makeText(this, "Error al guardar el contacto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
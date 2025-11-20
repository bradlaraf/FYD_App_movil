package com.mobile.massiveapp.ui.view.sociodenegocio

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityEditarSocioNegocioBinding
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditarSocioNegocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarSocioNegocioBinding
    private val socioViewModel: SocioViewModel by viewModels()
    private val articuloViewModel: ArticuloViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var infomacionSocioHash = HashMap<String, Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarSocioNegocioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val cardCode = intent.getStringExtra("cardCodeCliente")


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
                infomacionSocioHash["groupCode"] = listaGrupos[0].GroupCode
            } catch (e: Exception){
                e.printStackTrace()
            }

            binding.clNuevoSnGrupo.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvNuevoSnGrupoValue.text.toString(),
                    listaGrupos.map { it.GroupName }
                ){ grupoSeleccionado, id ->
                    binding.txvNuevoSnGrupoValue.text = grupoSeleccionado
                    infomacionSocioHash["groupCode"] = listaGrupos[id].GroupCode
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

            binding.clNuevoSnCondicioPago.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvNuevoSnCondicionPagoValue.text.toString(),
                    listaCondiciones.map { it.PymntGroup }
                ){ condicionSeleccionada, id->
                    binding.txvNuevoSnCondicionPagoValue.text = condicionSeleccionada
                    infomacionSocioHash["condicionPago"] = listaCondiciones[id].GroupNum
                }.show(supportFragmentManager, "Condicion Dialog")
            }
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


        // (Btn) Elegir un DocumentoContacto Principal
        binding.clNuevoSnContactoPrincipal.setOnClickListener {
            if (binding.edtNuevoSnCodigo.text.toString().isNotEmpty()){
                Intent(this, ContactosActivity::class.java)
                    .putExtra("cardCode", "C${binding.edtNuevoSnDocumento.text}")
                    .putExtra("accDocEntry", binding.txvNuevoSnCodigoDocumentoValue.text.toString())
                    .also {
                        startForResultDocumentoContacto.launch(it)
                    }

            } else {
                Toast.makeText(this, "Debe consultar el documento primero", Toast.LENGTH_SHORT).show()
            }
        }


    }





    // (Btn) Consultar el RUC
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val succsessResponse = data?.getBooleanExtra("insertSuccess", false)
            val tipo = data?.getStringExtra("tipo")

            if (succsessResponse == true && !tipo.isNullOrEmpty()){
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


















    /***********************NAVBAR********************/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.app_bar_check -> {
                onBackPressedDispatcher.onBackPressed()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
package com.mobile.massiveapp.ui.view.sociodenegocio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityNuevaDireccionBinding
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.view.util.agregarDireccion
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioDireccionesViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NuevaDireccionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevaDireccionBinding
    private val direccionesViewModel: SocioDireccionesViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var usuario = DoUsuario()
    private val hashInfo = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaDireccionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            try {
                this.usuario = usuario
            } catch (e: Exception){
                e.printStackTrace()
            }
        }



            //Edicion de direccion
        if (intent.getIntExtra("lineNum",-1) != -1) {
            direccionesViewModel.getDireccionPorCardCodeTipoYLineNum(
                cardCode = intent.getStringExtra("cardCode").toString(),
                tipo = intent.getStringExtra("tipo").toString(),
                lineNum = intent.getIntExtra("lineNum", -1)
            )
        }

            //Livedata de la direccion a editar
        direccionesViewModel.dataGetDireccionPorCardCodeTipoYLineNum.observe(this){ direccion->
            try {
                binding.txvPaisNuevaDFValue.text = direccion.Country
                binding.txvDepartamentoDFiscalValue.text = direccion.State
                binding.txvProvinciaDFiscalValue.text = direccion.County
                binding.txvDistritoDFiscal.text = direccion.City
                binding.txvCallenDFiscalValue.text = direccion.Street
                binding.txvCodigoPostalDFiscalValue.text = direccion.U_MSV_FE_UBI
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

            //Se consulta es lineNum para una nueva direccion
        direccionesViewModel.getLineNumPorCardCodeYTipo(
            intent.getStringExtra("cardCode").toString(),
            intent.getStringExtra("tipo").toString()
        )


            //Traer todos los datos generales
        generalViewModel.getAllPaises()
        generalViewModel.getAllDepartamentos()
        generalViewModel.getEmpleadoDeVentas()




            // (btn) para elegir un PAIS
        generalViewModel.dataGetAllPaises.observe(this){ listaPaises->
            try {
                binding.txvPaisNuevaDFValue.text = listaPaises[0].Name
                hashInfo["codigoPais"] = listaPaises[0].Code
            } catch (e: Exception){
                e.printStackTrace()
            }

            binding.clPaisDFiscal.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvPaisNuevaDFValue.text.toString(),
                    listaPaises.map { it.Name }
                ){ paisSeleccionado, id->
                if (paisSeleccionado.isNotEmpty()) {
                    binding.txvPaisNuevaDFValue.text = paisSeleccionado
                    hashInfo["codigoPais"] = listaPaises[id].Code
                }
            }.show(supportFragmentManager, "BaseDialogChecklist") }
        }

            // (btn) para elegir un DEPARTAMENTO
        generalViewModel.dataGetAllDepartamentos.observe(this){ listaDepartamentos->
            binding.clDepartamentoDFiscal.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvDepartamentoDFiscalValue.text.toString(),
                    listaDepartamentos.map { it.Name }
                ){ departamentoSeleccionado, id ->
                if (departamentoSeleccionado.isNotEmpty()) {
                    binding.txvDepartamentoDFiscalValue.text = departamentoSeleccionado
                    generalViewModel.getAllProvincias(
                        listaDepartamentos[id].Code
                    )

                    hashInfo["codigoState"] = listaDepartamentos[id].Code
                }
            }.show(supportFragmentManager, "BaseDialogChecklist") }
        }

            // (btn) para elegir una PROVINCIA
        generalViewModel.dataGetAllProvincias.observe(this){ listaProvincias->
            binding.clProvinciaDFiscal.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvProvinciaDFiscalValue.text.toString(),
                    listaProvincias.map { it.Name }
                ){ provinciaSeleccionada, id->
                if (provinciaSeleccionada.isNotEmpty()) {
                    binding.txvProvinciaDFiscalValue.text = provinciaSeleccionada
                    generalViewModel.getAllDistritos(
                        listaProvincias[id].Code
                    )
                }
            }.show(supportFragmentManager, "BaseDialogChecklist") }
        }

            // (btn) para elegir un DISTRITO
        generalViewModel.dataGetAllDistritos.observe(this){ listaDistritos->
            binding.clDistritoDFiscal.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvDistritoDFiscal.text.toString(),
                    listaDistritos.map { it.Name }
                ){ distritoSeleccionado, id->
                if (distritoSeleccionado.isNotEmpty()) {
                    binding.txvDistritoDFiscal.text = distritoSeleccionado
                    hashInfo["codigoDistrito"] = listaDistritos[id].Code
                }
            }.show(supportFragmentManager, "BaseDialogChecklist") }
        }



        // (btn) para elegir una CALLE
        binding.clCallenDFiscal.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvCallenDFiscalValue.text.toString()
            ){ value->
                if (value.isNotEmpty()) {
                    binding.txvCallenDFiscalValue.text = value
                }
        }.show(supportFragmentManager, "BaseDialogChecklist") }


        // (btn) para elegir un CODIGO POSTAL
        binding.clCodigoPostalDFiscal.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "phone",
                binding.txvCodigoPostalDFiscalValue.text.toString(),
                6
            ){ value->
            if (value.isNotEmpty()) {
                binding.txvCodigoPostalDFiscalValue.text = value
            }
        }.show(supportFragmentManager, "BaseDialogChecklist") }
    }








    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onBackPressed() {
        setResult(RESULT_OK,
            Intent( )
                .putExtra("cardCode", intent.getStringExtra("cardCode").toString())
                .putExtra("tipo", intent.getStringExtra("tipo").toString())
        )
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo_sn, menu)
        menu?.findItem(R.id.app_bar_connectivity_status)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_check -> {
                if (binding.txvPaisNuevaDFValue.text.isNotEmpty() &&
                    binding.txvDepartamentoDFiscalValue.text.isNotEmpty() &&
                    binding.txvProvinciaDFiscalValue.text.isNotEmpty() &&
                    binding.txvDistritoDFiscal.text.isNotEmpty() &&
                    binding.txvCallenDFiscalValue.text.isNotEmpty() ) {

                    direccionesViewModel.dataGetLineNumPorCardCodeYTipo.observe(this){ lineNum->
                        val lineNumToUse = if(intent.getIntExtra("lineNum", -1) != -1) intent.getIntExtra("lineNum", -1) else lineNum

                        direccionesViewModel.saveDireccion(
                            agregarDireccion(
                                cardCode =              intent.getStringExtra("cardCode").toString(),
                                tipo =                  intent.getStringExtra("tipo").toString(),
                                codigoPais =            hashInfo["codigoPais"].toString(),
                                codigoDepartamento =    hashInfo["codigoState"].toString(),
                                provincia =             binding.txvProvinciaDFiscalValue.text.toString(),
                                distrito =              binding.txvDistritoDFiscal.text.toString(),
                                calle =                 binding.txvCallenDFiscalValue.text.toString(),
                                ubigeo =                hashInfo["codigoDistrito"].toString(),
                                lineNum =               lineNumToUse,
                                accDocEntry =           intent.getStringExtra("accDocEntry").toString(),
                                usuario =               usuario.Code
                            )
                        )
                    }


                        //Resultado de la insercion
                    direccionesViewModel.dataSaveDireccion.observe(this){ success->
                        if(success){
                            onBackPressed()
                        } else{
                            Toast.makeText(this, "Error al guardar la direccion de despacho", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Faltan datos por completar", Toast.LENGTH_SHORT).show()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }


}
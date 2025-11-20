package com.mobile.massiveapp.ui.view.cobranzas

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.mobile.massiveapp.MassiveApp.Companion.prefs
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityInfoCobranzaBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMInfoCobranzaAdapter
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.util.getFechaAyer
import com.mobile.massiveapp.ui.viewmodel.CobranzaViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoCobranzaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoCobranzaBinding
    private val cobranzaViewModel: CobranzaViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var tabTitle = arrayOf("Cabecera", "Contenido")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoCobranzaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


            //Seteo del boton de navegacion entre fragments
        cobranzaViewModel.getAllPagoDetalleXAccDocEntry(intent.getStringExtra("accDocEntry").toString())
        cobranzaViewModel.getPagoCabeceraPorAccDocEntry(intent.getStringExtra("accDocEntry").toString())

        val pager = binding.viewPagerInfoCobranzas
        val tl = binding.tabLayoutInfoCobranzas
        binding.viewPagerInfoCobranzas.adapter = FMInfoCobranzaAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()


            //LiveData de la eliminacion de todos los PagoDetalle para editar
        /*cobranzaViewModel.dataDeleteAllPagoDetalleParaEditar.observe(this){ deleteSuccess->
            if (deleteSuccess){
                Intent(this, EditarCobranzaActivity::class.java)
                    .putExtra("accDocEntry", intent.getStringExtra("accDocEntry"))
                    .also { startForEdicionCobranzaResult.launch(it) }
            }
        }*/


            //LiveData de la cancelacion de la cobranza
        cobranzaViewModel.dataCancelCobranza.observe(this){ response->
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

        //LiveData Cierre de sesion
        cierreSesion()
    }


    private fun cierreSesion() {
        //LiveData Cierre de Sesion
        usuarioViewModel.isLoadingLogOut.observe(this){
            val loadingSimpleDialog = BaseDialogSImpleLoading(this)
            loadingSimpleDialog.startLoading("Cerrando Sesion...")

            if (!it){
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



    val startForEdicionCobranzaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            /*cobranzaViewModel.getAllPagosDetallePorAccDocEntry(intent.getStringExtra("accDocEntry").toString())
            cobranzaViewModel.getPagoCabeceraPorAccDocEntry(intent.getStringExtra("accDocEntry").toString())*/
            setResult(RESULT_OK)
            onBackPressedDispatcher.onBackPressed()
        }
        if (result.resultCode == Activity.RESULT_CANCELED){
            setResult(RESULT_CANCELED)
            onBackPressedDispatcher.onBackPressed()
        }
    }








    /*--------------------NAVBAR-----------------------*/


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_info_cobranzas, menu)
        val fechaDoc = intent.getStringExtra("fechaDoc").toString()
            //Get Usuario
        menu?.findItem(R.id.nav_item_editar_cobranza)?.isVisible = false
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){
            /*if (it.CanUpdate == "N"){
                menu?.findItem(R.id.nav_item_editar_cobranza)?.isVisible = false
            }*/
            if (it.CanCreate == "N" || fechaDoc == getFechaAyer()){
                menu?.findItem(R.id.nav_item_cancelar_cobranza)?.isVisible = false
             /*   menu?.findItem(R.id.nav_item_editar_cobranza)?.isVisible = false*/
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_item_editar_cobranza -> {
                prefs.wipe()
                /*cobranzaViewModel.deleteAllPagoDetalleParaEditar(
                    intent.getStringExtra("accDocEntry").toString()
                )*/
            }

            R.id.nav_item_cancelar_cobranza -> {
                cobranzaViewModel.cancelCobranza(intent.getStringExtra("accDocEntry").toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
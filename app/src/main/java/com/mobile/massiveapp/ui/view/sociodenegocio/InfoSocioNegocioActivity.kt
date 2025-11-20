package com.mobile.massiveapp.ui.view.sociodenegocio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityInfoSocioNegocioBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMClientesInfoAdapter
import com.mobile.massiveapp.ui.base.BaseDialogConfirmationBasicHelper
import com.mobile.massiveapp.ui.viewmodel.SocioContactoViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioDireccionesViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class InfoSocioNegocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoSocioNegocioBinding
    private val socioNegocioViewModel: SocioViewModel by viewModels()
    private val socioDireccionesViewModel: SocioDireccionesViewModel by viewModels()
    private val socioContactosViewModel: SocioContactoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var tabTitle = arrayOf("General", "Condición", "Contacto", "Dirección")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoSocioNegocioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imvMenu.setOnClickListener {
            showPopupMenu(it)
        }
        binding.imvBackArrow.setOnClickListener { onBackPressedDispatcher.onBackPressed() }


            //Set TABLAYOUT
        val pager = binding.viewPagerClientesInfo
        val tl = binding.tabLayoutCLientesInfo
        pager.adapter = FMClientesInfoAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()


            //Get Datos del CLIENTE POR CARDCODE
        socioDireccionesViewModel.getAllDireccionesPorCardCode(intent.getStringExtra("cardCode").toString())
        socioContactosViewModel.getSocioContactosPorCardCode(intent.getStringExtra("cardCode").toString())

        socioNegocioViewModel.getInfoClientePorCardCode(intent.getStringExtra("cardCode").toString())
        socioNegocioViewModel.dataGetInfoClientePorCardCode.observe(this){ infoCliente->
            try {
                binding.txvInfoSNCardName.text = infoCliente.CardName
                binding.txvInfoSNCardCode.text = infoCliente.CardCode
                binding.txvInfoSNType.text = infoCliente.CardType
            } catch (e: Exception){
                e.printStackTrace()
            }
        }


            //LiveData de la eliminacion del socio de negocio
        socioNegocioViewModel.dataDeleteSocioNegocio.observe(this){ deleteSuccess ->
            if (deleteSuccess){
                Toast.makeText(this, "Socio de negocio eliminado", Toast.LENGTH_SHORT).show()
                Intent(this, SocioNegocioActivity::class.java).also { startActivity(it) }
            }
        }


    }




    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_info_sn_edit_cancel, popupMenu.menu)

        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){
            if (it.CanUpdate == "N"){
                popupMenu.menu?.findItem(R.id.nav_item_editar_socio)?.isVisible = false
            }
            if (it.CanCreate == "N"){
                popupMenu.menu?.findItem(R.id.nav_item_eliminar_socio)?.isVisible = false
                popupMenu.menu?.findItem(R.id.nav_item_editar_socio)?.isVisible = false
            }
        }
        popupMenu.menu?.findItem(R.id.nav_item_editar_socio)?.isVisible = false



        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.nav_item_editar_socio -> {
                    Intent(this, EditarSocioNegocioActivity::class.java)
                        .putExtra("cardCode", intent.getStringExtra("cardCode"))
                        .also { startActivity(it) }
                    true
                }

                R.id.nav_item_eliminar_socio ->{
                    val dialogConfirmacion = BaseDialogConfirmationBasicHelper(this)
                    dialogConfirmacion.showConfirmationDialog(
                        "¿Seguro que quieres eliminar este Socio de Negocio?"
                    ){
                        socioNegocioViewModel.deleteSocioNegocio(intent.getStringExtra("cardCode").toString())
                    }
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }







    /*---------Barra de Titulo---------- */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_info_sn_edit_cancel, menu)

            //Get Usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){
            if (it.CanUpdate == "N"){
                menu?.findItem(R.id.nav_item_editar_socio)?.isVisible = false
            }
            if (it.CanCreate == "N"){
                menu?.findItem(R.id.nav_item_eliminar_socio)?.isVisible = false
                menu?.findItem(R.id.nav_item_editar_socio)?.isVisible = false
            }
        }
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return true
    }


}
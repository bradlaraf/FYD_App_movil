package com.mobile.massiveapp.ui.view.usuarios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityInfoUsuariosBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMUsuariosInfoAdapter
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.ArticulosConStockFragment
import com.mobile.massiveapp.ui.view.usuarios.fragments.UsuarioGeneralFragment
import com.mobile.massiveapp.ui.view.util.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoUsuariosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoUsuariosBinding
    private val usuariosViewModel: UsuariosViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by viewModels()
    //private val tabTitle = arrayOf("General", "Almacenes", "L. Precios" ,"G. Articulo", "G. Socios", "Zonas")
    private val tabTitle = arrayOf("General")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, UsuarioGeneralFragment())
                .commit()
        }

        setDefaultUi()
        setValoresIniciales()
    }

    private fun setValoresIniciales() {
        val usuarioCode = intent.getStringExtra("usuarioCode").toString()

        usuariosViewModel.getAllUsuarioZonas(usuarioCode)
        usuariosViewModel.getAllUsuarioAlmacenes(usuarioCode)
        usuariosViewModel.getAllUsuarioGrupoArticulos(usuarioCode)
        usuariosViewModel.getAllUsuarioGrupoSocios(usuarioCode)
        usuariosViewModel.getAllUsuarioListaPrecios(usuarioCode)
    }

    private fun setDefaultUi() {
        //Pager - TabLayout
        /*val pager = binding.viewPagerUsuariosInfo
        val tl = binding.tabLayoutUsuariosInfo
        pager.adapter = FMUsuariosInfoAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()*/

        //Click boton editar
        binding.btnEditarUsuario.setOnClickListener {
            Intent(this, NuevoUsuarioActivity::class.java)
                .putExtra("usuarioCode", intent.getStringExtra("usuarioCode")?:"")
                .putExtra("edicionUsuario", true)
                .also { startActivity(it) }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_expand)?.isVisible = false
        menu?.findItem(R.id.app_bar_add)?.isVisible = false

        val searchViewHelper = SearchViewHelper(menu, "Buscar",{ newText->
            usuariosViewModel.dataAllUsuariosLiveData.observe(this){ listaUsuarios->
                providerViewModel.saveData(newText)
            }

        },{textSubmit-> })
        searchViewHelper.setOnDismiss{}
        return super.onCreateOptionsMenu(menu)
    }
}
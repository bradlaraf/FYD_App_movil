package com.mobile.massiveapp.ui.view.usuarios

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityUsuariosBinding
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.ui.adapters.UsuariosAdapter
import com.mobile.massiveapp.ui.base.BaseDialogConfirmationBasicHelper
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsuariosActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityUsuariosBinding
    private lateinit var usuariosAdapter: UsuariosAdapter
    private lateinit var loadingDialog: BaseDialogSImpleLoading

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private var buttonClicked = false

    private val usuariosViewModel: UsuariosViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var messageLoadingDialog = "Cargando..."
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = BaseDialogSImpleLoading(this)
        val loadingDialog2 = BaseDialogSImpleLoading(this)

        loadingDialog.startLoading()
        setDefaultUi()



        lifecycleScope.launch {
            usuariosViewModel.dataAllUsuariosFlow.collect{ listaUsuarios->
                usuariosAdapter.updateData(listaUsuarios)
                delay(500)
                loadingDialog.onDismiss()
            }
        }

        usuariosViewModel.isLoading.observe(this){
            if (it){
                loadingDialog.startLoading(messageLoadingDialog)
            } else {
                loadingDialog.onDismiss()
            }
        }

        usuariosViewModel.isLoadingCierreMasivo.observe(this){
            if (it){
                loadingDialog2.startLoading(messageLoadingDialog)
            } else {
                loadingDialog2.onDismiss()
            }
        }

        //Cierre Sesion un Usuario
        usuariosViewModel.dataCerrarSesionUsuario.observe(this){ response->
            when(response.ErrorCodigo){
                500 ->{ usuarioViewModel.logOut() }
                0 -> { showMessage(response.ErrorMensaje) }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        //Reseteo ID Movil
        usuariosViewModel.dataResetearIDUnUsuario.observe(this) { response->
            when(response.ErrorCodigo){
                500 ->{ usuarioViewModel.logOut() }
                0 -> { showMessage(response.ErrorMensaje) }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        //Cierre Sesion Masivo
        usuariosViewModel.dataCerrarTodasLasSesiones.observe(this){ response->
            when(response.ErrorCodigo){
                500 ->{ usuarioViewModel.logOut() }
                0 -> { showMessage(response.ErrorMensaje) }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        //LiveData Cierre Sesion
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



    private fun showPopupMenu(view: View, usuario: DoConfigurarUsuario) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_usuario_options, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.nav_item_duplicar_usuario -> {
                    Intent(this, NuevoUsuarioActivity::class.java)
                        .putExtra("usuarioCode", usuario.Code)
                        .putExtra("usuarioDuplicado", true)
                        .also { startActivity(it) }
                    true
                }

                R.id.nav_item_cerrar_sesion_ususario -> {
                    messageLoadingDialog = "Cerrando Sesión..."
                    usuariosViewModel.cerrarSesionUsuario(usuario.Code)
                    true
                }

                R.id.nav_item_resetear_id_ususario -> {
                    messageLoadingDialog = "Reseteando ID..."
                    usuariosViewModel.resetearIDUnUsuario(usuario.Code)
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    private fun setDefaultUi() {
        usuariosAdapter = UsuariosAdapter(emptyList(), { usuario->
            Intent(this, InfoUsuariosActivity::class.java)
                .putExtra("usuarioCode", usuario.Code)
                .putExtra("showInfo", true)
                .also { startActivity(it) }
        }, { view, usuario->
            showPopupMenu(view, usuario)
        })
        binding.rvUsuarios.adapter = usuariosAdapter

        //Btn Opciones
        binding.btnNuevoUsuario.setOnClickListener {
            setVisivility(buttonClicked)
            setAnimation(buttonClicked)
            setClickable(buttonClicked)
            buttonClicked = !buttonClicked
        }

        //Btn Nuevo Usuario
        binding.btnNuevoUser.setOnClickListener {
            /*BaseSimpleInformativeDialog("Estado de Sesión", "Su sesión ha sido cerrada").show(supportFragmentManager, "showDialog")*/
            Intent(this, NuevoUsuarioActivity::class.java).also { startActivity(it) }
        }

        //Btn Cerrar Todas las sesiones
        binding.btnCierreSesionMasivo.setOnClickListener {
            BaseDialogConfirmationBasicHelper(this)
                .showConfirmationDialog("¿Seguro que desea cerrar la sesión de todos los usuarios?"){
                    //Aceptar
                    messageLoadingDialog = "Cerrando todas las sesiones..."
                    usuariosViewModel.cerrarTodasLasSesiones()
                }
        }
    }

    private fun setClickable(buttonClicked: Boolean) {
        binding.btnNuevoUser.isClickable = !buttonClicked
        binding.btnCierreSesionMasivo.isClickable = !buttonClicked
        binding.txvNuevoUsuarioTitle.isClickable = !buttonClicked
        binding.txvCerrarSesionesTitle.isClickable = !buttonClicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked){
            binding.btnNuevoUser.visibility = View.VISIBLE
            binding.btnCierreSesionMasivo.visibility = View.VISIBLE
            binding.txvCerrarSesionesTitle.visibility = View.VISIBLE
            binding.txvNuevoUsuarioTitle.visibility = View.VISIBLE
        } else {
            binding.btnNuevoUser.visibility = View.INVISIBLE
            binding.btnCierreSesionMasivo.visibility = View.INVISIBLE
            binding.txvCerrarSesionesTitle.visibility = View.INVISIBLE
            binding.txvNuevoUsuarioTitle.visibility = View.INVISIBLE
        }

    }

    private fun setVisivility(clicked: Boolean) {
        if (!clicked){
            binding.btnNuevoUser.startAnimation(fromBottom)
            binding.btnCierreSesionMasivo.startAnimation(fromBottom)
            binding.txvNuevoUsuarioTitle.startAnimation(fromBottom)
            binding.txvCerrarSesionesTitle.startAnimation(fromBottom)
            binding.btnNuevoUsuario.startAnimation(rotateOpen)
        } else {
            binding.btnNuevoUser.startAnimation(toBottom)
            binding.btnCierreSesionMasivo.startAnimation(toBottom)
            binding.txvNuevoUsuarioTitle.startAnimation(toBottom)
            binding.txvCerrarSesionesTitle.startAnimation(toBottom)
            binding.btnNuevoUsuario.startAnimation(rotateClose)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_socio_lupa_add, menu)
        menu?.findItem(R.id.app_bar_expand)?.isVisible = false
        menu?.findItem(R.id.app_bar_add)?.isVisible = false

        val searchViewHelper = SearchViewHelper(menu, "Buscar Usuario",{ newText->
            usuariosViewModel.dataAllUsuariosLiveData.observe(this){ listaUsuarios->
                val usuariosFiltrados = listaUsuarios.filter { usuario->
                    usuario.Code.contains(newText, ignoreCase = true) ||
                    usuario.Name.contains(newText, ignoreCase = true)
                }

                usuariosAdapter.updateData(usuariosFiltrados)
            }


        },{textSubmit-> })
        searchViewHelper.setOnDismiss{}
        return super.onCreateOptionsMenu(menu)
    }

}
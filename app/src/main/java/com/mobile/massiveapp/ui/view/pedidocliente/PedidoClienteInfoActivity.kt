package com.mobile.massiveapp.ui.view.pedidocliente

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
import com.mobile.massiveapp.data.model.ClientePedidos
import com.mobile.massiveapp.databinding.ActivityPedidoClienteInfoBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMPedidosClienteInfoAdapter
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.util.getFechaAyer
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.SocioContactoViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.mobile.massiveapp.ui.view.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class PedidoClienteInfoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPedidoClienteInfoBinding
    lateinit var navigation: BottomNavigationView
    private val providerViewModel: ProviderViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()
    private val socioContactoViewModel: SocioContactoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private var pedidoActual = ClientePedidos()
    private val tabTitle = arrayOf("Cabecera", "Contenido", "Logistica", "Finanzas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoClienteInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val loadingSimpleDialog = BaseDialogSImpleLoading(this)

        setDefaultUI()

        pedidoViewModel.dataGetPedidoPorAccDocEntry.observe(this){
            pedidoActual = it
        }


        //Obtener el accDocEntry del pedido seleccionado y se pasa a los fragment
        val accDocEntry = intent.getStringExtra("AccDocEntry")
        providerViewModel.saveAccDocEntry(accDocEntry.toString())



            //Livedata Pedido Cancelado
        pedidoViewModel.dataCancelPedido.observe(this){ response->
            when(response.ErrorCodigo){
                500 ->{
                    BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                        //Aceptar
                        usuarioViewModel.logOut()
                    }
                }

                0 -> {
                    showMessage(response.ErrorMensaje)
                    Intent(this, PedidoClienteActivity::class.java).apply { startActivity(this) }
                }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        //Live data de edicion de pedido
        pedidoViewModel.dataDeleteAllPedidoDetalleSinCabecera.observe(this){
            pedidoViewModel.deleteAllPedidoDetallesParaEditar(intent.getStringExtra("accDocEntry").toString())
        }

        pedidoViewModel.dataDeleteAllPedidoDetallesParaEditar.observe(this){
            Intent(this, EditarPedidoCabeceraActivity::class.java)
                .putExtra("cardCode", intent.getStringExtra("cardCode"))
                .putExtra("accDocEntry", intent.getStringExtra("accDocEntry"))
                .putExtra("editMode", true)
                .also { startForEdicionPedidoResult.launch(it) }

        }

        //LiveData Comprobar estado de pedido
        pedidoViewModel.dataComprobarEstadoActualPedido.observe(this){ response->
            when(response.ErrorCodigo){
                500 ->{
                    BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                        //Aceptar
                        usuarioViewModel.logOut()
                    }
                }

                -2 ->{
                    BaseDialogAlert(this).showConfirmationDialog("No puede editar este pedido"){
                        showMessage(response.ErrorMensaje)

                    }
                }

                -1 -> {
                    showMessage(response.ErrorMensaje)
                }

                0 -> {
                    showMessage(response.ErrorMensaje)
                    pedidoViewModel.deleteAllPedidoDetalleSinCabecera()
                }

                else -> { showMessage(response.ErrorMensaje) }
            }

        }
        pedidoViewModel.isLoadingComprobarPedido.observe(this){
            if (it){
                loadingSimpleDialog.startLoading("Comprobando estado del pedido...")
            } else {
                loadingSimpleDialog.onDismiss()
            }
        }

        //LiveData Cierre de sesion
        cierreSesion()
    }

    private fun setDefaultUI() {
        //Set TABLAYOUT
        socioContactoViewModel.getSocioContactosPorCardCode(intent.getStringExtra("cardCode").toString())
        pedidoViewModel.getPedidoCabeceraInfo(intent.getStringExtra("accDocEntry").toString())
        pedidoViewModel.getPedidoPorAccDocEntry(intent.getStringExtra("accDocEntry").toString())
        pedidoViewModel.getAllPedidoDetallePorAccDocEntry(intent.getStringExtra("accDocEntry").toString())
        usuarioViewModel.getUsuarioFromDatabase()

        val pager = binding.viewPagerPedidosInfo
        val tl = binding.tabLayoutPedidosInfo
        pager.adapter = FMPedidosClienteInfoAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()
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


    val startForEdicionPedidoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            /*cobranzaViewModel.getAllPagosDetallePorAccDocEntry(intent.getStringExtra("accDocEntry").toString())
            cobranzaViewModel.getPagoCabeceraPorAccDocEntry(intent.getStringExtra("accDocEntry").toString())*/
            setResult(RESULT_OK)
            //onBackPressedDispatcher.onBackPressed()
        }
        if (result.resultCode == Activity.RESULT_CANCELED){
            setResult(RESULT_CANCELED)
            //onBackPressedDispatcher.onBackPressed()
        }
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_info_pedidos, menu)

            //Get Usuario
        usuarioViewModel.getUsuarioFromDatabase()
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){
            val fechaDoc = intent.getStringExtra("fechaDoc").toString()
            try {
                if (it.CanUpdate == "N"){
                    menu?.findItem(R.id.nav_item_editar_pedido)?.isVisible = false
                }

                if (it.CanCreate == "N" || fechaDoc == getFechaAyer()){
                    menu?.findItem(R.id.nav_item_cancelar_pedido)?.isVisible = false
                    menu?.findItem(R.id.nav_item_editar_pedido)?.isVisible = false
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_item_editar_pedido -> {
                if (pedidoActual.DocEntry != -1 && (pedidoActual.NumAtCard.trim().isNotEmpty() || pedidoActual.ObjType == 17)){
                    Toast.makeText(this, "No se puede editar el pedido", Toast.LENGTH_SHORT).show()
                } else {
                    pedidoViewModel.comprobarEstadoActualPedido(intent.getStringExtra("AccDocEntry").toString(), this)
                }
            }

            R.id.nav_item_cancelar_pedido -> {
                if (pedidoActual.DocEntry != -1 && (pedidoActual.NumAtCard.trim().isNotEmpty() || pedidoActual.ObjType == 17)){
                    Toast.makeText(this, "No se puede cancelar el pedido", Toast.LENGTH_SHORT).show()
                } else {
                    pedidoViewModel.cancelPedido(intent.getStringExtra("accDocEntry").toString())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
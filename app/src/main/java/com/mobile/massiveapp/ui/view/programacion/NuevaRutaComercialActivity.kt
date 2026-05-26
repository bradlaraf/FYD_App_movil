package com.mobile.massiveapp.ui.view.programacion

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.mobile.massiveapp.MassiveApp.Companion.prefsRutaComercial
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityNuevaRutaComercialBinding
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.ui.adapters.RutaComercialDetalleAdapter
import com.mobile.massiveapp.ui.adapters.extension.SwipeToDeletePedidos
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogComentarioRuta
import com.mobile.massiveapp.ui.base.BaseDialogDireccionCliente
import com.mobile.massiveapp.ui.base.BaseDialogEdtCharacterLimit
import com.mobile.massiveapp.ui.base.BaseDialogLoadingCustom
import com.mobile.massiveapp.ui.view.pedidocliente.BuscarClienteActivity
import com.mobile.massiveapp.ui.view.util.agregarRutaComercialCabecera
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getCodigoDeDocumentoActual
import com.mobile.massiveapp.ui.view.util.mostrarCalendarioMaterial
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.view.util.observeOnceNotNull
import com.mobile.massiveapp.ui.view.util.showMessage
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.RutaComercialViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.trendyol.bubblescrollbarlib.BubbleTextProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pl.droidsonroids.gif.GifDrawable

@AndroidEntryPoint
class NuevaRutaComercialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevaRutaComercialBinding
    private val rutaComercialViewModel: RutaComercialViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private lateinit var detalleAdapter: RutaComercialDetalleAdapter
    private val addedCardCodes = mutableSetOf<String>()
    private var listaRutaDetalles:List<DoRutaComercialDetalleView> = emptyList()
    private var slpCode = -1

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            detalleAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            rutaComercialViewModel.updateDocLines(detalleAdapter.currentList)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaRutaComercialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Nueva Ruta Comercial"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDefaultUi()
        observeDetalle()
        setData()
    }

    private fun setData() {
        generalViewModel.getAllGeneralVendedores()
    }


    private fun setDefaultUi() {
        (binding.rvNuevaRutaClientes.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        binding.txvNuevaRutaFechaRutaValue.text = getFechaActual()

        binding.clNuevaRutaVendedor.setOnClickListener {
            generalViewModel.dataGetAllGeneralVendedores.observeOnce(this) { vendedores ->
                BaseDialogChecklistWithId(
                    checkSelected = binding.txvNuevaRutaVendedorValue.text.toString(),
                    opciones = vendedores.map { it.SlpName }
                ) { nombreSeleccionado, id ->
                    if (nombreSeleccionado.isNotEmpty()) {
                        binding.txvNuevaRutaVendedorValue.text = nombreSeleccionado
                        slpCode = vendedores[id].SlpCode
                    }
                }.show(supportFragmentManager, "VendedorDialog")
            }
        }

        binding.clNuevaRutaComentarios.setOnClickListener {
            BaseDialogComentarioRuta(
                binding.txvNuevaRutaComentariosValue.text.toString()
            ) { comentario ->
                binding.txvNuevaRutaComentariosValue.text = comentario
            }.show(supportFragmentManager, "ComentarioRutaDialog")
        }



        binding.clNuevaRutaFechaRuta.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mostrarCalendarioMaterial(
                    this,
                    binding.txvNuevaRutaFechaRutaValue.text.toString().ifEmpty { getFechaActual() }
                ) { dia, mes, anio ->
                    binding.txvNuevaRutaFechaRutaValue.text = "$anio-$mes-$dia"
                }
            }
        }

        binding.clNuevaRutaClientes.setOnClickListener {
            startForClienteResult.launch(Intent(this, BuscarClienteActivity::class.java))
        }

        //Loading save RutaComercial
        val gif = GifDrawable(this.resources, R.drawable.gif_loading)
        val loadingDialog = BaseDialogLoadingCustom(this, "Enviando Ruta Comercial...", gif)
        rutaComercialViewModel.isLoadingSaveRutaCabecera.observe(this){
            if (it){
                loadingDialog.startLoading()
            } else {
                loadingDialog.onDismiss()
            }
        }


        //Adapter
        detalleAdapter = RutaComercialDetalleAdapter(
            dataSet = emptyList(),
            onStartDrag = { viewHolder ->
                itemTouchHelper.startDrag(viewHolder)
            },
            onItemClick = { detalle ->
                getDireccionesCliente(detalle)
            }
        )
        binding.rvNuevaRutaClientes.adapter = detalleAdapter
        itemTouchHelper.attachToRecyclerView(binding.rvNuevaRutaClientes)
        binding.bubbleScrollBar.attachToRecyclerView(binding.rvNuevaRutaClientes)



        /**SWIPE TO DELETE**/
        val swipeToDeleteCallback = object : SwipeToDeletePedidos(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val docLine = listaRutaDetalles[position].LineNum
                val accDocEntr = listaRutaDetalles[position].AccDocEntry

                rutaComercialViewModel.deleteRutaComercialDetalle(docLine, accDocEntr)
                binding.rvNuevaRutaClientes.adapter?.notifyItemChanged(position)
                //showMessage(this@CobranzaManifiestoActivity, position.toString())
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvNuevaRutaClientes)
        /**SWIPE TO DELETE**/
    }

    private fun getDireccionesCliente(detalle: DoRutaComercialDetalleView) {
        rutaComercialViewModel.getAllDireccionesCliente(detalle.CardCode)
        rutaComercialViewModel.dataGetAllDireccionesCliente.observeOnceNotNull(this) { direccionesCliente ->
            BaseDialogDireccionCliente(
                checkSelected = detalle.Address,
                direcciones = direccionesCliente
            ) { calleSeleccionada,id ->
                rutaComercialViewModel.updateAddress(detalle.AccDocEntry,  calleSeleccionada, detalle.LineNum)
            }.show(supportFragmentManager, "DireccionClienteDialog")
        }
    }

    private fun observeDetalle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rutaComercialViewModel.detalleFlow.collectLatest { listaDetallesRutaComercial ->
                    listaRutaDetalles = listaDetallesRutaComercial
                    detalleAdapter.updateData(listaDetallesRutaComercial)
                    if (listaDetallesRutaComercial.isNotEmpty()) {
                        binding.rvNuevaRutaClientes.post {
                            binding.bubbleScrollBar.visibility = View.VISIBLE
                            binding.bubbleScrollBar.attachToRecyclerView(binding.rvNuevaRutaClientes)
                        }
                    }
                    actualizarContadorClientes(listaDetallesRutaComercial.size)
                }
            }
        }
    }

    private fun actualizarContadorClientes(cantidad: Int) {
        binding.txvNuevaRutaClientesValue.text =
            "$cantidad ${if (cantidad == 1) "cliente" else "clientes"}"
    }






    private val startForClienteResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val cardCode = result.data?.getStringExtra("cardCode")
                if (!cardCode.isNullOrEmpty()) {

                    addedCardCodes.add(cardCode)
                    rutaComercialViewModel.saveDetalle(prefsRutaComercial.getAccDocEntry(), cardCode)

                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_check -> {
                try {
                    val nombreVendedor = binding.txvNuevaRutaVendedorValue.text.toString()
                    if (nombreVendedor == "Seleccionar vendedor") {
                        throw ( Exception("Debe seleccionar un vendedor"))
                    }

                    val fechaRuta = binding.txvNuevaRutaFechaRutaValue.text.toString()
                    if (fechaRuta.isEmpty()) {
                        throw ( Exception("Debe seleccionar una fecha de ruta"))
                    }
                    if (addedCardCodes.isEmpty()) {
                        throw ( Exception("Debe agregar al menos un cliente"))
                    }

                    rutaComercialViewModel.saveRutaCabecera(
                        agregarRutaComercialCabecera(
                            accDocEntry = prefsRutaComercial.getAccDocEntry(),
                            fechaRuta = binding.txvNuevaRutaFechaRutaValue.text.toString(),
                            slpCode = slpCode,
                            comentarios = binding.txvNuevaRutaComentariosValue.text.toString()
                        )
                    )

                    rutaComercialViewModel.dataSaveRutaCabecera.observe(this){ response->
                        when(response.ErrorCodigo){
                            500 ->{
                                BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                                    //Aceptar
                                    usuarioViewModel.logOut()
                                }
                            }
                            0 -> {
                                showMessage(this, response.ErrorMensaje)
                                setResult(RESULT_OK)
                                onBackPressedDispatcher.onBackPressed()
                            }
                            else -> { showMessage(this, response.ErrorMensaje) }
                        }
                    }
                } catch (e:Exception){
                    showMessage(this, e.message.toString())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

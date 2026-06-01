package com.mobile.massiveapp.ui.view.programacion

import android.app.Activity
import android.app.AlertDialog
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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.mobile.massiveapp.MassiveApp.Companion.prefsRutaComercial
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityEditarRutaComercialBinding
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.ui.adapters.RutaComercialDetalleAdapter
import com.mobile.massiveapp.ui.adapters.extension.SwipeToDeletePedidos
import com.mobile.massiveapp.ui.base.BaseDialogAlert
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogComentarioRuta
import com.mobile.massiveapp.ui.base.BaseDialogDireccionCliente
import com.mobile.massiveapp.ui.base.BaseDialogEdtCharacterLimit
import com.mobile.massiveapp.ui.view.pedidocliente.BuscarClienteActivity
import com.mobile.massiveapp.ui.view.util.agregarRutaComercialCabecera
import com.mobile.massiveapp.ui.view.util.editarRutaComercialCabecera
import com.mobile.massiveapp.ui.view.util.eliminarRutaComercialCabecera
import com.mobile.massiveapp.ui.view.util.getFechaActual
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

@AndroidEntryPoint
class EditarRutaComercialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarRutaComercialBinding
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
        override fun isLongPressDragEnabled() = false

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
        binding = ActivityEditarRutaComercialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Editar Ruta Comercial"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        setDefaultUi()
        setData()
    }

    private fun setData() {
        generalViewModel.getAllGeneralVendedores()

        rutaComercialViewModel.cargarRuta(prefsRutaComercial.getAccDocEntry())
        rutaComercialViewModel.dataGetRutaComercial.observe(this) { ruta ->
            binding.txvEditarRutaFechaRutaValue.text = ruta?.DocDate ?: ""
            binding.txvEditarRutaComentariosValue.text = ruta?.Comments?:""
            generalViewModel.dataGetAllGeneralVendedores.observeOnce(this) { vendedores ->
                binding.txvNuevaRutaVendedorValue.text = vendedores.filter { it.SlpCode == (ruta?.SlpCode?:-1) }.first().SlpName
                this.slpCode = vendedores.first { it.SlpCode == (ruta?.SlpCode ?: -1) }.SlpCode
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rutaComercialViewModel.detalleFlow.collectLatest { lista ->
                    listaRutaDetalles = lista
                    if (addedCardCodes.isEmpty()) lista.forEach { addedCardCodes.add(it.CardCode) }
                    detalleAdapter.updateData(lista)

                    if (lista.isNotEmpty()) {
                        binding.rvEditarRutaClientes.post {
                            binding.bubbleScrollBar.visibility = View.VISIBLE
                            binding.bubbleScrollBar.attachToRecyclerView(binding.rvEditarRutaClientes)
                        }
                    }
                    binding.txvEditarRutaClientesValue.text = "${lista.size} ${if (lista.size == 1) "cliente" else "clientes"}"
                }
            }
        }
    }


    private fun setDefaultUi() {
        (binding.rvEditarRutaClientes.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        binding.txvEditarRutaFechaRutaValue.text = getFechaActual()

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

        binding.clEditarRutaComentarios.setOnClickListener {
            BaseDialogComentarioRuta(
                binding.txvEditarRutaComentariosValue.text.toString()
            ) { comentario ->
                binding.txvEditarRutaComentariosValue.text = comentario
            }.show(supportFragmentManager, "ComentarioRutaDialog")
        }

        binding.clEditarRutaFechaRuta.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mostrarCalendarioMaterial(
                    this,
                    binding.txvEditarRutaFechaRutaValue.text.toString().ifEmpty { getFechaActual() }
                ) { dia, mes, anio ->
                    binding.txvEditarRutaFechaRutaValue.text = "$anio-$mes-$dia"
                }
            }
        }

        binding.clEditarRutaClientes.setOnClickListener {
            startForClienteResult.launch(Intent(this, BuscarClienteActivity::class.java))
        }

        //Adapter RV
        detalleAdapter = RutaComercialDetalleAdapter(
            dataSet = emptyList(),
            onStartDrag = { viewHolder -> itemTouchHelper.startDrag(viewHolder) },
            onItemClick = { detalle ->
                if (detalle.Status == "P"){
                    getDireccionesCliente(detalle)
                }
            }
        )
        binding.rvEditarRutaClientes.adapter = detalleAdapter
        itemTouchHelper.attachToRecyclerView(binding.rvEditarRutaClientes)



        /**SWIPE TO CANCEL**/
        val swipeToCancelCallback = object : SwipeToDeletePedidos(this) {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_ID.toInt() || listaRutaDetalles.isEmpty()) return makeMovementFlags(0, 0)
                return if (listaRutaDetalles[position].Status == "P") {
                    makeMovementFlags(0, ItemTouchHelper.RIGHT)
                } else {
                    makeMovementFlags(0, 0)
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val docLine = listaRutaDetalles[position].LineNum
                val accDocEntry = listaRutaDetalles[position].AccDocEntry
                rutaComercialViewModel.cancelarRutaComercialDetalle(docLine, accDocEntry)
                binding.rvEditarRutaClientes.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToCancelCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvEditarRutaClientes)
        /**SWIPE TO CANCEL**/
    }

    private fun getDireccionesCliente(detalle: DoRutaComercialDetalleView) {
        rutaComercialViewModel.getAllDireccionesCliente(detalle.CardCode)
        rutaComercialViewModel.dataGetAllDireccionesCliente.observeOnceNotNull(this) { direccionesCliente ->
            BaseDialogDireccionCliente(
                checkSelected = detalle.Street,
                direcciones = direccionesCliente
            ) { calleSeleccionada,id ->
                rutaComercialViewModel.updateAddress(detalle.AccDocEntry, calleSeleccionada, detalle.LineNum)
            }.show(supportFragmentManager, "DireccionClienteDialog")
        }
    }



















    private val startForClienteResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val cardCode = result.data?.getStringExtra("cardCode")
                if (!cardCode.isNullOrEmpty()) {
                    if (addedCardCodes.contains(cardCode)) {
                        Toast.makeText(this, "El cliente ya fue agregado", Toast.LENGTH_SHORT).show()
                    } else {
                        addedCardCodes.add(cardCode)
                        rutaComercialViewModel.saveDetalle(prefsRutaComercial.getAccDocEntry(), cardCode)
                    }
                }
            }
        }


















    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        val gotValidate = listaRutaDetalles.filter { it.Status == "A" }
        if (gotValidate.isNotEmpty()){
            menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        }
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

                    val fechaRuta = binding.txvEditarRutaFechaRutaValue.text.toString()
                    if (fechaRuta.isEmpty()) {
                        throw ( Exception("Debe seleccionar una fecha de ruta"))
                    }
                    if (addedCardCodes.isEmpty()) {
                        throw ( Exception("Debe agregar al menos un cliente"))
                    }

                    rutaComercialViewModel.dataGetRutaComercial.observe(this){ ruta->

                        rutaComercialViewModel.saveRutaCabecera(
                            editarRutaComercialCabecera(
                                accDocEntry = prefsRutaComercial.getAccDocEntry(),
                                fechaRuta = binding.txvEditarRutaFechaRutaValue.text.toString(),
                                slpCode = slpCode,
                                comentarios = binding.txvEditarRutaComentariosValue.text.toString(),
                                accCreateDate = ruta?.AccCreateDate?:"",
                                accCreateHour = ruta?.AccCreateHour?:"",
                                accCreateUser = ruta?.AccCreateUser?:""
                            )
                        )
                    }

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
            R.id.app_bar_delete -> confirmarEliminarRuta()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun confirmarEliminarRuta() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar ruta")
            .setMessage("¿Está seguro que desea eliminar esta ruta comercial?")
            .setPositiveButton("Eliminar") { _, _ ->
                rutaComercialViewModel.dataGetRutaComercial.observe(this){ ruta->

                    rutaComercialViewModel.saveRutaCabecera(
                        eliminarRutaComercialCabecera(
                            accDocEntry = prefsRutaComercial.getAccDocEntry(),
                            fechaRuta = binding.txvEditarRutaFechaRutaValue.text.toString(),
                            slpCode = slpCode,
                            comentarios = binding.txvEditarRutaComentariosValue.text.toString(),
                            accCreateDate = ruta?.AccCreateDate?:"",
                            accCreateHour = ruta?.AccCreateHour?:"",
                            accCreateUser = ruta?.AccCreateUser?:""
                        )
                    )
                }

                rutaComercialViewModel.dataSaveRutaCabecera.observe(this){ response->
                    when(response.ErrorCodigo){
                        500 ->{
                            BaseDialogAlert(this).showConfirmationDialog("Su sesión ha sido cerrada"){
                                //Aceptar
                                usuarioViewModel.logOut()
                            }
                        }
                        0 -> {
                            showMessage(this,"Ruta Cancelada")
                            setResult(RESULT_OK)
                            onBackPressedDispatcher.onBackPressed()
                        }
                        else -> { showMessage(this, response.ErrorMensaje) }
                    }

                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

package com.mobile.massiveapp.ui.view.programacion

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityNuevaRutaComercialBinding
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.ui.adapters.RutaComercialDetalleAdapter
import com.mobile.massiveapp.ui.adapters.extension.SwipeToDeletePedidos
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.view.pedidocliente.BuscarClienteActivity
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getCodigoDeDocumentoActual
import com.mobile.massiveapp.ui.view.util.mostrarCalendarioMaterial
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.view.util.observeOnceNotNull
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.RutaComercialViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NuevaRutaComercialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevaRutaComercialBinding
    private val rutaComercialViewModel: RutaComercialViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private lateinit var accDocEntry: String
    private lateinit var detalleAdapter: RutaComercialDetalleAdapter
    private val addedCardCodes = mutableSetOf<String>()
    private var listaRutaDetalles:List<DoRutaComercialDetalleView> = emptyList()

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

        accDocEntry = getCodigoDeDocumentoActual(this)
        rutaComercialViewModel.initAccDocEntry(accDocEntry)

        setDefaultUi()
        observeDetalle()
        setData()
    }

    private fun setData() {
        generalViewModel.getAllGeneralVendedores()
    }


    private fun setDefaultUi() {
        binding.txvNuevaRutaFechaRutaValue.text = getFechaActual()

        binding.clNuevaRutaVendedor.setOnClickListener {
            generalViewModel.dataGetAllGeneralVendedores.observeOnce(this) { vendedores ->
                BaseDialogChecklistWithId(
                    checkSelected = binding.txvNuevaRutaVendedorValue.text.toString(),
                    opciones = vendedores.map { it.SlpName }
                ) { nombreSeleccionado, _ ->
                    if (nombreSeleccionado.isNotEmpty()) {
                        binding.txvNuevaRutaVendedorValue.text = nombreSeleccionado
                    }
                }.show(supportFragmentManager, "VendedorDialog")
            }
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

        /**SWIPE TO DELETE**/
        val swipeToDeleteCallback = object : SwipeToDeletePedidos(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val docLine = listaRutaDetalles[position].DocLine
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
            BaseDialogChecklistWithId(
                checkSelected = detalle.Address,
                opciones = direccionesCliente.map { it.Street }
            ) { calleSeleccionada, _ ->
                if (calleSeleccionada.isNotEmpty()) {
                    rutaComercialViewModel.updateAddress(detalle.AccDocEntry, detalle.CardCode, calleSeleccionada)
                }
            }.show(supportFragmentManager, "DireccionClienteDialog")
        }
    }

    private fun observeDetalle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rutaComercialViewModel.detalleFlow.collectLatest { listaDetallesRutaComercial ->
                    listaRutaDetalles = listaDetallesRutaComercial

                    detalleAdapter.updateData(listaDetallesRutaComercial)
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
                    if (addedCardCodes.contains(cardCode)) {
                        Toast.makeText(this, "El cliente ya fue agregado", Toast.LENGTH_SHORT).show()
                    } else {
                        addedCardCodes.add(cardCode)
                        rutaComercialViewModel.saveDetalle(accDocEntry, cardCode)
                    }
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
            R.id.app_bar_check -> guardarRuta()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun guardarRuta() {
        val nombreVendedor = binding.txvNuevaRutaVendedorValue.text.toString()
        if (nombreVendedor == "Seleccionar vendedor") {
            Toast.makeText(this, "Debe seleccionar un vendedor", Toast.LENGTH_SHORT).show()
            return
        }
        val fechaRuta = binding.txvNuevaRutaFechaRutaValue.text.toString()
        if (fechaRuta.isEmpty()) {
            Toast.makeText(this, "Debe seleccionar una fecha de ruta", Toast.LENGTH_SHORT).show()
            return
        }
        if (addedCardCodes.isEmpty()) {
            Toast.makeText(this, "Debe agregar al menos un cliente", Toast.LENGTH_SHORT).show()
            return
        }
        rutaComercialViewModel.saveRutaCabecera(
            accDocEntry = accDocEntry,
            fechaRuta = fechaRuta,
            nombreVendedor = nombreVendedor
        )
        Toast.makeText(this, "Ruta guardada exitosamente", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

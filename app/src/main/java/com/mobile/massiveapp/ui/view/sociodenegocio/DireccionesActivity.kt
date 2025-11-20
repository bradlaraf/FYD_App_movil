package com.mobile.massiveapp.ui.view.sociodenegocio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.ui.adapters.SocioDireccionesAdapter
import com.mobile.massiveapp.databinding.ActivityDireccionesFiscalBinding
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import com.mobile.massiveapp.ui.adapters.extension.SwipeToDeletePedidos
import com.mobile.massiveapp.ui.base.BaseDialogChecklist
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.viewmodel.SocioDireccionesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DireccionesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDireccionesFiscalBinding
    private lateinit var adapterDireccionesFiscales: SocioDireccionesAdapter
    private val direccionesViewModel: SocioDireccionesViewModel by viewModels()
    private var listaDireccionesAEliminar: List<DoSocioDirecciones> = emptyList()
    private var hashInfoActivity = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDireccionesFiscalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


            //Se traen todas las direcciones  registradas
        if (intent.getStringExtra("cardCode") != null){

            hashInfoActivity["cardCode"] = intent.getStringExtra("cardCode").toString()
            hashInfoActivity["tipo"] = intent.getStringExtra("tipo").toString()


            //Se setea el TITULO de la ACTIVITY
            title = if (intent.getStringExtra("tipo").toString() == "S") {
                "Direcciones Despacho"
            } else if (intent.getStringExtra("tipo").toString() == "B") {
                "Direcciones Fiscales"
            } else {
                ""
            }

            direccionesViewModel.getAllDireccionesPorCardCodeYTipo(
                hashInfoActivity["cardCode"].toString(),
                intent.getStringExtra("tipo").toString()
            )
        }

            //Seteo del Adapter
        adapterDireccionesFiscales = SocioDireccionesAdapter(emptyList()) { direccionSeleccionada->
            Intent(this, NuevaDireccionActivity::class.java)
                .putExtra("cardCode", direccionSeleccionada.CardCode)
                .putExtra("lineNum", direccionSeleccionada.LineNum)
                .putExtra("accDocEntry", direccionSeleccionada.AccDocEntry)
                .putExtra("tipo", direccionSeleccionada.AdresType)
                .apply {
                    startForNuevaDirecFiscalResult.launch(this)
                }
            }
        binding.rvDireccionesFiscal.adapter = adapterDireccionesFiscales


            //LiveData de las direcciones
        direccionesViewModel.dataGetAllDireccionesPorCardCodeYTipo.observe(this){ listaDirecciones->
            try {
                binding.txvDireccionActual.text = listaDirecciones.firstOrNull()?.Street ?: ""
                listaDireccionesAEliminar = listaDirecciones
                adapterDireccionesFiscales.updateList(listaDirecciones)
            } catch (e: Exception){
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

                //Dialog para elegir direccion fiscal
            binding.clDireccionFiscalActual.setOnClickListener {
                BaseDialogChecklist(
                    listaDirecciones.map { it.Street }
                ){ direccionSeleccionada ->
                    if (direccionSeleccionada.isNotEmpty()) {
                        binding.txvDireccionActual.text = direccionSeleccionada
                    }
                }.show(supportFragmentManager, "BaseDialogChecklist")
            }
        }


        //(btn) para crear una NUEVA DIRECCION
        binding.clNuevaDireccionFiscal.setOnClickListener {
            Intent(this, NuevaDireccionActivity::class.java)
                .putExtra("cardCode", hashInfoActivity["cardCode"].toString())
                .putExtra("accDocEntry", intent.getStringExtra("accDocEntry"))
                .putExtra("tipo", intent.getStringExtra("tipo").toString())
                .also{
                    startForNuevaDirecFiscalResult.launch(it)
                }
        }

        /**SWIPE TO DELETE**/
        val swipeToDeleteCallback = object :SwipeToDeletePedidos(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition

                direccionesViewModel.deleteUnaDireccionPorCardCodeYTipo(
                    listaDireccionesAEliminar[position].CardCode,
                    listaDireccionesAEliminar[position].AdresType
                )

                direccionesViewModel.dataDeleteDireccionPorCardCodeYTipo.observe(this@DireccionesActivity){ success->
                    val rv = binding.rvDireccionesFiscal
                    rv.adapter?.notifyItemRemoved(position)

                    if (success){
                        direccionesViewModel.getAllDireccionesPorCardCodeYTipo(
                            hashInfoActivity["cardCode"].toString(),
                            hashInfoActivity["tipo"].toString()
                        )
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvDireccionesFiscal)

    }



    //Manejo de la respuesta de la NUEVA DIRECCION
    val startForNuevaDirecFiscalResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val cardCode = data?.getStringExtra("cardCode")
            val tipo = data?.getStringExtra("tipo")
            if (cardCode != null && tipo != null){
                hashInfoActivity["cardCode"] = cardCode
                hashInfoActivity["tipo"] = tipo
                direccionesViewModel.getAllDireccionesPorCardCodeYTipo(cardCode, tipo)
            }
        }
    }







    /*----------------BARRA DE TITULO - NAV -------------------*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        setResult(RESULT_OK,
            Intent()
                .putExtra("tipo", hashInfoActivity["tipo"].toString())
                .putExtra("direccion", binding.txvDireccionActual.text.toString())
        )
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home->{
                setResult(RESULT_CANCELED, Intent()
                    .putExtra("tipo", hashInfoActivity["tipo"].toString())
                    .putExtra("direccion", binding.txvDireccionActual.text)
                )
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.app_bar_check -> {
                onBackPressed()
            }

            R.id.app_bar_delete->{
                BaseDialogChecklistWithId(
                    listaDireccionesAEliminar.map { it.Street }
                ){ direccionSeleccionada, id->
                    direccionesViewModel.deleteUnaDireccionPorCardCodeYTipo(
                        listaDireccionesAEliminar[id].CardCode,
                        listaDireccionesAEliminar[id].AdresType
                    )
                }.show(supportFragmentManager, "BaseDialogChecklistWithId")

                direccionesViewModel.dataDeleteDireccionPorCardCodeYTipo.observe(this){ success->
                    if (success){
                        direccionesViewModel.getAllDireccionesPorCardCodeYTipo(
                            hashInfoActivity["cardCode"].toString(),
                            hashInfoActivity["tipo"].toString()
                        )
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
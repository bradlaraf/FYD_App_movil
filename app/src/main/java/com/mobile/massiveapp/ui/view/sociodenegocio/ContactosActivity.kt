package com.mobile.massiveapp.ui.view.sociodenegocio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityContactoBinding
import com.mobile.massiveapp.domain.model.DoSocioContactos
import com.mobile.massiveapp.ui.adapters.SocioContactosAdapter
import com.mobile.massiveapp.ui.adapters.extension.SwipeToDeletePedidos
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.viewmodel.SocioContactoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactoBinding
    private lateinit var socioContactosAdapter: SocioContactosAdapter
    private val socioContactoViewModel: SocioContactoViewModel by viewModels()
    private var currentCardCode = ""
    private var listaContactosAEliminar: List<DoSocioContactos> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        //Inicializa el adapter
        socioContactosAdapter = SocioContactosAdapter(emptyList()){ contactoSeleccionado->

            Intent(this, NuevoContactoActivity::class.java).apply {
                putExtra("cardCode", contactoSeleccionado.CardCode)
                putExtra("cecularContacto", contactoSeleccionado.Cellolar)
            }.also {
                startForNewContacto.launch(it)
            }

        }
        binding.rvContactosSn.adapter = socioContactosAdapter

        //CardCode del Nuevo SN
        if (intent.getStringExtra("cardCode") != null){
            currentCardCode = intent.getStringExtra("cardCode").toString()
        }

        //Obtener lista de contactos por el CardCode
        socioContactoViewModel.getSocioContactosPorCardCode(currentCardCode)

        //LiveData de los contactos por CardCode
        socioContactoViewModel.dataGetSocioContactosPorCardCode.observe(this){ listaContactos->
            try {
                listaContactosAEliminar = listaContactos
                socioContactosAdapter.updateData(listaContactos)
                if (listaContactos.isNotEmpty()){
                    binding.txvContactoActual.text = listaContactos[0].Name
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        //Click Nuevo DocumentoContacto
        binding.clNuevoContacto.setOnClickListener {
            Intent(this, NuevoContactoActivity::class.java)
                .putExtra("cardCode", intent.getStringExtra("cardCode").toString())
                .putExtra("accDocEntry", intent.getStringExtra("accDocEntry").toString())
                .also {
                    startForNewContacto.launch(it)
                }
        }

        val swipeToDeleteCallback = object :SwipeToDeletePedidos(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition

                socioContactoViewModel.deleteSocioContactoPorCelularYCardCode(
                    celular = listaContactosAEliminar[position].Cellolar,
                    cardCode = listaContactosAEliminar[position].CardCode
                )

                socioContactoViewModel.dataDeleteSocioContactoPorCelularYCardCode.observe(this@ContactosActivity){ success->
                    val rv = binding.rvContactosSn
                    rv.adapter?.notifyItemRemoved(position)

                    if (success){
                        socioContactoViewModel.getSocioContactosPorCardCode(currentCardCode)
                    }
                }

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvContactosSn)


    }


    val startForNewContacto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val cardCode = data?.getStringExtra("cardCode")
            val succsessResponse = data?.getBooleanExtra("contactoAgregado", false)
            if (succsessResponse == true){
                if (currentCardCode.isNotEmpty()) {
                    currentCardCode = cardCode.toString()
                    socioContactoViewModel.getSocioContactosPorCardCode(currentCardCode)
                }
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
                .putExtra("contacto", binding.txvContactoActual.text.toString())
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

            android.R.id.home ->{
                setResult(RESULT_CANCELED, Intent()
                    .putExtra("contacto", binding.txvContactoActual.text)
                )
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.app_bar_check -> {
                onBackPressed()
            }

            R.id.app_bar_delete->{
                BaseDialogChecklistWithId(
                    listaContactosAEliminar.map { it.Name }
                ){ contactoSeleccionado, id->
                    socioContactoViewModel.deleteSocioContactoPorCelularYCardCode(
                        celular = listaContactosAEliminar[id].Cellolar,
                        cardCode = listaContactosAEliminar[id].CardCode
                    )
                }.show(supportFragmentManager, "BaseDialogChecklistWithId")

                socioContactoViewModel.dataDeleteSocioContactoPorCelularYCardCode.observe(this){ success->
                    if (success){
                        socioContactoViewModel.getSocioContactosPorCardCode(currentCardCode)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
package com.mobile.massiveapp.ui.view.infodata

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.mobile.massiveapp.databinding.ActivityInfoTablesBinding
import com.mobile.massiveapp.ui.adapters.InfoTablasAdapter
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.Exception

@AndroidEntryPoint
class InfoTablesActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityInfoTablesBinding
    private lateinit var infoTablasAdapter: InfoTablasAdapter
    private val generalViewModel: GeneralViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoTablesBinding.inflate(layoutInflater)
        allocateActivityTitle("Información Tablas")
        setContentView(binding.root)

            //Inicializa el adapter
        infoTablasAdapter = InfoTablasAdapter(emptyList()){}
        binding.rvInfoTablaAdapter.adapter = infoTablasAdapter

        generalViewModel.getInfoTablas()
        generalViewModel.dataGetInfoTablas.observe(this){ listaInfoTablas->
            try {
                if (listaInfoTablas.isEmpty()){
                    throw Exception("No hay datos")
                }
                infoTablasAdapter.updateData(listaInfoTablas)
            } catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}
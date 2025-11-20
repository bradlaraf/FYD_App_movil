package com.mobile.massiveapp.ui.view.log

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.mobile.massiveapp.databinding.ActivityLogBinding
import com.mobile.massiveapp.ui.adapters.LogAdapter
import com.mobile.massiveapp.ui.view.menu.drawer.DrawerBaseActivity
import com.mobile.massiveapp.ui.viewmodel.LogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityLogBinding
    private lateinit var logAdapter: LogAdapter
    private val logViewModel: LogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logAdapter = LogAdapter(emptyList()){ error->
            Intent(this, InfoErrorActivity::class.java)
                .putExtra("fecha", error.ErrorDate)
                .putExtra("hora", error.ErrorHour)
                .also { startActivity(it) } }
        binding.rvLog.adapter = logAdapter

        logViewModel.getAllErrores()
        logViewModel.dataGetAllErrores.observe(this){ listaErrores->
            logAdapter.updateData(listaErrores)
        }

    }
}
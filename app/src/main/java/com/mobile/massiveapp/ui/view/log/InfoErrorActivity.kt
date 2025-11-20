package com.mobile.massiveapp.ui.view.log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.mobile.massiveapp.databinding.ActivityInfoErrorBinding
import com.mobile.massiveapp.ui.viewmodel.LogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoErrorBinding
    private val logViewModel: LogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        logViewModel.getError(
            intent.getStringExtra("fecha").toString(),
            intent.getStringExtra("hora").toString()
        )

        logViewModel.dataGetError.observe(this){error->
            binding.txvLogInfoCodigoValue.text = error.ErrorCode
            binding.txvLogInfoFechaValue.text = error.ErrorDate
            binding.txvLogInfoHoraValue.text = error.ErrorHour
            binding.txvLogInfoMensajeValue.text = error.ErrorMessage
        }
    }









    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}
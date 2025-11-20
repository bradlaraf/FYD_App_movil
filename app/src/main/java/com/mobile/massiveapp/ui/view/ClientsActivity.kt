package com.mobile.massiveapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.massiveapp.databinding.ActivityClientsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
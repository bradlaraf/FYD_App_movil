package com.mobile.massiveapp.ui.view.inventario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.massiveapp.databinding.ActivityListaPreciosBinding

class ListaPreciosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListaPreciosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaPreciosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
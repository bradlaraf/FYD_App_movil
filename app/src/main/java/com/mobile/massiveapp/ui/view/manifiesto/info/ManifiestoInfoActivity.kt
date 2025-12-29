package com.mobile.massiveapp.ui.view.manifiesto.info

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mobile.massiveapp.databinding.ActivityManifiestoInfoBinding
import com.mobile.massiveapp.ui.adapters.fragment.FMManifiestoAdapter
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ManifiestoInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManifiestoInfoBinding
    private val manifiestoViewModel: ManifiestoViewModel by viewModels()
    private var tabTitle = arrayOf("General", "Documento")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManifiestoInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTabLayout()
        setDefaultUi()
        setContent()
    }

    private fun setTabLayout() {
        try {
            val pager = binding.viewPagerConductor
            val tl = binding.tabLayoutManifiesto
            pager.adapter = FMManifiestoAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tl, pager){ tab, position ->
                tab.text = tabTitle[position]
            }.attach()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun setContent() {
        manifiestoViewModel.getAllManifiestoDocumento()
    }

    private fun setDefaultUi() {

    }





    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
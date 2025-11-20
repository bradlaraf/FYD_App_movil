package com.mobile.massiveapp.ui.adapters.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobile.massiveapp.ui.view.cobranzas.fragments.CobranzasAprobadosFragment
import com.mobile.massiveapp.ui.view.cobranzas.fragments.InfoCobranzaCabeceraFragment
import com.mobile.massiveapp.ui.view.cobranzas.fragments.InfoCobranzaContenidoFragment

class FMInfoCobranzaAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> InfoCobranzaCabeceraFragment()
            1 -> InfoCobranzaContenidoFragment()

            else -> CobranzasAprobadosFragment()
        }

    }
}
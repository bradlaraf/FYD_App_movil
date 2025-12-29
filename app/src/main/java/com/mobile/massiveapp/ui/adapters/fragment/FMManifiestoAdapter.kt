package com.mobile.massiveapp.ui.adapters.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobile.massiveapp.ui.view.manifiesto.info.fragments.ManifiestoAyudanteFragment
import com.mobile.massiveapp.ui.view.manifiesto.info.fragments.ManifiestoDocumentoFragment
import com.mobile.massiveapp.ui.view.manifiesto.info.fragments.ManifiestoFragment

class FMManifiestoAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ManifiestoFragment()
            1 -> ManifiestoDocumentoFragment()

            else -> ManifiestoFragment()
        }

    }
}
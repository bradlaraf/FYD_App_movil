package com.mobile.massiveapp.ui.adapters.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobile.massiveapp.ui.view.sociodenegocio.fragments.ClienteInfoCondicionesFragment
import com.mobile.massiveapp.ui.view.sociodenegocio.fragments.ClienteInfoDireccionesFragment
import com.mobile.massiveapp.ui.view.sociodenegocio.fragments.InfoContactosFragment
import com.mobile.massiveapp.ui.view.sociodenegocio.fragments.InfoGeneralFragment

class FMClientesInfoAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> InfoGeneralFragment()
            1 -> ClienteInfoCondicionesFragment()
            2 -> InfoContactosFragment()
            3 -> ClienteInfoDireccionesFragment()

            else -> InfoGeneralFragment()
        }

    }
}
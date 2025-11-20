package com.mobile.massiveapp.ui.adapters.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobile.massiveapp.ui.view.sociodenegocio.fragments.ClientesFragment
import com.mobile.massiveapp.ui.view.sociodenegocio.fragments.LeadFragment

class FMClientesAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ClientesFragment()
            1 -> LeadFragment()

            else -> ClientesFragment()
        }

    }
}
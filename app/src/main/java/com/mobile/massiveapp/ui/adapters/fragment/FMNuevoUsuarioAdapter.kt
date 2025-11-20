package com.mobile.massiveapp.ui.adapters.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobile.massiveapp.ui.view.usuarios.fragments.UsuarioAlmacenesFragment
import com.mobile.massiveapp.ui.view.usuarios.fragments.UsuarioGeneralFragment
import com.mobile.massiveapp.ui.view.usuarios.fragments.UsuarioGrupoArticuloFragment
import com.mobile.massiveapp.ui.view.usuarios.fragments.UsuarioGrupoSocioFragment
import com.mobile.massiveapp.ui.view.usuarios.fragments.UsuarioListaPrecioFragment
import com.mobile.massiveapp.ui.view.usuarios.fragments.UsuarioZonasFragment

class FMNuevoUsuarioAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 6

    private val fragmentList: MutableMap<Int, Fragment> = mutableMapOf()

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> UsuarioGeneralFragment()
            1 -> UsuarioAlmacenesFragment()
            2 -> UsuarioListaPrecioFragment()
            3 -> UsuarioGrupoArticuloFragment()
            4 -> UsuarioGrupoSocioFragment()
            5 -> UsuarioZonasFragment()
            else -> UsuarioGeneralFragment()
        }
        fragmentList[position] = fragment
        return fragment
    }

}
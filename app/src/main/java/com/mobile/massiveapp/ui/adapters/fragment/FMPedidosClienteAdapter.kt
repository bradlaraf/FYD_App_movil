package com.mobile.massiveapp.ui.adapters.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.PedidoClienteCanceladosFragment
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.PedidoClientePendientesFragment
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.PedidoClienteTodosFragment

class FMPedidosClienteAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PedidoClienteTodosFragment()
            1 -> PedidoClientePendientesFragment()
            2 -> PedidoClienteCanceladosFragment()

            else -> PedidoClienteTodosFragment()
        }

    }
}
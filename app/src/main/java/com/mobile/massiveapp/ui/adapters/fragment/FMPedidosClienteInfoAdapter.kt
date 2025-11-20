package com.mobile.massiveapp.ui.adapters.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.PedidoClienteInfoCabeceraFragment
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.PedidoClienteInfoContenidoFragment
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.PedidoClienteInfoFinanzasFragment
import com.mobile.massiveapp.ui.view.pedidocliente.fragments.PedidoClienteInfoLogisticaFragment

class FMPedidosClienteInfoAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PedidoClienteInfoCabeceraFragment()
            1 -> PedidoClienteInfoContenidoFragment()
            2 -> PedidoClienteInfoLogisticaFragment()
            3 -> PedidoClienteInfoFinanzasFragment()

            else -> PedidoClienteInfoCabeceraFragment()
        }

    }
}
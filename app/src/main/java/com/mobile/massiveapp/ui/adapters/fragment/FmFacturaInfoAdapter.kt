package com.mobile.massiveapp.ui.adapters.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobile.massiveapp.ui.view.facturas.fragments.InfoFacturaCabeceraFragment
import com.mobile.massiveapp.ui.view.facturas.fragments.InfoFacturaDetalleFragment
import com.mobile.massiveapp.ui.view.facturas.fragments.InfoFacturaLogisticaFragment
import com.mobile.massiveapp.ui.view.inventario.fragments.GeneralInfoArticuloFragment

class FmFacturaInfoAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> InfoFacturaCabeceraFragment()
            1 -> InfoFacturaDetalleFragment()
            2 -> InfoFacturaLogisticaFragment()

            else -> GeneralInfoArticuloFragment()
        }

    }
}
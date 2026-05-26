package com.mobile.massiveapp.ui.view.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.mobile.massiveapp.domain.model.DoRutaComercial
import com.mobile.massiveapp.domain.model.DoRutaComercialView

class RutaComercialDiffUtil(
    private val oldList: List<DoRutaComercialView>,
    private val newList: List<DoRutaComercialView>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].AccDocEntry == newList[newItemPosition].AccDocEntry
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

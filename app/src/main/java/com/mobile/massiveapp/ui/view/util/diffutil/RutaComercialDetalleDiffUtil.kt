package com.mobile.massiveapp.ui.view.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView

class RutaComercialDetalleDiffUtil(
    private val oldList: List<DoRutaComercialDetalleView>,
    private val newList: List<DoRutaComercialDetalleView>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].CardCode == newList[newItemPosition].CardCode
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

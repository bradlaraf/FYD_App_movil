package com.mobile.massiveapp.ui.view.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.mobile.massiveapp.domain.model.DoClienteFacturaDetalle

class FacturasDetalleDiffUtil (
    private val oldList: List<DoClienteFacturaDetalle>,
    private val newList: List<DoClienteFacturaDetalle>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].DocEntry == newList[newItemPosition].DocEntry &&
                oldList[oldItemPosition].LineNum == newList[newItemPosition].LineNum
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }
}
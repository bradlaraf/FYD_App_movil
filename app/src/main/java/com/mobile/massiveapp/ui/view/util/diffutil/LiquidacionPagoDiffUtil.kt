package com.mobile.massiveapp.ui.view.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.mobile.massiveapp.domain.model.DoLiquidacionPagoView
import com.mobile.massiveapp.domain.model.DoPagoDetalle

class LiquidacionPagoDiffUtil  (
    private val oldList: List<DoLiquidacionPagoView>,
    private val newList: List<DoLiquidacionPagoView>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].DocLine == newList[newItemPosition].DocLine
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }
}
package com.mobile.massiveapp.ui.view.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.mobile.massiveapp.domain.model.DoClienteFacturas
import com.mobile.massiveapp.domain.model.DoFormaPago
import com.mobile.massiveapp.domain.model.DoFormaPagoItemView

class FormaPagoDiffUtil (
    private val oldList: List<DoFormaPagoItemView>,
    private val newList: List<DoFormaPagoItemView>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].Id == newList[newItemPosition].Id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }
}
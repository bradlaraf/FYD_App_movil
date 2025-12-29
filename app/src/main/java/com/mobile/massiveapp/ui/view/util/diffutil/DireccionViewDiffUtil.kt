package com.mobile.massiveapp.ui.view.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.mobile.massiveapp.domain.model.DoDireccionView
import com.mobile.massiveapp.domain.model.DoSocioDirecciones

class DireccionViewDiffUtil(
    private val oldList: List<DoDireccionView>,
    private val newList: List<DoDireccionView>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].CardCode == newList[newItemPosition].CardCode
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

package com.mobile.massiveapp.ui.view.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.mobile.massiveapp.domain.model.DoArticuloInventario

class ArticuloDiffUtil(

): DiffUtil.ItemCallback<DoArticuloInventario>() {
    override fun areItemsTheSame(oldItem: DoArticuloInventario, newItem: DoArticuloInventario): Boolean {
        return oldItem.ItemCode == newItem.ItemCode
    }

    override fun areContentsTheSame(oldItem: DoArticuloInventario, newItem: DoArticuloInventario): Boolean {
        return oldItem == newItem
    }
}
package com.mobile.massiveapp.ui.view.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.mobile.massiveapp.data.database.entities.ClienteSociosEntity

object SocioPagingComparator : DiffUtil.ItemCallback<ClienteSociosEntity>() {
    override fun areItemsTheSame(oldItem: ClienteSociosEntity, newItem: ClienteSociosEntity): Boolean {
        // Id is unique.
        return oldItem.CardCode == newItem.CardCode
    }

    override fun areContentsTheSame(oldItem: ClienteSociosEntity, newItem: ClienteSociosEntity): Boolean {
        return oldItem == newItem
    }
}
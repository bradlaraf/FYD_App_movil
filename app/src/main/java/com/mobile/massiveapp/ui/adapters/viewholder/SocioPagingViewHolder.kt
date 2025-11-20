package com.mobile.massiveapp.ui.adapters.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.database.entities.ClienteSociosEntity


class SocioPagingViewHolder(parent: ViewGroup, itemView: View): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_socio, parent, false)
) {
    val txvNumeroDocumento: TextView
    val txvCardName: TextView
    val imvMigrado: ImageView
    var storeObject: ClienteSociosEntity? = null

    init {
        txvCardName = itemView.findViewById(R.id.txvCardName)
        txvNumeroDocumento = itemView.findViewById(R.id.txvNumeroDocumento)
        imvMigrado = itemView.findViewById(R.id.imvSocioVerifySap)
    }

    fun bindTo(storeObject: ClienteSociosEntity){
        this.storeObject = storeObject
        if (storeObject.AccMigrated =="N"){
            imvMigrado.setImageResource(R.drawable.icon_cloud_await)
        } else if (storeObject.AccMigrated =="Y" && storeObject.AccFinalized == "N"){
            imvMigrado.setImageResource(R.drawable.icon_cloud_intermedia)
        } else if (storeObject.AccMigrated =="Y" && storeObject.AccFinalized == "Y"){
            imvMigrado.setImageResource(R.drawable.icon_cloud_done)
        }

        txvNumeroDocumento.text = storeObject.LicTradNum
        txvCardName.text = storeObject.CardName
    }
}
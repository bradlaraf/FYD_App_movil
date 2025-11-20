package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.database.entities.ClienteSociosEntity

class SociosPagingAdapter(
    diffCallBack: DiffUtil.ItemCallback<ClienteSociosEntity>,
    private val onClickListener:(ClienteSociosEntity) -> Unit
):PagingDataAdapter<ClienteSociosEntity, SociosPagingAdapter.ClienteSocioViewHolder>(diffCallBack) {

    class ClienteSocioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvNumeroDocumento: TextView
        val txvCardName: TextView
        val imvMigrado: ImageView

        init {
            txvCardName = view.findViewById(R.id.txvCardName)
            txvNumeroDocumento = view.findViewById(R.id.txvNumeroDocumento)
            imvMigrado = view.findViewById(R.id.imvSocioVerifySap)
        }

        fun bind(currentCliente: ClienteSociosEntity, onClickListener: (ClienteSociosEntity) -> Unit) {
            if (currentCliente.AccMigrated =="N"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_await)
            } else if (currentCliente.AccMigrated =="Y" && currentCliente.AccFinalized == "N"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_intermedia)
            } else if (currentCliente.AccMigrated =="Y" && currentCliente.AccFinalized == "Y"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_done)
            }

            txvNumeroDocumento.text = currentCliente.LicTradNum
            txvCardName.text = currentCliente.CardName
            itemView.setOnClickListener { onClickListener(currentCliente) }
        }

        fun bindPlaceholder() {
            txvNumeroDocumento.text = "Cargando..."
            txvCardName.text = "Cargando..."
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClienteSocioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ClienteSocioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteSocioViewHolder, position: Int) {
        val item = getItem(position)
        if (item == null){
            holder.bindPlaceholder()
        }else{
            holder.bind(item, onClickListener)
        }
    }
}
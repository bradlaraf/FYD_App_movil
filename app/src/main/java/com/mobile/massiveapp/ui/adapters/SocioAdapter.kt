package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoClienteSocios
import com.mobile.massiveapp.ui.view.util.diffutil.SocioNegocioDiffUtil

class SocioAdapter(
    private var dataSet: List<DoClienteSocios>,
    private val onClickListener:(DoClienteSocios) -> Unit
): RecyclerView.Adapter<SocioAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNumeroDocumento: TextView
        val txvCardName: TextView
        val imvMigrado: ImageView

        init {
            txvCardName = view.findViewById(R.id.txvCardName)
            txvNumeroDocumento = view.findViewById(R.id.txvNumeroDocumento)
            imvMigrado = view.findViewById(R.id.imvSocioVerifySap)
        }

        fun render(currentCliente: DoClienteSocios, onClickListener: (DoClienteSocios) -> Unit){

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
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentSocio = dataSet[position]
        viewHolder.render(currentSocio, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoClienteSocios>){
        val clienteDiffUtil = SocioNegocioDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(clienteDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoClienteScreen
import com.mobile.massiveapp.ui.view.util.diffutil.SocioScreenDiffUtil

class SocioScreenAdapter(
    private var dataset: List<DoClienteScreen>,
    private val onClickListener:(DoClienteScreen) -> Unit
): RecyclerView.Adapter<SocioScreenAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNumeroDocumento: TextView
        val txvCardName: TextView
        val imvMigrado: ImageView

        init {
            txvCardName = view.findViewById(R.id.txvCardName)
            txvNumeroDocumento = view.findViewById(R.id.txvNumeroDocumento)
            imvMigrado = view.findViewById(R.id.imvSocioVerifySap)
        }

        fun render(currentCliente: DoClienteScreen, onClickListener: (DoClienteScreen) -> Unit){

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
        val currentSocio = dataset[position]
        viewHolder.render(currentSocio, onClickListener)
    }


    override fun getItemCount() =dataset.size

    fun updateData(newDataSet: List<DoClienteScreen>) {
        val clienteDiffUtil = SocioScreenDiffUtil(dataset, newDataSet)
        val diffResult = DiffUtil.calculateDiff(clienteDiffUtil)
        dataset = newDataSet
        diffResult.dispatchUpdatesTo(this)

    }

    fun updateDataForSearching(newDataSet: List<DoClienteScreen>) {
        dataset = newDataSet
        notifyDataSetChanged()
    }
}
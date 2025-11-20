package com.mobile.massiveapp.ui.adapters

import android.graphics.Color
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

class SocioSeleccionableAdapter (
    private var dataSet: List<DoClienteScreen>,
    private val onClickListener:(DoClienteScreen) -> Unit
): RecyclerView.Adapter<SocioSeleccionableAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvCardCode: TextView
        val txvCardName: TextView
        val imvMigrado: ImageView

        init {
            txvCardName = view.findViewById(R.id.txvCardName)
            txvCardCode = view.findViewById(R.id.txvNumeroDocumento)
            imvMigrado = view.findViewById(R.id.imvSocioVerifySap)
        }

        fun render(currentCliente: DoClienteScreen){

            if (currentCliente.AccMigrated =="N"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_await)
            } else if (currentCliente.AccMigrated =="Y" && currentCliente.AccFinalized == "N"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_intermedia)
            } else if (currentCliente.AccMigrated =="Y" && currentCliente.AccFinalized == "Y"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_done)
            }

            txvCardCode.text = "${currentCliente.LicTradNum} - ${currentCliente.CardCode}"
            txvCardName.text = currentCliente.CardName

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentSocio = dataSet[position]
        viewHolder.render(currentSocio)

        viewHolder.itemView.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = viewHolder.layoutPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            itemSocioClick(currentSocio, onClickListener)
        }


        if (selectedPosition == viewHolder.layoutPosition){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#85D8FA"))
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }


    }
    fun itemSocioClick(socio: DoClienteScreen, onClickListener: (DoClienteScreen) -> Unit){
        onClickListener(socio)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoClienteScreen>){
        val clienteDiffUtil = SocioScreenDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(clienteDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateDataForSearching(newDataSet: List<DoClienteScreen>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}
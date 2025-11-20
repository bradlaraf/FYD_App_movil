package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.model.InfoTable
import com.mobile.massiveapp.ui.view.util.diffutil.InfoTablasDiffUtil

class InfoTablasAdapter(
    private var dataSet: List<InfoTable>,
    private val onClickListener:(InfoTable) -> Unit
): RecyclerView.Adapter<InfoTablasAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNombre: TextView
        val txvCantidad: TextView

        init {
            txvNombre = view.findViewById(R.id.txvInfoTablasNombreTabla)
            txvCantidad = view.findViewById(R.id.txvInfoTablasCantidadTabla)
        }

        fun render(infoTabla: InfoTable, onClickListener: (InfoTable) -> Unit){
            txvNombre.text = infoTabla.Tabla
            txvCantidad.text = infoTabla.Cantidad.toString()
            itemView.setOnClickListener { onClickListener(infoTabla) }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_info_tablas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPago = dataSet[position]
        viewHolder.render(currentPago, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<InfoTable>){
        val pagoDetalleItemsDiffUtil = InfoTablasDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pagoDetalleItemsDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
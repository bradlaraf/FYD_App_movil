package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.ui.view.util.diffutil.RutaComercialDetalleDiffUtil

class RutaComercialConfirmacionAdapter(
    private var dataSet: List<DoRutaComercialDetalleView>,
    private val onItemClick: (DoRutaComercialDetalleView) -> Unit
) : RecyclerView.Adapter<RutaComercialConfirmacionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvConfDocLine: TextView
        val txvConfCardName: TextView
        val txvConfStreet: TextView
        val txvConfMigrado: TextView

        init {
            txvConfDocLine  = view.findViewById(R.id.txvConfDocLine)
            txvConfCardName = view.findViewById(R.id.txvConfCardName)
            txvConfStreet   = view.findViewById(R.id.txvConfStreet)
            txvConfMigrado  = view.findViewById(R.id.txvConfMigrado)
        }

        fun render(item: DoRutaComercialDetalleView, onItemClick: (DoRutaComercialDetalleView) -> Unit) {
            txvConfDocLine.text  = (item.LineNum + 1).toString()
            txvConfCardName.text = item.CardName
            txvConfStreet.text   = item.Street.ifEmpty { item.Address }

            val migrado = item.AccMigrated == "Y"
            val aceptado = item.Status == "A"

            txvConfMigrado.text = if (migrado) "S" else "N"
            val color = if (migrado && aceptado)
                ContextCompat.getColor(itemView.context, R.color.color_green_dark)
            else
                ContextCompat.getColor(itemView.context, R.color.color_red)
            txvConfMigrado.setBackgroundColor(color)

            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ruta_comercial_confirmacion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(dataSet[position], onItemClick)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoRutaComercialDetalleView>) {
        val diffUtil = RutaComercialDetalleDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}

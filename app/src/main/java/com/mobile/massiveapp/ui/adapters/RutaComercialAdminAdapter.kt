package com.mobile.massiveapp.ui.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleAdminView
import com.mobile.massiveapp.ui.view.util.diffutil.RutaComercialDetalleAdminDiffUtil

class RutaComercialAdminAdapter(
    private var dataSet: List<DoRutaComercialDetalleAdminView>,
    private val onItemClick: (DoRutaComercialDetalleAdminView) -> Unit
) : RecyclerView.Adapter<RutaComercialAdminAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvAdminDocLine: TextView
        val txvAdminCardName: TextView
        val txvAdminStreet: TextView
        val txvAdminMigrado: TextView
        val clAdminRutaStatus: ConstraintLayout
        val imvAdminRutaStatus: ImageView

        init {
            txvAdminDocLine    = view.findViewById(R.id.txvAdminDocLine)
            txvAdminCardName   = view.findViewById(R.id.txvAdminCardName)
            txvAdminStreet     = view.findViewById(R.id.txvAdminStreet)
            txvAdminMigrado    = view.findViewById(R.id.txvAdminMigrado)
            clAdminRutaStatus  = view.findViewById(R.id.clAdminRutaStatus)
            imvAdminRutaStatus = view.findViewById(R.id.imvAdminRutaStatus)
        }

        fun render(item: DoRutaComercialDetalleAdminView, onItemClick: (DoRutaComercialDetalleAdminView) -> Unit) {
            txvAdminDocLine.text  = (item.LineNum + 1).toString()
            txvAdminCardName.text = item.CardName
            txvAdminStreet.text   = item.Street.ifEmpty { item.Address }
            val colorIcono = ContextCompat.getColor(itemView.context, R.color.color_white)
            imvAdminRutaStatus.setColorFilter(colorIcono, PorterDuff.Mode.SRC_IN)

            val migrado = item.AccMigrated == "Y"
            val aceptado = item.Status == "A"

            txvAdminMigrado.text = if (migrado) "S" else "N"
            var color = ContextCompat.getColor(itemView.context, R.color.color_green_dark)

            if (migrado && aceptado) {
                imvAdminRutaStatus.setImageResource(R.drawable.icon_confirmed)
            } else {
                color = ContextCompat.getColor(itemView.context, R.color.color_red)
                imvAdminRutaStatus.setImageResource(R.drawable.icon_pending)
            }

            clAdminRutaStatus.setBackgroundColor(color)

            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ver_ruta_comercial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(dataSet[position], onItemClick)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoRutaComercialDetalleAdminView>) {
        val diffUtil = RutaComercialDetalleAdminDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}

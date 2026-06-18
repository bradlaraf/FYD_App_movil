package com.mobile.massiveapp.ui.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.ui.view.util.diffutil.RutaComercialDetalleAdminDiffUtil

class RutaComercialAdminAdapter(
    private var dataSet: List<DoRutaComercialDetalleView>,
    private val onItemClick: (DoRutaComercialDetalleView) -> Unit
) : RecyclerView.Adapter<RutaComercialAdminAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvAdminDocLine: TextView
        val txvAdminCardName: TextView
        val txvAdminStreet: TextView
        val txvAdminMigrado: TextView
        val clAdminRutaStatus: ConstraintLayout
        val imvAdminRutaStatus: ImageView
        val imvGoogleMaps: ImageView
        val txvAdminRutaComentario: TextView
        val txvHoraVerRuta: TextView
        val txvFechaVerRuta: TextView
        val clFechaVerRuta: ConstraintLayout

        init {
            txvAdminDocLine    = view.findViewById(R.id.txvAdminDocLine)
            txvAdminCardName   = view.findViewById(R.id.txvAdminCardName)
            txvAdminStreet     = view.findViewById(R.id.txvAdminStreet)
            txvAdminMigrado    = view.findViewById(R.id.txvAdminMigrado)
            clAdminRutaStatus  = view.findViewById(R.id.clAdminRutaStatus)
            imvAdminRutaStatus = view.findViewById(R.id.imvAdminRutaStatus)
            imvGoogleMaps = view.findViewById(R.id.imvGoogleMaps)
            txvAdminRutaComentario = view.findViewById(R.id.txvAdminRutaComentario)
            txvHoraVerRuta = view.findViewById(R.id.txvRutaVerHoraValue)
            txvFechaVerRuta = view.findViewById(R.id.txvRutaVerFechaValue)
            clFechaVerRuta = view.findViewById(R.id.clRutaVerFecha)
        }

        fun render(item: DoRutaComercialDetalleView, onItemClick: (DoRutaComercialDetalleView) -> Unit) {
            txvAdminDocLine.text  = (item.LineNum + 1).toString()
            txvAdminCardName.text = item.CardName
            txvAdminStreet.text   = item.Street.ifEmpty { item.Address }
            imvGoogleMaps.isVisible = item.Latitud.isNotEmpty()
            txvAdminRutaComentario.isVisible = item.Comments.isNotEmpty()

            val colorIcono = ContextCompat.getColor(itemView.context, R.color.color_white)
            imvAdminRutaStatus.setColorFilter(colorIcono, PorterDuff.Mode.SRC_IN)

            val migrado = item.AccMigrated == "Y"
            val aceptado = item.Status == "A"

            txvAdminMigrado.text = if (migrado) "S" else "N"
            var color = ContextCompat.getColor(itemView.context, R.color.color_green_dark)

            if (migrado && aceptado) {
                imvAdminRutaStatus.setImageResource(R.drawable.icon_confirmed)

                txvFechaVerRuta.text = item.AccCreateDate
                txvHoraVerRuta.text = item.AccCreateHour.take(5)
                clFechaVerRuta.isVisible = true
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

    fun updateData(newDataSet: List<DoRutaComercialDetalleView>) {
        val diffUtil = RutaComercialDetalleAdminDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}

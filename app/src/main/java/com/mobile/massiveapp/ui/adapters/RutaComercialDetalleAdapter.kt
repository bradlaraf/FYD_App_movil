package com.mobile.massiveapp.ui.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.MotionEvent
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
import com.mobile.massiveapp.ui.view.util.diffutil.RutaComercialDetalleDiffUtil
import java.util.Collections

class RutaComercialDetalleAdapter(
    private var dataSet: List<DoRutaComercialDetalleView>,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit,
    private val onItemClick: ((DoRutaComercialDetalleView) -> Unit)? = null
) : RecyclerView.Adapter<RutaComercialDetalleAdapter.ViewHolder>() {

    val currentList: List<DoRutaComercialDetalleView> get() = dataSet

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvDetalleDocLine: TextView
        val txvDetalleCardName: TextView
        val txvDetalleStreet: TextView
        val imgDragHandle: ImageView
        val clDetalleRutaStatus: ConstraintLayout
        val imvDetalleRutaStatus: ImageView
        val txvDetalleCancelado: TextView
        val txvHoraEditar: TextView
        val txvFechaEditar: TextView
        val clFechaEditar: ConstraintLayout


        init {
            txvDetalleDocLine    = view.findViewById(R.id.txvDetalleDocLine)
            txvDetalleCardName   = view.findViewById(R.id.txvDetalleCardName)
            txvDetalleStreet     = view.findViewById(R.id.txvDetalleStreet)
            imgDragHandle        = view.findViewById(R.id.imgDragHandle)
            clDetalleRutaStatus  = view.findViewById(R.id.clDetalleRutaStatus)
            imvDetalleRutaStatus = view.findViewById(R.id.imvDetalleRutaStatus)
            txvDetalleCancelado  = view.findViewById(R.id.txvDetalleCancelado)
            txvHoraEditar = view.findViewById(R.id.txvEditarHoraValue)
            txvFechaEditar = view.findViewById(R.id.txvEditarFechaValue)
            clFechaEditar = view.findViewById(R.id.clEditarFecha)
        }

        fun render(
            item: DoRutaComercialDetalleView,
            onStartDrag: (RecyclerView.ViewHolder) -> Unit,
            onItemClick: ((DoRutaComercialDetalleView) -> Unit)?
        ) {
            txvDetalleDocLine.text = (item.LineNum + 1).toString()
            txvDetalleCardName.text = item.CardName
            txvDetalleStreet.text = item.Street.ifEmpty { item.Address }

            val colorIcono = ContextCompat.getColor(itemView.context, R.color.color_white)
            imvDetalleRutaStatus.setColorFilter(colorIcono, PorterDuff.Mode.SRC_IN)

            val migrado = item.AccMigrated == "Y"
            val aceptado = item.Status == "A"

            val color: Int
            if (migrado && aceptado) {
                imvDetalleRutaStatus.setImageResource(R.drawable.icon_confirmed)
                color = ContextCompat.getColor(itemView.context, R.color.color_green_dark)

                txvFechaEditar.text = item.AccCreateDate
                txvHoraEditar.text = item.AccCreateHour.take(5)
                clFechaEditar.isVisible = true
            } else {
                imvDetalleRutaStatus.setImageResource(R.drawable.icon_pending)
                color = ContextCompat.getColor(itemView.context, R.color.color_red)
            }
            clDetalleRutaStatus.setBackgroundColor(color)

            txvDetalleCancelado.isVisible = item.Status == "R"

            if (item.Status == "P"){

                imgDragHandle.setOnTouchListener { _, event ->
                    if (event.actionMasked == MotionEvent.ACTION_DOWN) onStartDrag(this)
                    false
                }
            }

            onItemClick?.let { listener ->
                itemView.setOnClickListener { listener(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ruta_comercial_detalle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(dataSet[position], onStartDrag, onItemClick)
    }

    override fun getItemCount() = dataSet.size

    fun onItemMove(from: Int, to: Int) {
        val mutableList = dataSet.toMutableList()
        Collections.swap(mutableList, from, to)
        dataSet = mutableList
        notifyItemMoved(from, to)
    }

    fun removeItem(position: Int): DoRutaComercialDetalleView {
        val mutableList = dataSet.toMutableList()
        val removed = mutableList.removeAt(position)
        dataSet = mutableList
        notifyItemRemoved(position)
        return removed
    }

    fun updateData(newDataSet: List<DoRutaComercialDetalleView>) {
        val diffUtil = RutaComercialDetalleDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}

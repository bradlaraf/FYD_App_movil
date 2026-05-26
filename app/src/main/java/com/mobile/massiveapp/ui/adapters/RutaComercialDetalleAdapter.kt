package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        init {
            txvDetalleDocLine = view.findViewById(R.id.txvDetalleDocLine)
            txvDetalleCardName = view.findViewById(R.id.txvDetalleCardName)
            txvDetalleStreet = view.findViewById(R.id.txvDetalleStreet)
            imgDragHandle = view.findViewById(R.id.imgDragHandle)
        }

        fun render(
            item: DoRutaComercialDetalleView,
            onStartDrag: (RecyclerView.ViewHolder) -> Unit,
            onItemClick: ((DoRutaComercialDetalleView) -> Unit)?
        ) {
            txvDetalleDocLine.text = (item.LineNum + 1).toString()
            txvDetalleCardName.text = item.CardName
            txvDetalleStreet.text = item.Street.ifEmpty { item.Address }

            imgDragHandle.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) onStartDrag(this)
                false
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

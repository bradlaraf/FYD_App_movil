package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import com.mobile.massiveapp.ui.view.util.diffutil.DireccionesDiffUtil

class SocioDireccionesAdapter(
    private var dataSet: List<DoSocioDirecciones>,
    private val onClickListener: (DoSocioDirecciones) -> Unit
): RecyclerView.Adapter<SocioDireccionesAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvTitle: TextView
        val txvOpcion: TextView
        val imvArrow: ImageView
        val clContainer: ConstraintLayout

        init {

            txvTitle = view.findViewById(R.id.txvTitle)
            txvOpcion = view.findViewById(R.id.txvValue)
            imvArrow = view.findViewById(R.id.imvArrow)
            clContainer = view.findViewById(R.id.clContainer)
        }

        fun render(direccion: DoSocioDirecciones ,onClickListener: (DoSocioDirecciones) -> Unit){
            clContainer.setOnClickListener { onClickListener(direccion) }
        }
    }

    fun updateList(newDataSet: List<DoSocioDirecciones>){
        val DireccionesDiff = DireccionesDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(DireccionesDiff)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).
        inflate(R.layout.item_inventario_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val direccion = dataSet[position]
        holder.txvTitle.text = "${direccion.Address}"
        holder.txvOpcion.text = "${direccion.County} - ${direccion.City} - ${direccion.Street}"
        holder.imvArrow.setImageResource(R.drawable.icon_arroy_enter)
        holder.render(direccion, onClickListener)
    }

    override fun getItemCount() = dataSet.size
}
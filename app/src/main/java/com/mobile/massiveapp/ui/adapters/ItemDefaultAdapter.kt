package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoItemDefaultView

class ItemDefaultAdapter (
    private var dataSet: MutableList<DoItemDefaultView>,
    private val onClickListener:(DoItemDefaultView, Int) -> Unit
): RecyclerView.Adapter<ItemDefaultAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvTitulo: TextView
        val txvValor: TextView
        val imvArrow: ImageView

        init {
            txvTitulo = view.findViewById(R.id.txvItemDefaultTitle)
            txvValor = view.findViewById(R.id.txvItemDefaultValue)
            imvArrow = view.findViewById(R.id.item_default_arrow)
        }

        fun render(itemDefault: DoItemDefaultView, onClickListener: (DoItemDefaultView, Int) -> Unit){
            txvTitulo.text = itemDefault.titulo
            txvValor.text = itemDefault.valor
            imvArrow.isVisible = itemDefault.arrow

            itemView.setOnClickListener { onClickListener(itemDefault, layoutPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_default_selectable, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPago = dataSet[position]
        viewHolder.render(currentPago, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: MutableList<DoItemDefaultView>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, newText: String) {
        if (position in 0 until itemCount) {
            dataSet[position].valor = newText
            notifyItemChanged(position)
        }
    }
}
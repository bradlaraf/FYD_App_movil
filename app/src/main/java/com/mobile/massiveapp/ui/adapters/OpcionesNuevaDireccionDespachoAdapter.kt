package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R

class OpcionesNuevaDireccionDespachoAdapter(
    private val dataList: HashMap<String, String>,
    private val onClickListener: (String) -> Unit
): RecyclerView.Adapter<OpcionesNuevaDireccionDespachoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvTitle: TextView
        val txvOpcion: TextView
        val imvArrow: ImageView

        init {
            txvTitle = view.findViewById(R.id.txvTitle)
            txvOpcion = view.findViewById(R.id.txvValue)
            imvArrow = view.findViewById(R.id.imvArrow)
        }

        fun render(nombre: String, onClickListener: (String) -> Unit){
            itemView.setOnClickListener { onClickListener(nombre) }
        }
    }

    fun updateOption(key: String, value: String) {
        dataList[key] = value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).
        inflate(R.layout.item_inventario_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val nombre = dataList.keys.elementAt(position)
        holder.txvTitle.text = nombre
        holder.txvOpcion.text = dataList.get(nombre)
        holder.imvArrow.setImageResource(R.drawable.icon_arroy_enter)
        holder.render(nombre, onClickListener)
    }

    override fun getItemCount() = dataList.size
}


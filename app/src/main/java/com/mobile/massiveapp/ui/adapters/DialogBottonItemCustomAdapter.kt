package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R

class DialogBottonItemCustomAdapter (
    private var dataSet: List<HashMap<String, Pair<Int, String>>>,
    private val onClickListener:(HashMap<String, Pair<Int, String>>) -> Unit
): RecyclerView.Adapter<DialogBottonItemCustomAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvTitle: TextView
        val txvValue: TextView
        val imvIcon: ImageView

        init {
            txvTitle = view.findViewById(R.id.txvTitleBottomSheet)
            txvValue = view.findViewById(R.id.txvValueBottomSheet)
            imvIcon = view.findViewById(R.id.imvIconDescription)
        }

        fun render(currentItem: HashMap<String, Pair<Int, String>>, onClickListener: (HashMap<String, Pair<Int, String>>) -> Unit){
            itemView.setOnClickListener { onClickListener(currentItem) }
            imvIcon.isVisible = true
            for ((clave, valor) in currentItem){
                val (imagen, descripcion) = valor
                txvTitle.text = clave
                txvValue.text = descripcion
                imvIcon.setImageResource(imagen)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_title_value, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        viewHolder.render(currentItem, onClickListener)
    }


    override fun getItemCount() = dataSet.size

}
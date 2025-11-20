package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R

class InfoUsuarioAdapter (
    private var dataSet: List<HashMap<String, String>>,
    private val onClickListener:(HashMap<String, String>) -> Unit
): RecyclerView.Adapter<InfoUsuarioAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvTitle: TextView
        val txvValue: TextView

        init {
            txvTitle = view.findViewById(R.id.txvUsuarioInfoTitle)
            txvValue = view.findViewById(R.id.txvUsuarioInfoValue)
        }

        fun render(currentItem: HashMap<String, String>, onClickListener: (HashMap<String, String>) -> Unit){
            itemView.setOnClickListener { onClickListener(currentItem) }
            txvTitle.text = currentItem.keys.first()
            txvValue.text = currentItem[currentItem.keys.first()]
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_info_usuario, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        viewHolder.render(currentItem, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<HashMap<String, String>>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}
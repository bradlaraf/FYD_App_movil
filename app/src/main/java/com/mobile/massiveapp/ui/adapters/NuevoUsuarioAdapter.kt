package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem

class NuevoUsuarioAdapter  (
    private var dataSet: List<DoNuevoUsuarioItem>,
    private val onClickListener:(DoNuevoUsuarioItem) -> Unit
): RecyclerView.Adapter<NuevoUsuarioAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvTitle: TextView
        val txvValue: TextView
        val cbCheck: CheckBox

        init {
            txvTitle = view.findViewById(R.id.txvNuevoUsuarioTitle)
            txvValue = view.findViewById(R.id.txvNuevoUsuarioValue)
            cbCheck = view.findViewById(R.id.cbCheck)
        }

        fun render(currentItem: DoNuevoUsuarioItem, onClickListener: (DoNuevoUsuarioItem) -> Unit){
            itemView.setOnClickListener { onClickListener(currentItem) }
            itemView.setOnClickListener {
                cbCheck.isChecked = !cbCheck.isChecked
                currentItem.Checked = cbCheck.isChecked
            }

            cbCheck.setOnClickListener {
                currentItem.Checked = cbCheck.isChecked
            }

            cbCheck.isChecked = currentItem.Checked
            txvTitle.text = currentItem.Name
            txvValue.text = currentItem.Code

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nuevo_usuario, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        viewHolder.render(currentItem, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun getAllData():List<DoNuevoUsuarioItem> {
        return dataSet
    }

    fun selectAll(select: Boolean){
        dataSet.forEach{
            it.Checked = select
        }
        notifyDataSetChanged()
    }

    fun updateData(newDataSet: List<DoNuevoUsuarioItem>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}
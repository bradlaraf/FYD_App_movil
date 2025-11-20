package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.ui.view.menu.model.MenuItem

class MenuMainAdapter(private val dataSet: List<MenuItem>, private val onClickListener:(MenuItem) -> Unit) :
    RecyclerView.Adapter<MenuMainAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvOptionName: TextView
        val imvIcon: ImageView
        val cvMenuOption: LinearLayout

        init {

            txvOptionName = view.findViewById(R.id.txvOptionName)
            imvIcon = view.findViewById(R.id.imvIcon)
            cvMenuOption = view.findViewById(R.id.cvMenuOption)
        }

        fun render(menuItem: MenuItem, onClickListener: (MenuItem) -> Unit){
            cvMenuOption.setOnClickListener { onClickListener(menuItem) }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_main_menu, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.txvOptionName.text = dataSet[position].nombre
        viewHolder.imvIcon.setImageResource(dataSet[position].icono)
        viewHolder.render(dataSet[position], onClickListener)
    }


    override fun getItemCount() = dataSet.size

}
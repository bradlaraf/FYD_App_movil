package com.mobile.massiveapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoArticuloInventario
import com.mobile.massiveapp.ui.view.util.diffutil.ArticuloInventarioDiffUtil

class ArticuloAdapter (
    private var dataSet: List<DoArticuloInventario>,
    private val onClickListener:(DoArticuloInventario) -> Unit

): RecyclerView.Adapter<ArticuloAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvItemCode: TextView
        val txvOnHand: TextView
        val clItemArticulo: ConstraintLayout
        val stock: TextView
        val grupoUM: TextView

        init {
            txvItemCode = view.findViewById(R.id.txvItemCode)
            txvOnHand = view.findViewById(R.id.txvOnHand)
            clItemArticulo = view.findViewById(R.id.clItemArticulo)
            stock = view.findViewById(R.id.txvInventarioStock)
            grupoUM = view.findViewById(R.id.txvGrupoUMArticulo)
        }

        fun render(articulo: DoArticuloInventario ,onClickListener: (DoArticuloInventario) -> Unit){
            itemView.setOnClickListener {
                onClickListener(articulo)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_articulo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentArticulo = dataSet[position]
        viewHolder.txvItemCode.text = dataSet[position].ItemCode
        viewHolder.txvOnHand.text = dataSet[position].ItemName
        viewHolder.stock.text = dataSet[position].OnHand.toString()
        viewHolder.grupoUM.text = dataSet[position].GrupoArticulo

        if (currentArticulo.OnHand <= 0){
            viewHolder.clItemArticulo.setBackgroundColor(Color.parseColor("#1AFF3131"))
        } else {
            viewHolder.clItemArticulo.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        viewHolder.render(currentArticulo, onClickListener)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoArticuloInventario>){
        val articuloDiffUtil = ArticuloInventarioDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(articuloDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateDataForSearching(newDataSet: List<DoArticuloInventario>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}

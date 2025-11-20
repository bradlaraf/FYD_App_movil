package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoArticuloPrecios
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.diffutil.ArticuloPreciosDiffUtil

class ArticuloPrecioAdapter (
    private var dataSet: List<DoArticuloPrecios>,
    private val onClickListener:(DoArticuloPrecios) -> Unit
): RecyclerView.Adapter<ArticuloPrecioAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvListaPrecios: TextView
        val txvPrecio: TextView

        init {
            txvListaPrecios = view.findViewById(R.id.txvArticuloInfoListaPrecioValue)
            txvPrecio = view.findViewById(R.id.txvArticuloInfoPrecioValue)

        }

        fun render(articuloPrecio: DoArticuloPrecios, onClickListener: (DoArticuloPrecios) -> Unit){
            itemView.setOnClickListener { onClickListener(articuloPrecio) }

            txvPrecio.text = "${SendData.instance.simboloMoneda}${articuloPrecio.Price}"
            txvListaPrecios.text = articuloPrecio.ListName
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_precios_articulo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currectPedido = dataSet[position]
        viewHolder.render(currectPedido, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoArticuloPrecios>){
        val articuloPreciosDiffUtil = ArticuloPreciosDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(articuloPreciosDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
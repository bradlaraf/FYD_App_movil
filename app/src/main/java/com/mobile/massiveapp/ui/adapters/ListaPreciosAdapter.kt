package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoArticuloListaPrecios

class ListaPreciosAdapter(private val dataSet: List<DoArticuloListaPrecios>):
    RecyclerView.Adapter<ListaPreciosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val txvListaDePrecios: TextView
        val txvPrecio: TextView

        init {
            txvListaDePrecios = view.findViewById(R.id.txvTipoListaPrecioInventario)
            txvPrecio = view.findViewById(R.id.txvPrecioListaPrecioInventario)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).
        inflate(R.layout.item_lista_precio_inventario, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {
        viewholder.txvListaDePrecios.text = dataSet[position].ListName
        viewholder.txvPrecio.text = dataSet[position].ListNum.toString()
    }
}
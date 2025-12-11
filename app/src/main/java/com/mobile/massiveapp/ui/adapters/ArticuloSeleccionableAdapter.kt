package com.mobile.massiveapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoArticuloInventario
import com.mobile.massiveapp.ui.view.util.diffutil.ArticuloInvDiffUtil

class ArticuloSeleccionableAdapter(
private var dataSet: List<DoArticuloInventario>,
private val onClickListener:(DoArticuloInventario) -> Unit
): RecyclerView.Adapter<ArticuloSeleccionableAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvItemCode: TextView
        val txvDescripcion: TextView
        val clItemArticulo: ConstraintLayout
        val txvCantidad1: TextView
        val txvCantidad2: TextView

        init {
            txvItemCode = view.findViewById(R.id.txvItemCode)
            txvDescripcion = view.findViewById(R.id.txvArticuloDescripcion)
            clItemArticulo = view.findViewById(R.id.clArticuloPedidoFyd)
            txvCantidad1 = view.findViewById(R.id.txvCantidad1)
            txvCantidad2 = view.findViewById(R.id.txvCantidad2)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_articulo_pedido_fyd, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentArticulo =               dataSet[position]
        viewHolder.txvItemCode.text =       dataSet[position].ItemCode
        viewHolder.txvDescripcion.text =    currentArticulo.ItemName
        viewHolder.txvCantidad1.text =      dataSet[position].OnHand.toBigDecimal().toPlainString().format(2)
        viewHolder.txvCantidad2.text =      dataSet[position].OnHand.toBigDecimal().toPlainString().format(2)

        viewHolder.clItemArticulo.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = viewHolder.adapterPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            render(currentArticulo, onClickListener)
        }


        if (selectedPosition ==viewHolder.adapterPosition){
            viewHolder.clItemArticulo.setBackgroundColor(Color.parseColor("#85D8FA"))
        } else {
            if (currentArticulo.OnHand <= 0.0){
                viewHolder.clItemArticulo.setBackgroundColor(Color.parseColor("#1AFF3131"))
            } else {
                viewHolder.clItemArticulo.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }

        }

    }
    fun render(articulo: DoArticuloInventario, onClickListener: (DoArticuloInventario) -> Unit){
        onClickListener(articulo)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoArticuloInventario>){
        val articulosDiffutil = ArticuloInvDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(articulosDiffutil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateDataForSearching(newDataSet: List<DoArticuloInventario>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}
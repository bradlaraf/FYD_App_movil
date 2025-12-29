package com.mobile.massiveapp.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoArticuloInv
import com.mobile.massiveapp.ui.view.util.diffutil.ArticuloSeleccionableDiffUtil

class ArticulosSeleccionablesAdapter(
    private var dataSet: List<DoArticuloInv>,
    private val onClickListener:(DoArticuloInv) -> Unit
): RecyclerView.Adapter<ArticulosSeleccionablesAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imvArticulo: ImageView
        val txvItemCode: TextView
        val txvDescripcion: TextView
        val clItemArticulo: ConstraintLayout
        val txvCantidad1: TextView
        val txvCantidad2: TextView
        val txvAlmacen1: TextView
        val txvAlmacen2: TextView

        init {
            imvArticulo = view.findViewById(R.id.imvArticuloSelect)
            txvItemCode = view.findViewById(R.id.txvItemCode)
            txvDescripcion = view.findViewById(R.id.txvArticuloDescripcion)
            clItemArticulo = view.findViewById(R.id.clArticuloPedidoFyd)
            txvCantidad1 = view.findViewById(R.id.txvCantidad1)
            txvCantidad2 = view.findViewById(R.id.txvCantidad2)
            txvAlmacen1 = view.findViewById(R.id.txvAlmacen1)
            txvAlmacen2 = view.findViewById(R.id.txvAlmacen2)
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
        viewHolder.txvCantidad1.text =      dataSet[position].OnHand1.toBigDecimal().toPlainString().format(2)
        viewHolder.txvCantidad2.text =      dataSet[position].OnHand2.toBigDecimal().toPlainString().format(2)
        viewHolder.txvAlmacen1.text =       currentArticulo.WhsName1
        viewHolder.txvAlmacen2.text =       currentArticulo.WhsName2

        viewHolder.clItemArticulo.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = viewHolder.adapterPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            render(currentArticulo, onClickListener)
        }

        if (currentArticulo.ItemCode.startsWith("B")){

            viewHolder.imvArticulo.imageTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(viewHolder.itemView.context, R.color.color_red_bonificacion)
                )

        }

        if (selectedPosition ==viewHolder.adapterPosition){
            viewHolder.clItemArticulo.setBackgroundColor(Color.parseColor("#85D8FA"))
        }


    }
    fun render(articulo: DoArticuloInv, onClickListener: (DoArticuloInv) -> Unit){
        onClickListener(articulo)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoArticuloInv>){
        val articulosDiffutil = ArticuloSeleccionableDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(articulosDiffutil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateDataForSearching(newDataSet: List<DoArticuloInv>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}
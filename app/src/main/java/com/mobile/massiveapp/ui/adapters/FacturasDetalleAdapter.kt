package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoClienteFacturaDetalle
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.diffutil.FacturasDetalleDiffUtil

class FacturasDetalleAdapter (
    private var dataSet: List<DoClienteFacturaDetalle>,
    private val onClickListener:(DoClienteFacturaDetalle) -> Unit
): RecyclerView.Adapter<FacturasDetalleAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNombre: TextView
        val txvCantidad: TextView
        val txvPrecioBruto: TextView
        val txvItemTotal: TextView
        val imvArrow: ImageView

        init {
            txvNombre = view.findViewById(R.id.txvPedidoInfoNombreItem)
            txvCantidad = view.findViewById(R.id.txvPedidoInfoCantidad)
            txvPrecioBruto = view.findViewById(R.id.txvPedidoInfoPrecioBruto)
            txvItemTotal = view.findViewById(R.id.txvPedidoInfoItemTotal)
            imvArrow = view.findViewById(R.id.imvArrowPedidoDetalle)

        }

        fun render(facturaDetalleInfo: DoClienteFacturaDetalle, onClickListener: (DoClienteFacturaDetalle) -> Unit){
            imvArrow.isVisible = false
            itemView.setOnClickListener { onClickListener(facturaDetalleInfo) }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido_cliente_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentFacturaDetalle = dataSet[position]
        viewHolder.txvNombre.text = dataSet[position].Dscription
        viewHolder.txvCantidad.text = "Cantidad: ${currentFacturaDetalle.Quantity} / Precio: ${currentFacturaDetalle.Price}"
        viewHolder.txvItemTotal.text = "${SendData.instance.simboloMoneda} ${dataSet[position].LineTotal}"
        viewHolder.render(currentFacturaDetalle, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoClienteFacturaDetalle>){
        val pedidoDetalleItemsDiffUtil = FacturasDetalleDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pedidoDetalleItemsDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
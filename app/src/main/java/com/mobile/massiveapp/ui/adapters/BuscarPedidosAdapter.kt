package com.mobile.massiveapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoClientePedido
import com.mobile.massiveapp.ui.view.util.diffutil.PedidoClienteItemsDiffUtil

class BuscarPedidosAdapter (
    private var dataSet: List<DoClientePedido>,
    private val onClickListener:(DoClientePedido) -> Unit
): RecyclerView.Adapter<BuscarPedidosAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvClienteNombre: TextView
        val txvTotal: TextView
        val txvFecha: TextView
        val txvIndicador: TextView
        val imvMigrado: ImageView

        init {
            txvClienteNombre = view.findViewById(R.id.txvPedidoNombreCliente)
            txvTotal = view.findViewById(R.id.txvPedidoTotal)
            txvFecha = view.findViewById(R.id.txvPedidoFecha)
            txvIndicador = view.findViewById(R.id.txvPedidoIndicator)
            imvMigrado = view.findViewById(R.id.imvPagoMigrado)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido_cliente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currectPedido = dataSet[position]
        viewHolder.txvClienteNombre.text = dataSet[position].CardName
        viewHolder.txvTotal.text = dataSet[position].DocTotal.toString()
        viewHolder.txvFecha.text = dataSet[position].DocDate
        val indicador = if (currectPedido.Indicator == "03") "BOLETA" else if (currectPedido.Indicator =="01") "FACTURA" else "OTRO"
        viewHolder.txvIndicador.text = indicador

        viewHolder.itemView.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = viewHolder.adapterPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            render(currectPedido, onClickListener)
        }

        if (selectedPosition ==viewHolder.adapterPosition){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#85D8FA"))
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

    }
    fun render(pedidos: DoClientePedido, onClickListener: (DoClientePedido) -> Unit){
        onClickListener(pedidos)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoClientePedido>){
        val pedidoClienteDiffUtil = PedidoClienteItemsDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pedidoClienteDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
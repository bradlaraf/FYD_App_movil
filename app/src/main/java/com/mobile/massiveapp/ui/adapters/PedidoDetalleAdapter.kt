package com.mobile.massiveapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.diffutil.PedidoDetalleItemsDiffUtil
import com.mobile.massiveapp.ui.view.util.format
import org.w3c.dom.Text

class PedidoDetalleAdapter(
    private var dataSet: List<ClientePedidoDetalle>,
    private val onClickListener:(ClientePedidoDetalle) -> Unit
): RecyclerView.Adapter<PedidoDetalleAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNombre: TextView
        val txvCantidad: TextView
        val txvPrecioBruto: TextView
        val txvItemTotal: TextView
        val txvUnidadMedida: TextView
        val txvLineNum: TextView
        val txvNuevoLanzamiento: TextView

        init {
            txvNombre = view.findViewById(R.id.txvPedidoInfoNombreItem)
            txvCantidad = view.findViewById(R.id.txvPedidoInfoCantidad)
            txvPrecioBruto = view.findViewById(R.id.txvPedidoInfoPrecioBruto)
            txvItemTotal = view.findViewById(R.id.txvPedidoInfoItemTotal)
            txvUnidadMedida = view.findViewById(R.id.txvUnidadMedida)
            txvLineNum = view.findViewById(R.id.txvLineNum)
            txvNuevoLanzamiento = view.findViewById(R.id.txvNuevoLanzamiento)
        }

        fun render(articuloinfo: ClientePedidoDetalle, onClickListener: (ClientePedidoDetalle) -> Unit){
            itemView.setOnClickListener { onClickListener(articuloinfo) }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido_cliente_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentArticulo = dataSet[position]
        viewHolder.txvNombre.text = dataSet[position].Dscription
        viewHolder.txvCantidad.text = "Cantidad: ${currentArticulo.Quantity} / Precio: ${currentArticulo.Price}"
        viewHolder.txvItemTotal.text = "${SendData.instance.simboloMoneda} ${dataSet[position].LineTotal.format(2)}"
        viewHolder.txvUnidadMedida.text = currentArticulo.UnitMsr
        viewHolder.txvLineNum.text = (currentArticulo.LineNum + 1).toString()
        viewHolder.txvNuevoLanzamiento.isVisible = currentArticulo.OcrCode == "Y"
        viewHolder.render(currentArticulo, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<ClientePedidoDetalle>){
        val pedidoDetalleItemsDiffUtil = PedidoDetalleItemsDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pedidoDetalleItemsDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateDataEditMode(newDataSet: List<ClientePedidoDetalle>){
        val nuevaListaDetalles = newDataSet.map { detalle ->
            detalle.copy(LineNum = detalle.LineNum - 1000)
        }
        val pedidoDetalleItemsDiffUtil = PedidoDetalleItemsDiffUtil(dataSet, nuevaListaDetalles)
        val diffResult = DiffUtil.calculateDiff(pedidoDetalleItemsDiffUtil)
        dataSet = nuevaListaDetalles
        diffResult.dispatchUpdatesTo(this)
    }



    @SuppressLint("NotifyDataSetChanged")
    fun update(newDataSet: List<ClientePedidoDetalle>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}
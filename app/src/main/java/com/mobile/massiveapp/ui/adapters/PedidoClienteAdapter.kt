package com.mobile.massiveapp.ui.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoClientePedido
import com.mobile.massiveapp.ui.view.util.diffutil.PedidoClienteItemsDiffUtil

class PedidoClienteAdapter(
    private var dataSet: List<DoClientePedido>,
    private val onClickListener:(DoClientePedido) -> Unit
): RecyclerView.Adapter<PedidoClienteAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvClienteNombre: TextView
        val txvTotal: TextView
        val txvFecha: TextView
        val txvIndicador: TextView
        val imvMigrado: ImageView
        val txvCancelado: TextView
        val txvNumAtCard: TextView
        val clMailChecked: ConstraintLayout
        val clItemPedido: ConstraintLayout

        init {
            txvClienteNombre = view.findViewById(R.id.txvPedidoNombreCliente)
            txvTotal = view.findViewById(R.id.txvPedidoTotal)
            txvFecha = view.findViewById(R.id.txvPedidoFecha)
            txvIndicador = view.findViewById(R.id.txvPedidoIndicator)
            imvMigrado = view.findViewById(R.id.imvPagoMigrado)
            txvCancelado = view.findViewById(R.id.txvPedidoCancelado)
            txvNumAtCard = view.findViewById(R.id.txvPedidoNumAtCard)
            clMailChecked = view.findViewById(R.id.clMailChecked)
            clItemPedido = view.findViewById(R.id.clPediddoClienteItem)
        }

        fun render(clientePedido: DoClientePedido, onClickListener: (DoClientePedido) -> Unit){
            clItemPedido.setOnClickListener { onClickListener(clientePedido) }

            if (clientePedido.CANCELED == "Y"){
                txvCancelado.isVisible = true
            }



            if (clientePedido.AccNotificado == "Y"){
                clMailChecked.setBackgroundColor(Color.parseColor("#CEE84242"))
            } else {
                clMailChecked.setBackgroundColor(Color.parseColor("#D574BB64"))
            }




            val migrado = clientePedido.AccMigrated == "Y"
            val finalizado = clientePedido.AccFinalized == "Y"
            val docCerrado = clientePedido.DocStatus == "C"
            val notificado = clientePedido.AccNotificado == "Y"
            /*val ordenDeVenta = clientePedido.ObjType == 17*/
            val tieneNumAtCard = clientePedido.NumAtCard.trim().isNotEmpty()

            when {
                clientePedido.AccMigrated == "N" -> {
                    imvMigrado.setImageResource(R.drawable.icon_cloud_await)
                }

                !finalizado && migrado -> {
                    imvMigrado.setImageResource(R.drawable.icon_cloud_intermedia)
                }

                migrado && finalizado && !docCerrado -> {
                    imvMigrado.setImageResource(R.drawable.icon_cloud_done)
                }

                migrado && finalizado && docCerrado -> {
                    imvMigrado.setImageResource(R.drawable.icon_documento_procesado)

                    if (notificado) {
                        val color = ContextCompat.getColor(itemView.context, R.color.color_green_dark)
                        imvMigrado.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                    }

                    if (tieneNumAtCard){
                        txvNumAtCard.isVisible = true
                        txvNumAtCard.text = clientePedido.NumAtCard
                    }
                }
            }
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


        viewHolder.render(currectPedido, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoClientePedido>){
        val pedidoClienteDiffUtil = PedidoClienteItemsDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pedidoClienteDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
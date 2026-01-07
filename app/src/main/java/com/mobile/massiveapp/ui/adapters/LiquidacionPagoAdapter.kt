package com.mobile.massiveapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoLiquidacionPagoView
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.diffutil.LiquidacionPagoDiffUtil

class LiquidacionPagoAdapter(
    private var dataSet: List<DoLiquidacionPagoView>,
    private val onClickListener:(DoLiquidacionPagoView) -> Unit
): RecyclerView.Adapter<LiquidacionPagoAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNombre: TextView
        val txvFecha: TextView
        val txvPago: TextView
        val txvMontoRestante: TextView
        val txvDocLine: TextView

        init {
            txvNombre = view.findViewById(R.id.txvPagoDetalleNombre)
            txvFecha = view.findViewById(R.id.txvPagoDetalleFecha)
            txvPago = view.findViewById(R.id.txvPagoDetallePago)
            txvMontoRestante = view.findViewById(R.id.txvPagoDetalleMontoRestante)
            txvDocLine = view.findViewById(R.id.txvPagoDetalleDocLine)
        }

        @SuppressLint("SetTextI18n")
        fun render(clientePago: DoLiquidacionPagoView, onClickListener: (DoLiquidacionPagoView) -> Unit){
            txvNombre.text = clientePago.SUNAT
            txvPago.text = "${SendData.instance.simboloMoneda}${clientePago.Monto}"
            txvFecha.text = clientePago.FechaCreacion
            txvMontoRestante.text = "${clientePago.Saldo}"
            txvDocLine.text = "${clientePago.DocLine+1}"
            itemView.setOnClickListener { onClickListener(clientePago) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pago_detalle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPago = dataSet[position]
        viewHolder.render(currentPago, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoLiquidacionPagoView>){
        val pagoDetalleItemsDiffUtil = LiquidacionPagoDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pagoDetalleItemsDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}
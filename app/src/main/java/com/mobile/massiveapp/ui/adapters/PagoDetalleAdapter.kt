package com.mobile.massiveapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoPagoDetalle
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.diffutil.PagoDetalleDiffUtil

class PagoDetalleAdapter(
    private var dataSet: List<DoPagoDetalle>,
    private val onClickListener:(DoPagoDetalle) -> Unit
): RecyclerView.Adapter<PagoDetalleAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNombre: TextView

        val txvPago: TextView

        val txvDocLine: TextView

        init {
            txvNombre = view.findViewById(R.id.txvPagoDetalleNombre)

            txvPago = view.findViewById(R.id.txvPagoDetallePago)

            txvDocLine = view.findViewById(R.id.txvPagoDetalleDocLine)
        }

        @SuppressLint("SetTextI18n")
        fun render(clientePago: DoPagoDetalle, onClickListener: (DoPagoDetalle) -> Unit){
            txvNombre.text = clientePago.NumeroFactura
            txvPago.text = "Pago: ${SendData.instance.simboloMoneda}${clientePago.SumApplied}"

            txvDocLine.text = "${clientePago.DocLine}"
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

    fun updateData(newDataSet: List<DoPagoDetalle>){
        val pagoDetalleItemsDiffUtil = PagoDetalleDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pagoDetalleItemsDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}
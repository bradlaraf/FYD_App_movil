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
import com.mobile.massiveapp.domain.model.DoClientePago
import com.mobile.massiveapp.ui.view.util.diffutil.PagosCabeceraDiffUtil
import com.mobile.massiveapp.ui.view.util.format

class CobranzasAdapter(
    private var dataSet: List<DoClientePago>,
    private val onClickListener:(DoClientePago) -> Unit
): RecyclerView.Adapter<CobranzasAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvSocioNegocio: TextView
        val txvMonto: TextView
        val txvFecha: TextView
        val txvTipoPago: TextView
        val imvMigrado: ImageView
        val txvCancelado: TextView

        init {
            txvSocioNegocio = view.findViewById(R.id.txvPagoCabeceraNombre)
            txvMonto = view.findViewById(R.id.txvPagoCabeceraMonto)
            txvFecha = view.findViewById(R.id.txvPagoCabeceraFecha)
            txvTipoPago = view.findViewById(R.id.txvPagoCabeceraTipoPago)
            imvMigrado = view.findViewById(R.id.imvPagoCabeceraMigrado)
            txvCancelado = view.findViewById(R.id.txvCobranzaCancelado)
        }

        fun render(clientePago: DoClientePago, onClickListener: (DoClientePago) -> Unit){
            itemView.setOnClickListener { onClickListener(clientePago) }

            if (clientePago.AccMigrated =="N"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_await)
            } else if(clientePago.AccMigrated =="Y" && clientePago.AccFinalized == "N"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_intermedia)
            } else if (clientePago.AccMigrated =="Y" && clientePago.AccFinalized == "Y" && clientePago.TransId == -1){
                imvMigrado.setImageResource(R.drawable.icon_cloud_done)
            } else if (clientePago.AccMigrated =="Y" && clientePago.AccFinalized == "Y" && clientePago.TransId != -1){
                imvMigrado.setImageResource(R.drawable.icon_documento_procesado)
            }

            when (clientePago.TypePayment){
                "E"->{
                    txvMonto.text = "S/${clientePago.CashSum.format(2)}"
                    txvTipoPago.text = "Efectivo"
                }

                "C"->{
                    txvMonto.text = "S/${clientePago.CheckSum.format(2)}"
                    txvTipoPago.text = "Cheque"
                }

                "T"->{
                    txvMonto.text = "S/${clientePago.TrsfrSum.format(2)}"
                    txvTipoPago.text = "Transferencia"
                }

                "D"->{
                    txvMonto.text = "S/${clientePago.TrsfrSum.format(2)}"
                    txvTipoPago.text = "Depósito"
                }
            }

            if (clientePago.Canceled == "Y"){
                txvCancelado.isVisible = true
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cobranzas_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPago = dataSet[position]
        viewHolder.txvSocioNegocio.text = dataSet[position].CardName
        viewHolder.txvFecha.text = dataSet[position].DocDate

        viewHolder.render(currentPago, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoClientePago>){
        val pagoCabeceraItemsDiffUtil = PagosCabeceraDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pagoCabeceraItemsDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
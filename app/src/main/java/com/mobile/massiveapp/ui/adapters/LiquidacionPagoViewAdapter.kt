package com.mobile.massiveapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoLiquidacionPagoView
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.diffutil.LiquidacionPagoDiffUtil


class LiquidacionPagoViewAdapter ( 
    private var dataSet: List<DoLiquidacionPagoView>,
    private val onClickListener:(DoLiquidacionPagoView) -> Unit
): RecyclerView.Adapter<LiquidacionPagoViewAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvMoneda: TextView
        val txvPago: TextView
        val txvTipoPago: TextView
        val txvDocLine: TextView
        val txvNroOperacion: TextView
        val txvSunat: TextView
        val txvFecha: TextView
        val txvMigrado: TextView
        val txvCancelado: TextView
        val cvMainLiquidacionPago: CardView

        init {
            txvMoneda = view.findViewById(R.id.txvPagoDetalleViewMoneda)
            txvPago = view.findViewById(R.id.txvPagoDetalleViewPago)
            txvTipoPago = view.findViewById(R.id.txvPagoDetalleViewNombre)
            txvDocLine = view.findViewById(R.id.txvPagoDetalleViewDocLine)
            txvNroOperacion = view.findViewById(R.id.txvPagoDetalleViewNroOperacion)
            txvSunat = view.findViewById(R.id.txvPagoDetalleViewSunat)
            txvFecha = view.findViewById(R.id.txvPagoDetalleViewFecha)
            txvMigrado = view.findViewById(R.id.txvPagoDetalleViewMigrado)
            txvCancelado = view.findViewById(R.id.txvPagoDetalleViewCancelado)
            cvMainLiquidacionPago = view.findViewById(R.id.cvMainLiquidacionpagoView)
        }

        @SuppressLint("SetTextI18n")
        fun render(clientePago: DoLiquidacionPagoView, onClickListener: (DoLiquidacionPagoView) -> Unit){
            txvTipoPago.text = clientePago.TipoPago
            txvPago.text = "${SendData.instance.simboloMoneda}${clientePago.Monto}"
            txvMoneda.text = clientePago.Moneda
            txvDocLine.text = "#${clientePago.DocLine+1}"
            txvSunat.text = clientePago.SUNAT
            txvFecha.text = clientePago.FechaCreacion
            txvCancelado.isVisible = clientePago.Canceled == "Y"
            itemView.isEnabled = clientePago.Canceled == "N"


            txvNroOperacion.isVisible = clientePago.NroOperacion.isNotEmpty()
            txvNroOperacion.text = "Nro Operacion: ${clientePago.NroOperacion}"

            val migrado = clientePago.AccMigrated == "Y"
            val finalizado = clientePago.AccFinalized == "Y"
            val docSap = clientePago.DocEntry != -1

            when {
                migrado && docSap -> {
                    txvMigrado.text = "S"
                    val color =
                        ContextCompat.getColor(itemView.context, R.color.color_green_dark)
                    txvMigrado.setBackgroundColor(color)
                }

                migrado -> {
                    txvMigrado.text = "I"
                    val color = ContextCompat.getColor(itemView.context, R.color.color_amarillo_sap)
                    txvMigrado.setBackgroundColor(color)
                }

                !migrado -> {
                    txvMigrado.text = "N"
                    val color = ContextCompat.getColor(itemView.context, R.color.color_red)
                    txvMigrado.setBackgroundColor(color)
                }
            }

            cvMainLiquidacionPago.setOnClickListener { onClickListener(clientePago) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pago_detalle_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPago = dataSet[position]
        viewHolder.render(currentPago, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoLiquidacionPagoView>){
        val PagoDetalleViewItemsDiffUtil = LiquidacionPagoDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(PagoDetalleViewItemsDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}
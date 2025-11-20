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
import com.mobile.massiveapp.domain.model.DoClientePago
import com.mobile.massiveapp.ui.view.util.diffutil.PagosCabeceraDiffUtil

class BuscarCobranzaAdapter (
    private var dataSet: List<DoClientePago>,
    private val onClickListener:(DoClientePago) -> Unit
): RecyclerView.Adapter<BuscarCobranzaAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvSocioNegocio: TextView
        val txvMonto: TextView
        val txvFecha: TextView
        val txvMontoRestante: TextView
        val imvMigrado: ImageView

        init {
            txvSocioNegocio = view.findViewById(R.id.txvPagoDetalleNombre)
            txvMonto = view.findViewById(R.id.txvPagoCabeceraMonto)
            txvFecha = view.findViewById(R.id.txvPagoCabeceraFecha)
            txvMontoRestante = view.findViewById(R.id.txvPagoCabeceraTipoPago)
            imvMigrado = view.findViewById(R.id.imvPagoMigrado)
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
        viewHolder.txvMonto.text = dataSet[position].CashSum.toString()
        viewHolder.txvFecha.text = dataSet[position].DocDate

        viewHolder.itemView.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = viewHolder.adapterPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            render(currentPago, onClickListener)
        }

        if (selectedPosition ==viewHolder.adapterPosition){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#85D8FA"))
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

    }
    fun render(pagos: DoClientePago, onClickListener: (DoClientePago) -> Unit){
        onClickListener(pagos)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoClientePago>){
        val pagoCabeceraItemsDiffUtil = PagosCabeceraDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pagoCabeceraItemsDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
package com.mobile.massiveapp.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoClienteFacturas
import com.mobile.massiveapp.ui.view.util.diffutil.FacturasDiffUtil

class BuscarFacturaAdapter (
    private var dataSet: List<DoClienteFacturas>,
    private val onClickListener:(DoClienteFacturas) -> Unit
): RecyclerView.Adapter<BuscarFacturaAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvFolio: TextView
        val txvTotal: TextView
        val txvFecha: TextView
        val txvDeuda: TextView

        init {
            txvFolio = view.findViewById(R.id.txvFacturaItemFolio)
            txvTotal = view.findViewById(R.id.txvFacturaItemMontoTotal)
            txvFecha = view.findViewById(R.id.txvFacturaItemFecha)
            txvDeuda = view.findViewById(R.id.txvFacturaItemMontoAPagar)

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_factura, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentFactura = dataSet[position]
        viewHolder.txvFolio.text = "Comprobante: ${dataSet[position].FolioPref} - ${dataSet[position].FolioNum}"
        viewHolder.txvFecha.text = "Fecha: ${dataSet[position].DocDate}"


        /** Metodo obsoleto para la logica del app de Seidor **/
        /*viewHolder.txvTotal.text = "Total Deuda: \n S/${dataSet[position].DocTotal}"*/

        /*if (currentFactura.Edit_Ptd == -11.0){
            viewHolder.txvDeuda.text = "Saldo Pendiente: \n S/${dataSet[position].PaidToDate}"
        } else {
            viewHolder.txvDeuda.text = "Saldo Pendiente: \n S/${dataSet[position].Edit_Ptd}"
        }*/

        viewHolder.txvDeuda.text = "Saldo Pendiente: \n S/${dataSet[position].PaidToDate}"

        if (currentFactura.PaidToDate > 0.0){
            viewHolder.itemView.setOnClickListener {
                lastSelectedPosition = selectedPosition
                selectedPosition = viewHolder.adapterPosition
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)
                render(currentFactura, onClickListener)
            }
        }

        if (selectedPosition == viewHolder.adapterPosition){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#85D8FA"))
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

    }
    fun render(facturas: DoClienteFacturas, onClickListener: (DoClienteFacturas) -> Unit){
        onClickListener(facturas)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoClienteFacturas>){
        val facturaClienteDiffUtil = FacturasDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(facturaClienteDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
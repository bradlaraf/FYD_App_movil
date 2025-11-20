package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoFacturaView
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.diffutil.FacturasDoDiffUtil
import com.mobile.massiveapp.ui.view.util.folioFormat
import com.mobile.massiveapp.ui.view.util.format

class FacturasAdapter (
    private var dataSet: List<DoFacturaView>,
    private val onClickListener:(DoFacturaView) -> Unit,
    private val onLongPressListener: (View, DoFacturaView) -> Unit

): RecyclerView.Adapter<FacturasAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvCardName: TextView
        val txvFolio: TextView
        val txvMoneda: TextView
        val txvImporte: TextView
        val txvMonedaPtd: TextView
        val txvImportePtd: TextView
        val txvFecha: TextView
        val txvCur: TextView

        init {
            txvCardName = view.findViewById(R.id.txvFacturasCardName)
            txvFolio = view.findViewById(R.id.txvFacturasItemFolio)
            txvMoneda = view.findViewById(R.id.txvFacturasMoneda)
            txvImporte = view.findViewById(R.id.txvFacturaImporteAPagar)
            txvMonedaPtd = view.findViewById(R.id.txvFacturasMonedaPaidTodDate)
            txvImportePtd = view.findViewById(R.id.txvFacturaImportePaidToDate)
            txvFecha = view.findViewById(R.id.txvFacturaFecha)
            txvCur = view.findViewById(R.id.txvFacturaCur)
        }

        fun render(factura: DoFacturaView, onClickListener: (DoFacturaView) -> Unit, onLongPressListener: (View, DoFacturaView) -> Unit) {

            val folioConFormato = folioFormat("${factura.FolioPref}-${factura.FolioNum}")

            txvCardName.text = factura.CardName
            txvFolio.text = "$folioConFormato"
            txvFecha.text = " / ${factura.DocDate}"
            txvCur.text = "${factura.DocCur }"
            txvImporte.text = "${SendData.instance.simboloMoneda} ${factura.DocTotal.format(2)}"
            txvImportePtd.text = "${SendData.instance.simboloMoneda} ${factura.PaidToDate.format(2)}"

            itemView.setOnLongClickListener {
                onLongPressListener(itemView, factura)
                true
            }

            itemView.setOnClickListener {
                onClickListener(factura)
            }


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_facturas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentFactura = dataSet[position]
        viewHolder.render(currentFactura, onClickListener, onLongPressListener)
    }

    override fun getItemCount() = dataSet.size


    fun updateData(newDataSet: List<DoFacturaView>){
        val facturasDiffUtil = FacturasDoDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(facturasDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}

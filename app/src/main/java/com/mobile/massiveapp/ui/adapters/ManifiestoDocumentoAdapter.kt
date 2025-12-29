package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoManifiestoDocumentoView
import com.mobile.massiveapp.ui.view.util.diffutil.ManifiestoDocuementoDiffUtil

class ManifiestoDocumentoAdapter(
    private var dataSet: List<DoManifiestoDocumentoView>,
    private val onClickListener:(DoManifiestoDocumentoView) -> Unit,
    private val onLongPressListener: (View, DoManifiestoDocumentoView) -> Unit
): RecyclerView.Adapter<ManifiestoDocumentoAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNombreCliente: TextView
        val txvTipoDocumento: TextView
        val txvSUNAT: TextView
        val txvVendedor: TextView
        val txvMoneda: TextView
        val txvTotal: TextView
        val txvSaldo: TextView
        val txvPagado: TextView

        init {
            txvNombreCliente = view.findViewById(R.id.txvManDocNombreClienteValue)
            txvTipoDocumento = view.findViewById(R.id.txvManDocTipoDocumentoValue)
            txvSUNAT = view.findViewById(R.id.txvManDocSUNATValue)
            txvVendedor = view.findViewById(R.id.txvManDocVendedorValue)
            txvMoneda = view.findViewById(R.id.txvManDocMonedaValue)
            txvTotal = view.findViewById(R.id.txvManDocTotalValue)
            txvSaldo = view.findViewById(R.id.txvManDocSaldoValue)
            txvPagado = view.findViewById(R.id.txvManDocPagadoValue)
        }

        fun render(documento: DoManifiestoDocumentoView, onClickListener: (DoManifiestoDocumentoView) -> Unit, onLongPressListener: (View, DoManifiestoDocumentoView) -> Unit) {
            txvVendedor.text = documento.Vendedor
            txvSUNAT.text = documento.SUNAT
            txvTipoDocumento.text = documento.TipoDocumento
            txvNombreCliente.text = documento.NombreCliente
            txvMoneda.text = documento.Moneda
            txvTotal.text = "${documento.MonedaSimbolo}${documento.Total}"
            txvSaldo.text = "${documento.MonedaSimbolo}${documento.Saldo}"
            txvPagado.text = "${documento.MonedaSimbolo}${documento.Pagado}"

            itemView.setOnLongClickListener {
                onLongPressListener(itemView, documento)
                true
            }

            itemView.setOnClickListener {
                onClickListener(documento)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manifiesto_documento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentdocumento = dataSet[position]
        viewHolder.render(currentdocumento, onClickListener, onLongPressListener)
    }

    override fun getItemCount() = dataSet.size


    fun updateData(newDataSet: List<DoManifiestoDocumentoView>){
        val documentosDiffUtil = ManifiestoDocuementoDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(documentosDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}

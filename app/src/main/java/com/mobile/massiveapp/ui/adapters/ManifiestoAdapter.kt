package com.mobile.massiveapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoManifiestoView
import com.mobile.massiveapp.ui.view.util.diffutil.ManifiestoDiffUtil


class ManifiestoAdapter (
    private var dataSet: List<DoManifiestoView>,
    private val onClickListener:(DoManifiestoView) -> Unit,
    private val onVerPagosClickListener: (DoManifiestoView) -> Unit

): RecyclerView.Adapter<ManifiestoAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNumero: TextView
        val txvMoneda: TextView
        val txvMontoPendiente: TextView
        val txvMontoCobrado: TextView
        val txvFechaSalida: TextView
        val btnVerPagos: Button

        init {
            txvNumero = view.findViewById(R.id.txvNumeroManifiestoValue)
            txvFechaSalida = view.findViewById(R.id.txvFechaSalidaManifiestoValue)
            txvMoneda = view.findViewById(R.id.txvMonedaManifiestoValue)
            txvMontoPendiente = view.findViewById(R.id.txvMontoPendienteManifiestoValue)
            txvMontoCobrado = view.findViewById(R.id.txvMontoCobradoManifiestoValue)
            btnVerPagos = view.findViewById(R.id.btnManifiestoVerPagos)
        }

        @SuppressLint("SetTextI18n")
        fun render(manifiesto: DoManifiestoView, onClickListener: (DoManifiestoView) -> Unit, onVerPagosClickListener: (DoManifiestoView) -> Unit) {

            txvNumero.text = "#${manifiesto.DocEntry}"
            txvFechaSalida.text = manifiesto.FechaSalida
            txvMoneda.text = manifiesto.Moneda
            txvMontoPendiente.text = "S/ ${manifiesto.MontoPendiente}"
            txvMontoCobrado.text = "S/ ${manifiesto.MontoCobrado}"


            itemView.setOnClickListener {
                onClickListener(manifiesto)
            }

            btnVerPagos.setOnClickListener {
                onVerPagosClickListener(manifiesto)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manifiesto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentManifiesto = dataSet[position]
        viewHolder.render(currentManifiesto, onClickListener, onVerPagosClickListener)
    }

    override fun getItemCount() = dataSet.size


    fun updateData(newDataSet: List<DoManifiestoView>){
        val manifiestoDiffUtil = ManifiestoDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(manifiestoDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}

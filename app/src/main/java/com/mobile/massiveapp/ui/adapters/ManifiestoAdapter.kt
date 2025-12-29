package com.mobile.massiveapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoManifiesto
import com.mobile.massiveapp.ui.view.util.diffutil.FacturasDoDiffUtil
import com.mobile.massiveapp.ui.view.util.diffutil.ManifiestoDiffUtil


class ManifiestoAdapter (
    private var dataSet: List<DoManifiesto>,
    private val onClickListener:(DoManifiesto) -> Unit,
    private val onLongPressListener: (View, DoManifiesto) -> Unit

): RecyclerView.Adapter<ManifiestoAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNumero: TextView
        val txvConductor: TextView
        val txvVehiculo: TextView
        val txvFechaSalida: TextView
        val txvEstado: TextView


        init {
            txvNumero = view.findViewById(R.id.txvNumeroManifiestoValue)
            txvConductor = view.findViewById(R.id.txvNombreConductorValue)
            txvVehiculo = view.findViewById(R.id.txvVehiculoManifiestoValue)
            txvFechaSalida = view.findViewById(R.id.txvFechaSalidaManifiestoValue)
            txvEstado = view.findViewById(R.id.txvEstadoManifiestoValue)
        }

        @SuppressLint("SetTextI18n")
        fun render(manifiesto: DoManifiesto, onClickListener: (DoManifiesto) -> Unit, onLongPressListener: (View, DoManifiesto) -> Unit) {

            txvNumero.text = "${manifiesto.DocEntry}"
            txvConductor.text = manifiesto.U_MSV_MA_CON
            txvVehiculo.text = manifiesto.U_MSV_MA_TRANSPNO
            txvFechaSalida.text = manifiesto.U_MSV_MA_FECSALIDA
            txvEstado.text = if (manifiesto.U_MSV_MA_ESTADO == "Y") "Activo" else "Inactivo"

            itemView.setOnLongClickListener {
                onLongPressListener(itemView, manifiesto)
                true
            }

            itemView.setOnClickListener {
                onClickListener(manifiesto)
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
        viewHolder.render(currentManifiesto, onClickListener, onLongPressListener)
    }

    override fun getItemCount() = dataSet.size


    fun updateData(newDataSet: List<DoManifiesto>){
        val manifiestoDiffUtil = ManifiestoDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(manifiestoDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}

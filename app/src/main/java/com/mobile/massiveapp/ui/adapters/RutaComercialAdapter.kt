package com.mobile.massiveapp.ui.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoRutaComercialView
import com.mobile.massiveapp.ui.view.util.diffutil.RutaComercialDiffUtil

class RutaComercialAdapter(
    private var dataSet: List<DoRutaComercialView>,
    private val onClickListener: (DoRutaComercialView) -> Unit,
    private val onButtonVerRutasListener: (DoRutaComercialView) -> Unit,
) : RecyclerView.Adapter<RutaComercialAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvNombreVendedor: TextView
        val txvFechaRuta: TextView
        val txvCantidadClientes: TextView
        val imvRutaMigrado: ImageView
        val clMigrado: ConstraintLayout
        val txvCancelado: TextView
        val btnVerRutas: Button

        init {
            txvNombreVendedor = view.findViewById(R.id.txvRutaComercialNombreVendedorValue)
            txvFechaRuta = view.findViewById(R.id.txvFechaRutaComercialValue)
            txvCantidadClientes = view.findViewById(R.id.txvRutaComercialCantidadClientesValue)
            imvRutaMigrado = view.findViewById(R.id.imvRutaMigrado)
            clMigrado = view.findViewById(R.id.clRutaMigrado)
            txvCancelado = view.findViewById(R.id.txvRutaCancelado)
            btnVerRutas = view.findViewById(R.id.btnVerRutas)
        }

        fun render(ruta: DoRutaComercialView, onClickListener: (DoRutaComercialView) -> Unit, onButtonVerRutasListener: (DoRutaComercialView) -> Unit) {
            txvNombreVendedor.text = ruta.NombreVendedor
            txvFechaRuta.text = ruta.FechaRuta
            txvCantidadClientes.text = ruta.CantidadClientes.toString()

            btnVerRutas.isVisible = ruta.SuperUser == "Y"

            txvCancelado.isVisible = ruta.Canceled == "Y"
            itemView.isEnabled = ruta.Canceled == "N"

            imvRutaMigrado.setColorFilter(
                ContextCompat.getColor(itemView.context, R.color.color_white),
                PorterDuff.Mode.SRC_IN
            )

            val migrado = ruta.AccMigrated == "Y"

            if (migrado){
                imvRutaMigrado.setImageResource(R.drawable.icon_cloud_done)
                clMigrado.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_green_dark))
            } else {
                imvRutaMigrado.setImageResource(R.drawable.icon_cloud_await)
                clMigrado.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_red))
            }


            itemView.setOnClickListener { onClickListener(ruta) }
            btnVerRutas.setOnClickListener { onButtonVerRutasListener(ruta) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ruta_comercial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(dataSet[position], onClickListener, onButtonVerRutasListener)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoRutaComercialView>) {
        val diffUtil = RutaComercialDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}

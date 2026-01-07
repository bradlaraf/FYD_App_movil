package com.mobile.massiveapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoFormaPagoItemView
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.view.util.diffutil.FormaPagoDiffUtil
import com.mobile.massiveapp.ui.view.util.diffutil.LiquidacionPagoDiffUtil

class FormaPagoAdapter(
    private var dataSet: List<DoFormaPagoItemView>,
    private val onClickListener:(DoFormaPagoItemView) -> Unit
): RecyclerView.Adapter<FormaPagoAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvTitle: TextView
        val txvValue: TextView
        val imvArrow: ImageView

        init {
            txvTitle = view.findViewById(R.id.txvFormaPagoTitle)
            txvValue = view.findViewById(R.id.txvFormaPagoValue)
            imvArrow = view.findViewById(R.id.imvArrowFormaPago)

        }

        @SuppressLint("SetTextI18n")
        fun render(clientePago: DoFormaPagoItemView, onClickListener: (DoFormaPagoItemView) -> Unit){

            itemView.setOnClickListener { onClickListener(clientePago) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forma_pago, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPago = dataSet[position]
        viewHolder.render(currentPago, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoFormaPagoItemView>){
        val pagoDetalleItemsDiffUtil = FormaPagoDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(pagoDetalleItemsDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}
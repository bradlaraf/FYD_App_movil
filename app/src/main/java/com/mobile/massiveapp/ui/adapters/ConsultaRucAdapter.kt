package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.ui.view.util.diffutil.HashMapDiffUtil

class ConsultaRucAdapter (
    private var dataSet: List<HashMap<String, String>>,
    private val onClickListener:(HashMap<String, String>) -> Unit

): RecyclerView.Adapter<ConsultaRucAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvTitle: TextView
        val txvValue: TextView


        init {
            txvTitle = view.findViewById(R.id.txvConsultaDocTitle)
            txvValue = view.findViewById(R.id.txvConsultaDocValue)

        }

        fun render(consultaDoc: HashMap<String, String>, onClickListener: (HashMap<String, String>) -> Unit) {
            txvTitle.text = consultaDoc.keys.toString().replace("[", "").replace("]", "")
            txvValue.text = consultaDoc.values.toString().replace("[", "").replace("]", "")

            itemView.setOnClickListener {
                onClickListener(consultaDoc)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consulta_documento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentFactura = dataSet[position]
        viewHolder.render(currentFactura, onClickListener)
    }

    override fun getItemCount() = dataSet.size


    fun updateData(newDataSet: List<HashMap<String, String>>){
        val hashMapDiffUtil = HashMapDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(hashMapDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
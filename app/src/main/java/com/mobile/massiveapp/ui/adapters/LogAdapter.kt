package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.data.database.entities.ErrorLogEntity

class LogAdapter (
    private var dataSet: List<ErrorLogEntity>,
    private val onClickListener:(ErrorLogEntity) -> Unit
): RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvMensage: TextView
        val txvFecha: TextView
        val txvHora: TextView

        init {
            txvMensage = view.findViewById(R.id.txvLogErrorMessage)
            txvFecha = view.findViewById(R.id.txvLogErrorDate)
            txvHora = view.findViewById(R.id.txvLogErrorCode)
        }

        fun render(error: ErrorLogEntity, onClickListener: (ErrorLogEntity) -> Unit){
            txvMensage.text = "${error.ErrorMessage} - ${error.ErrorHour}"
            txvFecha.text = error.ErrorDate
            txvHora.text = error.ErrorCode
            itemView.setOnClickListener { onClickListener(error) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_error_log, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentError = dataSet[position]
        viewHolder.render(currentError, onClickListener)
    }


    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<ErrorLogEntity>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}
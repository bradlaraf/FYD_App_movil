package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoSocioContactos
import com.mobile.massiveapp.ui.view.util.diffutil.SocioContactosDiffUtil

class SocioContactosAdapter (
    private var dataSet: List<DoSocioContactos>,
    private val onClickListener:(DoSocioContactos) -> Unit

): RecyclerView.Adapter<SocioContactosAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvContactoNombre: TextView
        val txvContactoTelefono: TextView

        init {
            txvContactoNombre = view.findViewById(R.id.txvSocioContactoItemNombre)
            txvContactoTelefono = view.findViewById(R.id.txvSocioCOntactoItemTelefono)

        }

        fun render(contacto: DoSocioContactos, onClickListener: (DoSocioContactos) -> Unit){
            itemView.setOnClickListener {
                onClickListener(contacto)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio_contacto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentSocioContacto = dataSet[position]
        viewHolder.txvContactoNombre.text = dataSet[position].Name
        viewHolder.txvContactoTelefono.text = "Celular: ${dataSet[position].Cellolar}"

        viewHolder.render(currentSocioContacto, onClickListener)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoSocioContactos>){
        val articuloDiffUtil = SocioContactosDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(articuloDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
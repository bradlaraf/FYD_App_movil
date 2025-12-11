package com.mobile.massiveapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoAnexoImagen
import com.mobile.massiveapp.ui.view.util.diffutil.AnexoImagenDiffUtil


class AnexoAdapter(
    private var dataSet: List<DoAnexoImagen>,
    private val onClickListener:(DoAnexoImagen) -> Unit,
    private val onLongPressListener: (View, DoAnexoImagen) -> Unit
): RecyclerView.Adapter<AnexoAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNombreImagen: TextView
        val txvUriImagen: ImageView

        init {
            txvNombreImagen = view.findViewById(R.id.txvAnexoImagenNombre)
            txvUriImagen = view.findViewById(R.id.imvAnexoImagen)
        }

        fun render(anexoImagen: DoAnexoImagen, onClickListener: (DoAnexoImagen) -> Unit, onLongPressListener: (View, DoAnexoImagen) -> Unit) {
            txvNombreImagen.text = anexoImagen.Nombre

            Glide.with(itemView.context)
                .load(anexoImagen.Uri)
                .placeholder(R.drawable.icon_image)
                .error(R.drawable.test_image_anexo)
                .into(txvUriImagen)

            itemView.setOnLongClickListener {
                onLongPressListener(itemView, anexoImagen)
                true
            }

            itemView.setOnClickListener {
                onClickListener(anexoImagen)
            }


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anexo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentFactura = dataSet[position]
        viewHolder.render(currentFactura, onClickListener, onLongPressListener)
    }

    override fun getItemCount() = dataSet.size


    fun updateData(newDataSet: List<DoAnexoImagen>){
        val anexoImagenDiffUtil = AnexoImagenDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(anexoImagenDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
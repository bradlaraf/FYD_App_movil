package com.mobile.massiveapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoClienteSocios
import com.mobile.massiveapp.domain.model.DoSocioNuevoAwait
import com.mobile.massiveapp.ui.view.util.diffutil.SocioNegocioDiffUtil
import com.mobile.massiveapp.ui.view.util.limitTo

class SocioBuscarAdapter(
    private var dataSet: List<DoClienteSocios>,
    private var dataSetNuevoSocioAwait: List<DoSocioNuevoAwait>,
    private val onClickListener:(Any) ->Unit
): RecyclerView.Adapter<SocioBuscarAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvCardName: TextView
        val txvCardCode: TextView
        val clSocio: ConstraintLayout
        val imvVerifySap: ImageView
        init {
            txvCardName = view.findViewById(R.id.txvCardName)
            txvCardCode = view.findViewById(R.id.txvCardCode)
            clSocio = view.findViewById(R.id.clSocio)
            imvVerifySap = view.findViewById(R.id.imvSocioVerifySap)
        }

        fun render(doSocio: Any, onClickListener: (Any) -> Unit, isSelected: Boolean) {
            clSocio.setOnClickListener {
                if (isSelected) {
                    clSocio.setBackgroundColor(Color.parseColor("#85D8FA"))
                } else {
                    clSocio.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }

                // Configurar el OnClickListener
                itemView.setOnClickListener {
                    onClickListener(doSocio) // Llamar al OnClickListener con el objeto de datos
                }

            }

        }


        fun assignIcon(item: Any){
            when(item){
                is DoClienteSocios -> {
                    txvCardName.text = item.CardName.limitTo(20)
                    txvCardCode.text = item.CardCode
                    imvVerifySap.setImageResource(R.drawable.icon_cloud_done)
                }
                is DoSocioNuevoAwait -> {
                    txvCardName.text = item.CardName.limitTo(20)
                    txvCardCode.text = item.CardCode
                    imvVerifySap.setImageResource(R.drawable.icon_cloud_await)
                }
            }
        }
    }

    fun itemCambio(selectPosicion: Int){
        lastSelectedPosition = selectedPosition
        selectedPosition = selectPosicion
        notifyItemChanged(lastSelectedPosition)
        notifyItemChanged(selectedPosition)

    }


    fun updateListOfSnMigrated(newDataSet: List<DoClienteSocios>){
        val socioNegocioDiffUtil = SocioNegocioDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(socioNegocioDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentSocio =
            if (position < dataSetNuevoSocioAwait.size)
                dataSetNuevoSocioAwait[position]
            else
                dataSet[position - dataSetNuevoSocioAwait.size]

        /*if (selectedPosition == viewHolder.adapterPosition) {
            viewHolder.clSocio.setBackgroundColor(Color.parseColor("#85D8FA"))
        } else {
            viewHolder.clSocio.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }*/

        viewHolder.assignIcon(currentSocio)
        val isSelected = selectedPosition == position
        viewHolder.render(currentSocio, onClickListener, isSelected)

    }

    override fun getItemCount() = dataSet.size + dataSetNuevoSocioAwait.size


}
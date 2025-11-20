package com.mobile.massiveapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoClienteScreen
import com.mobile.massiveapp.ui.view.util.diffutil.SocioScreenDiffUtil

class SocioSeleccionablePedidoAdapter (
    private var dataSet: List<DoClienteScreen>,
    private val setListNum: Boolean,
    private val onClickListener:(DoClienteScreen) -> Unit
): RecyclerView.Adapter<SocioSeleccionablePedidoAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvCardCode: TextView
        val txvCardName: TextView
        val imvMigrado: ImageView
        val txvListaPrecio: TextView

        init {
            txvCardName = view.findViewById(R.id.txvPedidosSeleccionableCardName)
            txvCardCode = view.findViewById(R.id.txvPedidosSeleccionableNumeroDocumento)
            imvMigrado = view.findViewById(R.id.imvPedidosSeleccionableSocioVerifySap)
            txvListaPrecio = view.findViewById(R.id.txvPedidosSeleccionableListaPrecio)
        }

        fun render(currentCliente: DoClienteScreen, setListNum: Boolean){

            if (currentCliente.AccMigrated =="N"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_await)
            } else if (currentCliente.AccMigrated =="Y" && currentCliente.AccFinalized == "N"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_intermedia)
            } else if (currentCliente.AccMigrated =="Y" && currentCliente.AccFinalized == "Y"){
                imvMigrado.setImageResource(R.drawable.icon_cloud_done)
            }

            txvCardCode.text = "${currentCliente.LicTradNum} - ${currentCliente.CardCode}"
            txvCardName.text = currentCliente.CardName

            if (setListNum){
                if (currentCliente.ListNum == 2){
                    txvListaPrecio.alpha = 1.0F
                } else {
                    txvListaPrecio.alpha = 0.0F
                }
            } else {
                txvListaPrecio.isVisible = false
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio_seleccionable_pedido, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentSocio = dataSet[position]
        viewHolder.render(currentSocio, setListNum)

        viewHolder.itemView.setOnClickListener {
            lastSelectedPosition = selectedPosition // LSP = -1 // LSP = 2
            selectedPosition = viewHolder.layoutPosition // SP = 2 // SP = 5
            notifyItemChanged(lastSelectedPosition) // Notify[-1] // Notify[2] // Se cambia el color al default
            notifyItemChanged(selectedPosition) // Notify[2] // Notify[5] // Se cambia el color al azul
            itemSocioClick(currentSocio, onClickListener)
        }


        if (selectedPosition == viewHolder.layoutPosition){
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#85D8FA"))
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }


    }
    fun itemSocioClick(socio: DoClienteScreen, onClickListener: (DoClienteScreen) -> Unit){
        onClickListener(socio)
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<DoClienteScreen>){
        val clienteDiffUtil = SocioScreenDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(clienteDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateDataForSearching(newDataSet: List<DoClienteScreen>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}
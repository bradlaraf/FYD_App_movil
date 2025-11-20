package com.mobile.massiveapp.ui.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.ui.view.util.diffutil.UsuariosDiffUtil

class UsuariosAdapter (
    private var dataSet: List<DoConfigurarUsuario>,
    private val onClickListener:(DoConfigurarUsuario) -> Unit,
    private val onLongPressListener: (View, DoConfigurarUsuario) -> Unit

): RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txvNombre: TextView
        val txvCorreo: TextView
        val txvCodigo: TextView
        val imvSession: ImageView
        val imvAlert: ImageView
        val imvBlocked: ImageView

        init {
            txvNombre = view.findViewById(R.id.txvUsuarioNombre)
            txvCorreo = view.findViewById(R.id.txvUsuarioCorreo)
            txvCodigo = view.findViewById(R.id.txvUsuarioCodigo)
            imvSession = view.findViewById(R.id.imvSessionActive)
            imvBlocked = view.findViewById(R.id.imvBlocked)
            imvAlert = view.findViewById(R.id.imvAlert)

        }

        fun render(usuario: DoConfigurarUsuario, onClickListener: (DoConfigurarUsuario) -> Unit, onLongPressListener: (View, DoConfigurarUsuario) -> Unit) {

            txvNombre.text = usuario.Name
            txvCodigo.text = usuario.Code
            txvCorreo.text = usuario.Email

            val colorGreen = ContextCompat.getColor(itemView.context, R.color.color_green_light)
            val colorRed = ContextCompat.getColor(itemView.context, R.color.color_red_light)
            if (usuario.AccStatusSession == "N"){
                imvSession.setColorFilter(colorRed, PorterDuff.Mode.SRC_IN)
            }

            if (usuario.AccStatusSession == "Y"){
                imvSession.setColorFilter(colorGreen, PorterDuff.Mode.SRC_IN)
            }

            if (usuario.AccLocked == "Y"){
                imvBlocked.setImageResource(R.drawable.icon_person_blocked)
            } else {
                imvBlocked.setImageResource(R.drawable.icon_person_check)
            }

            imvAlert.isVisible = usuario.ListaPrecios == 0  ||
                                 usuario.Zonas == 0  ||
                                 usuario.Almacenes == 0  ||
                                 usuario.GrupoSocios == 0  ||
                                 usuario.GrupoArticulos == 0


            itemView.setOnLongClickListener {
                onLongPressListener(txvNombre, usuario)
                true
            }

            itemView.setOnClickListener {
                onClickListener(usuario)
            }


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuarios, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentUsuario = dataSet[position]
        viewHolder.render(currentUsuario, onClickListener, onLongPressListener)
    }

    override fun getItemCount() = dataSet.size

    fun getData(): List<DoConfigurarUsuario> = dataSet

    fun updateData(newDataSet: List<DoConfigurarUsuario>){
        val facturasDiffUtil = UsuariosDiffUtil(dataSet, newDataSet)
        val diffResult = DiffUtil.calculateDiff(facturasDiffUtil)
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }
}
package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.content.Context
import com.mobile.massiveapp.R

class BaseDialogAceptDialog (
    private val context: Context,
) {
    fun showConfirmationDialog(
        title: String,
        icon: Int = R.drawable.icon_alertas,
        textConfirmacion: String = "Aceptar",
        onConfirmacion: () -> Unit,
        onCancel: () -> Unit
    ){
        val builder = AlertDialog.Builder(context)
        builder
            .setTitle(title)
            .setIcon(icon)
            .setCancelable(true)
            .setPositiveButton(textConfirmacion){ _, _ ->
                onConfirmacion()
            }
            .setNegativeButton("Cancelar"){ _, _ ->
                onCancel()
            }
        val dialog = builder.create()
        dialog.show()
    }
}

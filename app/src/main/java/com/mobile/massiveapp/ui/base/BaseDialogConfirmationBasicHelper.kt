package com.mobile.massiveapp.ui.base

import android.app.AlertDialog
import android.content.Context

class BaseDialogConfirmationBasicHelper(
    private val context: Context
    ) {

    fun showConfirmationDialog(
        title: String,
        textConfirmacion: String = "Aceptar",
        textCancelacion: String = "Cancelar",
        onConfirmacion: () -> Unit
    ){
        val builder = AlertDialog.Builder(context)
        builder
            .setTitle(title)
            .setPositiveButton(textConfirmacion){ _, _ ->
                onConfirmacion()
            }
            .setNegativeButton(textCancelacion){ dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
}
package com.mobile.massiveapp.ui.view.util

class SendData private constructor(){

    var accDocEntryDoc = ""
    var docLine = -1
    var simboloMoneda = ""
    var docEntry = -1
    var docEntryFactura = -1
    var lineIdManifiestoDocumento = -1

    companion object{
        val instance: SendData by lazy { SendData() }
    }
}

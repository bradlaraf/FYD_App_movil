package com.mobile.massiveapp.ui.view.util

class SendData private constructor(){

    var accDocEntryDoc = ""
    var docLine = -1
    var simboloMoneda = ""

    companion object{
        val instance: SendData by lazy { SendData() }
    }
}

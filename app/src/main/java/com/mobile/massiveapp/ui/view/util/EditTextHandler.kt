package com.mobile.massiveapp.ui.view.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText

class EditTextHandler (
    private val editText: EditText,
    private val emptyBorder: Drawable
    ): View.OnFocusChangeListener {

    private val originalBorder = editText.background

    init {
        editText.onFocusChangeListener = this
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus && editText.text.toString().isEmpty()) {
            editText.background = emptyBorder
        } else {
            editText.background = originalBorder
        }
    }
}

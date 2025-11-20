package com.mobile.massiveapp.ui.view.util

import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.mobile.massiveapp.R

class SearchViewHelper(
    val menu: Menu?,
    hint: String,
    val onQueryTextChangeCallback: (String) -> Unit,
    val onQueryTextSubmitCallback: (String) -> Unit
) {
    private val search = menu?.findItem(R.id.app_bar_search)
    private val searchView = search?.actionView as SearchView


    init {
        searchView.maxWidth = 420
        searchView.queryHint = hint
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onQueryTextSubmitCallback(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                onQueryTextChangeCallback(newText.orEmpty())
                return true
            }
        })
    }

    fun setOnDismiss(onDismissCallBack: () -> Unit){
        searchView.setOnCloseListener {
            onDismissCallBack()
            false
        }
    }
}

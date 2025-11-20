package com.mobile.massiveapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.core.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkConnectivityObserverViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _connectivityStatus = MutableStateFlow<ConnectivityObserver.Status>(
        ConnectivityObserver.Status.Available
    )

    val connectivityStatus: StateFlow<ConnectivityObserver.Status>
        get() = _connectivityStatus

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { newStatus ->
                _connectivityStatus.emit(newStatus)
            }
        }
    }
}
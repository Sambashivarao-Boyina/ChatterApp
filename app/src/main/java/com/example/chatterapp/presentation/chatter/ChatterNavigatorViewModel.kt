package com.example.chatterapp.presentation.chatter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatterNavigatorViewModel @Inject constructor(
    private val socket: Socket
) : ViewModel() {
    init {
        connectSocket()
    }
    private fun connectSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            socket.connect()
        }
    }

    private fun disconnectSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            socket.disconnect()
        }
    }

    public override fun onCleared() {
        super.onCleared()
        disconnectSocket()
    }
}
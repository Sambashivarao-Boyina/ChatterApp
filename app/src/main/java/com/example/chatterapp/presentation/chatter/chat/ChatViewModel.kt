package com.example.chatterapp.presentation.chatter.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.data.remote.Dto.SendedData
import com.example.chatterapp.domain.model.Chat
import com.example.chatterapp.domain.model.Friend
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.presentation.authetication.login.LoginScreen
import com.example.chatterapp.util.Constants.extractData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatterRepository: ChatterRepository,
    private val socket: Socket
) : ViewModel() {

    var friendID by mutableStateOf<String?>(null)
        private set

    var friend by mutableStateOf<Friend?>(null)
        private set

    var chat by mutableStateOf<Chat?>(null)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var message by mutableStateOf("")
        private set

    init {
        setUpSocket()
    }

    fun onEvent(event: ChatEvent) {
        when(event) {
            ChatEvent.RemoveSideEffect -> {
                sideEffect = null
            }
            is ChatEvent.UpDateMessage -> {
                message = event.message
            }
            ChatEvent.SendMessage -> {
                if(message.trim().isNotEmpty()) {
                    sendMessage()
                }
            }
        }
    }

    fun setFriendId(id: String) {
        friendID = id
        getFriend()
    }

    private fun setUpSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("message", "message received");
            socket.on("message_received", onMessageListner)
        }
    }

    private val onMessageListner = Emitter.Listener { args ->
        Log.d("reload","reload data")
        getChat()
    }

    fun getFriend() {
        if(friendID != null) {
            viewModelScope.launch {
                try {
                    val response = chatterRepository.getFriend(friendID!!)
                    if(response.isSuccessful) {
                        friend = response.body()
                        getChat()
                    } else {
                        sideEffect = extractData(response.errorBody(),"message")
                    }

                }catch (e: HttpException) {
                    sideEffect = e.localizedMessage
                } catch (e: IOException) {
                    sideEffect = e.localizedMessage
                } catch (e: SocketTimeoutException) {
                    sideEffect = e.localizedMessage
                } catch (e: Exception) {
                    sideEffect = e.localizedMessage
                }
            }
        }

    }

    private fun sendMessage() {
        viewModelScope.launch {
            try {
                val data = SendedData(
                    message = message.trim()
                )
                val response = chatterRepository.sendMessage(
                    id = friendID!!,
                    sendedData = data
                )
                if(response.isSuccessful) {
                    chat = response.body()
                    message = ""

                } else {
                    sideEffect = extractData(response.errorBody(),"message")
                }

            }catch (e: HttpException) {
                sideEffect = e.localizedMessage
            } catch (e: IOException) {
                sideEffect = e.localizedMessage
            } catch (e: SocketTimeoutException) {
                sideEffect = e.localizedMessage
            } catch (e: Exception) {
                sideEffect = e.localizedMessage
            }
        }
    }


    private fun getChat() {
        friend?.let {
            viewModelScope.launch {
                try {
                    val response = chatterRepository.getFriendChat(friend!!.chat)
                    if(response.isSuccessful) {
                        chat = response.body()
                        Log.d("chat",chat.toString())
                    } else {
                        sideEffect = extractData(response.errorBody(),"message")
                    }

                }catch (e: HttpException) {
                    sideEffect = e.localizedMessage
                } catch (e: IOException) {
                    sideEffect = e.localizedMessage
                } catch (e: SocketTimeoutException) {
                    sideEffect = e.localizedMessage
                } catch (e: Exception) {
                    sideEffect = e.localizedMessage
                }
            }
        }
    }

}

sealed class ChatEvent{
    object RemoveSideEffect: ChatEvent()
    data class UpDateMessage(val message: String) : ChatEvent()
    object SendMessage: ChatEvent()
}
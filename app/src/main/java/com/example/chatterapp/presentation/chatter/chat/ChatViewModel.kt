package com.example.chatterapp.presentation.chatter.chat

import android.os.Handler
import android.os.Looper
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
import com.example.chatterapp.util.Constants.extractData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import retrofit2.HttpException
import java.io.File
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

    private val _activeUser = MutableStateFlow<List<String>>(emptyList())
    val activeUsers: StateFlow<List<String>> = _activeUser

    init {
        getActiveUser()
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
            ChatEvent.BlockFriend -> {
                blockFriend()
            }
            ChatEvent.UnBlockFriend -> {
                unblockFriend()
            }
            ChatEvent.RefershData ->{
                getChat()
            }
        }
    }

    fun setFriendId(id: String) {
        friendID = id
        getFriend()
    }



    private fun setUpSocket() {

        if (socket == null) {
            return
        }
        if (socket.connected()) {

            addSocketListeners() // Socket is already connected, add listeners
        } else {
            socket.on(Socket.EVENT_CONNECT) {
                addSocketListeners() // Add listeners once connected
            }

            socket.on(Socket.EVENT_CONNECT_ERROR) {
                Log.d("SetUpSocket", "Error connecting to socket: ${it[0]}")
                // Optionally implement retry logic here
            }
        }

    }

    private fun addSocketListeners() {
        try {
            if(socket.connected()) {

                socket.on("message_received") { args ->
                    getChat()

                }
                socket.on("user_list"){ args ->
                    if(args.isNotEmpty()) {
                        val jsonArray = args[0] as JSONArray
                        val usersList = mutableListOf<String>()
                        for(i in 0 until jsonArray.length()){
                            usersList.add(jsonArray.getString(i))
                        }
                        _activeUser.value = usersList
                    }
                }
                socket.on("chat_updated"){ args ->
                    getChat()
                }


            } else {
                Log.d("socketConnection","socket is not connected so not able to listen")
            }
        } catch (e: Exception) {
            Log.d("error", "Error setting up socket listeners")
        }
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

                }catch (e: Exception) {
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

    private fun getActiveUser() {
        viewModelScope.launch {
            try {
                val response = chatterRepository.getActiveUser()
                if(response != null){
                    Log.d("respoene",response.toString())
                }
                if(response.isSuccessful) {
                    _activeUser.value = response.body() ?: emptyList<String>()
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

    private fun blockFriend() {
        viewModelScope.launch {
           friendID?.let {
               try {
                   val response = chatterRepository.blockFriend(friendID!!)

                   if(response.isSuccessful) {
                       chat = response.body()
                   } else{
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
               Log.d("error",sideEffect.toString())
           }
        }
    }

    private fun unblockFriend() {
        viewModelScope.launch {
            friendID?.let {
                try {
                    val response = chatterRepository.unBlockFriend( friendID!!)
                    if(response.isSuccessful) {
                        chat = response.body()
                    } else{
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

     fun sendImage(file: File) {
        viewModelScope.launch {
            isLoading = true
            try {
                Log.d("file",file.name)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file",file.name, requestFile )

                val response = chatterRepository.sendImageMessage(file = body,id = friendID!!)
                if(response.isSuccessful) {
                    chat = response.body()
                    sideEffect = "Image is sent"
                } else {
                    sideEffect = extractData(response.errorBody(), "message")
                    Log.d("error",sideEffect.toString())
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

            isLoading = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            socket.off("message_received")
            socket.off("user_list")
            socket.off("chat_updated")
        }
    }


}

sealed class ChatEvent{
    object RemoveSideEffect: ChatEvent()
    data class UpDateMessage(val message: String) : ChatEvent()
    object SendMessage: ChatEvent()
    object BlockFriend: ChatEvent()
    object UnBlockFriend: ChatEvent()
    object  RefershData: ChatEvent()
}
package com.example.chatterapp.presentation.chatter.addfriend

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.domain.model.User
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.util.Constants.extractData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val chatterRepository: ChatterRepository
): ViewModel() {
    private val _users = mutableStateOf<List<User>>(emptyList())
    val users:State<List<User>> = _users

    var isLoading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    init {
        getUsers()
    }

    fun onEvent(event:AddFriendEvent) {
        when(event) {
            is AddFriendEvent.SendRequest -> {
                viewModelScope.launch {
                    sendFriendRequest(id = event.id)
                }
            }
        }
    }

    private suspend fun sendFriendRequest(id: String) {
        isLoading = true

        try {
            val response = chatterRepository.sendFriendRequest(id =  id)

            if(response.isSuccessful) {
                val message = extractData(response.body(),"message")
                getUsers()
                Log.d("message",message!!)
            } else {
                val errorMessage = extractData(response.errorBody(),"message")
                Log.d("error",errorMessage!!)
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

     fun getUsers(){


        viewModelScope.launch {
            Log.d("started","process started")
            isLoading = true
            try {
                val response = chatterRepository.getAllUsers()

                if(response.isSuccessful) {
                    if(response.body() != null) {
                        _users.value = response.body()!!
                    } else {
                        sideEffect = "some went wrong"
                    }

                } else {
                    val message = extractData(response.errorBody(), "message")
                    sideEffect = message
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
            Log.d("ended","process ended");
            isLoading = false
        }


    }


}

sealed class AddFriendEvent {
    data class SendRequest(val id: String): AddFriendEvent()
}


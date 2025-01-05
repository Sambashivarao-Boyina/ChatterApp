package com.example.chatterapp.presentation.chatter

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.data.remote.Dto.UpdateData
import com.example.chatterapp.domain.model.UserDetails
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.util.Constants.extractData
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ChatterNavigatorViewModel @Inject constructor(
    private val socket: Socket,
    private val chatterRepository: ChatterRepository
) : ViewModel() {

    var userProfile by mutableStateOf<UserDetails?>(null)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    init {
        initToken()
        connectSocket()
        getUserDetails()
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

    private fun initToken() {
        viewModelScope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                chatterRepository.saveFcmToken(data = UpdateData(token))
            }catch (e:Exception) {
                Log.d("Error","Error")
            }
        }
    }

    public override fun onCleared() {
        super.onCleared()
        disconnectSocket()
    }

    fun getUserDetails() {
        viewModelScope.launch {
            try {
                val response = chatterRepository.getSelfDetails()

                if(response.isSuccessful) {
                    val user: UserDetails = response.body()!!
                    userProfile = user
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
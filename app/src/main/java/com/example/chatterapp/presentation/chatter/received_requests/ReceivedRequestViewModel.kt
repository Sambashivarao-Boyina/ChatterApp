package com.example.chatterapp.presentation.chatter.received_requests

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.domain.model.FriendRequest
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.util.Constants.extractData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReceivedRequestViewModel @Inject constructor(
    private val chatterRepository: ChatterRepository
) : ViewModel() {
    var receivededRequests by mutableStateOf<List<FriendRequest>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    init {
        getReceivedRequests()
    }


    fun onEvent(event:ReceivedRequestEvent) {
        when(event) {
            is ReceivedRequestEvent.AcceptRequest -> {
                viewModelScope.launch {
                    acceptRequest(id = event.id)
                }
            }
            is ReceivedRequestEvent.RejectRequest -> {
                viewModelScope.launch {
                    rejectRequest(id = event.id)
                }
            }

            is ReceivedRequestEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    private fun acceptRequest(id:String) {
        viewModelScope.launch {
            try {
                val response = chatterRepository.acceptRequest(id)
                if(response.isSuccessful) {
                    sideEffect = extractData(response.body(), "message")
                    getReceivedRequests()
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

    private suspend fun rejectRequest(id:String) {
        try {
            val response = chatterRepository.rejectRequest(id)
            if(response.isSuccessful) {
                sideEffect = extractData(response.body(), "message")
                getReceivedRequests()

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

    private fun getReceivedRequests() {
        Log.d("is Requesting","reqesting")
        viewModelScope.launch {
            isLoading = false

            try {
                val response = chatterRepository.userReceivedRequest()
                if(response.isSuccessful) {
                    val requests: List<FriendRequest> = response.body()!!
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

                    // Sort by `createdAt` in descending order
                    val sortedRequests = requests.sortedByDescending {
                        dateFormat.parse(it.createdAt) // Parse the date string
                    }
                    receivededRequests = sortedRequests

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

            isLoading = false
        }
    }
}

sealed class ReceivedRequestEvent {
    data class AcceptRequest(val id: String): ReceivedRequestEvent()
    data class RejectRequest(val id: String): ReceivedRequestEvent()
    object RemoveSideEffect : ReceivedRequestEvent()
}
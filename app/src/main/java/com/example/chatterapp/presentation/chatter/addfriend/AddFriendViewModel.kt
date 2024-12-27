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
import com.example.chatterapp.presentation.chatter.home.HomeEvent
import com.example.chatterapp.util.Constants.extractData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val chatterRepository: ChatterRepository
): ViewModel() {
    var users by mutableStateOf<List<User>>(emptyList())
        private set

    var searchValue by mutableStateOf("")
        private set

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
            is AddFriendEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            is AddFriendEvent.UpdateSearchValue -> {
                searchValue = event.value
            }

            AddFriendEvent.Search -> {
                if(searchValue.trim().isEmpty()) {
                    getUsers()
                } else {
                    searchUsers(searchValue)
                }
            }
        }
    }

    private suspend fun sendFriendRequest(id: String) {
        isLoading = true

        try {
            val response = chatterRepository.sendFriendRequest(id =  id)

            if(response.isSuccessful) {
                sideEffect = extractData(response.body(),"message")
                getUsers()
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

     fun getUsers(){


        viewModelScope.launch {
            isLoading = true
            try {
                val response = chatterRepository.getAllUsers()

                if(response.isSuccessful) {
                    if(response.body() != null) {
                        users = response.body() ?: emptyList()

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
            isLoading = false
        }


    }

    private fun searchUsers(searchValue: String) {
        viewModelScope.launch {


            try {
                val response =  chatterRepository.searchUsers(searchValue)
                if(response.isSuccessful) {
                    users = response.body() ?: emptyList()
                } else {
                    sideEffect = extractData(response.errorBody(), "message")
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

sealed class AddFriendEvent {
    data class SendRequest(val id: String): AddFriendEvent()
    object RemoveSideEffect: AddFriendEvent()

    data class UpdateSearchValue(val value: String): AddFriendEvent()

    object Search: AddFriendEvent()
}


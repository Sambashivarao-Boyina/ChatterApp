package com.example.chatterapp.presentation.chatter.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import org.json.JSONArray
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatterRepository: ChatterRepository,
    private val socket: Socket
) : ViewModel() {
    private val _friends = mutableStateOf<List<Friend>>(emptyList())
    var friends: State<List<Friend>> = _friends

    private val _activeUser = MutableStateFlow<List<String>>(emptyList())
    val activeUsers: StateFlow<List<String>> = _activeUser

    var searchValue by mutableStateOf("")
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set


    init {
        getFriends()
        setupSocket()
    }

    private fun setupSocket() {
        try {
            if(socket.connected()){
                socket.on("user_list"){ args ->
                    if (args.isNotEmpty()) {
                        val jsonArray = args[0] as JSONArray
                        val usersList = mutableListOf<String>()
                        for (i in 0 until jsonArray.length()) {
                            usersList.add(jsonArray.getString(i))
                        }
                        _activeUser.value = usersList
                    }
                }
            } else {
                socket.on(Socket.EVENT_CONNECT) {
                    socket.on("user_list") { args ->
                        if (args.isNotEmpty()) {
                            val jsonArray = args[0] as JSONArray
                            val usersList = mutableListOf<String>()
                            for (i in 0 until jsonArray.length()) {
                                usersList.add(jsonArray.getString(i))
                            }
                            _activeUser.value = usersList
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("error", "error")
        }
    }


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            is HomeEvent.UpdateSearchValue -> {
                searchValue = event.value
            }

            HomeEvent.Search -> {
                searchValue = searchValue.trim()
                if (searchValue == "") {
                    getFriends()
                } else {
                    _friends.value = filterFriendsByUsername(searchValue, _friends.value)
                }
            }
        }
    }

    fun getFriends() {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = chatterRepository.getFriendsList()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val friendsList = response.body()!!
                        val sortedFriends = friendsList.sortedWith { friend1, friend2 ->
                            val lastMessage1 = friend1.lastMessage
                            val lastMessage2 = friend2.lastMessage

                            // If both friends have a message, compare by createdAt (from new to old)
                            if (lastMessage1 != null && lastMessage2 != null) {
                                lastMessage2.createdAt.compareTo(lastMessage1.createdAt)  // new to old
                            } else if (lastMessage1 != null) {
                                -1
                            } else if (lastMessage2 != null) {
                                1
                            } else {
                                0
                            }
                        }
                        _friends.value = sortedFriends
                    } else {
                        sideEffect = "you didn't have frineds"
                    }

                } else {
                    sideEffect = extractData(response.errorBody(), "message")

                }

            } catch (e: HttpException) {
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

    private fun filterFriendsByUsername(searchValue: String, friends: List<Friend>): List<Friend> {
        return friends.filter { friend ->

            val normalizedUsername = friend.person.username.replace(" ", "").lowercase()
            val normalizedSearchValue = searchValue.replace(" ", "").lowercase()

            normalizedUsername.contains(normalizedSearchValue)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            socket.off("user_list")
        }
    }

}

sealed class HomeEvent {
    object RemoveSideEffect : HomeEvent()
    data class UpdateSearchValue(val value: String) : HomeEvent()

    object Search : HomeEvent()
}
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
        viewModelScope.launch(Dispatchers.IO) {
            socket.on("user_list", onUserListReceived)
        }
    }

    private val onUserListReceived = Emitter.Listener { args ->
        if(args.isNotEmpty()) {
            val jsonArray = args[0] as JSONArray
            val usersList = mutableListOf<String>()
            for(i in 0 until jsonArray.length()){
                usersList.add(jsonArray.getString(i))
            }
            _activeUser.value = usersList
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
                if(searchValue == "") {
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
                        _friends.value = response.body()!!
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
            Log.d("error", sideEffect.toString())
            isLoading = false

        }


    }

    private fun filterFriendsByUsername(searchValue: String, friends: List<Friend>): List<Friend> {
        return friends.filter { friend ->
            // Normalize the username and searchValue by removing spaces and ignoring case
            val normalizedUsername = friend.person.username.replace(" ", "").lowercase()
            val normalizedSearchValue = searchValue.replace(" ", "").lowercase()

            // Check if the normalized username contains the normalized search value
            normalizedUsername.contains(normalizedSearchValue)
        }
    }

}

sealed class HomeEvent {
    object RemoveSideEffect : HomeEvent()
    data class UpdateSearchValue(val value: String) : HomeEvent()

    object Search : HomeEvent()
}
package com.example.chatterapp.presentation.chatter.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatterapp.data.remote.ChatterApi
import com.example.chatterapp.domain.model.Friend
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
class HomeViewModel @Inject constructor(
    private val chatterRepository: ChatterRepository
) : ViewModel() {
    private val _friends = mutableStateOf<List<Friend>>(emptyList())
    val friends: State<List<Friend>> = _friends

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading  by mutableStateOf(false)
        private set
    init {
        getFriends()
    }

    fun getFriends() {

        viewModelScope.launch {

            try {
                isLoading = true
                val response = chatterRepository.getFriendsList()
                if(response.isSuccessful) {
                    if(response.body() != null) {
                        _friends.value = response.body()!!
                    } else {
                        sideEffect = "you didn't have frineds"
                    }

                } else {
                    val message = extractData(response.errorBody(),"message")
                    Log.d("Errror",message!!)
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


}
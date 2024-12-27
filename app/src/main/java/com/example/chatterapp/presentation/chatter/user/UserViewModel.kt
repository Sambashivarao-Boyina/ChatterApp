package com.example.chatterapp.presentation.chatter.user

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
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val chatterRepository: ChatterRepository
) :ViewModel() {
    var user by mutableStateOf<User?>(null)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    fun onEvent(event: UserEvent) {
        when(event) {
            is UserEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    fun getUser(id: String) {
        viewModelScope.launch {
            try {
                val response = chatterRepository.getUserDetails(id)
                if(response.isSuccessful) {
                    user = response.body()
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

sealed class UserEvent{
    object RemoveSideEffect: UserEvent()
}
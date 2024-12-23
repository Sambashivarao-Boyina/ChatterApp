package com.example.chatterapp.presentation.chatter.userProfile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.data.remote.Dto.UpdateData
import com.example.chatterapp.domain.manager.LocalUserManager
import com.example.chatterapp.domain.model.UserDetails
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.util.Constants.extractData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val chatterRepository: ChatterRepository,
    private val localUserManager: LocalUserManager
): ViewModel() {
    var userProfile by mutableStateOf<UserDetails?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    private var _updateAboutValue = mutableStateOf<String>("")
    val updateAboutValue:State<String> = _updateAboutValue

    init {
        getUserDetails()
    }

    fun onEvent(event: UserProfileEvent) {
        when(event) {
            is UserProfileEvent.UpdateAboutValue ->  {
                if(event.about.trim().length <= 100) {
                    _updateAboutValue.value = event.about
                }
            }

            is UserProfileEvent.UpdateAbout -> {
                viewModelScope.launch {
                    sendAbout()
                    _updateAboutValue.value = ""
                }
            }

            is UserProfileEvent.LogOutUser -> {
                viewModelScope.launch {
                    localUserManager.logOutUser()
                    sideEffect = "User Logged Out"
                }
            }
        }
    }

    private suspend fun sendAbout() {
        if(_updateAboutValue.value.trim().isNotEmpty()) {
            try {
                isLoading = true

                val data = UpdateData(data = _updateAboutValue.value)

                val response = chatterRepository.updateUserAbout(updateData = data)

                if(response.isSuccessful) {
                    val message = extractData(response.body(),"message")
                    Log.d("sideEffect",message!!)
                    getUserDetails()
                } else {
                    val message = extractData(response.errorBody(),"message")
                    Log.d("sideEffect",message!!)
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
        } else {
            sideEffect = "data is Empty please enter some thing"
        }
    }

    fun getUserDetails() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = chatterRepository.getSelfDetails()

                if(response.isSuccessful) {
                    val user: UserDetails = response.body()!!
                    userProfile = user
                    Log.d("user",user.toString())
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


sealed class UserProfileEvent {
    data class UpdateAboutValue(val about:String): UserProfileEvent()

    object UpdateAbout: UserProfileEvent()

    object LogOutUser: UserProfileEvent()
}
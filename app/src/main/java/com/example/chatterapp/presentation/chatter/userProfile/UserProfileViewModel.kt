package com.example.chatterapp.presentation.chatter.userProfile

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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
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

    var updatePasswordValue by mutableStateOf("")
        private set

    var updatePasswordError by mutableStateOf<Boolean>(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isUploading by mutableStateOf(false)
        private set

    var sideEffect by mutableStateOf<String?>(null)
        private set

    private var _updateAboutValue = mutableStateOf<String>("")
    val updateAboutValue:State<String> = _updateAboutValue

    private var _updateUserName = mutableStateOf<String>("")
    val updateUserName:State<String> = _updateUserName


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

            is UserProfileEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            UserProfileEvent.UpdateUserName -> {
               viewModelScope.launch {
                   sendUserName()
                   _updateUserName.value = ""
               }
            }
            is UserProfileEvent.UpdateUserNameValue -> {
                if(event.username.trim().length <= 20){
                    _updateUserName.value = event.username.trim()
                }
            }

            UserProfileEvent.UpdatePassword -> {
                viewModelScope.launch {
                    updatePassword()
                    updatePasswordValue = ""
                }
            }
            is UserProfileEvent.UpdatePasswordValue -> {
                if(!isStrongPassword(event.password)) {
                    updatePasswordError = true
                } else {
                    updatePasswordError = false
                }
                updatePasswordValue = event.password
            }
        }
    }

    private suspend fun sendAbout() {
        if(_updateAboutValue.value.trim().isNotEmpty()) {
            try {


                val data = UpdateData(data = _updateAboutValue.value)

                val response = chatterRepository.updateUserAbout(updateData = data)

                if(response.isSuccessful) {
                    sideEffect = extractData(response.body(),"message")

                    getUserDetails()
                } else {
                    sideEffect = extractData(response.errorBody(),"message")

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

        } else {
            sideEffect = "data is Empty please enter some thing"
        }
    }

    private suspend fun sendUserName() {
        if(_updateUserName.value.trim().isNotEmpty() && _updateUserName.value.length >= 6) {
            try {


                val data = UpdateData(data = _updateUserName.value)

                val response = chatterRepository.updateUserName(data = data)

                if(response.isSuccessful) {
                    sideEffect = extractData(response.body(),"message")

                    getUserDetails()
                } else {
                    sideEffect = extractData(response.errorBody(),"message")

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

        } else {
            sideEffect = "Username should contains at least 6 characters"
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

    fun uploadImage(file: File) {
        viewModelScope.launch {
            isUploading = true
            try {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file",file.name, requestFile )

                val  response = chatterRepository.updateUserProfile(body)

                if(response.isSuccessful) {
                     sideEffect = extractData(response.body(), "message")
                    getUserDetails()
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

            isUploading = false
        }
    }

    private suspend fun updatePassword() {
        try {
            val data = UpdateData(data = updatePasswordValue)
            val response = chatterRepository.updatePassword(data)
            if(response.isSuccessful) {
                sideEffect = extractData(response.body(),"message")
            } else {
                sideEffect = extractData(response.body(), "message")
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
    }


    private fun isStrongPassword(password: String): Boolean{
        // Check minimum length
        if (password.length < 8) {
            return false
        }

        if (!password.any { it.isUpperCase() }) {
            return false
        }

        // Check for at least one lowercase letter
        if (!password.any { it.isLowerCase() }) {
            return false
        }

        // Check for at least one digit
        if (!password.any { it.isDigit() }) {
            return false
        }

        // Check for at least one special character
        val specialCharacters = "!@#$%^&*(),.?\":{}|<>"
        if (!password.any { it in specialCharacters }) {
            return false
        }

        return true
    }


}



sealed class UserProfileEvent {
    data class UpdateAboutValue(val about:String): UserProfileEvent()
    data class UpdateUserNameValue(val username: String): UserProfileEvent()
    object UpdateAbout: UserProfileEvent()
    object UpdateUserName: UserProfileEvent()

    data class UpdatePasswordValue(val password: String): UserProfileEvent()
    object UpdatePassword: UserProfileEvent()

    object LogOutUser: UserProfileEvent()

    object RemoveSideEffect: UserProfileEvent()
}


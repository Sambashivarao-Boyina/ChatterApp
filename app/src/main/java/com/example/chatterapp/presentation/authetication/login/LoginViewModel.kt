package com.example.chatterapp.presentation.authetication.login

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.data.remote.ChatterApi
import com.example.chatterapp.data.remote.Dto.GoogleAuth
import com.example.chatterapp.data.remote.Dto.LoginUser
import com.example.chatterapp.data.remote.Dto.UpdateData
import com.example.chatterapp.domain.manager.LocalUserManager
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.presentation.authetication.components.AuthResponse
import com.example.chatterapp.util.Constants.extractData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localUserManager: LocalUserManager,
    private val chatterRepository: ChatterRepository
): ViewModel() {
    private val _state = mutableStateOf(LoginState())
    val state:State<LoginState> = _state

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf<Boolean>(false)
        private set

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailUpdate -> {
                _state.value = _state.value.copy(email = event.email)
                _state.value = _state.value.copy(isEmailError = false)
            }
            is LoginEvent.PasswordUpdate -> {
                _state.value = _state.value.copy(password = event.password)
                _state.value = _state.value.copy(isPasswordError = false)
            }
            LoginEvent.Login -> {
                viewModelScope.launch {
                    loginUser()
                }
            }
            LoginEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            is LoginEvent.GoogleAuth -> {
                googleAuth(email = event.email, username = event.username)
            }

            is LoginEvent.EmailErrorUpdate -> {
                _state.value = _state.value.copy(isEmailError = event.isEmailError)
            }
            is LoginEvent.PasswordErrorUpdate -> {
                _state.value = _state.value.copy(isPasswordError = event.isPasswordError)
            }
        }
    }

    private suspend fun loginUser() {
        if(validateData()) {
            val user = LoginUser(
                email = _state.value.email,
                password = _state.value.password
            )
            isLoading = true
            try {
                val response = chatterRepository.loginUser(user = user)
                if(response.isSuccessful) {
                    val authResponse: AuthResponse? = response.body()

                    if(authResponse!= null && authResponse.token.isNotEmpty()) {
                        localUserManager.saveUserToken(token = authResponse.token)
                        localUserManager.saveUserAuth()
                        sideEffect = authResponse.message
                        _state.value = LoginState()
                    } else {
                        sideEffect = "Some thing went wrong Please try again"
                    }
                } else {
                    val message = extractData(response.errorBody(),"message")
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

    private fun googleAuth(email: String, username: String) {
        viewModelScope.launch {
            try {
                val response = chatterRepository.googleAuth(data = GoogleAuth(
                    email = email,
                    username = username
                ))
                if(response.isSuccessful) {
                    val authResponse: AuthResponse? = response.body()

                    if(authResponse!= null && authResponse.token.isNotEmpty()) {
                        localUserManager.saveUserToken(token = authResponse.token)
                        localUserManager.saveUserAuth()
                        sideEffect = authResponse.message
                        _state.value = LoginState()
                    } else {
                        sideEffect = "Some thing went wrong Please try again"
                    }

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


    private fun validateData():Boolean {
        if(!isValidEmail(_state.value.email)) {
            onEvent(LoginEvent.EmailErrorUpdate(true))
            sideEffect = "Enter a valid Email"
            return false
        } else if(_state.value.password.isEmpty()) {
            onEvent(LoginEvent.PasswordErrorUpdate(true))
            sideEffect = "Enter Password"
            return false
        }
        return true
    }

    fun isValidEmail(email: String): Boolean {
        // Regular expression for validating an email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        // Match the input string against the regex
        return emailRegex.matches(email)
    }
}


data class LoginState (
    val email:String = "",
    val isEmailError:Boolean = false,
    val password: String = "",
    val isPasswordError:Boolean = false,
)

sealed class LoginEvent {
    data class EmailUpdate(val email:String): LoginEvent()
    data class PasswordUpdate(val password: String): LoginEvent()

    data class GoogleAuth(val email: String, val username: String): LoginEvent()

    data class EmailErrorUpdate(val isEmailError:Boolean) : LoginEvent()
    data class PasswordErrorUpdate(val isPasswordError:Boolean) : LoginEvent()

    object RemoveSideEffect: LoginEvent()
    object Login : LoginEvent()
}
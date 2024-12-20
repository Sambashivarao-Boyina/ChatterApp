package com.example.chatterapp.presentation.authetication.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.data.remote.ChatterApi
import com.example.chatterapp.data.remote.Dto.LoginUser
import com.example.chatterapp.domain.manager.LocalUserManager
import com.example.chatterapp.presentation.authetication.components.AuthResponse
import com.example.chatterapp.presentation.authetication.signup.SignUpEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localUserManager: LocalUserManager,
    private val chatterApi: ChatterApi
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
                val response = chatterApi.loginUser(user = user)
                if(response.isSuccessful) {
                    val authResponse: AuthResponse? = response.body()

                    if(authResponse!= null && authResponse.token.isNotEmpty()) {
                        localUserManager.saveUserTokne(token = authResponse.token)
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

    // Helper function to extract token from the response body
    private fun extractData(responseBody: ResponseBody?,element:String): String? {
        responseBody?.let {
            val responseJson = it.string()
            // Parse the JSON and extract token
            val jsonObject = JSONObject(responseJson)
            return jsonObject.optString(element, null)
        }
        return null
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

    data class EmailErrorUpdate(val isEmailError:Boolean) : LoginEvent()
    data class PasswordErrorUpdate(val isPasswordError:Boolean) : LoginEvent()

    object RemoveSideEffect: LoginEvent()
    object Login : LoginEvent()
}
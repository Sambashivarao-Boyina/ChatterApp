package com.example.chatterapp.presentation.authetication.signup

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.data.remote.ChatterApi
import com.example.chatterapp.data.remote.Dto.SignUpUser
import com.example.chatterapp.domain.manager.LocalUserManager
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.presentation.authetication.components.AuthResponse
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
class SignUpViewModel @Inject constructor(
    private val localUserManager: LocalUserManager,
    private val chatterRepository: ChatterRepository
) : ViewModel() {

    private val _state = mutableStateOf(SignupState())
    val state: State<SignupState> = _state

    var sideEffect by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf<Boolean>(false)
        private set

    fun onEvent(event: SignUpEvent) {
        when(event) {
            is SignUpEvent.ConfirmPasswordUpdate -> {
                _state.value = _state.value.copy(confirmPassword = event.confirmPassword)
                _state.value = _state.value.copy(isConfirmPasswordError = false)
            }
            is SignUpEvent.EmailUpdate -> {
                _state.value = _state.value.copy(email = event.email)
                _state.value = _state.value.copy(isEmailError = false)
            }
            is SignUpEvent.PasswordUpdate -> {
                _state.value = _state.value.copy(password = event.password)
                _state.value = _state.value.copy(isPasswordError = false)

            }
            is SignUpEvent.UsernameUpdate -> {
                _state.value = _state.value.copy(username = event.username)
                _state.value = _state.value.copy(isUserNameError = false)

            }
            SignUpEvent.SignUp -> {
                viewModelScope.launch {
                    signupUser()
                }
            }
            SignUpEvent.RemoveSideEffect -> {
                sideEffect = null
            }

            is SignUpEvent.ConfirmPasswordErrorUpdate -> {
                _state.value = _state.value.copy(isConfirmPasswordError = event.isConfirmPasswordError)
            }
            is SignUpEvent.EmailErrorUpdate -> {
                _state.value = _state.value.copy(isEmailError = event.isEmailError)
            }
            is SignUpEvent.PasswordErrorUpdate -> {
                _state.value = _state.value.copy(isPasswordError = event.isPasswordError)
            }
            is SignUpEvent.UsernameErrorUpdate -> {
                _state.value = _state.value.copy(isUserNameError = event.isUserNameError)
            }
        }
    }

    private suspend fun signupUser() {
        if(validateDate()) {
            isLoading = true
            try {
                val signUpUser = SignUpUser(
                    username = _state.value.username,
                    email = _state.value.email,
                    password = _state.value.password
                )
                val response = chatterRepository.signupUser(user = signUpUser)

                if(response.isSuccessful) {
                    val authResponse: AuthResponse? = response.body()
                    if(authResponse != null && authResponse.token.isNotEmpty()) {
                        localUserManager.saveUserTokne(token = authResponse.token)
                        localUserManager.saveUserAuth()

                        _state.value = SignupState()
                        sideEffect = authResponse.message
                    } else {
                        sideEffect = "Some went Wrong please try again"
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


    private fun validateDate(): Boolean {
        if(!isValidEmail(_state.value.email)) {
            onEvent(SignUpEvent.EmailErrorUpdate(true))
            sideEffect = "Enter a Valid Email"
            return false
        } else if(_state.value.username.isEmpty()) {
            onEvent(SignUpEvent.UsernameErrorUpdate(true))
            sideEffect = "Enter Username"
            return false
        } else if(_state.value.username.length < 6) {
            onEvent(SignUpEvent.UsernameErrorUpdate(true))
            sideEffect = "Username should contain at least 6 characters"
            return false
        }else if(!isStrongPassword(_state.value.password)) {
            onEvent(SignUpEvent.PasswordErrorUpdate(true))
            return false
        } else if(_state.value.confirmPassword.isEmpty()) {
            onEvent(SignUpEvent.ConfirmPasswordErrorUpdate(true))
            sideEffect = "Enter Confirm Password"
            return false
        } else if(!_state.value.password.equals(_state.value.confirmPassword)) {
            onEvent(SignUpEvent.ConfirmPasswordErrorUpdate(true))
            sideEffect = "Password and Confirm Password should Match"
            return false
        }
        return true
    }

    private fun isStrongPassword(password: String): Boolean{
        // Check minimum length
        if (password.length < 8) {
            sideEffect = "Password must be at least 8 characters long."
            return false
        }


        if (!password.any { it.isUpperCase() }) {
            sideEffect = "Password must contain at least one uppercase letter."
            return false
        }

        // Check for at least one lowercase letter
        if (!password.any { it.isLowerCase() }) {
            sideEffect =  "Password must contain at least one lowercase letter."
            return false
        }

        // Check for at least one digit
        if (!password.any { it.isDigit() }) {
            sideEffect = "Password must contain at least one numeric digit."
            return false
        }

        // Check for at least one special character
        val specialCharacters = "!@#$%^&*(),.?\":{}|<>"
        if (!password.any { it in specialCharacters }) {
            sideEffect = "Password must contain at least one special character."
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

data class SignupState (
    val username: String = "",
    val isUserNameError:Boolean = false,
    val email: String = "",
    val isEmailError:Boolean = false,
    val password: String = "",
    val isPasswordError:Boolean = false,
    val confirmPassword: String = "",
    val isConfirmPasswordError:Boolean = false
)

sealed class SignUpEvent {
    data class UsernameUpdate(val username:String) : SignUpEvent()
    data class EmailUpdate(val email:String) : SignUpEvent()
    data class PasswordUpdate(val password:String) : SignUpEvent()
    data class ConfirmPasswordUpdate(val confirmPassword:String) : SignUpEvent()

    data class UsernameErrorUpdate(val isUserNameError:Boolean) : SignUpEvent()
    data class EmailErrorUpdate(val isEmailError:Boolean) : SignUpEvent()
    data class PasswordErrorUpdate(val isPasswordError:Boolean) : SignUpEvent()
    data class ConfirmPasswordErrorUpdate(val isConfirmPasswordError:Boolean) : SignUpEvent()

    object RemoveSideEffect: SignUpEvent()
    object SignUp: SignUpEvent()
}
package com.example.chatterapp.presentation.navGraph.auth

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatterapp.presentation.authetication.login.LoginEvent
import com.example.chatterapp.presentation.authetication.login.LoginScreen
import com.example.chatterapp.presentation.authetication.login.LoginViewModel
import com.example.chatterapp.presentation.authetication.signup.SignUpEvent
import com.example.chatterapp.presentation.authetication.signup.SignUpScreen
import com.example.chatterapp.presentation.authetication.signup.SignUpViewModel
import com.example.chatterapp.presentation.navGraph.Route

@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.LoginScreen.route) {
        composable(route = Route.LoginScreen.route) {
            val loginViewModel:LoginViewModel = hiltViewModel()
            val state = loginViewModel.state.value
            val isLoading = loginViewModel.isLoading

            if(loginViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    loginViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                loginViewModel.onEvent(LoginEvent.RemoveSideEffect)
            }

            LoginScreen(navController = navController, state = state, onEvent = loginViewModel::onEvent, isLoading = isLoading)
        }

        composable(route = Route.SignupScreen.route) {
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            val state = signUpViewModel.state.value
            val isLoading = signUpViewModel.isLoading

            if(signUpViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    signUpViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                signUpViewModel.onEvent(SignUpEvent.RemoveSideEffect)
            }

            SignUpScreen(navController = navController, state = state, onEvent = signUpViewModel::onEvent, isLoading = isLoading)
        }
    }
}
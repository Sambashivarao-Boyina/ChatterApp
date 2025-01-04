package com.example.chatterapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.domain.manager.LocalUserManager
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.util.Constants.extractData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localUserManager: LocalUserManager,
    private val chatterRepository: ChatterRepository
):ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Route.AppStartScreen.route)

    init {
        localUserManager.readUserEntryAndAuth().onEach { states ->
            val onBoarding = states.first
            val userAuth = states.second
            val oldToken = localUserManager.readUserToken().firstOrNull()
            if(onBoarding && userAuth) {
                viewModelScope.launch {
                    try {
                        val response = chatterRepository.refreshUserToken()
                        if(response.isSuccessful) {
                            val token = extractData(response.body(),"token")
                            if(token != null) {
                                localUserManager.saveUserToken(token)
                                startDestination = Route.ChatApp.route
                                splashCondition = false
                            } else {
                                startDestination = Route.AuthRoutes.route
                                splashCondition = false
                            }
                        } else {
                            localUserManager.logOutUser()
                            startDestination = Route.AuthRoutes.route
                            splashCondition = false
                        }
                    }catch (e: Exception) {
                        startDestination = Route.AuthRoutes.route
                        splashCondition = false
                    }
                }
            }else if(!onBoarding) {
                startDestination = Route.AppStartScreen.route
                splashCondition = false
            } else{
                startDestination = Route.AuthRoutes.route
                splashCondition = false
            }
//            delay(1000)

        }.launchIn(viewModelScope)

    }
}
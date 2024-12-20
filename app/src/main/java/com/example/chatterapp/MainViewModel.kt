package com.example.chatterapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterapp.domain.manager.LocalUserManager
import com.example.chatterapp.presentation.navGraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localUserManager: LocalUserManager
):ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Route.AppStartScreen.route)

    init {
        localUserManager.readUserEntryAndAuth().onEach { states ->
            val onBoarding = states.first
            val userAuth = states.second
            if(onBoarding && userAuth) {
                startDestination = Route.ChatApp.route
            }else if(!onBoarding) {
                startDestination = Route.AppStartScreen.route
            } else{
                startDestination = Route.AuthRoutes.route
            }
            delay(300)
            splashCondition = false
        }.launchIn(viewModelScope)

    }
}
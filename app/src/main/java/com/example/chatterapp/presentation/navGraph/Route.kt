package com.example.chatterapp.presentation.navGraph

sealed class Route(val route:String) {
    object AppStartScreen: Route("appStartScreen")
    object OnBoardingScreen: Route("onBoardingScreen")

    object ChatApp: Route("chatApp")
    object HomeScreen: Route("homescreen")
}
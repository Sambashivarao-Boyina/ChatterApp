package com.example.chatterapp.presentation.navGraph

sealed class Route(val route:String) {
    object AppStartScreen: Route("appStartScreen")
    object OnBoardingScreen: Route("onBoardingScreen")

    object AuthRoutes: Route("appAuthRoute")
    object AuthNavigatorScreen: Route("authNavigatorScreen")

    object LoginScreen: Route("loginScreen")
    object SignupScreen: Route("signupScreen")

    object ChatApp: Route("chatApp")
    object ChatAppNavigator: Route("chatAppNavigatorScreen")

    object HomeScreen: Route("homeScreen")
    object UserProfile: Route("userProfileScreen")
    object SearchFriend: Route("searchFriend")

    object AboutScreen: Route("aboutScreen")
}
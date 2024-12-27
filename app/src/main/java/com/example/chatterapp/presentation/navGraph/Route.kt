package com.example.chatterapp.presentation.navGraph

import com.example.chatterapp.util.Constants.FRIEND_ID
import com.example.chatterapp.util.Constants.USER_ID

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

    object SendedRequest : Route("sendedRequests")
    object ReceivedRequest: Route("receivedRequests")

    object User:Route("userScreen/{${USER_ID}}") {
        fun passUserId(id: String): String {
            return this.route.replace(oldValue = "{${USER_ID}}", newValue = id)
        }
    }

    object Chat:Route("chatScreen/{${FRIEND_ID}}") {
        fun passChatId(id: String): String {
            return this.route.replace(oldValue = "{${FRIEND_ID}}", newValue = id)
        }
    }

}


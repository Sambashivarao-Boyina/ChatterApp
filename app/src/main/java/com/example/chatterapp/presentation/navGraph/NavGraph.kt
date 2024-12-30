package com.example.chatterapp.presentation.navGraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.chatterapp.presentation.authetication.AuthNavigatorScreen
import com.example.chatterapp.presentation.chatter.ChatterNavigator
import com.example.chatterapp.presentation.chatter.ChatterNavigatorViewModel
import com.example.chatterapp.presentation.onBoarding.OnBoardingScreen
import com.example.chatterapp.presentation.onBoarding.OnBoardingViewModel

@Composable
fun NavGraph(
    startDestination:String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartScreen.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(onEvent = onBoardingViewModel::onEvent)
            }
        }

        navigation(
            route = Route.AuthRoutes.route,
            startDestination = Route.AuthNavigatorScreen.route
        ) {
            composable(route =  Route.AuthNavigatorScreen.route) {
                AuthNavigatorScreen()
            }
        }

        navigation(
            route = Route.ChatApp.route,
            startDestination = Route.ChatAppNavigator.route
        ) {
            composable(route = Route.ChatAppNavigator.route) {
                val chatterNavigatorViewModel: ChatterNavigatorViewModel = hiltViewModel()
                ChatterNavigator(
                    chatterNavigatorViewModel = chatterNavigatorViewModel
                )
            }
        }
    }
}
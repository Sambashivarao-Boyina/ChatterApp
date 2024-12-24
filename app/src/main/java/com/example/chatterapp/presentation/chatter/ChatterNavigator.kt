package com.example.chatterapp.presentation.chatter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatterapp.presentation.chatter.components.BottomNavItem
import com.example.chatterapp.presentation.chatter.components.BottomNavition
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.presentation.navGraph.chatter.ChatterGraph
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue

@Composable
fun ChatterNavigator() {
    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value


    val isBottomBarVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == Route.HomeScreen.route ||
                backStackState?.destination?.route == Route.SearchFriend.route ||
                backStackState?.destination?.route == Route.UserProfile.route
    }
    Scaffold(
        bottomBar = {
            if(isBottomBarVisible) {
                BottomNavition(
                    navItems = listOf(BottomNavItem.Home, BottomNavItem.Chat, BottomNavItem.Settings),
                    navController = navController
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Black
                )
                .padding(it)
        ) {
            ChatterGraph(
                navController = navController
            )
        }
    }

}
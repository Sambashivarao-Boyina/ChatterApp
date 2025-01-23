package com.example.chatterapp.presentation.chatter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatterapp.MainActivity
import com.example.chatterapp.domain.model.UserDetails
import com.example.chatterapp.presentation.chatter.components.BottomNavItem
import com.example.chatterapp.presentation.chatter.components.BottomNavition
import com.example.chatterapp.presentation.chatter.components.TopBar
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.presentation.navGraph.chatter.ChatterGraph
import com.example.chatterapp.ui.theme.Black

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatterNavigator(
    userDetails: UserDetails?,
    chatterNavigatorViewModel: ChatterNavigatorViewModel
) {
    val scope = rememberCoroutineScope()
    DisposableEffect(Unit) {
        onDispose {
            chatterNavigatorViewModel.onCleared()
        }
    }


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
                    navController = navController,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        },

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Black
                )


        ) {

            ChatterGraph(
                navController = navController
            )
        }
    }

}


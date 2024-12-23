package com.example.chatterapp.presentation.chatter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.rememberNavController
import com.example.chatterapp.presentation.chatter.components.BottomNavItem
import com.example.chatterapp.presentation.chatter.components.BottomNavition
import com.example.chatterapp.presentation.navGraph.chatter.ChatterGraph
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.Purple

@Composable
fun ChatterNavigator() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavition(
                navItems = listOf(BottomNavItem.Home, BottomNavItem.Chat, BottomNavItem.Settings),
                navController = navController
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Black,
                            Black,
                            Black,
                            Black,
                            Blue.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(it)
        ) {
            ChatterGraph(
                navController = navController
            )
        }
    }

}
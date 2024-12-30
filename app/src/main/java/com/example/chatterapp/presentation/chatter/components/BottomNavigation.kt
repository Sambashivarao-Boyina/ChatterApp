package com.example.chatterapp.presentation.chatter.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatterapp.R
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.ChatterAppTheme
import com.example.chatterapp.ui.theme.Gray


@Composable
fun BottomNavition(
    navItems: List<BottomNavItem>,
    navController: NavHostController,
    modifier: Modifier
) {

    val backStack = navController.currentBackStackEntryAsState().value
    var currentRoute = remember(key1 = backStack) {
        backStack?.destination?.route
    }
    Row(
        modifier = modifier
            .background(color = Black)
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(70.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF222222),
                            Color(0xFF252525)
                        )
                    )
                ),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {


            navItems.forEach { item ->
                NavigationBarItem(
                    selected = item.route == currentRoute,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { homeScreen ->
                                popUpTo(homeScreen) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(painter = painterResource(item.icon), contentDescription = null, modifier = Modifier.size(30.dp))
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = Blue,
                        selectedTextColor = Blue,
                        unselectedTextColor = Gray,
                        unselectedIconColor = Gray,
                        selectedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }

}


@Composable
@Preview(showBackground = true)
fun BottomNavigationPreview() {
    ChatterAppTheme {
        BottomNavition(
            navController = rememberNavController(),
            navItems = listOf(BottomNavItem.Home, BottomNavItem.Chat, BottomNavItem.Settings),
            modifier = Modifier
        )
    }
}





sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object Home : BottomNavItem(
        Route.HomeScreen.route,
        R.drawable.message,
        "Messages"
    )

    object Chat : BottomNavItem(
        Route.SearchFriend.route,
        R.drawable.add,
        "Add"
    )

    object Settings : BottomNavItem(
        Route.UserProfile.route,
        R.drawable.profile,
        "Profile"
    )
}




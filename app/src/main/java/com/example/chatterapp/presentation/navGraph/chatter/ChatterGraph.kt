package com.example.chatterapp.presentation.navGraph.chatter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatterapp.presentation.chatter.addfriend.AddFriendScreen
import com.example.chatterapp.presentation.chatter.addfriend.AddFriendViewModel
import com.example.chatterapp.presentation.chatter.home.HomeScreen
import com.example.chatterapp.presentation.chatter.home.HomeViewModel
import com.example.chatterapp.presentation.chatter.userProfile.UserProfileScreen
import com.example.chatterapp.presentation.chatter.userProfile.UserProfileViewModel
import com.example.chatterapp.presentation.navGraph.Route
import kotlinx.coroutines.launch

@Composable
fun ChatterGraph(
    navController: NavHostController
) {

    NavHost(navController = navController, startDestination = Route.HomeScreen.route) {
        composable(route = Route.HomeScreen.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val friends = homeViewModel.friends.value
            val scope = rememberCoroutineScope()
            HomeScreen(
                friends = friends,
                isRefershing = homeViewModel.isLoading,
                refreshData = homeViewModel::getFriends
            )
        }

        composable(route = Route.SearchFriend.route) {
            val addFriendViewModel: AddFriendViewModel = hiltViewModel()
            val users = addFriendViewModel.users.value
            AddFriendScreen(
                users = users,
                onEvent = addFriendViewModel::onEvent,
                isLoading = addFriendViewModel.isLoading,
                refreshData = addFriendViewModel::getUsers
            )
        }

        composable(route = Route.UserProfile.route) {
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            val userDetails = userProfileViewModel.userProfile

            UserProfileScreen(
                userDetails = userDetails,
                isLoading = userProfileViewModel.isLoading,
                refershData = userProfileViewModel::getUserDetails,
                onEvent = userProfileViewModel::onEvent,
                updateAboutValue = userProfileViewModel.updateAboutValue.value
            )
        }
    }
}
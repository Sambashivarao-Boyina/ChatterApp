package com.example.chatterapp.presentation.navGraph.chatter

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chatterapp.presentation.chatter.addfriend.AddFriendEvent
import com.example.chatterapp.presentation.chatter.addfriend.AddFriendScreen
import com.example.chatterapp.presentation.chatter.addfriend.AddFriendViewModel
import com.example.chatterapp.presentation.chatter.chat.ChatEvent
import com.example.chatterapp.presentation.chatter.chat.ChatScreen
import com.example.chatterapp.presentation.chatter.chat.ChatViewModel
import com.example.chatterapp.presentation.chatter.home.HomeEvent
import com.example.chatterapp.presentation.chatter.home.HomeScreen
import com.example.chatterapp.presentation.chatter.home.HomeViewModel
import com.example.chatterapp.presentation.chatter.received_requests.ReceivedRequestEvent
import com.example.chatterapp.presentation.chatter.received_requests.ReceivedRequestScreen
import com.example.chatterapp.presentation.chatter.received_requests.ReceivedRequestViewModel
import com.example.chatterapp.presentation.chatter.sended_requests.SendRequestEvent
import com.example.chatterapp.presentation.chatter.sended_requests.SendedRequestScreen
import com.example.chatterapp.presentation.chatter.sended_requests.SendedRequestViewModel
import com.example.chatterapp.presentation.chatter.user.UserEvent
import com.example.chatterapp.presentation.chatter.user.UserScreen
import com.example.chatterapp.presentation.chatter.user.UserViewModel
import com.example.chatterapp.presentation.chatter.userProfile.UserProfileEvent
import com.example.chatterapp.presentation.chatter.userProfile.UserProfileScreen
import com.example.chatterapp.presentation.chatter.userProfile.UserProfileViewModel
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.util.Constants.FRIEND_ID
import com.example.chatterapp.util.Constants.USER_ID

@Composable
fun ChatterGraph(
    navController: NavHostController
) {

    NavHost(navController = navController, startDestination = Route.HomeScreen.route) {
        composable(route = Route.HomeScreen.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val friends = homeViewModel.friends.value
            val activeUsers = homeViewModel.activeUsers.collectAsState()

            if (homeViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    homeViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                homeViewModel.onEvent(HomeEvent.RemoveSideEffect)
            }

            HomeScreen(
                friends = friends,
                isRefershing = homeViewModel.isLoading,
                refreshData = homeViewModel::getFriends,
                searchValue = homeViewModel.searchValue,
                onEvent = homeViewModel::onEvent,
                navController = navController,
                activeUsers = activeUsers
            )
        }

        composable(route = Route.SearchFriend.route) {
            val addFriendViewModel: AddFriendViewModel = hiltViewModel()
            val users = addFriendViewModel.users

            if (addFriendViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    addFriendViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                addFriendViewModel.onEvent(AddFriendEvent.RemoveSideEffect)
            }


            AddFriendScreen(
                users = users,
                onEvent = addFriendViewModel::onEvent,
                isLoading = addFriendViewModel.isLoading,
                refreshData = addFriendViewModel::getUsers,
                navController = navController,
                searchValue = addFriendViewModel.searchValue
            )
        }

        composable(route = Route.UserProfile.route) {
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            val userDetails = userProfileViewModel.userProfile


            if (userProfileViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    userProfileViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userProfileViewModel.onEvent(UserProfileEvent.RemoveSideEffect)
            }

            UserProfileScreen(
                userDetails = userDetails,
                isRefreshing = userProfileViewModel.isLoading,
                refershData = userProfileViewModel::getUserDetails,
                onEvent = userProfileViewModel::onEvent,
                updateAboutValue = userProfileViewModel.updateAboutValue.value,
                navController = navController,
                uploadImage = userProfileViewModel::uploadImage,
                isUploading = userProfileViewModel.isUploading
            )
        }

        composable(route = Route.SendedRequest.route) {
            val sendedRequestViewModel: SendedRequestViewModel = hiltViewModel()

            if (sendedRequestViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    sendedRequestViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                sendedRequestViewModel.onEvent(SendRequestEvent.RemoveSideEffect)
            }

            SendedRequestScreen(
                navController = navController,
                requests = sendedRequestViewModel.sendedRequests
            )
        }

        composable(route = Route.ReceivedRequest.route) {
            val receivedRequestViewModel: ReceivedRequestViewModel = hiltViewModel()

            if (receivedRequestViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    receivedRequestViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                receivedRequestViewModel.onEvent(ReceivedRequestEvent.RemoveSideEffect)
            }

            ReceivedRequestScreen(
                navController = navController,
                requests = receivedRequestViewModel.receivededRequests,
                onEvent = receivedRequestViewModel::onEvent
            )
        }

        composable(
            route = Route.User.route,
            arguments = listOf(navArgument(USER_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(USER_ID) ?: return@composable
            val userViewModel: UserViewModel = hiltViewModel()
            val user = userViewModel.user

            if (userViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    userViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                userViewModel.onEvent(UserEvent.RemoveSideEffect)
            }

            LaunchedEffect(Unit) {
                userViewModel.getUser(id = userId)
            }
            UserScreen(user = user, navController = navController)
        }

        composable(
            route = Route.Chat.route,
            arguments = listOf(navArgument(FRIEND_ID){ type = NavType.StringType })
        ) { backStackEntry ->
            val friendId = backStackEntry.arguments?.getString(FRIEND_ID) ?: return@composable
            val chatViewModel: ChatViewModel = hiltViewModel()
            val activeUser = chatViewModel.activeUsers.collectAsState()

            if (chatViewModel.sideEffect != null) {
                Toast.makeText(
                    LocalContext.current,
                    chatViewModel.sideEffect,
                    Toast.LENGTH_SHORT
                ).show()
                chatViewModel.onEvent(ChatEvent.RemoveSideEffect)
            }

            LaunchedEffect(Unit) {
                chatViewModel.setFriendId(friendId)
            }

            ChatScreen(
                friend = chatViewModel.friend,
                message = chatViewModel.message,
                navController = navController,
                onEvent = chatViewModel::onEvent,
                chat = chatViewModel.chat,
                activeUsers = activeUser,
                sendImage = chatViewModel::sendImage,
                imageUploading = chatViewModel.isLoading
            )


        }
    }
}


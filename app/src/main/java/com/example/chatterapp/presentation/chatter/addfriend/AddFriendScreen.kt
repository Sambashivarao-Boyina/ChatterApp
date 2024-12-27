package com.example.chatterapp.presentation.chatter.addfriend

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.chatterapp.domain.model.User
import com.example.chatterapp.presentation.chatter.components.AddRequestCard
import com.example.chatterapp.presentation.chatter.components.EmptyList
import com.example.chatterapp.presentation.chatter.components.FriendCard
import com.example.chatterapp.presentation.chatter.components.SearchBox
import com.example.chatterapp.presentation.chatter.components.TopBar
import com.example.chatterapp.presentation.chatter.home.HomeEvent
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.Gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    users: List<User>,
    isLoading: Boolean,
    refreshData: () -> Unit,
    onEvent: (AddFriendEvent) -> Unit,
    navController: NavHostController,
    searchValue: String
) {
    val pullToRefreshState = rememberPullToRefreshState()
    Scaffold(
        topBar = {
            TopBar(
                title = "Add Friend"
            )
        },
        containerColor = Black
    ) {
        Box(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)

            ) {
                item {
                    SearchBox(
                        value = searchValue,
                        onChange = {
                            onEvent(AddFriendEvent.UpdateSearchValue(it))
                        },
                        onSearch = {
                            onEvent(AddFriendEvent.Search)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
                if(users.isEmpty()) {
                    item {
                        EmptyList()
                    }
                }
                items(users) { user ->
                    AddRequestCard(
                        user = user,
                        sendRequest = {
                            onEvent(AddFriendEvent.SendRequest(user._id))
                        },
                        onClick = {
                            navController.navigate(Route.User.passUserId(id = user._id))
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            if (pullToRefreshState.isRefreshing) {
                LaunchedEffect(true) {
                    refreshData()
                }
            }

            LaunchedEffect(isLoading) {
                if (isLoading) {
                    pullToRefreshState.startRefresh()
                } else {
                    pullToRefreshState.endRefresh()
                }
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = Blue,
                containerColor = if(pullToRefreshState.isRefreshing) {
                    PullToRefreshDefaults.containerColor
                } else {
                    Color.Transparent
                }

            )
        }
    }




}
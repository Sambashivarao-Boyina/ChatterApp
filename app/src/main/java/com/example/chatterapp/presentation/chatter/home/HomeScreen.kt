package com.example.chatterapp.presentation.chatter.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.Friend
import com.example.chatterapp.presentation.chatter.components.EmptyList
import com.example.chatterapp.presentation.chatter.components.FriendCard
import com.example.chatterapp.presentation.chatter.components.SearchBox
import com.example.chatterapp.presentation.chatter.components.TopBar
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    friends: List<Friend>,
    isRefershing: Boolean,
    refreshData: () -> Unit,
    searchValue: String,
    onEvent: (HomeEvent) -> Unit,
    navController: NavHostController,
    activeUsers: State<List<String>>
) {




    Scaffold(
        topBar = {
            TopBar(
                title = "${stringResource(R.string.app_name)}"
            )
        },
        containerColor = Black
    ) {
        val pullToRefreshState = rememberPullToRefreshState()

        Box(
            modifier = Modifier
                .padding(it)
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
                            onEvent(HomeEvent.UpdateSearchValue(it))
                        },
                        onSearch = {
                            onEvent(HomeEvent.Search)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (friends.isEmpty()) {
                    item {
                        EmptyList()
                    }
                }

                items(friends) { friend ->
                    FriendCard(
                        friend = friend, onClick = {
                            navController.navigate(Route.Chat.passChatId(friend._id))
                        },
                        isOnline = activeUsers.value.contains(friend.person._id)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            if (pullToRefreshState.isRefreshing) {
                LaunchedEffect(true) {
                    refreshData()
                }
            }

            LaunchedEffect(isRefershing) {
                if (isRefershing) {
                    pullToRefreshState.startRefresh()
                } else {
                    pullToRefreshState.endRefresh()
                }
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = Blue,
                containerColor = if (pullToRefreshState.isRefreshing) {
                    PullToRefreshDefaults.containerColor
                } else {
                    Color.Transparent
                }

            )
        }
    }

}


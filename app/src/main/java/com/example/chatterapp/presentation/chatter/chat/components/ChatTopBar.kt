package com.example.chatterapp.presentation.chatter.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.Chat
import com.example.chatterapp.domain.model.Friend
import com.example.chatterapp.presentation.chatter.chat.ChatEvent
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.DarkGray
import com.example.chatterapp.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    friend: Friend,
    chat: Chat?,
    onBackClick: () -> Unit,
    isOnline: Boolean,
    onEvent: (ChatEvent) -> Unit,
    navigateToProfie: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackClick()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    navigateToProfie()
                }
            ) {
                if (friend.person.userProfile == null) {
                    Image(
                        painter = painterResource(R.drawable.default_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(friend.person.userProfile)
                            .placeholder(R.drawable.default_profile)
                            .error(R.drawable.default_profile)
                            .transformations(CircleCropTransformation())
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = friend.person.username,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    if (isOnline) {
                        Text(
                            text = "online",
                            color = Blue,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                ) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(color = LightGray)
                    ) {
                        chat?.let {
                            if (chat.blockedBy == null) {
                                DropdownMenuItem(
                                    text = { Text("Block") },
                                    onClick = {
                                        onEvent(ChatEvent.BlockFriend)
                                    }
                                )
                            } else if (chat.blockedBy._id != friend.person._id) {
                                DropdownMenuItem(
                                    text = { Text("UnBlock") },
                                    onClick = {
                                        onEvent(ChatEvent.UnBlockFriend)
                                    }
                                )
                            }
                        }
                        DropdownMenuItem(
                            text = { Text("Refresh") },
                            onClick = {
                                onEvent(ChatEvent.RefershData)
                            }
                        )
                    }
                }


            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkGray,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        modifier = Modifier.height(100.dp)
    )

}


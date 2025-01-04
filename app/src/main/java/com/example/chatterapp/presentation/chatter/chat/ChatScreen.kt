package com.example.chatterapp.presentation.chatter.chat

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.Chat
import com.example.chatterapp.domain.model.Friend
import com.example.chatterapp.domain.model.Message
import com.example.chatterapp.presentation.chatter.chat.components.ChatTopBar
import com.example.chatterapp.presentation.chatter.chat.components.MessageBox
import com.example.chatterapp.presentation.chatter.components.BottomNavition
import com.example.chatterapp.presentation.chatter.components.UploadingIndicator
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.Gray
import com.example.chatterapp.ui.theme.LightGray
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    friend: Friend?,
    message: String,
    navController: NavHostController,
    chat: Chat?,
    onEvent: (ChatEvent) -> Unit,
    activeUsers: State<List<String>>,
    sendImage:(File)->Unit,
    imageUploading:Boolean
) {

    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {

                val file = getFileFromUri(context, uri)
                sendImage(file)

            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted ->
            permissionGranted = isGranted
            if(isGranted) {
                launcher.launch("image/*")
            } else {
                Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show()
            }
        }
    )



    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    chat?.let {
        LaunchedEffect(chat.messages.size) {
            if (chat.messages.isNotEmpty()) {
                listState.animateScrollToItem(chat.messages.size - 1)
            }
        }
    }

    friend?.let {
        Scaffold(
            topBar = {
                ChatTopBar(
                    friend = friend,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    isOnline = activeUsers.value.contains(friend.person._id),
                    onEvent = onEvent,
                    chat = chat,
                    navigateToProfie = {
                        navController.navigate(Route.User.passUserId(friend.person._id))
                    }
                )
            },
            containerColor = Black,
        ) {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize()
                    .imePadding()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    chat?.let {
                        items(chat.messages) { message ->
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (!message.sender.equals(friend.person._id)) {
                                    Spacer(Modifier.weight(1f))
                                }
                                MessageBox(
                                    message = message,
                                    backGroundColor = if (message.sender.equals(friend.person._id)) LightGray else Blue,
                                    navController = navController
                                )
                                if (message.sender.equals(friend.person._id)) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                        }
                        if(chat.blockedBy != null) {
                            item {
                                Text(
                                    text = "This chat has been blocked by ${chat.blockedBy.username}\n you cannot send message Now",
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(bottom = 20.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                chat?.let {
                    if(chat.blockedBy == null) {
                        Row(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                                .background(
                                    color = Color.Transparent
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = message,
                                onValueChange = {
                                    onEvent(ChatEvent.UpDateMessage(it))
                                },
                                placeholder = {
                                    Text(
                                        text = "Message..."
                                    )
                                },
                                leadingIcon = {
                                    IconButton(
                                        onClick = {
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                                launcher.launch("image/*")
                                            } else {
                                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.image),
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors().copy(
                                    focusedIndicatorColor = Blue,
                                    unfocusedIndicatorColor = Gray.copy(alpha = 0.2f),
                                    focusedContainerColor = LightGray,
                                    unfocusedTextColor = LightGray,
                                    cursorColor = Blue,
                                    unfocusedContainerColor = LightGray,
                                    unfocusedLeadingIconColor = Blue,
                                    focusedLeadingIconColor = Blue

                                ),
                                modifier = Modifier
                                    .weight(0.8f),
                                shape = RoundedCornerShape(50.dp)
                            )
                            Spacer(Modifier.weight(0.05f))
                            IconButton(
                                onClick = {
                                    onEvent(ChatEvent.SendMessage)
                                },
                                colors = IconButtonDefaults.iconButtonColors().copy(
                                    containerColor = Blue,
                                    disabledContainerColor = Blue,
                                    contentColor = LightGray,
                                    disabledContentColor = LightGray
                                ),
                                modifier = Modifier.size(50.dp),
                                enabled = message.trim().isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }



            }
        }
    }

    if(imageUploading) {
        UploadingIndicator()
    }
}

fun getFileFromUri(context: Context, uri: Uri): File {
    val contextResolver = context.contentResolver
    val inputStream = contextResolver.openInputStream(uri)
    val tempFile = File.createTempFile("${System.currentTimeMillis()}",".jpg",context.cacheDir)
    tempFile.outputStream().use { outputStream ->
        inputStream?.copyTo(outputStream)
    }

    return tempFile
}
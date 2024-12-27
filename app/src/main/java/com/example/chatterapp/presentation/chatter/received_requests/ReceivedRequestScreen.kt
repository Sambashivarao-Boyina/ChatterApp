package com.example.chatterapp.presentation.chatter.received_requests

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.FriendRequest
import com.example.chatterapp.presentation.chatter.components.RequestBox
import com.example.chatterapp.presentation.chatter.components.TopBar
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.Gray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceivedRequestScreen(
    requests: List<FriendRequest>,
    navController: NavHostController,
    onEvent: (ReceivedRequestEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Received Request",
                navigationBox = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(30.dp))
                    }
                }
            )
        },
        containerColor = Black
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {


            LazyColumn(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                items(requests) { request ->
                    Spacer(modifier = Modifier.height(10.dp))
                    if (request.status == "Pending") {
                        ReceivedRequestBox(request = request, acceptRequest = {
                            onEvent(ReceivedRequestEvent.AcceptRequest(request._id))
                        }, rejectRequest = {
                            onEvent(ReceivedRequestEvent.RejectRequest(request._id))
                        },
                            onClick = {
                                navController.navigate(Route.User.passUserId(request.sender._id))
                            })
                    } else {
                        RequestBox(request = request, user = request.sender, onClick = {
                            navController.navigate(Route.User.passUserId(request.sender._id))
                        })
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceivedRequestBox(
    acceptRequest: () -> Unit,
    rejectRequest: () -> Unit,
    request: FriendRequest,
    onClick: () -> Unit
) {

    var acceptDialog by remember {
        mutableStateOf(false)
    }

    var rejectDialog by remember {
        mutableStateOf(false)
    }


    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 16.dp,
        ),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
            containerColor = Color(0XFF1F1F1F)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .padding(top = 10.dp, bottom = 0.dp, start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (request.sender.userProfile == null) {
                    Image(
                        painter = painterResource(R.drawable.default_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .clickable {
                                onClick()
                            }
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(request.sender.userProfile)
                            .placeholder(R.drawable.default_profile)
                            .error(R.drawable.default_profile)
                            .transformations(CircleCropTransformation())
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(70.dp)
                            .clickable {
                                onClick()
                            }
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = request.sender.username,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable {
                            onClick()
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = request.sender.about,
                        style = MaterialTheme.typography.titleSmall,
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {


                TextButton(
                    onClick = {
                        rejectDialog = true
                    },
                    colors = ButtonDefaults.buttonColors().copy(
                        contentColor = Color.Red,
                        containerColor = Color.Red.copy(alpha = 0.2f),
                        disabledContentColor = Color.Red,
                        disabledContainerColor = Color.Red.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(0.45f)
                ) {
                    Text(
                        text = "REJECT"
                    )
                }
                Spacer(Modifier.weight(0.05f))
                TextButton(
                    onClick = {
                        acceptDialog = true
                    },
                    colors = ButtonDefaults.buttonColors().copy(
                        contentColor = Color.Green,
                        containerColor = Color.Green.copy(alpha = 0.2f),
                        disabledContentColor = Color.Green,
                        disabledContainerColor = Color.Green.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(0.45f)

                ) {
                    Text(
                        text = "ACCEPT"
                    )
                }
            }

        }
    }

    if (acceptDialog) {
        BasicAlertDialog(
            onDismissRequest = {
                acceptDialog = false
            },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
                usePlatformDefaultWidth = true
            ),

            ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Gray),
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Accept Request",
                        color = Color.Green,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Are you sure do you want to Accept the Request?"
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                acceptDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Blue.copy(alpha = 0.1f),
                                contentColor = Blue
                            )
                        ) {
                            Text("Cancel", fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                acceptRequest()
                                acceptDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Color.Green,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Accept", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }

    if (rejectDialog) {
        BasicAlertDialog(
            onDismissRequest = {
                rejectDialog = false
            },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
                usePlatformDefaultWidth = true
            ),

            ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Gray),
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Reject Request",
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Are you sure do you want to Reject the Request?"
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                rejectDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Blue.copy(alpha = 0.1f),
                                contentColor = Blue
                            )
                        ) {
                            Text("Cancel", fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                rejectRequest()
                                rejectDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Reject", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}



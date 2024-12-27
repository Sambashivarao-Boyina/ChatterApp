package com.example.chatterapp.presentation.chatter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.User
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.Gray
import com.example.chatterapp.ui.theme.Purple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRequestCard(
    user: User,
    sendRequest:()->Unit,
    onClick:()->Unit
) {
    var sendRequestDialog by remember {
        mutableStateOf(false)
    }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation =  8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F1F),
        ),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (user.userProfile == null) {
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
                        .data(user.userProfile)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .transformations(CircleCropTransformation())
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                        .clickable { onClick() }
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.username,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { onClick() }
                )

                Text(
                    text = user.about,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }



            Spacer(modifier = Modifier.width(20.dp))

            IconButton(
                onClick = {
                    sendRequestDialog = true
                },
                modifier = Modifier.size(70.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_user),
                    tint = Blue,
                    contentDescription = "Add User",
                    modifier = Modifier.size(40.dp)
                )
            }


        }
    }



    if (sendRequestDialog) {
        BasicAlertDialog(
            onDismissRequest = {
                sendRequestDialog = false
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
                        text = "Send Request",
                        color = Blue,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Do you want to send request to this person?"
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                sendRequestDialog = false
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
                                sendRequest()
                                sendRequestDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Blue,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Send", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }

}


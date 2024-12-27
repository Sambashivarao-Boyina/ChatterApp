package com.example.chatterapp.presentation.chatter.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.User
import com.example.chatterapp.presentation.chatter.components.RequestBox
import com.example.chatterapp.presentation.chatter.components.TopBar
import com.example.chatterapp.presentation.chatter.received_requests.ReceivedRequestBox
import com.example.chatterapp.presentation.chatter.received_requests.ReceivedRequestEvent
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.Gray

@Composable
fun UserScreen(
    user: User?,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "User Details",
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
                modifier = Modifier.fillMaxSize()
                    .padding(10.dp)
            ) {

                if(user == null) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column {
                                if (user.userProfile == null) {
                                    Image(
                                        painter = painterResource(R.drawable.default_profile),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(400.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(user.userProfile)
                                            .placeholder(R.drawable.default_profile)
                                            .error(R.drawable.default_profile)
                                            .build(),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxWidth()
                                            .height(400.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.height(40.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                        tint = Gray
                                    )
                                    Spacer(Modifier.width(20.dp))
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "UserName",
                                            color = Gray
                                        )
                                        Text(
                                            text = user.username,
                                            style = MaterialTheme.typography.displaySmall,
                                            color = Blue,
                                            fontWeight = FontWeight.Bold
                                        )

                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                        tint = Gray
                                    )
                                    Spacer(Modifier.width(20.dp))
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "About",
                                            color = Gray
                                        )
                                        Text(
                                            text = user.about,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                    }


                                }


                            }

                        }


                    }
                }
            }
        }
    }



}
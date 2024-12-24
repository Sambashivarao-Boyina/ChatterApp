package com.example.chatterapp.presentation.chatter.userProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.UserDetails
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.Gray
import com.example.chatterapp.ui.theme.Purple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userDetails: UserDetails?,
    isLoading: Boolean,
    refershData: () -> Unit,
    onEvent:(UserProfileEvent) -> Unit,
    updateAboutValue: String,
    navController : NavHostController
) {
    val pullToRefreshState = rememberPullToRefreshState()
    var logoutDialog by remember {
        mutableStateOf(false)
    }

    var sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        if (userDetails != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column {
                            if (userDetails.userProfile == null) {
                                Image(
                                    painter = painterResource(R.drawable.default_profile),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(userDetails.userProfile)
                                        .placeholder(R.drawable.default_profile)
                                        .error(R.drawable.default_profile)
                                        .transformations(CircleCropTransformation())
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            IconButton(
                                onClick = {

                                },
                                colors = IconButtonColors(
                                    containerColor = Blue,
                                    contentColor = Color.White,
                                    disabledContentColor = Color.White,
                                    disabledContainerColor = Blue
                                ),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(painter = painterResource(R.drawable.outline_camera_alt_24), contentDescription = null)
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
                                        text = userDetails.username,
                                        style = MaterialTheme.typography.displaySmall,
                                        color = Blue,
                                        fontWeight = FontWeight.Bold
                                    )

                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(40.dp))
                                Spacer(Modifier.width(20.dp))
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "The username cannot be edited it is created when you created your account",
                                        color = Gray,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    HorizontalDivider()
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
                                        text = userDetails.about,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                }
                                Spacer(Modifier.width(20.dp))
                                IconButton(
                                    onClick = {
                                        showBottomSheet = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                        tint = Blue
                                    )
                                }


                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = {
                                        navController.navigate(Route.SendedRequest.route)
                                    },
                                    modifier = Modifier.weight(0.45f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors().copy(
                                        containerColor = Gray.copy(0.2f)
                                    )
                                ) {
                                    Text(
                                        text = "Sended Requests".uppercase(),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Blue
                                    )
                                }
                                Spacer(modifier = Modifier.weight(0.05f))
                                TextButton(
                                    onClick = {
                                        navController.navigate(Route.ReceivedRequest.route)
                                    },
                                    modifier = Modifier.weight(0.45f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors().copy(
                                        containerColor = Gray.copy(0.2f)
                                    )
                                ) {
                                    Text(
                                        text = "Received Requests".uppercase(),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Blue
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            TextButton(
                                onClick = {
                                    logoutDialog = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = Color.Red.copy(0.05f),
                                    contentColor = Color.Red
                                )
                            ) {
                                Text(
                                    text = "Log Out".uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    painter = painterResource(R.drawable.logout),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                    }


                }
            }
        }
    }

    if(showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Surface(
                modifier = Modifier.wrapContentWidth().wrapContentHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.Transparent),
            ) {
                Column(
                    modifier = Modifier
                        .background(color = Color.Transparent)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Describe the About",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = updateAboutValue,
                        onValueChange = {
                            onEvent(UserProfileEvent.UpdateAboutValue(it))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                            )
                        },
                        trailingIcon = {
                            Text(
                                text = "${100 - updateAboutValue.length}"
                            )
                        },
                        placeholder = {
                            Text(text = "Enter the about")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors().copy(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Blue,
                            unfocusedIndicatorColor = Blue,
                            unfocusedTrailingIconColor = Gray,
                            focusedTrailingIconColor = Blue,
                            cursorColor = Blue,
                            focusedLeadingIconColor = Blue,
                            unfocusedLeadingIconColor = Color.Gray
                        ),
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                               showBottomSheet = false
                            },
                        ) {
                            Text(text = "Cancel", color = Blue)
                        }
                        TextButton(
                            onClick = {
                                onEvent(UserProfileEvent.UpdateAbout)
                                showBottomSheet = false
                            },
                        ) {
                            Text(text = "Save", color = Blue)
                        }
                    }
                }
            }
        }
    }

    if(logoutDialog) {
        BasicAlertDialog(
            onDismissRequest = {
                logoutDialog = false
            },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
                usePlatformDefaultWidth = true
            ),

        ) {
            Surface(
                modifier = Modifier.wrapContentWidth().wrapContentHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Gray),
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        text = "LogOut",
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Are you sure do you want to logout?"
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                logoutDialog = false
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
                                onEvent(UserProfileEvent.LogOutUser)
                                logoutDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Logout", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }


    }


}
package com.example.chatterapp.presentation.chatter.userProfile

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.UserDetails
import com.example.chatterapp.presentation.authetication.components.PasswordInput
import com.example.chatterapp.presentation.chatter.components.TopBar
import com.example.chatterapp.presentation.chatter.components.UploadingIndicator
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.Gray
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userDetails: UserDetails?,
    isRefreshing: Boolean,
    refershData: () -> Unit,
    onEvent:(UserProfileEvent) -> Unit,
    updateAboutValue: String,
    updateUserNameValue: String,
    navController : NavHostController,
    uploadImage:(File)->Unit,
    isUploading: Boolean,
    updatePasswordValue: String,
    updatePasswordError: Boolean
) {

    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    //laucner to get image
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val file = getFileFromUri(context, uri)
                uploadImage(file)
            }
        }
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
            if (isGranted) {
                launcher.launch("image/*")
            } else {
                // You can show a message to the user here explaining why permission is needed.
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )



    Scaffold(
        topBar = {
            TopBar(
                title = "Profile"
            )
        },
        containerColor = Black
    ) {

        var logoutDialog by remember {
            mutableStateOf(false)
        }

        var passwordDialog by remember{
            mutableStateOf(false)
        }

        var aboutSheet = rememberModalBottomSheetState()
        val userNameSheet = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        var showAboutSheet by remember {
            mutableStateOf(false)
        }

        var showUserNameSheet by remember {
            mutableStateOf(false)
        }


        val pullToRefreshState = rememberPullToRefreshState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
                .padding(top = it.calculateTopPadding())

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
                                            .size(200.dp)
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
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                            // For Android 11 (API 30) and above, no need for storage permission
                                            // Just launch the file picker directly
                                            launcher.launch("image/*")
                                        } else {
                                            // For Android versions below 11, request storage permission
                                            if (permissionGranted) {
                                                launcher.launch("image/*")
                                            } else {
                                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                            }
                                        }

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
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = Blue,
                                            fontWeight = FontWeight.Bold
                                        )

                                    }

                                    Spacer(Modifier.width(20.dp))
                                    IconButton(
                                        onClick = {
                                            showUserNameSheet = true
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
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                    }
                                    Spacer(Modifier.width(20.dp))
                                    IconButton(
                                        onClick = {
                                            showAboutSheet = true
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
                                        passwordDialog = true
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors().copy(
                                        containerColor = Blue.copy(0.06f),
                                        contentColor = Blue
                                    )
                                ) {
                                    Text(
                                        text = "Change Password",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                    )

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
                                        text = "LOG OUT",
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
            }else {
                // Ensure a scrollable container is present when userDetails is null
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text(
                            text = "No user details available.",
                            color = Gray,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Pull to refresh to try again.",
                            color = Blue,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }



            if(pullToRefreshState.isRefreshing) {
                LaunchedEffect(true) {
                    refershData()
                }
            }

            LaunchedEffect(isRefreshing) {
                if(isRefreshing) {
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

        if(showAboutSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAboutSheet = false
                },
                sheetState = aboutSheet
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
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
                                    showAboutSheet = false
                                },
                            ) {
                                Text(text = "Cancel", color = Blue)
                            }
                            TextButton(
                                onClick = {
                                    onEvent(UserProfileEvent.UpdateAbout)
                                    showAboutSheet = false
                                },
                            ) {
                                Text(text = "Save", color = Blue)
                            }
                        }
                    }
                }
            }
        }

        if(showUserNameSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showUserNameSheet = false
                },
                sheetState = userNameSheet
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = Color.Transparent),
                ) {
                    Column(
                        modifier = Modifier
                            .background(color = Color.Transparent)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Change UserName",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            value = updateUserNameValue,
                            onValueChange = {
                                onEvent(UserProfileEvent.UpdateUserNameValue(it))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                )
                            },
                            placeholder = {
                                Text(text = "Enter the Username")
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
                                    showUserNameSheet = false
                                },
                            ) {
                                Text(text = "Cancel", color = Blue)
                            }
                            TextButton(
                                onClick = {
                                    onEvent(UserProfileEvent.UpdateUserName)
                                    showUserNameSheet = false
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

        if(passwordDialog) {
            BasicAlertDialog(
                onDismissRequest = {
                    passwordDialog = false
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
                            text = "Change Password",
                            color = Blue,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(20.dp))

                        PasswordInput(
                            value = updatePasswordValue,
                            placeholder = "Password",
                            onChange = {
                                onEvent(UserProfileEvent.UpdatePasswordValue(it))
                            },
                            isError = updatePasswordError
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "The password length should be at least 8 , it should be the combination of uppercase, lowercase, digits and sepcial characters.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Gray
                        )

                        Spacer(Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    passwordDialog = false
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
                                    onEvent(UserProfileEvent.UpdatePassword)
                                    passwordDialog = false

                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = Blue,
                                    contentColor = Color.White
                                ),
                                enabled = !updatePasswordError
                            ) {
                                Text("Change", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        if(isUploading) {
            UploadingIndicator()
        }
    }





}

fun getFileFromUri(context:Context,uri: Uri):File {
    val contextResolver = context.contentResolver
    val inputStream = contextResolver.openInputStream(uri)
    val tempFile =  File.createTempFile("${System.currentTimeMillis()}", ".jpg", context.cacheDir)
    tempFile.outputStream().use { outputStream ->
        inputStream?.copyTo(outputStream)
    }

    return tempFile
}

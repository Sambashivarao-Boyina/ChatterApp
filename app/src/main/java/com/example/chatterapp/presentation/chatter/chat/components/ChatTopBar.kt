package com.example.chatterapp.presentation.chatter.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.chatterapp.domain.model.Friend
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.DarkGray
import com.example.chatterapp.ui.theme.Gray
import com.example.chatterapp.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    friend: Friend,
    onBackClick:()->Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackClick()
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(30.dp))
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    bottom = 10.dp
                )
            ) {
                if(friend.person.userProfile == null) {
                    Image(
                        painter = painterResource(R.drawable.default_profile),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
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
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                Text(text = friend.person.username,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold)
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
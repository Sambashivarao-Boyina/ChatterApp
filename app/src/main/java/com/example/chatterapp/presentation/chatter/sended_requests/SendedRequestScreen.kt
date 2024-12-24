package com.example.chatterapp.presentation.chatter.sended_requests

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.FriendRequest
import com.example.chatterapp.domain.model.User
import com.example.chatterapp.presentation.chatter.components.RequestBox
import com.example.chatterapp.presentation.chatter.components.TitleBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendedRequestScreen(
    navController: NavHostController,
    requests: List<FriendRequest>
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TitleBar(navController = navController, "Sended Requests")


        LazyColumn(
            modifier = Modifier.padding(horizontal = 15.dp)
        ){
            items(requests) { request ->
                Spacer(modifier = Modifier.height(10.dp))
                RequestBox(request = request, user = request.receiver)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

    }
}



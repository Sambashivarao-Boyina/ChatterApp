package com.example.chatterapp.presentation.chatter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.Friend

@Composable
fun FriendCard(
    friend: Friend
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = Color(0xFF1F1F1F)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(friend.person.userProfile == null) {
            Image(
                painter = painterResource(R.drawable.default_profile),
                contentDescription = null,
                modifier = Modifier.size(70.dp)
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
                modifier = Modifier.size(70.dp)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text = friend.person.username,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}